package com.react.gabriel.wbam;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.react.gabriel.wbam.padoc.Message;
import com.react.gabriel.wbam.padoc.Messenger;
import com.react.gabriel.wbam.padoc.PadocManager;
import com.react.gabriel.wbam.padoc.TestThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //DEBUG
    private boolean DBG = true;
    private static final String TAG = "WBAM";
    Date date = null;
    SimpleDateFormat dateFormat = null;
    private TextView console = null;

    private PadocManager padocManager;

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

        //PADOC
        padocManager = new PadocManager(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        padocManager.onDestroy();
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

                if(selectedPeer.get("addr").equals("ALL")){
                    padocManager.sendCBS();
                    peerDialog.dismiss();
                }else {
                    padocManager.sendMsg(selectedPeer.get("addr"));
                    peerDialog.dismiss();
                }


            }
        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        Map<String, String> cbsEntry = new HashMap<>();
        cbsEntry.put("name", "CBS (" + padocManager.getMeshUUID() + ")");
        cbsEntry.put("addr", "ALL");
        data.add(cbsEntry);

        for (Map.Entry<String, String> peerItem : padocManager.getPeerNames()) {

            Map<String, String> datum = new HashMap<String, String>(2);
            String addr = peerItem.getKey();

            String nameAndHops = peerItem.getValue() + " ("+ padocManager.getHopsFor(addr) + ")";
            datum.put("name", nameAndHops);
            datum.put("addr", addr);
            data.add(datum);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[] {"name", "addr"}, new int[] {android.R.id.text1, android.R.id.text2});

        lv.setAdapter(simpleAdapter);
        peerDialog.show();

    }

    public void startBTDiscovery(View view){
//        padocManager.startBluetoothDiscovery();
    }

    public void stopBTDiscovery(View view){
//        padocManager.stopBluetoothDiscovery();
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
        padocManager.forceStopWifiDirectService();
    }

    public void initialize(View view){
        padocManager.initialize();
    }

    public void startFLOODTestSlow(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING SLOW FLOOD");
        testThread.startTest(Message.Algo.FLOOD, 2000, null);
    }

    public void startFLOODTestMedium(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING MEDIUM FLOOD");
        testThread.startTest(Message.Algo.FLOOD, 1000, null);
    }

    public void startFLOODTestFast(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING FAST FLOOD");
        testThread.startTest(Message.Algo.FLOOD, 500, null);
    }

    public void startCBSTestSlow(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING SLOW CBS");
        testThread.startTest(Message.Algo.CBS, 2000, null);
    }

    public void startCBSTestMedium(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING SLOW CBS");
        testThread.startTest(Message.Algo.CBS, 1000, null);
    }

    public void startCBSTestFast(View view){
        TestThread testThread = new TestThread(padocManager);
        debugPrint("STARTING SLOW CBS");
        testThread.startTest(Message.Algo.CBS, 500, null);
    }

    public void requestFLOODTestSlow(View view){
        padocManager.requestTest(Message.Type.FLOOD_TEST_REQUEST, 2000);
        debugPrint("Request for SLOW FLOOD SENT");
    }

    public void requestFLOODTestMedium(View view){
        padocManager.requestTest(Message.Type.FLOOD_TEST_REQUEST, 1000);
        debugPrint("Request for MEDIUM FLOOD SENT");
    }

    public void requestFLOODTestFast(View view){
        padocManager.requestTest(Message.Type.FLOOD_TEST_REQUEST, 500);
        debugPrint("Request for FAST FLOOD SENT");
    }

    public void requestCBSTestSlow(View view){
        padocManager.requestTest(Message.Type.CBS_TEST_REQUEST, 2000);
        debugPrint("Request for SLOW CBS SENT");
    }

    public void requestCBSTestMedium(View view){
        padocManager.requestTest(Message.Type.CBS_TEST_REQUEST, 1000);
        debugPrint("Request for MEDIUM CBS SENT");
    }

    public void requestCBSTestFast(View view){
        padocManager.requestTest(Message.Type.CBS_TEST_REQUEST, 500);
        debugPrint("Request for FAST CBS SENT");
    }

    public void requestROUTETestSlow(View view){
        padocManager.requestTest(Message.Type.ROUTE_TEST_REQUEST, 2000);
        debugPrint("Request for SLOW ROUTE SENT");
    }

    public void requestROUTETestMedium(View view){
        padocManager.requestTest(Message.Type.ROUTE_TEST_REQUEST, 1000);
        debugPrint("Request for MEDIUM ROUTE SENT");
    }

    public void requestROUTETestFast(View view){
        padocManager.requestTest(Message.Type.ROUTE_TEST_REQUEST, 500);
        debugPrint("Request for FAST ROUTE SENT");
    }

    public void sendCBS(View view){
        padocManager.sendCBS();
    }

    public void sendFLOOD(View view){
        padocManager.sendFLOOD();
    }

    public void showMessageCount(View view){
        padocManager.showTestResults();
    }

    public void clearMessageCount(View view){
        padocManager.clearMessageCount();
        console.setText("");
        debugPrint("OK");
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
