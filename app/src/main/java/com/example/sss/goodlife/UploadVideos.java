package com.example.sss.goodlife;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadVideos extends Fragment  implements  EasyPermissions.PermissionCallbacks{
    private Button btnUploadVideos;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "";

    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;





    public UploadVideos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_upload_videos, container, false);

        btnUploadVideos=view.findViewById(R.id.btnUploadVideos);
        displayRecordedVideo=(VideoView) view.findViewById(R.id.video_display);

        btnUploadVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(videoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                }

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE){
            if (data!=null){
                uri = data.getData();
                //Log.e(TAG, "Recorded Video Path " + uri);
                if(EasyPermissions.hasPermissions(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                    pathToStoredVideo = getRealPathFromURIPath(uri, getActivity());
                    Log.e(TAG, "Recorded Video Path " + pathToStoredVideo);

                    getInit(pathToStoredVideo);

                    //Store the video to your server
                    //uploadVideoToServer(pathToStoredVideo);

                }else{
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
            else {
                Log.e(TAG, "Data null" + pathToStoredVideo);
            }

        }
    }

    public void getInit(String uri) {
        media_Controller = new MediaController(getActivity());
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels; int width = dm.widthPixels;
        displayRecordedVideo.setMinimumWidth(width);
        displayRecordedVideo.setMinimumHeight(height);
        displayRecordedVideo.setMediaController(media_Controller);
        displayRecordedVideo.setVideoPath(uri);
        displayRecordedVideo.start();
    }


    private String getFileDestinationPath(){
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");
        if(!directoryFolder.exists()){
            directoryFolder.mkdir();
        }
        Log.e(TAG, "Full path " + filePathEnvironment + "/video/" + generatedFilename + ".mp4");
        return filePathEnvironment + "/video/" + generatedFilename + ".mp4";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            if(EasyPermissions.hasPermissions(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri,getActivity());
                Log.e(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                //uploadVideoToServer(pathToStoredVideo);

            }
        }
    }

//    private void uploadVideoToServer(String pathToVideoFile){
//        File videoFile = new File(pathToVideoFile);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
//        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(SERVER_PATH)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        VideoInterface vInterface = retrofit.create(VideoInterface.class);
//        Call<ResultObject>  serverCom = vInterface.uploadVideoToServer(vFile);
//        serverCom.enqueue(new Callback<ResultObject>() {
//            @Override
//            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                ResultObject result = response.body();
//                if(!TextUtils.isEmpty(result.getSuccess())){
//                    Toast.makeText(MainActivity.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "Result " + result.getSuccess());
//                }
//            }
//            @Override
//            public void onFailure(Call<ResultObject> call, Throwable t) {
//                Log.d(TAG, "Error message " + t.getMessage());
//            }
//        });
//    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e(TAG, "User has denied requested permission");
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }



}
