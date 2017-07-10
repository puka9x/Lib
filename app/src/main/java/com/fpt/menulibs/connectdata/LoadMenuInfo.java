package com.fpt.menulibs.connectdata;

import com.fpt.menulibs.model.Recommend;

import java.util.ArrayList;

/**
 * Created by HoangMinh on 19/06/2017.
 */

public class LoadMenuInfo {
    public static String PROFILE = "VDSL";
    public static String MAC = "B046FCBD2AE6";
    public static String CONTRACT = "SGDN00030";
    public static String CUSTOMERID = "870144";
    public static String CUSTOMERTYPE = "1";
    public static String USERID = "5000003";
    public static String LANGUAGE = "vi";
    public static String LOCALTYPE = "1";
    public static String ADSNUM = "21";
    public static String FOLDER = "19";
    public static String TYPE_RELAX = "1";
    public static String TYPE_CHILDREN = "2";
    public static String APP_ID_VOD = "21";
    public static String APP_ID_RELAX = "23";
    public static String APP_ID_CHILDREN = "24";
    public static String CATALOG = "";
    public static String PATENTAL = "0";
    public static String ALLOWDISPLAY = "2";
    public ManagerOnmenu managerOnmenu;
    public ArrayList<Recommend> recommends;
    public static void Loadmenu(){
        LoadmenuApp();
        LoadRecommend();
    }

    private static void LoadmenuApp() {
        String connecttion = "https://dev-fbox-api.fpt.vn/fboxapi/customers/getcustomerinfo/v2/" +
                MAC + "/" + CONTRACT + "/" + CUSTOMERID + "/" + CUSTOMERTYPE + "/" + USERID + "/" + LANGUAGE ;
        String result = ConnectDATAMenu.GET(connecttion);
        ParsingProviderMenu.parseListMenu(result);

    }

    private static void LoadRecommend() {
        String connecttion = "http://fbox-api.onetv.vn/fboxapi/general/recommendmenu/v1/" +
                MAC + "/" + CONTRACT + "/" + CUSTOMERID + "/" + CUSTOMERTYPE + "/" + USERID + "/" + LANGUAGE
                + "/" + PROFILE  + "/" +ALLOWDISPLAY + "/" + PATENTAL;
        String result = ConnectDATAMenu.GET(connecttion);
        ParsingProviderMenu.parseRecommend(result);
    }
}
