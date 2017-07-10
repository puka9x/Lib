package com.fpt.menulibs.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.menulibs.R;
import com.fpt.menulibs.connectdata.ManagerOnmenu;
import com.fpt.menulibs.model.FPTApp;
import com.fpt.menulibs.model.Menumodel;
import com.fpt.menulibs.provider.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangMinh on 09/06/2017.
 */

public class AdapterFavoriteAppMenu extends BaseAdapter {
    protected ImageLoader imageLoader;
    public ArrayList<Menumodel> listData;
    public Activity context;
    public OnmenuFocus onmenuFocus;
    public FPTApp fptApp;
    public AdapterFavoriteAppMenu(Activity ct, ArrayList<Menumodel> list){
        listData = list;
        this.context = ct;
        fptApp = new FPTApp(context);
        imageLoader=new ImageLoader(ct, true, R.drawable.unloading_vod);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(listData.get(position).getMenuId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflat = LayoutInflater.from(parent.getContext());
            convertView = inflat.inflate(R.layout.menu_item_libs, null);
        }

        TextView tv = (TextView)convertView.findViewById(R.id.tvname);
        tv.setText(listData.get(position).getMenuNameVi());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imagemenu) ;
        imageLoader.DisplayImage(listData.get(position).getIcon().toString(), imageView);
        RelativeLayout layouttv = (RelativeLayout)convertView.findViewById(R.id.layouttv);
        layouttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",position+"");
                String type = "";
                switch (listData.get(position).getMenuId()){
                    case "21": //Movies
                        type = fptApp.TYPE_MOVIE;
                        break;
                    case "23":
                        type = fptApp.TYPE_RELAX;
                        break;
                    case "24":
                        type = fptApp.TYPE_KIDS;
                        break;
                }
                fptApp.startApplication("",type);
            }
        });
        layouttv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true){
                    onmenuFocus.OnFocusFavoriteApp(listData.get(position), position);
                    String type = "";
                    switch (listData.get(position).getMenuId()){
                        case "21": //Movies
                            type = fptApp.TYPE_MOVIE;
                            break;
                        case "23":
                            type = fptApp.TYPE_RELAX;
                            break;
                        case "24":
                            type = fptApp.TYPE_KIDS;
                            break;
                    }
                    ManagerOnmenu managerOnmenu = ManagerOnmenu.getInstance();
                    managerOnmenu.setFavoriteTypeNow(type);
                    managerOnmenu.setPackagenow(listData.get(position).getPackagename());
                }

            }
        });

        return convertView;
    }
    public interface OnmenuFocus{
        void OnFocusFavoriteApp(Menumodel menumodel, int positon);
    }
    public void SetmenuCallback(OnmenuFocus onmenuFocus){
        this.onmenuFocus = onmenuFocus;
    }



    public void startApplication(String packageName, String value, String TYPE) {

        try {
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
        intent.setComponent(new ComponentName(packageName, name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("value", value);
        intent.putExtra("TYPE", TYPE);
        // mVideoView.stopPlayback();
        context.startActivity(intent);
    }

}