package com.mokelock.houseleasing.services;

import java.io.File;

public interface FaceService {
    public String match(File get_pic, String name, String eth_File, String ethPassword);
}
