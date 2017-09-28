package ba.unsa.pmf.planerputovanja;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.UUID;

import ba.unsa.pmf.planerputovanja.database.PutovanjeBaseHelper;
import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema;

public class Putovanje {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mReserved;
    private String mMjesta;
    private String mNapomena;

    public Putovanje() {
        /*mId = UUID.randomUUID();
        mDate = new Date();*/
        this(UUID.randomUUID());
    }

    public Putovanje(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {return mDate; }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isReserved() {
        return mReserved;
    }

    public void setReserved(boolean reserved) {
        mReserved = reserved;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getMjesta() {
        return mMjesta;
    }

    public void setMjesta(String mjesta) {
        mMjesta = mjesta;
    }

    public String getNapomena() {
        return mNapomena;
    }

    public void setNapomena(String napomena) {
        mNapomena = napomena;
    }
}
