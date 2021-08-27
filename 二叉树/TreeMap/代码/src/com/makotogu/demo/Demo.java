package com.makotogu.demo;

import java.util.TreeMap;

public class Demo {

    public static void main(String[] args) {
        TreeMap<Integer,String> map = new TreeMap<>();

        map.put(123,"abc");
        map.put(789,"def");
        map.put(456,"qwe");
        map.put(213,"zxc");

        System.out.println(map);
    }
}
