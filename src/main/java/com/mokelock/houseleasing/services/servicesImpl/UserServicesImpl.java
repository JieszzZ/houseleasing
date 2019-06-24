package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserService {
    @Override
    public boolean signIn() {
        return false;
    }
}
