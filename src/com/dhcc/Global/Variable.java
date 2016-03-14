package com.dhcc.Global;

import com.dhcc.model.Branch;
import com.dhcc.model.Bus;
import com.dhcc.model.Info;

public class Variable {
	private static Info pf_info;
	private static Branch[] branch;		//Nb
	private static Bus[] generator;		//Ng
	private static Bus[] load;			//Nl
	
	
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
	
}
