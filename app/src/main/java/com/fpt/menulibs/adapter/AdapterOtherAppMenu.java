package com.fpt.menulibs.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpt.menulibs.R;
import com.fpt.menulibs.connectdata.ManagerOnmenu;
import com.fpt.menulibs.model.FPTApp;
import com.fpt.menulibs.model.Menumodel;
import com.fpt.menulibs.provider.ImageLoader;

import java.util.ArrayList;

/**
 * Created by HoangMinh on 09/06/2017.
 */

public class AdapterOtherAppMenu extends BaseAdapter {
    protected ImageLoader imageLoader;
    public ArrayList<Menumodel> listData;
    private Activity context;
    private FPTApp fptApp;
    public OnAppOtherFocus onmenuFocus;
    public AdapterOtherAppMenu(Activity ct, ArrayList<Menumodel> list){
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
        try {
            imageLoader.DisplayImage(listData.get(position).getIcon().toString(), imageView);
        }catch (Exception e){

        }
        RelativeLayout layouttv = (RelativeLayout)convertView.findViewById(R.id.layouttv);
        layouttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",position+"");
                fptApp.startApplication(listData.get(position).getId(), ManagerOnmenu.getInstance().getFavoriteTypeNow());
            }
        });
        layouttv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true){
                    onmenuFocus.OnFocusOther(listData.get(position), position);
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
    public interface OnAppOtherFocus{
        void OnFocusOther(Menumodel menumodel, int positon);
    }
    public void SetOtherCallback(OnAppOtherFocus onmenuFocus){
        this.onmenuFocus = onmenuFocus;
    }

}