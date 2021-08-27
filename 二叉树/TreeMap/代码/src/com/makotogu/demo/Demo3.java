package com.makotogu.demo;

import java.util.TreeMap;

public class Demo3 {
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(30,"a");
        map.put(20,"b");
        map.put(42,"c");
        map.put(15,"d");
        map.put(22,"e");
        map.put(38,"f");
        String s = map.put(41,"g");
        String b = map.put(42,"h");
        System.out.println(s);
        System.out.println(b);

        System.out.println(map);
    }
}
