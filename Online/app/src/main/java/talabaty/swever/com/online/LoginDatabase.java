package talabaty.swever.com.online;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import talabaty.swever.com.online.Utils.AppToastUtil;

public class LoginDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Admin";

    private static final String TABLE_NAME = "TABLE_Local";

    private static final String UID = "id";

    private static final String NAME = "name";

    private static final String USER_ID = "userId";

    private static final String PHONE = "phone";

    private static final String TYPE = "type";

    private static final String IMAGE = "image"; //http://www.selltlbaty.sweverteam.com/

    private static final String AccountType = "account_type";

    private static final String Mail = "mail";

    private static final int DATABASE_VERSION = 2;
    Context mContext;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME +
            "( " + UID + " integer primary key , "
            + NAME + " varchar(255) not null, "
            + USER_ID + " varchar(255) , "
            + PHONE + " varchar(20) , "
            + TYPE + " varchar(255) not null, "
            + IMAGE + " varchar(255), "
            + AccountType + " varchar(255) , "
            + Mail + " varchar(255) );";

    // Database Deletion
    private static final String DATABASE_DROP = "drop table if exists " + TABLE_NAME + ";";

    public LoginDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            db.execSQL("insert into " + TABLE_NAME + " ( " + UID + ", " + NAME + ", " + USER_ID + ", " + PHONE + ", " + TYPE + "," + IMAGE + "," + AccountType + "," + Mail + ") values ( '1', 'e', '0', '0', '0', '0', '0', '0');");
        } catch (SQLException e) {
            AppToastUtil.showErrorToast("database doesn't created " + e.toString(),
                    AppToastUtil.LENGTH_LONG, mContext);
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
            AppToastUtil.showErrorToast("database doesn't upgraded " + e.toString(),
                    AppToastUtil.LENGTH_LONG, mContext);
        }
    }

    public boolean InsertData(String name, String userid, String phone, String type, String image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(USER_ID, userid);
        contentValues.put(PHONE, phone);
        contentValues.put(TYPE, type);
        contentValues.put(IMAGE, image);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        return result == -1 ? false : true;
    }

    public Cursor ShowData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " ;", null);
        return cursor;
    }

    public boolean UpdateData(String id, String name, String userid, String phone, String type, String image, String AccountTyp, String Mal) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, id);
        contentValues.put(NAME, name);
        contentValues.put(USER_ID, userid);
        contentValues.put(PHONE, phone);
        contentValues.put(TYPE, type);
        contentValues.put(IMAGE, image);
        contentValues.put(AccountType, AccountTyp);
        contentValues.put(Mail, Mal);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "id = " + Integer.parseInt(id), null);

        return true;
    }

    public boolean UpdateData(String id,
                              String AccountTyp) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID, id);
        contentValues.put(AccountType, AccountTyp);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "id = " + Integer.parseInt(id), null);

        return true;
    }

//    public boolean UpdateState(String id, String PROUCT_STATE)
//    {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UID,id);
//        contentValues.put(State, PROUCT_STATE);
//        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});
//
//        return true;
//    }

    public int DeleteData(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
