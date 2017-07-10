package com.fpt.menulibs;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.menulibs.adapter.AdapterFavoriteAppMenu;
import com.fpt.menulibs.adapter.AdapterOtherAppMenu;
import com.fpt.menulibs.adapter.AdapterRelateAppMenu;
import com.fpt.menulibs.adapter.AdapterRelateTVAppMenu;
import com.fpt.menulibs.connectdata.LoadMenuInfo;
import com.fpt.menulibs.connectdata.ManagerOnmenu;
import com.fpt.menulibs.model.Menumodel;
import com.fpt.menulibs.model.RecommendModel;
import com.fpt.menulibs.ui.HorizontalListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuMainActivity extends Activity
        implements AdapterFavoriteAppMenu.OnmenuFocus,
        AdapterRelateAppMenu.OnApprelateFocus,
        AdapterOtherAppMenu.OnAppOtherFocus,
        AdapterRelateTVAppMenu.OnApprelateFocus{
    public final  int REQUEST_READWRITE_STORAGE = 1234;
    public final String TAG = "TAG";
    public RelativeLayout menulayout;
    public int indexmenulayout = 0;
    public int CHANGE_APP = 184;
    public final int UP = 19;
    public final int DOWN = 20;
    public final int LEFT = 21;
    public final int RIGHT = 22;
    public String VOD_TYPE = "1";
    public String RELAX_TYPE = "3";
    public String CHILDREN_TYPE = "2";
    public String TYPE_NOW;
    public OnmenuClikVOD onmenuClikVOD;
    public HorizontalListView lvfavorite;
    public HorizontalListView lvsuggest;
    public HorizontalListView lvorhterApp_1;
    public HorizontalListView lvorhterApp_2;
    public HorizontalListView lvorhterApp_3;
    public int positon_;
    public int preIndexListAppFavorite;
    public AdapterFavoriteAppMenu AdapterfavoriteApp;
    public AdapterRelateAppMenu adapterRelate;
    public AdapterRelateTVAppMenu adapterRelateTV;
    public AdapterOtherAppMenu adapterother;
    public ScrollView scrollView;
    public ManagerOnmenu managerOnmenu;
    public HorizontalListView listviewsuggesttv;
    private boolean onshowMenu = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        managerOnmenu = ManagerOnmenu.getInstance();
        allowNetword();
        LoadMenuInfo.Loadmenu();

    }
    public void showHomeMenu(){
        if(!onshowMenu){
            //	ForcusMenuLayout(indexmenulayout);
            menulayout.setVisibility(View.VISIBLE);
            menulayout.requestFocus();
            requestpermisstion();
            onshowMenu = true;
        }else{
            menulayout.setVisibility(View.GONE);
            onshowMenu = false;
        }

    }
    public void initlibs() {

        lvfavorite = (HorizontalListView) this.findViewById(R.id.listviewitem);
        lvsuggest = (HorizontalListView) this.findViewById(R.id.listviewsuggest);
        scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        lvorhterApp_1 = (HorizontalListView) this.findViewById(R.id.listviewohter1);
        lvorhterApp_2 = (HorizontalListView)this. findViewById(R.id.listviewohter2);
        lvorhterApp_3= (HorizontalListView) this.findViewById(R.id.listviewohter3);
        listviewsuggesttv = (HorizontalListView)this. findViewById(R.id.listviewsuggesttv);
    }

    public void Loaddata(int index) {
        if(AdapterfavoriteApp==null){
            {

                AdapterfavoriteApp = new AdapterFavoriteAppMenu(this, managerOnmenu.getFavoriteapp());
                AdapterfavoriteApp.SetmenuCallback(this);
                lvfavorite.setAdapter(AdapterfavoriteApp);
                ////////////////////////////////////
                int page = managerOnmenu.getOtherapp().size() / 6;
                int moreitem = managerOnmenu.getOtherapp().size() - (page*6);
                if(moreitem > 0){
                    page++;
                }
                int start = 0;
                for (int i = 1; i <= page; i++){
                    ArrayList<Menumodel> arrayListother = new ArrayList<Menumodel>();
                    for (int j = 0; j < 6; j++) {
                        try {
                            arrayListother.add(managerOnmenu.getOtherapp().get(start));
                        }catch (Exception e){

                        }
                        start++;

                    }

                    if(start <= 6){
                        AdapterOtherAppMenu adapterother = new AdapterOtherAppMenu(this, arrayListother);
                        adapterother.SetOtherCallback(this);
                        lvorhterApp_1.setAdapter(adapterother);
                    }else if(start > 6 && start <= 12){
                        AdapterOtherAppMenu adapterother = new AdapterOtherAppMenu(this, arrayListother);
                        adapterother.SetOtherCallback(this);
                        lvorhterApp_2.setAdapter(adapterother);
                    }else if(start > 12){
                        AdapterOtherAppMenu adapterother = new AdapterOtherAppMenu(this, arrayListother);
                        adapterother.SetOtherCallback(this);
                        lvorhterApp_3.setAdapter(adapterother);
                    }
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // getWindow().getCurrentFocus().setBackground(getResources().getDrawable(R.drawable.borderitemonmenu));
                        lvfavorite.requestFocus();
                        lvfavorite.getChildAt(0).setSelected(true);
                        lvfavorite.getChildAt(0).requestFocus();
                        listener();
                    }
                }, 500);


            }
        }
    }

    public void listener() {
        lvfavorite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               // getWindow().getCurrentFocus().setBackground(getResources().getDrawable(R.drawable.borderitemonmenu));
                Log.d(TAG,"lvfavorite change focus");
                lvfavorite.getChildAt(preIndexListAppFavorite).setSelected(true);
                lvfavorite.getChildAt(preIndexListAppFavorite).requestFocus();
            }
        });
    }

    public void ClickOnMenu() {
        menulayout.setVisibility(View.GONE);
        switch (indexmenulayout) {
            case 0:
                changeAppDetailVOD("");
                startApplication("com.fpt.tvapp", "");
                finish();
                break;
            case 1:
                changeAppDetailVOD(VOD_TYPE);
                startApplication("com.fpt.vod", VOD_TYPE);

                break;
            case 2:
                changeAppDetailVOD(RELAX_TYPE);
                startApplication("com.fpt.vod", RELAX_TYPE);
                break;
            case 3:
                changeAppDetailVOD(CHILDREN_TYPE);
                startApplication("com.fpt.vod", CHILDREN_TYPE);
                break;

            case 4:
                startApplication("com.fpt.sport", "");
                break;
            default:
                break;
        }

    }

    public void changeAppDetailVOD(String type) {
        if (onmenuClikVOD != null) {
            onmenuClikVOD.ChangeAppInDetailVOD(type);
        }
    }

    public void startApplication(String packageName, String value) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);

            for (ResolveInfo info : resolveInfoList)
                if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                    launchComponent(info.activityInfo.packageName, info.activityInfo.name, value);
                    return;
                }

            // No match, so application is not installed
            //  showInMarket(packageName);

        } catch (Exception e) {
            Showmess();
        }
    }



    public void Showmess() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "Appplication not installed", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void launchComponent(String packageName, String name, String value) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(packageName, name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("value", value);
        // mVideoView.stopPlayback();
        startActivity(intent);
    }

    @Override
    public void OnFocusFavoriteApp(Menumodel menumodel, int positon_) {
        Log.d(TAG, positon_ + "");
        UpdateSussgest(menumodel,positon_);
        scrollView.smoothScrollTo(0, 0);


    }

    @Override
    public void OnFocusOther(Menumodel menumodel, int positon) {
        preIndexListAppFavorite = positon;
        scrollView.smoothScrollTo(199, 199);

    }

    @Override
    public void OnFocusRelate(RecommendModel menumodel, int positon) {

    }

    public interface OnmenuClikVOD {
        void ChangeAppInDetailVOD(String type);
    }

    public void SetCallBackOnmenuClikVOD(OnmenuClikVOD onmenuClikVOD) {
        this.onmenuClikVOD = onmenuClikVOD;
    }

    public void UpdateSussgest(Menumodel menumodel,final int index) {
        TextView textView = (TextView)findViewById(R.id.tvtitlerelate);
        ArrayList<RecommendModel> arrayList = new ArrayList<RecommendModel>();
        for(int i = 0; i < ManagerOnmenu.getInstance().getRecommends().size(); i++){
            if(menumodel.getMenuId().equals(ManagerOnmenu.getInstance().getRecommends().get(i).getAppID())){
                textView.setText(ManagerOnmenu.getInstance().getRecommends().get(i).getNametype());
                arrayList = ManagerOnmenu.getInstance().getRecommends().get(i).getListrecommend();
                if(menumodel.getMenuId().equals("20")){
                    listviewsuggesttv.setVisibility(View.VISIBLE);
                    lvsuggest.setVisibility(View.GONE);
                    adapterRelateTV = new AdapterRelateTVAppMenu(this, arrayList);
                    adapterRelateTV.SetRelateCallback(this);
                    listviewsuggesttv.setAdapter(adapterRelateTV);
                }else{
                    listviewsuggesttv.setVisibility(View.GONE);
                    lvsuggest.setVisibility(View.VISIBLE);
                    adapterRelate = new AdapterRelateAppMenu(this, arrayList);
                    adapterRelate.SetRelateCallback(this);
                    lvsuggest.setAdapter(adapterRelate);
                }
            }
        }


    }
    public void requestpermisstion(){

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_READWRITE_STORAGE);
        } else {
            initlibs();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Loaddata(0);
                }
            }, 500);

            String folder_main = "BoxInfo";
            File parent = Environment.getExternalStorageDirectory();
            File f = new File(parent, folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_READWRITE_STORAGE) {
                int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_READWRITE_STORAGE);
                } else {
                    initlibs();
                    Loaddata(0);
                    String folder_main = "BoxInfo";
                    File parent = Environment.getExternalStorageDirectory();
                    File f = new File(parent, folder_main);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                }
            }
}
public void allowNetword(){
    if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
}
