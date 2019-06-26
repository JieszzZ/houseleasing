//package com.mokelock.houseleasing.IPFS;
//import com.alibaba.fastjson.*;
//
//import java.io.*;
//
//public class jsontest{
//    public static void insert(String name,String eid){
//        String path="C:\\Users\\徐宇钦\\Desktop\\table1.txt";
//        try{
//            FileOutputStream fos=new FileOutputStream(path,true);
//            String template="name: "+name+";eid: "+eid+"\r\n";
//            fos.write( template.getBytes("utf-8") );
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//    public static String Query(String key){
//
//        String result=null;
//        return result;
//    }
//    public static void main(String args[]){
////        String test="{\"hero_id\":2,\"result\":{\"gold_per_min\":[{\"percentile\":0.1,\"value\":304},{\"percentile\":0.2,\"value\":347},{\"percentile\":0.3,\"value\":379},{\"percentile\":0.4,\"value\":404},{\"percentile\":0.5,\"value\":428},{\"percentile\":0.6,\"value\":453},{\"percentile\":0.7,\"value\":476},{\"percentile\":0.8,\"value\":505},{\"percentile\":0.9,\"value\":544},{\"percentile\":0.95,\"value\":579},{\"percentile\":0.99,\"value\":649}],\"xp_per_min\":[{\"percentile\":0.1,\"value\":365},{\"percentile\":0.2,\"value\":433},{\"percentile\":0.3,\"value\":484},{\"percentile\":0.4,\"value\":530},{\"percentile\":0.5,\"value\":570},{\"percentile\":0.6,\"value\":610},{\"percentile\":0.7,\"value\":646},{\"percentile\":0.8,\"value\":688},{\"percentile\":0.9,\"value\":737},{\"percentile\":0.95,\"value\":779},{\"percentile\":0.99,\"value\":865}],\"kills_per_min\":[{\"percentile\":0.1,\"value\":0.07334963325183375},{\"percentile\":0.2,\"value\":0.11070110701107011},{\"percentile\":0.3,\"value\":0.141643059490085},{\"percentile\":0.4,\"value\":0.16881028938906753},{\"percentile\":0.5,\"value\":0.19586507072905332},{\"percentile\":0.6,\"value\":0.23089609675645958},{\"percentile\":0.7,\"value\":0.26666666666666666},{\"percentile\":0.8,\"value\":0.3112263801407929},{\"percentile\":0.9,\"value\":0.39215686274509803},{\"percentile\":0.95,\"value\":0.4639922667955534},{\"percentile\":0.99,\"value\":0.6079797340088664}],\"last_hits_per_min\":[{\"percentile\":0.1,\"value\":2.5920360631104433},{\"percentile\":0.2,\"value\":3.172737955346651},{\"percentile\":0.3,\"value\":3.5202492211838003},{\"percentile\":0.4,\"value\":3.8485523385300664},{\"percentile\":0.5,\"value\":4.1080196399345335},{\"percentile\":0.6,\"value\":4.450261780104712},{\"percentile\":0.7,\"value\":4.852941176470589},{\"percentile\":0.8,\"value\":5.218818380743983},{\"percentile\":0.9,\"value\":6.020408163265306},{\"percentile\":0.95,\"value\":6.661435857198902},{\"percentile\":0.99,\"value\":8.911954187544739}],\"hero_damage_per_min\":[{\"percentile\":0.1,\"value\":338.3162217659138},{\"percentile\":0.2,\"value\":419.87927565392357},{\"percentile\":0.3,\"value\":478.5645933014354},{\"percentile\":0.4,\"value\":543.1353282715498},{\"percentile\":0.5,\"value\":589.9236641221373},{\"percentile\":0.6,\"value\":640.8260869565217},{\"percentile\":0.7,\"value\":697.351724137931},{\"percentile\":0.8,\"value\":775.4522613065326},{\"percentile\":0.9,\"value\":885.479302832244},{\"percentile\":0.95,\"value\":973.9223300970874},{\"percentile\":0.99,\"value\":1227.3283480625423}],\"hero_healing_per_min\":[{\"percentile\":0.1,\"value\":0},{\"percentile\":0.2,\"value\":0},{\"percentile\":0.3,\"value\":0},{\"percentile\":0.4,\"value\":0},{\"percentile\":0.5,\"value\":0},{\"percentile\":0.6,\"value\":0},{\"percentile\":0.7,\"value\":0},{\"percentile\":0.8,\"value\":0},{\"percentile\":0.9,\"value\":0},{\"percentile\":0.95,\"value\":0},{\"percentile\":0.99,\"value\":13.00768386388584}],\"tower_damage\":[{\"percentile\":0.1,\"value\":0},{\"percentile\":0.2,\"value\":53},{\"percentile\":0.3,\"value\":186},{\"percentile\":0.4,\"value\":426},{\"percentile\":0.5,\"value\":754},{\"percentile\":0.6,\"value\":1171},{\"percentile\":0.7,\"value\":1706},{\"percentile\":0.8,\"value\":2297},{\"percentile\":0.9,\"value\":3247},{\"percentile\":0.95,\"value\":4073},{\"percentile\":0.99,\"value\":5791}],\"stuns_per_min\":[{\"percentile\":0.1,\"value\":0},{\"percentile\":0.2,\"value\":0},{\"percentile\":0.3,\"value\":0},{\"percentile\":0.4,\"value\":0},{\"percentile\":0.5,\"value\":0},{\"percentile\":0.6,\"value\":0},{\"percentile\":0.7,\"value\":0},{\"percentile\":0.8,\"value\":0},{\"percentile\":0.9,\"value\":0},{\"percentile\":0.95,\"value\":0.04857401210287443},{\"percentile\":0.99,\"value\":0.24275473979183346}],\"lhten\":[{\"percentile\":0.1,\"value\":20},{\"percentile\":0.2,\"value\":25},{\"percentile\":0.3,\"value\":30},{\"percentile\":0.4,\"value\":34},{\"percentile\":0.5,\"value\":38},{\"percentile\":0.6,\"value\":42},{\"percentile\":0.7,\"value\":47},{\"percentile\":0.8,\"value\":54},{\"percentile\":0.9,\"value\":64},{\"percentile\":0.95,\"value\":73},{\"percentile\":0.99,\"value\":91}]}}";
////        JSONObject jsonObject=JSON.parseObject(test);
////        JSONArray features=jsonObject.getJSONArray("hero_id");
////        System.out.println(features);
////        insert("A","0x123");
////        insert("B","0x345");
//        try{
//            String key="C";
//            String str  = null;
//            BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\徐宇钦\\Desktop\\table1.txt"));
//            while((str = br.readLine()) != null){
//                String[] result=str.split("name: |;eid: ");
////                System.out.print(result[1]+" "+result[2]+"\n");
//                if(result[1].equals(key)){
//                    System.out.println(result[2]);
//                    break;
//                }
//
//            }
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
//    }
//
//}
