package com.i9930.croptrails.Landing.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Landing.Fragments.InterFaces.ExpenseRemoveNotifyInterface;
import com.i9930.croptrails.Landing.Fragments.Model.Datum;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseRemoveResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

/**
 * Created by hp on 03-07-2018.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private String[] mDataSet;
    Context context;
    List<Datum> expenseData;
    boolean status;
    ExpenseRemoveNotifyInterface notifyInterface;
    Activity activity;


    public ExpenseAdapter(List<Datum> expenseData, Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.expenseData = expenseData;
        //Log.d("Data:", "TaskRecyclerAdapter :" + farmImages);
    }

    public ExpenseAdapter(List<Datum> expenseData, Context context, ExpenseRemoveNotifyInterface notifyInterface) {
        this.context = context;
        this.expenseData = expenseData;
        this.notifyInterface = notifyInterface;
        //Log.d("Data:", "TaskRecyclerAdapter :" + farmImages);
    }

    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_content, parent, false);
        ExpenseAdapter.ViewHolder vh = new ExpenseAdapter.ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final ExpenseAdapter.ViewHolder holder, final int position) {
        Datum datum = expenseData.get(position);

        if (datum.getVerifiedOn() == null) {
            holder.deleteExpenseTv.setVisibility(View.VISIBLE);
            holder.verifiedExpenseImage.setVisibility(View.GONE);
        } else {
            holder.deleteExpenseTv.setVisibility(View.GONE);
            holder.verifiedExpenseImage.setVisibility(View.VISIBLE);
        }

        if (datum.getExpDate() != null) {
            //holder.date.setText(datum.getExpDate());
            String date = datum.getExpDate();
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date d = input.parse(date);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                holder.date.setText(output.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (datum.getComment() != null) {
            holder.comment.setText(datum.getComment());
        }
        if (datum.getAmount() != null) {
            holder.amount.setText(context.getResources().getString(R.string.rupee_symbol) + datum.getAmount());
        }

        //String img_url=datum.getImgUrl();


        if (datum.getImgUrl() != null) {
            Uri uriprofile = Uri.parse(datum.getImgUrl());
           /* if (datum.getImgUrl().contains("croptrailsimages.s3")){
                ShowAwsImage.getInstance(context).downloadFile(uriprofile,holder.expense_image,datum.getImgUrl());
            }else {*/
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.crp_trails_icon_splash)
                        .error(R.drawable.crp_trails_icon_splash);
                Glide.with(context).load(uriprofile).apply(options).into(holder.expense_image);
            //}
        } else {
            holder.expense_image.setImageDrawable(context.getResources().getDrawable(R.drawable.crp_trails_icon_splash));
        }

        holder.deleteExpenseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage(context.getResources().getString(R.string.confirm_remove_ecpense_msg))
                        .setCancelable(false)
                        .setPositiveButton((context.getResources().getString(R.string.yes)), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                notifyInterface.deletingExpense();
                                if (deleteExpense(expenseData.get(position).getSvDailyExpId(), position)) {
                                   /* expenseData.remove(position);
                                    notifyDataSetChanged();*/
                                } else {

                                }
                            }
                        })
                        .setNegativeButton((context.getResources().getString(R.string.no)), null)
                        .show();
            }
        });

       /* if (farm_images.getTaskTitle() != null) {
            holder.tvtitle.setText(farm_images.getTaskTitle());
        }*/
      /* if(farm_images.getFarm_image_link()!=null){
           holder.farm_image.setImageResource();
       }*/
       /* RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.addfarm)
                .error(R.drawable.addfarmnew);

        if (farm_images.getFarm_image_link() != null) {
            if (farm_images.getFarm_image_link().equals("null")) {
                holder.farm_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_green_24dp));
            } else {
                Uri uriprofile = Uri.parse(farm_images.getFarm_image_link());
                Glide.with(context).load(uriprofile).apply(options).into(holder.farm_image);

            }
        }*/
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public int getItemCount() {
        return expenseData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView expense_image;
        TextView amount, comment, date, deleteExpenseTv;
        ImageView verifiedExpenseImage;

        public ViewHolder(View v) {
            super(v);
            expense_image = (ImageView) v.findViewById(R.id.img_expense_recycler_content);
            amount = (TextView) v.findViewById(R.id.amount_expense_content);
            comment = (TextView) v.findViewById(R.id.comment_exp_content);
            date = (TextView) v.findViewById(R.id.date_exp_content);

            verifiedExpenseImage = (ImageView) v.findViewById(R.id.verifiedExpenseImage);
            deleteExpenseTv = v.findViewById(R.id.deleteExpenseTv);

        }
    }

    private boolean deleteExpense(String expId, final int position) {
        status = false;
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String deletedBy = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.deleteExpense(expId, deletedBy, userId, token).enqueue(new Callback<ExpenseRemoveResponse>() {
            @Override
            public void onResponse(Call<ExpenseRemoveResponse> call, Response<ExpenseRemoveResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("ExpenseAdapter", new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(activity, response.body().getMsg());
                    }  else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(activity, context.getResources().getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(activity,context.getResources().getString(R.string.authorization_expired));
                    }else if (response.body().getStatus() == 1) {
                        status = true;
                        Toasty.success(context, context.getResources().getString(R.string.expense_remove_success_msg), Toast.LENGTH_LONG).show();
                        notifyInterface.expenseDeletedSuccess(position);
                    } else {
                        notifyInterface.expenseDeletingFailed();
                        status = false;
                        Toasty.error(context, context.getResources().getString(R.string.expense_remove_fail_msg), Toast.LENGTH_LONG).show();

                    }
                }  else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    status = false;
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(activity, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    status = false;
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(activity,context.getResources().getString(R.string.authorization_expired));
                } else {
                    status = false;
                    notifyInterface.expenseDeletingFailed();
                    Toasty.error(context, context.getResources().getString(R.string.expense_remove_fail_msg), Toast.LENGTH_LONG).show();
                    try {
                        Log.e("ExpenseAdapter", response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpenseRemoveResponse> call, Throwable t) {
                notifyInterface.expenseDeletingFailed();
                Log.e("ExpenseAdapter", t.toString());
                Toasty.error(context, context.getResources().getString(R.string.expense_remove_fail_msg), Toast.LENGTH_LONG).show();
                status = false;
            }
        });
        return status;
    }
}
