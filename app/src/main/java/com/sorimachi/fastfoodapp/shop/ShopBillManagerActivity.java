package com.sorimachi.fastfoodapp.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorimachi.fastfoodapp.R;
import com.sorimachi.fastfoodapp.adapter.BillAdapter;
import com.sorimachi.fastfoodapp.adapter.FoodInCartAdapter;
import com.sorimachi.fastfoodapp.data.model.Bill;
import com.sorimachi.fastfoodapp.data.model.ModelFoodInCart;
import com.sorimachi.fastfoodapp.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShopBillManagerActivity extends AppCompatActivity {

    RecyclerView recyclerView, rvListCart;

    ArrayList<Bill> lstOldBills, lstNewBills;
    ArrayList<Order> lstOrders;

    String phone;
    ArrayList<ModelFoodInCart> lstModelFoodsInCart;

    Runnable runnable;
    Handler handler;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_bill_manager);

        database = FirebaseDatabase.getInstance();

        phone = getIntent().getExtras().getString("phone");

        lstModelFoodsInCart = new ArrayList<>();
        lstOldBills = new ArrayList<>();
        lstOrders = new ArrayList<>();
        lstNewBills = new ArrayList<>();

        Mapping();

        ControlButton();

        Date curDate = new Date();
        String strCurDate = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
        strCurDate = strCurDate.replace("-","");
        String finalStrCurDate = strCurDate;
        database.getReference("bill").child(strCurDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                recyclerView = findViewById(R.id.rvList);

                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Bill bill = postSnapshot.getValue(Bill.class);
                    if(bill.getShopCode().equals(phone) && bill.getStatus() == 0){
                        lstOldBills.add(bill);
                        lstNewBills.add(bill);
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(ShopBillManagerActivity.this);
                RecyclerView.LayoutManager layoutManager1 =  layoutManager;
                recyclerView.setLayoutManager(layoutManager1);

                BillAdapter adapter = new BillAdapter(ShopBillManagerActivity.this, lstOldBills);

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new BillAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        lstModelFoodsInCart.clear();
                        lstOrders = lstOldBills.get(position).getOrdersList();
                        for(Order order: lstOrders){
                            lstModelFoodsInCart.add(new ModelFoodInCart(0, order.getFoodName(), order.getAmount(), order.getPrice()+""));
                        }
                        final Dialog dialog = new Dialog(ShopBillManagerActivity.this);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.layout_customer_current_cart);
                        dialog.show();

                        rvListCart = dialog.findViewById(R.id.rvList);
                        LinearLayoutManager layoutManagerCart = new LinearLayoutManager(dialog.getContext());
                        RecyclerView.LayoutManager rvLayoutManagerCart = layoutManagerCart;
                        rvListCart.setLayoutManager(rvLayoutManagerCart);

                        FoodInCartAdapter adapterCart = new FoodInCartAdapter(dialog.getContext(), lstModelFoodsInCart, false);
                        rvListCart.setAdapter(adapterCart);

                        Button btn_ok = dialog.findViewById(R.id.btn_ok);
                        btn_ok.setText("Xác nhận");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lstOldBills.get(position).setStatus(1);
                                database.getReference("bill").child(finalStrCurDate).child(lstOldBills.get(position).getBillCode()).setValue(lstOldBills.get(position));
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //do sth
                loadData();
                handler.postDelayed(runnable, 4000);
            }
        };
        handler.postDelayed(runnable, 4000);
        //handler.removeCallbacks(runnable);
    }

    private void printMsg(String msg){
        Toast.makeText(ShopBillManagerActivity.this, msg,Toast.LENGTH_SHORT).show();
    }

    private void loadData(){
        Date curDate = new Date();
        String strCurDate = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
        strCurDate = strCurDate.replace("-","");
        String finalStrCurDate = strCurDate;
        database.getReference("bill").child(strCurDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstNewBills.clear();

                recyclerView = findViewById(R.id.rvList);

                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Bill bill = postSnapshot.getValue(Bill.class);
                    if(bill.getShopCode().equals(phone) && bill.getStatus() == 0){
                        lstNewBills.add(bill);
                    }
                }

                if(lstNewBills.size() != lstOldBills.size()){
                    lstOldBills.clear();
                    lstOrders.clear();

                    for (Bill bill: lstNewBills) {
                        lstOldBills.add(bill);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ShopBillManagerActivity.this);
                    RecyclerView.LayoutManager layoutManager1 =  layoutManager;
                    recyclerView.setLayoutManager(layoutManager1);

                    BillAdapter adapter = new BillAdapter(ShopBillManagerActivity.this, lstOldBills);

                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new BillAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            lstModelFoodsInCart.clear();
                            lstOrders = lstOldBills.get(position).getOrdersList();
                            for(Order order: lstOrders){
                                lstModelFoodsInCart.add(new ModelFoodInCart(0, order.getFoodName(), order.getAmount(), order.getPrice()+""));
                            }
                            final Dialog dialog = new Dialog(ShopBillManagerActivity.this);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.layout_customer_current_cart);
                            dialog.show();

                            rvListCart = dialog.findViewById(R.id.rvList);
                            LinearLayoutManager layoutManagerCart = new LinearLayoutManager(dialog.getContext());
                            RecyclerView.LayoutManager rvLayoutManagerCart = layoutManagerCart;
                            rvListCart.setLayoutManager(rvLayoutManagerCart);

                            FoodInCartAdapter adapterCart = new FoodInCartAdapter(dialog.getContext(), lstModelFoodsInCart, false);
                            rvListCart.setAdapter(adapterCart);

                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
                            btn_ok.setText("Xác nhận");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    lstOldBills.get(position).setStatus(1);
                                    database.getReference("bill").child(finalStrCurDate).child(lstOldBills.get(position).getBillCode()).setValue(lstOldBills.get(position));
                                    printMsg("Đã xác nhận!");
                                    dialog.cancel();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopBillManagerActivity.this, ShopMainActivity.class);
        intent.putExtra("phone", phone);
        handler.removeCallbacks(runnable);
        startActivity(intent);
        finish();
    }


    private void ControlButton() {
    }

    private void Mapping() {
    }
}