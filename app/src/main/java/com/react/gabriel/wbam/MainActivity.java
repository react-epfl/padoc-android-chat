package com.react.gabriel.wbam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.react.gabriel.wbam.padoc.Padoc;
import com.react.gabriel.wbam.padoc.PadocInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements PadocInterface {

    Date date = null;
    SimpleDateFormat dateFormat = null;
    private TextView console = null;

    private Padoc padoc;

    AlertDialog peerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DEBUG
        date = new Date();
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        console = (TextView)findViewById(R.id.console);
        console.setMovementMethod(new ScrollingMovementMethod());

        padoc = new Padoc(this, getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view){

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select a Peer");

        peerDialog = alertDialog.create();


        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> selectedPeer = (HashMap) lv.getItemAtPosition(position);

                if(selectedPeer.get("addr").equals("FLOOD")){

                    sendFLOOD();
                    peerDialog.dismiss();
                }else if(selectedPeer.get("addr").equals("CBS")){
                    sendCBS();
                    peerDialog.dismiss();
                }else {
                    MainActivity.this.sendMessage(selectedPeer.get("addr"));
                    peerDialog.dismiss();
                }
            }
        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        Map<String, String> floodEntry = new HashMap<>();
        floodEntry.put("name", "FLOOD");
        floodEntry.put("addr", "FLOOD");
        data.add(floodEntry);
        Map<String, String> cbsEntry = new HashMap<>();
        cbsEntry.put("name", "CBS");
        cbsEntry.put("addr", "CBS");
        data.add(cbsEntry);

        Set peers = padoc.getPeers();

        if(peers != null){

            for (Map.Entry<String, String> peerItem : padoc.getPeers()) {

                Map<String, String> datum = new HashMap<String, String>(2);
                String addr = peerItem.getKey();

                String nameAndHops = peerItem.getValue() + " ("+ padoc.getHopsFor(addr) + ")";
                datum.put("name", nameAndHops);
                datum.put("addr", addr);
                data.add(datum);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                    new String[] {"name", "addr"}, new int[] {android.R.id.text1, android.R.id.text2});

            lv.setAdapter(simpleAdapter);
            peerDialog.show();
        }else {
            Toast.makeText(getApplicationContext(), "There is no one to chat with : (", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFLOOD(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                padoc.sendFLOOD(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void sendCBS(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                padoc.sendCBS(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void sendMessage(final String destination){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                padoc.sendMessage(destination, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    @Override
    public void print(final Spanned spannedHtml, final boolean time){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log on the console output of the app
                if(time){
                    console.append(TextUtils.concat(TextUtils.concat(Html.fromHtml("<i>" + dateFormat.format(new Date())+ ": </i>"), spannedHtml), "\n\n"));
                }else{
                   console.append(TextUtils.concat(spannedHtml, Html.fromHtml("\n\n")));
                }

                console.post(new Runnable() {
                    public void run() {
                        Layout l = console.getLayout();
                        if (l == null)
                            return;
                        final int scrollAmount = l.getLineTop(console.getLineCount()) - console.getHeight();
                        if (scrollAmount > 0)
                            console.scrollTo(0, scrollAmount);
                        else
                            console.scrollTo(0, 0);
                    }
                });
            }
        });
    }
}
