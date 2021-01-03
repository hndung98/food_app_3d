package com.sorimachi.fastfoodapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.sorimachi.fastfoodapp.data.model.Bill;
import com.sorimachi.fastfoodapp.data.model.Customer;
import com.sorimachi.fastfoodapp.data.model.Food;
import com.sorimachi.fastfoodapp.data.model.ModelFood;
import com.sorimachi.fastfoodapp.data.model.ModelFoodInCart;
import com.sorimachi.fastfoodapp.data.model.Order;
import com.sorimachi.fastfoodapp.data.model.Shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerFoodListActivity extends AppCompatActivity {

    RecyclerView rvList, rvListCart;
    SearchView searchView;
    ImageButton btnCart, btnHome, btnProfile, btnHistory, btnSignOut, btnFavourite;

    ArrayList<String> lstShop;

    ArrayList<Food> lstFoods;
    ArrayList<ModelFood> lstModelFoods;
    ArrayList<ModelFoodInCart> lstModelFoodsInCart;

    Customer customer;

    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String code, phone;
    String shopCode;
    boolean isFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_list);

        code = getIntent().getExtras().getString("code");
        shopCode = getIntent().getExtras().getString("shop-code");
        phone = getIntent().getExtras().getString("phone");
        isFavourite = false;
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        lstModelFoodsInCart = new ArrayList<>();
        lstModelFoods = new ArrayList<>();

        Mapping();

        ControlButton();
 
        database.getReference("shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Shop shop = postSnapshot.getValue(Shop.class);
                    if(shop.getShopCode().equals(shopCode)){
                        lstFoods = shop.getFoodList();
                        for(Food food: lstFoods){
                            storageReference.child(food.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    lstModelFoods.add(new ModelFood(food.getFoodCode() , uri.toString(), food.getName(), food.getUnit(),food.getPrice()+""));
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(CustomerFoodListActivity.this);
                                    RecyclerView.LayoutManager rvLayoutManager = layoutManager;
                                    rvList.setLayoutManager(rvLayoutManager);

                                    FoodAdapter adapter = new FoodAdapter(CustomerFoodListActivity.this, lstModelFoods, "Thêm");

                                    rvList.setAdapter(adapter);

                                    adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            for(int i = 0; i < lstModelFoodsInCart.size(); i++){
                                                if(lstModelFoodsInCart.get(i).getFoodCode() == lstModelFoods.get(position).getFoodCode()){
                                                    lstModelFoodsInCart.get(i).setAmount(lstModelFoodsInCart.get(i).getAmount() + 1);
                                                    return;
                                                }
                                            }
                                            Toast.makeText(CustomerFoodListActivity.this,"Đã thêm vào giỏ hàng!",Toast.LENGTH_SHORT).show();
                                            lstModelFoodsInCart.add(new ModelFoodInCart(lstModelFoods.get(position).getFoodCode(), lstModelFoods.get(position).getName(), 1, lstModelFoods.get(position).getPrice()));
                                        }
                                    });
                                }
                            });
                        }
                        break;
                    }
//                    Shop shop = postSnapshot.getValue(Shop.class);
//                    lstFoods = shop.getFoodList();
//                    for(Food food: lstFoods){
//                        storageReference.child(food.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                lstModelFoods.add(new ModelFood(food.getFoodCode() , uri.toString(), food.getName()+" 1", food.getUnit(),food.getPrice()+""));
//                                lstModelFoods.add(new ModelFood(food.getFoodCode(), uri.toString(), food.getName()+" 2", food.getUnit(),food.getPrice()+""));
//                                lstModelFoods.add(new ModelFood(food.getFoodCode(), uri.toString(), food.getName()+" 3", food.getUnit(),food.getPrice()+""));
//                                lstModelFoods.add(new ModelFood(food.getFoodCode(), uri.toString(), food.getName()+" 4", food.getUnit(),food.getPrice()+""));
//                                lstModelFoods.add(new ModelFood(food.getFoodCode(), uri.toString(), food.getName()+" 5", food.getUnit(),food.getPrice()+""));
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(CustomerFoodListActivity.this);
//                                RecyclerView.LayoutManager rvLayoutManager = layoutManager;
//                                rvList.setLayoutManager(rvLayoutManager);
//
//                                FoodAdapter adapter = new FoodAdapter(CustomerFoodListActivity.this, lstModelFoods, "Thêm");
//
//                                rvList.setAdapter(adapter);
//
//                                adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(int position) {
//                                        Toast.makeText(CustomerFoodListActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
//                                        lstModelFoosInCart.add(new ModelFoodInCart(lstModelFoods.get(position).getFoodCode(), lstModelFoods.get(position).getName(), 1, lstModelFoods.get(position).getPrice()));
//                                    }
//                                });
//                            }
//                        });
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference("customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(phone).exists()){
                    customer = snapshot.child(phone).getValue(Customer.class);
                    lstShop = customer.getLstShop();
                }
                if(lstShop.contains(shopCode)){
                    isFavourite = true;
                    btnFavourite.setImageResource(R.mipmap.ic_favourite_red);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerFoodListActivity.this, CustomerMainActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        startActivity(intent);
        finish();
    }

    private void Mapping(){
        rvList = findViewById(R.id.rvList);
        btnCart = findViewById(R.id.btn_cart);
        btnHome = findViewById(R.id.btn_home);
        btnProfile = findViewById(R.id.btn_profile);
        btnHistory = findViewById(R.id.btn_history);
        btnSignOut = findViewById(R.id.btn_sign_out);
        btnFavourite = findViewById(R.id.btn_favourite);
    }

    private void ControlButton(){
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite = !isFavourite;
                if(isFavourite){
                    lstShop.add(shopCode);
                    btnFavourite.setImageResource(R.mipmap.ic_favourite_red);
                }
                else{
                    lstShop.remove(shopCode);
                    btnFavourite.setImageResource(R.mipmap.ic_favourite_border_red);
                }
                customer.setLstShop(lstShop);
                database.getReference("customer").child(phone).setValue(customer);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CustomerFoodListActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.layout_customer_current_cart);
                dialog.show();
                rvListCart = dialog.findViewById(R.id.rvList);
                LinearLayoutManager layoutManagerCart = new LinearLayoutManager(dialog.getContext());
                RecyclerView.LayoutManager rvLayoutManagerCart = layoutManagerCart;
                rvListCart.setLayoutManager(rvLayoutManagerCart);

                FoodInCartAdapter adapterCart = new FoodInCartAdapter(dialog.getContext(), lstModelFoodsInCart, true);
                rvListCart.setAdapter(adapterCart);

                adapterCart.setOnMinusItemClickListener(new FoodInCartAdapter.OnMinusItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(lstModelFoodsInCart.get(position).getAmount() > 0){
                            lstModelFoodsInCart.get(position).setAmount(lstModelFoodsInCart.get(position).getAmount()-1);
                        }
                    }
                });

                adapterCart.setOnPlusItemClickListener(new FoodInCartAdapter.OnPlusItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(lstModelFoodsInCart.get(position).getAmount() < 99){
                            lstModelFoodsInCart.get(position).setAmount(lstModelFoodsInCart.get(position).getAmount()+1);
                        }
                    }
                });

                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(lstModelFoodsInCart.size() > 0){
                            Date curDate = new Date();
                            String strCurDate = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
                            String strCurTime = new SimpleDateFormat("HH:mm:ss").format(curDate);
                            strCurDate = strCurDate.replace("-","");
                            strCurTime = strCurTime.replace(":","");
                            String finalStrCurDate = strCurDate;
                            String finalStrCurTime = strCurTime;
                            new AlertDialog.Builder(CustomerFoodListActivity.this)
                                    .setTitle("Confirm")
                                    .setMessage("Xác nhận đặt hàng ? " + strCurDate)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface subDialog, int which) {
                                            Bill bill = new Bill();
                                            ArrayList<Order> lstOrders = new ArrayList<>();
                                            for(ModelFoodInCart food: lstModelFoodsInCart){
                                                lstOrders.add(new Order(food.getName(), Integer.parseInt(food.getPrice()), food.getAmount(), 0));
                                            }
                                            bill.setOrdersList(lstOrders);
                                            bill.setBillCode(finalStrCurTime +code);
                                            bill.setShopCode(shopCode);
                                            bill.setCustomerCode(phone);
                                            bill.setYmd(Integer.parseInt(finalStrCurDate));
                                            bill.setHms(Integer.parseInt(finalStrCurTime));
                                            bill.setStatus(0);
                                            database.getReference().child("bill").child(finalStrCurDate).child(""+bill.getBillCode()).setValue(bill);

                                            lstModelFoodsInCart.clear();
                                            dialog.cancel();
                                            Toast.makeText(CustomerFoodListActivity.this, "Đã đặt hàng!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                        else{
                            dialog.cancel();
                            Toast.makeText(CustomerFoodListActivity.this, "Vui lòng chọn món!", Toast.LENGTH_SHORT).show();
                        }

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
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerFoodListActivity.this, "setting", Toast.LENGTH_SHORT).show();
            }
        });
    }
}