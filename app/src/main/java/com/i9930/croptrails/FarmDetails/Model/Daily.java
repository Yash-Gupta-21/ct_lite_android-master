package com.i9930.croptrails.FarmDetails.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Daily implements Serializable {

@SerializedName("summary")
@Expose
private String summary;
@SerializedName("icon")
@Expose
private String icon;
@SerializedName("data")
@Expose
private List<WeatherDatum_> data = null;

public String getSummary() {
return summary;
}

public void setSummary(String summary) {
this.summary = summary;
}

public String getIcon() {
return icon;
}

public void setIcon(String icon) {
this.icon = icon;
}

public List<WeatherDatum_> getData() {
return data;
}

public void setData(List<WeatherDatum_> data) {
this.data = data;
}

}