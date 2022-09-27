package com.mds.mobile.ui.driver.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.faq.request.GetFaqRequestEntity;
import com.mds.mobile.remote.entity.faq.response.GetFaqResponseEntity;
import com.mds.mobile.remote.entity.faq.response.ResponseDatum;
import com.mds.mobile.remote.service.CustomerInformationServiceClient;
import retrofit2.Call;

public class DriverFaqActivity extends DriverBaseUi {

    final static String rowTemplate = "<button class=\"accordion\">" + "##TITLE##" +"</button><div class=\"panel\"><p>" + "##DESC##" +"</p></div>\n";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_faq;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {
        Loading.showLoading(this, "Loading ...");
        callJSONGetFaq();

    }

    private void callJSONGetFaq() {
        GetFaqRequestEntity ent = new GetFaqRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("faq");
        ent.setUserCode(getUserProfile().getUserCode());

        CustomerInformationServiceClient serviceClient = ServiceGenerator.createService(CustomerInformationServiceClient.class);

        Call<GetFaqResponseEntity> call = serviceClient.faq(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_faq),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetFaqResponseEntity) {
            GetFaqResponseEntity resp = (GetFaqResponseEntity) entity;

            MyLog.info("Promo respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("Promo respEntity getResponseType " + resp.getResponseType());
            MyLog.info("Promo respEntity getResponseMessage " + resp.getResponseMessage());

            List<ResponseDatum> list = resp.getResponseData();

            StringBuffer sbContent = new StringBuffer("");

            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getQuestion());

                String tempContent = rowTemplate;
                tempContent = tempContent.replace("##TITLE##",temp.getQuestion() ).replace("##DESC##", temp.getAnswer());
                sbContent.append(tempContent);
            }

            WebView webView = findViewById(R.id.wv_content);

            WebSettings webSetting = webView.getSettings();

            webSetting.setAllowFileAccess(true);
            webSetting.setAllowFileAccessFromFileURLs(true);
            webSetting.setAllowUniversalAccessFromFileURLs(true);

            webSetting.setBuiltInZoomControls(false);
            webSetting.setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("file:///android_asset/faq_content.html");

            StringBuilder result = new StringBuilder();
            try {
                InputStream is = getAssets().open("faq_content.html");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));
                String str;
                while ((str = br.readLine()) != null) {
                    result.append(str);
                }
                br.close();
            } catch (Exception e){

            }

            String strResult = result.toString();
            strResult = strResult.replaceFirst("##ADI##", sbContent.toString());

            webView.loadDataWithBaseURL(null,strResult,"text/html", "utf-8", null);

        }

        Loading.cancelLoading();

    }

    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}
