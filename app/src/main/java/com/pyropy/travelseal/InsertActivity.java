package com.pyropy.travelseal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        String txtTitle = title.getText().toString();
        String txtPrice = price.getText().toString();
        String txtDescription = description.getText().toString();

        TravelDeal travelDeal = new TravelDeal(txtTitle,txtDescription, txtPrice,"");
        mDatabaseReference.push().setValue(travelDeal);
    }
}