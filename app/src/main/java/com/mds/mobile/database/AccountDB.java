package com.mds.mobile.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Objects;

public class AccountDB extends MasterDB {

    public static final String TAG          = "AccountDB";
    public static final String TABLE_NAME   = "AccountDB";

    public static final String CODE = "_user_code";
    public static final String USERID = "_user_id";
    public static final String NAME = "_name";
    public static final String ROLE = "_user_role";
    public static final String USERNAME = "_username";
    public static final String PASSWORD = "_password";
    public static final String CLIENT_NAME = "_client_name";

    public String code = "";
    public String userId = "";
    public String name = "";
    public String role = "";
    public String username = "";
    public String password = "";
    public String clientName = "";

    public String getCreateTable() {
        String create = "create table " + TABLE_NAME + " "
                + "(" +
                " " + CODE + " varchar(100) NULL," +
                " " + USERID + " varchar(100) NULL," +
                " " + NAME + " varchar(100) NULL," +
                " " + USERNAME + " varchar(20) NULL," +
                " " + PASSWORD + " varchar(100) NULL," +
                " " + CLIENT_NAME + " varchar(100) NULL," +
                " " + ROLE + " varchar(100) NULL" +
                "  )";
        Log.d(TAG,create);
        return create;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @SuppressLint("Range")
    @Override
    protected AccountDB build(Cursor res) {
        AccountDB jp = new AccountDB();
        jp.code = res.getString(res.getColumnIndex(CODE));
        jp.userId = res.getString(res.getColumnIndex(USERID));
        jp.username = res.getString(res.getColumnIndex(USERNAME));
        jp.password = res.getString(res.getColumnIndex(PASSWORD));
        jp.role = res.getString(res.getColumnIndex(ROLE));
        jp.name = res.getString(res.getColumnIndex(NAME));
        jp.clientName = res.getString(res.getColumnIndex(CLIENT_NAME));
        return jp;
    }

    @SuppressLint("Range")
    @Override
    protected void buildSingle(Cursor res) {
        this.code = res.getString(res.getColumnIndex(CODE));
        this.userId = res.getString(res.getColumnIndex(USERID));
        this.username = res.getString(res.getColumnIndex(USERNAME));
        this.password = res.getString(res.getColumnIndex(PASSWORD));
        this.role = res.getString(res.getColumnIndex(ROLE));
        this.name = res.getString(res.getColumnIndex(NAME));
        this.clientName = res.getString(res.getColumnIndex(CLIENT_NAME));
    }

    public ContentValues createContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CODE, code);
        contentValues.put(USERID, userId);
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(ROLE, role);
        contentValues.put(NAME, name);
        contentValues.put(CLIENT_NAME, clientName);
        return contentValues;
    }


    public void delete(Context context, String code) {
        super.delete(context, CODE +"='"+code+"'");
    }

    @Override
    public boolean insert(Context context) {
        delete(context,code);
        return super.insert(context);
    }



    public void loadData(Context context){

        DatabaseManager pDB = new DatabaseManager(context);
        SQLiteDatabase db = pDB.getReadableDatabase();
        Cursor res = db.rawQuery("select *  from " + TABLE_NAME , null);
        try {
            while (res.moveToNext()){
                buildSingle(res);
            }
            pDB.close();
        }catch (Exception e){
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        finally {
            res.close();
            pDB.close();
        }

    }

}
