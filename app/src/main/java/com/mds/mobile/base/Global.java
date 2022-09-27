package com.mds.mobile.base;

import android.content.Context;
import android.content.Intent;

import com.mds.mobile.model.UserProfile;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.ui.BaseUi;
import com.mds.mobile.ui.LoginActivity;

public class Global {

    public static final int REQUEST_OPEN_CAMERA = 12;
    public static String PATH_ABSENT = "absent";
    public static String PATH_IMAGES = "/"+ FileProcessing.ROOT+"/"+PATH_ABSENT+"/";

//    public static void startAppIniData( Context p_context)
//    {
//        Shared.initialize(p_context);
//    }

    public static UserProfile userProfile = null;

    public static void clearGlobalData() {
        userProfile = null;
    }

//    public static void setUserProfile(UserProfile tmpUserProfile){
//        userProfile = tmpUserProfile;
//    }

//    public static UserProfile getGlobalUserProfile(){
//
//        UserProfile temp = userProfile;
//
//        if(temp == null) {
//            try {
//                temp = Shared.getUserProfile();
//            } catch (Exception e){
//            }
//        }
//
//        return temp;
//
//    }
}
