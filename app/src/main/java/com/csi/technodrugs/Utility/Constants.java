package com.csi.technodrugs.Utility;

import android.content.SharedPreferences;

/**
 * Created by Jahid on 23/10/18.
 */

public class Constants {

    private static int SPLASH_TIME_OUT = 1500;
    private static String CITY = "CITY";

    public class SharedprefItem {
        public static  final String globalPreferenceNameForUser = "USER";
        public static final String API_TOKEN = "API_TOKEN";
        public static final String USER_TYPE = "USER_TYPE";
        public static final String MPO_CODE = "MPO_CODE";
        public static final String USER_ID = "USER_ID";
        public static final String COMPANY_NAME = "COMPANY_NAME";
        public static final String USER_NAME = "USER_NAME";
    }
    public class API {
        public static final String  URL = "http://27.147.142.148/tdlapp/public/api/v1/"; //server
        //public static final String  URL = "http://192.168.37.42/tdlapp/public/api/v1/";
        //public static final String  URL = "http://172.16.2.7/tdlapp/public/api/v1/";
        //public static final String  URL = "http://192.168.37.74/work/tdlapp/public/api/v1/";
        //public static final String  URL = "http://192.168.37.77/work/technodrugs/public/api/v1/";
        public static final String  USER_LOGIN = URL+"login";
        public static final String  CUSTOMER_TYPE = URL+"customer-type-list";
        public static final String  TERITORY = URL+"teritory-list";
        public static final String  MPO_LIST = URL+"mpo-list?";
        public static final String  MARKET = URL+"market-list";
        public static final String  CUSTOMER_LIST = URL+"customer-list";
        public static final String  PRODUCT_LIST = URL+"product-list";
        public static final String  ORDER_LIST = URL+"order-list";
        public static final String  DOCTOR_LIST = URL+"doctor-list";
        public static final String  CUSTOMER_ENTRY = URL+"customer-add";
        public static final String  PAYMENT_MODE = URL+"payment-mode-list";
        public static final String  ORDER_ENTRY = URL+"order-add";
        public static final String  CHANGE_PASSWORD = URL+"forget-password";
        public static final String  ORDER_DETAILS = URL+"order-details";
        public static final String  APP_VERSION = URL+"mobile-apps-version";
        public static final String  IMAGE_URL = "http://192.168.37.74/work/tdlapp/public/api/v1/";
    }
}
