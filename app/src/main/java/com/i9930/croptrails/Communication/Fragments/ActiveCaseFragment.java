package com.i9930.croptrails.Communication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Communication.Adapter.CaseRvAdapter;
import com.i9930.croptrails.Communication.CommunicationActivity;
import com.i9930.croptrails.Communication.Model.CaseDatum;
import com.i9930.croptrails.Communication.Model.CaseListResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static android.app.Activity.RESULT_OK;

public class ActiveCaseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String TAG="ActiveCaseFragment";
    Context context;
    Resources resources;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActiveCaseFragment(OnFragmentInteractionListener onFragmentInteractionListener) {
        // Required empty public constructor
        this.onFragmentInteractionListener=onFragmentInteractionListener;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCaseStatusChanged();
    }
    OnFragmentInteractionListener onFragmentInteractionListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_case, container, false);
    }
    RecyclerView caseRecycler;
    List<CaseDatum> caseDatumList=new ArrayList<>();
    CaseRvAdapter caseRvAdapter;

    String auth, userId, token, personId,compId;
    ViewFailDialog viewFailDialog;
    TextView noDataMsgTv;
    RelativeLayout noDataAvailableLayout;

    public static int REQ_CODE=101;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noDataMsgTv=view.findViewById(R.id.noDataMsgTv);
        noDataAvailableLayout=view.findViewById(R.id.noDataAvailableLayout);
        caseRecycler=view.findViewById(R.id.caseRecycler);
        caseRvAdapter=new CaseRvAdapter(getActivity(),caseDatumList,true);
        caseRvAdapter.setOnItemClickListener(new CaseRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index,long caseId) {
                Intent intent = new Intent(context, CommunicationActivity.class);
                intent.putExtra("isForeCase", true);
                intent.putExtra("case_id", caseId);
                startActivityForResult(intent,REQ_CODE);
            }
        });
        caseRecycler.setHasFixedSize(true);
        caseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        caseRecycler.setAdapter(caseRvAdapter);
        noDataMsgTv.setText(resources.getString(R.string.no_active_case_available));
        getCases();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"onActivityResult req:"+requestCode+", res:"+resultCode);
        if (requestCode==REQ_CODE&&resultCode==RESULT_OK){
            onFragmentInteractionListener.onCaseStatusChanged();
        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context=context;
        viewFailDialog= new ViewFailDialog();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        resources=getResources();
    }

    public void getCases() {
        //try {
        //progressDialog.show();
        caseDatumList.clear();
//        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id",  compId);
        jsonObject.addProperty("user_id",  userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("person_id",  personId);
        jsonObject.addProperty("sender_id",  personId);
        jsonObject.addProperty("status", "N" );
        ApiInterface apiInterface= RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

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
                    if (responseBody.getData()!=null&&responseBody.getData().size()>0)
                    {
                        caseRecycler.setVisibility(View.VISIBLE);
                        noDataAvailableLayout.setVisibility(View.GONE);
                        caseDatumList.addAll(responseBody.getData());
                        caseRvAdapter.notifyDataSetChanged();
                    }else{
                        caseRecycler.setVisibility(View.GONE);
                        noDataAvailableLayout.setVisibility(View.VISIBLE);
                    }
                } else if ( response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if ( response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    isSpinnerDataLoaded = false;
//                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG , "err  " + error);

                        viewFailDialog.showDialogForFinish(getActivity(), resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


//                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<CaseListResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                Log.e(TAG,"Fail "+ t.toString());


            }
        });

    }
}