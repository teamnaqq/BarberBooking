package com.teamnaqq.androidbaberbooking.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.R;

import java.util.List;

public class CommentApdapter  extends RecyclerView.Adapter<CommentApdapter.MyViewHolder> {

    private Context context;
    private List<CommentItem> commentItemList;



    public CommentApdapter(Context context, List<CommentItem> commentItemList) {
        this.context = context;
        this.commentItemList = commentItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cmt, parent, false);
        return new CommentApdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CommentItem commentItem=commentItemList.get(position);

        holder.tv_name.setText(commentItem.getName());
        holder.tv_cmt.setText(commentItem.getCmt());
        holder.tv_date.setText(commentItem.getDate());
        holder.tv_time.setText(commentItem.getTime());

    }

    @Override
    public int getItemCount() {
        return commentItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_name,tv_time,tv_date,tv_cmt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date=(TextView) itemView.findViewById(R.id.tv_date);
            tv_time=(TextView) itemView.findViewById(R.id.tv_time);
            tv_cmt=(TextView) itemView.findViewById(R.id.tv_cmtContent);
            tv_name=(TextView) itemView.findViewById(R.id.tv_cmtUsername);

        }
    }
}
