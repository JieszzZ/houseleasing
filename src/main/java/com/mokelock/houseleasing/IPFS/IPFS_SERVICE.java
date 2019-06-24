package com.mokelock.houseleasing.IPFS;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import  java.io.FileOutputStream;
import java.io.IOException;


public class IPFS_SERVICE {

    private static IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");//ipfs的服务器地址和端口

    public static String upload(String filePathName) throws IOException {
        //filePathName指的是文件的上传路径+文件名，如D:/1.png
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePathName));
        //添加文件到IPFS返回HASH值
        MerkleNode addResult = ipfs.add(file).get(0);
        //输出hash值
        return addResult.hash.toString();
    }

    public static void download(String filePathName,String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        //通过HASH值查询文件转为byte[]
        byte[] data = ipfs.cat(filePointer);
        File file;
        if(data != null){
            file  = new File(filePathName);
            if(file.exists()){
                file.delete();
            }
            //通过文件流输出
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
            fos.flush();
            fos.close();
        }
    }
}
