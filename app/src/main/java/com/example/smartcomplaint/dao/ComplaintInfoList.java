package com.example.smartcomplaint.dao;

import java.util.ArrayList;

/**
 * Created by Rakshith on 10/13/2015.
 */
public class ComplaintInfoList {

    public ArrayList<ComplaintInfo> getComplaints() {
        return complaints;
    }

    public void setComplaints(ArrayList<ComplaintInfo> complaints) {
        this.complaints = complaints;
    }

    ArrayList<ComplaintInfo>  complaints=new ArrayList<ComplaintInfo>();
}
