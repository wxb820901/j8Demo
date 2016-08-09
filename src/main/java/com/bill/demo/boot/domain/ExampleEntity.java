package com.bill.demo.boot.domain;

/**
 * Created by WANGBIL on 7/25/2016.
 */

public class ExampleEntity {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExampleEntity(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id:"+id+",");
        if(name!=null){
            sb.append("name:"+name+",");
        }
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }
}
