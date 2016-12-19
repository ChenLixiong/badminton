package com.ymq.badminton_rank.badminton;

import java.util.Random;

public class MathEx {
	public static int log(int n){
	    int a=0;
	    while(n>Math.pow(2,a)){	        
	        a++;
	    }
	    //if(Math.pow(2, a)>n) a--;
	    return a;
	}

	public static boolean isPowerOfTwo(int val) {
		return (val & -val) == val;
	}
	public static int randnumber(int b,int e) {
		Random rand = new Random();
		return b+rand.nextInt(e-b);
	}
	public static double logarthm(double value,double base) {
		return Math.log(value)/Math.log(base);
	}
	public static double log2(double value) {
		return logarthm(value,2.0);
	}
	public static double log10(double value) {
		return logarthm(value,10.0);
	}
}
