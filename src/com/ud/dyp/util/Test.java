package com.ud.dyp.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ud-dyp on 2017/8/21.
 */
public class Test {
    public static void main(String[] args){
        String ss = "tvRight = (TextView) findViewById(R.id.tv_right);\n" +
                "    tvTitle = (TextView) findViewById(R.drawable.tv_title_DaSD);\n" +
                " Toast.makeText(this, R.string.super_invalid_id_no_params, Toast.LENGTH_LONG).show(); "+
                "    tvLoading = (TextView) findViewById(R.layout.tv_progress);";
        int count = 0;
//        String regEx = "R\\.[a-z]*\\.[a-z_0-9]*";
        String regEx = "R\\.[a-z]*\\.\\w*";
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

        for (String item : list) {
            String s = replaceResource(item);
            ss = ss.replaceAll(item,s);
        }
        System.err.println(ss);
    }

    private static String replaceResource(String idStr){
        String[] split = idStr.split("\\.");
        String sourceFormat = new String("getResources().getIdentifier(\"%s\",\"%s\",getPackageName())");
        return String.format(sourceFormat,split[2],split[1]);
    }

}

