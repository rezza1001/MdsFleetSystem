package com.mds.mobile.ui.client.secure;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mds.mobile.R;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.entity.customerinstallment.response.DetailResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.ResponseData;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.remote.post.Utility;
import com.mds.mobile.ui.DynamicDialog;
import com.mds.mobile.ui.client.secure.adapter.DocumentAdapter;
import com.mds.mobile.util.GlobalHelper;

public class ClientInformationDetailActivity extends ClientBaseUi {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_information_detail;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_info;
    }

    Activity mActivity;
    ImageButton ibBack;
    ArrayList<Bundle> listDocuments = new ArrayList<>();
    DocumentAdapter mAdapter;
    TextView txvw_empty,tvContract;
    private static final String ROOT_FILE = "document/";

    @Override
    protected void onMyCreate() {
        mActivity = ClientInformationDetailActivity.this;
        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);

        txvw_empty = findViewById(R.id.txvw_empty);
        RecyclerView rcvw_documents = findViewById(R.id.rcvw_documents);
        rcvw_documents.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new DocumentAdapter(listDocuments);
        rcvw_documents.setAdapter(mAdapter);



        DetailResponseEntity entity = (DetailResponseEntity)getIntent().getSerializableExtra("my_detail");
        String sisaTenor = getIntent().getStringExtra("sisa_tenor");
        if(entity==null){

        } else {
            MyLog.info("InformationDetailActivity respEntity getResponseCode " + entity.getResponseCode());
            MyLog.info("InformationDetailActivity respEntity getResponseType " + entity.getResponseType());
            MyLog.info("InformationDetailActivity respEntity getResponseMessage " + entity.getResponseMessage());

            TextView tvBranch = findViewById(R.id.tv_branch);
                    tvContract = findViewById(R.id.tv_contract);
            TextView tvContractDate = findViewById(R.id.tv_contract_date);
            TextView tvTenor = findViewById(R.id.tv_tenor);
            TextView tvAngsuran = findViewById(R.id.tv_angsuran);
            TextView tvCaraBayar = findViewById(R.id.tv_carabayar);
            TextView tvLastInstallment = findViewById(R.id.tv_lastinstallment);
            TextView tvNextInstallment = findViewById(R.id.tv_nextinstallment);
            TextView tvAsset = findViewById(R.id.tv_asset);
            TextView tvSisaTenor = findViewById(R.id.tv_sisa_tenor);
            TextView tvStatus = findViewById(R.id.tv_status);

            ResponseData data = entity.getResponseData();
            if(data == null){
                tvBranch.setText("");
                tvContract.setText("");
                tvContractDate.setText("");
                tvTenor.setText("");
                tvAngsuran.setText("");
                tvCaraBayar.setText("");
                tvLastInstallment.setText("");
                tvNextInstallment.setText("");
                tvAsset.setText("");
                tvSisaTenor.setText("");
                tvStatus.setText("");
            } else {

                tvSisaTenor.setText(sisaTenor + " Bulan");

//                Locale localeID = new Locale("in", "ID");
//                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd"); //  "contract_date": "2019-04-25",
                SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");

                String strContractDate = data.getContractDate();
                try {
                    if("0000-00-00".equals(strContractDate)){
                        strContractDate = "";
                    } else {
                        Date tempDate = yyyyMMdd.parse(data.getContractDate());
                        strContractDate = ddMMyyyy.format(tempDate);
                    }
                } catch (Exception e){
                    strContractDate = "";
                }

                String strLastDate = data.getLastPaymentDate();
                try {
                    if("0000-00-00".equals(strLastDate)){
                        strLastDate = "";
                    } else {
                        Date tempDate = yyyyMMdd.parse(data.getLastPaymentDate());
                        strLastDate = ddMMyyyy.format(tempDate);

                        String tmp = data.getLastPaymentPeriod();
                        if(tmp == null || "".equals(tmp)){
                            tmp = "";
                        }

                        strLastDate = strLastDate + " / " +tmp;
                    }
                } catch (Exception e){
                    strLastDate = "";
                }

                String strNexDate = data.getNextInstallmentDate();
                try {
                    if("0000-00-00".equals(strNexDate)){
                        strNexDate = "";
                    } else {
                        Date tempDate = yyyyMMdd.parse(data.getNextInstallmentDate());
                        strNexDate = ddMMyyyy.format(tempDate);

                        String tmp = data.getNextInstallmentPeriod();
                        if(tmp == null || "".equals(tmp)){
                            tmp = "";
                        }

                        strNexDate = strNexDate + " / " +tmp;

                    }
                } catch (Exception e){
                    strNexDate = "";
                }

                tvBranch.setText(data.getBranch());
                tvContract.setText(data.getAgreementId());
                tvContractDate.setText(strContractDate);
                tvTenor.setText(data.getTenor() + " Bulan");
//                tvAngsuran.setText(formatRupiah.format(Double.valueOf(data.getInstallment())));

                tvAngsuran.setText("Rp. "+ GlobalHelper.formatGermanCurrencyWithoutDigit(Double.valueOf(data.getInstallment()))+",-");

                tvCaraBayar.setText(data.getPaymentType());
                tvLastInstallment.setText(strLastDate);
                tvNextInstallment.setText(strNexDate);
                tvAsset.setText(data.getAssetDescription());
                tvStatus.setText(data.getContractStatus());

                loadDocuments();
            }

            findViewById(R.id.rvly_detail).setOnClickListener(view -> {
                String agreementNo = tvContract.getText().toString();
//                agreementNo = "001221270200011";
                Intent intent = new Intent(mActivity, DetailInstallmentActivity.class);
                intent.putExtra("agreement",agreementNo);
                startActivity(intent);
            });

        }

    }

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.ibBack) {
            onBackPressed();
        } else {
            super.onClick(view);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDocuments(){
        String agreementNo = tvContract.getText().toString();
//        agreementNo = "001221270200011";

        listDocuments.clear();
        PostManager post = new PostManager(this,"document/getlistdocument");
        post.addParam("AgreementNo",agreementNo);
        post.exPost();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK){
                try {
                    JSONArray docType = obj.getJSONArray("DocType");
                    for (int i=0; i<docType.length(); i++){
                        Bundle bundle = new Bundle();
                        bundle.putString("document",docType.getString(i));
                        listDocuments.add(bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.notifyDataSetChanged();
            if (listDocuments.size() == 0){
                txvw_empty.setVisibility(View.VISIBLE);
            }
            else {
                txvw_empty.setVisibility(View.GONE);
            }
        });

        String finalAgreementNo = agreementNo;
        mAdapter.setOnSelectedListener(data -> {
            String[] PERMISSIONS =  new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
            if (!Utility.hasPermission(mActivity,PERMISSIONS)){
                return;
            }
            FileProcessing.createFolder(ClientInformationDetailActivity.this,FileProcessing.ROOT+ROOT_FILE);
            downloadDocument(data.getString("document"), finalAgreementNo);
        });
    }

    private void downloadDocument(String pName, String agreementNo){
        PostManager post = new PostManager(mActivity,"document/getdocument");
        post.addParam("AgreementNo",agreementNo);
        post.addParam("DocType",pName);
        post.exPost();
        post.setOnReceiveListener((obj, code, message) -> {
            try {
                if (code == ErrorCode.OK){
                    String file = obj.getString("File");
                    String name = obj.getString("DocType");
                    String ext = obj.getString("FileExt");
                    convertToFile(file,name,ext);
                }
                else {
                    if (obj.getString("errCode").equals("1")){
                        DynamicDialog dialog = new DynamicDialog(mActivity);
                        dialog.showError("Gagal Download",obj.getString("errMsg"));
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    private void convertToFile(String bas64File, String fileName, String fileType) {
        String mediaPath = Environment.getExternalStorageDirectory()+"/"+FileProcessing.ROOT+ROOT_FILE;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/";
        }
        final File dwldsPath = new File(mediaPath + fileName + fileType);
        byte[] pdfAsBytes = Base64.decode(bas64File, 0);
        FileOutputStream os;
        try {
            os = new FileOutputStream(dwldsPath, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();

            DynamicDialog dialog = new DynamicDialog(mActivity);
            dialog.showInfo("Download Berhasil","Dokumen tersimpan di folder "+ dwldsPath.getAbsolutePath());
            dialog.setAction("Buka Dokumen");
            String finalMediaPath = mediaPath;
            dialog.setOnCloseLister(action -> {
                FileProcessing.openFile(mActivity, finalMediaPath +"/"+fileName+fileType);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
