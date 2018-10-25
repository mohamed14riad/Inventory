package com.example.riad.inventory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEDtText = null, quantityEDtText = null, priceEDtText = null, supplierEDtText = null;
    private ImageView pickedImageView = null;
    private Button pickImageBtn = null, saveBtn = null;

    private Intent galleryIntent = null;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private static final int RESULT_LOAD_IMAGE = 111;
    //private String imageDecodableString;
    private Uri selectedImage = null;

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEDtText = (EditText) findViewById(R.id.nameEDtText);
        quantityEDtText = (EditText) findViewById(R.id.quantityEDtText);
        priceEDtText = (EditText) findViewById(R.id.priceEDtText);
        supplierEDtText = (EditText) findViewById(R.id.supplierEDtText);

        pickedImageView = (ImageView) findViewById(R.id.pickedImageView);

        pickImageBtn = (Button) findViewById(R.id.pickImageBtn);
        pickImageBtn.setOnClickListener(this);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickImageBtn:
                galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (ContextCompat.checkSelfPermission(InsertActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InsertActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
                break;
            case R.id.saveBtn:
                Product product = new Product();
                product.setProductName(nameEDtText.getText().toString());
                product.setProductQuantity(Integer.parseInt(quantityEDtText.getText().toString()));
                product.setProductPrice(Integer.parseInt(priceEDtText.getText().toString()));
                //product.setProductImage(imageDecodableString);
                product.setProductImage(selectedImage.toString());
                product.setSupplierMail(supplierEDtText.getText().toString());
                if (databaseHelper.checkProductFound(selectedImage.toString())) {
                    Toast.makeText(getBaseContext(), "Product already exists in the Database.", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.insertProduct(product);
                    Toast.makeText(getBaseContext(), "Product Inserted Successfully.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                selectedImage = data.getData();
                /*String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageDecodableString = cursor.getString(columnIndex);
                cursor.close();
                pickedImageview.setImageBitmap(BitmapFactory.decodeFile(imageDecodableString));*/
                pickedImageView.setImageURI(selectedImage);
            } else {
                Toast.makeText(this, "You haven't picked Image.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                } else {
                    Toast.makeText(getBaseContext(), "You can't pick image without permission.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
