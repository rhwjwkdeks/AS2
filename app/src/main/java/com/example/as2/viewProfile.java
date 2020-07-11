package com.example.as2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class viewProfile extends AppCompatActivity {

    private Button Back;
    private Button Edit;

    TextView name, ad1, ad2, city, state, zip;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        name = findViewById(R.id.nameTextView);
        ad1 = findViewById(R.id.ad1TextView);
        ad2 = findViewById(R.id.ad2TextView);
        city = findViewById(R.id.cityTextView);
        state = findViewById(R.id.stateTextView);
        zip = findViewById(R.id.zipTextView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("FullName"));
                ad1.setText(documentSnapshot.getString("Address1"));
                ad2.setText(documentSnapshot.getString("Address2"));
                city.setText(documentSnapshot.getString("City"));
                state.setText(documentSnapshot.getString("State"));
                zip.setText(documentSnapshot.getString("Zip"));
            }
        });

        Back = (Button) findViewById(R.id.backButton);
        Edit = (Button) findViewById(R.id.editProfileButton);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEdit();
            }
        });
    }

    public void goBack(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void goEdit(){
        Intent intent = new Intent(this, Clientprofile.class);
        startActivity(intent);
    }
}