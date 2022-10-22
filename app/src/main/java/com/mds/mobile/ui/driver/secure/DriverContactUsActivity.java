package com.mds.mobile.ui.driver.secure;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mds.mobile.R;
import com.mds.mobile.module.loading.Loading;

public class DriverContactUsActivity extends DriverBaseUi {

    private TextView txvw_tlp,txvw_phone,txvw_email,txvw_work;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_contact_us;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_contact_us;
    }

    @Override
    protected void onMyCreate() {
        txvw_tlp = findViewById(R.id.txvw_tlp);
        txvw_phone = findViewById(R.id.txvw_phone);
        txvw_email = findViewById(R.id.txvw_email);
        txvw_work = findViewById(R.id.txvw_work);
        loadData();
    }

    private void loadData(){
        Loading.showLoading(this,"Please wait..");
        txvw_phone.setText(null);
        txvw_tlp.setText(null);
        txvw_email.setText(null);
        txvw_work.setText(null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("CONFIG").document("help");
        docRef.get().addOnCompleteListener(task -> {
            Loading.cancelLoading();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    txvw_tlp.setText(document.getString("call"));
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
