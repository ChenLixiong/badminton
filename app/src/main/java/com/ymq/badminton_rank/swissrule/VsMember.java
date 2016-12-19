package com.ymq.badminton_rank.swissrule;

import java.io.Serializable;

public class VsMember implements Serializable{
	public Member vsm = null;
	public Member vsm1 = null;
	public int turn = 0;
	public int winballs = 0;//Ӯ����
	public int winscore = 0;//Ӯһ�ֵ�һ�֣����򲻵÷֣�1 or 0
}