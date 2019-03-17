package talabaty.swever.com.online.Cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CartDatabase extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Online";

    private static final String TABLE_NAME = "TABLE_Local";

    private static final String UID = "id";

    private static final String NAME = "name";

    private static final String PROUCT_IMAGE = "image";

    private static final String PROUCT_COLOR = "color";

    private static final String PROUCT_SIZE = "size";

    private static final String PROUCT_AMOUNT = "amount";

    private static final String PROUCT_STATE = "state";

    private static final String PROUCT_PRICE = "price";

    private static final String COLOR_val = "colorval";

    private static final String SIZE_val = "sizeval";

    private static final String CONTACT_NAME = "contactname";

    private static final String ADDRESS = "address";

    private static final String PRODUCT_ID = "productid";

    private static final String PROUCT_IMAGE_ID = "imageid";

    private static final String ISOFFER = "isoffer";

    private static final int DATABASE_VERSION = 2;
    Context cont;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " +TABLE_NAME +
            "( "+UID+" integer primary key , "
            +NAME+" varchar(255) not null, "
            +PROUCT_IMAGE+" varchar(255) , "
            +PROUCT_COLOR+" varchar(20) , "
            +PROUCT_SIZE+" varchar(20) , "
            +PROUCT_AMOUNT+" varchar(255) not null, "
            +PROUCT_STATE+" varchar(255), "
            +PROUCT_PRICE+" varchar(255) not null , "
            +COLOR_val+" varchar(20) , "
            +SIZE_val+" varchar(20), "
            +CONTACT_NAME+" varchar(225), "
            +ADDRESS+" varchar(225) not null, "
            +PRODUCT_ID+" varchar(225) not null, "
            +PROUCT_IMAGE_ID+" varchar(225) , "
            +ISOFFER+" varchar(225) not null );";

    // Database Deletion
    private static final String DATABASE_DROP = "drop table if exists "+TABLE_NAME+";";

    public CartDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cont = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            Toast.makeText(cont,"تم إنشاء سله تسوق", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            Toast.makeText(cont,"database doesn't created " +e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DATABASE_DROP);
            onCreate(db);
            Toast.makeText(cont,"تم تحديث سله التسوق", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            Toast.makeText(cont,"database doesn't upgraded " +e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public long InsertData (String name,
                               String image,
                               String imageid,
                               String COLOR,
                               String size,
                               String amount,
                               String state,
                               String price,
                               String COLORval,
                               String sizeval,
                               String contact,
                               String add,
                               String id,
                               String isOffer)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(PROUCT_IMAGE,image);
        contentValues.put(PROUCT_IMAGE_ID,imageid);
        contentValues.put(PROUCT_COLOR,COLOR);
        contentValues.put(PROUCT_SIZE,size);
        contentValues.put(PROUCT_AMOUNT,amount);
        contentValues.put(PROUCT_STATE,state);
        contentValues.put(PROUCT_PRICE,price);
        contentValues.put(COLOR_val,COLORval);
        contentValues.put(SIZE_val,sizeval);
        contentValues.put(CONTACT_NAME,contact);
        contentValues.put(ADDRESS,add);
        contentValues.put(PRODUCT_ID,id);
        contentValues.put(ISOFFER,isOffer);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        return result;
    }

    public Cursor ShowData ()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" ;",null);
        return cursor;
    }

    public Cursor getID ( String id )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select "+UID+" from "+TABLE_NAME+" where productid = "+id+" ;",null);
        return cursor;
    }

    public boolean UpdateData (String id, String name, String pass , String PROUCT_COLO ) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(NAME,name);
        contentValues.put(PROUCT_IMAGE,pass);
        contentValues.put(PROUCT_COLOR,PROUCT_COLO);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = "+Integer.parseInt( id ),null);

        return true;
    }

    public boolean UpdateData (String id, String lang, String PROUCT_STATE ,int c) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(PROUCT_STATE,PROUCT_STATE);
        contentValues.put(PROUCT_AMOUNT,lang);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});

        return true;
    }

    public int DeleteData (String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,"id = ?",new String[] {id});
    }
    public void DeleteData ()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+ TABLE_NAME);
    }
}
