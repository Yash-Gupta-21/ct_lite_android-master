package com.i9930.croptrails.Croppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;
import ai.api.model.Status;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import com.i9930.croptrails.Croppy.Adapter.CroppyMessageRvAdapter;
import com.i9930.croptrails.Croppy.Model.CroppyMessage;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.Utility.AppConstants;

public class CroppyActivity extends AppCompatActivity implements AIListener {

    CroppyActivity context;
    String TAG="CroppyActivity";
    AIDataService aiDataService;
    @BindView(R.id.messageRecycler)
    RecyclerView messageRecycler;
    @BindView(R.id.messageEt)
    EditText messageEt;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    CroppyMessageRvAdapter adapter;
    List<CroppyMessage> messageList=new ArrayList<>();
    Toolbar mActionBarToolbar;
    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
//        mAdView.setAdUnitId("ca-app-pub-5740952327077672/5919581872");
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e(TAG,"Add Errro "+adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croppy);
        context=this;
        ButterKnife.bind(this);
        loadAds();
        TextView tittle = (TextView) findViewById(R.id.tittle);
        tittle.setText("Croppy");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter=new CroppyMessageRvAdapter(context,messageList);
        messageRecycler.setAdapter(adapter);


        final AIConfiguration config = new AIConfiguration("38471c0e5f584af490674f19c33fb4ba",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

         aiDataService= new AIDataService(config);



      /* AIService aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiService.startListening();*/

      sendButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (!TextUtils.isEmpty(messageEt.getText().toString().trim())){

                  messageList.add(new CroppyMessage(messageEt.getText().toString().trim(),true));
                  adapter.notifyDataSetChanged();
                  requestQuery(messageEt.getText().toString().trim());
                  messageEt.setText("");
                  messageRecycler.scrollToPosition(messageList.size() - 1);
              }
          }
      });

        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                if (dropDown!=null){
                    Log.e(TAG,"Cache1 "+new Gson().toJson(dropDown));
                    dropDownT=dropDown;
                    try {
                        JSONArray array = new JSONArray(dropDown.getData());
                        List<CroppyMessage>msgs=new ArrayList<>();
                        for (int k = 0; k < array.length(); k++) {
                            JSONObject object = array.getJSONObject(k);
                            CroppyMessage msg= new Gson().fromJson(object.toString(), CroppyMessage.class);
                            msgs.add(msg);
                        }
                        messageList.addAll(msgs);
                        adapter.notifyDataSetChanged();
                        messageRecycler.scrollToPosition(messageList.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e(TAG,"Cache2 "+new Gson().toJson(dropDown));
                    DropDownT downT=new DropDownT(AppConstants.CHEMICAL_UNIT.CHAT,"[]");
                    DropDownCacheManager.getInstance(context).addChemicalUnit(downT, new DropDownCacheManager.DropDownAddListener() {
                        @Override
                        public void onDropDownAdded() {
                            DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                                @Override
                                public void onDataLoaded(DropDownT dropDown) {
                                    Log.e(TAG,"Cache3 "+new Gson().toJson(dropDown));
                                    dropDownT=dropDown;
                                }
                            },AppConstants.CHEMICAL_UNIT.CHAT);
                        }
                    });
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CHAT);



    }

    DropDownT dropDownT;

    private void requestQuery(String query){
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(query);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(request);
                    ResponseMessage msg=response.getResult().getFulfillment().getMessages().get(0);
                    Log.e(TAG,"Message "+new Gson().toJson(msg));
                    Log.e(TAG,"Message2 "+new Gson().toJson(response.getResult().getFulfillment().getMessages()));
                    JSONObject obj=new JSONObject(new Gson().toJson(msg));
                    JSONArray array=obj.getJSONArray("speech");
                    // Show results in TextView.

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // messageEt.setText(m);
                            for (int i=0;i<array.length();i++) {
                                String m= null;
                                try {
                                    m = ""+array.getString(i);
                                    messageList.add(new CroppyMessage(m, false));
                                    adapter.notifyDataSetChanged();
                                    messageRecycler.scrollToPosition(messageList.size() - 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    return response;
                } catch (AIServiceException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    // process aiResponse here
                }
            }
        }.execute(aiRequest);

    }

    public void onResult(final AIResponse response) {
        Log.e(TAG, "Action1: " + response.getResult().getAction());
        final Status status = response.getStatus();
        Log.e(TAG, "Status code1: " + status.getCode());
        Log.e(TAG, "Status type1: " + status.getErrorType());

        final Result result = response.getResult();
        Log.e(TAG, "Resolved query1: " + result.getResolvedQuery());
        // process response object
    }

    @Override
    public void onError(ai.api.model.AIError error) {

        Log.e(TAG, "Error query: " + error.toString());

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dropDownT!=null) {

            List<CroppyMessage>list=messageList;
            if (messageList.size()>20){
                list=messageList.subList(Math.max(messageList.size() - 20, 0), messageList.size());
            }
            String msgs=new Gson().toJson(list);
            dropDownT.setData(msgs);
            DropDownCacheManager.getInstance(context).updateFarm(dropDownT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
