package com.mds.mobile.module.loading;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by pho0890910 on 8/3/2018.
 */
public class Loading {

    private static ProgressDialog g_progressDialog;
//    private static Timer g_timer;
//
//    private static int g_intTimeOut = (60 * 1000) * 1;

//    public static void showLoading(Context p_cContext, String msg, ILoadingTimerListener p_loadingTimerListener )
//    {
//        showLoading(p_cContext, msg, p_loadingTimerListener, g_intTimeOut);
//    }

    public static void showLoading( Context p_cContext, String msg) //, ILoadingTimerListener p_loadingTimerListener, int p_intTimeOutTime )
    {
        cancelLoading();

//        progressTimer(p_loadingTimerListener, p_intTimeOutTime);

        g_progressDialog = new ProgressDialog(p_cContext);
        g_progressDialog.setTitle(null);
        g_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        g_progressDialog.setIndeterminate(true);
        g_progressDialog.setMessage(msg);
        g_progressDialog.setCancelable(false);
        g_progressDialog.show();
    }

    public static void cancelLoading()
    {
//        if (g_timer != null)
//        {
//            g_timer.cancel();
//            g_timer = null;
//        }
        if (g_progressDialog != null)
        {
            g_progressDialog.cancel();
            g_progressDialog = null;
        }
    }

//    private static void progressTimer( final ILoadingTimerListener p_loadingTimerListener, int p_intTimeOut )
//    {
//        g_timer = new Timer();
//        TimerTask g_timerTask = new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                p_loadingTimerListener.onLoadingTimerOut();
//            }
//        };
//        g_timer.schedule(g_timerTask, p_intTimeOut);
//    }

}
