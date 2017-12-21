package com.example.pc.mmsr_reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pc.mmsr_reader.Class.Language;
import com.example.pc.mmsr_reader.Class.Page;
import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.Class.Storybook;

import java.util.ArrayList;

/**
 * Created by pc on 10/29/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    static ArrayList<Storybook> storybooks;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mmsr.db";

    // Tables name
    private static final String STORYBOOK_TABLE = "storybook";
    private static final String PAGE_TABLE = "page";
    private static final String READER_TABLE = "reader";
    private static final String CONTRIBUTOR_TABLE = "contributor";

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
    private static final String STORYBOOK_COVERPAGE = "coverpage";
    private static final String STORYBOOK_RATING = "rating";
    // Page Table Columns names
    private static final String PAGE_ID = "pageID";
    private static final String PAGE_LANGUAGECODE = "languageCode";
    private static final String PAGE_PAGENO = "pageNo";
    private static final String PAGE_MEDIA = "Media";
    private static final String PAGE_CONTENT = "content";
    private static final String PAGE_WORDCOUNT = "WordCount";

    // Reader Table Columns names
    private static final String READER_NAME = "userName";
    private static final String READER_EMAIL = "email";
    private static final String READER_DOB = "userDOB";
    private static final String READER_POINTS = "points";

    // Contributor Table Columns names
    private static final String CONTRIBUTOR_NAME = "name";
    private static final String CONTRIBUTOR_EMAIL = "email";
    private static final String CONTRIBUTOR_DOB = "dob";
    private static final String CONTRIBUTOR_LANGUAGECODE = "languageCode";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
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
                + STORYBOOK_AGEGROUPCODE + " TEXT,"
                + STORYBOOK_COVERPAGE + " BLOB,"
                + STORYBOOK_RATING + " INTEGER"+ ")";
        db.execSQL(CREATE_STORYBOOK_TABLE);

        String CREATE_PAGE_TABLE = "CREATE TABLE " + PAGE_TABLE + "("
                + PAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PAGE_LANGUAGECODE + " TEXT,"
                + PAGE_PAGENO + " INTEGER,"
                + PAGE_MEDIA + " BLOB,"
                + PAGE_CONTENT + " TEXT,"
                + PAGE_WORDCOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_PAGE_TABLE);

        String CREATE_READER_TABLE = "CREATE TABLE " + READER_TABLE + "("
                + READER_NAME + " TEXT,"
                + READER_EMAIL + " TEXT,"
                + READER_DOB + " TEXT,"
                + READER_POINTS + " TEXT" + ")";
        db.execSQL(CREATE_READER_TABLE);

        String CREATE_CONTRIBUTOR_TABLE = "CREATE TABLE " + CONTRIBUTOR_TABLE + "("
                + CONTRIBUTOR_NAME + " TEXT,"
                + CONTRIBUTOR_EMAIL + " TEXT,"
                + CONTRIBUTOR_DOB + " TEXT,"
                + CONTRIBUTOR_LANGUAGECODE + " TEXT" + ")";
        db.execSQL(CREATE_CONTRIBUTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + STORYBOOK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONTRIBUTOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + READER_TABLE);
        onCreate(db);
    }

    boolean addStorybook(Storybook storybook) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORYBOOK_STORYBOOKID, storybook.getStorybookID());
        values.put(STORYBOOK_LANGUAGECODE, storybook.getLanguage());
        values.put(STORYBOOK_DATEOFCREATION, storybook.getDateOfCreation());
        values.put(STORYBOOK_READABILITY, storybook.getReadability());
        values.put(STORYBOOK_TITLE, storybook.getTitle());
        values.put(STORYBOOK_DESC, storybook.getDesc());
        values.put(STORYBOOK_TYPE, storybook.getType());
        values.put(STORYBOOK_STATUS, storybook.getStatus());
        values.put(STORYBOOK_EMAIL, storybook.getEmail());
        values.put(STORYBOOK_AGEGROUPCODE, storybook.getAgeGroup());
        values.put(STORYBOOK_COVERPAGE, storybook.getCoverPage());
        values.put(STORYBOOK_RATING, storybook.getRate());

        // Inserting Row
        long result = db.insert(STORYBOOK_TABLE, null, values);
        //   db.close(); // Closing database connection
        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }

    boolean addReaderProfile(Reader reader) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(READER_NAME, reader.getUserName());
        values.put(READER_EMAIL, reader.getEmail());
        values.put(READER_DOB, reader.getUserDOB());
        values.put(READER_POINTS, reader.getPoints());
        // Inserting Row
        long result = db.insert(READER_TABLE, null, values);
        // db.close(); // Closing database connection
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getLastStorybookID() {
        String query = "SELECT seq from sqlite_sequence where name = 'storybook'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int laststorybookID = 0;
        if (cursor.moveToFirst()) {
            do {
                laststorybookID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("seq")));
                // Log.e("heree", laststorybookID + "");
            } while (cursor.moveToNext());
        }
        // cursor.close();
        return laststorybookID;

    }

    public Reader getReaderProfile() {
        Reader reader = new Reader();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] allColumn = {
                READER_NAME,
                READER_EMAIL,
                READER_DOB,
                READER_POINTS
        };
        Cursor cursor = database.query(READER_TABLE, allColumn, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            reader.setUserName(cursor.getString(0));
            reader.setEmail(cursor.getString(1));
            reader.setUserDOB(cursor.getString(2));
            reader.setPoints(cursor.getInt(3));
            cursor.moveToNext();
        }
        return reader;
    }

    public void deleteExistingRecordInReaderTable() {
        String query = "DELETE FROM " + READER_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
    }

    public void deleteExistingRecordInStorybookTable() {
        String query = "DELETE FROM " + STORYBOOK_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
    }

    public long countExistingRecordInStorybookTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(database,STORYBOOK_TABLE);

//String query = "SELECT count(*) AS counter FROM " + STORYBOOK_TABLE;
//Cursor cursor = database.rawQuery(query, null);
//cursor.moveToFirst();
//while (!cursor.isAfterLast()) {
//  counter = cursor.getInt(0);
//  cursor.moveToNext();
//}
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
                STORYBOOK_AGEGROUPCODE,
                STORYBOOK_COVERPAGE,
                STORYBOOK_RATING
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
            storybook.setCoverPage(cursor.getBlob(10));
            storybook.setRating(cursor.getString(11));
            storybooks.add(storybook);
            cursor.moveToNext();
        }
        return storybooks;
    }

    public void updateLocalDB(ArrayList<Storybook>storybooks){
        //TODO: insert storybooks to local DB
        if(storybooks.size() > 0){
            //SQLiteDatabase database = this.getWritableDatabase();
            for (Storybook s : storybooks
                    ) {
                //Insert Stroybook to DB
                //Prepare record
                ContentValues values = new ContentValues();
                values.put(STORYBOOK_STORYBOOKID, s.getStorybookID());
                values.put(STORYBOOK_LANGUAGECODE, s.getLanguage());
                values.put(STORYBOOK_DATEOFCREATION, s.getDateOfCreation());
                values.put(STORYBOOK_READABILITY, s.getReadability());
                values.put(STORYBOOK_TITLE, s.getTitle());
                values.put(STORYBOOK_DESC, s.getDesc());
                values.put(STORYBOOK_TYPE, s.getType());
                values.put(STORYBOOK_STATUS, s.getStatus());
                values.put(STORYBOOK_EMAIL, s.getEmail());
                values.put(STORYBOOK_AGEGROUPCODE, s.getAgeGroup());
                values.put(STORYBOOK_COVERPAGE, s.getCoverPage());
                values.put(STORYBOOK_RATING, s.getRate());

                //Insert a row
                boolean b = addStorybook(s);
                //database.insert(STORYBOOK_TABLE, null, values);

            }
            //Close database connection
           // database.close();
        }
    }

    public ArrayList<Language> getUserLanguage() {
        ArrayList<Language> languages = new ArrayList<Language>();

        SQLiteDatabase database = this.getReadableDatabase();
        String[] allColumn = {
                CONTRIBUTOR_NAME,
                CONTRIBUTOR_EMAIL,
                CONTRIBUTOR_DOB,
                CONTRIBUTOR_LANGUAGECODE
        };
        Cursor cursor = database.query(CONTRIBUTOR_TABLE, allColumn, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String languageCode = cursor.getString(3);
            String[] AvailableLanguage = languageCode.split(" ");
            for (int i = 0; i < AvailableLanguage.length; i++) {
                Language language = new Language();
                language.setLanguageCode(AvailableLanguage[i]);
                languages.add(language);
                Log.e("language", language.getLanguageCode());
            }
            cursor.moveToNext();
        }
        return languages;
    }
}
