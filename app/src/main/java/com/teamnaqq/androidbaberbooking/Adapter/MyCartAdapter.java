package com.teamnaqq.androidbaberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamnaqq.androidbaberbooking.Database.CartDatabase;
import com.teamnaqq.androidbaberbooking.Database.CartItem;
import com.teamnaqq.androidbaberbooking.Database.DatabaseUtils;
import com.teamnaqq.androidbaberbooking.Interface.ICartItemUpdateListener;
import com.teamnaqq.androidbaberbooking.R;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {
    Context context;
    List<CartItem> cartItemList;

    CartDatabase cartDatabase;
    ICartItemUpdateListener iCartItemUpdateListener;

    public MyCartAdapter(Context context, List<CartItem> cartItemList, ICartItemUpdateListener iCartItemUpdateListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.iCartItemUpdateListener = iCartItemUpdateListener;
        this.cartDatabase = CartDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(cartItemList.get(position).getProductImage()).into(holder.img_product);
        holder.txtCartName.setText(new StringBuilder(cartItemList.get(position).getProductName()));
        holder.txtCartPrice.setText(new StringBuilder("$").append(cartItemList.get(position).getProductPrice()));
        holder.txtCartQuantity.setText(new StringBuilder(String.valueOf(cartItemList.get(position).getProductQuantity())));


        holder.setListener(new IImageButtonListener() {
            @Override
            public void onImageButtonClick(View view, int pos, boolean isDecrease) {
                if (isDecrease) {
                    if (cartItemList.get(pos).getProductQuantity() > 0) {
                        cartItemList.get(pos)
                                .setProductQuantity(cartItemList.get(pos).getProductQuantity() - 1);

                        DatabaseUtils.updateCart(cartDatabase,cartItemList.get(pos));
                    }

                }
                else {
                    if (cartItemList.get(pos).getProductQuantity() < 99) {
                        cartItemList.get(pos)
                                .setProductQuantity(cartItemList.get(pos).getProductQuantity() + 1);
                        DatabaseUtils.updateCart(cartDatabase,cartItemList.get(pos));
                    }
                }
                holder.txtCartQuantity.setText(new StringBuilder(String.valueOf(cartItemList.get(position).getProductQuantity())));
                iCartItemUpdateListener.onCartItemUpdateSuccess();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    interface IImageButtonListener {
        void onImageButtonClick(View view, int pos, boolean isDecrease);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txtCartName;
        TextView txtCartPrice;
        ImageView imgDecrease;
        TextView txtCartQuantity;
        ImageView imgIncrease;
        IImageButtonListener listener;

        public void setListener(IImageButtonListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.cart_image);
            txtCartName = (TextView) itemView.findViewById(R.id.txt_cart_name);
            txtCartPrice = (TextView) itemView.findViewById(R.id.txt_cart_price);
            imgDecrease = (ImageView) itemView.findViewById(R.id.img_decrease);
            txtCartQuantity = (TextView) itemView.findViewById(R.id.txt_cart_quantity);
            imgIncrease = (ImageView) itemView.findViewById(R.id.img_increase);

            imgDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageButtonClick(v, getAdapterPosition(), true);
                }
            });
            imgIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageButtonClick(v, getAdapterPosition(), false);

                }
            });
        }
    }
}
