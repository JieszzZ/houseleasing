package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.services.UserServices;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserServices {
    @Override
    public boolean signIn() {
        return false;
    }
}
