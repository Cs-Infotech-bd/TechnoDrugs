package com.csi.technodrugs.OrderStatus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.csi.technodrugs.R;
import com.csi.technodrugs.Utility.Constants;
import com.csi.technodrugs.Utility.MonthToText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class OrderStatusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    EditText editTextStartDate,editTextEndDate,editTextProductName,editTextRate,editTextQuantity,editTextInvoiceQuantity,editTextInvoiceAmount;
    RecyclerView recyclerViewOrderStatus,recyclerViewProductList;
    TextView textViewCustomerName,textViewStoreName,textViewPaymentMode;
    Button buttonSearch;
    Calendar calendar;
    int year,day,month;
    JSONArray jsonArrayOrder,jsonArrayProduct;
    List<Product> productList = new ArrayList<>();
    List<Order> orderList = new ArrayList<>();
    String mpoCode,startDate,endDate;
    String orderNo,orderNoFuture,orderDate,invoiceNo,invoiceDate,customerCode,customerName,storeCode,storeName,mpoOrderNo,mpoGroup,mpoGroupName,srCode,saleType,territoryCode,businessUnit,paymentMode;
    View view;
    Dialog dialog;
    ACProgressPie acProgressPie;
    SharedPreferences sharedPreferencesUser;
    DatePickerDialog datePickerDialog;
    private OnFragmentInteractionListener mListener;

    public OrderStatusFragment() {
    }

    public static OrderStatusFragment newInstance(String param1, String param2) {
        OrderStatusFragment fragment = new OrderStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);
        sharedPreferencesUser = this.getActivity().getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, Context.MODE_PRIVATE);
        mpoCode = sharedPreferencesUser.getString(Constants.SharedprefItem.MPO_CODE,"");
        // progress dialog
        acProgressPie = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initUI(view);
        selectDate(view);
        loadOrderList(view);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDate = editTextStartDate.getText().toString();
                endDate = editTextEndDate.getText().toString();
                productList.clear();
                orderList.clear();
                loadOrderList(view);
            }
        });
        recyclerViewOrderStatus.setHasFixedSize(true);
        recyclerViewOrderStatus.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewOrderStatus.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity().getApplicationContext()).color(Color.TRANSPARENT).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build());
        return view;
    }

    private void loadOrderList(View view) {
        acProgressPie.show();
        productList.clear();
        orderList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.ORDER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("re",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.matches("0")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                PrductAdapter adapter = new PrductAdapter(getActivity().getApplicationContext(), orderList);
                                recyclerViewOrderStatus.setAdapter(adapter);
                            }
                            else  {
                                jsonArrayOrder = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jsonArrayOrder.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayOrder.getJSONObject(i);
                                    jsonObject = jsonArrayOrder.getJSONObject(i);
                                    jsonArrayProduct = jsonObject.getJSONArray("product");
                                    orderNo = jsonObject1.getString("order_no");
                                    orderDate = jsonObject1.getString("order_date");
                                    invoiceNo = jsonObject1.getString("invoice_no");
                                    invoiceDate = jsonObject1.getString("invoice_date");
                                    customerCode = jsonObject1.getString("customer_code");
                                    customerName = jsonObject1.getString("customer_name");
                                    storeCode = jsonObject1.getString("store_code");
                                    storeName = jsonObject1.getString("store_name");
                                    mpoOrderNo = jsonObject1.getString("mpo_order_no");
                                    mpoGroup = jsonObject1.getString("mpo_group");
                                    mpoGroupName = jsonObject1.getString("mpo_group_name");
                                    srCode = jsonObject1.getString("sr_code");
                                    saleType = jsonObject1.getString("sale_type");
                                    territoryCode = jsonObject1.getString("teritory_code");
                                    businessUnit = jsonObject1.getString("business_unit");
                                    paymentMode = jsonObject1.getString("payment_mode");

                                    orderList.add(new Order(
                                            jsonObject1.getString("order_no"),
                                            jsonObject1.getString("order_date"),
                                            jsonObject1.getString("invoice_no"),
                                            jsonObject1.getString("invoice_date"),
                                            jsonObject1.getString("customer_code"),
                                            jsonObject1.getString("customer_name"),
                                            jsonObject1.getString("store_code"),
                                            jsonObject1.getString("store_name"),
                                            jsonObject1.getString("mpo_order_no"),
                                            jsonObject1.getString("mpo_group"),
                                            jsonObject1.getString("mpo_group_name"),
                                            jsonObject1.getString("sr_code"),
                                            jsonObject1.getString("sale_type"),
                                            jsonObject1.getString("teritory_code"),
                                            jsonObject1.getString("business_unit"),
                                            jsonObject1.getString("payment_mode"),
                                            jsonObject1.getString("order_net_value"),
                                            jsonObject1.getString("invoice_net_value")
                                    ));
                                    for (int i1 = 0; i1 < jsonArrayProduct.length(); i1++) {
                                        JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject(i1);
                                    /*orderNo = jsonObject1.getString("order_no");
                                    actual_price = jsonObjectProduct.getString("actual_price");*/
                                        productList.add(new Product(
                                                jsonObject1.getString("order_no"),
                                                jsonObject1.getString("order_date"),
                                                jsonObject1.getString("invoice_no"),
                                                jsonObject1.getString("invoice_date"),
                                                jsonObject1.getString("customer_code"),
                                                jsonObject1.getString("customer_name"),
                                                jsonObject1.getString("store_code"),
                                                jsonObject1.getString("store_name"),
                                                jsonObject1.getString("mpo_order_no"),
                                                jsonObject1.getString("mpo_group"),
                                                jsonObject1.getString("mpo_group_name"),
                                                jsonObject1.getString("sr_code"),
                                                jsonObject1.getString("sale_type"),
                                                jsonObject1.getString("teritory_code"),
                                                jsonObject1.getString("business_unit"),
                                                jsonObject1.getString("payment_mode"),
                                                jsonObjectProduct.getString("actual_price"),
                                                jsonObjectProduct.getString("discount_price"),
                                                jsonObjectProduct.getString("vat_price"),
                                                jsonObjectProduct.getString("net_price"),
                                                jsonObjectProduct.getString("custsign"),
                                                jsonObjectProduct.getString("enter_dt"),
                                                jsonObjectProduct.getString("code"),
                                                jsonObjectProduct.getString("rate"),
                                                jsonObjectProduct.getString("quantity"),
                                                jsonObjectProduct.getString("inv_quantity"),
                                                jsonObjectProduct.getString("category_code"),
                                                jsonObjectProduct.getString("vat"),
                                                jsonObjectProduct.getString("sale_percentage"),
                                                jsonObjectProduct.getString("inv_act_value"),
                                                jsonObjectProduct.getString("name")
                                        ));
                                    }
                                    PrductAdapter adapter = new PrductAdapter(getActivity().getApplicationContext(), orderList);
                                    recyclerViewOrderStatus.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                                //loadProduct(jsonArrayOrder);
                            }
                        }catch (JSONException e) {

                            e.printStackTrace();
                        }
                        finally {
                            acProgressPie.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        acProgressPie.dismiss();
                    }

                })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("mpo_code",mpoCode);
                map.put("date1",startDate);
                map.put("date2",endDate);
                Log.d("map",map.toString());
                return map;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void selectDate(View view) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        editTextStartDate.setText(MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year));
        editTextEndDate.setText(MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year));
        startDate = MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year);
        endDate = MonthToText.mothNameText(day + "-" + (month + 1) + "-" + year);
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                        editTextStartDate.setText(MonthToText.mothNameText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + mYear));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                        editTextEndDate.setText(MonthToText.mothNameText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + mYear));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void initUI(View view) {
        editTextStartDate = (EditText) view.findViewById(R.id.editTextStartDate);
        editTextEndDate = (EditText) view.findViewById(R.id.editTextEndDate);
        recyclerViewOrderStatus = (RecyclerView) view.findViewById(R.id.recylcerViewOrderStatus);
        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class PrductAdapter extends RecyclerView.Adapter<PrductAdapter.OrderViewHolder> {
        private Context context;
        private List<Order> orderList;
        String nullObject = "null";

        public PrductAdapter(Context context, List<Order> orderList) {
            this.context = context;
            this.orderList = orderList;
        }

        @Override
        public PrductAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.status_card, null);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PrductAdapter.OrderViewHolder holder, int position) {
            final Product product = productList.get(position);
            final Order order = orderList.get(position);
            String invoiceNo,invoiceDate,invoiceAmount;
            holder.textViewOrderNo.setText(order.getOrderNo());
            holder.textViewCustomerName.setText(order.getCustomerName());
            holder.textViewOrderAmount.setText(order.getOrderAmount());
            holder.textViewInvoiceNo.setText(order.getInvoiceNo());
            holder.textViewInvoiceDate.setText(order.getInvoiceDate());
            holder.textViewInvoiceAmount.setText(order.getInvoiceAmount().replace("null",""));
            invoiceNo = holder.textViewInvoiceNo.getText().toString();
            invoiceDate = holder.textViewInvoiceDate.getText().toString();
            invoiceAmount = holder.textViewInvoiceAmount.getText().toString();
            if (invoiceNo.matches(nullObject)){
                holder.textViewInvoiceNo.setText("");
            }
            if (invoiceDate.matches(nullObject)){
                holder.textViewInvoiceDate.setText("");
            }
            if (invoiceAmount.matches(nullObject)){
                holder.textViewInvoiceAmount.setText("");
            }
           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productName,rate,qty,inQty,inAmount;
                    int itemPosition = recyclerViewOrderStatus.getChildLayoutPosition(view);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
                    final View promptsView = layoutInflater.inflate(R.layout.prompts_details, null);
                    textViewCustomerName = (TextView) promptsView.findViewById(R.id.textViewCustomerName);
                    textViewStoreName = (TextView) promptsView.findViewById(R.id.textViewStoreName);
                    textViewPaymentMode = (TextView) promptsView.findViewById(R.id.textViewPaymentMode);
                    editTextProductName = (EditText) promptsView.findViewById(R.id.editTextProductName);
                    editTextRate = (EditText) promptsView.findViewById(R.id.editTextRate);
                    editTextQuantity = (EditText) promptsView.findViewById(R.id.editTextQuantity);
                    editTextInvoiceQuantity = (EditText) promptsView.findViewById(R.id.editTextInvoiceQuantity);
                    editTextInvoiceAmount = (EditText) promptsView.findViewById(R.id.editTextInvoiceAmount);

                    textViewCustomerName.setText(product.getCustomerName());
                    textViewStoreName.setText(product.getStoreName());
                    textViewPaymentMode.setText(product.getPaymentMode());
                    editTextProductName.setText(product.getProductName());
                    editTextRate.setText(product.getRate());
                    editTextQuantity.setText(product.getQuantity());
                    editTextInvoiceQuantity.setText(product.getInvQuantity());
                    editTextInvoiceAmount.setText(product.getInvActualValue());

                    productName = editTextProductName.getText().toString();
                    rate = editTextRate.getText().toString();
                    qty = editTextQuantity.getText().toString();
                    inQty = editTextInvoiceQuantity.getText().toString();
                    inAmount = editTextInvoiceAmount.getText().toString();
                    if ( productName.matches(nullObject)){
                        editTextProductName.setText("");
                    }
                    if ( rate.matches(nullObject)){
                        editTextRate.setText("0");
                    }
                    if ( qty.matches(nullObject)){
                        editTextQuantity.setText("0");
                    }
                    if ( inQty.matches(nullObject)){
                        editTextInvoiceQuantity.setText("0");
                    }
                    if ( inAmount.matches(nullObject)){
                        editTextInvoiceAmount.setText("0");
                    }


                    dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(promptsView);
                    ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.imageViewClose);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    imageViewClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }

            });*/


           //new code 11-02-2019
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderNoFuture = order.getOrderNo();
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
                    final View promptsView = layoutInflater.inflate(R.layout.prompts_details_2, null);
                    recyclerViewProductList = (RecyclerView) promptsView.findViewById(R.id.recylcerViewProductList);
                    textViewCustomerName = (TextView) promptsView.findViewById(R.id.textViewCustomerName);
                    textViewStoreName = (TextView) promptsView.findViewById(R.id.textViewStoreName);
                    textViewPaymentMode = (TextView) promptsView.findViewById(R.id.textViewPaymentMode);
                    //
                    productList.clear();
                    orderList.clear();
                    acProgressPie.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.API.ORDER_DETAILS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        if (status.matches("0")){
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                            PrductAdapter adapter = new PrductAdapter(getActivity().getApplicationContext(), orderList);
                                            recyclerViewOrderStatus.setAdapter(adapter);
                                        }
                                        else  {
                                            jsonArrayOrder = jsonObject.getJSONArray("list");
                                            for (int i = 0; i < jsonArrayOrder.length(); i++) {
                                                JSONObject jsonObject1 = jsonArrayOrder.getJSONObject(i);
                                                jsonObject = jsonArrayOrder.getJSONObject(i);
                                                jsonArrayProduct = jsonObject.getJSONArray("product");
                                                for (int i1 = 0; i1 < jsonArrayProduct.length(); i1++) {
                                                    JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject(i1);
                                                    productList.add(new Product(
                                                            jsonObject1.getString("order_no"),
                                                            jsonObject1.getString("order_date"),
                                                            jsonObject1.getString("invoice_no"),
                                                            jsonObject1.getString("invoice_date"),
                                                            jsonObject1.getString("customer_code"),
                                                            jsonObject1.getString("customer_name"),
                                                            jsonObject1.getString("store_code"),
                                                            jsonObject1.getString("store_name"),
                                                            jsonObject1.getString("mpo_order_no"),
                                                            jsonObject1.getString("mpo_group"),
                                                            jsonObject1.getString("mpo_group_name"),
                                                            jsonObject1.getString("sr_code"),
                                                            jsonObject1.getString("sale_type"),
                                                            jsonObject1.getString("teritory_code"),
                                                            jsonObject1.getString("business_unit"),
                                                            jsonObject1.getString("payment_mode"),
                                                            jsonObjectProduct.getString("actual_price"),
                                                            jsonObjectProduct.getString("discount_price"),
                                                            jsonObjectProduct.getString("vat_price"),
                                                            jsonObjectProduct.getString("net_price"),
                                                            jsonObjectProduct.getString("custsign"),
                                                            jsonObjectProduct.getString("enter_dt"),
                                                            jsonObjectProduct.getString("code"),
                                                            jsonObjectProduct.getString("rate"),
                                                            jsonObjectProduct.getString("quantity"),
                                                            jsonObjectProduct.getString("inv_quantity"),
                                                            jsonObjectProduct.getString("category_code"),
                                                            jsonObjectProduct.getString("vat"),
                                                            jsonObjectProduct.getString("sale_percentage"),
                                                            jsonObjectProduct.getString("inv_act_value"),
                                                            jsonObjectProduct.getString("name")
                                                    ));
                                                }
                                                OrderAdapter orderAdapter = new OrderAdapter(getActivity().getApplicationContext(), productList);
                                                recyclerViewProductList.setHasFixedSize(true);
                                                recyclerViewProductList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                                recyclerViewProductList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity().getApplicationContext()).color(Color.TRANSPARENT).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build());
                                                recyclerViewProductList.setAdapter(orderAdapter);
                                                orderAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                    finally {
                                        acProgressPie.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    acProgressPie.dismiss();
                                }

                            })
                    {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("mpo_code",mpoCode);
                            map.put("order_no",orderNoFuture);
                            Log.d("map",map.toString());
                            return map;
                        }};

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue.add(stringRequest);


                    dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(promptsView);
                    ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.imageViewClose);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    imageViewClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public class OrderViewHolder extends RecyclerView.ViewHolder{
            TextView textViewOrderNo,textViewCustomerName,textViewOrderAmount,textViewInvoiceNo,textViewInvoiceDate,textViewInvoiceAmount;
            public OrderViewHolder(View itemView) {
                super(itemView);

                textViewOrderNo = (TextView) itemView.findViewById(R.id.textViewOrderNo);
                textViewCustomerName = (TextView) itemView.findViewById(R.id.textViewCustomerName);
                textViewOrderAmount = (TextView) itemView.findViewById(R.id.textViewOrderAmount);
                textViewInvoiceNo = (TextView) itemView.findViewById(R.id.textViewInvoiceNo);
                textViewInvoiceDate = (TextView) itemView.findViewById(R.id.textViewInvoiceDate);
                textViewInvoiceAmount = (TextView) itemView.findViewById(R.id.textViewInvoiceAmount);
            }
        }
    }

    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private Context context;
        private List<Product> productList;
        String nullObject = "null";

        public OrderAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.product_card, null);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrderAdapter.OrderViewHolder holder, int position) {
            final Product product = productList.get(position);
            String invoiceNo,invoiceQuantity,invoiceAmount;
            holder.textViewProductName.setText(product.getProductName());
            holder.textViewRate.setText(product.getRate());
            holder.textViewQuantity.setText(product.getQuantity());
            holder.textViewInvoiceQuantity.setText(product.getInvQuantity());
            holder.textViewInvoiceAmount.setText(product.getInvActualValue());
            textViewCustomerName.setText(product.getCustomerName());
            textViewStoreName.setText(product.getStoreName());
            textViewPaymentMode.setText(product.getPaymentMode());
            invoiceQuantity = holder.textViewInvoiceQuantity.getText().toString();
            invoiceAmount = holder.textViewInvoiceAmount.getText().toString();
            if (invoiceQuantity.matches(nullObject)){
                holder.textViewInvoiceQuantity.setText("");
            }
            if (invoiceAmount.matches(nullObject)){
                holder.textViewInvoiceAmount.setText("");
            }

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class OrderViewHolder extends RecyclerView.ViewHolder{
            TextView textViewProductName,textViewRate,textViewQuantity,textViewInvoiceQuantity,textViewInvoiceAmount;
            public OrderViewHolder(View itemView) {
                super(itemView);

                textViewProductName = (TextView) itemView.findViewById(R.id.textViewProductName);
                textViewRate = (TextView) itemView.findViewById(R.id.textViewRate);
                textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
                textViewInvoiceQuantity = (TextView) itemView.findViewById(R.id.textViewInvoiceQuantity);
                textViewInvoiceAmount = (TextView) itemView.findViewById(R.id.textViewInvoiceAmount);
            }
        }
    }
}
