package com.csi.technodrugs.Utility;

import android.util.Log;

public class MonthToText {
    public static String mothNameText(String date) {
        String value = "";
        try {
            String[] strings = date.split("-");
            int month = Integer.parseInt(strings[1]);
            String name = "";
            if (month == 1) {
                name = "1";
            } else if (month == 2) {
                name = "2";
            } else if (month == 3) {
                name = "3";
            } else if (month == 4) {
                name = "4";
            } else if (month == 5) {
                name = "5";
            } else if (month == 6) {
                name = "6";
            } else if (month == 7) {
                name = "7";
            } else if (month == 8) {
                name = "8";
            } else if (month == 9) {
                name = "9";
            } else if (month == 10) {
                name = "10";
            } else if (month == 11) {
                name = "11";
            } else if (month == 12) {
                name = "12";
            } else {
                value = "";
            }

            value = strings[0] + "-" + name + "-" + strings[2];
        } catch (Exception e) {
            Log.e("LP", "ERROR DATE PARSE");
        }

        return value;
    }

    public static String monthNameInt(String date) {

        String value = "";
        try {
            String[] strings = date.split("-");
            String month = strings[1];
            int name;

            if (month.equals("JAN")) {
                name = 1;
            } else if (month.equals("FEB")) {
                name = 2;
            } else if (month.equals("MAR")) {
                name = 3;
            } else if (month.equals("APR")) {
                name = 4;
            } else if (month.equals("MAY")) {
                name = 5;
            } else if (month.equals("JUN")) {
                name = 6;
            } else if (month.equals("JUL")) {
                name = 7;
            } else if (month.equals("AUG")) {
                name = 8;
            } else if (month.equals("SEP")) {
                name = 9;
            } else if (month.equals("OCT")) {
                name = 10;
            } else if (month.equals("NOV")) {
                name = 11;
            } else if (month.equals("DEC")) {
                name = 12;
            } else {
                name = 0;
            }

            value = strings[0] + "-" + name + "-" + strings[2];
        } catch (Exception e) {
            Log.e("LP", "PARSE ERROR");
        }

        return value;
    }
}


