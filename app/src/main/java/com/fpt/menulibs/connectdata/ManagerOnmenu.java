package com.fpt.menulibs.connectdata;

import com.fpt.menulibs.model.Menumodel;
import com.fpt.menulibs.model.Recommend;

import java.util.ArrayList;

public class ManagerOnmenu {
	private static ManagerOnmenu instance;
	private ArrayList<Recommend> recommends;
	private ArrayList<Menumodel> favoriteapp;
	private ArrayList<Menumodel> otherapp;
	private String favoriteTypeNow;
	private String packagenow;
	protected ManagerOnmenu() {
		recommends = new ArrayList<Recommend>();
		favoriteapp = new ArrayList<Menumodel>();
		otherapp = new ArrayList<Menumodel>();
	}

	public static ManagerOnmenu getInstance() {
		if (instance == null) {
			instance = new ManagerOnmenu();
		}
		return instance;
	}

	public String getPackagenow() {
		return packagenow;
	}

	public void setPackagenow(String packagenow) {
		if(packagenow.equals("com.fpt.kids") || packagenow.equals("com.fpt.entertainment")){
			packagenow = "com.fpt.movies";
		}
		this.packagenow = packagenow;
	}

	public String getFavoriteTypeNow() {
		return favoriteTypeNow;
	}

	public void setFavoriteTypeNow(String favoriteTypeNow) {
		this.favoriteTypeNow = favoriteTypeNow;
	}

	public static void setInstance(ManagerOnmenu instance) {
		ManagerOnmenu.instance = instance;
	}

	public ArrayList<Recommend> getRecommends() {
		return recommends;
	}

	public void setRecommends(ArrayList<Recommend> recommends) {
		this.recommends = recommends;
	}

	public ArrayList<Menumodel> getFavoriteapp() {
		return favoriteapp;
	}

	public void setFavoriteapp(ArrayList<Menumodel> favoriteapp) {
		this.favoriteapp = favoriteapp;
	}

	public ArrayList<Menumodel> getOtherapp() {
		return otherapp;
	}

	public void setOtherapp(ArrayList<Menumodel> otherapp) {
		this.otherapp = otherapp;
	}
}
