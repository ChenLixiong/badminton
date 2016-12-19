package com.ymq.badminton_rank.swissrule;

import com.ymq.badminton_rank.badminton.MathEx;

import java.util.ArrayList;
import java.util.HashMap;

public class SwissRule {
	public static final int NEIGHBORVS = 0;	//ͬ��֣�����С��pk
	public static final int MAXVSMIN = 1;	//ͬ��֣�������СС��pk
	public static final int MAXVSLOWER = 0;	//���С����PK�ͷ���
	public static final int LOWERUP = 1;	//�ͷ��߽������߷������һ��
	protected HashMap<Integer,ArrayList<Member>> mMap = null;
	protected int curTurn = 0;
	protected int maxScore = 0;
	private int mMembers = 0;
	public int vsRuleOfSameScoreGroup = NEIGHBORVS;	//ͬ����PK����
	public int vsRuleOfOddGroup = MAXVSLOWER;
	public double allowSameScorePercent = 0.1;	//Ϊ�˼ӿ�������������ֺ�ʣ��һ������ͬ���߿���С��Χ���������ⳡ�ι���

	public HashMap<Integer, ArrayList<Member>> getMap() {
		return mMap;
	}

	public SwissRule() {
		mMap = new HashMap<Integer,ArrayList<Member>>();		
	}
	protected ArrayList<Member> insertToList(ArrayList<Member> newlist,Member m ,int currentTurn,int times,int mode) {
		if(newlist.size() ==0) {
			newlist.add(m);
			return newlist;
		}
		int i = 0;
		for( i=0;i<newlist.size();i++) {
			if(m.isBiger(newlist.get(i),times,mode,currentTurn) == 1) {
				newlist.add(i,m);
				break;
			}
		}
		if(i == newlist.size()) {
			newlist.add(m);
		}
		return newlist;
	}
	
	public void resetMap(ArrayList<Member> memberlist,int turn,int times,int mode) {
		curTurn = turn;
		mMap.clear();
		this.mMembers = memberlist.size();
		for(int i=0;i<memberlist.size();i++) {
			ArrayList<Member> list = mMap.get(memberlist.get(i).bigscore);
			if(list == null ) list = new ArrayList<Member>();
			list = insertToList(list,memberlist.get(i),curTurn,times,mode);
			mMap.put(Integer.valueOf(memberlist.get(i).bigscore), list);
			this.maxScore = Math.max(this.maxScore, memberlist.get(i).bigscore);
//			printMap(turn);
		}
	}
	private Member[] getVsMember(Member m,ArrayList<Member> list) {
		if(! m.isDoubleVs()) return null;
		
		Member[] retArr = new Member[4];
		ArrayList<Member> notCo = new ArrayList<Member>();
		ArrayList<Member> notVs = new ArrayList<Member>();

		for(int i=0;i<list.size();i++) {
			Member tm = list.get(vsRuleOfSameScoreGroup == NEIGHBORVS?i:list.size()-1-i);
			if(m.inCoList(tm) == null) {
				notCo.add(tm);
			}
		}
		if(notCo.size()==0)  {
			return null;
		}
		for(int i=0;i<list.size() && notVs.size()<2;i++) {
			Member tm = list.get(vsRuleOfSameScoreGroup == NEIGHBORVS?i:list.size()-1-i);
			if(m.inVsList(tm) == null) {
				if(!notCo.contains(tm)) {
					notVs.add(tm);
				}else if(notCo.size()>1) {
					notVs.add(tm);
					notCo.remove(tm);
				}
			}
		}
		if(notVs.size()<2) {
			return null;
		}
		retArr[0] = m;
		retArr[1] = notCo.get(0);
		retArr[2] = notVs.get(0);
		retArr[3] = notVs.get(1);
		m.colist.add(notCo.get(0));
		notCo.get(0).colist.add(m);
		notVs.get(0).colist.add(notVs.get(1));
		notVs.get(1).colist.add(notVs.get(0));
		VsMember vs1 = new VsMember();
		VsMember vs2 = new VsMember();
		vs1.vsm = retArr[2];
		vs1.vsm1 = retArr[3];
		vs2.vsm = retArr[0];
		vs2.vsm1 = retArr[1];
		retArr[0].vslist.add(vs1);
		retArr[1].vslist.add(vs1);
		retArr[2].vslist.add(vs2);
		retArr[3].vslist.add(vs2);		
		return retArr;
	}
	public ArrayList<Member[]> getDoubleVsList() {
		ArrayList<Member[]> vsList = new ArrayList<Member[]>();
		//int turn = getTurnCount(memberCount);
		for(int i=this.maxScore;i>=0;i--) {	//ѭ����ͬ����������
			ArrayList<Member> curList = mMap.get(Integer.valueOf(i));
			if(curList == null || curList.size() == 0) continue;
			if(!curList.get(0).isDoubleVs()) {				
				return null;
			}
			ArrayList<Member> nextList = null;	
			for(int k=i-1;k>=0;k--) { 
				nextList = mMap.get(Integer.valueOf(k));
				if(nextList != null && nextList.size()>0) break;
			}
			int nextinsertpos = 0;
			while(curList.size()>0) {
				Member m1 = curList.get(0);					
				curList.remove(0);					
				//Ѱ����ӳ�Ա
				Member[] retArr = getVsMember(m1,curList);
				if(retArr == null) { //û����ͬ�����ҵ����ʵ���ԣ��ӵ���һ����ȥ
					if(nextList == null || nextList.size() == 0) {	//û����һ��ɴ��ֿ�
						Member[] mm = new Member[4];
						mm[0] = m1;
						mm[1] = mm[2] = mm[3] =null;	
						vsList.add(mm);
					}else{
						nextList.add(nextinsertpos,m1);//������һ�������ǰ��
						nextinsertpos++;
					}
				}else {
					vsList.add(retArr);
					curList.remove(retArr[1]);
					curList.remove(retArr[2]);
					curList.remove(retArr[3]);					
				}
			}
		}
		return vsList;
	}

	public ArrayList<Member[]> getSingleVsList() {
		ArrayList<Member[]> vsList = new ArrayList<Member[]>();
		//int turn = getTurnCount(memberCount);
		for(int i=this.maxScore;i>=0;i--) {	//ѭ����ͬ����������
			ArrayList<Member> curList = mMap.get(Integer.valueOf(i));
			if(curList == null || curList.size() == 0) continue;
			ArrayList<Member> nextList = null;	
			for(int k=i-1;k>=0;k--) { 
				nextList = mMap.get(Integer.valueOf(k));
				if(nextList != null && nextList.size()>0) break;
			}
			if(curList.size()%2 != 0) {	//���ԱΪ��������Ҫ����һ�鲹��һ������							
				Member firstMemberOfNextGroup = null;				
				if(nextList!= null && nextList.size() >= 1) {
					firstMemberOfNextGroup = nextList.get(0);
					nextList.remove(0);		//������˴Ӹ����Ƴ�����������һ��
				}
				if(firstMemberOfNextGroup != null) { //�ҵ��˲�����Ա��������������
					if(this.vsRuleOfSameScoreGroup == SwissRule.NEIGHBORVS 
							&& this.vsRuleOfOddGroup == SwissRule.MAXVSLOWER)	 {
						//ͬ��������pk�ҵͷֲ����������С����pk����������ڶ�λ
						curList.add(1,firstMemberOfNextGroup);
					}else if(this.vsRuleOfSameScoreGroup == SwissRule.MAXVSMIN 
							&& this.vsRuleOfOddGroup == SwissRule.MAXVSLOWER)	 {
						//ͬ������ߺ����pk�ҵͷֲ����������С����pk�������������
						curList.add(firstMemberOfNextGroup);
					}else if(this.vsRuleOfOddGroup == SwissRule.LOWERUP)	 {
						//�ͷֲ�������Ϊ�������һ��������������󣬲���ͬ�ֹ������
						curList.add(firstMemberOfNextGroup);
					}
				}
			}
			//�Ը�ͬ�������pk���
			while(curList.size()>0) {
				Member m1 = curList.get(0);
				curList.remove(0);
				//Ѱ��δ���ֹ��Ķ���
				Member m2 = null;
				int nextpos = 0;
				Member lower = null;	//Ϊ�˷�ֹm1ͬ���Ҳ����������ֶ�m1��lowerû�Թ���m1��vsLowerLuck!=0,��������»��ǻ����lower��m1,
				int lowerpos = 0;
				for(int v = 0;v<curList.size();v++) {
					nextpos = (this.vsRuleOfSameScoreGroup == NEIGHBORVS)?0+v:curList.size()-1-v;
					m2 = curList.get(nextpos);
					if(m1.inVsList(m2) == null) {
						if(m2.bigscore < m1.bigscore && m1.vsLowerLuck > 0) {	//�����ܷ���ͷ���
							lower = m2;
							lowerpos = nextpos;
							continue;
						}else {
							break;
						}
					}else {
						m2 = null;
					}
				}
				if(m2 == null && lower != null) {
					m2 = lower;
					m1.vsLowerLuck = 4;
					nextpos = lowerpos;
				}
				if(m2 != null) { 	//�ҵ��˶���
					curList.remove(nextpos);
					VsMember vm1 = new VsMember();
					vm1.vsm = m1;
					vm1.turn = curTurn;
					VsMember vm2 = new VsMember();
					vm2.vsm = m2;		
					vm2.turn = curTurn;
					m1.curVsm = vm2;
					m1.vslist.add(vm2);
					m2.curVsm = vm1;
					m2.vslist.add(vm1);
					Member[] arr = {m1,m2};
					m1.vsLowerLuck = Math.max(0, m1.vsLowerLuck - 1);					
					m2.vsLowerLuck = Math.max(0, m2.vsLowerLuck - 1);					
					vsList.add(arr);
				}else {			//�������ôһ�����,���ͬ���Ҳ������ӵ���һ��ȥ?					
					if(nextList != null && nextList.size()>0) {
						nextList.add(0,m1);
					}else {			
						VsMember vm = new VsMember();
						vm.turn = curTurn;
						m1.curVsm = vm;					
						m1.vsLowerLuck = Math.max(0, m1.vsLowerLuck - 1);
						Member[] arr = {m1,null};
						vsList.add(arr);
					}
				}
			}
		}
		return vsList;		
	}
	public boolean isFullOrder(int times,int mode , int currentTurn ) {
		double sameCount = 0;
		ArrayList<Member> samelist = new ArrayList<Member>();
		for(Integer i:mMap.keySet()) {
			ArrayList<Member> ms = mMap.get(i);
			if(ms.size()==0) continue;
			Member m0 = ms.get(0);
			for(int k=1;k<ms.size();k++) {
				Member m1 = ms.get(k);
				int issame = m0.isBiger(m1,times,mode,currentTurn);
				if(issame == 0) {
					if(!samelist.contains(m0))
						samelist.add(m0);
					if(!samelist.contains(m1));
						samelist.add(m1);
				}
				m0 = m1;
			}
		}
		sameCount = samelist.size();
		System.out.println("Same score count is "+ sameCount+"," +sameCount/((double)mMembers));
		if(sameCount/((double)mMembers) > this.allowSameScorePercent) {
			return false;
		}
		return true;
	}
	public static int getMinTurnCount(int member_count) {
		//2x-1<= y<=2x
		int x = (int) MathEx.log2((double)member_count);
		return x;
	}
	public void printVsList(ArrayList<Member[]> vsList,int turn) {
		System.out.println("The turn "+turn+" vs list:");
		for(int i=0;i<vsList.size();i++) {
			Member[] vs = vsList.get(i);
			String ss = "[";
			for(int k=0;k<vs.length;k++) {
				ss+=(vs[k]!=null?vs[k].toString():"N")+((k==vs.length-1)?"],":",");
			}
			System.out.print(ss);
		}
		System.out.print("\n\n");
		
	}
	/*private void test(ArrayList<Member> list,boolean isDouble) {
		int members = list.size();
		ArrayList<Member[]> vsList = null;
		int turn  = getMinTurnCount(members);
		System.out.println("the turncount is "+turn+"\n---------------------");
		for(int i=0;i<members-2;i++) {			
			resetMap(list,i);
			printMap(i);
			if(i>turn-1 && isFullOrder()) {	//����Ƿ����ȫ���ų�ʤ�������ok��ֹͣ����
				break;
			}
			vsList =isDouble? getDoubleVsList():getSingleVsList();			
			printVsList(vsList,i);
			list =isDouble? reloadDoubleList(vsList):reloadSingleList(vsList);	//ģ��ʤ��
		}
	}*/
	/*public void SingleTest(int members) {
		ArrayList<Member> list = new ArrayList<Member>();
		for(int i=0;i<members;i++) {
			SingleMember m = new SingleMember();
			m.id = i+1;
			list.add(m);		
		}
		test(list,false);
	}*/
	/*public void DoubleTest(int members) {
		ArrayList<Member> list = new ArrayList<Member>();
		for(int i=0;i<members;i++) {
			DoubleMember m = new DoubleMember();
			m.id = i+1;
			list.add(m);		
		}
		test(list,true);
	}*/
	public HashMap<Integer,ArrayList<Member>> printMap(int turn) {

		System.out.println("The turn "+turn+" score map:");
		for(Integer i:mMap.keySet()) {
			ArrayList<Member> ms = mMap.get(i);
			System.out.printf("%d:[",i);
			for(int k=0;k<ms.size();k++) {
				System.out.printf("%s,",ms.get(k));
			}
			System.out.printf("]\n");
		}


		/*for(int i = 0 ;i< mMap.keySet().size();i++){
			ArrayList<Member> ms = mMap.get(i);
			System.out.printf("%d:[",i);
			if (ms!=null){
				for(int k=0;k<ms.size();k++) {
					System.out.printf("%s,",ms.get(k));
				}
			}

			System.out.printf("]\n");
		}*/
		return mMap;
	}
	public  ArrayList<Member> reloadDoubleList(ArrayList<Member[]> vsList) {
		 ArrayList<Member> list = new  ArrayList<Member>();
		for(int k = 0;k<vsList.size();k++) {
			Member[] arr = vsList.get(k);
			Member m1 = arr[0];
			Member m2 = arr[1];
			Member v1 = arr[2];
			Member v2 = arr[3];
			if(m2 == null) {
				if(!m1.hasVsBye) {
					m1.hasVsBye = true;
					m1.bigscore++;
					m1.totalscore += m1.bigscore;
				}
				list.add(m1);
			}else {	//�������ʤ��
//				int rnd = MathEx.randnumber(0,2);
//				int rndWinBalls = MathEx.randnumber(2,18);

				int rnd = -1;
				int rndWinBalls = 0;
				if (m1.currenscore >= v1.currenscore){
					rnd = 0;
					rndWinBalls = m1.currenscore;

				}else {
					rnd = 1;
					rndWinBalls = v1.currenscore;
				}
				m1.bigscore += (rnd == 0)?1:0;
				m1.totalscore += (rnd == 0)?m1.bigscore:0;
				m1.smallscore += (rnd == 0)?v1.bigscore+v2.bigscore:0;	
				m1.winballs += (rnd == 0)?rndWinBalls:0;
				
				m2.bigscore += (rnd == 0)?1:0;				
				m2.totalscore += (rnd == 0)?m2.bigscore:0;
				m2.smallscore += (rnd == 0)?(v1.bigscore+v2.bigscore):0;
				m2.winballs += (rnd == 0)?rndWinBalls:0;
				
				m1.vslist.get(m1.vslist.size()-1).winscore = (rnd == 0)?0:1;
				m1.vslist.get(m1.vslist.size()-1).winballs = (rnd == 0)?rndWinBalls:0;
				m2.vslist.get(m2.vslist.size()-1).winscore = (rnd == 0)?0:1;
				m2.vslist.get(m2.vslist.size()-1).winballs = (rnd == 0)?rndWinBalls:0;
				
				
				v1.bigscore += (rnd > 0)?1:0;
				v1.totalscore += (rnd >0)?v1.bigscore:0;
				v1.smallscore += (rnd >0)?(m1.bigscore+m2.bigscore):0;
				v1.winballs += (rnd >0)?rndWinBalls:0;
				
				v2.bigscore += (rnd > 0)?1:0;
				v2.totalscore += (rnd >0)?v2.bigscore:0;
				v2.smallscore += (rnd >0)?(m1.bigscore+m2.bigscore):0;		
				v2.winballs += (rnd >0)?rndWinBalls:0;
				
				v1.vslist.get(v1.vslist.size()-1).winscore = (rnd > 0)?0:1;
				v1.vslist.get(v1.vslist.size()-1).winballs = (rnd > 0)?rndWinBalls:0;
				v2.vslist.get(v2.vslist.size()-1).winscore = (rnd > 0)?0:1;
				v2.vslist.get(v2.vslist.size()-1).winballs = (rnd > 0)?rndWinBalls:0;
				
				list.add(m1);
				list.add(m2);
				list.add(v1);
				list.add(v2);
			}
		}
		return list;
	}
	public  ArrayList<Member> reloadSingleList(ArrayList<Member[]> vsList) {
		 ArrayList<Member> list = new  ArrayList<Member>();
		for(int k = 0;k<vsList.size();k++) {
			Member[] arr = vsList.get(k);
			Member m1 = arr[0];
			Member m2 = arr[1];
			if(m2 == null) {
				if(!m1.hasVsBye) {
					m1.hasVsBye = true;
					m1.bigscore++;
					m1.totalscore += m1.bigscore;
				}
				list.add(m1);
			}else {	//判断胜负
//				int rnd = MathEx.randnumber(0,2);
//				int rndVictorPoints = MathEx.randnumber(2,18);

				int rnd = -1;
				int rndVictorPoints = 0;
				if (m1.currenscore >= m2.currenscore){
					rnd = 0;
					rndVictorPoints = m1.currenscore;
				}else {
					rnd = 1;
					rndVictorPoints = m1.currenscore;
				}
				m1.bigscore += (rnd == 0)?1:0;
				m1.totalscore += (rnd == 0)?m1.bigscore:0;
				m1.smallscore += (rnd == 0)?m2.bigscore:0;
				m1.winballs += (rnd==0)?rndVictorPoints:0;
				m1.vslist.get(m1.vslist.size()-1).winscore = (rnd == 0)?0:1;
				m1.vslist.get(m1.vslist.size()-1).winballs = (rnd == 0)?rndVictorPoints:0;
				
				m2.bigscore += (rnd > 0)?1:0;
				m2.totalscore += (rnd >0)?m2.bigscore:0;
				m2.smallscore += (rnd >0)?m1.bigscore:0;
				m2.winballs += (rnd>0)?rndVictorPoints:0;
				m2.vslist.get(m2.vslist.size()-1).winscore = (rnd >0)?0:1;
				m2.vslist.get(m2.vslist.size()-1).winballs = (rnd >0)?rndVictorPoints:0;
				
				list.add(m1);
				list.add(m2);
			}
		}
		return list;
	}
}
