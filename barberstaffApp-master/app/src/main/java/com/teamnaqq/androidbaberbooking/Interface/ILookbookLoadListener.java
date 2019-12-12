package com.teamnaqq.androidbaberbooking.Interface;

import com.teamnaqq.androidbaberbooking.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
