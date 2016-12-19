package com.ymq.badminton_rank.swissrule;


import java.io.Serializable;

public class DoubleMember extends Member implements Serializable{
	@Override
	public boolean isDoubleVs() {
		return true;
	}
	@Override
	public VsMember inVsList(Member m) {
		for(int i=0;i < vslist.size();i++) {
			if(vslist.get(i).vsm.id == m.id || vslist.get(i).vsm1.id == m.id) {
				return vslist.get(i);
			}
		}
		return null;
	}

	@Override
	public Member inCoList(Member m) {
		for(int i=0;i < colist.size();i++) {
			if(colist.get(i).id == m.id) {
				return colist.get(i);
			}
		}
		return null;
	}
}
