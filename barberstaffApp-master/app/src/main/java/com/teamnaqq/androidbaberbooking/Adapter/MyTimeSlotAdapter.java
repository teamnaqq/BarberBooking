package com.teamnaqq.androidbaberbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamnaqq.androidbaberbooking.Common.Common;
import com.teamnaqq.androidbaberbooking.Interface.IRecyclerItemSelectedListener;
import com.teamnaqq.androidbaberbooking.Model.EventBus.EnableNextButton;
import com.teamnaqq.androidbaberbooking.Model.TimeSlot;
import com.teamnaqq.androidbaberbooking.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
  //  LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
       /// this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
       // this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtTimeSlot.setText(new StringBuilder(Common.convertTimeSlotToString(position)));
        if (timeSlotList.size() == 0) {
            holder.cardTinmSlot.setEnabled(true);
            holder.txtTimeSlotDescription.setText("Available");
            holder.txtTimeSlotDescription.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txtTimeSlot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.cardTinmSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        } else {
            for (TimeSlot slotValue : timeSlotList) {
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot == position) {

                    holder.cardTinmSlot.setEnabled(false);
                    holder.cardTinmSlot.setTag(Common.DISABLE_TAG);
                    holder.cardTinmSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.txtTimeSlotDescription.setText("Full");
                    holder.txtTimeSlotDescription.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    holder.txtTimeSlot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }
        }
        if (!cardViewList.contains(holder.cardTinmSlot))
            cardViewList.add(holder.cardTinmSlot);

           holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
               @Override
               public void onItemSelectedListener(View view,final int pos) {
                   for (CardView cardView : cardViewList) {
                       if (cardView.getTag() == null)
                           cardView.setCardBackgroundColor(context.getResources()
                                   .getColor(android.R.color.white));
                   }
                   holder.cardTinmSlot.setCardBackgroundColor(context.getResources()
                           .getColor(android.R.color.holo_orange_dark));

//                   Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                   intent.putExtra(Common.KEY_TIME_SLOT,pos);
//                   intent.putExtra(Common.KEY_STEP,3);
                //   localBroadcastManager.sendBroadcast(intent);

                   //Event Bus
                   EventBus.getDefault().postSticky(new EnableNextButton(3,pos));
               }
           });
       }


    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardTinmSlot;
        private TextView txtTimeSlot;
        private TextView txtTimeSlotDescription;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTinmSlot = (CardView) itemView.findViewById(R.id.card_tinm_slot);
            txtTimeSlot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txtTimeSlotDescription = (TextView) itemView.findViewById(R.id.txt_time_slot_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
