package com.mds.mobile.remote.post;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.mds.mobile.BuildConfig;
import com.mds.mobile.R;
import com.mds.mobile.util.MyToast;

/**
 * Created by Mochamad Rezza Gumilang on 19/Oct/2021.
 * Class Info :
 */

public class Utility {

    public static void LogDbug(String tag, String message){
        if (BuildConfig.DEBUG){
            Log.d(tag, message);
        }
    }

    public static boolean hasPermission(Activity activity, String[] permissions){
        if(!hasPermissions(activity, permissions)){
            ActivityCompat.requestPermissions(Objects.requireNonNull(activity), permissions, 32);
            return false;
        }
        else {
            return true;
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Utility","hasPermissions "+ permission+" false");
                    return false;
                }
            }
        }
        return true;
    }

    public static String toCurrency(String currencyName, String pData){
        boolean isNegative = false;
        try {
            double dData = Double.parseDouble(pData);
            if (dData < 0){
                isNegative = true;
                dData = (dData * -1);
            }
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(dData);
            data = data.substring(1,(data.length()-3));

            if (isNegative){
                return "- "+currencyName+" "+data;
            }
            return currencyName+" "+data;
        }catch (Exception e){
            return "0";
        }

    }

    public static String toCurrency(String currencyName, long pData){
        boolean isNegative = false;
        try {
            double dData = Double.parseDouble(pData+"");
            if (dData < 0){
                isNegative = true;
                dData = (dData * -1);
            }
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(dData);
            data = data.substring(1,(data.length()-3));
            if (isNegative){
                return "- "+currencyName+" "+data;
            }
            return currencyName+" "+data;
        }catch (Exception e){
            return "0";
        }
    }

    public static Date getDate(String pDate, String formatCfg){
        DateFormat format = new SimpleDateFormat(formatCfg, new Locale("id"));
        try {
            return format.parse(pDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getDateString(Date pDate, String formatCfg){
        DateFormat format = new SimpleDateFormat(formatCfg, new Locale("id"));
        return format.format(pDate);
    }

    public static void showToastError(Context context, String message){
        MyToast myToast = new MyToast(context, null);
        myToast.setIcon(R.drawable.ic_clear, Color.RED);
        myToast.setMessage(message);
        myToast.show(Toast.LENGTH_LONG, Gravity.CENTER, 0,0);
    }
    public static void showToastSuccess(Context context, String message){
        MyToast myToast = new MyToast(context, null);
        myToast.setIcon(R.drawable.ic_check, Color.GREEN);
        myToast.setMessage(message);
        myToast.show(Toast.LENGTH_LONG, Gravity.CENTER, 0,0);
    }

    public static ShapeDrawable getOvalBackground(String color_code){
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor("#"+color_code));
        return shapeDrawable;
    }

    public static ShapeDrawable getRectBackground(Context context,String color_code, int radius){
        radius = Utility.dpToPx(context, radius);
        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                radius, radius, radius, radius,
                radius, radius, radius, radius}, null, null);

        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#"+color_code));
        return shapeDrawable;
    }
    public static ShapeDrawable getRectBackground(String color_code, int leftTop, int rightTop, int leftBottom, int rightBottom){
        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                leftTop, leftTop, rightTop, rightTop,
                leftBottom, leftBottom, rightBottom ,rightBottom}, null, null);

        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#"+color_code));
        return shapeDrawable;
    }

    public static Drawable getRectBackground(int color_code, int linesize, int linecolor, int leftTop, int rightTop, int leftBottom, int rightBottom){
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadii(new float[] { leftTop, rightTop, leftBottom, rightBottom, 0, 0, 0, 0 });
        bg.setColor(color_code);
        bg.setStroke(linesize ,linecolor);
        return bg;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static Bitmap compressImage(String sourceImagePath, String destinationImagePath) throws IOException {
        Bitmap sourceBitmap = BitmapFactory.decodeFile(sourceImagePath);
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int quality = 80;
        FileOutputStream fos = new FileOutputStream(destinationImagePath);
        sourceBitmap.compress(compressFormat, quality, fos);
        fos.close();

        return BitmapFactory.decodeFile(destinationImagePath);
    }

}
