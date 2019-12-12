package com.teamnaqq.androidbaberbooking.Interface;

import com.teamnaqq.androidbaberbooking.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);

}
