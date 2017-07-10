package com.fpt.menulibs.model;

import java.util.ArrayList;

/**
 * Created by HoangMinh on 19/06/2017.
 */

public class Recommend {
    private String appID;
    private String nametype;
    private ArrayList<RecommendModel> listrecommend;
    public  Recommend(){
        listrecommend = new ArrayList<RecommendModel>();
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public ArrayList<RecommendModel> getListrecommend() {
        return listrecommend;
    }

    public void setListrecommend(ArrayList<RecommendModel> listrecommend) {
        this.listrecommend = listrecommend;
    }

    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }
}
