package com.mokelock.houseleasing.IPFS;

import java.io.IOException;

public class IPFS_TEST {


    public static void main(String[] args) throws IOException {
        IPFS_SERVICE_IMPL demo = new IPFS_SERVICE_IMPL();
        String hash = demo.upload("D:\\go-ipfs\\hello1.txt");
        demo.download("D:\\ipfs_downloads\\hello.txt",hash);
        System.out.println(hash);




    }



}
