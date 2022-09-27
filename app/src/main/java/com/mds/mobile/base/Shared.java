package com.mds.mobile.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.mds.mobile.model.UserProfile;

public class Shared {

//    private static ContextWrapper instance;
//    private static SharedPreferences pref;
//
//    private static final String PREF_NAME = "MncLeasing";
//    private static final String IS_LOGIN = "IsLoggedIn";
//
//    public static final String KEY_USERNAME = "USERNAME";
//    public static final String KEY_USERCODE = "USERCODE";
//    public static final String KEY_USERROLE = "USERROLE";
//    public static final String KEY_NAME = "NAME";
//    public static final String KEY_USERID = "USERID";
//
//    public static void initialize( Context base )
//    {
//        instance = new ContextWrapper(base);
//        pref = instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//    // todo. tambahin context dimasing2
//    public static boolean isLoggedIn(){
//        return pref.getBoolean(IS_LOGIN, false);
//    }
//
//    /**
//     * Get stored session data
//     * */
//    public static UserProfile getUserProfile(){
//
//        String username = pref.getString(KEY_USERNAME, null);
//        String userCode = pref.getString(KEY_USERCODE, null);
//        String userRole = pref.getString(KEY_USERROLE, null);
//        String userId = pref.getString(KEY_USERID, null);
//        String name = pref.getString(KEY_NAME, null);
//
//        UserProfile user = new UserProfile(username, userCode, name, userRole, userId);
//
//        return user;
//    }
//
//    public static void createLoginSession(UserProfile profile){
//        // Storing login value as TRUE
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean(IS_LOGIN, true);
//        editor.putString(KEY_NAME, profile.getName());
//        editor.putString(KEY_USERNAME, profile.getUsername());
//        editor.putString(KEY_USERCODE, profile.getUserCode());
//        editor.putString(KEY_USERROLE, profile.getUserRole());
//        editor.putString(KEY_USERID, profile.getUserId());
//
//        // commit changes
//        editor.commit();
//    }
//
//    public static void clear()
//    {
//        SharedPreferences.Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();
//    }

}
