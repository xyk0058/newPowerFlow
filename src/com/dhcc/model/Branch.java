package com.dhcc.model;

public class Branch {
	
	//输电线路端点
	private int from;
	//输电线路端点
	private int to;
	//输电线路电阻
	private double R;
	//输电线路电抗
	private double X;
	//输电线路充电电容的容纳
	private double Y0;
	
//	public Branch(){};
	public Branch(int from, int to, double R, double X, double Y0) {
		this.from = from;
		this.to = to;
		this.R = R;
		this.X = X;
		this.Y0 = Y0;
	}
	
	public int getFrom() {
		return from;
	}
	public int getTo() {
		return to;
	}
	public double getR() {
		return R;
	}
	public double getX() {
		return X;
	}
	public double getY0() {
		return Y0;
	}
	
}
