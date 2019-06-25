package com.mokelock.houseleasing.Cipher.CiphersImpl;
import com.mokelock.houseleasing.Cipher.Ciphers;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.*;

public class CiphersImpl implements Ciphers {
    static Encoder encoder= Base64.getEncoder();
    static Decoder decoder= Base64.getDecoder();

    private static String getKey(int length){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(length);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = encoder.encodeToString(b);
//            System.out.println(s);
//            System.out.println("十六进制密钥长度为"+s.length());
//            System.out.println("二进制密钥的长度为"+s.length()*4);
            return s;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }catch (InvalidParameterException e){
            e.printStackTrace();
            System.out.println("length error");
        }
        return null;
    }
    public String getKey(int length,String pwd){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(length, new SecureRandom(pwd.getBytes()));
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = encoder.encodeToString(b);
//            System.out.println(s);
//            System.out.println("十六进制密钥长度为"+s.length());
//            System.out.println("二进制密钥的长度为"+s.length()*4);
            return s;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }catch (InvalidParameterException e){
            e.printStackTrace();
            System.out.println("length error");
        }
        return null;
    }
    public String encryAES(String text,String key,int mode){
        byte[] bKey=decoder.decode(key);
        SecretKeySpec sk = new SecretKeySpec(bKey, "AES");
        try{
            Cipher cipher = Cipher.getInstance("AES");
            if(mode==0) {
                byte[] bText=text.getBytes("utf8");
                cipher.init(Cipher.ENCRYPT_MODE, sk); // 初始化
                byte[] result = cipher.doFinal(bText);
                String fin_text = encoder.encodeToString(result);
                return fin_text;
            }else{
                byte[] bText=decoder.decode(text);
                cipher.init(Cipher.DECRYPT_MODE, sk); // 初始化
                byte[] result = cipher.doFinal(bText);
                String fin_text = new String(result);
                return fin_text;
            }
        }catch (NoSuchAlgorithmException e){
            System.out.println("check algo");
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }catch(InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return "fail";
    }


    public void encryFileAES(File src_file,String key,String path,int mode){
        byte[] bKey=decoder.decode(key);
        SecretKeySpec sk = new SecretKeySpec(bKey, "AES");
        File dst_file=new File(path);
        if(src_file.exists()&&src_file.isFile()){
            if (!dst_file.getParentFile().exists()) {
                dst_file.getParentFile().mkdirs();
            }
            try{
                dst_file.createNewFile();
                InputStream in =new FileInputStream(src_file);
                OutputStream out=new FileOutputStream(dst_file);
                Cipher cipher = Cipher.getInstance("AES");
                if(mode==0)
                    cipher.init(Cipher.ENCRYPT_MODE, sk); // 初始化
                else
                    cipher.init(Cipher.DECRYPT_MODE, sk); // 初始化
                CipherInputStream cin = new CipherInputStream(in, cipher);
                byte[] cache = new byte[1024];
                int nRead = 0;
                while ((nRead = cin.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                cin.close();
                in.close();
                in.close();


            }catch (NoSuchAlgorithmException e){
                System.out.println("check algo");
            }catch (NoSuchPaddingException e){
                e.printStackTrace();
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }catch(InvalidKeyException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

        }


    }


    private final  static String algo_type="PBEWithMD5AndDES";
    private final static int SALT_COUNT=100;
    private static byte[] saltgen(){
        String salt_text="qfgkpmng";
        byte[] salt=salt_text.getBytes();
//        Random random=new Random();
//        random.nextBytes(salt);
        return salt;
    }//生成盐值
    private static Key getPBEKey(String passwd){
        SecretKey sk=null;
        PBEKeySpec keySpec=new PBEKeySpec(passwd.toCharArray());
        try{
            SecretKeyFactory factory=SecretKeyFactory.getInstance(algo_type);
            sk=factory.generateSecret(keySpec);
        }catch (NoSuchAlgorithmException e){
            System.out.println("No Algo named: "+algo_type);
            e.printStackTrace();
        }catch (InvalidKeySpecException e){
            System.out.println("check your keySpec");
            e.printStackTrace();
        }
        return sk;
    }//用口令passwd生成密钥
    private static final byte[] EncryptByPBE(byte[] plainText,String passwd,byte[]salt,int mode){ ;
        byte[] bEncryText=null;
        try{
            Key k=getPBEKey(passwd);
            PBEParameterSpec paraSpec=new PBEParameterSpec(salt,SALT_COUNT);
            Cipher cipher=Cipher.getInstance(algo_type);
            if(mode==0)
                cipher.init(Cipher.ENCRYPT_MODE,k,paraSpec);
            else
                cipher.init(Cipher.DECRYPT_MODE,k,paraSpec);
            bEncryText=cipher.doFinal(plainText);
        }catch(NoSuchAlgorithmException e){
            System.out.println("check your algo_type :"+algo_type);
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return bEncryText;
    }
    private static final String convertToBase64(byte[]data){
        Encoder encoder=Base64.getEncoder();
        String text=encoder.encodeToString(data);
        return text;
    }
    private static final byte[] convertFromBase64(String text){
        Decoder decoder=Base64.getDecoder();
        byte[] data=decoder.decode(text);
        return data;
    }
    public String encryPBE(String pwd,String Text,int mode){
        byte[] salt=saltgen();
        if(mode==0){
            //开始加密
            byte[] target=Text.getBytes();
            byte[] encodeData=EncryptByPBE(target,pwd,salt,0);
            //System.out.println(new String(encodeData));
            String data_text=convertToBase64(encodeData);
            return data_text;
        }else{
            //开始解密
            byte[] temp_data=convertFromBase64(Text);
            //System.out.println(new String(temp_data));
            byte[] decodeData=EncryptByPBE(temp_data,pwd,salt,1);
            String data_text=new String(decodeData);
            return data_text;
        }
    }


    public String encryHASH(String str){
        MessageDigest messageDigest=null;
        try{
            messageDigest=MessageDigest.getInstance("MD5");
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            System.out.println("check for algo ");
        }
        messageDigest.update(str.getBytes());
        Encoder encoder= Base64.getEncoder();
        String text=encoder.encodeToString(messageDigest.digest());
        return text;
    }
    public static void main(String args[]){
        System.out.println("1");
    }
}