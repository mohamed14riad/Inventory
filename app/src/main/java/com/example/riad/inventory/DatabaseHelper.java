package com.example.riad.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context = null;

    public DatabaseHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.Table.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL(Contract.Table.DELETE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public void insertProduct(Product product) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Table.COLUMN_NAME, product.getProductName());
        contentValues.put(Contract.Table.COLUMN_QUANTITY, product.getProductQuantity());
        contentValues.put(Contract.Table.COLUMN_PRICE, product.getProductPrice());
        contentValues.put(Contract.Table.COLUMN_IMAGE, product.getProductImage());
        contentValues.put(Contract.Table.COLUMN_SUPPLIER_MAIL, product.getSupplierMail());

        sqLiteDatabase.insert(Contract.Table.TABLE_PRODUCTS, null, contentValues);
        sqLiteDatabase.close();
    }

    private Cursor read() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String[] columns = {Contract.Table.COLUMN_ID, Contract.Table.COLUMN_NAME, Contract.Table.COLUMN_QUANTITY,
                Contract.Table.COLUMN_PRICE, Contract.Table.COLUMN_IMAGE, Contract.Table.COLUMN_SUPPLIER_MAIL};

        Cursor cursor = sqLiteDatabase.query(Contract.Table.TABLE_PRODUCTS, columns, null, null, null, null, null);

        return cursor;
    }

    public ArrayList<Product> readAllProducts() {
        ArrayList<Product> productsList = new ArrayList<Product>();

        Cursor cursor = read();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId(cursor.getInt(0));
                product.setProductName(cursor.getString(1));
                product.setProductQuantity(cursor.getInt(2));
                product.setProductPrice(cursor.getInt(3));
                product.setProductImage(cursor.getString(4));
                product.setSupplierMail(cursor.getString(5));
                productsList.add(product);
            } while (cursor.moveToNext());
        }

        return productsList;
    }

    public void updateProduct(int id, int quantity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Table.COLUMN_QUANTITY, quantity);
        sqLiteDatabase.update(Contract.Table.TABLE_PRODUCTS, contentValues, Contract.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.delete(Contract.Table.TABLE_PRODUCTS, Contract.Table.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public boolean checkProductFound(String image) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] columns = {Contract.Table.COLUMN_IMAGE};
        Cursor cursor = sqLiteDatabase.query(Contract.Table.TABLE_PRODUCTS, columns, null, null, null, null, null);
        ArrayList<String> imagesList = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String img = cursor.getString(0);
                imagesList.add(img);
            } while (cursor.moveToNext());
        }
        for (int i = 0; i < imagesList.size(); i++) {
            if (imagesList.get(i).equals(image)) {
                return true;
            }
        }
        return false;
    }


}
