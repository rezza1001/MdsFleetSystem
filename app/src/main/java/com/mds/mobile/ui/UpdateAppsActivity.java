package com.mds.mobile.ui;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mds.mobile.R;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.ui.driver.secure.DriverBaseUi;

public class UpdateAppsActivity extends DriverBaseUi{

    private String linkUrl = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_updateapps;
    }

    @Override
    protected void onMyCreate() {
        TextView txvw_desc_00 = findViewById(R.id.txvw_desc_00);
        String note = "Aplikasi #A1 telah berganti versi. Untuk menggunakan #A1 silahkan download aplikasi terbaru terlebih dahulu";
        note = note.replaceAll("#A1",getResources().getString(R.string.app_name));
        txvw_desc_00.setText(note);

        findViewById(R.id.mrly_download_00).setOnClickListener(v -> {
            if (linkUrl.isEmpty()){
                Toast.makeText(this,"Apk URL not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(linkUrl));
            startActivity(viewIntent);
            UpdateAppsActivity.this.finish();
        });

        load();
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    private void load(){
        Loading.showLoading(this,"Load link apk. Please wait..");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("CONFIG").document("apk");
        docRef.get().addOnCompleteListener(task -> {
            Loading.cancelLoading();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    linkUrl = document.getString("link");
                } else {
                    Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Failed "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
