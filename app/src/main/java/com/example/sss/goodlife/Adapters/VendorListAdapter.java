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
import com.example.sss.goodlife.UpDateFinanceReport;
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
    private String programId;




    public VendorListAdapter(Context context, List<VendorDataModel> vendorDataModels, String programId) {
        this.context=context;
        this.vendorDataModels=vendorDataModels;
        this.programId=programId;
    }

    @NonNull
    @Override
    public VendorHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.vendor_details_from_db, parent, false);
        return new VendorHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VendorHoder holder, final int position) {

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

        holder.UpdateFinanceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update=new Intent(context,UpDateFinanceReport.class);
                update.putExtra("finance id",vendorDataModels.get(position).getFinance_id());
                update.putExtra("vendor_id",vendorDataModels.get(position).getVendor_id());
                update.putExtra("program_id",programId);
                context.startActivity(update);
            }
        });


    }

    @Override
    public int getItemCount() {
        return vendorDataModels.size();
    }

    public class VendorHoder extends RecyclerView.ViewHolder{
        public TextView vendor_expenditure_from_db,vendor_name_from_db,vendor_location_from_db,
                vendor_phone_from_db,vendor_amount_from_db,vendor_ac_name_from_db,vendor_payment_process_from_db,
                vendor_bank_ac_from_db,vendor_banl_Ifsc_from_db,UpdateFinanceDetails;
        public ImageView vendor_Quatation_from_db;



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
            UpdateFinanceDetails=itemView.findViewById(R.id.UpdateFinanceDetails);




        }

    }



}
