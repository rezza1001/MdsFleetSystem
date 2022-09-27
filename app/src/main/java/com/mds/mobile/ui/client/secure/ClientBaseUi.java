package com.mds.mobile.ui.client.secure;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.ClientContactUsItem;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.ui.BaseSecureUi;
import com.mds.mobile.ui.driver.secure.DriverDashboardActivity;
import com.mds.mobile.ui.driver.secure.DriverFaqActivity;
import com.mds.mobile.ui.driver.secure.DriverNotificationActivity;

public abstract class ClientBaseUi extends BaseSecureUi implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected abstract Integer getNavigationMenuItemIndex();

    BottomNavigationView navigationView;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, ClientDashboardActivity.class));
                return true;
            case R.id.nav_info:
                startActivity(new Intent(this, ClientInformationActivity.class));
                return true;
            case R.id.nav_promo:
                startActivity(new Intent(this, ClientPromoActivity.class));
                return true;
            case R.id.nav_contact_us:
                startActivity(new Intent(this, ClientContactUsActivity.class));
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(this, ClientProfileActivity.class));
                return true;
        }

        return false;
    }

    @Override
    protected void onMyBaseCreate() {
        configureBottomNavigationView();
    }

    void configureBottomNavigationView(){

        MyLog.info("configureBottomNavigationView getNavigationMenuItemIndex: "+getNavigationMenuItemIndex());

        navigationView = findViewById(R.id.bottom_nav_bar);
        if(navigationView!=null) {
            navigationView.setItemIconTintList(null);
            if(getNavigationMenuItemIndex()!=null){
                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(getNavigationMenuItemIndex());
                if(menuItem!=null) menuItem.setChecked(true);
            } else {
                Menu menu = navigationView.getMenu();
                int size = menu.size();
                for (int i = 0; i < size; i++)
                {
                    menu.getItem(i).setCheckable(false);
                }
            }
            navigationView.setOnNavigationItemSelectedListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyLog.info("onStart getNavigationMenuItemIndex: "+getNavigationMenuItemIndex());

        if(navigationView!=null) {

            MyLog.info("onStart navigationView != null ");

            if(getNavigationMenuItemIndex()!=null){

                MyLog.info("onStart getNavigationMenuItemIndex != null ");

                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(getNavigationMenuItemIndex());
                if(menuItem!=null) menuItem.setChecked(true);

            } else {
                Menu menu = navigationView.getMenu();
                int size = menu.size();
                for (int i = 0; i < size; i++)
                {
                    menu.getItem(i).setCheckable(false);
                }
            }
        }
    }

    @Override
    protected void backToMainScreen() {
        Intent loginScreen = new Intent(this, ClientDashboardActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginScreen);

    }

    @Override
    protected void showFaq() {
        startActivity(new Intent(this, ClientFaqActivity.class));
    }

    @Override
    protected void showNotification() {
        startActivity(new Intent(this, ClientNotificationActivity.class));
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {

    }

    @Override
    protected void onSuccessReceived(Object entity) {

    }

}
