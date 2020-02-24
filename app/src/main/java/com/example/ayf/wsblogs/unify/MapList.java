package com.example.ayf.wsblogs.unify;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/12/15.
 */

public class MapList {
    public static List<Map<String, Object>> getList() {
        return list;
    }

    public static void setList(List<Map<String, Object>> list) {
        MapList.list = list;
    }

    public static List<Map<String, Object>> list;
}
