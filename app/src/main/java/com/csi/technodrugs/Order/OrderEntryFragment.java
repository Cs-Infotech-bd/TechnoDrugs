package com.csi.technodrugs.Order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.Activity.ActivityLogin;
import com.csi.technodrugs.Activity.MainActivity;
import com.csi.technodrugs.Model.Datagetset;
import com.csi.technodrugs.R;
import com.csi.technodrugs.SqLite.SqlitieFunction;
import com.csi.technodrugs.Utility.Constants;
import com.csi.technodrugs.Utility.MonthToText;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class OrderEntryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    EditText editTextOrderDate,editTextDeliveryDate,editTextTottalItem,editTextTotalTradeValue,editTextTotalNetAmount,editTextCustomerCode,editTextPack,editTextTradePrice,editTextQuantity,editTextNetAmount,editTextOrderReference,editTextTotalVat,editTextTotalDiscount,editTextDrCode,editTextAddress;
    AutoCompleteTextView editTextCustomerName,autoCompleteProductName,autoCompleteDrName;
    Button buttonAdd,buttonSubmit;
    Spinner spinnerPaymentMode;
    ExpandableHeightListView listViewOrderList;
    LinearLayout linearLayoutCustomerEntryForm,linearLayoutOrderEntryForm,linearLayoutTotalOrderForm;
    Calendar calendar;
    int year,day,month,itemCount,paymentPosition;
    String customerName,customerCode,productName,orderDate,deliveryDate,paymentMode,reference,code,pack,price,vat,quantity,totalQuantity,discount,message,drName,drCode,netAmount;
    double quantityDouble,priceDouble,vatDouble,netAmountDouble,discountDouble,vatValue,discountValue,tradeValue;
    SqlitieFunction sqlitieFunction;
    SQLiteDatabase sqLiteDatabase;
    SQLiteListAdapter listAdapter;
    Cursor cursor;
    int iCount;
    ArrayList<String> productArrayList = new ArrayList();
    ArrayList<String> idArrayList = new ArrayList();
    ArrayList<String> quantityArrayList = new ArrayList();
    ArrayList<String> packSizeArrayList = new ArrayList();
    ArrayList<String> tradePriceArrayList = new ArrayList();
    ArrayList<String> netAmountArrayList = new ArrayList();
    ArrayList<Double> arrayListTotalTradeValue = new ArrayList<>();
    ArrayList<Double> arrayListTotalNetAmount = new ArrayList<>();
    ArrayList<Double> arrayListTotalVatValue = new ArrayList<>();
    ArrayList<Double> arrayListTotalDiscountValue = new ArrayList<>();
    ArrayList<Double> arrayListTotalQuantity = new ArrayList<>();
    ArrayList<String> customerList = new ArrayList();
    ArrayList<String> doctorList = new ArrayList();
    ArrayList<String> productNameList = new ArrayList();
    ArrayList<String> customerCodeList = new ArrayList();
    private ArrayList<String> paymentModeList = new ArrayList<>();
    //send data list
    private ArrayList<String> productIdListSend = new ArrayList<>();
    private ArrayList<String> tradePriceListSend = new ArrayList<>();
    private ArrayList<String> quantityListSend = new ArrayList<>();
    private ArrayList<String> vatListSend = new ArrayList<>();
    private ArrayList<String> discountListSend = new ArrayList<>();
    private ArrayList<String> totalTradePriceListSend = new ArrayList<>();
    private ArrayList<String> totalVatListSend = new ArrayList<>();
    private ArrayList<String> totalDiscountListSend = new ArrayList<>();
    private ArrayList<String> totalNetAmountListSend = new ArrayList<>();
    //Map<String, String> customerMap = new HashMap<String, String>();
    Map<String, String> customerMap = new HashMap<>();
    Map<String, String> addressMap = new HashMap<>();
    Map<String, String> doctorMap = new HashMap<>();
    Map<String, String[]> productMap = new HashMap<String, String[]>();
    String[] productInformationArray;
    JSONArray jsonArray,jsonArrayPaymentMode;
    Animation animationLeft,animationRight,animationShake;
    private OnFragmentInteractionListener mListener;
    String mpoCode,userId;
    SharedPreferences sharedPreferencesUser;
    ACProgressPie acProgressPie;
    View view;

    public OrderEntryFragment() {
    }

    public static OrderEntryFragment newInstance(String param1, String param2) {
        OrderEntryFragment fragment = new OrderEntryFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_entry, container, false);
        animationLeft = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.left_enter);
        animationRight = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.right_enter);
        animationShake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake);
        sharedPreferencesUser = this.getActivity().getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, Context.MODE_PRIVATE);
        mpoCode = sharedPreferencesUser.getString(Constants.SharedprefItem.MPO_CODE,"");
        userId = sharedPreferencesUser.getString(Constants.SharedprefItem.USER_ID,"");
        acProgressPie = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initUI(view);
        linearLayoutCustomerEntryForm.setAnimation(animationLeft);
        linearLayoutOrderEntryForm.setAnimation(animationRight);
        selectDate(view);
        loadCustomer(view);
        loadProduct(view);
        loadDrRef(view);
        loadPaymentMode(view);
        sqlitieFunction = new SqlitieFunction(getContext());
        showSqliteData(view);
        editTextCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayAdapter customerNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, customerList);
                editTextCustomerName.setAdapter(customerNameAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        autoCompleteDrName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayAdapter doctorNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, doctorList);
                autoCompleteDrName.setAdapter(doctorNameAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        autoCompleteDrName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                drName = autoCompleteDrName.getText().toString();
                editTextDrCode.setText(doctorMap.get( drName ));
            }
        });
        editTextCustomerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                customerName = editTextCustomerName.getText().toString();
                editTextCustomerCode.setText(customerMap.get( customerName ));
                editTextAddress.setText(addressMap.get( customerName ));
            }
        });
        autoCompleteProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayAdapter productNameAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, productNameList);
                autoCompleteProductName.setAdapter(productNameAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinnerPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentPosition = spinnerPaymentMode.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        autoCompleteProductName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productName = autoCompleteProductName.getText().toString();
                code = productMap.get(productName)[0];
                pack = productMap.get(productName)[1];
                price = productMap.get(productName)[2];
                vat = productMap.get(productName)[3];
                discount = productMap.get(productName)[4];
                editTextPack.setText(pack);
                editTextTradePrice.setText(price);
            }
        });
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    String quantity = editTextQuantity.getText().toString();
                    quantityDouble = Double.parseDouble(quantity);
                    priceDouble = Double.parseDouble(price);
                    vatDouble = Double.parseDouble(vat);
                    discountDouble = Double.parseDouble(discount);
                    vatValue = quantityDouble * vatDouble;
                    discountValue = quantityDouble * discountDouble;
                    tradeValue = priceDouble * quantityDouble;
                    netAmountDouble = ( tradeValue + vatValue ) - discountValue;
                    editTextNetAmount.setText(new DecimalFormat("##.##").format(netAmountDouble));
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    customerName = editTextCustomerName.getText().toString();
                    customerCode = editTextCustomerCode.getText().toString();
                    orderDate = editTextOrderDate.getText().toString();
                    deliveryDate = editTextDeliveryDate.getText().toString();
                    paymentMode = spinnerPaymentMode.getSelectedItem().toString();
                    reference = editTextOrderReference.getText().toString();
                    productName = autoCompleteProductName.getText().toString();
                    quantity = editTextQuantity.getText().toString();
                    drName = autoCompleteDrName.getText().toString();
                    drCode = editTextDrCode.getText().toString();
                    netAmount = editTextNetAmount.getText().toString();
                    sqlitieFunction = new SqlitieFunction(getActivity().getApplicationContext());
                if (customerName.isEmpty()){
                    editTextCustomerName.setError("Enter Customer Name");
                    editTextCustomerName.requestFocus();
                    linearLayoutCustomerEntryForm.setAnimation(animationShake);
                }
                else if (paymentPosition == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"Select Payment Mode",Toast.LENGTH_SHORT).show();
                }
                else if (reference.isEmpty()){
                    editTextOrderReference.setError("Enter Reference");
                    editTextOrderReference.requestFocus();
                    linearLayoutCustomerEntryForm.setAnimation(animationShake);
                }

                else if (productName.isEmpty()){
                    autoCompleteProductName.setError("Enter Product");
                    autoCompleteProductName.requestFocus();
                    linearLayoutOrderEntryForm.setAnimation(animationShake);
                }
                else if (quantity.isEmpty()){
                    editTextQuantity.setError("Enter Quantity");
                    editTextQuantity.requestFocus();
                    linearLayoutOrderEntryForm.setAnimation(animationShake);
                }
                else if ( sqlitieFunction.isExist(code)){
                    Toast.makeText(getActivity().getApplicationContext(), "Duplicate Entry", Toast.LENGTH_SHORT).show();
                }
                else {
                    insertintoDatabase(view);
                    autoCompleteProductName.setText("");
                    editTextPack.setText("");
                    editTextTradePrice.setText("");
                    editTextQuantity.setText("");
                    editTextNetAmount.setText("");
                }
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextCustomerCode.setText("");
                editTextCustomerName.setText("");
                editTextOrderReference.setText("");
                sqLiteDatabase = sqlitieFunction.getWritableDatabase();
                String count = "SELECT count(*) FROM productTable";
                Cursor mCursor = sqLiteDatabase.rawQuery(count, null);
                mCursor.moveToFirst();
                iCount = mCursor.getInt(0);
                if ( iCount == 0){
                    showErrorDialog();
                }
                else {
                    sendData();
                }
            }
        });
        listViewOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ImageView imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
                imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = idArrayList.get(i);
                        sqlitieFunction.dataDelete(id);
                        //sqlitieFunction.deleteAll();
                        arrayListTotalNetAmount.clear();
                        arrayListTotalTradeValue.clear();
                        showSqliteData(view);
                    }
                });
            }
        });
        return view;
    }

    private void loadDrRef(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.DOCTOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("list");
                            getDrRef(jsonArray);
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
                map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getDrRef(JSONArray jsonArray) {
        for(int i = 0; i<jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                doctorList.add(json.getString("name"));
                doctorMap.put(json.getString("name"), json.getString("code"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendData() {
        sqLiteDatabase = sqlitieFunction.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM productTable", null);
        if ( cursor.moveToFirst())
        do {
            customerCode = cursor.getString(cursor.getColumnIndex(sqlitieFunction.customerCode));
            orderDate = cursor.getString(cursor.getColumnIndex(sqlitieFunction.orderDate));
            deliveryDate = cursor.getString(cursor.getColumnIndex(sqlitieFunction.deliveryDate));
            paymentMode = cursor.getString(cursor.getColumnIndex(sqlitieFunction.paymentMode));
            reference = cursor.getString(cursor.getColumnIndex(sqlitieFunction.orderRef));
            drCode = cursor.getString(cursor.getColumnIndex(sqlitieFunction.drCode));
            final String productId = cursor.getString(cursor.getColumnIndex(sqlitieFunction.productId));
            final String tradePrice = cursor.getString(cursor.getColumnIndex(sqlitieFunction.tradePrice));
            final String quantity = cursor.getString(cursor.getColumnIndex(sqlitieFunction.quantity));
            final String vat = cursor.getString(cursor.getColumnIndex(sqlitieFunction.vat));
            final String discount = cursor.getString(cursor.getColumnIndex(sqlitieFunction.discount));
            final String totalTradePrice = cursor.getString(cursor.getColumnIndex(sqlitieFunction.tradeValue));
            final String totalVat = cursor.getString(cursor.getColumnIndex(sqlitieFunction.totalVat));
            final String totalDiscount = cursor.getString(cursor.getColumnIndex(sqlitieFunction.totalDiscount));
            final String totalNetAmount = cursor.getString(cursor.getColumnIndex(sqlitieFunction.netAmount));
            productIdListSend.add(productId);
            tradePriceListSend.add(tradePrice);
            quantityListSend.add(quantity);
            vatListSend.add(vat);
            discountListSend.add(discount);
            totalTradePriceListSend.add(totalTradePrice);
            totalVatListSend.add(totalVat);
            totalDiscountListSend.add(totalDiscount);
            totalNetAmountListSend.add(totalNetAmount);

        }while ( cursor.moveToNext());
        acProgressPie.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.ORDER_ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                try {
                    acProgressPie.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.matches("1")) {
                        message = jsonObject.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                message, Toast.LENGTH_SHORT).show();
                        sqlitieFunction.deleteAll();
                        showSqliteData(view);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        //errorMessage = jsonObject.getString("message");
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
                acProgressPie.dismiss();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mpo_code",mpoCode);
                params.put("customer_code", customerCode);
                params.put("order_dt", orderDate);
                params.put("delivery_dt", deliveryDate);
                params.put("pay_mode", paymentMode);
                params.put("order_ref", reference);
                params.put("user_id", userId);
                params.put("dr_ref", drCode);
                for (int x = 0; x < productIdListSend.size(); x++) {
                    params.put("product_code["+x+"]", productIdListSend.get(x));
                    params.put("product_price["+x+"]", tradePriceListSend.get(x));
                    params.put("product_quantity["+x+"]", quantityListSend.get(x));
                    params.put("product_vat["+x+"]", vatListSend.get(x));
                    params.put("product_discount["+x+"]", discountListSend.get(x));
                    params.put("product_total_price["+x+"]", totalTradePriceListSend.get(x));
                    params.put("product_total_vat["+x+"]", totalVatListSend.get(x));
                    params.put("product_total_discount["+x+"]", totalDiscountListSend.get(x));
                    params.put("product_total_net_price["+x+"]", totalNetAmountListSend.get(x));
                }
                params.put("total_price", editTextTotalTradeValue.getText().toString());
                params.put("total_vat", editTextTotalVat.getText().toString());
                params.put("total_discount", editTextTotalDiscount.getText().toString());
                params.put("net_total_price", editTextTotalNetAmount.getText().toString());
                params.put("productId", totalQuantity);
                params.put("item_total", editTextTottalItem.getText().toString());
                Log.d("All data", params.toString());
                return params;
            }};
        queue.add(stringRequest);
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
        text.setText("No Product added");
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

    private void loadPaymentMode(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.PAYMENT_MODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArrayPaymentMode = jsonObject.getJSONArray("list");
                            getCustomerType(jsonArrayPaymentMode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    private void getCustomerType(JSONArray jsonArrayCustomerType) {
                        paymentModeList.add("Select Payment Mode");
                        for(int i = 0; i<jsonArrayCustomerType.length(); i++) {
                            try {
                                JSONObject json = jsonArrayCustomerType.getJSONObject(i);
                                paymentModeList.add(json.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spinnerPaymentMode.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, paymentModeList));
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

    private void visiableTotalForm(View view) {
            linearLayoutTotalOrderForm.setVisibility(View.VISIBLE);
            linearLayoutTotalOrderForm.setAnimation(animationLeft);

    }

    private void loadProduct(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.PRODUCT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("list");
                            getProduct(jsonArray);
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
                map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getProduct(JSONArray jsonArray) {
        for(int i = 0; i<jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                productNameList.add(json.getString("name"));
                productInformationArray = new String[]{json.getString("code"),json.getString("pack"), json.getString("price"), json.getString("vat"),json.getString("discount")};
                productMap.put(json.getString("name"), productInformationArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCustomer(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.CUSTOMER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("list");
                            getCustomer(jsonArray);
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
                map.put("mpo_code",mpoCode);
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getCustomer(JSONArray jsonArray) {
        String houseNo,holdingNo,roadNo,roadName,blockNo,sector,address;
        for(int i = 0; i<jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                customerList.add(json.getString("name")+" ("+json.getString("house_name")+")");
                customerMap.put(json.getString("name")+" ("+json.getString("house_name")+")", json.getString("code"));
                houseNo = json.optString("house_name").replace("null","");
                holdingNo = json.optString("holding_no").replace("null","");
                roadNo = json.optString("road_no").replace("null","");
                roadName = json.optString("road_name").replace("null","");
                blockNo = json.optString("block_no").replace("null","");
                sector = json.optString("sector").replace("null","");
                address = "Holding #: "+holdingNo+", House : "+houseNo+", Road #: "+roadNo+", Road name: "+roadName+", Block #: "+blockNo+", Sector: "+sector;
                addressMap.put(json.getString("name")+" ("+json.getString("house_name")+")", address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public String getCustomerCode(int position){
        String code="";
        try {
            JSONObject json = jsonArray.getJSONObject(position);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private void showSqliteData(View view) {
        sqLiteDatabase = sqlitieFunction.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM productTable", null);
        idArrayList.clear();
        productArrayList.clear();
        quantityArrayList.clear();
        packSizeArrayList.clear();
        tradePriceArrayList.clear();
        netAmountArrayList.clear();
        arrayListTotalTradeValue.clear();
        if (cursor.moveToFirst()) {
            do {
                idArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.id)));
                productArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.productName)));
                quantityArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.quantity)));
                packSizeArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.packSize)));
                tradePriceArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.tradePrice)));
                netAmountArrayList.add(cursor.getString(cursor.getColumnIndex(sqlitieFunction.netAmount)));
                //sum of all values
                arrayListTotalTradeValue.add(Double.parseDouble(cursor.getString(cursor.getColumnIndex(sqlitieFunction.tradeValue))));
                arrayListTotalNetAmount.add(Double.parseDouble(cursor.getString(cursor.getColumnIndex(sqlitieFunction.netAmount))));
                arrayListTotalVatValue.add(Double.parseDouble(cursor.getString(cursor.getColumnIndex(sqlitieFunction.totalVat))));
                arrayListTotalDiscountValue.add(Double.parseDouble(cursor.getString(cursor.getColumnIndex(sqlitieFunction.totalDiscount))));
                arrayListTotalQuantity.add(Double.parseDouble(cursor.getString(cursor.getColumnIndex(sqlitieFunction.quantity))));

            } while (cursor.moveToNext());
        }
        listAdapter = new SQLiteListAdapter(getActivity(),idArrayList,productArrayList,quantityArrayList,packSizeArrayList,tradePriceArrayList,netAmountArrayList);
        listViewOrderList.setAdapter(listAdapter);
        listViewOrderList.setExpanded(true);
        itemCount = listAdapter.getCount();
        //Calculated Total Items
        editTextTottalItem.setText(String.valueOf(itemCount));
        if ( itemCount != 0){
            visiableTotalForm(view);
        }
        if ( itemCount == 0){
            linearLayoutTotalOrderForm.setVisibility(View.GONE);
        }
        totalQuantity();
        totalTradeValue();
        totalNetAmount();
        totalVatValue();
        totalDiscountValue();
        cursor.close();
    }

    private double totalQuantity() {
        double sum = 0;
        for(int i = 0; i < arrayListTotalQuantity.size(); i++)
        {
            sum += arrayListTotalQuantity.get(i);
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        totalQuantity = String.valueOf(formatter.format(sum));
        return sum;
    }

    private double totalDiscountValue() {
        double sum = 0;
        for(int i = 0; i < arrayListTotalDiscountValue.size(); i++)
        {
            sum += arrayListTotalDiscountValue.get(i);
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        editTextTotalDiscount.setText(formatter.format(sum));
        return sum;
    }

    private double totalVatValue() {
        double sum = 0;
        for(int i = 0; i < arrayListTotalVatValue.size(); i++)
        {
            sum += arrayListTotalVatValue.get(i);
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        editTextTotalVat.setText(formatter.format(sum));
        return sum;
    }

    private double totalNetAmount() {
        double sum = 0;
        for(int i = 0; i < arrayListTotalNetAmount.size(); i++)
        {
            sum += arrayListTotalNetAmount.get(i);
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        editTextTotalNetAmount.setText(formatter.format(sum));
        return sum;
    }

    private double totalTradeValue() {
        double sum = 0;
        for(int i = 0; i < arrayListTotalTradeValue.size(); i++)
        {
            sum += arrayListTotalTradeValue.get(i);
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        editTextTotalTradeValue.setText(formatter.format(sum));
        return sum;
    }

    private void insertintoDatabase(View view) {
        Datagetset datagetset = new Datagetset(customerName,customerCode,orderDate,deliveryDate,paymentMode,reference,productName,code,pack,price,quantity,netAmount,vat,String.valueOf(vatValue),discount,String.valueOf(discountDouble),String.valueOf(tradeValue),drCode);
        sqlitieFunction.datainsert(datagetset);
        listAdapter.notifyDataSetChanged();
        arrayListTotalTradeValue.clear();
        arrayListTotalNetAmount.clear();
        arrayListTotalVatValue.clear();
        arrayListTotalDiscountValue.clear();
        showSqliteData(view);
        Log.d("All Data",datagetset.toString());
    }

    private void selectDate(View view) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);

        editTextOrderDate.setText(MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year));
        editTextDeliveryDate.setText(MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year));
        //editTextDeliveryDate.setText(MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year));
        /*editTextOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                        editTextOrderDate.setText(MonthToText.mothNameText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + mYear));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });*/
        editTextDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateNew();
            }
        });

    }

    private void selectDateNew() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _year = String.valueOf(year);
                String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String pickedDate = _date + "-" + _month + "-" + year;
                int endYear = year+1;
                String nextDate = _date + "-" + _month + "-" + endYear;
                    editTextDeliveryDate.setText(pickedDate);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void initUI(View view) {
        editTextOrderDate = (EditText) view.findViewById(R.id.editTextOrderDate);
        editTextDeliveryDate = (EditText) view.findViewById(R.id.editTextDeliveryDate);
        editTextTottalItem = (EditText) view.findViewById(R.id.editTextTotalOrderItems);
        editTextTotalTradeValue = (EditText) view.findViewById(R.id.editTradeValue);
        editTextTotalNetAmount = (EditText) view.findViewById(R.id.editTextNetValue);
        editTextCustomerCode = (EditText) view.findViewById(R.id.editTextCustomerCode);
        editTextPack = (EditText) view.findViewById(R.id.editTextPack);
        editTextTradePrice = (EditText) view.findViewById(R.id.editTextTradePrice);
        editTextQuantity = (EditText) view.findViewById(R.id.editTextQuantity);
        editTextNetAmount = (EditText) view.findViewById(R.id.editTextNetAmount);
        editTextDrCode = (EditText) view.findViewById(R.id.editTextDrRefCode);
        editTextOrderReference = (EditText) view.findViewById(R.id.editTextOrderRef);
        editTextTotalVat = (EditText) view.findViewById(R.id.editTextVatValue);
        editTextTotalDiscount = (EditText) view.findViewById(R.id.editTextDisValue);
        editTextCustomerName = (AutoCompleteTextView) view.findViewById(R.id.editTextCustomerName);
        editTextAddress = (EditText) view.findViewById(R.id.editTextCustomerAddress);
        autoCompleteProductName = (AutoCompleteTextView) view.findViewById(R.id.editTextProductName);
        autoCompleteDrName = (AutoCompleteTextView) view.findViewById(R.id.editTextDrRefName);
        buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        listViewOrderList = (ExpandableHeightListView) view.findViewById(R.id.listViewOrderList);
        linearLayoutCustomerEntryForm = (LinearLayout) view.findViewById(R.id.customerEntryForm);
        linearLayoutOrderEntryForm = (LinearLayout) view.findViewById(R.id.orderEntryForm);
        linearLayoutTotalOrderForm = (LinearLayout) view.findViewById(R.id.totalOrderForm);
        spinnerPaymentMode = (Spinner) view.findViewById(R.id.spinnerPaymentMode);
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
