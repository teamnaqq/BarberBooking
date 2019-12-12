package com.teamnaqq.androidbaberbooking.Interface;

import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.Model.ShoppingItem;

import java.util.List;

public interface ICommentDataLoadListener {
    void onCommentDataLoadSuccess(List<CommentItem> commentItemList);

    //void onCommentDataLoadSuccess(List<CommentItem> commentItemList);

    void onCommentDataLoadFailed(String message);
}
