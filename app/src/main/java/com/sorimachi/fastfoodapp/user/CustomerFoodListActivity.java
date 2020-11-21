package com.sorimachi.fastfoodapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sorimachi.fastfoodapp.R;
import com.sorimachi.fastfoodapp.adapter.FoodAdapter;
import com.sorimachi.fastfoodapp.adapter.FoodInCartAdapter;
import com.sorimachi.fastfoodapp.data.model.Food;
import com.sorimachi.fastfoodapp.data.model.ModelFood;
import com.sorimachi.fastfoodapp.data.model.ModelFoodInCart;
import com.sorimachi.fastfoodapp.data.model.Shop;

import java.util.ArrayList;

public class CustomerFoodListActivity extends AppCompatActivity {

    RecyclerView rvList, rvListCart;
    SearchView searchView;
    ImageButton btnCart, btnHome, btnProfile, btnSetting;

    ArrayList<Food> lstFoods;
    ArrayList<ModelFood> lstModelFoods;
    ArrayList<ModelFoodInCart> lstModelFoosInCart;

    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String code;
    String shopCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        code = getIntent().getExtras().getString("code");

        lstModelFoosInCart = new ArrayList<>();
        lstModelFoods = new ArrayList<>();

        Mapping();

        ControlButton();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database.getReference("shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Shop shop = postSnapshot.getValue(Shop.class);
                    lstFoods = shop.getFoodList();
                    for(Food food: lstFoods){
                        storageReference.child(food.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                lstModelFoods.add(new ModelFood(uri.toString(), food.getName()+" 1", food.getUnit(),food.getPrice()+""));
                                lstModelFoods.add(new ModelFood(uri.toString(), food.getName()+" 2", food.getUnit(),food.getPrice()+""));
                                lstModelFoods.add(new ModelFood(uri.toString(), food.getName()+" 3", food.getUnit(),food.getPrice()+""));
                                lstModelFoods.add(new ModelFood(uri.toString(), food.getName()+" 4", food.getUnit(),food.getPrice()+""));
                                lstModelFoods.add(new ModelFood(uri.toString(), food.getName()+" 5", food.getUnit(),food.getPrice()+""));
                                LinearLayoutManager layoutManager = new LinearLayoutManager(CustomerFoodListActivity.this);
                                RecyclerView.LayoutManager rvLayoutManager = layoutManager;
                                rvList.setLayoutManager(rvLayoutManager);

                                FoodAdapter adapter = new FoodAdapter(CustomerFoodListActivity.this, lstModelFoods);

                                rvList.setAdapter(adapter);

                                adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Toast.makeText(CustomerFoodListActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
                                        lstModelFoosInCart.add(new ModelFoodInCart(lstModelFoods.get(position).getName(), 1, lstModelFoods.get(position).getPrice()));
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Mapping(){
        rvList = findViewById(R.id.rvList);
        btnCart = findViewById(R.id.btn_cart);
        btnHome = findViewById(R.id.btn_home);
        btnProfile = findViewById(R.id.btn_profile);
        btnSetting = findViewById(R.id.btn_setting);
    }

    private void ControlButton(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CustomerFoodListActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.layout_current_cart);
                dialog.show();
                rvListCart = dialog.findViewById(R.id.rvList);
                LinearLayoutManager layoutManagerCart = new LinearLayoutManager(dialog.getContext());
                RecyclerView.LayoutManager rvLayoutManagerCart = layoutManagerCart;
                rvListCart.setLayoutManager(rvLayoutManagerCart);

                FoodInCartAdapter adapterCart = new FoodInCartAdapter(dialog.getContext(), lstModelFoosInCart);
                rvListCart.setAdapter(adapterCart);

                adapterCart.setOnMinusItemClickListener(new FoodInCartAdapter.OnMinusItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(lstModelFoosInCart.get(position).getAmount() > 0){
                            lstModelFoosInCart.get(position).setAmount(lstModelFoosInCart.get(position).getAmount()-1);
                        }
                    }
                });

                adapterCart.setOnPlusItemClickListener(new FoodInCartAdapter.OnPlusItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(lstModelFoosInCart.get(position).getAmount() < 99){
                            lstModelFoosInCart.get(position).setAmount(lstModelFoosInCart.get(position).getAmount()+1);
                        }
                    }
                });

                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Toast.makeText(CustomerFoodListActivity.this, "pos: " + lstModelFoosInCart.size(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerFoodListActivity.this, "profile", Toast.LENGTH_SHORT).show();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerFoodListActivity.this, "home", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerFoodListActivity.this, CustomerMainActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
                finish();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerFoodListActivity.this, "setting", Toast.LENGTH_SHORT).show();
            }
        });
    }
}