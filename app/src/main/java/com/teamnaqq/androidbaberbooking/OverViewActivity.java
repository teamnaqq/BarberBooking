package com.teamnaqq.androidbaberbooking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.teamnaqq.androidbaberbooking.Adapter.MyServiceItemAdapter;
import com.teamnaqq.androidbaberbooking.Common.SpacesItemDecoration;
import com.teamnaqq.androidbaberbooking.Interface.IServiceDataLoadListener;
import com.teamnaqq.androidbaberbooking.Model.CommentItem;
import com.teamnaqq.androidbaberbooking.Model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OverViewActivity extends AppCompatActivity implements IServiceDataLoadListener {
    CollectionReference shoppingItemRef;

    Unbinder unbinder;

    IServiceDataLoadListener iServiceDataLoadListener;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @BindView(R.id.re_overview)
    RecyclerView re_overview;

    @OnClick(R.id.chip_wax)
    void waxChipClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Toc");
    }

    @OnClick(R.id.chip_spray)
    void sprayChipClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("Mat");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);
        unbinder = ButterKnife.bind(OverViewActivity.this);
        iServiceDataLoadListener = this;
        loadShoppingItem("Wax");
        initView();
    }

    private void initView() {
        re_overview.setHasFixedSize(true);
        re_overview.setLayoutManager(new GridLayoutManager(getApplication(), 1));
        re_overview.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyServiceItemAdapter adapter = new MyServiceItemAdapter(getApplication(), shoppingItemList);
        re_overview.setAdapter(adapter);

    }

    @Override
    public void onCommentDataLoadSuccess(List<CommentItem> commentItemList) {

    }

    @Override
    public void onShoppingDataLoadFailed(String message) {

    }

    @Override
    public void onCommentDataLoadFailed(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

}
