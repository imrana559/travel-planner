package ba.unsa.pmf.planerputovanja.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ba.unsa.pmf.planerputovanja.Putovanje;
import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema.PutovanjeTable;

public class PutovanjeCursorWrapper extends CursorWrapper {
    public PutovanjeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Putovanje getPutovanje() {
        String uuidString = getString(getColumnIndex(PutovanjeTable.Cols.UUID));
        String title = getString(getColumnIndex(PutovanjeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(PutovanjeTable.Cols.DATE));
        int isReserved = getInt(getColumnIndex(PutovanjeTable.Cols.RESERVED));
        String mjesta = getString(getColumnIndex(PutovanjeTable.Cols.MJESTA));
        String napomena = getString(getColumnIndex(PutovanjeTable.Cols.NAPOMENA));

        Putovanje putovanje = new Putovanje(UUID.fromString(uuidString));
        putovanje.setTitle(title);
        putovanje.setDate(new Date(date));
        putovanje.setReserved(isReserved != 0);
        putovanje.setMjesta(mjesta);
        putovanje.setNapomena(napomena);

        return putovanje;
    }
}
