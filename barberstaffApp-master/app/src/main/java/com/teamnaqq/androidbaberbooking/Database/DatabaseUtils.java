package com.teamnaqq.androidbaberbooking.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.teamnaqq.androidbaberbooking.Common.Common;
import com.teamnaqq.androidbaberbooking.Interface.ICartItemLoadListener;
import com.teamnaqq.androidbaberbooking.Interface.ICountItemInCartListener;
import com.teamnaqq.androidbaberbooking.Interface.ISumCartListener;

import java.util.List;

public class DatabaseUtils {

    public static void sumCart(CartDatabase db, ISumCartListener iSumCartListener) {
        SumCartAsync task = new SumCartAsync(db, iSumCartListener);
        task.execute();

    }

    public static void getAllCart(CartDatabase db, ICartItemLoadListener cartItemLoadListener) {
        GetAllCartAsync task = new GetAllCartAsync(db, cartItemLoadListener);
        task.execute();

    }

    public static void updateCart(CartDatabase db, CartItem cartItem) {
        UpdateCartAsync task = new UpdateCartAsync(db);
        task.execute(cartItem);
    }

    private static class UpdateCartAsync extends AsyncTask<CartItem, Void, Void> {
        private final CartDatabase db;

        public UpdateCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().update(cartItems[0]);
            return null;
        }
    }



    public static void insertToCart(CartDatabase db, CartItem... cartItems) {
        InsertToCartAsync task = new InsertToCartAsync(db);
        task.execute(cartItems);
    }

    public static void countItemInCart(CartDatabase db, ICountItemInCartListener iCountItemInCartListener) {
        CountItemInCartAsync task = new CountItemInCartAsync(db, iCountItemInCartListener);
        task.execute();
    }


    private static class GetAllCartAsync extends AsyncTask<String, Void, List<CartItem>> {
        CartDatabase db;

        ICartItemLoadListener listener;

        public GetAllCartAsync(CartDatabase cartDatabase, ICartItemLoadListener iCartItemLoadListener) {
            db = cartDatabase;
            listener = iCartItemLoadListener;

        }

//
//        private void getAllItemFromCartByUserPhone(CartDatabase db, String userPhone) {
//            List<CartItem> cartItems = db.cartDAO().getAllItemFromCart(userPhone);
//            Log.d("COUNT_CART", "" + cartItems.size());
//        }

        @Override
        protected List<CartItem> doInBackground(String... strings) {
            return db.cartDAO().getAllItemFromCart(Common.currentUser.getPhoneNumber());
        }

        @Override
        protected void onPostExecute(List<CartItem> cartItemList) {
            super.onPostExecute(cartItemList);
            listener.onGetAllItemFromCartSuccess(cartItemList);
        }
    }

    public static class InsertToCartAsync extends AsyncTask<CartItem, Void, Void> {
        CartDatabase db;

        public InsertToCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            inserToCart(db, cartItems[0]);
            return null;
        }

        private void inserToCart(CartDatabase db, CartItem cartItem) {
            try {
                db.cartDAO().insert(cartItem);
            } catch (SQLiteConstraintException e) {
                CartItem updateCartItem = db.cartDAO().getProductInCart(cartItem.getProductId(),
                        Common.currentUser.getPhoneNumber());
                updateCartItem.setProductQuantity(updateCartItem.getProductQuantity() + 1);
                db.cartDAO().update(updateCartItem);
            }
        }

        private void getAllItemFromCartByUserPhone(CartDatabase db, String userPhone) {
            List<CartItem> cartItems = db.cartDAO().getAllItemFromCart(userPhone);
            Log.d("COUNT_CART", "" + cartItems.size());
        }
    }

    public static class CountItemInCartAsync extends AsyncTask<Void, Void, Integer> {
        CartDatabase db;
        ICountItemInCartListener listener;

        public CountItemInCartAsync(CartDatabase cartDatabase, ICountItemInCartListener iCountItemInCartListener) {
            db = cartDatabase;
            listener = iCountItemInCartListener;

        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return Integer.parseInt(String.valueOf(countItemInCartRun(db)));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            listener.onCartItemCountSuccess(integer.intValue());
        }

        public int countItemInCartRun(CartDatabase db) {

            return db.cartDAO().CountItemInCart(Common.currentUser.getPhoneNumber());
        }

    }

    private static class SumCartAsync extends AsyncTask<Void, Void, Long> {
        private final CartDatabase db;
        private final ISumCartListener listener;

        public SumCartAsync(CartDatabase db, ISumCartListener listener) {
            this.db = db;
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return db.cartDAO().sumPrice(Common.currentUser.getPhoneNumber());
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            listener.onSumCartSuccess(aLong);
        }

//        private Long sumInCart(CartDatabase db) {
//            return db.cartDAO().sumPrice(Common.currentUser.getPhoneNumber());
//        }
    }


    public void getAllItemFromCartByUserPhone(CartDatabase db, String userPhone) {
        List<CartItem> cartItems = db.cartDAO().getAllItemFromCart(userPhone);
        Log.d("COUNT_CART", "" + cartItems.size());
    }
}




