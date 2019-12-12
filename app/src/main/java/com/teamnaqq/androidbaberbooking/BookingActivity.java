package com.teamnaqq.androidbaberbooking;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;
import com.teamnaqq.androidbaberbooking.Adapter.MyViewPagerAdapter;
import com.teamnaqq.androidbaberbooking.Common.Common;
import com.teamnaqq.androidbaberbooking.Common.NonSwipeViewPager;
import com.teamnaqq.androidbaberbooking.Model.Barber;
import com.teamnaqq.androidbaberbooking.Model.EventBus.BarberDoneEvent;
import com.teamnaqq.androidbaberbooking.Model.EventBus.ConfirmBookingEvent;
import com.teamnaqq.androidbaberbooking.Model.EventBus.DisplayTimeSlotEvent;
import com.teamnaqq.androidbaberbooking.Model.EventBus.EnableNextButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {
    // LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference barberRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @OnClick(R.id.btn_previous_step)
    void previousStep() {
        if (Common.step == 3 || Common.step > 0) {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if (Common.step < 3) {
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }


    @OnClick(R.id.btn_next_step)
    void nextClick() {
        if (Common.step < 3 || Common.step == 0) {
            Common.step++;
            if (Common.step == 1) {
                if (Common.currentSalon != null)
                    //   Toast.makeText(this, ""+Common.currentSalon.getSalonId(), Toast.LENGTH_SHORT).show();
                    loadBarberBySalon(Common.currentSalon.getSalonId());
            } else if (Common.step == 2) {
                if (Common.currentBarber != null)
                    //   Toast.makeText(this, "" + Common.currentBarber.getBarberId(), Toast.LENGTH_SHORT).show();
                    loadTimeSlotOfBarber(Common.currentBarber.getBarberId());
            } else if (Common.step == 3) {
                if (Common.currentTimeSlot != -1)
                    confirmBooking();
                //   Toast.makeText(this, "" + Common.currentBarber.getBarberId(), Toast.LENGTH_SHORT).show();
            }

            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
//        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
//        localBroadcastManager.sendBroadcast(intent);
        EventBus.getDefault().postSticky(new ConfirmBookingEvent(true));

    }


    private void loadTimeSlotOfBarber(String barberId) {
//        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
//        localBroadcastManager.sendBroadcast(intent);

        EventBus.getDefault().postSticky(new DisplayTimeSlotEvent(true));

    }

    private void loadBarberBySalon(String salonId) {
        dialog.show();
        // /AllSalon/NewYork/Branch/trI33eemAuukur3PMxFD/Babers
        if (!TextUtils.isEmpty(Common.city)) {
            barberRef = FirebaseFirestore.getInstance()
                    .collection("AllSalon")
                    .document(Common.city)
                    .collection("Branch")
                    .document(salonId)
                    .collection("Babers");

            barberRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Barber> barbers = new ArrayList<>();
                            for (QueryDocumentSnapshot barberSnapshot : task.getResult()) {
                                Barber barber = barberSnapshot.toObject(Barber.class);
                                barber.setPassword("");
                                barber.setBarberId(barberSnapshot.getId());
                                barbers.add(barber);
                            }
//                            Intent intent = new Intent(Common.KEY_BARBER_LOAD_DONE);
//                            intent.putParcelableArrayListExtra(Common.KEY_BARBER_LOAD_DONE, barbers);
//                            localBroadcastManager.sendBroadcast(intent);

                            EventBus.getDefault().postSticky(new BarberDoneEvent(barbers));
                            dialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }

    }

    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    //Event Bus

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void buttonNextRecevicer(EnableNextButton  event){
        int step = event.getStep();
        if (step == 1)
            Common.currentSalon =  event.getSalon();
        else if (step == 2)
            Common.currentBarber = event.getBarber();
        else if (step == 3)
            Common.currentTimeSlot =  event.getTimeSlot();
        //Common.currentSalon = intent.getParcelableExtra(Common.KEY_SALON_STORE);
        btn_next_step.setEnabled(true);
        setColorButton();
    }

//    @Override
//    protected void onDestroy() {
//        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
//        super.onDestroy();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
//        localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        setupStepView();
        setColorButton();

        //view
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                stepView.go(i, true);
                if (i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()) {
            btn_next_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }

        if (btn_previous_step.isEnabled()) {
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }

    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Salon");
        stepList.add("Barber");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
       EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
