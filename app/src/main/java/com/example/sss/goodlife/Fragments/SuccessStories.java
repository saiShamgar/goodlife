package com.example.sss.goodlife.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.goodlife.Adapters.ProgramsIdAdapter;
import com.example.sss.goodlife.Api.APIUrl;
import com.example.sss.goodlife.Api.ApiService;
import com.example.sss.goodlife.MainActivity;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ProgramIds;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
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
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessStories extends Fragment {

    private Spinner SuccessStoriesSpinnerLayout;
    private EditText SuccessStories_phone,SuccessStories_email,edt_SuccessStories_description;
    private ImageView SuccessStoriesImage;
    private TextView SuccessStoriesUploadButton;
    private Button btn_submit_SuccessStories;

    //Api calls
    private ApiService apiService;
    private List<ProgramIds> programIds;
    private String programId,locationId;
    private ProgramsIdAdapter programidsAdapter;
    private ProgressDialog progressDialog;

    private int GALLERY = 1, CAMERA = 2;
    private boolean permissionCheck=false;
    private Bitmap bmp;

    public SuccessStories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Success Stories");
        View view= inflater.inflate(R.layout.fragment_success_stories, container, false);

        if (!permissionCheck){
            requestMultiplePermissions();
        }
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SuccessStoriesSpinnerLayout=(Spinner) view.findViewById(R.id.SuccessStories_spinner_program_id);
        SuccessStories_email=view.findViewById(R.id.SuccessStories_email);
        SuccessStories_phone=view.findViewById(R.id.SuccessStories_phone);
        edt_SuccessStories_description=view.findViewById(R.id.edt_SuccessStories_description);
        SuccessStoriesImage=view.findViewById(R.id.SuccessStoriesImage);
        SuccessStoriesUploadButton=view.findViewById(R.id.SuccessStoriesUploadButton);
        btn_submit_SuccessStories=view.findViewById(R.id.btn_submit_SuccessStories);

        //Spinner Dropdown for ProgramNames
        apiService= APIUrl.getApiClient().create(ApiService.class);
        final retrofit2.Call<ProgramIdsStatus> call=apiService.getProgramids();
        call.enqueue(new Callback<ProgramIdsStatus>() {
            @Override
            public void onResponse(retrofit2.Call<ProgramIdsStatus> call, Response<ProgramIdsStatus> response) {
                if (response.body()==null){
                    Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().getMessage().size()!=0) {
                    programIds = response.body().getMessage();
                    programidsAdapter = new ProgramsIdAdapter(getActivity(), (ArrayList<ProgramIds>) programIds);
                    SuccessStoriesSpinnerLayout.setAdapter(programidsAdapter);
                    progressDialog.dismiss();

                    SuccessStoriesSpinnerLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            programId= String.valueOf(programidsAdapter.getItem(position).getProgram_id());
                            locationId= String.valueOf(programidsAdapter.getItem(position).getLocation_id());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Programs not forund",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ProgramIdsStatus> call, Throwable t) {
                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        btn_submit_SuccessStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SuccessStories_phone.getText().toString())){
                    SuccessStories_phone.setError("Field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edt_SuccessStories_description.getText().toString())){
                    edt_SuccessStories_description.setError("Field cannot be empty");
                    return;
                }
                btn_submit_SuccessStories.setClickable(false);
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setTitle("Submitting Success Story");
                progressDialog.setMessage("Please wait...,While we are submitting your details");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                final retrofit2.Call<FormStatus> call=apiService.successStory(
                            programId,
                        locationId,
                        SuccessStories_email.getText().toString(),
                        SuccessStories_phone.getText().toString(),
                        edt_SuccessStories_description.getText().toString(),
                        imageToString(bmp));

                call.enqueue(new Callback<FormStatus>() {
                    @Override
                    public void onResponse(Call<FormStatus> call, Response<FormStatus> response) {
                        if (response.body()==null){
                            progressDialog.dismiss();
                            btn_submit_SuccessStories.setClickable(true);
                            Toast.makeText(getActivity(),"responce null",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment fragment=new HomeFragment();
                        transaction.replace(R.id.frameContainer, fragment);
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(Call<FormStatus> call, Throwable t) {
                        progressDialog.dismiss();
                        btn_submit_SuccessStories.setClickable(true);
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        SuccessStoriesUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPictureDialog();
            }
        });
        return view;
    }

    //Upload Image
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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

    private void takePhotoFromCamera() {
        if (permissionCheck){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }else {
            requestMultiplePermissions();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    SuccessStoriesImage.setImageBitmap(bmp);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bmp = (Bitmap) data.getExtras().get("data");
            SuccessStoriesImage.setImageBitmap(bmp);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

        }
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
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
                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
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
