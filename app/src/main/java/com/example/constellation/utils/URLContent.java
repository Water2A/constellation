package com.example.constellation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLContent {

    //星座配对接口
    public static String getParnterURL(String man, String woman) {
        man = man.replace("座", "");
        woman = woman.replace("座", "");
        try {
            man = URLEncoder.encode(man, "UTF-8");
            woman = URLEncoder.encode(woman, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url="http://apis.juhe.cn/xzpd/query?men="+man+"&women="+woman+"&key=6ee1bd32df67a0882c719fee117b2699";
        return url;
    }

    //星座运势接口
    public static String getLuckURL(String name) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://web.juhe.cn/constellation/getAll?consName=" + name + "&type=year&key=80c7e9679481e3743137eafc854d2a99";
        return url;
    }
}
