package com.pyropy.travelseal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    //private TextView mTvDeals;
    //private RecyclerView mTvDealsRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FirebaseUtil.openFbReference(getString(R.string.firebase_reference));

        //mTvDealsRecycle = (RecyclerView) findViewById(R.id.rvDeals);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);


//        mTvDeals = (TextView) findViewById(R.id.tvDeals);
//        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
//        mDatabaseReference = FirebaseUtil.mDatabaseReference;
//
//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                TravelDeal trDeals = snapshot.getValue(TravelDeal.class);
//                mTvDeals.setText(mTvDeals.getText() + "\n" + trDeals.getTitle() +"  -----------  "+trDeals.getPrice());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                //TravelDeal trDeals = snapshot.getValue(TravelDeal.class);
//                //mTvDeals.setText(trDeals.getTitle() +"  -----------  "+trDeals.getPrice()+"\n");
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        mDatabaseReference.addChildEventListener(mChildEventListener);
        RecyclerView travelDealsRecycle = (RecyclerView) findViewById(R.id.rvDeals);
        LinearLayoutManager rvManager = new LinearLayoutManager(this);
        travelDealsRecycle.setLayoutManager(rvManager);
        final DealAdapter dealAdapter = new DealAdapter(this);
        travelDealsRecycle.setAdapter(dealAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_deal:
                addDeal(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDeal(Context c) {
        Intent intent = new Intent(c,InsertActivity.class);
        startActivity(intent);
    }
}