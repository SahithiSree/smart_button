package com.vaagdevi.smartbutton;

import android.util.Log;

public class Getset {
    public Getset(){

    }
    String cname,cnumber,latitude,longitude,Cuid;



    public Getset(String cname, String cnumber,String latitude,String longitude,String Cuid){
        this.cname=cname;
        this.cnumber=cnumber;
        this.latitude=latitude;
        this.longitude=longitude;
        this.Cuid=Cuid;

    }
    public String getCname() {
        return cname;
    }

    public String getCnumber() {
        return cnumber;
    }
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public String getCuid() {
        return Cuid;
    }


}
