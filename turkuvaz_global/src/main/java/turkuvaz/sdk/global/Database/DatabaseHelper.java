package turkuvaz.sdk.global.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import turkuvaz.sdk.models.Models.Database.GalleryCategory;


/**
 * Created by turgay.ulutas on 12/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "category_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GalleryCategory.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GalleryCategory.TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_DATA, data);
        long id = db.insert(GalleryCategory.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public GalleryCategory getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GalleryCategory.TABLE_NAME,
                new String[]{GalleryCategory.COLUMN_ID, GalleryCategory.COLUMN_DATA},
                GalleryCategory.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        GalleryCategory galleryCategory = new GalleryCategory(cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));
        cursor.close();

        return galleryCategory;
    }

    public List<GalleryCategory> getAllNotes() {
        List<GalleryCategory> galleryCategories = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GalleryCategory.TABLE_NAME + " ORDER BY " + GalleryCategory.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GalleryCategory galleryCategory = new GalleryCategory();
                galleryCategory.setId(cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)));
                galleryCategory.setData(cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));

                galleryCategories.add(galleryCategory);
            } while (cursor.moveToNext());
        }

        db.close();
        return galleryCategories;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + GalleryCategory.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateNote(GalleryCategory galleryCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_DATA, galleryCategory.getData());

        return db.update(GalleryCategory.TABLE_NAME, values, GalleryCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(galleryCategory.getId())});
    }

    public void deleteNote(GalleryCategory galleryCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GalleryCategory.TABLE_NAME, GalleryCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(galleryCategory.getId())});
        db.close();
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GalleryCategory.TABLE_NAME, null, null);
        db.close();
    }

    public void setVideoGalleryCategory(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_ID, "videoCategoryList");
        values.put(GalleryCategory.COLUMN_DATA, data);
        db.insert(GalleryCategory.TABLE_NAME, null, values);
        db.close();
    }

    public void setProgramsCategory(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_ID, "programsCategoryList");
        values.put(GalleryCategory.COLUMN_DATA, data);
        db.insert(GalleryCategory.TABLE_NAME, null, values);
        db.close();
    }

    public void setTvSeriesCategory(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_ID, "listTvSeries");
        values.put(GalleryCategory.COLUMN_DATA, data);
        db.insert(GalleryCategory.TABLE_NAME, null, values);
        db.close();
    }

    public String getVideoGalleryCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GalleryCategory.TABLE_NAME,
                new String[]{GalleryCategory.COLUMN_ID, GalleryCategory.COLUMN_DATA},
                GalleryCategory.COLUMN_ID + "=?", new String[]{"videoCategoryList"},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        GalleryCategory galleryCategory = new GalleryCategory(
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));

        cursor.close();

        if (galleryCategory != null && galleryCategory.getData() != null)
            return galleryCategory.getData();
        else
            return null;
    }

    public String getProgramsCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GalleryCategory.TABLE_NAME,
                new String[]{GalleryCategory.COLUMN_ID, GalleryCategory.COLUMN_DATA},
                GalleryCategory.COLUMN_ID + "=?", new String[]{"programsCategoryList"},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        GalleryCategory galleryCategory = new GalleryCategory(
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));

        cursor.close();

        if (galleryCategory != null && galleryCategory.getData() != null)
            return galleryCategory.getData();
        else
            return null;
    }

    public String getTvSeriesCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GalleryCategory.TABLE_NAME,
                new String[]{GalleryCategory.COLUMN_ID, GalleryCategory.COLUMN_DATA},
                GalleryCategory.COLUMN_ID + "=?", new String[]{"listTvSeries"},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        GalleryCategory galleryCategory = new GalleryCategory(
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));

        cursor.close();

        if (galleryCategory != null && galleryCategory.getData() != null)
            return galleryCategory.getData();
        else
            return null;
    }

    public void setPhotoGalleryCategory(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GalleryCategory.COLUMN_ID, "photoCategoryList");
        values.put(GalleryCategory.COLUMN_DATA, data);
        db.insert(GalleryCategory.TABLE_NAME, null, values);
        db.close();
    }

    public String getPhotoGalleryCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GalleryCategory.TABLE_NAME,
                new String[]{GalleryCategory.COLUMN_ID, GalleryCategory.COLUMN_DATA},
                GalleryCategory.COLUMN_ID + "=?", new String[]{"photoCategoryList"},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        GalleryCategory galleryCategory = new GalleryCategory(
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(GalleryCategory.COLUMN_DATA)));

        cursor.close();

        if (galleryCategory != null && galleryCategory.getData() != null)
            return galleryCategory.getData();
        else
            return null;
    }
}