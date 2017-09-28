package ba.unsa.pmf.planerputovanja.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema.PutovanjeTable;

public class PutovanjeBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "putovanjeBase.db";
    public PutovanjeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PutovanjeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PutovanjeTable.Cols.UUID + ", " +
                PutovanjeTable.Cols.TITLE + ", " +
                PutovanjeTable.Cols.DATE + ", " +
                PutovanjeTable.Cols.RESERVED + ", " +
                PutovanjeTable.Cols.MJESTA + ", " +
                PutovanjeTable.Cols.NAPOMENA +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
