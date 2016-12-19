package com.ymq.badminton_rank.bean;

import com.ymq.badminton_rank.swissrule.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenlixiong on 2016/11/28.
 */

public class SeriMap implements Serializable {
    HashMap<Integer,ArrayList<Member>> map;


    public SeriMap() {
    }

    public HashMap<Integer, ArrayList<Member>> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, ArrayList<Member>> map) {
        this.map = map;
    }
}
