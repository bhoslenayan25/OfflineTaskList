package com.nayan.task.offlinetasklist.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nayan.task.offlinetasklist.database.DataBaseAdapter;
import com.nayan.task.offlinetasklist.database.TaskList;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nayan on 9/8/17.
 */

public class SyncData extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param SyncData.class.getName() Used to name the worker thread, important only for debugging.
     */

    String API_URL = "http://hungrybelly.000webhostapp.com/app/menu/list";//api end points url
    String API_PARAMETER = "task_name";//api parameter

    DataBaseAdapter dba;

    public SyncData() {
        super(SyncData.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            dba = new DataBaseAdapter(this);
            String data = "";
            dba.open();
            Cursor cursor = TaskList.getData();
            if (cursor.getCount() > 0) {
                for(int i = 0;i<cursor.getCount();i++) {
                    if(i == 0 || i == cursor.getCount() - 1) {
                        data = data + cursor.getString(i);
                    }else {
                        data = data + cursor.getString(i) + ",";
                    }
                }
            }
            cursor.close();
            dba.close();

            if(data != "") {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(API_PARAMETER,data);

                if (pushToServer(getPostDataString(hashMap))) {
                    dba.open();
                    TaskList.clearTable();
                    dba.close();
                }
            }
            this.stopSelf();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private boolean pushToServer(String data){

        Log.w("SyncData",data);

        boolean status = false;
        try {

            String response = "";
            URL url = new URL(API_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //   conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }

                JSONObject jsonObject = new JSONObject(response);

                if(jsonObject.getString("status").equalsIgnoreCase("success")){
                    status = true;
                }else{
                    status = false;
                }

            } else {
                status = false;
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return status;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}
