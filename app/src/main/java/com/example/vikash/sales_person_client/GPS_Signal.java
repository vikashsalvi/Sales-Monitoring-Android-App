package com.example.vikash.sales_person_client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vikash on 1/28/2017.
 */

public class GPS_Signal extends Service {
    LocationListener locationListener;
    LocationManager locationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission,ResourceType
            //locationManager.removeUpdates(locationListener);
            Intent in = new Intent();
            in.setAction("YouWillNeverKillMe");
            sendBroadcast(in);
            //sendBroadcast(new Intent("YouWillNeverKillMe"));
        }
    }
    /*   Thread t = new Thread(){
           public void run(){


           }
       };
   */
    String mssg;
    void set_da(String masg)
    {
        mssg = masg;
    }
    String get_da()
    {
        return mssg ;
    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),mssg, Toast.LENGTH_LONG).show();
        }
    };
    @Override
    public void onCreate() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        Message myMessage=new Message();
                        Bundle resBundle = new Bundle();
                        resBundle.putString("status", "SUCCESS");
                        myMessage.obj=resBundle;
                        String msg = "Location "+location.getLongitude()+" "+location.getLatitude()+"";
                        set_da(msg);
                /*asy_task asy_task = new asy_task();
                asy_task.execute("");*/
                        handler.sendMessage(myMessage);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                };
            }
            }, 0, 5000);//put here time 1000 milliseconds=1 second


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission,ResourceType
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,locationListener);
    }

    public class asy_task extends AsyncTask<String, Void, String> {
        StringBuffer chaine = new StringBuffer("");
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            InputStream is = null;
            String result = "";
            BufferedReader bufferedReader = null;
            Uri.Builder uri = new Uri.Builder();
            uri.scheme("http");
            uri.authority("www.swasth.esy.es");
            uri.appendPath("gps_app");
            uri.appendPath("op.php");
            uri.appendQueryParameter("input",mssg);
            String surl = uri.build().toString();
            try{
                URL url = new URL(surl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while((json = bufferedReader.readLine())!= null){
                    sb.append(json+"\n");
                }
                return sb.toString().trim();
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Message myMessage=new Message();
            Bundle resBundle = new Bundle();
            resBundle.putString("status", "SUCCESS");
            myMessage.obj=resBundle;
            set_da(result);
            handler.sendMessage(myMessage);
        }

    }
}
