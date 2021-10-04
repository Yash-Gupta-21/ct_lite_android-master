package com.i9930.croptrails.Maps.WalkAround;

import com.google.gson.annotations.SerializedName;

public class LocationData {

    double latitude,longitude;
    float[] gravity;
    float[] gyroscop;
    double step;
    double distance;
    float direction;
    String speed;
    float accuracy;
    /*@SerializedName("stepTimestamp")
    long stepTimestampToStore;*/
    @SerializedName("stepTimestamp")
    String stepTimestampToStoreR;

    public LocationData(double latitude, double longitude, float[] gravity, double step, double distance, float direction,String speed,float[] gyroscop,long stepTimestampToStore,String stepTimestampToStoreR,float accuracy) {
        this.latitude = latitude;
        this.accuracy=accuracy;
        this.gyroscop=gyroscop;
//        this.stepTimestampToStore=stepTimestampToStore;
        this.longitude = longitude;
        this.stepTimestampToStoreR=stepTimestampToStoreR;
        this.speed=speed;
        this.gravity = gravity;
        this.step = step;
        this.distance = distance;
        this.direction = direction;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getStepTimestampToStoreR() {
        return stepTimestampToStoreR;
    }

    public void setStepTimestampToStoreR(String stepTimestampToStoreR) {
        this.stepTimestampToStoreR = stepTimestampToStoreR;
    }

    /*public long getStepTimestampToStore() {
        return stepTimestampToStore;
    }

    public void setStepTimestampToStore(long stepTimestampToStore) {
        this.stepTimestampToStore = stepTimestampToStore;
    }*/

    public float[] getGyroscop() {
        return gyroscop;
    }

    public void setGyroscop(float[] gyroscop) {
        this.gyroscop = gyroscop;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float[] getGravity() {
        return gravity;
    }

    public void setGravity(float[] gravity) {
        this.gravity = gravity;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}
