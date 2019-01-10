package com.example.sss.goodlife.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.VendorDataModel;
import com.example.sss.goodlife.R;
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

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.VendorHoder> {
    private Context context;
    private List<VendorDataModel> vendorDataModels;


    private int GALLERY = 1, CAMERA = 2;
    private boolean permissionCheck=false;
    private Bitmap bmp;


    public VendorListAdapter(Context context,List<VendorDataModel> vendorDataModels) {
        this.context=context;
        this.vendorDataModels=vendorDataModels;
    }

    @NonNull
    @Override
    public VendorHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.vendor_details_from_db, parent, false);
        return new VendorHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VendorHoder holder, int position) {

        holder.vendor_expenditure_from_db.setText(vendorDataModels.get(position).getExpenditure_desc());
        holder.vendor_name_from_db.setText(vendorDataModels.get(position).getVendor_name());
        holder.vendor_location_from_db.setText(vendorDataModels.get(position).getLocation());
        holder.vendor_phone_from_db.setText(vendorDataModels.get(position).getPhone());
        holder.vendor_amount_from_db.setText(vendorDataModels.get(position).getTotal_amount());
        holder.vendor_ac_name_from_db.setText(vendorDataModels.get(position).getAccount_name());
        holder.vendor_payment_process_from_db.setText(vendorDataModels.get(position).getPayment_type());
        holder.vendor_bank_ac_from_db.setText(vendorDataModels.get(position).getBank_acno());
        holder.vendor_banl_Ifsc_from_db.setText(vendorDataModels.get(position).getBank_ifsc_code());

        Log.e("image",vendorDataModels.get(position).getQuation_image());
        Glide.with(context).load(vendorDataModels.get(position).getQuation_image()).into(holder.vendor_Quatation_from_db);

        holder.Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(holder.amountFromDb.getText().toString())){
                    holder.amountFromDb.setError("field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(holder.EnterAmountFinance.getText().toString())){
                    holder.EnterAmountFinance.setError("field cannot be empty");
                    return;
                }
                int totalamount= Integer.parseInt(holder.amountFromDb.getText().toString());
                int enteredAmount= Integer.parseInt(holder.EnterAmountFinance.getText().toString());
                String finalAmount= String.valueOf(totalamount-enteredAmount);
                holder.amountFr.setVisibility(View.VISIBLE);
                holder.amountFr.setText(finalAmount);
            }
        });

        holder.UpdateFinanceDetails.setVisibility(View.VISIBLE);
        if (!permissionCheck){
            holder.requestMultiplePermissions();
        }

        holder.UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPictureDialog();
            }
        });

        holder.UpdateFinanceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.UpdateFinanceDetails.getText().equals("Update Finance Details")){
                        holder.updateFinanceLayout.setVisibility(View.VISIBLE);
                        holder.UpdateFinanceDetails.setText("Cancel Update");
                    }
                    else {
                        holder.updateFinanceLayout.setVisibility(View.GONE);
                        holder.UpdateFinanceDetails.setText("Update Finance Details");
                    }
                }
            });






    }

    @Override
    public int getItemCount() {
        return vendorDataModels.size();
    }

    public void getSelectedUrl(Uri contentURI) {
        Toast.makeText(context,"dfgdigfgf",Toast.LENGTH_SHORT).show();
    }


    public class VendorHoder extends RecyclerView.ViewHolder{
        public TextView vendor_expenditure_from_db,vendor_name_from_db,vendor_location_from_db,
                vendor_phone_from_db,vendor_amount_from_db,vendor_ac_name_from_db,vendor_payment_process_from_db,
                vendor_bank_ac_from_db,vendor_banl_Ifsc_from_db;
        public ImageView vendor_Quatation_from_db;

        private RelativeLayout updateFinanceLayout;
        private TextView UpdateFinanceDetails,Calculate,amountFr;
        private ImageView updatedQuotationFinance;
        private TextView UploadButton;


        private EditText amountFromDb,EnterAmountFinance;
        private Button submitUpdatedFinanceReport;

        public VendorHoder(View itemView) {
            super(itemView);

            vendor_expenditure_from_db=itemView.findViewById(R.id.vendor_expenditure_from_db);
            vendor_name_from_db=itemView.findViewById(R.id.vendor_name_from_db);
            vendor_location_from_db=itemView.findViewById(R.id.vendor_location_from_db);
            vendor_phone_from_db=itemView.findViewById(R.id.vendor_phone_from_db);
            vendor_amount_from_db=itemView.findViewById(R.id.vendor_amount_from_db);
            vendor_ac_name_from_db=itemView.findViewById(R.id.vendor_ac_name_from_db);
            vendor_payment_process_from_db=itemView.findViewById(R.id.vendor_payment_process_from_db);
            vendor_bank_ac_from_db=itemView.findViewById(R.id.vendor_bank_ac_from_db);
            vendor_banl_Ifsc_from_db=itemView.findViewById(R.id.vendor_banl_Ifsc_from_db);
            vendor_Quatation_from_db=itemView.findViewById(R.id.vendor_Quatation_from_db);

            updateFinanceLayout=itemView.findViewById(R.id.updateFinanceLayout);
            UpdateFinanceDetails=itemView.findViewById(R.id.UpdateFinanceDetails);
            updatedQuotationFinance=itemView.findViewById(R.id.updatedQuotationFinance);
            UploadButton=itemView.findViewById(R.id.UploadButton);

            submitUpdatedFinanceReport=itemView.findViewById(R.id.submitUpdatedFinanceReport);

            amountFromDb=itemView.findViewById(R.id.amountFromDb);
            EnterAmountFinance=itemView.findViewById(R.id.EnterAmountFinance);
            Calculate=itemView.findViewById(R.id.Calculate);
            amountFr=itemView.findViewById(R.id.amountFr);
        }

        //Upload Image
        private void showPictureDialog(){
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
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

                ((Activity)context).startActivityForResult(galleryIntent, GALLERY);
            }else {
                requestMultiplePermissions();
            }

        }

        private void takePhotoFromCamera() {
            if (permissionCheck){
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(intent, CAMERA);
            }else {
                requestMultiplePermissions();
            }


        }

        private void  requestMultiplePermissions(){
            Dexter.withActivity((Activity) context)
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
                                Toast.makeText(context, "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Some Error! ", Toast.LENGTH_SHORT).show();
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



}
