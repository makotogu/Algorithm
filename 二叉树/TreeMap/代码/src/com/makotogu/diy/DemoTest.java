package com.makotogu.diy;

public class DemoTest {

    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(2,"a");
        map.put(1,"b");
        map.put(3,"c");
        map.put(6,"d");
        map.put(4,"e");
        map.put(5,"f");
        map.put(7,"g");
        System.out.println(map);
        map.remove(4);
        map.remove(6);
        System.out.println(map);
    }
}
