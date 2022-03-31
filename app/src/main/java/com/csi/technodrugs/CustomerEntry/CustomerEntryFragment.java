package com.csi.technodrugs.CustomerEntry;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.DefaultRetryPolicy;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

import static android.app.Activity.RESULT_OK;

public class CustomerEntryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    JSONArray jsonArrayCustomerType,jsonArrayTerritory,jsonArrayMarket;
    private ArrayList<String> customerTypeList;
    private ArrayList<String> territoryList;
    private ArrayList<String> marketList;
    String stringCustomerCode,stringCustomerName,stringCareOf,stringMarketCode,stringTerritoryGroup,stringTerritoryCode,stringHouseName,stringHoldingNo,stringRoadNo,stringRoadName,stringBlockNo,stringSector;
    String message,errorMessage,mpoCode,userId,image = "";
    int territoryPosition,customerTypePosition,marketPosition;
    Spinner spinnerCustomerType,spinnerTeritory,spinnerMarket;
    Button buttonSubmit,buttonChoosePhoto;
    EditText editTextCustomerName,editTextCareOf,editTextHouseName,editTextHoldingNo,editTextRoadNo,editTextRoadName,editTextBlockNno,editTextSector;
    ImageView imageViewUpload;
    LinearLayout linearLayoutForm;
    Animation animation,animationShake;
    ACProgressPie dialog;
    SharedPreferences sharedPreferencesUser;
    private int RESULT_LOAD_IMAGE = 1,REQUEST_CAMERA = 0;
    public CustomerEntryFragment() {
    }
    public static CustomerEntryFragment newInstance(String param1, String param2) {
        CustomerEntryFragment fragment = new CustomerEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_entry, container, false);
        customerTypeList = new ArrayList<>();
        territoryList = new ArrayList<>();
        marketList = new ArrayList<>();
        animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.left_enter);
        animationShake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake);
        sharedPreferencesUser = this.getActivity().getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, Context.MODE_PRIVATE);
        mpoCode = sharedPreferencesUser.getString(Constants.SharedprefItem.MPO_CODE,"");
        userId = sharedPreferencesUser.getString(Constants.SharedprefItem.USER_ID,"");
        // progress dialog
        dialog = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initUI(view);
        linearLayoutForm.setAnimation(animation);
        loadCustomerType(view);
        loadTeritory(view);
        //customer type select
        spinnerCustomerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stringCustomerCode = getCustomerCode(position);
                customerTypePosition = spinnerCustomerType.getSelectedItemPosition();
                Log.d("Code", stringCustomerCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //market select
        spinnerTeritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                territoryPosition = spinnerTeritory.getSelectedItemPosition();
                stringTerritoryCode = getTerritoryCode(position);
                stringTerritoryGroup = getTerritoryGroup(position);
                if ( territoryPosition == 1){
                    loadMarket(view);
                }
                else {
                    marketList.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //market select
        spinnerMarket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                marketPosition = spinnerMarket.getSelectedItemPosition();
                stringMarketCode = getMarketCode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringCustomerName = editTextCustomerName.getText().toString();
                stringCareOf = editTextCareOf.getText().toString();
                stringHouseName = editTextHouseName.getText().toString();
                stringHoldingNo = editTextHoldingNo.getText().toString();
                stringRoadNo = editTextRoadNo.getText().toString();
                stringRoadName = editTextRoadName.getText().toString();
                stringBlockNo = editTextBlockNno.getText().toString();
                stringSector = editTextSector.getText().toString();
                if (stringCustomerName.isEmpty()){
                    editTextCustomerName.setError("Enter Customer Name");
                    editTextCustomerName.requestFocus();
                    linearLayoutForm.setAnimation(animationShake);
                }
                else if (stringCareOf.isEmpty()){
                    editTextCareOf.setError("Enter Care Of");
                    editTextCareOf.requestFocus();
                    linearLayoutForm.setAnimation(animationShake);
                }
                /*else if ( image.length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"Choose Image",Toast.LENGTH_SHORT).show();
                }*/
                else if (customerTypePosition == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"Select Customer Type",Toast.LENGTH_SHORT).show();
                    linearLayoutForm.setAnimation(animationShake);
                }
                else if (territoryPosition == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"Select Territory",Toast.LENGTH_SHORT).show();
                    linearLayoutForm.setAnimation(animationShake);
                }
                else if (marketPosition == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"Select Market",Toast.LENGTH_SHORT).show();
                    linearLayoutForm.setAnimation(animationShake);
                }
                else{
                   sendData(view);
                }
            }
        });
        return view;
    }

    private void selectImage(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[which].equals("Choose from Library")) {
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void sendData(final View view) {
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.CUSTOMER_ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.matches("1")) {
                        message = jsonObject.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                message, Toast.LENGTH_SHORT).show();
                        resetAll(view);
                    } else {
                        errorMessage = jsonObject.getString("message");
                        /*Toast.makeText(getActivity().getApplicationContext(),
                                errorMessage, Toast.LENGTH_SHORT).show();*/
                        showErrorDialog();
                    }
                }catch (Exception e){
                    Log.d("Error", e.toString());
                    Log.d("Error2", response.toString());
                    showErrorDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_name", stringCustomerName);
                params.put("co", stringCareOf);
                params.put("mpo_code",mpoCode);
                params.put("mpo_group", stringTerritoryGroup);
                params.put("customer_type", stringCustomerCode);
                params.put("teritory_code", stringTerritoryCode);
                params.put("market_code", stringMarketCode);
                params.put("house_name", stringHouseName);
                params.put("holding_no", stringHoldingNo);
                params.put("road_no", stringRoadNo);
                params.put("road_name", stringRoadName);
                params.put("block_no", stringBlockNo);
                params.put("sector", stringSector);
                params.put("image", image);
                params.put("user_id", userId);
                Log.d("customer Reg", params.toString());
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    //image choose for profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 50, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageViewUpload.setImageBitmap(bmp);
            image = imageToString(bmp);
            Log.d("IMAGE",image);
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageViewUpload.setImageBitmap(bmp);
            image = imageToString(bmp);
            Log.d("image",image);
        }
    }

    private String imageToString(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes, Base64.DEFAULT);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void showErrorDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.a);
        text.setText(errorMessage);
        imageView.startAnimation(animation);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        handler.postDelayed(runnable, 10000);
        dialog.show();
    }

    private void resetAll(View view) {
        editTextCustomerName.setText("");
        editTextCareOf.setText("");
        editTextHouseName.setText("");
        editTextSector.setText("");
        editTextBlockNno.setText("");
        editTextRoadName.setText("");
        editTextRoadNo.setText("");
        editTextHoldingNo.setText("");
        spinnerCustomerType.setSelection(0);
        spinnerTeritory.setSelection(0);
        spinnerMarket.setSelection(0);
        imageViewUpload.setImageBitmap(null);
    }

    private void loadMarket(View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.MARKET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArrayMarket = jsonObject.getJSONArray("list");
                            getMarket(jsonArrayMarket);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void getMarket(JSONArray jsonArrayMarket) {
                        marketList.add("Select Market");
                        for(int i = 0; i<jsonArrayMarket.length(); i++) {
                            try {
                                JSONObject json = jsonArrayMarket.getJSONObject(i);
                                marketList.add(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spinnerMarket.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, marketList));

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
                map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public String getMarketCode(int position){
        String code="";
        try {
            JSONObject json = jsonArrayMarket.getJSONObject(position-1);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void loadTeritory(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.TERITORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArrayTerritory = jsonObject.getJSONArray("list");
                            getTeritory(jsonArrayTerritory);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void getTeritory(JSONArray jsonArrayTerritory) {
                        territoryList.add("Select Territory");
                        for(int i = 0; i<jsonArrayTerritory.length(); i++) {
                            try {
                                JSONObject json = jsonArrayTerritory.getJSONObject(i);
                                territoryList.add(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spinnerTeritory.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, territoryList));

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
                map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public String getTerritoryGroup(int position){
        String group="";
        try {
            JSONObject json = jsonArrayTerritory.getJSONObject(position-1);
            group = json.getString("group");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return group;
    }
    public String getTerritoryCode(int position){
        String code="";
        try {
            JSONObject json = jsonArrayTerritory.getJSONObject(position-1);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void loadCustomerType(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.CUSTOMER_TYPE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArrayCustomerType = jsonObject.getJSONArray("list");
                            getCustomerType(jsonArrayCustomerType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    private void getCustomerType(JSONArray jsonArrayCustomerType) {
                        customerTypeList.add("Select Customer Type");
                        for(int i = 0; i<jsonArrayCustomerType.length(); i++) {
                            try {
                                JSONObject json = jsonArrayCustomerType.getJSONObject(i);
                                customerTypeList.add(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spinnerCustomerType.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, customerTypeList));
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public String getCustomerCode(int position){
        String code="";
        try {
            JSONObject json = jsonArrayCustomerType.getJSONObject(position-1);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void initUI(View view) {
        spinnerCustomerType = (Spinner) view.findViewById(R.id.spinnerCustomerType);
        spinnerTeritory = (Spinner) view.findViewById(R.id.spinnerTerritory);
        spinnerMarket = (Spinner) view.findViewById(R.id.spinnerMarket);
        linearLayoutForm = (LinearLayout) view.findViewById(R.id.linerLayoutForm);
        buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        buttonChoosePhoto = (Button) view.findViewById(R.id.buttonChoosePhoto);
        imageViewUpload = (ImageView) view.findViewById(R.id.imageViewUpload);
        editTextCustomerName = (EditText) view.findViewById(R.id.editTextCustomerName);
        editTextCareOf = (EditText) view.findViewById(R.id.editTextcareOf);
        editTextHouseName = (EditText) view.findViewById(R.id.editTextHouseName);
        editTextHoldingNo = (EditText) view.findViewById(R.id.editTextHoldingNo);
        editTextRoadNo = (EditText) view.findViewById(R.id.editTextRoadNo);
        editTextRoadName = (EditText) view.findViewById(R.id.editTextRoadName);
        editTextBlockNno = (EditText) view.findViewById(R.id.editTextBlockNo);
        editTextSector = (EditText) view.findViewById(R.id.editTextSector);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
