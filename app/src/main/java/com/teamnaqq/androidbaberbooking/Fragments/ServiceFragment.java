package com.teamnaqq.androidbaberbooking.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamnaqq.androidbaberbooking.Adapter.CommentApdapter;
import com.teamnaqq.androidbaberbooking.Adapter.MyServiceItemAdapter;
import com.teamnaqq.androidbaberbooking.Common.Common;
import com.teamnaqq.androidbaberbooking.Common.SpacesItemDecoration;
import com.teamnaqq.androidbaberbooking.Interface.IServiceDataLoadListener;
import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.Model.ShoppingItem;
import com.teamnaqq.androidbaberbooking.Model.User;
import com.teamnaqq.androidbaberbooking.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment implements IServiceDataLoadListener {
    private static final String TAG1 ="pppppp" ;
    private List<User> userList;


    private static final String TAG ="3123123123" ;
    FirebaseFirestore db;
    Unbinder unbinder;
    Bitmap selectedBitmap;

    CollectionReference shoppingItemRef;
    CollectionReference commentItemRef;

    IServiceDataLoadListener iServiceDataLoadListener;
    ServiceFragment iCommentDataLoadListener;

    ImageView imageView;

    String sImg;



    @BindView(R.id.edt_cmt)
    EditText edt_cmt;
    @BindView(R.id.btn_cmt)
    ImageView btn_cmt;

    @BindView(R.id.btn_loadImg)
    ImageView btn_choose;

    @BindView(R.id.btn_close)
    ImageView btn_close;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;
    @BindView(R.id.recycler_cmt)
    RecyclerView recycler_cmt;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm a";



    int i;
    @OnClick(R.id.chip_wax)
    void waxChipClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Toc");
        loadCommentItem("CMTToc");
        i=0;
    }


    @OnClick(R.id.chip_spray)
    void sprayChipClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("Mat");
        loadCommentItem("CMTMat");
        i=1;

    }

    @OnClick(R.id.btn_loadImg)
    void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100&& resultCode == RESULT_OK) {
//xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            //imgPicture.setImageBitmap(selectedBitmap);
        }
        else if(requestCode == 200&& resultCode == RESULT_OK) {
            try {
//xử lý lấy ảnh chọn từ điện thoại:
                Uri uri = data.getData();
                Log.e(TAG, String.valueOf(uri));
                selectedBitmap =(Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);


                imageView = (ImageView)getActivity().findViewById(R.id.img_load);
                imageView.setImageBitmap(selectedBitmap);

                if(imageView!=null){
                    btn_cmt.setVisibility(View.VISIBLE);
                    btn_close.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] Image_To_Bye(ImageView v){

        BitmapDrawable drawable=(BitmapDrawable) v.getDrawable();

        selectedBitmap=drawable.getBitmap();

        ByteArrayOutputStream stream=new ByteArrayOutputStream();

        selectedBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

    @OnClick(R.id.btn_close)
    void clickClose() {
        imageView.setImageBitmap(null);
        imageView=null;
        btn_close.setVisibility(View.INVISIBLE);

        if(edt_cmt.getText().toString().equals("")){
            btn_cmt.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick(R.id.btn_cmt)
    void clickCmt() {
        Date cmtTimeDate=new Date();
        btn_close.setVisibility(View.INVISIBLE);
        //----img----
        if(imageView!=null){
            byte[] arrImg = Image_To_Bye(imageView);
            sImg = Base64.encodeToString(arrImg, Base64.DEFAULT);
        }
        //---getNameUser---
        String name= Common.currentUser.getName();
        //-----getDate-----
        SimpleDateFormat simpleDateFormatdate=new SimpleDateFormat(DATE_FORMAT);
        String date=simpleDateFormatdate.format(cmtTimeDate);
        //----getTime-----
        SimpleDateFormat simpleDateFormattime=new SimpleDateFormat(TIME_FORMAT);
        String time=simpleDateFormattime.format(cmtTimeDate);

        String msg=edt_cmt.getText().toString();

        String regex="[\\s]+";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(msg);

        String msg1=matcher.replaceAll(" ");


        if(!msg.equals("")||imageView!=null){
            sendComment(name,time,date,msg1,sImg);
        }

        if(imageView!=null){
            imageView.setImageBitmap(null);
            imageView=null;
        }
        edt_cmt.setText("");
    }


    private void sendComment(String customer,String time,String date,String cmt,String img){
        db=FirebaseFirestore.getInstance();
        Map<String, Object> newContact=new HashMap<>();

        newContact.put("content",cmt);
        newContact.put("time",time);
        newContact.put("date",date);
        newContact.put("customer",customer);
        newContact.put("imgCmt",img);



        if(i==0){
            db.collection("Comment").document("CMTToc").collection("comment").add(newContact);
            loadCommentItem("CMTToc");
        }else if(i==1){
            db.collection("Comment").document("CMTMat").collection("comment").add(newContact);
            loadCommentItem("CMTMat");
        }

    }

    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Service")
                .document(itemMenu)
                .collection("Items");

        shoppingItemRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iServiceDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ShoppingItem> shoppingItems = new ArrayList<>();
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ShoppingItem shoppingItem = snapshot.toObject(ShoppingItem.class);
                        shoppingItem.setId(snapshot.getId());
                        shoppingItems.add(shoppingItem);
                    }
                    iServiceDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
                }
            }
        });
    }

    private void loadCommentItem(String itemMenu) {
        commentItemRef = FirebaseFirestore.getInstance().collection("Comment")
                .document(itemMenu).collection("comment");

        Log.e(TAG, String.valueOf(commentItemRef));


        commentItemRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iCommentDataLoadListener.onCommentDataLoadFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<CommentItem> commentItemList = new ArrayList<>();
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        CommentItem commentItem = snapshot.toObject(CommentItem.class);
                        //commentItem.setId(snapshot.getId());
                        commentItemList.add(commentItem);
                    }
                    iCommentDataLoadListener.onCommentDataLoadSuccess(commentItemList);
                }
            }
        });
    }

    private void setSelectedChip(Chip chip_wax) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chipItem = (Chip) chipGroup.getChildAt(i);
            if (chipItem.getId() != chip_wax.getId()) {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public ServiceFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shoppinfragment, container, false);
        Object target;
        unbinder = ButterKnife.bind(this, view);
        iServiceDataLoadListener = this;
        iCommentDataLoadListener=this;
        loadShoppingItem("Toc");
        loadCommentItem("CMTToc");
        initView();

        btn_cmt.setVisibility(View.INVISIBLE);
        btn_close.setVisibility(View.INVISIBLE);
        edt_cmt.addTextChangedListener(textChange);
        return view;
    }



    public TextWatcher textChange=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String cmt=edt_cmt.getText().toString().trim();

            if(!cmt.equals("")||imageView!=null){
                btn_cmt.setVisibility(View.VISIBLE);
            }else {
                btn_cmt.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    private void initView() {
        recycler_items.setHasFixedSize(true);
        recycler_items.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycler_items.addItemDecoration(new SpacesItemDecoration(8));

        recycler_cmt.setHasFixedSize(true);
        recycler_cmt.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycler_cmt.addItemDecoration(new SpacesItemDecoration(8));

    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyServiceItemAdapter adapter = new MyServiceItemAdapter(getContext(), shoppingItemList);
        recycler_items.setAdapter(adapter);

    }

    @Override
    public void onCommentDataLoadSuccess(List<CommentItem> commentItemList) {
        CommentApdapter adapter = new CommentApdapter(getContext(), commentItemList);
        recycler_cmt.setAdapter(adapter);

    }


    @Override
    public void onShoppingDataLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentDataLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



}
