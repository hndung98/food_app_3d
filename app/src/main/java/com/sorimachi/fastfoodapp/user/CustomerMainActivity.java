package com.sorimachi.fastfoodapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sorimachi.fastfoodapp.R;
import com.sorimachi.fastfoodapp.data.model.ModelShop;
import com.sorimachi.fastfoodapp.adapter.ShopAdapter;
import com.sorimachi.fastfoodapp.data.model.Shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomerMainActivity extends AppCompatActivity {

    RecyclerView rvFavouriteShop, rvRecommendShop;
    ArrayList<ModelShop> lstFavouriteShops, lstRecommendShop;

    ImageButton btnHome, btnSetting, btnProfile;
    SearchView searchView;

    ShopAdapter adapterFavourite, adapterRecommend;
    LinearLayoutManager layoutFavouriteManager, layoutRecommendManager;
    RecyclerView.LayoutManager rvFavouriteLiLayoutManager, rvRecommendLiLayoutManager;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cutomer);

        code = getIntent().getExtras().getString("code");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Mapping();
        ControlButton();

        lstRecommendShop = new ArrayList<>();
        lstFavouriteShops = new ArrayList<>();


        LoadFavouriteShops();
        LoadRecommendShops();
    }




    private void LoadFavouriteShops(){
        database.getReference("shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Shop shop = postSnapshot.getValue(Shop.class);
                    storageReference.child(shop.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            lstFavouriteShops.add(new ModelShop(shop.getShopCode(), shop.getName()+" 1", uri.toString(), shop.getAddress()));
                            lstFavouriteShops.add(new ModelShop(shop.getShopCode(), shop.getName()+" 2", uri.toString(), shop.getAddress()));
                            lstFavouriteShops.add(new ModelShop(shop.getShopCode(), shop.getName()+" 3", uri.toString(), shop.getAddress()));
                            lstFavouriteShops.add(new ModelShop(shop.getShopCode(), shop.getName()+" 4", uri.toString(), shop.getAddress()));
                            lstFavouriteShops.add(new ModelShop(shop.getShopCode(), shop.getName()+" 5", uri.toString(), shop.getAddress()));

                            layoutFavouriteManager = new LinearLayoutManager(CustomerMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            rvFavouriteLiLayoutManager = layoutFavouriteManager;
                            rvFavouriteShop.setLayoutManager(rvFavouriteLiLayoutManager);
                            adapterFavourite = new ShopAdapter(CustomerMainActivity.this, lstFavouriteShops);

                            rvFavouriteShop.setAdapter(adapterFavourite);
                            adapterFavourite.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(CustomerMainActivity.this, CustomerFoodListActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("shop-code", lstFavouriteShops.get(position).getCode());
                                    intent.putExtra("code", code);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void LoadRecommendShops(){
        database.getReference("shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Shop shop = postSnapshot.getValue(Shop.class);
                    storageReference.child(shop.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            lstRecommendShop.add(new ModelShop(shop.getShopCode(), shop.getName()+" 1", uri.toString(), shop.getAddress()));
                            lstRecommendShop.add(new ModelShop(shop.getShopCode(), shop.getName()+" 2", uri.toString(), shop.getAddress()));
                            lstRecommendShop.add(new ModelShop(shop.getShopCode(), shop.getName()+" 3", uri.toString(), shop.getAddress()));
                            lstRecommendShop.add(new ModelShop(shop.getShopCode(), shop.getName()+" 4", uri.toString(), shop.getAddress()));
                            lstRecommendShop.add(new ModelShop(shop.getShopCode(), shop.getName()+" 5", uri.toString(), shop.getAddress()));

                            layoutRecommendManager = new LinearLayoutManager(CustomerMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            rvRecommendLiLayoutManager = layoutRecommendManager;
                            rvRecommendShop.setLayoutManager(rvRecommendLiLayoutManager);

                            adapterRecommend = new ShopAdapter(CustomerMainActivity.this, lstRecommendShop);

                            rvRecommendShop.setAdapter(adapterRecommend);

                            adapterRecommend.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(CustomerMainActivity.this, CustomerFoodListActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void Mapping(){
        rvFavouriteShop = findViewById(R.id.rv_favourite_shop);
        rvRecommendShop = findViewById(R.id.rv_recommend_shop);
        btnHome = findViewById(R.id.btn_home);
        btnProfile = findViewById(R.id.btn_profile);
        btnSetting = findViewById(R.id.btn_setting);
//        searchView = findViewById(R.id.search_view);
    }

    private void ControlButton(){
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerMainActivity.this, "profile", Toast.LENGTH_SHORT).show();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerMainActivity.this, "home", Toast.LENGTH_SHORT).show();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerMainActivity.this, "setting", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void StartAdapter(){
        layoutFavouriteManager = new LinearLayoutManager(CustomerMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvFavouriteLiLayoutManager = layoutFavouriteManager;
        rvFavouriteShop.setLayoutManager(rvFavouriteLiLayoutManager);
        adapterFavourite = new ShopAdapter(CustomerMainActivity.this, lstFavouriteShops);

        rvFavouriteShop.setAdapter(adapterFavourite);
        adapterFavourite.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CustomerMainActivity.this, CustomerFoodListActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("shop-code", 1);
                startActivity(intent);
                finish();
            }
        });

        layoutRecommendManager = new LinearLayoutManager(CustomerMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvRecommendLiLayoutManager = layoutRecommendManager;
        rvRecommendShop.setLayoutManager(rvRecommendLiLayoutManager);

        adapterRecommend = new ShopAdapter(CustomerMainActivity.this, lstRecommendShop);

        rvRecommendShop.setAdapter(adapterRecommend);

        adapterRecommend.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CustomerMainActivity.this, CustomerFoodListActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadImage(String uri, ImageView image){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.capsule)
                .error(R.drawable.capsule)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(uri)
                .apply(options)
                .into(image);
    }
}