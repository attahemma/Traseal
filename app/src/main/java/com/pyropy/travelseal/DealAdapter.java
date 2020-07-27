package com.pyropy.travelseal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    Context mContext;
    //List<TravelDeal> mTravelDeals;
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ImageView mImageDeal;

    public DealAdapter(Context context){
        mContext = context;
        FirebaseUtil.openFbReference(mContext.getString(R.string.firebase_reference),(Activity) mContext);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        deals = FirebaseUtil.mTravelDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                TravelDeal trDeals = snapshot.getValue(TravelDeal.class);
                Log.d("Deal:", trDeals.getTitle());
                trDeals.setId(snapshot.getKey());
                deals.add(trDeals);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //TravelDeal trDeals = snapshot.getValue(TravelDeal.class);
                //mTvDeals.setText(trDeals.getTitle() +"  -----------  "+trDeals.getPrice()+"\n");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.deal_item,parent,false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal travelDeal = deals.get(position);
        holder.bind(travelDeal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public  class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        String price="Cost: N";
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mImageDeal = (ImageView) itemView.findViewById(R.id.deal_img);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal travelDeal){
            tvTitle.setText(travelDeal.getTitle());
            String description = travelDeal.getDescription();
            StringBuilder shortenedDescription= new StringBuilder();
            if (description.length() > 70) {
                shortenedDescription.append(description.substring(0, 70));
                shortenedDescription.append("...");
            }else{
                shortenedDescription.append(description);
            }
            tvDescription.setText(shortenedDescription.toString());
            price = travelDeal.getPrice()==null? price + "In Review": price + travelDeal.getPrice();
            tvPrice.setText(price);
            showImage(travelDeal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Item Clicked", String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal",selectedDeal);
            view.getContext().startActivity(intent);
        }

        public void showImage(String url){
            if (url != null && url.isEmpty() == false){
                //int width = Resources.getSystem().getDisplayMetrics().widthPixels - 10;
                Picasso.get().load(url).resize(140, 140).centerCrop().placeholder(android.R.drawable.gallery_thumb).into(mImageDeal);
            }
        }
    }
}
