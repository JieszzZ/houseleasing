package com.mokelock.houseleasing.IPFS.TableImpl;
import com.mokelock.houseleasing.IPFS.Table;

import java.io.*;

public class TableImpl implements  Table{
    public void create(String path){
        File table_file=new File(path);
    }
    public void insert(String user_name,String eth_id,String path){
        File file=new File(path);
        try{
            FileOutputStream fos=new FileOutputStream(path,true);
            String template="name: "+user_name+";eid: "+eth_id+"\r\n";
            fos.write( template.getBytes("utf-8") );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String query(String key,String path){
        try{
            String str  = null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str = br.readLine()) != null){
                String[] result=str.split("name: |;eid: ");
//                System.out.print(result[1]+" "+result[2]+"\n");
                if(result[1].equals(key)){
                    return result[2];
                }

            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return "Not found";
    }
    public static void main(String args[]){
        TableImpl tml=new TableImpl();
        String path="C:\\Users\\徐宇钦\\Desktop\\te.txt";
        tml.create(path);
        tml.insert("A","0X123456",path);
        tml.insert("B","0X456",path);
        tml.insert("C","0X123",path);
        System.out.println(tml.query("A",path));

    }
}
