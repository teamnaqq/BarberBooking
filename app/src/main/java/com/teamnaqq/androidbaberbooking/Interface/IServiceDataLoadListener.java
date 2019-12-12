package com.teamnaqq.androidbaberbooking.Interface;

import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.Model.ShoppingItem;

import java.util.List;

public interface IServiceDataLoadListener {
    void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList);

    //void onCommentDataLoadSuccess(List<CommentItem> commentItemList);

    void onCommentDataLoadSuccess(List<CommentItem> commentItemList);

    void onShoppingDataLoadFailed(String message);

    void onCommentDataLoadFailed(String message);
}
