package com.react.gabriel.wbam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    public void sendMsgToWhite(View view){
        padocManager.sendMsgToWhite("WHITE hello white");
    }

    public void sendMsgToBlue(View view){
        padocManager.sendMsgToBlue("BLUE hello blue");
    }

    public void sendMsgToGalaxy(View view){
        padocManager.sendMsgToGalaxy("GALAX hello galax");
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
