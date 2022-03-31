package com.csi.technodrugs.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.CustomerEntry.CustomerEntryFragment;
import com.csi.technodrugs.CustomerList.CustomerListFragment;
import com.csi.technodrugs.Order.OrderEntryFragment;
import com.csi.technodrugs.OrderStatus.OrderStatusFragment;
import com.csi.technodrugs.R;
import com.csi.technodrugs.SqLite.SqlitieFunction;
import com.csi.technodrugs.Utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SharedPreferences sharedPreferencesUser;
    TextView textViewUserName;
    String userName;
    SqlitieFunction sqlitieFunction;
    Spinner spinnerMpo;
    String mpoCode,userId;
    JSONArray jsonArrayMpo;
    private ArrayList<String> mpoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.orderEntry);
        setSupportActionBar(toolbar);
        sqlitieFunction = new SqlitieFunction(MainActivity.this);
        sharedPreferencesUser = getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, MODE_PRIVATE);
        userName = sharedPreferencesUser.getString(Constants.SharedprefItem.USER_NAME,"");
        userId = sharedPreferencesUser.getString(Constants.SharedprefItem.USER_ID,"");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        textViewUserName = (TextView) header.findViewById(R.id.textViewUserName);
        textViewUserName.setText(userName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new OrderEntryFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,ActivitySetting.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_order_entry) {
            selectedFragment = new OrderEntryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            toolbar.setTitle(R.string.orderEntry);
        } else if (id == R.id.nav_customer_entry) {
            selectedFragment = new CustomerEntryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            toolbar.setTitle(R.string.customerEntry);
        } else if (id == R.id.nav_orderStatus) {
            selectedFragment = new OrderStatusFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            toolbar.setTitle(R.string.orderStatus);
        }
        else if (id == R.id.nav_customer_list) {
            selectedFragment = new CustomerListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            toolbar.setTitle(R.string.customerList);
        }else if (id == R.id.nav_logout) {
            logoutUser();
        } else if (id == R.id.nav_share) {
            /*Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.setAction("https://play.google.com/store/apps/details?id=com.csi.technodrugs");
            startActivity(Intent.createChooser(intent, "Share With"));*/
        }else if (id == R.id.nav_setting) {
            dialogMpo();
        }
        else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,ActivityAbout.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialogMpo() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_mpo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
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
                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                editor.putString(Constants.SharedprefItem.MPO_CODE,mpoCode);
                editor.putString(Constants.SharedprefItem.USER_NAME,userName);
                editor.apply();
                editor.commit();
                dialog.dismiss();
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
        mpoList.clear();
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
                        spinnerMpo.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, mpoList));
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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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

    private void logoutUser() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.a);
        text.setText("Do You Want to Logout?");
        imageView.startAnimation(animation);
        Button buttonNo = (Button) dialog.findViewById(R.id.buttonNo);
        Button buttonYes = (Button) dialog.findViewById(R.id.buttonYes);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesUser = getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this,ActivityLogin.class));
                sqlitieFunction.deleteAll();
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
}
