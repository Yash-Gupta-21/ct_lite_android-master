package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardData {

        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("grade")
        @Expose
        private String grade;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

}

