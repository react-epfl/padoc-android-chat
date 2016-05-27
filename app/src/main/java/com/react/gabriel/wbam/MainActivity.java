package com.react.gabriel.wbam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.react.gabriel.wbam.padoc.PadocManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //DEBUG
    private boolean DBG = true;
    private static final String TAG = "WBAM";
    Date date = null;
    SimpleDateFormat dateFormat = null;
    private TextView console = null;

    private PadocManager padocManager;

    private Context context;


    AlertDialog peerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = getApplicationContext();

        //DEBUG
        date = new Date();
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        console = (TextView)findViewById(R.id.console);
        console.setMovementMethod(new ScrollingMovementMethod());

        //PADOC
        padocManager = new PadocManager(this);

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


    public void showPeers(View view){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Hardik");
        arrayAdapter.add("Archit");
        arrayAdapter.add("Jignesh");
        arrayAdapter.add("Umang");
        arrayAdapter.add("Gatti");

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.create();
        builderSingle.show();

    }

    public void showPeers2(View view){

        final String peers[] = padocManager.getPeers();

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
                String selectedAddress = lv.getItemAtPosition(position).toString();

                padocManager.sendMsg(selectedAddress);
                peerDialog.dismiss();

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, peers);
        lv.setAdapter(adapter);
        peerDialog.show();

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void startBTDiscovery(View view){
        padocManager.startBluetoothDiscovery();
    }

    public void stopBTDiscovery(View view){
        padocManager.stopBluetoothDiscovery();
    }

    public void makeBTVisible(View view){
        padocManager.setBluetoothVisible();
    }

    public void makeBTInvisible(View view){
        padocManager.setBluetoothInvisible();
    }

    public void startBTServer(View view){
        padocManager.startBluetoothServer();
    }

    public void unpairDevices(View view) {
        padocManager.unpairBluetoothDevices();
    }

    public void sendCBS(View view){
        padocManager.sendCBS();
    }

    public void startWDDiscovery(View view){
        padocManager.startWifiDirectDiscovery();
    }

    public void stopWDDiscovery(View view){
        padocManager.stopWifiDirectDiscovery();
    }

    public void makeWDVisible(View view){
        padocManager.startWifiDirectService();
    }

    public void makeWDInvisible(View view){
        padocManager.stopWifiDirectService();
    }

    //DEBUG
    public void debugPrint(final String msg){

        if(DBG) {
            //Log on Android Monitor
            Log.d(MainActivity.TAG, msg);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log on the console output of the app
                    console.append(dateFormat.format(new Date()) + " " + msg + "\n");
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
}
