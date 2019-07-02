package com.mokelock.houseleasing.IPFS.IpfsImpl;

import com.mokelock.houseleasing.IPFS.IPFS_SERVICE;
import com.mysql.cj.xdevapi.AddResult;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import  java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class IPFS_SERVICE_IMPL implements IPFS_SERVICE {

    public static IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");//ipfs的服务器地址和端口
    public static String upload(String filePathName) throws IOException {
        //filePathName指的是文件(夹)的上传路径+文件名，如D:/1.png
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePathName));
        //添加文件到IPFS返回HASH值
        List<MerkleNode> addList = ipfs.add(file);
        MerkleNode addResult=addList.get(addList.size()-1);
        //输出hash值
        return addResult.hash.toString();
    }
    public static void download(String filePathName,String hash,String fileName) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data=null;
        //通过HASH值查询文件转为byte[]
        try {
            //fileName指的是下载文件的名字，如hello.txt
            data = ipfs.cat(filePointer, "/" + fileName);
        }catch (Exception e){
            data = ipfs.cat(filePointer);
        }finally {
        File file;
        if(data != null){
            file  = new File(filePathName);
            if(file.exists()){
                file.delete();
            }
            //通过文件流输出.
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
            fos.flush();
            fos.close();
        }
        }
    }
}
