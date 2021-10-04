package com.i9930.croptrails.Communication.Adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.Communication.Model.CaseDatum;
import com.i9930.croptrails.Communication.Model.MessageObj;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class CaseRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<CaseDatum> caseList;
    Gson gson;
    boolean isCaseForThreads = true;
    double personId = 0.0;
    private static final int THREAD = 0, CHAT = 1;
    public interface OnItemClickListener{
        public void onItemClicked(int index,long caseId);
    }
    OnItemClickListener onItemClickListener;
    OnCaseClickListener onCaseClickListener;
    public interface OnCaseClickListener{
        public void onImageClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView);
        public void onAudioClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView);
        public void onFileClick(int position,CaseDatum caseDatum,String url,String fileName,ImageView imageView);
    }
    int width;
    LinearLayout.LayoutParams layoutParams;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isCaseForThreads)
            return THREAD;
        else return CHAT;
    }
    Resources resources;
    public CaseRvAdapter(Activity context, List<CaseDatum> caseList, boolean isCaseForThreads) {
        this.context = context;
        this.caseList = caseList;
        this.isCaseForThreads = isCaseForThreads;
        personId = Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        gson = new Gson();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width= displayMetrics.widthPixels;
        resources=context.getResources();
        layoutParams= new LinearLayout.LayoutParams((width-100), (width-100));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == THREAD) {
            view = LayoutInflater.from(context).inflate(R.layout.rv_layout_cases, parent, false);
            return new Holder(view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.rv_layout_communication_chat, parent, false);
            return new HolderChat(view);
        }

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final CaseDatum datum = caseList.get(position);
        String subject = null;
        String doa=datum.getDoa();
        try {
            doa=AppConstants.convertDateTimeToLocal(doa);
        }catch (Exception e){
            e.printStackTrace();
        }
        String msg = "";
        List<String> filesLink=new ArrayList<>();;
        if (AppConstants.isValidString(datum.getMessage())) {
            try {
                MessageObj messageObj = gson.fromJson(datum.getMessage(), MessageObj.class);
                if (AppConstants.isValidString(messageObj.getSubject()))
                    subject = messageObj.getSubject();
                if (AppConstants.isValidString(messageObj.getMessage()))
                    msg = messageObj.getMessage();
                filesLink=messageObj.getFilesLink();

            } catch (Exception e) {

            }
        }
        if (viewHolder.getItemViewType() == THREAD) {
            Holder holder = (Holder) viewHolder;
            holder.subjectTv.setText(subject);
            holder.timeTv.setText(doa);
            holder.caseIdTv.setText("" + datum.getCaseId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null)
                        onItemClickListener.onItemClicked(position,datum.getCaseId());

                }
            });

            int color=0;
            if (AppConstants.isValidString(datum.getStatus())){
                if (datum.getStatus().trim().equalsIgnoreCase("N")){
                    color=R.color.yellow;
                }else if (datum.getStatus().trim().equalsIgnoreCase("C")){
                    color=R.color.green;
                }else if (datum.getStatus().trim().equalsIgnoreCase("R")){
                    color=R.color.purple;
                }else if (datum.getStatus().trim().equalsIgnoreCase("O")){
                    color=R.color.red;
                }
                if (color!=0){
                    setTextViewDrawableColor(holder.subjectTv,color);
                    setTextViewDrawableColor(holder.caseIdTv,color);
                    holder.timeTv.setTextColor(resources.getColor(color));
                }
            }

        } else {
            HolderChat holder = (HolderChat) viewHolder;

            if (datum.getFileLinks()!=null&&datum.getFileLinks().size()>0){
                for (int i=0;i<datum.getFileLinks().size();i++){
                    String imFile=datum.getFileLinks().get(i).getFileLink();
                    String ext=datum.getFileLinks().get(i).getExtension();
                    String fName=filesLink.get(i);
                    int countImage =0;

                    if ((ext.contains("png")||ext.contains("jpg")||ext.contains("jpeg"))){
                        countImage++;
                    }else if ((imFile.contains("amr")||imFile.contains("mp3"))){

                        checkFile(holder,datum,imFile,fName,ext, AppConstants.FILE_PATH_TYPE.AUDIO,position);
                    }else{
                        checkFile(holder,datum,imFile,fName,ext, AppConstants.FILE_PATH_TYPE.DOCUMENT,position);
                    }


                    if (countImage>=1){
//                        holder.messageSend.setVisibility(View.GONE);
                        holder.imageLayout.setVisibility(View.VISIBLE);
                        holder.img1.setVisibility(View.VISIBLE);
                        showImage(holder.img1,imFile,fName);
                        if (onCaseClickListener!=null) {
                            View.OnClickListener listener=new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (v.getId()==R.id.img1)
                                    onCaseClickListener.onImageClick(position,datum,imFile,fName,holder.downloadImg);
                                }
                            };
                            holder.img1.setOnClickListener(listener);
                            holder.img2.setOnClickListener(listener);
                            holder.img3.setOnClickListener(listener);

                        }
                    } if (countImage>=2){
                        holder.img2.setVisibility(View.VISIBLE);
                        showImage(holder.img2,imFile,fName);
                    } if (countImage>=3){
                        holder.img3.setVisibility(View.VISIBLE);
                        showImage(holder.img3,imFile,fName);
                    }
                }
            }else{
                holder.imageLayout.setVisibility(View.GONE);
            }
            holder.fileLayout.setVisibility(View.VISIBLE);
            holder.messageSend.setText(msg);

            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)holder.fileLayout.getLayoutParams();

            if (personId == datum.getSenderId()) {

                holder.fileLayout.setBackgroundResource(R.drawable.message_sender_background);
                linearParams.setMargins(100, 0, 0, 0);
                linearParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.fileLayout.setGravity(Gravity.RIGHT);
            } else {
                holder.fileLayout.setBackgroundResource(R.drawable.message_reciever_background);
                holder.fileLayout.setGravity(Gravity.LEFT);
                linearParams.setMargins(0, 0, 100, 0);
                linearParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            if (!AppConstants.isValidString(msg)) {
                holder.messageSend.setVisibility(View.GONE);
            }if (!AppConstants.isValidString(subject)) {
                holder.subjectTv.setVisibility(View.GONE);
            }else{
                holder.subjectTv.setVisibility(View.VISIBLE);
                holder.subjectTv.setText(subject);
            }
            holder.fileLayout.setLayoutParams(linearParams);
            holder.setIsRecyclable(false);
        }
    }

    private void showImage(ImageView imageView,String url,String name){
//        imageView.setLayoutParams(layoutParams);
//        imageView.getLayoutParams().width=(width-30);
//        imageView.getLayoutParams().height=(width-100);
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.crp_trls_rounded_icon)
//                .error(R.drawable.crp_trls_rounded_icon);
        String filePath=AppConstants.getFullPath(AppConstants.FILE_PATH_TYPE.OTHER,context,name.replace("/","_"));
        File file=new File(filePath);
        if (file.exists())
            url=filePath;
        Log.e("Downloading","Downloading URL "+url+"\n "+filePath);
        Glide.with(context).load(url)/*.apply(options)*/.into(imageView);
    }

    private void checkFile(HolderChat holderChat,final CaseDatum datum,
                           String url,String name,String extention,
                           AppConstants.FILE_PATH_TYPE type,int position){
        String filePath=AppConstants.getFullPath(type,context,name.replace("/","_"));
        File file=new File(filePath);
        if (file.exists())
        {
            holderChat.downloadImg.setVisibility(View.GONE);
        }else{
            holderChat.downloadImg.setVisibility(View.VISIBLE);
        }
        holderChat.fileNameTv.setText(name);
//        holderChat.messageSend.setVisibility(View.GONE);
        holderChat.fileContentLayout.setVisibility(View.VISIBLE);
        if (extention.toLowerCase().contains("mp3")||extention.toLowerCase().contains("amr")) {
            holderChat.extensionImg.setImageResource(R.drawable.extension_amr);
        }
        else  {
            holderChat.extensionImg.setImageResource(R.drawable.extension_pdf);
        }
        holderChat.fileContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extention.toLowerCase().contains("mp3")||extention.toLowerCase().contains("amr")) {
                    onCaseClickListener.onAudioClick(position,datum, url, name,holderChat.downloadImg);
                }
                else if (extention.toLowerCase().contains("pdf")||extention.toLowerCase().contains("xl")||extention.toLowerCase().contains("xls")) {
                    onCaseClickListener.onFileClick(position,datum, url, name,holderChat.downloadImg);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (caseList == null)
            return 0;
        else return caseList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView timeTv, caseIdTv, subjectTv;


        public Holder(@NonNull View itemView) {
            super(itemView);
            subjectTv = itemView.findViewById(R.id.subjectTv);
            caseIdTv = itemView.findViewById(R.id.caseIdTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }

    public class HolderChat extends RecyclerView.ViewHolder {

        TextView  messageSend ;
        LinearLayout imageLayout;
        ImageView img1,img2,img3;

        LinearLayout fileLayout;
        ImageView extensionImg,downloadImg;
        TextView fileNameTv,extensionTv,subjectTv;
        RelativeLayout fileContentLayout;


        public HolderChat(@NonNull View itemView) {
            super(itemView);
            subjectTv = itemView.findViewById(R.id.subjectTv);
            fileContentLayout = itemView.findViewById(R.id.fileContentLayout);
            fileLayout = itemView.findViewById(R.id.fileLayout);
            extensionImg = itemView.findViewById(R.id.extensionImg);
            fileNameTv = itemView.findViewById(R.id.fileNameTv);
            downloadImg = itemView.findViewById(R.id.downloadImg);
            extensionTv = itemView.findViewById(R.id.extensionTv);

            img3 = itemView.findViewById(R.id.img3);
            img2 = itemView.findViewById(R.id.img2);
            img1 = itemView.findViewById(R.id.img1);
            messageSend = itemView.findViewById(R.id.messageSend);
            imageLayout = itemView.findViewById(R.id.imageLayout);
        }
    }

    public OnCaseClickListener getOnCaseClickListener() {
        return onCaseClickListener;
    }

    public void setOnCaseClickListener(OnCaseClickListener onCaseClickListener) {
        this.onCaseClickListener = onCaseClickListener;
    }
}
