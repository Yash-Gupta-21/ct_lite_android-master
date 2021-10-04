package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;

public class FPOCrop {
    Season selectedSeason;
    DDNew selectedCrop;
    int cropPosition = 0;
    @SerializedName("season_id")
    String seasonId;
    @SerializedName("crop_id")
    String cropId;
    @SerializedName("farm_area")
    String farmArea="";
    @SerializedName("datatype")
    String expectedOrActual="";

    @SerializedName("profit_loss")
    String profitLoss="";
    @SerializedName("satisfactory")
    String satisfactory="";

    @SerializedName("assets")
    List<List<CropFormDatum>> cropFormDatumArrayLists=new ArrayList<>();

    /*FertilizerOrganic fertilizerOrganic;
    Harvester harvester;
    Pesticide pesticide;
    Pestilence pestilence;
    ProductSale productSale;
    Seed seed;
    Tractor tractor;
    Worker worker;*/
    @SerializedName("farm_add_info_json")
    FormModel formModel;

    /*public FPOCrop(FertilizerOrganic fertilizerOrganic, Harvester harvester, Pesticide pesticide, Pestilence pestilence, ProductSale productSale, Seed seed, Tractor tractor, Worker worker) {
        this.fertilizerOrganic = fertilizerOrganic;
        this.harvester = harvester;
        this.pesticide = pesticide;
        this.pestilence = pestilence;
        this.productSale = productSale;
        this.seed = seed;
        this.tractor = tractor;
        this.worker = worker;
        this.formModel = new FormModel(fertilizerOrganic, harvester, pesticide, pestilence, productSale, seed, tractor, worker);
    }*/

    public FPOCrop(FormModel formModel) {

        this.formModel = formModel;
    }
    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getFarmArea() {
        return farmArea;
    }

    public void setFarmArea(String farmArea) {
        this.farmArea = farmArea;
    }

    public String getExpectedOrActual() {
        return expectedOrActual;
    }

    public void setExpectedOrActual(String expectedOrActual) {
        this.expectedOrActual = expectedOrActual;
    }

    /*public FertilizerOrganic getFertilizerOrganic() {
        return fertilizerOrganic;
    }

    public void setFertilizerOrganic(FertilizerOrganic fertilizerOrganic) {
        this.fertilizerOrganic = fertilizerOrganic;
    }

    public Harvester getHarvester() {
        return harvester;
    }

    public void setHarvester(Harvester harvester) {
        this.harvester = harvester;
    }

    public Pesticide getPesticide() {
        return pesticide;
    }

    public void setPesticide(Pesticide pesticide) {
        this.pesticide = pesticide;
    }

    public Pestilence getPestilence() {
        return pestilence;
    }

    public void setPestilence(Pestilence pestilence) {
        this.pestilence = pestilence;
    }

    public ProductSale getProductSale() {
        return productSale;
    }

    public void setProductSale(ProductSale productSale) {
        this.productSale = productSale;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Tractor getTractor() {
        return tractor;
    }

    public void setTractor(Tractor tractor) {
        this.tractor = tractor;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }*/

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getSatisfactory() {
        return satisfactory;
    }

    public void setSatisfactory(String satisfactory) {
        this.satisfactory = satisfactory;
    }

    public int getCropPosition() {
        return cropPosition;
    }

    public void setCropPosition(int cropPosition) {
        this.cropPosition = cropPosition;
    }

    public FormModel getFormModel() {
        return formModel;
    }

    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

    public List<List<CropFormDatum>> getCropFormDatumArrayLists() {
        return cropFormDatumArrayLists;
    }

    public void setCropFormDatumArrayLists(List<List<CropFormDatum>> cropFormDatumArrayLists) {
        this.cropFormDatumArrayLists = cropFormDatumArrayLists;
    }

    public Season getSelectedSeason() {
        return selectedSeason;
    }

    public void setSelectedSeason(Season selectedSeason) {
        this.selectedSeason = selectedSeason;
    }

    public DDNew getSelectedCrop() {
        return selectedCrop;
    }

    public void setSelectedCrop(DDNew selectedCrop) {
        this.selectedCrop = selectedCrop;
    }
}
