package com.example.pc.mmsr_reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pc.mmsr_reader.Class.Page;
import com.example.pc.mmsr_reader.Class.Storybook;

import java.util.ArrayList;

/**
 * Created by pc on 10/29/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "storybookdb";

    // Tables name
    private static final String STORYBOOK_TABLE = "storybook";
    private static final String PAGE_TABLE = "page";

    // StorybookLibrary Table Columns names
    private static final String STORYBOOK_STORYBOOKID = "storybookID";
    private static final String STORYBOOK_LANGUAGECODE = "languageCode";
    private static final String STORYBOOK_DATEOFCREATION = "dateOfCreation";
    private static final String STORYBOOK_READABILITY = "readability";
    private static final String STORYBOOK_TITLE = "title";
    private static final String STORYBOOK_DESC = "description";
    private static final String STORYBOOK_TYPE = "type";
    private static final String STORYBOOK_STATUS = "status";
    private static final String STORYBOOK_EMAIL = "email";
    private static final String STORYBOOK_AGEGROUPCODE = "agegroup";
    // Page Table Columns names
    private static final String PAGE_ID = "pageID";
    private static final String PAGE_LANGUAGECODE = "languageCode";
    private static final String PAGE_PAGENO = "pageNo";
    private static final String PAGE_MEDIA = "Media";
    private static final String PAGE_CONTENT = "content";
    private static final String PAGE_WORDCOUNT = "WordCount";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STORYBOOK_TABLE = "CREATE TABLE " + STORYBOOK_TABLE + "("
                + STORYBOOK_STORYBOOKID + " INTEGER PRIMARY KEY,"
                + STORYBOOK_LANGUAGECODE + " TEXT,"
                + STORYBOOK_DATEOFCREATION + " TEXT,"
                + STORYBOOK_READABILITY + " TEXT,"
                + STORYBOOK_TITLE + " TEXT,"
                + STORYBOOK_DESC + " TEXT,"
                + STORYBOOK_TYPE + " TEXT,"
                + STORYBOOK_STATUS + " TEXT,"
                + STORYBOOK_EMAIL + " TEXT,"
                + STORYBOOK_AGEGROUPCODE + " TEXT" + ")";
        db.execSQL(CREATE_STORYBOOK_TABLE);

        String CREATE_PAGE_TABLE = "CREATE TABLE " + PAGE_TABLE + "("
                + PAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PAGE_LANGUAGECODE + " TEXT,"
                + PAGE_PAGENO + " INTEGER,"
                + PAGE_MEDIA + " BLOB,"
                + PAGE_CONTENT + " TEXT,"
                + PAGE_WORDCOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_PAGE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + STORYBOOK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAGE_TABLE);
        onCreate(db);
    }

    boolean addStorybook(Storybook storybook) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //   values.put(STORYBOOK_STORYBOOKID, storybook.getStorybookId());
        values.put(STORYBOOK_LANGUAGECODE, storybook.getLanguage());
        //    Format formatter = new SimpleDateFormat("MM/dd/yyyy");
        //   String dateCreated = formatter.format(storybook.getDateOfCreation());
        values.put(STORYBOOK_DATEOFCREATION, storybook.getDateOfCreation());
        values.put(STORYBOOK_READABILITY, storybook.getReadability());
        values.put(STORYBOOK_TITLE, storybook.getTitle());
        values.put(STORYBOOK_DESC, storybook.getDesc());
        values.put(STORYBOOK_TYPE, storybook.getType());
        values.put(STORYBOOK_STATUS, storybook.getStatus());
        values.put(STORYBOOK_EMAIL, storybook.getEmail());
        values.put(STORYBOOK_AGEGROUPCODE, storybook.getAgeGroup());
        // Inserting Row
        long result = db.insert(STORYBOOK_TABLE, null, values);
        //   db.close(); // Closing database connection
        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }

    boolean addPage(Page page) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PAGE_LANGUAGECODE, page.getLanguageCode());
        values.put(PAGE_PAGENO, page.getPageNo());
        values.put(PAGE_MEDIA, page.getMedia());
        values.put(PAGE_CONTENT, page.getContent());
        values.put(PAGE_WORDCOUNT, page.getWordCount());
        // Inserting Row
        long result = db.insert(PAGE_TABLE, null, values);
        // db.close(); // Closing database connection
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Storybook> getAllStorybook() {
        ArrayList<Storybook> storybooks = new ArrayList<Storybook>();

        SQLiteDatabase database = this.getReadableDatabase();
        String[] allColumn = {
                STORYBOOK_STORYBOOKID,
                STORYBOOK_LANGUAGECODE,
                STORYBOOK_DATEOFCREATION,
                STORYBOOK_READABILITY,
                STORYBOOK_TITLE,
                STORYBOOK_DESC,
                STORYBOOK_TYPE,
                STORYBOOK_STATUS,
                STORYBOOK_EMAIL,
                STORYBOOK_AGEGROUPCODE
        };
        Cursor cursor = database.query(STORYBOOK_TABLE, allColumn, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Storybook storybook = new Storybook();
            storybook.setStorybookID(cursor.getString(0));
            storybook.setLanguage(cursor.getString(1));
            storybook.setDateOfCreation(cursor.getString(2));
            storybook.setReadability(cursor.getString(3));
            storybook.setTitle(cursor.getString(4));
            Log.e("Data", cursor.getString(4));
            storybook.setDesc(cursor.getString(5));
            storybook.setType(cursor.getString(6));
            storybook.setStatus(cursor.getString(7));
            storybook.setEmail(cursor.getString(8));
            storybook.setAgeGroup(cursor.getString(9));
            storybooks.add(storybook);
            cursor.moveToNext();
        }
        return storybooks;
    }
}
