package com.mokelock.houseleasing.services.servicesImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.services.tools_face_recog.*;
import java.util.*;
import com.mokelock.houseleasing.services.FaceService;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FaceServiceImpl implements FaceService{
    private BlockChain bc=new BlockChain();
    private Table table=new TableImpl();
    private static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "GUImyl9U9ORVQiqWXBPYLQFB";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "OG0O71RBm68AibOVRbrvkEG9uZuvyq33";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    private static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            JSONObject jsonObject= JSONObject.parseObject(result);
            return jsonObject.getString("refresh_token");
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
    public String match(File get_pic,String name,String ethPassword) {
        // 请求url
        String hash1=bc.getHash(0);
        String path1=System.getProperty("user.dir") + "\\src\\main\\file\\"+hash1+"\\user.txt";
        try{
            IPFS_SERVICE_IMPL.download(path1,hash1,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        String[]key_for_search1={"user_name"};
        String[]value_for_search1={name};
        System.out.println(name);
        String[]key_to_get1={"sk"};
        ArrayList<String[]>v=table.query(key_for_search1,value_for_search1,key_to_get1,path1);
        String eth_File=v.get(0)[0];
        BlockChain bc=new BlockChain();
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        try {
            FileInputStream fis = new FileInputStream(get_pic);
            byte[] byt = new byte[fis.available()];
            fis.read(byt);
            String path=System.getProperty("user.dir") + "\\src\\main\\file\\face\\";
            String hash=bc.getHash(0);
            IPFS_SERVICE.download(path+hash+".txt",hash,"123");
            Table table=new TableImpl();
            String[] key_for_search={"user_name"};
            String[] value_for_search={name};
            String[] key_to_get={"eth_id"};
            ArrayList<String[]>t=table.query(key_for_search,value_for_search,key_to_get,path+hash+".txt");
            String eth_id=t.get(0)[0];
            byte[] bytes1 = byt;
            String info=bc.getMessage(eth_id,eth_File,ethPassword);
            JSONObject jsonObject= JSON.parseObject(info);
            String hash2=jsonObject.getString("IPFS_hash");
            IPFS_SERVICE.download(path+hash2+".jpg",hash2,"123");
            byte[] bytes2 = FileUtil.readFileByBytes(path+hash2+".jpg");
            String image1 = Base64Util.encode(bytes1);
            String image2 = Base64Util.encode(bytes2);

            List<Map<String, Object>> images = new ArrayList<>();

            Map<String, Object> map1 = new HashMap<>();
            map1.put("image", image1);
            map1.put("image_type", "BASE64");
            map1.put("face_type", "LIVE");
            map1.put("quality_control", "LOW");
            map1.put("liveness_control", "NORMAL");

            Map<String, Object> map2 = new HashMap<>();
            map2.put("image", image2);
            map2.put("image_type", "BASE64");
            map2.put("face_type", "LIVE");
            map2.put("quality_control", "LOW");
            map2.put("liveness_control", "NORMAL");

            images.add(map1);
            images.add(map2);

            String param = GsonUtils.toJson(images);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken =getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            JSONObject jsonObject1 = JSON.parseObject(result);
            return jsonObject1.getString("score");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String []args){
        System.out.println(getAuth());
    }
}
