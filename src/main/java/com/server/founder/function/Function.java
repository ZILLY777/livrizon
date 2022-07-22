package com.server.founder.function;

import com.server.founder.constant.Constant;
import com.server.founder.model.Role;
import com.server.founder.security.JwtUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Function {
    static String url="jdbc:mysql://127.0.0.1:3306/founder";
    static String user="root";
    static String password="root";
    public static int listSum(List<Integer> list){
        int sum=0;
        for (Integer integer : list) {
            sum += integer;
        }
        return sum;
    }
    public static boolean roleFilter(String token, Role role){
        if(role==Role.PERMIT_ALL) return true;
        else {
            Role userRole = JwtUtil.extractRole(token);
            if(userRole==Role.ADMIN) return true;
            else return role==userRole;
        }

    }
    public static List<Integer> isContain(List<Integer> list1, List<Integer> list2){
        List<Integer> list=new ArrayList<>();
        int count = 0;
        for (int a : list2) for (int b : list1) if (a == b) {
            list.add(a);
        }
        return list;
    }
    public static String concat(Object item1,Object item2){
        return concat(item1,item2,".");
    }
    public static String concat(Object item1,Object item2,String delimiter){
        return item1+delimiter+item2;
    }
    public static String toValue(int number) {
        StringBuilder str= new StringBuilder();
        for (int i=0;i<number;i++){
            if(i!=0) str.append(",");
            str.append("(").append("?").append(",").append("?").append(")");
        }
        return str.toString();
    }
    public static String toValues(int number) {
        StringBuilder str= new StringBuilder();
        str.append("(");
        for (int i=0;i<number;i++){
            if(i!=0) str.append(",");
            str.append("?");
        }
        str.append(")");
        return str.toString();
    }
    public static long toMB(long space){
        return space*1024*1024;
    }
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
    public static int random(int a,int b){
        return (int) (Math.random()*(b-a))+a;
    }
}
