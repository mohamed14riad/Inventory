package com.example.riad.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.RecyclerViewClickListener {
    private DatabaseHelper databaseHelper = null;
    private ArrayList<Product> productArrayList = null;
    private RecyclerView recyclerView = null;
    private ProductsAdapter productsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseHelper = new DatabaseHelper(MainActivity.this);

        productArrayList = databaseHelper.readAllProducts();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        productsAdapter = new ProductsAdapter(MainActivity.this, productArrayList, MainActivity.this);
        recyclerView.setAdapter(productsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.insertBtn:
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void recyclerViewItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Product product = productArrayList.get(position);
        intent.putExtra("selectedProduct", product);
        startActivity(intent);
    }

    @Override
    public void itemButtonClick(View view, int position) {
        Product product = productArrayList.get(position);
        product.setProductQuantity(product.getProductQuantity() - 1);
        databaseHelper.updateProduct(product.getProductId(), product.getProductQuantity());

        productArrayList.clear();
        productArrayList = databaseHelper.readAllProducts();

        productsAdapter = null;

        productsAdapter = new ProductsAdapter(MainActivity.this, productArrayList, MainActivity.this);
        recyclerView.setAdapter(productsAdapter);
    }
}
