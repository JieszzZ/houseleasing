package com.mokelock.houseleasing.Cipher;
import java.io.File;
public interface Ciphers {
    String getKey(int length,String pwd);//通过口令生成指定长度（128，192，256）的AES密钥，可以第一次的时候调用，记住pwd即可解密。
    String encryAES(String text,String key,int mode);//用于加密字符串类型的隐私信息如身份证号，需求。
    void encryFileAES(File src_file,String key,String path,int mode);//用于加密文件类型的隐私信息（如照片），需求。
    String encryPBE(String pwd,String plainText,int mode/*mode=0加密，mode=1解密*/);//基于口令的加解密，目前没有需求。
    String encryHASH(String pwd);//用来保护数据库中用户的密码，前端密码明文调用这个函数后得到的HASH值存进数据库，每次用户登陆验证的是HASH值，最好用。
}