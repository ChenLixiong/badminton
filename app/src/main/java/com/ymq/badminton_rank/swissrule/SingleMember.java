package com.ymq.badminton_rank.swissrule;

import java.io.Serializable;

public class SingleMember extends Member implements Serializable{
	@Override
	public VsMember inVsList(Member m) {
		for(int i=0;i < vslist.size();i++) {
			if(vslist.get(i).vsm.id == m.id) {
				return vslist.get(i);
			}
		}
		return null;
	}

	@Override
	public Member inCoList(Member m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDoubleVs() {
		// TODO Auto-generated method stub
		return false;
	}

}