package com.gachon.dawaga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//임의로 만들어둔 임시 데이터베이스(firebase로 변경예정)
public class SimpleDB {
    private static Map<String, myAppointment> db = new HashMap<>();

    public static void addAppointment(String index, myAppointment mAppo){
        db.put(index, mAppo);
    }

    public static myAppointment getAppo(String index){
        return db.get(index);
    }

    public static List<String> getIndexes(){
        Iterator<String> keys = db.keySet().iterator();

        List<String> keyList = new ArrayList<String>();
        String key = "";
        while (keys.hasNext()){
            key = keys.next();
            keyList.add(key);
        }
        return keyList;
    }
}
