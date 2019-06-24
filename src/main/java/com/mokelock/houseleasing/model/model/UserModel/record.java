package com.mokelock.houseleasing.model.UserModel;

public class record {
    private String time;
    private int gas;
    private low_location lowLocation;
    private String specific_location;


    public void setTime(String _time){time = _time;}
    public String getTime(){return time;}
    public void setGas(int _gas){gas = _gas;}
    public int getGas(){return gas;}
    public void setLowLocation(low_location _lowLocation){lowLocation = _lowLocation;}
    public low_location getLowLocation(){return lowLocation;}
    public void setSpecific_location(String _specific_location){specific_location = _specific_location;}
    public String getSpecific_location(){return specific_location;}


}
