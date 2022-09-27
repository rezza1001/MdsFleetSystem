package com.mds.mobile.remote.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.remote.ServiceGenerator;

public class PostManager extends AsyncTask<String, String, String> {

    private static final String TAG = "PostManager";
    private static final String SPARATOR = "M0C1-14";
    private String mainUrl = ServiceGenerator.API_URL;
    private String apiUrl;
    private JSONObject mData        = new JSONObject();
    private final ArrayList<Bundle> headerParams = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private boolean showLoading     = true;
    private Date dateStart;

    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public PostManager(Context mContext, String apiUrl) {
        this.apiUrl = apiUrl;
        context = mContext;
        Loading.showLoading(mContext,"Loading..");
    }


    public void exGet(){
        execute("GET");
    }

    public void exPost(){
        execute("POST");
    }

    public void exDelete(){
        execute("DELETE");
    }

    public void showloading(boolean show){
        showLoading = show;
    }

    public void setData(JSONObject jo) {
        mData = jo;
    }


    public void addHeaderParam(String key, String value){
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("value", value);
        headerParams.add(bundle);
    }


    public void addParam(String key, int value){
        if (context == null){
            return;
        }
        try {
            mData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addParam(String key, String value){
        if (context == null){
            return;
        }
        try {
            mData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addParam(String key, double value){
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        try {
            mData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addParam(String key, boolean value){
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        try {
            mData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addParam(String key, JSONObject data){
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        try {
            mData.put(key, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getParamData(){
        return mData;
    }

    public void addParam(String key, JSONArray data){
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        try {
            mData.put(key, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        dateStart = new Date();
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        Utility.LogDbug(TAG,"onPreExecute with loading : "+ showLoading);
        Loading.showLoading(context,"Loading");

        super.onPreExecute();
    }

    protected String doInBackground(String... arg0) {

        StringBuilder sbResponse = new StringBuilder();
        try {

            String type = arg0[0];
            if (type.equals("GET")) {
                StringBuilder param = new StringBuilder();
                if (headerParams.size() > 0){
                    param = new StringBuilder("&");
                }
                for (Bundle bundle : headerParams) {
                    param.append(bundle.getString("key")).append("=").append(bundle.getString("value")).append("&");
                }
                apiUrl = apiUrl + param;
            }
            Log.d(TAG,"API : "+mainUrl +apiUrl);
            URL url = new URL(mainUrl +apiUrl); //Enter URL here
            String host = url.getHost();
            InetAddress address = InetAddress.getByName(host);
            String ip = address.getHostAddress();
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(type); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            if (type.equals("POST") || type.equals("PUT")){
                httpURLConnection.setDoOutput(true);
            }
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.setRequestProperty("Accept", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            DateFormat format = new SimpleDateFormat("dd", Locale.getDefault());
            byte[] hash = digest.digest((ServiceGenerator.API_SECRET_KEY+format.format(new Date())).getBytes(StandardCharsets.UTF_8));
            String token =  bytesToHex(hash);
            mData.put("token",token);

            try {
                httpURLConnection.connect();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Utility.LogDbug(TAG,type+ " "+url+" "+apiUrl);
            if (type.equals("POST")|| type.equals("PUT")){
                Utility.LogDbug(TAG,"HOST : "+host+", InetAddress : "+address+", IP : "+ip);
                Utility.LogDbug(TAG,"data "+ " : "+ mData.toString());
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(mData.toString());
                wr.flush();
                wr.close();
            }


            int responseCode = httpURLConnection.getResponseCode();
            Utility.LogDbug(TAG,"RESPONSE CODE : "+ responseCode);
            sbResponse.append(responseCode).append(SPARATOR);
            BufferedReader in;
            if (responseCode == ErrorCode.OK_200 || responseCode == ErrorCode.CODE_201){
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }
            else if (responseCode == ErrorCode.BLOCK){
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }
            else {
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                Utility.LogDbug(TAG,httpURLConnection.getResponseMessage());
            }


            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            sbResponse.append(response.toString());
            return sbResponse.toString();

        } catch (Exception e) {
            e.printStackTrace();
            sbResponse.append("Request time out");
            Loading.cancelLoading();
            return sbResponse.toString();
        }

    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    protected void onPostExecute(String presults) {
        Utility.LogDbug(TAG,"onPostExecute "+ presults.length());
        if (context == null){
            Utility.LogDbug(TAG,"Request Canceled !!!");
            return;
        }
        Date date1 = new Date();
        long diff = date1.getTime() - dateStart.getTime();
        Utility.LogDbug(TAG,"TOTAL TIME : "+ diff+" Seconds");

        Loading.cancelLoading();
        try {

            String results = presults.split(SPARATOR)[1];
            int code    =  Integer.parseInt(presults.split(SPARATOR)[0]);

            if (results != null){
                Utility.LogDbug(TAG,"TEXT RESPONSE "+this.apiUrl +" | "+ results +" | HEADER CODE : "+ code);
                if (results.equals("Request time out")){
                    Toast.makeText(context,"Request time out", Toast.LENGTH_SHORT).show();
                    mReceiveListener.onReceive(null, ErrorCode.TIME_OUT, "Time Out");
                    return;
                }
                try {
                    JSONObject jo;
                    if (results.startsWith("[")){
                        jo = new JSONObject();
                        jo.put("data", new JSONArray(results));
                    }
                    else {
                        jo   = new JSONObject(results);
                    }


                    String status   = jo.has("errCode") ? jo.getString("errCode"): "0";
                    code = Integer.parseInt(status);
                    if (jo.has("code")){
                        code = jo.get("code") instanceof Integer ? jo.getInt("code") : ErrorCode.FAILED;
                    }
                    String message  = jo.has("errMsg") ? jo.getString("errMsg"): "";
                    if (jo.has("message")){
                        message = jo.getString("message");
                    }

                    mReceiveListener.onReceive(jo, code,message);
                } catch (JSONException e) {
                    sendError(e,presults);
                    mReceiveListener.onReceive(null, ErrorCode.CODE_UNPROCESSABLE_ENTITY, "Undefined");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            sendError(e,presults);
            if (mReceiveListener != null){
                mReceiveListener.onReceive(null, ErrorCode.UNDIFINED_ERROR,"Error Connection "+ e.getMessage());
            }
            Toast.makeText(context, "Error Connection "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void sendError(Exception e, String responseData){
        if (dateStart == null){
            dateStart = new Date();
        }
        Log.d(TAG,"SEND Error ---- >");
        UserProfile userProfile = Global.userProfile;
        long diff = new Date().getTime() - dateStart.getTime();
        MyDevice device = new MyDevice(context);
        JSONObject joDevice = new JSONObject();
        try {
            joDevice.put("name",device.getDeviceName());
            joDevice.put("version",device.getVersion());
            joDevice.put("os",device.getOs());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("apiUrl", mainUrl +""+ apiUrl);
        data.put("param", mData.toString());
        data.put("user_id", userProfile.getUserId());
        data.put("username", userProfile.getUsername());
        data.put("versionApps",device.getVersion());
        data.put("device",joDevice.toString());
        data.put("time", format.format(new Date()));
        data.put("timemillis", System.currentTimeMillis());
        data.put("long_time", diff+"");
        data.put("date", format.format(new Date()));
        String response = "-";
        if (e.getMessage() != null){
            if (Objects.requireNonNull(e.getMessage()).length() > 100){
                response = e.getMessage().substring(0,100);
            }
            else {
                response = e.getMessage();
            }
        }
        if (responseData.length() > 500){
            responseData = responseData.substring(0,500);
        }

        Calendar calendar = Calendar.getInstance();
        data.put("error", response);
        data.put("response", responseData);
        db.collection("DBUG_"+calendar.get(Calendar.YEAR)+"_"+calendar.get(Calendar.MONTH))
                .add(data)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e1 -> Log.w(TAG, "Error adding document", e1));
    }

    private onReceiveListener mReceiveListener;
    public void setOnReceiveListener(onReceiveListener mReceiveListener){
        this.mReceiveListener = mReceiveListener;
    }
    public interface onReceiveListener{
        void onReceive(JSONObject obj, int code, String message);
    }

}