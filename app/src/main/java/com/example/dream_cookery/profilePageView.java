package com.example.dream_cookery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_cookery.Models.Classes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profilePageView extends AppCompatActivity {

    private TextView nameText;
    FirebaseAuth fBaseAuth;
    String currentID, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView purchaseHistory = findViewById(R.id.purchase_history_text);
        purchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profilePageView.this, purchaseHistoryView.class);
                startActivity(intent);
            }
        });

        ImageButton backPress = findViewById(R.id.backProfile);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profilePageView.this, MainClassView.class);
                startActivity(intent);
            }
        });

        fBaseAuth = FirebaseAuth.getInstance();
        currentID = fBaseAuth.getCurrentUser().getUid();
        nameText = findViewById(R.id.customerName);

    }

    private void userInfoDisplay() {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        UsersRef.child(currentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    userName = dataSnapshot.child("username").getValue(String.class);
                    Log.d("hello", userName);
                    nameText.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfoDisplay();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }
    public void onGroupItemClick(MenuItem item) {
    }

    public void goToProfileDetail(View arg0) {
        Intent it1 = new Intent(getApplicationContext(), editProfileView.class);
        startActivity(it1);
    }

    public void goToPurchaseHistory(View arg0) {
        Intent it2 = new Intent(getApplicationContext(), purchaseHistoryView.class);
        startActivity(it2);
    }

    public void goToAboutUs(View arg0) {
        Intent it3 = new Intent(getApplicationContext(), aboutUsView.class);
        startActivity(it3);
    }

    public void paymentmethod(View view)
    {
        Intent payment = new Intent(this, PaymentMethod.class);
        startActivity(payment);
    }

    public void logOut(View arg0) {

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("stayLogged","false");
        editor.apply();

        fBaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

}


