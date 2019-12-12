package com.teamnaqq.androidbaberbooking.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamnaqq.androidbaberbooking.Interface.IRecyclerItemSelectedListener;
import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.R;

import java.util.List;

public class CommentApdapter  extends RecyclerView.Adapter<CommentApdapter.MyViewHolder> {

    private static final String TAG ="accccc" ;
    private Context context;
    private List<CommentItem> commentItemList;

    Bitmap bitmap;




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

        if(commentItem.getImgCmt()!=null){
            byte[] arrImg= Base64.decode(commentItem.getImgCmt(),Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(arrImg,0,arrImg.length);
            holder.imgcmt.setImageBitmap(Bitmap.createScaledBitmap(bitmap,150,250,false));
        }


        holder.tv_custumer.setText(commentItem.getCustomer());
        holder.tv_cmt.setText(commentItem.getContent());
        holder.tv_date.setText(commentItem.getDate());
        holder.tv_time.setText(commentItem.getTime());


        Log.e(TAG, String.valueOf(bitmap));

    }

    @Override
    public int getItemCount() {
        return commentItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_time,tv_date,tv_cmt,tv_custumer;
        private ImageView imgcmt;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date=(TextView) itemView.findViewById(R.id.tv_date);
            tv_time=(TextView) itemView.findViewById(R.id.tv_time);
            tv_cmt=(TextView) itemView.findViewById(R.id.tv_cmtContent);
            tv_custumer=(TextView) itemView.findViewById(R.id.tv_customer);
            imgcmt=(ImageView) itemView.findViewById(R.id.img_Cmt);

        }

    }
}
