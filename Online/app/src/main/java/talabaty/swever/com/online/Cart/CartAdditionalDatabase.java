package talabaty.swever.com.online.Cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import talabaty.swever.com.online.Utils.AppToastUtil;

public class CartAdditionalDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Addition";

    private static final String TABLE_NAME = "TABLE_Additions";

    private static final String UID = "id";

    private static final String AID = "addition_id";

    private static final String BID = "food_id";

    private static final String NAME = "name";

    private static final String PROUCT_PRICE = "price";

    private static final int DATABASE_VERSION = 2;
    Context mContext;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME +
            "( " + UID + " integer primary key , "
            + BID + " varchar(255) not null, "
            + AID + " varchar(255) not null, "
            + NAME + " varchar(255), "
            + PROUCT_PRICE + " varchar(255) );";

    // Database Deletion
    private static final String DATABASE_DROP = "drop table if exists " + TABLE_NAME + ";";

    public CartAdditionalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            AppToastUtil.showInfoToast("تم إنشاء سله وجبات",
                    AppToastUtil.LENGTH_SHORT, mContext);
        } catch (SQLException e) {
            AppToastUtil.showErrorToast("database doesn't created " + e.toString(),
                    AppToastUtil.LENGTH_SHORT, mContext);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DATABASE_DROP);
            onCreate(db);
            AppToastUtil.showInfoToast("تم تحديث سله التسوق",
                    AppToastUtil.LENGTH_SHORT, mContext);
        } catch (SQLException e) {
            AppToastUtil.showInfoToast("database doesn't upgraded " + e.toString(),
                    AppToastUtil.LENGTH_SHORT, mContext);
        }
    }

    public boolean InsertData(String name,
                              String price,
                              String addition_id,
                              String food_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(BID, food_id);
        contentValues.put(AID, addition_id);
        contentValues.put(PROUCT_PRICE, price);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        return result == -1 ? false : true;
    }

    public Cursor ShowData(String Id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + BID + " = " + Id + " ;", null);
        return cursor;
    }

//    public boolean UpdateData (String id, String name, String pass , String PROUCT_COLO )
//    {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UID,id);
//        contentValues.put(NAME,name);
//        contentValues.put(PROUCT_IMAGE,pass);
//        contentValues.put(PROUCT_COLOR,PROUCT_COLO);
//        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = "+Integer.parseInt( id ),null);
//
//        return true;
//    }

    public int DeleteData(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "food_id = ?", new String[]{id});
    }

    public int DeleteData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME);
        return sqLiteDatabase.delete(TABLE_NAME, null, null);
    }
}
