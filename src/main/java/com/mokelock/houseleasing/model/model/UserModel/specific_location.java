package com.mokelock.houseleasing.model.UserModel;

public class specific_location extends low_location{
    private  String specific_location;

    public specific_location(){
        super();
    }

    public specific_location(String _provi,String _city,String _sector,String _commu_name,String _specific_location )
    {
        super(_provi,_city,_sector,_commu_name);
        specific_location = _specific_location;
    }

    public void setSpecific_location(String _specific_location){specific_location = _specific_location;}
    public String getSpecific_location(){return specific_location;}

}
