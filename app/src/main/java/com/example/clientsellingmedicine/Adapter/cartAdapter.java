package com.example.clientsellingmedicine.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.clientsellingmedicine.R;
import com.example.clientsellingmedicine.interfaces.IOnCartItemListener;
import com.example.clientsellingmedicine.DTO.CartItemDTO;
import com.example.clientsellingmedicine.DTO.Total;
import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    private IOnCartItemListener onCheckboxChangedListener;

    private Context mContext;
    private  List<CartItemDTO> listCartItems;
    private  List<CartItemDTO> listCartItemsChecked ;

   // private boolean isAllSelected = false;

    public cartAdapter(List<CartItemDTO> listCartItems, List<CartItemDTO> listCartItemsChecked, IOnCartItemListener mListener) {
        this.listCartItems = listCartItems;
        this.listCartItemsChecked = listCartItemsChecked;
        this.onCheckboxChangedListener = mListener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCartItem, tvPriceCartItem, tvQuantityCartItem,tv_MinusCartItem, tv_PlusCartItem;
        public ImageView ivCartItem;

        public CheckBox checkboxCartItem;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvNameCartItem = itemView.findViewById(R.id.tvNameCartItem);
            tvPriceCartItem = itemView.findViewById(R.id.tvPriceCartItem);
            ivCartItem = itemView.findViewById(R.id.ivCartItem);
            checkboxCartItem = itemView.findViewById(R.id.checkboxCartItem);
            this.setIsRecyclable(false);
            tvQuantityCartItem = itemView.findViewById(R.id.tvQuantityCartItem);
            tv_MinusCartItem = itemView.findViewById(R.id.tv_MinusCartItem);
            tv_PlusCartItem = itemView.findViewById(R.id.tv_PlusCartItem);

            mContext = context;

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsview = inflater.inflate(R.layout.cart_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(newsview, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, int position) {
        CartItemDTO cart = listCartItems.get(position);
        if (cart == null) {
            return;
        }
        holder.tvNameCartItem.setText(cart.getProduct().getName());
        int quantity = cart.getQuantity();
        holder.tvQuantityCartItem.setText(String.valueOf(quantity));
        String price = Convert.convertPrice(cart.getProduct().getPrice());
        holder.tvPriceCartItem.setText(price);

        // minus quantity
        holder.tv_MinusCartItem.setOnClickListener(v -> {
            if (quantity > 1) {
                updateQuantityCartItem(holder.itemView.getContext(), cart, -1);   // update on recycler view and shared preferences
            }
        });
        // plus quantity
        holder.tv_PlusCartItem.setOnClickListener(v -> {
            updateQuantityCartItem(holder.itemView.getContext(), cart, 1);  // update on recycler view and shared preferences
        });

        //checked cart item
        boolean exists = listCartItemsChecked.stream().anyMatch(item -> item.getProduct().equals(cart.getProduct()));
        if(exists)
            holder.checkboxCartItem.setChecked(true);


        holder.checkboxCartItem.setOnClickListener(v -> {
            if (holder.checkboxCartItem.isChecked()) {
                listCartItemsChecked.add(cart);
            }
            else {
                Iterator<CartItemDTO> iterator = listCartItemsChecked.iterator();
                while (iterator.hasNext()) {
                    CartItemDTO cartItem = iterator.next();
                    if (cartItem.getProduct().equals(cart.getProduct())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            // Save CartItems Checked to SharedPreferences
            SharedPref.saveData(holder.itemView.getContext(), listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

            updateUIAfterMasterCheckboxChanged();
        });


        updateUIAfterMasterCheckboxChanged();

        //load image
        Glide.with(holder.itemView.getContext())
                .load(cart.getProduct().getImage())
                .placeholder(R.drawable.loading_icon) // Hình ảnh thay thế khi đang tải
                .error(R.drawable.error_image) // Hình ảnh thay thế khi có lỗi
                .into(holder.ivCartItem);
    }

    @Override
    public int getItemCount() {
        return listCartItems.size();
    }


    public void setAllSelected(boolean selected) {
        if(listCartItems.size() == 0)
            return;
        handleCheckBoxSelectedAll( selected);

    }

    // update listCartItemsChecked when user click on checkbox selected all
    public void handleCheckBoxSelectedAll( Boolean isAllSelected) {
        // Clear the current list of checked items
        this.listCartItemsChecked.clear();

        // If checkbox select all items is selected, add all cart items  to 'list Cart Items Checked'
        if (isAllSelected) {
            this.listCartItemsChecked.addAll(listCartItems);
        }

        // Save the updated list of checked items to SharedPreferences
        SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }

    public void removeItems(CartItemDTO cartItem) {
        // Remove items from list Cart Items
        listCartItems.remove(cartItem);
        // Remove item from CartItems Checked
        listCartItemsChecked.remove(cartItem);
        // Save CartItems Checked to SharedPreferences
        SharedPref.saveData(mContext, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);

        updateUIAfterMasterCheckboxChanged();

        notifyDataSetChanged();

    }

    public Total calculateTotalAmount() {
        Total totalItem = new Total(0,0);
        int total = 0, totalProductDiscount = 0;
        if(listCartItemsChecked == null)
            return totalItem;
        //Log.d("tag", "listCartItemsChecked: "+listCartItemsChecked.size());
        for (CartItemDTO item: listCartItemsChecked) {
            total += item.getProduct().getPrice() * item.getQuantity();
            int discountPercent = item.getProduct().getDiscountPercent();
            int price = item.getProduct().getPrice()*item.getQuantity();
            totalProductDiscount+= (price * discountPercent) / 100;
        }
        totalItem.setTotalPrice(total);
        totalItem.setTotalProductDiscount(totalProductDiscount);
        return totalItem;
    }



    public void updateQuantityCartItem(Context context, CartItemDTO item, int quantity) {
        int oldQuantity = item.getQuantity();
        int newQuantity = oldQuantity + quantity;
        item.setQuantity(newQuantity);
        notifyDataSetChanged();
        // Save CartItems Checked to SharedPreferences
        for (CartItemDTO itemChecked : listCartItemsChecked) {
            if (itemChecked.getProduct().equals(item.getProduct())) {
                itemChecked.setQuantity(newQuantity);
                SharedPref.saveData(context, listCartItemsChecked, Constants.CART_PREFS_NAME, Constants.KEY_CART_ITEMS_CHECKED);
                break;
            }
        }
        // get Total Amount Item Checked
        onCheckboxChangedListener.getTotal(calculateTotalAmount());
        //  update on database
        CartItem cart = new CartItem();
        cart.setQuantity(item.getQuantity());
        cart.setId_product(item.getProduct().getId());
        onCheckboxChangedListener.updateCartItemQuantity(cart);
    }

    private void updateUIAfterMasterCheckboxChanged() {
        // Set value for master checkbox
        boolean allItemsChecked = listCartItemsChecked.size() == listCartItems.size() && !listCartItems.isEmpty();
        onCheckboxChangedListener.setValueOfMasterCheckbox(allItemsChecked);

        // Set status for delete items text
        onCheckboxChangedListener.setStatusOfDeleteText(!listCartItemsChecked.isEmpty());

        // Get Total Amount Item Checked
        onCheckboxChangedListener.getTotal(calculateTotalAmount());
    }
}
