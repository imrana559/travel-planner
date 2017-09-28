package ba.unsa.pmf.planerputovanja.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class GalerijaBaseHelper extends SQLiteOpenHelper{
    public GalerijaBaseHelper(Context context, String ime, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, ime, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String ime, String opis, byte[] slika){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO GALERIJA VALUES (NULL, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, ime);
        statement.bindString(2, opis);
        statement.bindBlob(3, slika);

        statement.executeInsert();
    }

    public void updateData(String ime, String opis, byte[] slika, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE GALERIJA SET ime = ?, opis = ?, slika = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, ime);
        statement.bindString(2, opis);
        statement.bindBlob(3, slika);
        statement.bindDouble(4, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM GALERIJA WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
