package turkuvaz.sdk.global.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.Objects;

import turkuvaz.sdk.models.Models.Database.AllCategory;


/**
 * Created by turgay.ulutas on 12/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class CategoryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "allCategory_db";

    public CategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AllCategory.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AllCategory.TABLE_NAME);
        onCreate(db);
    }

    public void insertCategory(String categoryID, String nameForUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllCategory.COLUMN_ID, categoryID);
        values.put(AllCategory.COLUMN_NAME_FOR_URL, nameForUrl);
        db.insert(AllCategory.TABLE_NAME, null, values);

        db.close();
    }

    public String getCategoryNameForUrl(String id) {
        String galleryCategory = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.query(AllCategory.TABLE_NAME,
                    new String[]{AllCategory.COLUMN_ID, AllCategory.COLUMN_NAME_FOR_URL},
                    AllCategory.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst())
//                cursor.moveToFirst();
                galleryCategory = Objects.requireNonNull(cursor).getString(cursor.getColumnIndex(AllCategory.COLUMN_NAME_FOR_URL));
            Objects.requireNonNull(cursor).close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cursor != null)
                    cursor.close();
                Objects.requireNonNull(cursor).close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        return galleryCategory;
    }

    public int getCategoryCount() {
        String countQuery = "SELECT  * FROM " + AllCategory.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateCAtegory(AllCategory galleryCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AllCategory.COLUMN_NAME_FOR_URL, galleryCategory.getData());

        return db.update(AllCategory.TABLE_NAME, values, AllCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(galleryCategory.getId())});
    }

    public void deleteCategory(AllCategory galleryCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AllCategory.TABLE_NAME, AllCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(galleryCategory.getId())});
        db.close();
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AllCategory.TABLE_NAME, null, null);
        db.close();
    }
}