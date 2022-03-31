package com.csi.technodrugs.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.R;
import com.csi.technodrugs.Utility.Constants;
import com.csi.technodrugs.Utility.CustomToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class ActivityLogin extends AppCompatActivity {
    Button buttonLogin;
    EditText editTextEmail,editTextPassword;
    LinearLayout linearLayoutRedBox;
    String getEmail,getPassword,errorMessage,status,mpoCode,userId,companyName,userName;
    Animation shakeAnimation,leftAnimation;
    Context context = this;
    JSONObject jsonObject;
    JSONArray jsonArrayMpo;
    SharedPreferences sharedPreferencesUser;
    ACProgressPie acProgressPie;
    ImageView imageViewLogo;
    Spinner spinnerMpo;
    private ArrayList<String> mpoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // progress dialog
        acProgressPie = new ACProgressPie.Builder(ActivityLogin.this)
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initUI();
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
        leftAnimation = AnimationUtils.loadAnimation(context, R.anim.fade);
        imageViewLogo.setAnimation(leftAnimation);
        sharedPreferencesUser = getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser,MODE_PRIVATE);
        if(sharedPreferencesUser.contains(Constants.SharedprefItem.MPO_CODE)) {
            startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            finish();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getEmail = editTextEmail.getText().toString();
                 getPassword = editTextPassword.getText().toString();
                 //startActivity(new Intent(ActivityLogin.this,MainActivity.class));
                if (getEmail.equals("") || getEmail.length() == 0
                        || getPassword.equals("") || getPassword.length() == 0) {
                    new CustomToast().Show_Toast(context, view,
                            "Enter both credentials.");

                }
                else {
                    //startActivity(new Intent(ActivityLogin.this,MainActivity.class));
                    sendData();
                }
            }
        });
    }

    private void sendData() {
        acProgressPie.show();
        RequestQueue queue = Volley.newRequestQueue(ActivityLogin.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseText) {
                try {
                    jsonObject = new JSONObject(responseText);
                    status = jsonObject.getString("message");
                    //mpoCode = jsonObject.getString("mpo_code");
                    userId = jsonObject.getString("user_id");
                    companyName = jsonObject.getString("company_name");
                    userName = jsonObject.getString("user_name");
                    //Intent intent= new Intent(ActivityLogin.this,MainActivity.class);
                    //finish();
                    //startActivity(intent);
                    dialogMpo();
                    acProgressPie.dismiss();
                } catch (JSONException e) {
                    Log.d("hi2","hi");
                    e.printStackTrace();
                    Toast.makeText(ActivityLogin.this, status, Toast.LENGTH_SHORT).show();
                    acProgressPie.dismiss();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("hi3",error.toString());
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
               /* flag = 1;
                loadingDialog.dismiss();
                showErrorDialog();*/
               acProgressPie.dismiss();
            }
        })

        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_name", getEmail);
                map.put("password",getPassword);
                Log.d("hi",map.toString());
                return map;
            }
        };
        queue.add(stringRequest);
    }

    private void dialogMpo() {
        final Dialog dialog = new Dialog(ActivityLogin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_mpo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);

        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        spinnerMpo = (Spinner) dialog.findViewById(R.id.spinnerMpo);
        loadMpo();
        spinnerMpo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mpoCode = getMpoCode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("code",mpoCode);
                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                editor.putString(Constants.SharedprefItem.MPO_CODE,mpoCode);
                editor.putString(Constants.SharedprefItem.USER_ID,userId);
                editor.putString(Constants.SharedprefItem.COMPANY_NAME,companyName);
                editor.putString(Constants.SharedprefItem.USER_NAME,userName);
                editor.commit();
                Intent intent= new Intent(ActivityLogin.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                handler.removeCallbacks(runnable);
            }
        });

        dialog.show();
    }

    private void loadMpo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.MPO_LIST+"user_id="+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArrayMpo = jsonObject.getJSONArray("list");
                            getMpo(jsonArrayMpo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void getMpo(JSONArray jsonArrayMpo) {
                        for(int i = 0; i<jsonArrayMpo.length(); i++) {
                            try {
                                JSONObject json = jsonArrayMpo.getJSONObject(i);
                                mpoList.add(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spinnerMpo.setAdapter(new ArrayAdapter<String>(ActivityLogin.this, R.layout.spinner_item, mpoList));
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
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityLogin.this);
        requestQueue.add(stringRequest);
    }
    public String getMpoCode(int position){
        String code="";
        try {
            JSONObject json = jsonArrayMpo.getJSONObject(position);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void initUI() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextUserEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextUserpassword);
        linearLayoutRedBox = (LinearLayout) findViewById(R.id.linearRedBox);
        imageViewLogo = (ImageView) findViewById(R.id.logo);
    }
}
