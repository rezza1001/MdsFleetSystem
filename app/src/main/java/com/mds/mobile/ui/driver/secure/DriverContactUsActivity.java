package com.mds.mobile.ui.driver.secure;

import androidx.appcompat.app.AppCompatActivity;

import com.mds.mobile.R;

public class DriverContactUsActivity extends DriverBaseUi {

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

    }

}
