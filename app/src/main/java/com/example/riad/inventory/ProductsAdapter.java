package com.example.riad.inventory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context context = null;
    private ArrayList<Product> productArrayList = null;
    private static RecyclerViewClickListener itemClickListener = null;

    public ProductsAdapter(Context context, ArrayList<Product> productArrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.itemClickListener = recyclerViewClickListener;
    }

    public interface RecyclerViewClickListener {
        public void recyclerViewItemClick(View view, int position);

        public void itemButtonClick(View view, int position);
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.itemNameTxtView.setText(product.getProductName());
        holder.itemQuantityTxtView.setText(String.valueOf(product.getProductQuantity()));
        holder.itemPriceTxtView.setText(String.valueOf(product.getProductPrice()));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemNameTxtView;
        TextView itemQuantityTxtView;
        TextView itemPriceTxtView;
        Button itemSaleBtn;

        public ProductsViewHolder(View itemView) {
            super(itemView);
            itemNameTxtView = (TextView) itemView.findViewById(R.id.itemNameTxtView);
            itemQuantityTxtView = (TextView) itemView.findViewById(R.id.itemQuantityTxtView);
            itemPriceTxtView = (TextView) itemView.findViewById(R.id.itemPriceTxtView);
            itemSaleBtn = (Button) itemView.findViewById(R.id.itemSaleBtn);
            itemView.setOnClickListener(this);
            itemSaleBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == itemSaleBtn.getId()) {
                itemClickListener.itemButtonClick(view, getLayoutPosition());
            } else {
                itemClickListener.recyclerViewItemClick(view, getLayoutPosition());
            }
        }
    }

}
