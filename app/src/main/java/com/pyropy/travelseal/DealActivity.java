package com.pyropy.travelseal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText title;
    private EditText price;
    private EditText description;
    TravelDeal mTravelDeal;
    private static final int PICTURE_RESULT = 42;
    private ImageView mDealImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        FirebaseUtil.openFbReference(getString(R.string.firebase_reference),this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.deal_price);
        description = (EditText) findViewById(R.id.deal_description);
        ImageButton btn = (ImageButton) findViewById(R.id.upload_btn);
        mDealImg = (ImageView) findViewById(R.id.deal_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent,"Insert Deal Cover"),PICTURE_RESULT);
            }
        });

        Intent intent = getIntent();
        TravelDeal deal = intent.getParcelableExtra("Deal");
        if (deal==null){
            deal = new TravelDeal();
        }
        mTravelDeal = deal;
//        ActivityDealBinding uDealBinding = DataBindingUtil.setContentView(this,R.layout.activity_deal);
//        uDealBinding.setDeal(mTravelDeal);
        title.setText(deal.getTitle());
        price.setText(deal.getPrice());
        description.setText(deal.getDescription());
        if (deal.getImageUrl() == null) {

        }else{
            Log.d("Deal Image",deal.getImageUrl());
            showImage(deal.getImageUrl());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        if (FirebaseUtil.isadmin){
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
            enableEditTexts(true);
        }else{
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
            enableEditTexts(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this,"Deal Saved!", Toast.LENGTH_SHORT).show();
                clean();
                return true;
            case R.id.delete:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_SHORT).show();
                clean();
                return true;
            case R.id.backtolist:
                returnToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        title.setText("");
        price.setText("");
        description.setText("");
        title.requestFocus();
    }

    private void saveDeal() {
        mTravelDeal.setTitle(title.getText().toString());
        mTravelDeal.setPrice(price.getText().toString());
        mTravelDeal.setDescription(description.getText().toString());

        if (mTravelDeal.getId() == null){
            mDatabaseReference.push().setValue(mTravelDeal);
        }
        else{
            mDatabaseReference.child(mTravelDeal.getId()).setValue(mTravelDeal);
        }
    }

    private void deleteDeal(){
        if (mTravelDeal.getId() == null){
            Toast.makeText(this,"Can't delete new Deal", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            mDatabaseReference.child(mTravelDeal.getId()).removeValue();
        }
    }
    private void returnToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
    private void enableEditTexts(boolean isEnabled){
        title.setEnabled(isEnabled);
        price.setEnabled(isEnabled);
        description.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imgUrl = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageReference.child(imgUrl.getLastPathSegment());
            UploadTask uUploadTask = ref.putFile(imgUrl);
//            uUploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String url = taskSnapshot.getMetadata().getPath();
//                    String downloadUrl = ref.getDownloadUrl().toString();
//                    mTravelDeal.setImageUrl(downloadUrl);
//                }
//            });
            Task<Uri> uUriTask = uUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Upload failed", Toast.LENGTH_SHORT).show();
                    }

//                    Log.d("Upload Url", ref.getDownloadUrl().toString());
//                    mTravelDeal.setImageUrl(ref.getDownloadUrl().toString());
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri uUri  = task.getResult();
                        mTravelDeal.setImageUrl(uUri.toString());
                        Log.d("Suspected Upload Url", uUri.toString());
                        showImage(uUri.toString());
                    }
                }
            });
        }
    }

//    public void uploadImage(View view) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/jpeg");
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        startActivityForResult(intent.createChooser(intent,"Insert Deal Cover"),PICTURE_RESULT);
//    }

    public void showImage(String url){
        if (url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels - 10;
            Picasso.get().load(url).resize(width, width*2/3).centerCrop().placeholder(android.R.drawable.gallery_thumb).into(mDealImg);
        }

    }
}