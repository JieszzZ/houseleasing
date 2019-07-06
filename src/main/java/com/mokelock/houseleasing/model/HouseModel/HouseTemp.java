package com.mokelock.houseleasing.model.HouseModel;

public class HouseTemp {

    private String user_id;
    private String user;
    private String house_owner_credit;
    private String house_id;
    private String state;
    private String low_location;
    private String specific_location;
    private String floor;
    private boolean elevator;
    private String lease;
    private String lease_type;
    private String house_type;
    private String lon;
    private String lat;
    private String area;

    public HouseTemp() {
    }

    public HouseTemp(String user_id, String user, String house_owner_credit, String house_id, String state, String low_location, String specific_location, String floor, boolean elevator, String lease, String lease_type, String house_type, String lon, String lat, String area) {
        this.user_id = user_id;
        this.user = user;
        this.house_owner_credit = house_owner_credit;
        this.house_id = house_id;
        this.state = state;
        this.low_location = low_location;
        this.specific_location = specific_location;
        this.floor = floor;
        this.elevator = elevator;
        this.lease = lease;
        this.lease_type = lease_type;
        this.house_type = house_type;
        this.lon = lon;
        this.lat = lat;
        this.area = area;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHouse_owner_credit() {
        return house_owner_credit;
    }

    public void setHouse_owner_credit(String house_owner_credit) {
        this.house_owner_credit = house_owner_credit;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLow_location() {
        return low_location;
    }

    public void setLow_location(String low_location) {
        this.low_location = low_location;
    }

    public String getSpecific_location() {
        return specific_location;
    }

    public void setSpecific_location(String specific_location) {
        this.specific_location = specific_location;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public String getLease() {
        return lease;
    }

    public void setLease(String lease) {
        this.lease = lease;
    }

    public String getLease_type() {
        return lease_type;
    }

    public void setLease_type(String lease_type) {
        this.lease_type = lease_type;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
