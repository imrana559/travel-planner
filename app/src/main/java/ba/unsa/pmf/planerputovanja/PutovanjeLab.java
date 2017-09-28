package ba.unsa.pmf.planerputovanja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Adapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ba.unsa.pmf.planerputovanja.database.PutovanjeBaseHelper;
import ba.unsa.pmf.planerputovanja.database.PutovanjeCursorWrapper;
import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema;
import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema.PutovanjeTable;

public class PutovanjeLab {
    private static PutovanjeLab sPutovanjeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PutovanjeLab get(Context context) {
        if (sPutovanjeLab == null) {
            sPutovanjeLab = new PutovanjeLab(context);
        }
        return sPutovanjeLab;
    }

    public void addPutovanje(Putovanje c) {
            ContentValues values = getContentValues(c);
            mDatabase.insert(PutovanjeTable.NAME, null, values);
    }

    public void deletePutovanje(UUID putovanjeId)
    {
        String uuidString = putovanjeId.toString();
        mDatabase.delete(PutovanjeTable.NAME, PutovanjeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }


    private PutovanjeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PutovanjeBaseHelper(mContext).getWritableDatabase();
    }
    public List<Putovanje> getPutovanja() {
        List<Putovanje> putovanja = new ArrayList<>();
        PutovanjeCursorWrapper cursor = queryPutovanja(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                putovanja.add(cursor.getPutovanje());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return putovanja;
    }
    public Putovanje getPutovanje(UUID id) {
        PutovanjeCursorWrapper cursor = queryPutovanja(
                PutovanjeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPutovanje();
        } finally {
            cursor.close();
        }
    }



    public File getPhotoFile(Putovanje putovanje) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, putovanje.getPhotoFilename());
    }

    public void updatePutovanje(Putovanje putovanje) {
        String uuidString = putovanje.getId().toString();
        ContentValues values = getContentValues(putovanje);
        mDatabase.update(PutovanjeTable.NAME, values,
                PutovanjeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private PutovanjeCursorWrapper queryPutovanja(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PutovanjeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new PutovanjeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Putovanje putovanje) {
        ContentValues values = new ContentValues();
        values.put(PutovanjeTable.Cols.UUID, putovanje.getId().toString());
        values.put(PutovanjeTable.Cols.TITLE, putovanje.getTitle());
        values.put(PutovanjeTable.Cols.DATE, putovanje.getDate().getTime());
        values.put(PutovanjeTable.Cols.RESERVED, putovanje.isReserved() ? 1 : 0);
        values.put(PutovanjeTable.Cols.MJESTA, putovanje.getMjesta());
        values.put(PutovanjeTable.Cols.NAPOMENA, putovanje.getNapomena());
        return values;
    }

}
