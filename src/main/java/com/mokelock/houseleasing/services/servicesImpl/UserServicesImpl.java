package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;
import com.mokelock.houseleasing.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServicesImpl implements UserService {
    @Override
    public int getBalance(String _username) {
        return 0;
    }

    @Override
    public boolean getUser(User _one, String _username) {
        return false;
    }

    @Override
    public boolean postAccount(String _username, int _money) {
        return false;
    }

    @Override
    public ArrayList<record> getRecords(String _username) {
        return null;
    }

    @Override
    public boolean postCredit(User _old, short _credit) {
        return false;
    }

    @Override
    public boolean postGender(User _old, byte _gender) {
        return false;
    }

    @Override
    public boolean postPassword(User _old, String _password) {
        return false;
    }

    @Override
    public boolean postPhone(User _old, String _phone) {
        return false;
    }

    @Override
    public boolean postUser(User _old, modifyUser _modified) {
        return false;
    }


}
