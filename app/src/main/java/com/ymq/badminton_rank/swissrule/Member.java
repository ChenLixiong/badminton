package com.ymq.badminton_rank.swissrule;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Member implements Serializable{
	public int id = 0;
	public String username;
	public int currenscore;	//本场得分
	public int bigscore = 0;	//���,ÿӮһ�ּ�һ��
	public int smallscore = 0;	//С�֣�ÿ�ֶ��ִ��֮��
	public int totalscore = 0;	//�۽��֣���ѡ��ÿһ�ֵĴ��֮��
	public int winballs = 0;
	public boolean hasVsBye = false;	//�Ƿ���Ϊ�ֿն���á���ս-ʤ����һ����ֻ�ܻ��һ�Ρ���ս-ʤ���Ļ���
	public int vsLowerLuck = 0;			//ͬ�ֲ�Ϊż��ʱ�����С���ߺ͵ͷ�pk����ÿ���ֲ��ܻ��һ�λ��ᣬ��vsLowerLuck == 0���ܷ��䣻
	public VsMember curVsm = null;		//��ǰvs����
	public VsMember curVsm1 = null;
	public ArrayList<VsMember> vslist = new ArrayList<VsMember>();	//��ʷvs�����б�
	public ArrayList<Member> colist = new ArrayList<Member>();
	public String toString() {
		String str = String.format("%d(%d,%d,%d)",id,bigscore,smallscore,winballs);
		return str;
	}

	//@Override
	/*public String toString() {
		return "Member{" +"id=" + id  +
				", username='" + username + '\'' +
				",currentscore" + currenscore +
				", bigscore=" + bigscore +
				", smallscore=" + smallscore +
				", totalscore=" + totalscore +

				'}';
	}*/

	public abstract VsMember inVsList(Member m);
	public abstract Member inCoList(Member m);
	public abstract boolean isDoubleVs();
	// -1 精确模式  1 默认模式
	public int isBiger(Member m,int times,int mode , int currentTurn) {
		/*long self = 0;
		long other = 0;
		if (mode<=0 || currentTurn < times){
			//精确模式
			self = bigscore*1000000+smallscore*10000;
			other = m.bigscore*1000000+m.smallscore*10000;
		}else{//默认模式 超过了指定的局数之后加入winBalls参数
			self = bigscore*1000000+smallscore*10000 +winballs;
			other = m.bigscore*1000000+m.smallscore*10000 + winballs;
		}*/
		long self = bigscore*1000000+smallscore*10000;
		long other = m.bigscore*1000000+m.smallscore*10000;
		if(self > other) return 1;
		else if(self < other) return -1;
		else {
			VsMember vsm = inVsList(m);
			if(vsm == null) { //δ����ս
				return 0;
			}else if(vsm.winscore == 1) {
				return 1;
			}else {
				return -1;
			}
		}
	}
}
