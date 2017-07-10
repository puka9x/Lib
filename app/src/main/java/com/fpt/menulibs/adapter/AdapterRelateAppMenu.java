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
import com.fpt.menulibs.model.RecommendModel;
import com.fpt.menulibs.provider.ImageLoader;

import java.util.ArrayList;

/**
 * Created by HoangMinh on 09/06/2017.
 */

public class AdapterRelateAppMenu extends BaseAdapter {
    protected ImageLoader imageLoader;
    public ArrayList<RecommendModel> listData;
    private Activity context;
    public OnApprelateFocus onmenuFocus;
    private FPTApp fptApp;
    public AdapterRelateAppMenu(Activity ct, ArrayList<RecommendModel> list){
        listData = list;
        this.context = ct;
        imageLoader=new ImageLoader(ct, true, R.drawable.unloading_vod);
        fptApp = new FPTApp(context);
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
        return Integer.parseInt(listData.get(position).getId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflat = LayoutInflater.from(parent.getContext());
            convertView = inflat.inflate(R.layout.menu_item_vod, null);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imagemenu) ;
        TextView tv = (TextView)convertView.findViewById(R.id.tvname);
        try{
            if(listData.get(position).getTitle().length() > 0){
                tv.setText(listData.get(position).getTitle());
                imageLoader.DisplayImage(listData.get(position).getImage().toString(), imageView);
            }else {
                tv.setText(listData.get(position).getChannelname());
                imageLoader.DisplayImage(listData.get(position).getLogo().toString(), imageView);
            }
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
                    onmenuFocus.OnFocusRelate(listData.get(position), position);
                }

            }
        });

        return convertView;
    }
    public interface OnApprelateFocus{
        void OnFocusRelate(RecommendModel menumodel, int positon);
    }
    public void SetRelateCallback(OnApprelateFocus onmenuFocus){
        this.onmenuFocus = onmenuFocus;
    }

}