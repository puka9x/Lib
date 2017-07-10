package com.fpt.menulibs.model;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import com.fpt.menulibs.connectdata.ManagerOnmenu;

import java.util.List;

/**
 * Created by HoangMinh on 19/06/2017.
 */

public class FPTApp {
    public  static String TYPE_TV = "0";
    public  static String TYPE_MOVIE = "1";
    public  static String TYPE_RELAX = "3";
    public  static String TYPE_KIDS = "2";
    public  static String TYPE_SPORT = "4";
    public Activity context;
    public  FPTApp(Activity context){
        this.context=context;
    }
    public void startApplication(String value, String TYPE) {

        try {
            String packageName = ManagerOnmenu.getInstance().getPackagenow();
            Log.d("TAG",packageName);
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

            for (ResolveInfo info : resolveInfoList)
                if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                    launchComponent(info.activityInfo.packageName, info.activityInfo.name, value, TYPE);
                    return;
                }

            // No match, so application is not installed
            //  showInMarket(packageName);

        } catch (Exception e) {
            Showmess();
        }
    }

    public void Showmess() {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Toast.makeText(context.getApplicationContext(), "Appplication not installed", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void launchComponent(String packageName, String name, String value, String TYPE) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addCategory("android.intent.category.LEANBACK_LAUNCHER");
        intent.setComponent(new ComponentName(packageName, name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("value", value);
        intent.putExtra("TYPE", TYPE);
        // mVideoView.stopPlayback();
        context.startActivity(intent);
    }
}
