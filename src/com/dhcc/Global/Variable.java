package com.dhcc.Global;

import com.dhcc.model.Branch;
import com.dhcc.model.Bus;
import com.dhcc.model.Info;
import com.dhcc.model.PVNode;
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
	private static Yij[] Yij;
	private static int[] NYseq;
	private static int[] NYsum;
	
	
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
