package com.mds.mobile.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mds.mobile.R;
import com.mds.mobile.module.loading.Loading;

public class OpenContactUsActivity extends AppCompatActivity {

    private TextView txvw_phone,txvw_call,txvw_email,txvw_work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_contact_us);

        txvw_phone = findViewById(R.id.txvw_phone);
        txvw_call = findViewById(R.id.txvw_call);
        txvw_email = findViewById(R.id.txvw_email);
        txvw_work = findViewById(R.id.txvw_work);

        loadData();
    }

    private void loadData(){
        Loading.showLoading(this,"Please wait..");
        txvw_phone.setText(null);
        txvw_call.setText(null);
        txvw_email.setText(null);
        txvw_work.setText(null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("CONFIG").document("help");
        docRef.get().addOnCompleteListener(task -> {
            Loading.cancelLoading();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    txvw_call.setText(document.getString("call"));
                    txvw_phone.setText(document.getString("phone"));
                    txvw_email.setText(document.getString("email"));
                    txvw_work.setText(document.getString("work_time"));
                } else {
                    Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Failed "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
