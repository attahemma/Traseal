package com.pyropy.travelseal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText title;
    private EditText price;
    private EditText description;
    TravelDeal mTravelDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        FirebaseUtil.openFbReference(getString(R.string.firebase_reference));
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.deal_price);
        description = (EditText) findViewById(R.id.deal_description);

        Intent intent = getIntent();
        TravelDeal deal = intent.getParcelableExtra("Deal");
        if (deal==null){
            deal = new TravelDeal();
        }
        mTravelDeal = deal;
        title.setText(deal.getTitle());
        price.setText(deal.getPrice());
        description.setText(deal.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
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
}