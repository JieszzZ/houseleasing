package com.mokelock.houseleasing.IPFS;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE ;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;

import java.io.IOException;

public class IPFS_TEST {


    public static void main(String[] args) throws IOException {

        IPFS_SERVICE_IMPL demo = new IPFS_SERVICE_IMPL();
        String hash = demo.upload("D:\\untitled\\houseleasing\\src\\main\\file\\1.txt");
        demo.download("D:\\ipfs_downloads\\1.txt",hash,"HouseState0.txt");
        System.out.println(hash);




    }



}
