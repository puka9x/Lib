package com.fpt.menulibs.connectdata;

import com.fpt.menulibs.model.Menumodel;
import com.fpt.menulibs.model.Recommend;
import com.fpt.menulibs.model.RecommendModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingProviderMenu {

	private static final String ROOT = "Root";
	private static final String ROOT_ = "root";
	private static final String JSONArrayName = "item";
	public static void parseListMenu(String jsonString){
		ArrayList<Menumodel> listFavoriteApp = new ArrayList<Menumodel>();
		ArrayList<Menumodel> listOtherApp = new ArrayList<Menumodel>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject root = jsonObject.getJSONObject(ROOT_);
            JSONArray listmenuhome = root.getJSONArray("listmenuhome");
            JSONArray listappfavorite = listmenuhome.getJSONObject(0).getJSONArray("listapp");
            JSONArray listappother = listmenuhome.getJSONObject(1).getJSONArray("listapp");
            listFavoriteApp = parseListmenuDetail(listappfavorite);
            listOtherApp = parseListmenuDetail(listappother);
            ManagerOnmenu managerOnmenu = ManagerOnmenu.getInstance();
            managerOnmenu.setFavoriteapp(listFavoriteApp);
            managerOnmenu.setOtherapp(listOtherApp);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
	public static ArrayList<Menumodel> parseListmenuDetail(JSONArray string){
		ArrayList<Menumodel> list = new ArrayList<Menumodel>();
        for (int i = 0; i < string.length(); i++){
            try {
                Menumodel menumodel = new Menumodel();
                menumodel.setStatus(string.getJSONObject(i).getString("status"));
                menumodel.setAppUrl(string.getJSONObject(i).getString("appurl"));
                menumodel.setMessageVi(string.getJSONObject(i).getString("messagevi"));
                menumodel.setMenuNameEn(string.getJSONObject(i).getString("menunameen"));
                menumodel.setMessageEn(string.getJSONObject(i).getString("messageen"));
                menumodel.setMenuId(string.getJSONObject(i).getString("menuid"));
                menumodel.setMenuNameVi(string.getJSONObject(i).getString("menunamevi"));
                menumodel.setLogin(string.getJSONObject(i).getString("login"));
                menumodel.setIconDisable(string.getJSONObject(i).getString("icondisable"));
                menumodel.setIcon(string.getJSONObject(i).getString("icon"));
                menumodel.setPackagename(string.getJSONObject(i).getString("package"));
                if(!menumodel.getStatus().equals("0")){
                    list.add(menumodel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

		return list;
	}

    public static void parseRecommend(String result) {
        ArrayList<Recommend> recommends = new ArrayList<Recommend>();
        try {
            JSONObject root = new JSONObject(result).getJSONObject(ROOT_);
            JSONArray jsonArray = root.getJSONArray(JSONArrayName);
            for (int i = 0; i < jsonArray.length(); i++){
                Recommend recommend = new Recommend();
                recommend.setAppID(jsonArray.getJSONObject(i).getString("appid"));
                recommend.setNametype(jsonArray.getJSONObject(i).getString("nametype"));
                JSONObject temp = jsonArray.getJSONObject(i);
                JSONArray array_temp = temp.getJSONArray("details");
                recommend.setListrecommend(parseRecommendDetail(array_temp));
                recommends.add(recommend);
            }
            ManagerOnmenu.getInstance().setRecommends(recommends);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<RecommendModel> parseRecommendDetail(JSONArray result) {
        ArrayList<RecommendModel> list = new ArrayList<RecommendModel>();
        for (int i = 0; i < result.length(); i++){
            RecommendModel recommendModel = new RecommendModel();
            try{
                recommendModel.setChannelno(result.getJSONObject(i).getString("channelno"));
            }catch (Exception e){

            }
            try{
                recommendModel.setChannelid(result.getJSONObject(i).getString("channelid"));
            }catch (Exception e){

            }
            try{
                recommendModel.setIstimeshift(result.getJSONObject(i).getString("istimeshift"));
            }catch (Exception e){

            }
            try{
                recommendModel.setLogo(result.getJSONObject(i).getString("logo"));
            }catch (Exception e){

            }
            try{
                recommendModel.setChannelname(result.getJSONObject(i).getString("channelname"));
            }catch (Exception e){

            }
            try{
                recommendModel.setLogonotify(result.getJSONObject(i).getString("logonotify"));
            }catch (Exception e){

            }
            try{
                recommendModel.setParental(result.getJSONObject(i).getString("parental"));
            }catch (Exception e){

            }
            try{
                recommendModel.setImage(result.getJSONObject(i).getString("image"));
            }catch (Exception e){

            }
            try{
                recommendModel.setTitle(result.getJSONObject(i).getString("title"));
            }catch (Exception e){

            }
            try{
                recommendModel.setLabel(result.getJSONObject(i).getString("label"));
            }catch (Exception e){

            }
            try{
                recommendModel.setMainitemid(result.getJSONObject(i).getString("mainitemid"));
            }catch (Exception e){

            }
            try{
                recommendModel.setId(result.getJSONObject(i).getString("id"));
            }catch (Exception e){

            }
            list.add(recommendModel);
        }
        return list;

    }
}
