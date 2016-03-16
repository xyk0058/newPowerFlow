package com.dhcc.Global;

import com.dhcc.model.Branch;
import com.dhcc.model.Bus;
import com.dhcc.model.Info;
import com.dhcc.model.PVNode;
import com.dhcc.model.U_Type;
import com.dhcc.model.Yii;
import com.dhcc.model.Yij;

public class Variable {
	
	public static int PQ = 1;
	public static int PV = 2;
	public static int REF = 3;
	private static Info pf_info;
	private static Branch[] branch;		//Nb
	private static Bus[] generator;		//Ng
	private static Bus[] load;			//Nl
	private static PVNode[] pvNode;
	private static Yii[] Yii;
	private static Yii[] Yii1;
	private static Yij[] Yij;
	private static Yij[] Yij1;
	private static int[] NYseq;
	private static int[] NYsum;
	private static int[] NUsum1;
	private static double[] D1;
	private static U_Type[] U1;
	private static int[] NUsum2;
	private static double[] D2;
	private static U_Type[] U2;
	
	public static int[] getNUsum1() {
		return NUsum1;
	}
	public static void setNUsum1(int[] nUsum1) {
		NUsum1 = nUsum1;
	}
	public static double[] getD1() {
		return D1;
	}
	public static void setD1(double[] d1) {
		D1 = d1;
	}
	public static U_Type[] getU1() {
		return U1;
	}
	public static void setU1(U_Type[] u1) {
		U1 = u1;
	}
	public static int[] getNUsum2() {
		return NUsum2;
	}
	public static void setNUsum2(int[] nUsum2) {
		NUsum2 = nUsum2;
	}
	public static double[] getD2() {
		return D2;
	}
	public static void setD2(double[] d2) {
		D2 = d2;
	}
	public static U_Type[] getU2() {
		return U2;
	}
	public static void setU2(U_Type[] u2) {
		U2 = u2;
	}
	public static Yii[] getYii1() {
		return Yii1;
	}
	public static void setYii1(Yii[] yii1) {
		Yii1 = yii1;
	}
	public static Yij[] getYij1() {
		return Yij1;
	}
	public static void setYij1(Yij[] yij1) {
		Yij1 = yij1;
	}
	public static int[] getNYseq() {
		return NYseq;
	}
	public static void setNYseq(int[] nYseq) {
		NYseq = nYseq;
	}
	public static int[] getNYsum() {
		return NYsum;
	}
	public static void setNYsum(int[] nYsum) {
		NYsum = nYsum;
	}
	public static Yii[] getYii() {
		return Yii;
	}
	public static void setYii(Yii[] yii) {
		Yii = yii;
	}
	public static Yij[] getYij() {
		return Yij;
	}
	public static void setYij(Yij[] yij) {
		Yij = yij;
	}
	public static Info getPf_info() {
		return pf_info;
	}
	public static void setPf_info(Info pf_info) {
		Variable.pf_info = pf_info;
	}
	public static Branch[] getBranch() {
		return branch;
	}
	public static void setBranch(Branch[] branch) {
		Variable.branch = branch;
	}
	public static Bus[] getGenerator() {
		return generator;
	}
	public static void setGenerator(Bus[] generator) {
		Variable.generator = generator;
	}
	public static Bus[] getLoad() {
		return load;
	}
	public static void setLoad(Bus[] load) {
		Variable.load = load;
	}
	public static PVNode[] getPvNode() {
		return pvNode;
	}
	public static void setPvNode(PVNode[] pvNode) {
		Variable.pvNode = pvNode;
	}
	
}
