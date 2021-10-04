package com.i9930.croptrails.Test.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class SeedFullScreenDialog extends DialogFragment {
    public static String TAG = "SeedFullScreenDialog";

    Context context;
    Resources resources;
    FetchFarmResult farmResult;
    Farm farm;
    boolean isSeedAdded;

    public SeedFullScreenDialog(FetchFarmResult farmResult, Farm farm, boolean isSeedAdded) {
        this.farmResult = farmResult;
        this.farm = farm;
        this.isSeedAdded = isSeedAdded;
    }

    public SeedFullScreenDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_submit_seed_qty_details, container, false);
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        Toolbar toolbar = view.findViewById(R.id.toolbar_full);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_18dp);
        //this.dismiss();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        toolbar.setTitle(resources.getString(R.string.seed_issue_details_label));
        toolbar.setTitleTextColor(resources.getColor(R.color.white));


        showSeedProvideDialog(view);

        return view;
    }

    private void dismissDialog() {
        this.dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private String convertAreaTo(String area) {
        Log.e(TAG,"Exp Area "+area);
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area) );
        return convertArea;
    }



    private void showSeedProvideDialog(View dialog) {
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

        EditText seedLotNoEt = dialog.findViewById(R.id.seedLotNoEt);
        EditText expectedAreaEt = dialog.findViewById(R.id.expectedAreaEt);

        EditText seedProvidedQtyEt = dialog.findViewById(R.id.seedProvidedQtyEt);
        EditText seedProvidedOnEt = dialog.findViewById(R.id.seedProvidedOnEt);

        Button submitSeedBtn = dialog.findViewById(R.id.submitSeedBtn);

        expectedAreaEt.setText(convertAreaTo(farmResult.getExpArea()));

        if (isSeedAdded) {
            seedLotNoEt.setText(farmResult.getSeedLotNo());
            seedProvidedQtyEt.setText(farmResult.getQtySeedProvided() + " " + resources.getString(R.string.kg_label));
            seedProvidedOnEt.setText(AppConstants.getShowableDate(farmResult.getSeedProvidedOn(),context));
            expectedAreaEt.setClickable(false);
            expectedAreaEt.setEnabled(false);

            seedLotNoEt.setClickable(false);
            seedLotNoEt.setEnabled(false);
            seedProvidedQtyEt.setClickable(false);
            seedProvidedQtyEt.setEnabled(false);
            seedProvidedOnEt.setClickable(false);
            seedProvidedOnEt.setEnabled(false);
            submitSeedBtn.setVisibility(View.GONE);

        }


        Calendar myCalendar=Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                seedProvidedOnEt.setText(AppConstants.getShowableDate(sdf.format(myCalendar.getTime()),context));
                seedProvidedOnEt.setError(null);
            }
        };
        seedProvidedOnEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                DatePickerDialog dpDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                dpDialog.show();
            }
        });

        submitSeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(seedLotNoEt.getText().toString())) {
                    seedLotNoEt.setError(resources.getString(R.string.please_enter_seed_lot_number_label));
                    Toasty.error(context, resources.getString(R.string.please_enter_seed_lot_number_label), Toast.LENGTH_LONG, false).show();
                } else if (TextUtils.isEmpty(expectedAreaEt.getText().toString())) {
                    expectedAreaEt.setError(resources.getString(R.string.pease_enter_dispached_area_label));

                    Toasty.error(context, resources.getString(R.string.pease_enter_dispached_area_label), Toast.LENGTH_LONG, false).show();
                } else if (TextUtils.isEmpty(seedProvidedQtyEt.getText().toString())) {
                    seedProvidedQtyEt.setError(resources.getString(R.string.please_enter_quantity_msg));

                    Toasty.error(context, resources.getString(R.string.please_enter_quantity_msg), Toast.LENGTH_LONG, false).show();
                } else if (TextUtils.isEmpty(seedProvidedOnEt.getText().toString())) {
                    seedProvidedOnEt.setError(resources.getString(R.string.please_enter_date_label));

                    Toasty.error(context, resources.getString(R.string.please_enter_date_label), Toast.LENGTH_LONG, false).show();
                } else {
                    addSeedData(seedLotNoEt.getText().toString(), expectedAreaEt.getText().toString(),
                            seedProvidedQtyEt.getText().toString(), seedProvidedOnEt.getText().toString());
                }
            }
        });

    }

    private void addSeedData(String seedLot, String expArea, String seedQty, String date) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(resources.getString(R.string.please_wait_msg));
        dialog.show();
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

        JsonObject obj=new JsonObject();
        obj.addProperty("user_id",""+userId);
        obj.addProperty("token",""+token);
        obj.addProperty("farm_id",""+farmId);
        obj.addProperty("seed_lot_no",""+seedLot);
        obj.addProperty("exp_area",""+expArea);
        obj.addProperty("qty_seed_provided",""+seedQty);
        obj.addProperty("seed_provided_on",""+AppConstants.getUploadableDate(date,context));
        Log.e(TAG,"Sending Data "+new Gson().toJson(obj));

        apiInterface.addSeedQty(obj).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    Log.e(TAG, "Seed Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        Toasty.success(context, resources.getString(R.string.seed_added_successful_msg), Toast.LENGTH_LONG, false).show();
                        startActivity(new Intent(context, LandingActivity.class));
                        getActivity().finishAffinity();
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                    } else {
                        Toasty.error(context, resources.getString(R.string.failed_to_add_seed_issue_details_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG, false).show();
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                }else {
                    dialog.dismiss();
                    try {
                        Toasty.error(context, resources.getString(R.string.failed_to_add_seed_issue_details_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG, false).show();
                        Log.e(TAG, "Seed Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Toasty.error(context, resources.getString(R.string.failed_to_add_seed_issue_details_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG, false).show();
                Log.e(TAG, "Seed Failure " + t.toString());
                dialog.dismiss();
            }
        });

    }


}
