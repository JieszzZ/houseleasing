package com.mokelock.houseleasing.IPFS;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE ;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;

import java.io.IOException;

public class IPFS_TEST {


    public static void main(String[] args) throws IOException {

        IPFS_SERVICE_IMPL demo = new IPFS_SERVICE_IMPL();
        String hash = demo.upload("D:\\ipfs_downloads\\20161229225526_KUFBH.jpeg");
        demo.download("D:\\go-ipfs\\1.jpg",hash,"hello.txt");
        System.out.println(hash);




    }



}
