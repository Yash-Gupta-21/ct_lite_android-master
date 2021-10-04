package com.i9930.croptrails.AddFarm;

public class Share{
        String certificate;
        String howManyCert;
        String howMuchValue;

        public Share(String certificate, String howManyCert, String howMuchValue) {
            this.certificate = certificate;
            this.howManyCert = howManyCert;
            this.howMuchValue = howMuchValue;
        }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getHowManyCert() {
        return howManyCert;
    }

    public void setHowManyCert(String howManyCert) {
        this.howManyCert = howManyCert;
    }

    public String getHowMuchValue() {
        return howMuchValue;
    }

    public void setHowMuchValue(String howMuchValue) {
        this.howMuchValue = howMuchValue;
    }
}