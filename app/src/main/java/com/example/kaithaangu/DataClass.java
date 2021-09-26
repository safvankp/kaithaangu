package com.example.kaithaangu;

import java.util.ArrayList;
import java.util.List;

public class DataClass {

    static DataClass dataClass = null;
    private static SharedData sharedData;

    public static String LOG_TAG = "Kaithaangu";

    public static List<DocumentClass> documentClassList = new ArrayList<>();

    //screen Names
    public static String Admin = "Admin";
    public static String Profile = "Profile";

    public static DataClass getInstance(SharedData pref){
        sharedData = pref;
        if (dataClass == null)
            dataClass = new DataClass();
        return dataClass;
    }


    public void saveUserName(String name){
        sharedData.getPref().edit().putString("UserName",name).apply();
    }

    public String getUserName(){
        return sharedData.getPref().getString("UserName","");
    }

    public void saveMobile(String number){
        sharedData.getPref().edit().putString("Mobile",number).apply();
    }

    public String getMobile(){
        return sharedData.getPref().getString("Mobile","");
    }

    public void saveEmail(String email){
        sharedData.getPref().edit().putString("Email",email).apply();
    }

    public String getEmail(){
        return sharedData.getPref().getString("Email","");
    }

    public void saveLoggedState(boolean flag){
        sharedData.getPref().edit().putBoolean("isLogged",flag).apply();
    }

    public boolean getLoggedStatus(){
        return sharedData.getPref().getBoolean("isLogged",false);
    }

    public void saveLastVisitedScreen(String screeName){
        sharedData.getPref().edit().putString("LastVisitedScreen",screeName).apply();
    }

    public String getLastVisitedScreen(){
        return sharedData.getPref().getString("LastVisitedScreen","");
    }


}
