package com.i9930.croptrails.Communication;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import filePicker.Constant;
import filePicker.activity.AudioPickActivity;
import filePicker.activity.NormalFilePickActivity;
import filePicker.filter.entity.AudioFile;
import filePicker.filter.entity.ImageFile;
import filePicker.filter.entity.NormalFile;
import filePicker.filter.entity.VideoFile;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Communication.Adapter.CaseRvAdapter;
import com.i9930.croptrails.Communication.Model.CaseDatum;
import com.i9930.croptrails.Communication.Model.CaseFileDatum;
import com.i9930.croptrails.Communication.Model.CaseListResponse;
import com.i9930.croptrails.Communication.Model.InsertCaseResponse;
import com.i9930.croptrails.Communication.Model.MessageObj;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import whatsappMessengerView.AttachmentOption;
import whatsappMessengerView.AttachmentOptionsListener;
import whatsappMessengerView.AudioRecordView;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static filePicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static filePicker.activity.ImagePickActivity.IS_NEED_CAMERA;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class CommunicationActivity extends AppCompatActivity {
    String TAG = "CommunicationActivity";
    CommunicationActivity context;
    List<AttachmentOption> attachmentOptions = new ArrayList<>();
    boolean isForeCase = false;
    String caseId;
    List<CaseDatum> caseDatumList = new ArrayList<>();
    CaseRvAdapter caseRvAdapter;
    String auth, userId, token, personId, compId;
    ViewFailDialog viewFailDialog;
    Resources resources;
    Toolbar mActionBarToolbar;
    RecyclerView messageRecycler;

    public enum SELECTED_TYPE {
        CAMERA, GALLERY, PDF, AUDIO,XL;
    }

    SELECTED_TYPE selected_type = null;

    String status ="N";
//    @BindView(R.id.bottomView)
//    View bottomView;
    int statusChange=RESULT_CANCELED;
    Intent intent=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        context = this;
        ButterKnife.bind(this);
        viewFailDialog = new ViewFailDialog();
        resources = getResources();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);

        intent=getIntent();
        isForeCase = intent.getBooleanExtra("isForeCase", false);
        long caseid = intent.getLongExtra("case_id",0);
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.CASE_ID,""+caseid);

        attachmentOptions.add(new AttachmentOption(AttachmentOption.DOCUMENT_ID, "Add File", R.drawable.ic_attachment_document));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.CAMERA_ID, "Capture", R.drawable.ic_attachment_camera));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.GALLERY_ID, "Select", R.drawable.ic_attachment_gallery));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.AUDIO_ID, "Audio", R.drawable.ic_attachment_audio));
        TextView tittle = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(mActionBarToolbar);
//        getSupportActionBar().setTitle("");
        caseId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CASE_ID);
        tittle.setText("Case:- " + caseId);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        setResult(statusChange,intent);
        finish();
    }

    AudioRecordView audioRecordView;
    private void integrateWhatsappView(boolean showMesssageView) {
//        if (audioRecordView!=null)
//            audioRecordView.hideAttachmentOptionView();
        audioRecordView= new AudioRecordView();
        audioRecordView.initView((FrameLayout) findViewById(R.id.layoutMain));
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        messageRecycler = containerView.findViewById(R.id.messageRecycler);
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(new LinearLayoutManager(context));


        caseRvAdapter = new CaseRvAdapter(context, caseDatumList, false);
        caseRvAdapter.setOnCaseClickListener(new CaseRvAdapter.OnCaseClickListener() {
            @Override
            public void onImageClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView) {
//                AppConstants.failToast(context,"Image clicked");
                downloadFile(position,url,fileName, AppConstants.FILE_PATH_TYPE.OTHER,imageView);
            }

            @Override
            public void onAudioClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView ) {
//                AppConstants.failToast(context,"audio clicked");
                downloadFile(position,url,fileName,AppConstants.FILE_PATH_TYPE.AUDIO,imageView);
            }

            @Override
            public void onFileClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView) {
//                AppConstants.failToast(context,"File clicked");
                downloadFile(position,url,fileName,AppConstants.FILE_PATH_TYPE.DOCUMENT,imageView);
            }
        });
        messageRecycler.setAdapter(caseRvAdapter);
        audioRecordView.getMessageView().setPadding(3, 0, 0, 0);

        if (!checkPermission())
            requestPermission();
        else {
            if (checkPermission()) {
                audioRecordView.setRecordingListener(new AudioRecordView.RecordingListener() {
                    @Override
                    public void onRecordingStarted() {
                        try {
                            selected_type = SELECTED_TYPE.AUDIO;
                            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +Environment.DIRECTORY_MUSIC+"/"+
                                            AppConstants.getCurrentMills() + "_" + caseId + "AudioRecording.amr";

                            MediaRecorderReady();
                            mediaRecorder.prepare();
                            mediaRecorder.start();

                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRecordingLocked() {
                    }

                    @Override
                    public void onRecordingCompleted() {

                        try {
                            mediaRecorder.stop();
                        }catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.e(TAG,"AUdio path "+AudioSavePathInDevice);
                        mSelected.clear();
                        mSelected.add(Uri.parse(AudioSavePathInDevice));
                        insertMessage("",false,null,null,null);
                    }

                    @Override
                    public void onRecordingCanceled() {
                        try {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                AppConstants.failToast(context,"Cancelled");
                    }
                });
            } else {
                requestPermission();
            }
        }
        audioRecordView.getEmojiView().setVisibility(View.GONE);

        if (showMesssageView){
//            bottomView.setVisibility(View.GONE);
            audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraIntent();
                }
            });
            audioRecordView.setAttachmentOptions(attachmentOptions, new AttachmentOptionsListener() {
                @Override
                public void onClick(AttachmentOption attachmentOption) {
                    switch (attachmentOption.getId()) {

                        case AttachmentOption.DOCUMENT_ID: //Ids for default Attachment Options
                            Intent intent4 = new Intent(CommunicationActivity.this, NormalFilePickActivity.class);
                            intent4.putExtra(Constant.MAX_NUMBER, 1);
                            intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"pdf"});
                            startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
                            break;
                        case AttachmentOption.CAMERA_ID:
                            cameraIntent();
                            break;
                        case AttachmentOption.GALLERY_ID:
                            Intent intent1 = new Intent(context, filePicker.activity.ImagePickActivity.class);
                            intent1.putExtra(IS_NEED_CAMERA, false);
                            intent1.putExtra(Constant.MAX_NUMBER, 3);
                            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
                            break;
                        case AttachmentOption.AUDIO_ID:
                            Intent intent3 = new Intent(CommunicationActivity.this, AudioPickActivity.class);
                            intent3.putExtra(IS_NEED_RECORDER, false);
                            intent3.putExtra(Constant.MAX_NUMBER, 3);
                            startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
                            break;
                        case AttachmentOption.LOCATION_ID:
                            showToast("Location Clicked");
                            break;
                        case AttachmentOption.CONTACT_ID:
                            showToast("Contact Clicked");
                            break;
                    }
                }
            });
            audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = audioRecordView.getMessageView().getText().toString();
                    audioRecordView.getMessageView().setText("");
                    if (isValidString(msg))
                        insertMessage(msg,false,null,null,null);
                }
            });

        }else{
            audioRecordView.showCameraIcon(false);
            audioRecordView.showAttachmentIcon(false);
            audioRecordView.showEmojiIcon(false);
            audioRecordView.getAttachmentView().setVisibility(View.GONE);
            audioRecordView.hideAttachmentOptionView();
            audioRecordView.getCameraView().setVisibility(View.GONE);
            audioRecordView.getMessageView().setVisibility(View.GONE);
            audioRecordView.getImageViewAudio().setVisibility(View.GONE);
            audioRecordView.layoutMessage.setVisibility(View.GONE);


//            bottomView.setVisibility(View.VISIBLE);
        }
    }
    // on create case
    private void downloadFile(int position, String url, String randomAudioFileName, AppConstants.FILE_PATH_TYPE type, ImageView imageView){

       try {
           String fName=randomAudioFileName.replace("/","_");
           File direct = new File(Environment.DIRECTORY_PICTURES + AppConstants.getImagePath(null, context));
           if (!direct.exists()) {
               direct.mkdirs();
           }
           File path = new File(Environment.DIRECTORY_PICTURES , AppConstants.getImagePath(type, context));
           if (!path.exists()) {
               path.mkdirs();
           }
           File file=new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES+"/"+AppConstants.getImagePath(type, context)+"/"+fName);
           Log.e(TAG, "Downloading URL:" + url + ", name:" + randomAudioFileName + ", path:" + path.getAbsolutePath() + "/" + fName+", path2:"+file.getAbsolutePath());
           if (file.exists()) {
               Intent target = new Intent(Intent.ACTION_VIEW);

               String ext=getExtension(file.getAbsolutePath()).replace(".","");
               if (type== AppConstants.FILE_PATH_TYPE.DOCUMENT) {

                   target.setDataAndType(Uri.fromFile(file), "application/"+ext);
               }else if (type== AppConstants.FILE_PATH_TYPE.OTHER){
                   target.setDataAndType(Uri.fromFile(file), "image/"+ext);
               }else{
                   target.setDataAndType(Uri.fromFile(file), "audio/"+ext);
               }
               target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
               Intent intent = Intent.createChooser(target, "Open File");
               try {
                   startActivity(intent);
               } catch (ActivityNotFoundException e) {
                   // Instruct the user to install a PDF reader here, or something
                   e.printStackTrace();
               }
//               AppConstants.failToast(context,"Already downlaoded");
           }else{
               DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
               Uri uri = Uri.parse(url);
               DownloadManager.Request request = new DownloadManager.Request(uri);
               request.setTitle("Downloading");
//               request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
               request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, AppConstants.getImagePath(type, context)+"/"+fName);
//               request.setDestinationInExternalPublicDir(path.getAbsolutePath(), fName);




               long ln=downloadmanager.enqueue(request);
               Uri ur=downloadmanager.getUriForDownloadedFile(ln);
               if (ur!=null) {
                   caseDatumList.get(position).setDownloaded(true);
                   caseRvAdapter.notifyItemChanged(position);
                   if (imageView!=null)
                       imageView.setVisibility(View.GONE);
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    private void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    List<Uri> mSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CAMERA1 :
                selected_type = SELECTED_TYPE.CAMERA;
                String result = "capture1";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
                break;

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    mSelected.clear();
//                    mSelected = Matisse.obtainResult(data);
                    selected_type = SELECTED_TYPE.GALLERY;
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mSelected.add(Uri.parse(list.get(i).getPath()));
                        }

                        insertMessage("",false,null,null,null);
                    }
                }
                break;
            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                }
                break;
            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    mSelected.clear();
                    selected_type = SELECTED_TYPE.AUDIO;
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    if (list != null && list.size() > 0) {
                        for (AudioFile audioFile : list) {
                            mSelected.add(Uri.parse(audioFile.getPath()));
                        }
                        insertMessage("",false,null,null,null);
                    }
                }
                break;
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    mSelected.clear();
                    selected_type = SELECTED_TYPE.PDF;
                    if (list != null && list.size() > 0) {
                        for (NormalFile audioFile : list) {
                            mSelected.add(Uri.parse(audioFile.getPath()));
                        }
                        insertMessage("",false,null,null,null);
                    }
                }
                break;
        }

    }


    public void insertMessage(String msg,boolean isRemark,final String status,Menu menu,final CaseDatum caseDatum) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("person_id", personId);
        jsonObject.addProperty("sender_id", personId);
        jsonObject.addProperty("message", msg);
        jsonObject.addProperty("receiver_id", "0");
        jsonObject.addProperty("status", "N");
        jsonObject.addProperty("is_sent", "Y");
        jsonObject.addProperty("is_read", "N");
        jsonObject.addProperty("case_id", caseId);
        Log.e(TAG, "Posting msg " + new Gson().toJson(jsonObject));
        addMessage(msg,isRemark,status,menu,caseDatum);

    }


    private String getExtension(String fileName){
        return  fileName.substring(fileName.lastIndexOf(".")).replace(".","");
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());

        if (selected_type== SELECTED_TYPE.CAMERA){
          /*  File compressedFile=compressFile(file);
            if (compressedFile!=null)
                file=compressedFile;*/
            try {
                Log.e(TAG,"SiliCompressor BEFORE "+file.length()+", "+file.getPath());
                if (file.length()/1024>150)
                file=compressFile(file);
                Log.e(TAG,"SiliCompressor AFTER "+file.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            if (selected_type== SELECTED_TYPE.GALLERY){
          /*  File compressedFile=compressFile(file);
            if (compressedFile!=null)
                file=compressedFile;*/
                try {
//                    File dest=new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES+"/"+AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.OTHER, context)+"/"+AppConstants.getCurrentMills()+".jpg");
//                     String path =copy(file,dest);
                    Log.e(TAG,"SiliCompressor BEFORE "+file.length());
//                    String destPath= SiliCompressor.with(context).compress(dest.getAbsolutePath(),dest);
                    file=compressFile(file);
                    Log.e(TAG,"SiliCompressor AFTER "+file.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//        Log.e(TAG, "Uri to file " + file.getAbsolutePath());
        // create RequestBody instance from file
        if (file!=null) {
            String extension = getExtension(fileUri.getPath());
            if (extension.contains("."))
                extension=extension.replace(".","");
            RequestBody requestFile =
                    RequestBody.create(
//                            MediaType.parse(getContentResolver().getType(fileUri)),
                            MediaType.parse("file/"+extension),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }else return null;
    }

    private List<MultipartBody.Part> getMultipartFiles() {
        List<MultipartBody.Part> partList = new ArrayList<>();
        String name = "";
        if (selected_type == SELECTED_TYPE.GALLERY||selected_type == SELECTED_TYPE.CAMERA) {
            name="image";
        } else if (selected_type == SELECTED_TYPE.AUDIO) {
            name="voice";
        }else if (selected_type == SELECTED_TYPE.PDF) {
            name="pdf";
        }else if (selected_type == SELECTED_TYPE.XL) {
            name="excel";
        }

        if (isValidString(name))
            for (Uri uri : mSelected) {
                Log.e(TAG,"URIII:-"+uri.getPath()+"\n AND:-"+uri);
                MultipartBody.Part part =prepareFilePart(name, uri);
//                if (part!=null)
                partList.add(part);
            }

        return partList;
    }

    private RequestBody getRequestBody(String val) {
        if (isValidString(val))
            return RequestBody.create(MultipartBody.FORM, val);
        else return null;
    }

    private String copy(File file,File dest){
        try {
            FileInputStream inStream = new FileInputStream(file);
            FileOutputStream outStream = new FileOutputStream(dest);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        }catch (IOException e){
            e.printStackTrace();

        }catch (Exception e){
            e.printStackTrace();
        }

        return dest.getAbsolutePath();
    }

    private void copyFiles(InsertCaseResponse caseResponse,String msg){


        List<CaseFileDatum> fileLinks=new ArrayList<>();
        Gson gson=new Gson();
        MessageObj messageObj = gson.fromJson(caseResponse.getData().getMessage(), MessageObj.class);
        if (messageObj.getFilesLink()!=null&&messageObj.getFilesLink().size()>0) {
            for (int i = 0; i < messageObj.getFilesLink().size(); i++) {
                CaseFileDatum caseFileDatum = new CaseFileDatum();
                caseFileDatum.setExtension(getExtension(messageObj.getFilesLink().get(i)));
                caseFileDatum.setFileLink(messageObj.getFilesLink().get(i));

                AppConstants.FILE_PATH_TYPE type=null;
                if (selected_type== SELECTED_TYPE.AUDIO){
                    type=AppConstants.FILE_PATH_TYPE.AUDIO;

                }else if (selected_type== SELECTED_TYPE.PDF||selected_type== SELECTED_TYPE.XL){
                    type=AppConstants.FILE_PATH_TYPE.DOCUMENT;
                }else if (selected_type== SELECTED_TYPE.GALLERY||selected_type== SELECTED_TYPE.CAMERA){
                    type=AppConstants.FILE_PATH_TYPE.OTHER;
                }
                String fName=messageObj.getFilesLink().get(i).replace("/","_");
                File direct = new File(Environment.DIRECTORY_PICTURES + AppConstants.getImagePath(null, context));
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                File path = new File(Environment.DIRECTORY_PICTURES , AppConstants.getImagePath(type, context));
                if (!path.exists()) {
                    path.mkdirs();
                }
                File file=new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES+"/"+AppConstants.getImagePath(type, context)+"/"+fName);
//
                copy(new File(mSelected.get(i).getPath()),file);
                fileLinks.add(caseFileDatum);

            }
        }

        CaseDatum datum = new CaseDatum(0, Long.valueOf(caseId), Long.valueOf(compId), Long.valueOf(personId),
                0, new Gson().toJson(messageObj), null, "Y", "N", "N", "N");
        datum.setFileLinks(fileLinks);
        caseDatumList.add(datum);
        caseRvAdapter.notifyDataSetChanged();

        messageRecycler.scrollToPosition(caseDatumList.size()-1);

    }

    private void addMessage(String msg,boolean isRemark,final String status,Menu menu,final CaseDatum caseDatum) {
        try {
//            createBtn.setClickable(false);
//            createBtn.setEnabled(false);
//            progressBar.setVisibility(View.VISIBLE);

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};

             /*if (!isRemark &&( mSelected==null||mSelected.size()==0)) {
                 MessageObj obj = new MessageObj();
                 obj.setMessage(msg);
                 CaseDatum datum = new CaseDatum(0, Long.valueOf(caseId), Long.valueOf(compId), Long.valueOf(personId),
                         0, new Gson().toJson(obj), null, "Y", "N", "N", "N");

                 caseDatumList.add(datum);
                 caseRvAdapter.notifyDataSetChanged();
             }*/

//            Log.e(TAG,"Adding Req "+new Gson().toJson(object));
            apiInterface.addMessageWithFiles(getMultipartFiles(), getRequestBody(userId), getRequestBody(compId), getRequestBody(token), getRequestBody(personId)
                    , getRequestBody(personId), getRequestBody(msg),null, getRequestBody("0"), getRequestBody("N"), getRequestBody("Y"), getRequestBody("N"),
                    getRequestBody(caseId)).enqueue(new Callback<InsertCaseResponse>() {
                @Override
                public void onResponse(Call<InsertCaseResponse> call, Response<InsertCaseResponse> response) {
                    String error = null;
                    isLoaded[0] = true;
                    Log.e(TAG, "Query code " + response.code());
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Query body " + new Gson().toJson(response.body()));
//                        progressBar.setVisibility(View.GONE);
//                        createBtn.setClickable(true);
//                        createBtn.setEnabled(true);
                        if (response.body().getStatus() == 1) {

                            copyFiles(response.body(),msg);

                            if (isRemark){
                                changeStatus(caseDatum.getStatus(),menu,caseDatum);
//                                AppConstants.successToast(context,"Case masterrked ass");
                            }
//                            mSelected.clear();
                        } else if (response.body().getStatus() == 10) {

                            viewFailDialog.showSessionExpireDialog(context);
                        } else {
//                            Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        }

                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access

                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
//                            Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Query Add err " + response.errorBody().string() + " code " + response.code());
//                            progressBar.setVisibility(View.GONE);
//                            createBtn.setClickable(true);
//                            createBtn.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<InsertCaseResponse> call, Throwable t) {
                    Log.e(TAG, "Query Add fail " + t.toString());
//                    progressBar.setVisibility(View.GONE);
//                    createBtn.setClickable(true);
//                    createBtn.setEnabled(true);
//                    Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

//            viewFailDialog.showDialogForFinish(QueryActivity.this, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
        }

    }


    private void getCases(Menu menu) {
        //try {
        //progressDialog.show();
        caseDatumList.clear();
//        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("case_id", caseId);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

        Log.e(TAG, "Data Sending  " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCases(jsonObject).enqueue(new Callback<CaseListResponse>() {
            @Override
            public void onResponse(Call<CaseListResponse> call, Response<CaseListResponse> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    CaseListResponse responseBody = response.body();

                    Log.e(TAG, "Data  " + new Gson().toJson(responseBody));
                    if (responseBody.getData() != null && responseBody.getData().size() > 0) {

                        statusChangeAction(menu,responseBody.getData().get(0));
                        caseDatumList.addAll(responseBody.getData());
                        caseRvAdapter.notifyDataSetChanged();
                        messageRecycler.scrollToPosition(caseDatumList.size()-1);

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    isSpinnerDataLoaded = false;
//                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "err  " + error);

                        viewFailDialog.showDialogForFinish(context, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<CaseListResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Fail " + t.toString());

            }
        });
    }

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer;


    private final int RequestPermissionCode = 661,REQUEST_CAMERA1=662;

    private void requestPermission() {
        ActivityCompat.requestPermissions(context, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                    } else {
                        AppConstants.failToast(context, "Please allow permission to send media files");
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_WB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    String pictureImagePath="";
    private void cameraIntent() {
        String [] params=new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context,params)) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, "720000");
            startActivityForResult(intent, REQUEST_CAMERA1);
        }else
            EasyPermissions.requestPermissions(this, getString(R.string.permission_necessary),
                    1, params);
    }

    private void onCaptureImageResult(Intent data, String onclick) {
        File imgFile = new File(pictureImagePath);

        if (imgFile.exists()) {
            Log.e(TAG,"CAPTURE FILE exist "+pictureImagePath);
            mSelected.clear();
            mSelected.add(Uri.parse(pictureImagePath));
            insertMessage("",false,null,null,null);
        }else{
            Log.e(TAG,"CAPTURE FILE not exist");
        }
    }

    public File compressFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            Log.e(TAG, "BEFORE " + file.length());

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 50;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Log.e(TAG, " AFTER " + file.length());
            return file;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.case_options, menu);

        getCases(menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void statusChangeAction(Menu menu,CaseDatum caseDatum){
        String status=caseDatum.getStatus();
        if (AppConstants.isValidString(status)){
            MenuItem resolveMenu=menu.findItem(R.id.resolveAction);
            MenuItem closeMenu=menu.findItem(R.id.caseAction);

            if (status.trim().equalsIgnoreCase("N")||status.trim().equalsIgnoreCase("O")){
                resolveMenu.setVisible(true);
                closeMenu.setVisible(true);
                resolveMenu.setTitle("Case Resolved");
                closeMenu.setTitle("Close Case");
                resolveMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        caseDatum.setStatus("R");

                        showRemarksLabel(caseDatum.getStatus(),menu,caseDatum);

//                        changeStatus(caseDatum.getStatus(),menu,caseDatum);
                        return false;
                    }
                });

                closeMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        caseDatum.setStatus("C");
                        showRemarksLabel(caseDatum.getStatus(),menu,caseDatum);
//                        changeStatus(caseDatum.getStatus(),menu,caseDatum);
                        return false;
                    }
                });
                integrateWhatsappView(true);
            }else{
                resolveMenu.setVisible(true);
                closeMenu.setVisible(false);
                resolveMenu.setTitle("Reopen");
                integrateWhatsappView(false);
                resolveMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        caseDatum.setStatus("O");
                        showRemarksLabel(caseDatum.getStatus(),menu,caseDatum);
//                        changeStatus(caseDatum.getStatus(),menu,caseDatum);
                        return false;
                    }
                });
            }

        }
    }

    private void changeStatus(final String status,Menu menu,final CaseDatum caseDatum){

        //try {
        //progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", "" + token);
//        jsonObject.addProperty("person_id", personId);
//        jsonObject.addProperty("sender_id", personId);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("case_id", caseId);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

        Log.e(TAG, "changeStatus Sending  " + new Gson().toJson(jsonObject));
        apiInterface.changeStatusOfCase(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error = "";
                if (response.isSuccessful()) {
                    StatusMsgModel responseBody = response.body();
                    Log.e(TAG, "changeStatus res  " + new Gson().toJson(responseBody));
                    if (responseBody.getStatus()==1){
                        statusChangeAction(menu,caseDatum);
                        statusChange=RESULT_OK;
                    }else{
                        AppConstants.failToast(context,"Failed to change case status, Please try again");
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    isSpinnerDataLoaded = false;
//                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "changeStatus err  " + error);

                        AppConstants.failToast(context,"Failed to change case status, Please try again");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Fail " + t.toString());
                AppConstants.failToast(context,"Failed to change case status, Please try again");

            }
        });
    }

    private void showRemarksLabel(final String status,Menu menu,final CaseDatum caseDatum) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_case_remark);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        EditText etRemarks = dialog.findViewById(R.id.etRemarks);

        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView submitTv = dialog.findViewById(R.id.submitTv);


        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etRemarks.getText().toString())){
                    AppConstants.failToast(context,"Please enter remarks");
                    etRemarks.setError(resources.getString(R.string.required_label));
                }else{
                    insertMessage(etRemarks.getText().toString(),true,status,menu,caseDatum);
                }
                dialog.dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}