package com.teamfrugal.budgetapp.database;

/* This is the DAO
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.teamfrugal.budgetapp.database.SQLiteHelper.COLUMN_transID;

public class DataAccess {
    private static SQLiteDatabase database;
    private SQLiteHelper helper;
    private static Cursor cc;
    private String[] allColumns = { COLUMN_transID,
            SQLiteHelper.COLUMN_name, SQLiteHelper.COLUMN_amount, SQLiteHelper.COLUMN_account,
            SQLiteHelper.COLUMN_category, SQLiteHelper.COLUMN_type, SQLiteHelper.COLUMN_datetime
    };

    public DataAccess (Context context){
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Transaction newTransact(String name, Double amount, String account, String category,
                                   String type, String date){
        Transaction t = new Transaction(name, amount, account, category, type, date);

        return t;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    // grabbing all items?
    public Cursor query() {
        System.out.println(database.rawQuery("SELECT * FROM transactionA ORDER BY " + COLUMN_transID + " DESC ",null));
        return database.rawQuery("SELECT * FROM transactionA ORDER BY " + COLUMN_transID + " DESC",null);
    }
    public void drop() {
        //drops table of the database and creates new one
        database.execSQL("DROP TABLE if exists " + SQLiteHelper.TABLE_TRANSACTION);
        database.execSQL(SQLiteHelper.CREATE_DB);
    }

    public void insertTest() {

        //final String SQL_ADD = "INSERT INTO transactionA Values (" + 0 + ", 'Deposit', '" + 1000
        //        + "', 'a', 'Deposit' , '0', '2017/04/28' );";
        //this.getDatabase().execSQL(SQL_ADD);

        ContentValues test = new ContentValues();
        test.put(SQLiteHelper.COLUMN_transID,DataAccess.nextId());
        test.put(SQLiteHelper.COLUMN_name, "Starbucks");
        test.put(SQLiteHelper.COLUMN_amount, 20.0);
        test.put(SQLiteHelper.COLUMN_account, "a");
        test.put(SQLiteHelper.COLUMN_category, "Entertainment");
        test.put(SQLiteHelper.COLUMN_type, "expense");
        test.put(SQLiteHelper.COLUMN_datetime, "2017/05/01");
        //final String SQL_ADD = "INSERT INTO transactionA Values (" + 1 + ", 'Starbucks', '" + 20
        //        + "', 'a', 'Entertainment' , 'expense', '2017/05/01' );";
        //this.getDatabase().execSQL(SQL_ADD);
        this.getDatabase().insert(SQLiteHelper.TABLE_TRANSACTION, null, test);
        System.out.println("WE INSERTED IT :D");
    }

    public static int nextId() {
        //String stmt = "SELECT '" + COLUMN_transID + "' FROM transactionA";
        cc = database.rawQuery(SQLiteHelper.LAST_ID, null);

        int id = 1;
        if (cc.moveToFirst())
        {
            id = cc.getInt(0);
            id+=1;
        }
            /*
        int id = 0;
        if (cc.moveToFirst())
        {
            do
            {
                int tmp = cc.getInt(0);
                if (tmp > id) {
                    id = tmp;
                }
                //id = cc.getInt(0);
                System.out.println("iddd::::" + id);
            } while(cc.moveToNext());
        }
        System.out.println("idb: " + id);
        System.out.println("ida: " + (id+1));
        */
        return id;
    }
}