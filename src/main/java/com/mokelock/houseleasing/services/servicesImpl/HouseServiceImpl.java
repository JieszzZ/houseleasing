package com.mokelock.houseleasing.services.servicesImpl;

import com.alibaba.fastjson.JSON;
import com.mokelock.houseleasing.services.HouseService;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl implements HouseService {
    @Override
    public boolean hasLoggedIn() {
        return false;
    }

    @Override
    public int currentRole() {
        return 0;
    }

    @Override
    public JSON speinfo(String house_hash) {
        return null;
    }

    @Override
    public JSON search(JSON low_location, int lease_inter, int house_type, int lease_type) {
        return null;
    }

    @Override
    public JSON valuation(String house_hash, int house_level, String comment, String[] comment_pic) {
        return null;
    }

    @Override
    public JSON allinfo() {
        return null;
    }
}
