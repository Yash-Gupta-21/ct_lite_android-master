package com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

/**
 * Created by hp on 15-08-2018.
 */

public class SampleGermiAdapter extends RecyclerView.Adapter<SampleGermiAdapter.ViewHolder> {
        Context context;
        List<SampleGerminationDatum> sampleGerminationData;
        Resources resources;

        public SampleGermiAdapter(List<SampleGerminationDatum> sampleGerminationData, Context context) {
            this.context = context;
            this.sampleGerminationData = sampleGerminationData;
            final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
            Context contextlang = LocaleHelper.setLocale(context, languageCode);
            resources = contextlang.getResources();

            //Log.d("Data:", "TaskRecyclerAdapter :" + farmImages);
        }

        public SampleGermiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_space_content, parent, false);
            SampleGermiAdapter.ViewHolder vh = new SampleGermiAdapter.ViewHolder(v);
            return vh;
        }

        public void onBindViewHolder(final SampleGermiAdapter.ViewHolder holder, int position) {
            double actualPop=1.0,idealPop=1.0;
            SampleGerminationDatum sampleGerminationDatum = sampleGerminationData.get(position);
            if(sampleGerminationDatum.getConfiguredArea()!=null){
                holder.sample_confi_area.setText(sampleGerminationDatum.getConfiguredArea());
            }
            if(sampleGerminationDatum.getSpacingPtp()!=null){
                holder.sample_p_to_p_spacing.setText(sampleGerminationDatum.getSpacingPtp());
            }
            if(sampleGerminationDatum.getSpacingRtr()!=null){
                holder.sample_r_to_r_spacing.setText(sampleGerminationDatum.getSpacingRtr());
            }
            if(sampleGerminationDatum.getActualPop()!=null){
                holder.sample_actual_population.setText(sampleGerminationDatum.getActualPop());
                actualPop=Double.valueOf(sampleGerminationDatum.getActualPop());
            }
            if(sampleGerminationDatum.getIdealPop()!=null){
                holder.sample_ideal_population.setText(sampleGerminationDatum.getIdealPop());
                idealPop=Double.valueOf(sampleGerminationDatum.getIdealPop());
            }
            if(sampleGerminationDatum.getActualTotalPopulation()!=null){
                holder.sample_total_popu.setText(sampleGerminationDatum.getActualTotalPopulation());
            }
            if(sampleGerminationDatum.getGermination()!=null){
                holder.sample_germination.setText(String.valueOf(sampleGerminationDatum.getGermination()));
            }
            holder.sample_title.setText(resources.getString(R.string.sample_label)+" "+(position+1));

            if (SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.OFFLINE_MODE)){
                double germination = actualPop / idealPop * 100.0;
                holder.sample_germination.setText(String.valueOf(String.format("%.2f", germination)));
            }

        }

        @Override
        public void onBindViewHolder(SampleGermiAdapter.ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        public int getItemCount() {
            return sampleGerminationData.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.sample_confi_area)
            TextView sample_confi_area;
            @BindView(R.id.sample_p_to_p_spacing)
            TextView sample_p_to_p_spacing;
            @BindView(R.id.sample_r_to_r_spacing)
            TextView sample_r_to_r_spacing;
            @BindView(R.id.sample_ideal_population)
            TextView sample_ideal_population;
            @BindView(R.id.sample_actual_population)
            TextView sample_actual_population;
            @BindView(R.id.sample_germination)
            TextView sample_germination;
            @BindView(R.id.sample_total_popu)
            TextView sample_total_popu;
            @BindView(R.id.sample_title)
            TextView sample_title;
            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this,v);

            }
        }

    }


