package com.ud.dyp.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ud-dyp on 2017/8/21.
 */
public class TestActivity {
    public static void main(String[] args){
        String ss = "public class MainActivity extends BaseActivity {\n" +
                "    tvTitle = (TextView) findViewById(R.drawable.tv_title_DaSD);\n" +
                " Toast.makeText(this, R.string.super_invalid_id_no_params, Toast.LENGTH_LONG).show(); "+
                "    tvLoading = (TextView) findViewById(R.layout.tv_progress);";
        int count = 0;
//        String regEx = "R\\.[a-z]*\\.[a-z_0-9]*";
        String regEx = "extends\\s\\w*";



        String str = ss;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            count = count + 1;
            System.out.println(m.groupCount());
            System.out.println(m.group());
            list.add(m.group());
        }
        System.out.println("共有 " + count + "个 ");
    }

//    private static List<String> getRxList(String data,String regEx){
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(data);
//        List<String> list = new ArrayList<>();
//        while (m.find()) {
//            list.add(m.group());
//        }
//        return list;
//
//    }
}

