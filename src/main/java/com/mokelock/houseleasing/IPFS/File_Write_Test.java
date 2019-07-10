package com.mokelock.houseleasing.IPFS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class File_Write_Test {
    public static void main(String[] args) {
        File file = null;
        FileWriter fw = null;
        file = new File("C:\\Users\\10922\\Desktop\\demo.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            for (int i = 1; i <= 3000; i++) {
                fw.write("eid" + i + ",");//向文件写内容
                fw.write("name" + i + ",\r\n");//加上换行
                fw.flush();
            }
            System.out.println("写数据成功！");
        } catch (IOException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
