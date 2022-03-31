package com.csi.technodrugs.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.csi.technodrugs.Model.Datagetset;


/**
 * Created by Jahid on 12/11/2018.
 */

public class SqlitieFunction extends SQLiteOpenHelper {

    public  static final String DB_Name ="product";
    public  static final String Table_Name ="productTable";
    public  static final String id = "id";
    public  static final String customerName = "customerName";
    public  static final String customerCode ="customerCode";
    public  static final String orderDate = "orderDate";
    public  static final String deliveryDate = "deliveryDate";
    public  static final String paymentMode = "paymentMode";
    public  static final String orderRef = "orderRef";
    public  static final String productName = "productName";
    public  static final String productId = "productId";
    public  static final String packSize = "packSize";
    public  static final String tradePrice = "tradePrice";
    public  static final String quantity = "quantity";
    public  static final String netAmount = "netAmount";
    public  static final String vat = "vat";
    public  static final String totalVat = "totalVat";
    public  static final String discount = "discount";
    public  static final String totalDiscount = "totalDiscount";
    public  static final String tradeValue = "tradeValue";
    public  static final String drCode = "drCode";
    public  static final int version = 5;

    public SqlitieFunction(Context c)
    {
        super(c,DB_Name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String string = "CREATE TABLE "+Table_Name+" ("+id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+customerName+" TEXT, "+customerCode+" TEXT, "+orderDate+" TEXT, "+deliveryDate+" TEXT,"+paymentMode+" TEXT,"+orderRef+" TEXT,"+productName+" TEXT,"+productId+" TEXT,"+packSize+" TEXT,"+tradePrice+" TEXT,"+quantity+" TEXT,"+netAmount+" TEXT,"+vat+" TEXT,"+totalVat+" TEXT,"+discount+" TEXT,"+totalDiscount+" TEXT,"+tradeValue+" TEXT,"+drCode+" TEXT)";
        db.execSQL(string);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void datainsert(Datagetset datagetset)
    {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(customerName,datagetset.getCustomerName());
            contentValues.put(customerCode,datagetset.getCustomerCode());
            contentValues.put(orderDate,datagetset.getOrderDate());
            contentValues.put(deliveryDate,datagetset.getDeliverDate());
            contentValues.put(paymentMode,datagetset.getPaymentMode());
            contentValues.put(orderRef,datagetset.getOrderRef());
            contentValues.put(productName,datagetset.getProductName());
            contentValues.put(productId,datagetset.getProductId());
            contentValues.put(packSize,datagetset.getPackSize());
            contentValues.put(tradePrice,datagetset.getTradePrice());
            contentValues.put(quantity,datagetset.getQuantity());
            contentValues.put(netAmount,datagetset.getNetAmount());
            contentValues.put(vat,datagetset.getVat());
            contentValues.put(totalVat,datagetset.getTotalVat());
            contentValues.put(discount,datagetset.getDiscount());
            contentValues.put(totalDiscount,datagetset.getTotalDiscount());
            contentValues.put(tradeValue,datagetset.getTradeValue());
            contentValues.put(drCode,datagetset.getDrCode());
            sqLiteDatabase.insert(Table_Name,null, contentValues);
            sqLiteDatabase.close();

    }

    public void dataDelete(String id)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("delete from "+ Table_Name +" WHERE id='"+id+"'");
        db.close();
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Table_Name);
        db.close();
    }

    public boolean isExist(String productId)
    {
        SQLiteDatabase sq=this.getReadableDatabase();
        String get="SELECT id FROM "+Table_Name+" WHERE productId='"+productId+"' LIMIT 1";
        Cursor c = sq.rawQuery(get,null);
        if(c.getCount() == 1) {
            return true;
        }
        return false;
    }


    /*public String[] getlatitude()
    {
        SQLiteDatabase sq=this.getReadableDatabase();
        String get="SELECT * FROM "+Table_Name;
        Cursor c=sq.rawQuery(get,null);
        String[]received=new String[c.getCount()];
        c.moveToFirst();
        if(c.moveToFirst())
        {
            int counter=0;
            do{
                received[counter]=c.getString(c.getColumnIndex(latitude+""));
                counter=counter+1;

            }while(c.moveToNext());
        }
        return received;
    }*/

    /*public String[] getlongititude()
    {
        SQLiteDatabase sq=this.getReadableDatabase();
        String get="SELECT * FROM "+Table_Name;
        Cursor c=sq.rawQuery(get,null);
        String[]received=new String[c.getCount()];
        c.moveToFirst();
        if(c.moveToFirst())
        {
            int counter=0;
            do{
                received[counter]=c.getString(c.getColumnIndex(longitude+""));
                counter=counter+1;

            }while(c.moveToNext());
        }
        return received;
    }*/
   /* public String[] getallinfo()
    {
        SQLiteDatabase sq=this.getReadableDatabase();
        String get="SELECT * FROM "+Table_Name;
        Cursor c=sq.rawQuery(get,null);
        String[]received=new String[c.getCount()];
        c.moveToFirst();
        if(c.moveToFirst())
        {
            int counter=0;
            do{
                received[counter]="\nName: "+c.getString(c.getColumnIndex(ship_name+""))+" \nQR CODE: "+c.getString(c.getColumnIndex(qr_code+""))
                        +" \nLatitude: "+c.getString(c.getColumnIndex(latitude+""))+" \nLongitude: "+c.getString(c.getColumnIndex(longitude+""))+"\nImage:"+c.getString(c.getColumnIndex(image+""));
                counter=counter+1;

            }while(c.moveToNext());
        }
        return received;
    }*/
}
