package com.csi.technodrugs.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.R;
import com.csi.technodrugs.Utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivitySplash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 000;
    Context context = this;
    int verCode,updatedVersion;
    JSONArray jsonArray;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        builder = new AlertDialog.Builder(this);
        getVersion();
        getUpdatedVersion();

    }

    private void getUpdatedVersion() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.APP_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("list");
                            for ( int i = 0; i < jsonArray.length(); i++ ){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String version = jsonObject1.getString("version");
                                updatedVersion = Integer.parseInt(version);
                                if ( verCode != updatedVersion){
                                    getUpdate();
                                }
                                else {
                                    openApp();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void openApp() {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void getUpdate() {
        builder.setTitle("Update!");

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.update)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String appPackage = ActivitySplash.this.getPackageName();
                        String url = "https://play.google.com/store/apps/details?id=" + appPackage;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

}

    private void getVersion() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
