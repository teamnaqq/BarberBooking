package com.teamnaqq.androidbaberbooking.Interface;

import com.teamnaqq.androidbaberbooking.Model.BookingInformation;

public interface IBookingInforLoadListener {
    void onBookingInforLoadEmpty();

    void onBookingInforLoadSuccess(BookingInformation bookingInformation, String  documentId);

    void onBookingInforLoadFailed(String message);
}
