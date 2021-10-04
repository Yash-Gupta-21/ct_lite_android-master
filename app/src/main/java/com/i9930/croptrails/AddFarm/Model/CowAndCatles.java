package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.SerializedName;


public class CowAndCatles {
    String animalCount;
    @SerializedName("milkLitresPerDay")
    String milkLitersPerDay;
    String milkSalePrice;
    String isOtherAnimal;

    public CowAndCatles(String animalCount, String milkLittersPerDay, String milkSalePrice, String isOtherAnimal) {
        this.animalCount = animalCount;
        this.milkLitersPerDay = milkLittersPerDay;
        this.milkSalePrice = milkSalePrice;
        this.isOtherAnimal = isOtherAnimal;
    }

    public String getAnimalCount() {
        return animalCount;
    }

    public void setAnimalCount(String animalCount) {
        this.animalCount = animalCount;
    }

    public String getMilkLittersPerDay() {
        return milkLitersPerDay;
    }

    public void setMilkLittersPerDay(String milkLittersPerDay) {
        this.milkLitersPerDay = milkLittersPerDay;
    }

    public String getMilkSalePrice() {
        return milkSalePrice;
    }

    public void setMilkSalePrice(String milkSalePrice) {
        this.milkSalePrice = milkSalePrice;
    }

    public String getIsOtherAnimal() {
        return isOtherAnimal;
    }

    public void setIsOtherAnimal(String isOtherAnimal) {
        this.isOtherAnimal = isOtherAnimal;
    }
}
