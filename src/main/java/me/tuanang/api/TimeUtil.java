package me.tuanang.api;

public class TimeUtil {

    public static String format(long sec){
        long h=sec/3600, m=(sec%3600)/60, s=sec%60;
        return h+"h"+m+"p"+s+"s";
    }

    public static String ago(long t){
        long d=(System.currentTimeMillis()-t)/1000;
        if(d<60) return d+" giây trước";
        if(d<3600) return d/60+" phút trước";
        if(d<86400) return d/3600+" giờ trước";
        return d/86400+" ngày trước";
    }
}
