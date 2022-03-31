package com.csi.technodrugs.CustomerList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csi.technodrugs.Model.CustomerList;
import com.csi.technodrugs.R;
import com.csi.technodrugs.Utility.Constants;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerViewCustomerList;
    List<CustomerList> customerLists = new ArrayList<>();
    SharedPreferences sharedPreferencesUser;
    String mpoCode,searchText = "";
    View view;
    Dialog dialog;
    EditText editTextCustomerName,editTextCareOf,editTextTerritory,editTextMarket,editTextCustomerType;
    CircleImageView circleImageView;
    EditText editTextSearchText;
    Button buttonSearch;
    ACProgressPie acProgressPie;
    String imageStringFinal,oldImageString;
    private OnFragmentInteractionListener mListener;

    public CustomerListFragment() {
        // Required empty public constructor
    }
    public static CustomerListFragment newInstance(String param1, String param2) {
        CustomerListFragment fragment = new CustomerListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        sharedPreferencesUser = this.getActivity().getSharedPreferences(Constants.SharedprefItem.globalPreferenceNameForUser, Context.MODE_PRIVATE);
        mpoCode = sharedPreferencesUser.getString(Constants.SharedprefItem.MPO_CODE,"");
        // progress dialog
        acProgressPie = new ACProgressPie.Builder(getActivity())
                .ringColor(Color.WHITE)
                .pieColor(Color.parseColor("#0196A0"))
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        initUI(view);
        loadCustomer(view);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = editTextSearchText.getText().toString();
                customerLists.clear();
                loadCustomer(view);
            }
        });
        recyclerViewCustomerList.setHasFixedSize(true);
        recyclerViewCustomerList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewCustomerList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity().getApplicationContext()).color(Color.TRANSPARENT).sizeResId(R.dimen.dividerLarge).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build());

        return view;
    }

    private void loadCustomer(View view) {
        acProgressPie.show();
        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API.CUSTOMER_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            acProgressPie.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                //Toast.makeText(ActivityAuthority.this, response, Toast.LENGTH_LONG).show();
                                if ( 0 == jsonArray.length() ) {
                                    Toast.makeText(getActivity(), "No Customer available", Toast.LENGTH_LONG).show();

                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String houseNo,holdingNo,roadNo,roadName,blockNo,sector,address;
                                    houseNo = jsonObject1.optString("house_name").replace("null","");
                                    holdingNo = jsonObject1.optString("holding_no").replace("null","");
                                    roadNo = jsonObject1.optString("road_no").replace("null","");
                                    roadName = jsonObject1.optString("road_name").replace("null","");
                                    blockNo = jsonObject1.optString("block_no").replace("null","");
                                    sector = jsonObject1.optString("sector").replace("null","");
                                    address = "Holding #: "+holdingNo+",House : "+houseNo+",Road #: "+roadNo+",Road name: "+roadName+",Block #: "+blockNo+",Sector: "+sector;
                                    customerLists.add(new CustomerList(
                                            jsonObject1.getString("name"),
                                            jsonObject1.getString("co_of"),
                                            jsonObject1.getString("cust_type_name"),
                                            jsonObject1.getString("teri_name"),
                                            jsonObject1.getString("mkt_name"),
                                            jsonObject1.getString("image"),
                                            address
                                    ));
                                }
                                CustomerListAdapter adapter = new CustomerListAdapter(getActivity().getApplicationContext(), customerLists);
                                recyclerViewCustomerList.setAdapter(adapter);
                                //recyclerViewAuthority.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).color(Color.TRANSPARENT).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            acProgressPie.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Server maybe down. Please try again later...", Toast.LENGTH_LONG).show();
                            //Log.d("error", error.getLocalizedMessage());
                            acProgressPie.dismiss();
                        }
                    })
                 {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mpo_code",mpoCode);
                    map.put("name",searchText);
                    Log.d("map",map.toString());
                    return map;
                }};
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
        }catch (Exception e){}
    }

    private void initUI(View view) {
        recyclerViewCustomerList = (RecyclerView) view.findViewById(R.id.recylcerViewCustomerList);
        editTextSearchText = (EditText) view.findViewById(R.id.editTextCustomerName);
        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //adapter class
    public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder> {
        private Context context;
        private List<CustomerList> customerLists;

        public CustomerListAdapter(Context context, List<CustomerList> customerLists) {
            this.context = context;
            this.customerLists = customerLists;
        }

        @Override
        public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.customer_card, null);
            return new CustomerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomerViewHolder holder, int position) {
            final CustomerList customerList = customerLists.get(position);
            oldImageString = customerList.getImage();
            imageStringFinal = oldImageString.replace("172.16.2.7/","27.147.142.148/");
            //loading the image
            RequestOptions options = new RequestOptions()
                    .override(1600,1600)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user);
            Glide.with(getActivity().getApplicationContext()).asBitmap().load(imageStringFinal).apply(options).into(holder.imageViewProfilePic);


            holder.textViewCustomerName.setText(customerList.getName());
            holder.textViewCareOf.setText(customerList.getCareOf());
            holder.textViewAddress.setText(customerList.getAddress());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = recyclerViewCustomerList.getChildLayoutPosition(view);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
                    View promptsView = layoutInflater.inflate(R.layout.prompts_customer, null);
                    editTextCustomerName = (EditText) promptsView.findViewById(R.id.editTextCustomerName);
                    editTextCareOf = (EditText) promptsView.findViewById(R.id.editTextCareOf);
                    editTextCustomerType = (EditText) promptsView.findViewById(R.id.editTextCustomerType);
                    editTextMarket = (EditText) promptsView.findViewById(R.id.editTextMarket);
                    editTextTerritory = (EditText) promptsView.findViewById(R.id.editTextTerritory);
                    circleImageView = (CircleImageView)  promptsView.findViewById(R.id.user_profile_photo);
                    ImageView imageViewCover = (ImageView) promptsView.findViewById(R.id.header_cover_image);

                    editTextCustomerName.setText(customerList.getName());
                    editTextCareOf.setText(customerList.getCareOf());
                    editTextCustomerType.setText(customerList.getCustomerType());
                    editTextMarket.setText(customerList.getMarket());
                    editTextTerritory.setText(customerList.getTerritory());
                    RequestOptions options = new RequestOptions()
                            .override(1600,1600)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.ic_launcher_background);
                    Glide.with(getActivity().getApplicationContext()).asBitmap().load(imageStringFinal).apply(options).into(circleImageView);
                    Glide.with(getActivity().getApplicationContext()).asBitmap().load(imageStringFinal).apply(options).into(imageViewCover);

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
            return customerLists.size();
        }

        public class CustomerViewHolder extends RecyclerView.ViewHolder{
            TextView textViewCustomerName, textViewCareOf,textViewAddress;
            ImageView imageViewProfilePic;

            public CustomerViewHolder(View itemView) {
                super(itemView);

                textViewCustomerName = (TextView) itemView.findViewById(R.id.textViewCustomerName);
                textViewCareOf = (TextView) itemView.findViewById(R.id.textViewCareOf);
                textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
                imageViewProfilePic = (ImageView) itemView.findViewById(R.id.imageViewProfilePic);
            }
        }
    }
}
