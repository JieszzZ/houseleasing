package com.mokelock.houseleasing.IPFS;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE ;

import java.io.IOException;

public class IPFS_TEST {


    public static void main(String[] args) throws IOException {
        IPFS_SERVICE demo = new IPFS_SERVICE();
        String hash = demo.upload("D:\\go-ipfs\\hello1.txt");
        demo.download("D:\\ipfs_downloads\\hello.txt",hash);
        System.out.println(hash);




    }



}
