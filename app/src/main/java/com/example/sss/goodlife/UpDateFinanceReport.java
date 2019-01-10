package com.example.sss.goodlife;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.Fragments.HomeFragment;
import com.example.sss.goodlife.Models.FormStatus;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpDateFinanceReport extends Activity {

    private RelativeLayout updateFinanceLayout;
    private TextView Calculate,amountFr;
    private ImageView updatedQuotationFinance;
    private TextView UploadButton;


    private EditText amountFromDb,EnterAmountFinance;
    private Button submitUpdatedFinanceReport;

    private int GALLERY = 1, CAMERA = 2;
    private boolean permissionCheck=false;
    private Bitmap bmp;

    private ProgressDialog progressDialog;
    private ApiService apiService;

    private String financeId,Program_id,vendorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date_finance_report);

        financeId=getIntent().getExtras().getString("finance id");
        vendorId=getIntent().getExtras().getString("vendor_id");
        Program_id=getIntent().getExtras().getString("program_id");

        progressDialog=new ProgressDialog(this);

        updateFinanceLayout=findViewById(R.id.updateFinanceLayout);
        updatedQuotationFinance=findViewById(R.id.updatedQuotationFinance);
        UploadButton=findViewById(R.id.UploadButtonFinanceReport);

        submitUpdatedFinanceReport=findViewById(R.id.submitUpdatedFinanceReport);

        amountFromDb=findViewById(R.id.amountFromDb);
        EnterAmountFinance=findViewById(R.id.EnterAmountFinance);
        Calculate=findViewById(R.id.Calculate);
        amountFr=findViewById(R.id.amountFr);

        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(amountFromDb.getText().toString())){
                    amountFromDb.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(EnterAmountFinance.getText().toString())){
                    EnterAmountFinance.setError("field cannot be empty");
                    return;
                }
                int totalamount= Integer.parseInt(amountFromDb.getText().toString());
                int enteredAmount= Integer.parseInt(EnterAmountFinance.getText().toString());
                String finalAmount= String.valueOf(totalamount-enteredAmount);
                amountFr.setVisibility(View.VISIBLE);
                amountFr.setText(finalAmount);
            }
        });

        if (!permissionCheck){
            requestMultiplePermissions();
        }

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        submitUpdatedFinanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(amountFromDb.getText().toString())){
                    amountFromDb.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(EnterAmountFinance.getText().toString())){
                    EnterAmountFinance.setError("field cannot be empty");
                    return;
                }
                if (amountFr.getVisibility()==View.GONE){
                    Toast.makeText(getApplicationContext(),"please tally amount",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bmp==null){
                    Toast.makeText(getApplicationContext(),"please upload quotation image",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setTitle("Updating finance report");
                progressDialog.setMessage("Please wait...,while we are submitting your details");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<FormStatus> transportCall=apiService.updateFinanceReport(
                        Program_id,
                        financeId,
                        vendorId,
                        amountFromDb.getText().toString(),
                        EnterAmountFinance.getText().toString(),
                        amountFr.getText().toString(),
                        imageToString(bmp));
                transportCall.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Intent Main=new Intent(UpDateFinanceReport.this,MainActivity.class);
                        startActivity(Main);
                    }
                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    //Upload Image
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        if (permissionCheck){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

           startActivityForResult(galleryIntent, GALLERY);
        }else {
            requestMultiplePermissions();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    updatedQuotationFinance.setImageBitmap(bmp);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bmp = (Bitmap) data.getExtras().get("data");
            updatedQuotationFinance.setImageBitmap(bmp);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();

        }
    }

    private void takePhotoFromCamera() {
        if (permissionCheck){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(intent, CAMERA);
        }else {
            requestMultiplePermissions();
        }


    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCheck=true;
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                            permissionCheck=false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    //upload images
    public String  imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }
}
