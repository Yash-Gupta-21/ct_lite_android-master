package com.i9930.croptrails.AddFarm.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FormModel;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCity;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCountry;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDDValue;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterSeason;
import com.i9930.croptrails.AddFarm.SpinnerAdapterState;
import com.i9930.croptrails.CommonClasses.Address.CityDatum;
import com.i9930.croptrails.CommonClasses.Address.CountryDatum;
import com.i9930.croptrails.CommonClasses.Address.StateDatum;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.HarvestSubmit.Interfaces.OnFarmDatasetChanged;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class AddFarmAdapter2 extends RecyclerView.Adapter<AddFarmAdapter2.MyViewHolder> {
    private List<FarmData> farmList;
    Context context;
    List<Integer> positionList = new ArrayList<>();
    List<DDNew> hpMotorList = new ArrayList<>();
    List<AddFarmAdapter2.MyViewHolder> myViewHolderList = new ArrayList<>();
    Resources resources;
    public interface OnImagePickClickListener{
        public void onImageClicked(int index,ImageType imageType);
    }

    OnImagePickClickListener imagePickClickListener;
    OnFarmDatasetChanged listener;
    boolean hideAddl2 = false;
    boolean isPreviousCropMandatory=false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextInputLayout tiAddl2;
        EditText ti_et_farmid, ti_et_farmname, ti_et_farmarea, etAddressLine1, etTransplantingData,etFarmImage,etOwnershipImage;
        EditText  villageOrCity, mandlTehasilEt, districtEt, stateEt, countryEt;
        AutoCompleteTextView etAddressLine2;
        SearchableSpinner seasonSpinner,motorHPSpinner;
        SearchableSpinner cropSpinner,cropSpinnerPrevious, irrigationSourceSpinner, irrigationTypeSpinner, soilTypeSpinner, spinnerCroppingPattern, spinnerPlantingMethod, spinnerLandCategory;
        ImageView deleteFarm;
        AutoCompleteTextView et_previous_crop, et_current_crop;
        TextView moreDetailsFarmBtn;
        LinearLayout extraFarmDetailsLayout,isOwnLayout,farmDetailsLayoutN;
        RecyclerView recyclerView;
        TextView addFpoDetails;
        RadioButton radioOwnYes, radioOwnNo;
        RecyclerView recyclerViewAssets;
        LinearLayout layout;
        SearchableSpinner countrySpinner, stateSpinner, districtSpinner;

//        AutoCompleteTextView etAHName,etAHQuantity,etAHFoder,etAHHousing,etAHPrice,etAHBreed;
//        AutoCompleteTextView etEQName,etEQQuantity,etEQType,etEQPrice,etEQOperationalCost,etEQOwnership;

        TextInputLayout tiTransplant;
        int ref;

        public MyViewHolder(View v) {
            super(v);
//            etAHName=v.findViewById(R.id.etAHName);
//            etAHQuantity=v.findViewById(R.id.etAHQuantity);
//            etAHFoder=v.findViewById(R.id.etAHFoder);
//            etAHHousing=v.findViewById(R.id.etAHHousing);
//            etAHPrice=v.findViewById(R.id.etAHPrice);
//            etAHBreed=v.findViewById(R.id.etAHBreed);
//
//            etEQName=v.findViewById(R.id.etEQName);
//            etEQQuantity=v.findViewById(R.id.etEQQuantity);
//            etEQType=v.findViewById(R.id.etEQType);
//            etEQPrice=v.findViewById(R.id.etEQPrice);
//            etEQOperationalCost=v.findViewById(R.id.etEQOperationalCost);
//            etEQOwnership=v.findViewById(R.id.etEQOwnership);


            farmDetailsLayoutN = v.findViewById(R.id.farmDetailsLayoutN);
            isOwnLayout = v.findViewById(R.id.isOwnLayout);
            motorHPSpinner = v.findViewById(R.id.motorHPSpinner);
            etFarmImage = v.findViewById(R.id.etFarmImage);
            etOwnershipImage = v.findViewById(R.id.etOwnershipImage);

            tiAddl2 = v.findViewById(R.id.tiAddl2);
            tiTransplant = v.findViewById(R.id.tiTransplant);
            layout = v.findViewById(R.id.layout);
            countrySpinner = v.findViewById(R.id.countrySpinner);
            stateSpinner = v.findViewById(R.id.stateSpinner);
            districtSpinner = v.findViewById(R.id.districtSpinner);
            //--------------------------------
            recyclerViewAssets = v.findViewById(R.id.recyclerViewAssets);
            etTransplantingData = v.findViewById(R.id.etTransplantingData);
            recyclerView = v.findViewById(R.id.recyclerView);
            addFpoDetails = v.findViewById(R.id.addFpoDetails);
            ti_et_farmid = v.findViewById(R.id.ti_et_farmid);
            ti_et_farmname = v.findViewById(R.id.ti_et_farmname);
            ti_et_farmarea = v.findViewById(R.id.ti_et_farmarea);
            etAddressLine1 = v.findViewById(R.id.etAddressLine1);
            seasonSpinner = v.findViewById(R.id.seasonSpinner);
            cropSpinner = v.findViewById(R.id.cropSpinner);
            cropSpinnerPrevious=v.findViewById(R.id.cropSpinnerPrevious);
            etAddressLine2 = v.findViewById(R.id.etAddressLine2);
            villageOrCity = v.findViewById(R.id.villageOrCity);
            mandlTehasilEt = v.findViewById(R.id.mandlTehasilEt);
            districtEt = v.findViewById(R.id.districtEt);
            stateEt = v.findViewById(R.id.stateEt);
            countryEt = v.findViewById(R.id.countryEt);
            et_previous_crop = v.findViewById(R.id.ti_et_previous_crop);
            irrigationSourceSpinner = v.findViewById(R.id.irrigationSourceSpinner);
            irrigationTypeSpinner = v.findViewById(R.id.irrigationTypeSpinner);
            soilTypeSpinner = v.findViewById(R.id.soilTypeSpinner);
            deleteFarm = v.findViewById(R.id.deleteFarm);
            et_current_crop = v.findViewById(R.id.et_current_crop);
            moreDetailsFarmBtn = v.findViewById(R.id.moreDetailsFarmBtn);
            extraFarmDetailsLayout = v.findViewById(R.id.extraFarmDetailsLayout);

            radioOwnNo = v.findViewById(R.id.radioOwnNo);
            radioOwnYes = v.findViewById(R.id.radioOwnYes);
            spinnerCroppingPattern = v.findViewById(R.id.spinnerCroppingPattern);
            spinnerPlantingMethod = v.findViewById(R.id.spinnerPlantingMethod);
            spinnerLandCategory = v.findViewById(R.id.spinnerLandCategory);
        }
    }

    private List<DDNew> irrigationTypeDDList = new ArrayList<>();
    private List<DDNew> irrigationSourceDDList = new ArrayList<>();
    private List<DDNew> soilTypeDDList = new ArrayList<>();
    private List<Season> seasonDDList = new ArrayList<>();
    private List<DDNew> farmCropList = new ArrayList<>();

    private List<DDNew> plantingMethodDDList = new ArrayList<>();
    private List<DDNew> isIrrigatedDDList = new ArrayList<>();
    private List<DDNew> croppingPatternDDList = new ArrayList<>();

    List<CropFormDatum> cropFormDatumList;
    List<CropFormDatum> cropFormDatumListSuper;
    ArrayList<CountryDatum> countryDatumList;
    boolean isCrop = false;
    boolean isFarmAddress = false;
    int countryIndex = 0;

    public void setCountryIndex(int countryIndex) {
        try {
            this.countryIndex = countryIndex;
            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    public AddFarmAdapter2(Context context, List<FarmData> FarmList, List<DDNew> irrigationTypeDDList,
                           List<DDNew> irrigationSourceDDList,
                           List<DDNew> soilTypeDDList, List<Season> seasonDDList, List<DDNew> farmCropList,
                           List<DDNew> plantingMethodDDList, List<DDNew> isIrrigatedDDList, List<DDNew> croppingPatternDDList,
                           List<CropFormDatum> cropFormDatumList, List<CropFormDatum> cropFormDatumListSuper,
                           ArrayList<CountryDatum> countryDatumList,
                           OnFarmDatasetChanged listener) {

        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                Log.e("CompRegCacheManager", AppConstants.COMP_REG.FARM_CROP + " " + new Gson().toJson(compRegModel));
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                isCrop = true;
                            }
                        } else {
                        }
                    } catch (Exception e) {
                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    isCrop = true;
                }
            }
        }, AppConstants.COMP_REG.FARM_CROP);
//        isCrop = true;
        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                Log.e("CompRegCacheManager", AppConstants.COMP_REG.FARM_DEMOGRAPHICS + " " + new Gson().toJson(compRegModel));
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                isFarmAddress = true;
                            }
                        } else {
                        }
                    } catch (Exception e) {
                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    isFarmAddress = true;
                }
            }
        }, AppConstants.COMP_REG.FARM_DEMOGRAPHICS);
//        isFarmAddress=true;
        this.farmList = FarmList;
        this.context = context;
        this.countryDatumList = countryDatumList;
        this.cropFormDatumList = cropFormDatumList;
        this.cropFormDatumListSuper = cropFormDatumListSuper;
        this.plantingMethodDDList = plantingMethodDDList;
        this.isIrrigatedDDList = isIrrigatedDDList;
        this.croppingPatternDDList = croppingPatternDDList;

        this.irrigationTypeDDList = irrigationTypeDDList;
        this.irrigationSourceDDList = irrigationSourceDDList;
        this.soilTypeDDList = soilTypeDDList;
        this.seasonDDList = seasonDDList;
        this.farmCropList = farmCropList;

        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        this.listener = listener;
        isPreviousCropMandatory=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY);

    }

    @Override
    public AddFarmAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_detail_add, parent, false);
        AddFarmAdapter2.MyViewHolder vh = new AddFarmAdapter2.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AddFarmAdapter2.MyViewHolder holder, final int position) {
        holder.ref = position;


        if (hpMotorList!=null&&hpMotorList.size()>0){
            if (farmList.get(position).getMotorHpList()==null||farmList.get(position).getMotorHpList().size()==0){
                farmList.get(position).setMotorHpList(hpMotorList);
            }
        }
        if (AppConstants.isValidString(farmList.get(position).getOwnerShipPath()))
            holder.etOwnershipImage.setText(farmList.get(position).getOwnerShipPath());

        if (AppConstants.isValidString(farmList.get(position).getFarmImagePath()))
            holder.etFarmImage.setText(farmList.get(position).getFarmImagePath());
        if (listener!=null){

            holder.etOwnershipImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOwnerShipImageClick(holder.ref,holder.etOwnershipImage);
                }
            });

            holder.etFarmImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFarmImageClick(holder.ref,holder.etFarmImage);
                }
            });
        }

        if (isPreviousCropMandatory)
            holder.cropSpinnerPrevious.setVisibility(View.VISIBLE);
        else
            holder.cropSpinnerPrevious.setVisibility(View.GONE);
        holder.et_previous_crop.setVisibility(View.GONE);

        if (hideAddl2) {
            holder.tiAddl2.setVisibility(View.GONE);
        } else
            holder.tiAddl2.setVisibility(View.VISIBLE);
        if (isFarmAddress) {
            holder.layout.setVisibility(View.VISIBLE);
        } else {
            holder.layout.setVisibility(View.GONE);
        }

        if (isCrop) {
            holder.addFpoDetails.setVisibility(View.VISIBLE);
        } else {
            holder.addFpoDetails.setVisibility(View.GONE);
        }

        AddFarmCropAdapter2 addFarmCropAdapter = new AddFarmCropAdapter2(context,
                farmList.get(position).getFpoCropList(), farmCropList,
                seasonDDList, new AddFarmCropAdapter2.OnLastItemremoved() {
            @Override
            public void onLastItem(boolean isLast, int position) {
                if (isLast) {
                    holder.addFpoDetails.setText(resources.getText(R.string.add_crop_details));
                } else {
                    holder.addFpoDetails.setText(resources.getText(R.string.add_more_crop_label));
                }
            }
            @Override
            public void onDataSetChanged(List<FPOCrop> fpoCropList) {
                listener.onDataSetChanged(farmList);
            }
        });

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setNestedScrollingEnabled(false);
        holder.recyclerView.setAdapter(addFarmCropAdapter);
        holder.addFpoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormModel formModel = new FormModel();
                FPOCrop fpoCrop = new FPOCrop(formModel);
                fpoCrop.setCropFormDatumArrayLists(getListData());
                farmList.get(position).getFpoCropList().add(fpoCrop);
                addFarmCropAdapter.notifyDataSetChanged();
                holder.addFpoDetails.setText(resources.getText(R.string.add_more_crop_label));
                listener.onDataSetChanged(farmList);
            }
        });

        holder.etTransplantingData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransplantDate(position, holder.etTransplantingData);
            }
        });

        holder.radioOwnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                farmList.get(holder.ref).setIsOwned("Y");
                listener.onDataSetChanged(farmList);
            }
        });
        holder.radioOwnNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                farmList.get(holder.ref).setIsOwned("N");
                listener.onDataSetChanged(farmList);
            }
        });

        holder.etAddressLine2.setTag(holder);
        holder.villageOrCity.setTag(holder);
        holder.mandlTehasilEt.setTag(holder);
        holder.districtEt.setTag(holder);
        holder.stateEt.setTag(holder);
        holder.countryEt.setTag(holder);
        holder.cropSpinner.setTag(holder);
        holder.seasonSpinner.setTag(holder);
        holder.irrigationSourceSpinner.setTag(holder);
        holder.irrigationTypeSpinner.setTag(holder);
        holder.soilTypeSpinner.setTag(holder);
        holder.et_previous_crop.setTag(holder);
        holder.et_current_crop.setTag(holder);
        holder.ti_et_farmid.setTag(holder);
        holder.ti_et_farmarea.setTag(holder);

        farmList.get(holder.ref).setCluster(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        farmList.get(holder.ref).setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        holder.deleteFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                farmList.remove(holder.ref);
                myViewHolderList.remove(holder.ref);
                positionList.remove(holder.ref);
                notifyDataSetChanged();
                listener.onDataSetChanged(farmList);
                if (farmList.size() == 0) {
                    listener.isLastItemRemoved(true, holder.ref);
                }
            }
        });
        if (positionList.contains(position)) {
            Log.e("Adapter", "Item at " + position + " Already added");
        } else {
            Log.e("Adapter", "Item at " + position + " Adding");
            positionList.add(holder.ref);
            myViewHolderList.add(holder);
        }

        if (farmList.get(holder.ref).getId() != null) {
            holder.ti_et_farmid.setText(farmList.get(holder.ref).getId());
        } else {
            holder.ti_et_farmid.setText(null);

        }
        if (farmList.get(holder.ref).getName() != null) {
            holder.ti_et_farmname.setText(farmList.get(holder.ref).getName());

        } else {
            holder.ti_et_farmname.setText(null);

        }
        if (farmList.get(holder.ref).getAreaS() != null) {
            holder.ti_et_farmarea.setText(farmList.get(holder.ref).getAreaS());
        } else {
            holder.ti_et_farmarea.setText(null);

        }

        if (farmList.get(holder.ref).getAddL1() != null && !farmList.get(holder.ref).getAddL1().trim().equals("0")) {
            holder.etAddressLine1.setText(farmList.get(holder.ref).getAddL1());
        } else {
            holder.etAddressLine1.setText(null);

        }
        if (farmList.get(holder.ref).getAddL2() != null && !farmList.get(holder.ref).getAddL2().trim().equals("0")) {
            holder.etAddressLine2.setText(farmList.get(holder.ref).getAddL2());
        } else {
            holder.etAddressLine2.setText(null);

        }
        if (farmList.get(holder.ref).getVillageOrCity() != null && !farmList.get(holder.ref).getVillageOrCity().trim().equals("0")) {
            holder.villageOrCity.setText(farmList.get(holder.ref).getVillageOrCity());
        } else {
            holder.villageOrCity.setText(null);

        }
        if (farmList.get(holder.ref).getMandalOrTehsil() != null && !farmList.get(holder.ref).getMandalOrTehsil().trim().equals("0")) {
            holder.mandlTehasilEt.setText(farmList.get(holder.ref).getMandalOrTehsil());
        } else {
            holder.mandlTehasilEt.setText(null);

        }
        if (farmList.get(holder.ref).getDistrict() != null && !farmList.get(holder.ref).getDistrict().trim().equals("0")) {
            holder.districtEt.setText(farmList.get(holder.ref).getDistrict());
        } else {
            holder.districtEt.setText(null);

        }
        if (farmList.get(holder.ref).getState() != null && !farmList.get(holder.ref).getState().trim().equals("0")) {
            holder.stateEt.setText(farmList.get(holder.ref).getState());
        } else {
            holder.stateEt.setText(null);

        }
        if (farmList.get(holder.ref).getCountry() != null && !farmList.get(holder.ref).getCountry().trim().equals("0")) {
            holder.countryEt.setText(farmList.get(holder.ref).getCountry());
        } else {
            holder.countryEt.setText(null);

        }

        holder.ti_et_farmid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setId(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.ti_et_farmname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setName(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.ti_et_farmarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence!=null&&charSequence.length() > 0) {
                    farmList.get(holder.ref).setArea(charSequence.toString().trim());
                    farmList.get(holder.ref).setAreaS(charSequence.toString().trim());
                    listener.onDataSetChanged(farmList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.etAddressLine1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setAddL1(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.etAddressLine2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setAddL2(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.villageOrCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setVillageOrCity(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.mandlTehasilEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setMandalOrTehsil(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.districtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setDistrict(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.stateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setState(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.countryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setCountry(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.et_previous_crop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setPreviouscrop(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.et_current_crop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                farmList.get(holder.ref).setCurrentCropName(charSequence.toString());
                listener.onDataSetChanged(farmList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (farmList.get(position).getCityDatumList() == null || farmList.get(position).getCityDatumList().size() == 0) {
            farmList.get(position).getCityDatumList().add(new CityDatum("", resources.getString(R.string.select_district)));
        }

        if (farmList.get(position).getStateDatumList() == null || farmList.get(position).getStateDatumList().size() == 0) {
            farmList.get(position).getStateDatumList().add(new StateDatum("", resources.getString(R.string.select_state)));
        }
        SpinnerAdapterCountry countrySpinnerAdapter = new SpinnerAdapterCountry(context, countryDatumList);
        SpinnerAdapterState stateSpinnerAdapter = new SpinnerAdapterState(context, farmList.get(position).getStateDatumList());
        SpinnerAdapterCity citySpinnerAdapter = new SpinnerAdapterCity(context, farmList.get(position).getCityDatumList());

        holder.countrySpinner.setAdapter(countrySpinnerAdapter);
        holder.stateSpinner.setAdapter(stateSpinnerAdapter);
        holder.districtSpinner.setAdapter(citySpinnerAdapter);

        try {
            if (farmList.get(position).getCountryIndex() > 0)
                holder.countrySpinner.setSelectedItem(farmList.get(position).getCountryIndex());
//            else if (AddFarm2Activity.countryIndex > 0)
//                holder.countrySpinner.setSelectedItem(AddFarm2Activity.countryIndex);
        } catch (Exception e) {
        }

        try {
            if (farmList.get(position).getStateIndex() > 0)
                holder.stateSpinner.setSelectedItem(farmList.get(position).getStateIndex());
        } catch (Exception e) {
        }

        try {
            if (farmList.get(position).getCityIndex() > 0)
                holder.districtSpinner.setSelectedItem(farmList.get(position).getCityIndex());
        } catch (Exception e) {
        }

        if (farmList.get(position).getMotorHpList()!=null&&farmList.get(position).getMotorHpList().size()>0){
            holder.motorHPSpinner.setVisibility(View.VISIBLE);

            SpinnerAdapterNewDD motorHpSpinnerAdapter= new SpinnerAdapterNewDD(context, farmList.get(position).getMotorHpList(), resources.getString(R.string.select_motor_hp));
            holder.motorHPSpinner.setAdapter(motorHpSpinnerAdapter);

            holder.motorHPSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int i, long id) {
                    try {
                        if (motorHpSpinnerAdapter.getItem(i)!=null)
                            farmList.get(position).setMotorHp(motorHpSpinnerAdapter.getItem(i).getId());
                        else
                            farmList.get(position).setMotorHp(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        farmList.get(position).setMotorHp(null);
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });

        }else {
            holder.motorHPSpinner.setVisibility(View.GONE);
        }

        holder.countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    try {
                        farmList.get(position).setCountryIndex(i);
                        farmList.get(position).setCountry(countrySpinnerAdapter.getItem(i).getName());
                        farmList.get(position).setCountryId(countrySpinnerAdapter.getItem(i).getId());
                        farmList.get(position).setDistrictId("0");
                        farmList.get(position).setStateId("0");

                        if (farmList.get(position).getStateDatumList() != null) {
                            farmList.get(position).getStateDatumList().clear();
//                            farmList.get(position).getStateDatumList().add(new StateDatum("",resources.getString(R.string.select_state)));
                            if (countrySpinnerAdapter.getItem(i).getStateDatumList() != null)
                                farmList.get(position).getStateDatumList().addAll(countrySpinnerAdapter.getItem(i).getStateDatumList());
                        }
                        if (stateSpinnerAdapter != null)
                            stateSpinnerAdapter.notifyDataSetChanged();

                        if (farmList.get(position).getCityDatumList() != null) {
                            farmList.get(position).getCityDatumList().clear();
//                            farmList.get(position).getCityDatumList().add(new CityDatum("",resources.getString(R.string.select_district)));
                        }
                        if (citySpinnerAdapter != null)
                            citySpinnerAdapter.notifyDataSetChanged();

//                        getStates(position,countryDatumList.get(i).getId(),stateSpinnerAdapter);
//                        holder.stateSpinner.setSelection(0);
//                        holder.districtSpinner.setSelection(0);

                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(position).setStateIndex(i);
                    farmList.get(position).setState(stateSpinnerAdapter.getItem(i).getName());
                    farmList.get(position).setStateId(stateSpinnerAdapter.getItem(i).getId());
                    farmList.get(position).setDistrictId("0");
                    if (farmList.get(position).getCityDatumList() != null) {
                        farmList.get(position).getCityDatumList().clear();
//                            farmList.get(position).getCityDatumList().add(new CityDatum("",resources.getString(R.string.select_district)));
                        if (stateSpinnerAdapter.getItem(i).getCities() != null)
                            farmList.get(position).getCityDatumList().addAll(stateSpinnerAdapter.getItem(i).getCities());
                    }
                    if (citySpinnerAdapter != null)
                        citySpinnerAdapter.notifyDataSetChanged();
                        /*getCity( position,farmList.get(position).getStateDatumList().get(i).getCountryId(),
                                farmList.get(position).getStateDatumList().get(i).getId(),citySpinnerAdapter);*/

//                        holder.districtSpinner.setSelection(0);

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.districtSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(position).setCityIndex(i);
                    farmList.get(position).setDistrict(citySpinnerAdapter.getItem(i).getName());
                    farmList.get(position).setDistrictId(citySpinnerAdapter.getItem(i).getId());
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        SpinnerAdapterNewDD isSpinnerAdapter2 = new SpinnerAdapterNewDD(context, irrigationSourceDDList, resources.getString(R.string.select_irri_source_prompt));
        SpinnerAdapterNewDD itSpinnerAdapter2 = new SpinnerAdapterNewDD(context, irrigationTypeDDList, resources.getString(R.string.select_irri_type_prompt));
        SpinnerAdapterNewDD stSpinnerAdapter2 = new SpinnerAdapterNewDD(context, soilTypeDDList, resources.getString(R.string.select_soil_type_prompt));
        SpinnerAdapterSeason adapter = new SpinnerAdapterSeason(context,  seasonDDList);
        SpinnerAdapterNewDD cropSpinnerAdapter = new SpinnerAdapterNewDD(context, farmCropList, resources.getString(R.string.select_crop_rompt));
        SpinnerAdapterNewDD cropSpinnerAdapterPrevious = new SpinnerAdapterNewDD(context, farmCropList, resources.getString(R.string.select_crop_previous_rompt));

        SpinnerAdapterNewDDValue croppingPattern = new SpinnerAdapterNewDDValue(context, croppingPatternDDList, resources.getString(R.string.select_cropping_pattern));
        SpinnerAdapterNewDDValue plantingMethod = new SpinnerAdapterNewDDValue(context, plantingMethodDDList, resources.getString(R.string.select_planting_method));
        SpinnerAdapterNewDDValue landCategory = new SpinnerAdapterNewDDValue(context, isIrrigatedDDList, resources.getString(R.string.select_land_category));

        holder.seasonSpinner.setAdapter(adapter);
        holder.cropSpinner.setAdapter(cropSpinnerAdapter);
        holder.cropSpinner.setSelectedItem(farmList.get(holder.ref).getCropPosition());
        holder.seasonSpinner.setSelectedItem(farmList.get(holder.ref).getSeasonPosition());
        holder.cropSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setCropId(cropSpinnerAdapter.getItem(holder.cropSpinner.getSelectedPosition()).getId());
                    farmList.get(holder.ref).setCurrentCropName(cropSpinnerAdapter.getItem(holder.cropSpinner.getSelectedPosition()).getParameters());
                    farmList.get(holder.ref).setCropPosition(holder.cropSpinner.getSelectedPosition());
                    listener.onDataSetChanged(farmList);
                    holder.et_current_crop.setText(cropSpinnerAdapter.getItem(holder.cropSpinner.getSelectedPosition()).getParameters());
                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected() {

            }
        });
        holder.cropSpinnerPrevious.setAdapter(cropSpinnerAdapterPrevious);
        holder.cropSpinnerPrevious.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
//                Toast.makeText(context, ""+i+" "+new Gson().toJson(cropSpinnerAdapter.getItem(i)), Toast.LENGTH_SHORT).show();
                try {
                    farmList.get(holder.ref).setPreviouscrop(cropSpinnerAdapterPrevious.getItem(i).getParameters());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected() {

            }
        });
        holder.irrigationSourceSpinner.setAdapter(isSpinnerAdapter2);
        holder.irrigationSourceSpinner.setSelectedItem(farmList.get(holder.ref).getIrriSourcePosition());
        holder.irrigationSourceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setIrrigationsource(isSpinnerAdapter2.getItem(i).getId());
                    farmList.get(holder.ref).setIrriSourcePosition(i);
                    farmList.get(holder.ref).setIrriSourceName(isSpinnerAdapter2.getItem(i).getParameters());
                    listener.onDataSetChanged(farmList);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.irrigationTypeSpinner.setAdapter(itSpinnerAdapter2);
        holder.irrigationTypeSpinner.setSelectedItem(farmList.get(holder.ref).getIrriTypePosition());
        holder.irrigationTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setIrrigationtype(itSpinnerAdapter2.getItem(i).getId());
                    farmList.get(holder.ref).setIrriTypename(itSpinnerAdapter2.getItem(i).getParameters());
                    farmList.get(holder.ref).setIrriTypePosition(i);
                    listener.onDataSetChanged(farmList);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.soilTypeSpinner.setAdapter(stSpinnerAdapter2);
        holder.soilTypeSpinner.setSelectedItem(farmList.get(holder.ref).getSoilTypePosition());
        holder.soilTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setSoiltype(stSpinnerAdapter2.getItem(i).getId());
                    farmList.get(holder.ref).setSoiltypeName(stSpinnerAdapter2.getItem(i).getParameters());
                    farmList.get(holder.ref).setSoilTypePosition(i);
                    listener.onDataSetChanged(farmList);
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.spinnerPlantingMethod.setAdapter(plantingMethod);
        holder.spinnerPlantingMethod.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setPlanting_method(plantingMethod.getItem(i).getParameters());
                    listener.onDataSetChanged(farmList);

                    if (plantingMethod.getItem(i).getValue() != null && plantingMethod.getItem(i).getValue().toLowerCase().contains("transpla")) {
                        holder.tiTransplant.setVisibility(View.VISIBLE);
                    }else {
                        holder.tiTransplant.setVisibility(View.GONE);
                    }
//                        Log.e("spinnerPlantingMethod",plantingMethodDDList.get(i).getParameters());
//                        Log.e("spinnerPlantingMethod",plantingMethodDDList.get(i).getValue());


                } catch (Exception e) {
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.spinnerLandCategory.setAdapter(landCategory);
        holder.spinnerLandCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setIs_irrigated(landCategory.getItem(i).getParameters());
                    listener.onDataSetChanged(farmList);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.spinnerCroppingPattern.setAdapter(croppingPattern);
        holder.spinnerCroppingPattern.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    farmList.get(holder.ref).setCropping_pattern(croppingPattern.getItem(i).getParameters());
                    listener.onDataSetChanged(farmList);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        holder.seasonSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    Season season=adapter.getItem(i);
                    if (season!=null) {
                        farmList.get(holder.ref).setSeason(season.getSeasonNum());
                        farmList.get(holder.ref).setSeasonPosition(i);
                        listener.onDataSetChanged(farmList);
                    }else {
                        farmList.get(holder.ref).setSeason("0");
                        farmList.get(holder.ref).setSeasonPosition(i);
                        listener.onDataSetChanged(farmList);
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

//--------------------------------------

        if (farmList.get(position).isFullDetailClicked()){

        }else {

        }
        holder.moreDetailsFarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate( position,holder);
//                holder.cropSpinnerPrevious.setVisibility(View.VISIBLE);
            }
        });


        //----------------------
        if (cropFormDatumListSuper != null && cropFormDatumListSuper.size() > 0) {
            farmList.get(position).setAssetsList(getListDataSuper());
            CropFormRvAdapterOuter cropFormRvAdapter = new CropFormRvAdapterOuter(context, farmList.get(position).getAssetsList());
            holder.recyclerViewAssets.setHasFixedSize(true);
            holder.recyclerViewAssets.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerViewAssets.setAdapter(cropFormRvAdapter);
        }
        //----------------------
        try {
            holder.setIsRecyclable(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Calendar myCalendarsowing = Calendar.getInstance();

    private void addTransplantDate(int position, EditText etTransplantingData) {
        try {


            final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendarsowing.set(Calendar.YEAR, year);
                    myCalendarsowing.set(Calendar.MONTH, monthOfYear);
                    myCalendarsowing.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String time = sdf.format(myCalendarsowing.getTime());
                    etTransplantingData.setText(AppConstants.getShowableDate(time,context));

                    farmList.get(position).setTransplant_date(AppConstants.getUploadableDate(etTransplantingData.getText().toString().trim(),context));
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, myCalendarsowing
                    .get(Calendar.YEAR), myCalendarsowing.get(Calendar.MONTH),
                    myCalendarsowing.get(Calendar.DAY_OF_MONTH));
//            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return farmList.size();
    }


    public void add(FarmData farmData) {
        farmList.add(farmData);
        notifyItemInserted(farmList.size());
        notifyDataSetChanged();

    }

    public void notifyData(List<FarmData> farmData) {
        this.farmList = farmData;
        notifyDataSetChanged();
    }


    public void setErrorAndFocus(int position, String from, String msg) {
        AddFarmAdapter2.MyViewHolder holder = myViewHolderList.get(position);
        if (from.equals("id")) {
            holder.ti_et_farmid.getParent().requestChildFocus(holder.ti_et_farmid, holder.ti_et_farmid);
            holder.ti_et_farmid.setError(msg);
        } else if (from.equals("area")) {
            holder.ti_et_farmarea.getParent().requestChildFocus(holder.ti_et_farmarea, holder.ti_et_farmarea);
            holder.ti_et_farmarea.setError(msg);
        } else if (from.equals("season")) {
            holder.seasonSpinner.getParent().requestChildFocus(holder.seasonSpinner, holder.seasonSpinner);
        } else if (from.equals("crop")) {
            holder.cropSpinner.getParent().requestChildFocus(holder.cropSpinner, holder.cropSpinner);
        }
        else if (from.equals("cropP")) {
            try {
                holder.cropSpinnerPrevious.getParent().requestChildFocus(holder.cropSpinnerPrevious, holder.cropSpinnerPrevious);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void animate(int index, MyViewHolder holder) {


        if (!farmList.get(index).isFullDetailClicked()) {

            farmList.get(index).setFullDetailClicked(true);

            holder.extraFarmDetailsLayout.setVisibility(View.VISIBLE);
            holder.cropSpinnerPrevious.setVisibility(View.VISIBLE);
            holder.spinnerPlantingMethod.setVisibility(View.VISIBLE);
            holder.spinnerCroppingPattern.setVisibility(View.VISIBLE);
            holder.tiTransplant.setVisibility(View.VISIBLE);

            holder.irrigationSourceSpinner.setVisibility(View.VISIBLE);
            holder.irrigationTypeSpinner.setVisibility(View.VISIBLE);
            holder.soilTypeSpinner.setVisibility(View.VISIBLE);
            holder.motorHPSpinner.setVisibility(View.VISIBLE);
            holder.spinnerLandCategory.setVisibility(View.VISIBLE);
            holder.isOwnLayout.setVisibility(View.VISIBLE);
            holder.etFarmImage.setVisibility(View.VISIBLE);
            holder.etOwnershipImage.setVisibility(View.VISIBLE);
            holder.farmDetailsLayoutN.setVisibility(View.VISIBLE);
            holder.moreDetailsFarmBtn.setText(resources.getString(R.string.remove_details_label));

        } else {
//            if (!isPreviousCropMandatory)
//            cropSpinnerPrevious.setVisibility(View.GONE);
            farmList.get(index).setFullDetailClicked(false);

            holder.extraFarmDetailsLayout.setVisibility(View.GONE);
            holder.cropSpinnerPrevious.setVisibility(View.GONE);
            holder.spinnerPlantingMethod.setVisibility(View.GONE);
            holder.spinnerCroppingPattern.setVisibility(View.GONE);
            holder.tiTransplant.setVisibility(View.GONE);

//            holder.irrigationSourceSpinner.setVisibility(View.GONE);
//            holder.irrigationTypeSpinner.setVisibility(View.GONE);
//            holder.soilTypeSpinner.setVisibility(View.GONE);
//            holder.motorHPSpinner.setVisibility(View.GONE);
//            holder.spinnerLandCategory.setVisibility(View.GONE);
//            holder.isOwnLayout.setVisibility(View.GONE);
//            holder.etFarmImage.setVisibility(View.GONE);
            holder.farmDetailsLayoutN.setVisibility(View.GONE);
            holder.moreDetailsFarmBtn.setText(resources.getString(R.string.add_more_details_label));
        }

    }

    public List<CropFormDatum> getCropFormDatumList() {
        return cropFormDatumList;
    }

    public void setCropFormDatumList(List<CropFormDatum> cropFormDatumList) {
        try {
            this.cropFormDatumList = cropFormDatumList;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CropFormDatum> getCropFormDatumListSuper() {
        return cropFormDatumListSuper;
    }

    public void setCropFormDatumListSuper(List<CropFormDatum> cropFormDatumListSuper) {
        try {
            this.cropFormDatumListSuper = cropFormDatumListSuper;
            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    private List<List<CropFormDatum>> getListData() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumList.size(); i++) {

            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumList.get(i)));
            lists.add(list);
        }
        return lists;
    }

    private List<List<CropFormDatum>> getListDataSuper() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumListSuper.size(); i++) {
            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumListSuper.get(i)));
            lists.add(list);
        }
        return lists;
    }

    public List<DDNew> getIrrigationTypeDDList() {
        return irrigationTypeDDList;
    }

    public void setIrrigationTypeDDList(List<DDNew> irrigationTypeDDList) {
        this.irrigationTypeDDList = irrigationTypeDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getIrrigationSourceDDList() {
        return irrigationSourceDDList;
    }

    public void setIrrigationSourceDDList(List<DDNew> irrigationSourceDDList) {
        this.irrigationSourceDDList = irrigationSourceDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getSoilTypeDDList() {
        return soilTypeDDList;
    }

    public void setSoilTypeDDList(List<DDNew> soilTypeDDList) {
        this.soilTypeDDList = soilTypeDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getFarmCropList() {
        return farmCropList;
    }

    public void setFarmCropList(List<DDNew> farmCropList) {
        this.farmCropList = farmCropList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getPlantingMethodDDList() {
        return plantingMethodDDList;
    }

    public void setPlantingMethodDDList(List<DDNew> plantingMethodDDList) {
        this.plantingMethodDDList = plantingMethodDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getIsIrrigatedDDList() {
        return isIrrigatedDDList;
    }

    public void setIsIrrigatedDDList(List<DDNew> isIrrigatedDDList) {
        this.isIrrigatedDDList = isIrrigatedDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<DDNew> getCroppingPatternDDList() {
        return croppingPatternDDList;
    }

    public void setCroppingPatternDDList(List<DDNew> croppingPatternDDList) {
        this.croppingPatternDDList = croppingPatternDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public List<Season> getSeasonDDList() {
        return seasonDDList;
    }

    public void setSeasonDDList(List<Season> seasonDDList) {
        this.seasonDDList = seasonDDList;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public boolean isHideAddl2() {
        return hideAddl2;
    }

    public void setHideAddl2(boolean hideAddl2) {
        try {
            this.hideAddl2 = hideAddl2;
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }


    public List<DDNew> getHpMotorList() {
        return hpMotorList;
    }

    public void setHpMotorList(List<DDNew> hpMotorList) {
        try {
            this.hpMotorList = hpMotorList;
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public OnImagePickClickListener getImagePickClickListener() {
        return imagePickClickListener;
    }

    public void setImagePickClickListener(OnImagePickClickListener imagePickClickListener) {
        this.imagePickClickListener = imagePickClickListener;
    }
}

