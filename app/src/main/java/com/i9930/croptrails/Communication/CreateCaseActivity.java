package com.i9930.croptrails.Communication;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
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
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
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
import pl.droidsonroids.gif.GifImageView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Communication.Adapter.SelectedFilesAdapter;
import com.i9930.croptrails.Communication.Model.CaseFileDatum;
import com.i9930.croptrails.Communication.Model.InsertCaseResponse;
import com.i9930.croptrails.Communication.Model.MessageObj;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import whatsappMessengerView.AttachmentOption;
import whatsappMessengerView.AttachmentOptionsListener;
import whatsappMessengerView.AudioRecordView;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static filePicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static filePicker.activity.ImagePickActivity.IS_NEED_CAMERA;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class CreateCaseActivity extends AppCompatActivity {

    CreateCaseActivity context;
    String TAG = "CreateCaseActivity";
    String auth, userId, token, personId, caseId, compId;

    @BindView(R.id.etSubject)
    EditText etSubject;
    @BindView(R.id.layoutAttachment)
    CardView layoutAttachment;
    @BindView(R.id.layoutAttachmentOptions)
    LinearLayout layoutAttachmentOptions;
    @BindView(R.id.imageViewAttachment)
    ImageView imageViewAttachment;

    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.subjectIl)
    TextInputLayout subjectIl;

    @BindView(R.id.createBtn)
    Button createBtn;

    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.fileRecycler)
    RecyclerView fileRecycler;

    Resources resources;
    Toolbar mActionBarToolbar;
    boolean isNewCase = true;
    ViewFailDialog viewFailDialog;
    Intent intent;
    private float dp = 0;

    enum SELECTED_TYPE {
        CAMERA, GALLERY, PDF, AUDIO, XL;
    }

    SelectedFilesAdapter selectedFilesAdapter;

    CommunicationActivity.SELECTED_TYPE selected_type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_create_case);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        viewFailDialog = new ViewFailDialog();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        selectedFilesAdapter = new SelectedFilesAdapter(mSelected, this);
        fileRecycler.setHasFixedSize(true);
        fileRecycler.setLayoutManager(new LinearLayoutManager(this));
        fileRecycler.setNestedScrollingEnabled(false);
        fileRecycler.setAdapter(selectedFilesAdapter);
        TextView title = (TextView) findViewById(R.id.tittle);
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mActionBarToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateCaseActivity.super.onBackPressed();
                    }
                }
        );

        audioRecordView = new AudioRecordView();
        audioRecordView.initView((FrameLayout) findViewById(R.id.layoutMain));

//        audioRecordView.getMessageView().setMinLines(5);
        audioRecordView.getMessageView().setVisibility(View.GONE);
        audioRecordView.getAttachmentView().setVisibility(View.GONE);
        audioRecordView.getCameraView().setVisibility(View.GONE);
        audioRecordView.getMessageView().setFocusable(false);
        audioRecordView.getMessageView().setEnabled(false);

        audioRecordView.showCameraIcon(false);
        audioRecordView.showEmojiIcon(false);
        audioRecordView.showAttachmentIcon(false);
        audioRecordView.layoutMessage.setVisibility(View.GONE);
//        audioRecordView.getMessageView().setHint("Description");
        audioRecordView.getEmojiView().setVisibility(View.GONE);
        List<AttachmentOption> attachmentOptions = new ArrayList<>();
        attachmentOptions.add(new AttachmentOption(AttachmentOption.DOCUMENT_ID, "Add File", R.drawable.ic_attachment_document));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.CAMERA_ID, "Capture", R.drawable.ic_attachment_camera));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.GALLERY_ID, "Select", R.drawable.ic_attachment_gallery));
        attachmentOptions.add(new AttachmentOption(AttachmentOption.AUDIO_ID, "Audio", R.drawable.ic_attachment_audio));

        setupAudioRecord();
        setAttachmentOptions(attachmentOptions, new AttachmentOptionsListener() {
            @Override
            public void onClick(AttachmentOption attachmentOption) {
                switch (attachmentOption.getId()) {

                    case AttachmentOption.DOCUMENT_ID: //Ids for default Attachment Options
                        Intent intent4 = new Intent(CreateCaseActivity.this, NormalFilePickActivity.class);
                        intent4.putExtra(Constant.MAX_NUMBER, 1);
                        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"pdf", "xls"});
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
                        Intent intent3 = new Intent(CreateCaseActivity.this, AudioPickActivity.class);
                        intent3.putExtra(IS_NEED_RECORDER, false);
                        intent3.putExtra(Constant.MAX_NUMBER, 3);
                        startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
                        break;
                    case AttachmentOption.LOCATION_ID:
//                        showToast("Location Clicked");
                        break;
                    case AttachmentOption.CONTACT_ID:
//                        showToast("Contact Clicked");
                        break;
                }
            }
        });
        setupAttachmentOptions();
       /* imageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttachmentOptionView();
            }
        });*/
//        audioRecordView.showAttachmentOptionView();
        intent = getIntent();
        isNewCase = intent.getBooleanExtra("isNewCase", true);
        if (isNewCase) {
            title.setText("Create New Case");
            subjectIl.setHint("Subject");
        } else {
            caseId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CASE_ID);
            title.setText("Add Detailed Message");
            subjectIl.setHint("Message");
        }

        onClicks();
    }


    private void onClicks() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSubject.getText().toString())) {
//                    if (isNewCase)
                    AppConstants.failToast(context, "Please enter subject");
//                    else
//                        AppConstants.failToast(context, "Please enter message");
                    return;
                }
                insertMessage("");
            }
        });
    }

    private List<AttachmentOption> attachmentOptionList;
    private AttachmentOptionsListener attachmentOptionsListener;
    private List<LinearLayout> layoutAttachments;

    public void setAttachmentOptions(List<AttachmentOption> attachmentOptionList, final AttachmentOptionsListener attachmentOptionsListener) {

        this.attachmentOptionList = attachmentOptionList;
        this.attachmentOptionsListener = attachmentOptionsListener;

        if (this.attachmentOptionList != null && !this.attachmentOptionList.isEmpty()) {
            layoutAttachmentOptions.removeAllViews();
            int count = 0;
            LinearLayout linearLayoutMain = null;
            layoutAttachments = new ArrayList<>();

            for (final AttachmentOption attachmentOption : this.attachmentOptionList) {

                if (count == 6) {
                    break;
                }

                if (count == 0 || count == 3) {
                    linearLayoutMain = new LinearLayout(context);
                    linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutMain.setGravity(Gravity.CENTER);

                    layoutAttachmentOptions.addView(linearLayoutMain);
                }

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (dp * 84), LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setPadding((int) (dp * 4), (int) (dp * 12), (int) (dp * 4), (int) (dp * 0));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);

                layoutAttachments.add(linearLayout);

                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (dp * 48), (int) (dp * 48)));
                imageView.setImageResource(attachmentOption.getResourceImage());

                TextView textView = new TextView(context);
                TextViewCompat.setTextAppearance(textView, R.style.TextAttachmentOptions);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setPadding((int) (dp * 4), (int) (dp * 4), (int) (dp * 4), (int) (dp * 0));
                textView.setMaxLines(1);
                textView.setText(attachmentOption.getTitle());

                linearLayout.addView(imageView);
                linearLayout.addView(textView);

                linearLayoutMain.addView(linearLayout);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideAttachmentOptionView();
                        CreateCaseActivity.this.attachmentOptionsListener.onClick(attachmentOption);
                    }
                });

                count++;
            }
        }
    }

    public void hideAttachmentOptionView() {
        if (layoutAttachment.getVisibility() == View.VISIBLE) {
            imageViewAttachment.performClick();
        }
    }

    public void showAttachmentOptionView() {
        if (layoutAttachment.getVisibility() != View.VISIBLE) {
            imageViewAttachment.performClick();
        }
    }

    private void setupAttachmentOptions() {
        int screenWidth, screenHeight;
        boolean removeAttachmentOptionAnimation = false;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        boolean isLayoutDirectionRightToLeft;
        isLayoutDirectionRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        imageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutAttachment.getVisibility() == View.VISIBLE) {
                    int x = isLayoutDirectionRightToLeft ? (int) (dp * (18 + 40 + 4 + 56)) : (int) (screenWidth - (dp * (18 + 40 + 4 + 56)));
                    int y = (int) (dp * 220);

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(screenWidth - (dp * (8 + 8)), (dp * 220));

                    Animator anim = ViewAnimationUtils.createCircularReveal(layoutAttachment, x, y, endRadius, startRadius);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            layoutAttachment.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    anim.start();

                } else {

                    if (!removeAttachmentOptionAnimation) {
                        int count = 0;
                        if (layoutAttachments != null && !layoutAttachments.isEmpty()) {

                            int[] arr = new int[]{5, 4, 2, 3, 1, 0};

                            if (isLayoutDirectionRightToLeft) {
                                arr = new int[]{3, 4, 0, 5, 1, 2};
                            }

                            for (int i = 0; i < layoutAttachments.size(); i++) {
                                if (arr[i] < layoutAttachments.size()) {
                                    final LinearLayout layout = layoutAttachments.get(arr[i]);
                                    layout.setScaleX(0.4f);
                                    layout.setAlpha(0f);
                                    layout.setScaleY(0.4f);
                                    layout.setTranslationY(dp * 48 * 2);
                                    layout.setVisibility(View.INVISIBLE);

                                    layout.animate().scaleX(1f).scaleY(1f).alpha(1f).translationY(0).setStartDelay((count * 25) + 50).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
                                    layout.setVisibility(View.VISIBLE);

                                    count++;
                                }
                            }
                        }
                    }

                    int x = isLayoutDirectionRightToLeft ? (int) (dp * (18 + 40 + 4 + 56)) : (int) (screenWidth - (dp * (18 + 40 + 4 + 56)));
                    int y = (int) (dp * 220);

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(screenWidth - (dp * (8 + 8)), (dp * 220));

                    Animator anim = ViewAnimationUtils.createCircularReveal(layoutAttachment, x, y, startRadius, endRadius);
                    anim.setDuration(500);
                    layoutAttachment.setVisibility(View.VISIBLE);
                    anim.start();
                }
            }
        });
    }


    AudioRecordView audioRecordView;

    private void setupAudioRecord() {
        if (checkPermission()) {
            audioRecordView.setRecordingListener(new AudioRecordView.RecordingListener() {
                @Override
                public void onRecordingStarted() {
                    try {
                        selected_type = CommunicationActivity.SELECTED_TYPE.AUDIO;
                        AudioSavePathInDevice =
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_MUSIC + "/" +
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
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "AUdio path " + AudioSavePathInDevice);
                    mSelected.clear();
                    mSelected.add(Uri.parse(AudioSavePathInDevice));
                    selectedFilesAdapter.setSelectedOption(selected_type);
                    selectedFilesAdapter.notifyDataSetChanged();
//                    insertMessage("");
                }

                @Override
                public void onRecordingCanceled() {
                    try {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                AppConstants.failToast(context,"Cancelled");
                }
            });
        } else {
            requestPermission();
        }
    }

    List<Uri> mSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CAMERA1:
                selected_type = CommunicationActivity.SELECTED_TYPE.CAMERA;
                String result = "capture1";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
                break;

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    mSelected.clear();
//                    mSelected = Matisse.obtainResult(data);
                    selected_type = CommunicationActivity.SELECTED_TYPE.GALLERY;
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);

                    Log.e(TAG, "GALLERY SELECTED " + new Gson().toJson(list));
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mSelected.add(Uri.parse(list.get(i).getPath()));
                        }

                        selectedFilesAdapter.setSelectedOption(selected_type);
                        selectedFilesAdapter.notifyDataSetChanged();
//                        insertMessage("");
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
                    selected_type = CommunicationActivity.SELECTED_TYPE.AUDIO;
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    if (list != null && list.size() > 0) {
                        for (AudioFile audioFile : list) {
                            mSelected.add(Uri.parse(audioFile.getPath()));
                        }
                        selectedFilesAdapter.setSelectedOption(selected_type);
                        selectedFilesAdapter.notifyDataSetChanged();
//                        insertMessage("");
                    }
                }
                break;
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    mSelected.clear();
                    selected_type = CommunicationActivity.SELECTED_TYPE.PDF;
                    if (list != null && list.size() > 0) {
                        for (NormalFile audioFile : list) {
                            mSelected.add(Uri.parse(audioFile.getPath()));
                        }
                        selectedFilesAdapter.setSelectedOption(selected_type);
                        selectedFilesAdapter.notifyDataSetChanged();
//                        insertMessage("");
                    }

                }
                break;
        }

    }


    public void insertMessage(String msg) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("person_id", personId);
        jsonObject.addProperty("sender_id", personId);
//        jsonObject.addProperty("msg",etSubject.getText().toString().trim());
        jsonObject.addProperty("message", etDescription.getText().toString().trim());
        jsonObject.addProperty("receiver_id", "0");
        jsonObject.addProperty("status", "N");
        jsonObject.addProperty("is_sent", "Y");
        jsonObject.addProperty("is_read", "N");
        jsonObject.addProperty("subject", etSubject.getText().toString().trim());
        Log.e(TAG, "Posting msg " + new Gson().toJson(jsonObject));
        addMessage(etDescription.getText().toString().trim(), etSubject.getText().toString().trim());

    }


    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).replace(".", "");
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());

        if (selected_type == CommunicationActivity.SELECTED_TYPE.CAMERA) {
          /*  File compressedFile=compressFile(file);
            if (compressedFile!=null)
                file=compressedFile;*/
            try {
                Log.e(TAG, "SiliCompressor BEFORE " + file.length() + ", " + file.getPath());
                if (file.length() / 1024 > 150)
                    file = compressFile(file);
                Log.e(TAG, "SiliCompressor AFTER " + file.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (selected_type == CommunicationActivity.SELECTED_TYPE.GALLERY) {
          /*  File compressedFile=compressFile(file);
            if (compressedFile!=null)
                file=compressedFile;*/
                try {
//                    File dest=new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES+"/"+AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.OTHER, context)+"/"+AppConstants.getCurrentMills()+".jpg");
//                     String path =copy(file,dest);
                    Log.e(TAG, "SiliCompressor BEFORE " + file.length());
//                    String destPath= SiliCompressor.with(context).compress(dest.getAbsolutePath(),dest);
                    file = compressFile(file);
                    Log.e(TAG, "SiliCompressor AFTER " + file.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//        Log.e(TAG, "Uri to file " + file.getAbsolutePath());
        // create RequestBody instance from file
        if (file != null) {
            String extension = getExtension(fileUri.getPath());
            if (extension.contains("."))
                extension = extension.replace(".", "");
            RequestBody requestFile =
                    RequestBody.create(
//                            MediaType.parse(getContentResolver().getType(fileUri)),
                            MediaType.parse("file/" + extension),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } else return null;
    }

    private List<MultipartBody.Part> getMultipartFiles() {
        List<MultipartBody.Part> partList = new ArrayList<>();
        String name = "";
        if (selected_type == CommunicationActivity.SELECTED_TYPE.GALLERY || selected_type == CommunicationActivity.SELECTED_TYPE.CAMERA) {
            name = "image";
        } else if (selected_type == CommunicationActivity.SELECTED_TYPE.AUDIO) {
            name = "voice";
        } else if (selected_type == CommunicationActivity.SELECTED_TYPE.PDF) {
            name = "pdf";
        } else if (selected_type == CommunicationActivity.SELECTED_TYPE.XL) {
            name = "excel";
        }

        if (isValidString(name))
            for (Uri uri : mSelected) {
                Log.e(TAG, "URIII:-" + uri.getPath() + "\n AND:-" + uri);
                MultipartBody.Part part = prepareFilePart(name, uri);
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

    private String copy(File file, File dest) {
        try {
            FileInputStream inStream = new FileInputStream(file);
            FileOutputStream outStream = new FileOutputStream(dest);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dest.getAbsolutePath();
    }

    private void copyFiles(InsertCaseResponse caseResponse, String msg) {


        List<CaseFileDatum> fileLinks = new ArrayList<>();
        Gson gson = new Gson();
        MessageObj messageObj = gson.fromJson(caseResponse.getData().getMessage(), MessageObj.class);
        if (messageObj.getFilesLink() != null && messageObj.getFilesLink().size() > 0) {
            for (int i = 0; i < messageObj.getFilesLink().size(); i++) {
                CaseFileDatum caseFileDatum = new CaseFileDatum();
                caseFileDatum.setExtension(getExtension(messageObj.getFilesLink().get(i)));
                caseFileDatum.setFileLink(messageObj.getFilesLink().get(i));

                AppConstants.FILE_PATH_TYPE type = null;
                if (selected_type == CommunicationActivity.SELECTED_TYPE.AUDIO) {
                    type = AppConstants.FILE_PATH_TYPE.AUDIO;

                } else if (selected_type == CommunicationActivity.SELECTED_TYPE.PDF || selected_type == CommunicationActivity.SELECTED_TYPE.XL) {
                    type = AppConstants.FILE_PATH_TYPE.DOCUMENT;
                } else if (selected_type == CommunicationActivity.SELECTED_TYPE.GALLERY || selected_type == CommunicationActivity.SELECTED_TYPE.CAMERA) {
                    type = AppConstants.FILE_PATH_TYPE.OTHER;
                }
                String fName = messageObj.getFilesLink().get(i).replace("/", "_");
                File direct = new File(Environment.DIRECTORY_PICTURES + AppConstants.getImagePath(null, context));
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                File path = new File(Environment.DIRECTORY_PICTURES, AppConstants.getImagePath(type, context));
                if (!path.exists()) {
                    path.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES + "/" + AppConstants.getImagePath(type, context) + "/" + fName);
//
                copy(new File(mSelected.get(i).getPath()), file);
                fileLinks.add(caseFileDatum);

            }
        }
        AppConstants.successToast(context, "Case created successfully");
//        CaseDatum datum = new CaseDatum(0, Long.valueOf(caseId), Long.valueOf(compId), Long.valueOf(personId),
//                0, new Gson().toJson(messageObj), null, "Y", "N", "N", "N");
//        datum.setFileLinks(fileLinks);
//        caseDatumList.add(datum);
//        caseRvAdapter.notifyDataSetChanged();
//
//        messageRecycler.scrollToPosition(caseDatumList.size()-1);

    }

    private void addMessage(String msg, String subject) {
        try {
//            createBtn.setClickable(false);
//            createBtn.setEnabled(false);
//            progressBar.setVisibility(View.VISIBLE);

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};


//            Log.e(TAG,"Adding Req "+new Gson().toJson(object));
            apiInterface.addMessageWithFiles(getMultipartFiles(), getRequestBody(userId), getRequestBody(compId), getRequestBody(token), getRequestBody(personId)
                    , getRequestBody(personId), getRequestBody(msg), getRequestBody(subject), getRequestBody("0"), getRequestBody("N"), getRequestBody("Y"), getRequestBody("N"),
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
                            if (mSelected != null && mSelected.size() > 0)
                                copyFiles(response.body(), msg);
                            else
                                AppConstants.successToast(context, "Case created successfully");

                            setResult(RESULT_OK, intent);
                            finish();

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

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer;


    private final int RequestPermissionCode = 661, REQUEST_CAMERA1 = 662;

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

    String pictureImagePath = "";

    private void cameraIntent() {
        String[] params = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context, params)) {
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
        } else
            EasyPermissions.requestPermissions(this, getString(R.string.permission_necessary), 1, params);
    }

    private void onCaptureImageResult(Intent data, String onclick) {
        File imgFile = new File(pictureImagePath);

        if (imgFile.exists()) {
            Log.e(TAG, "CAPTURE FILE exist " + pictureImagePath);
            mSelected.clear();
            mSelected.add(Uri.parse(pictureImagePath));
            selectedFilesAdapter.setSelectedOption(selected_type);
            selectedFilesAdapter.notifyDataSetChanged();
//            insertMessage("");
        } else {
            Log.e(TAG, "CAPTURE FILE not exist");
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


}