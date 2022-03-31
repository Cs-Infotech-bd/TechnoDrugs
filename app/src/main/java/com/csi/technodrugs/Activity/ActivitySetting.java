package com.csi.technodrugs.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.R;
import com.csi.technodrugs.Utility.Constants;
import com.csi.technodrugs.Utility.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class ActivitySetting extends AppCompatActivity {
    Toolbar toolbar;
    EditText editTextOldPassword,editTextNewPassword,editTextConfirmPassword;
    Button buttonSubmit;
    String getCurrentPassword,getNewPassword,getConfirmPassword;
    Context context = this;
    JSONObject jsonObject;
    String errorMessage,mpoCode,userId;
    SharedPreferences sharedPreferencesUser;
    ACProgressPie acProgressPie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferencesUser = ActivitySetting.this.getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, Context.MODE_PRIVATE);
        mpoCode = sharedPreferencesUser.getString(Constants.SharedprefItem.MPO_CODE,"");
        userId = sharedPreferencesUser.getString(Constants.SharedprefItem.USER_ID,"");
        acProgressPie = new ACProgressPie.Builder(ActivitySetting.this)
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initToolBar();
        initUI();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentPassword = editTextOldPassword.getText().toString();
                getNewPassword = editTextNewPassword.getText().toString();
                getConfirmPassword = editTextConfirmPassword.getText().toString();
                if (getCurrentPassword.equals("") || getCurrentPassword.length() == 0
                        || getNewPassword.equals("") || getNewPassword.length() == 0
                        || getConfirmPassword.equals("") || getConfirmPassword.length() == 0){
                    new CustomToast().Show_Toast(context, view,
                            "Enter both credentials.");
                }
                else if (!getNewPassword.matches(getConfirmPassword)){
                    new CustomToast().Show_Toast(context, view,
                            "Password don't match");
                }
                else {
                    sendData();
                }
            }
        });
    }

    private void sendData() {
        acProgressPie.show();
        RequestQueue queue = Volley.newRequestQueue(ActivitySetting.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseText) {
                try {
                    Log.d("response",responseText);
                    jsonObject = new JSONObject(responseText);
                    String message = jsonObject.getString("message");
                    Toast.makeText(ActivitySetting.this,message, Toast.LENGTH_SHORT).show();
                    logoutUser();
                    acProgressPie.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonError);
                        errorMessage = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                acProgressPie.dismiss();
            }
        })

        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("password", getCurrentPassword);
                map.put("new_password",getNewPassword);
                map.put("user_name",userId);
                map.put("host","Android app");
                Log.d("All Data", map.toString());
                return map;
            }
        };
        queue.add(stringRequest);
    }

    private void logoutUser() {
        sharedPreferencesUser = getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(ActivitySetting.this,ActivityLogin.class));
        finish();
    }

    private void initUI() {
        editTextOldPassword = (EditText)  findViewById(R.id.editTextOldPassword);
        editTextNewPassword = (EditText)  findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = (EditText)  findViewById(R.id.editTextConfirmPassword);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
