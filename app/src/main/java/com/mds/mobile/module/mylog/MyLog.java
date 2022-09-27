package com.mds.mobile.module.mylog;

import android.util.Log;

/**
 * Created by pho0890910 on 8/4/2018.
 */
public class MyLog {

    public static final String C_TAB_LOG = "MNCLEASING";

    public static void debug( String p_strMsg )
    {
        String l_strMsg = "** debug ** " + p_strMsg;
        Log.d(C_TAB_LOG, l_strMsg);
    }

    public static void info( String p_strMsg )
    {
        String l_strMsg = "** Info ** " + p_strMsg;
        Log.i(C_TAB_LOG, l_strMsg);
    }

    public static void warn( String p_strMsg )
    {
//        String l_strMsg = "** Warn ** " + p_strMsg;
//        Log.w(C_TAB_LOG, l_strMsg);
    }

    public static void error( String p_strMsg, Throwable tr )
    {
//        String l_strMsg = "** Error ** " + p_strMsg;
//        Log.e(C_TAB_LOG, l_strMsg, tr);
    }

}
