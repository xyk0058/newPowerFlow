package com.dhcc.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.dhcc.Global.Variable;

public class ProcData {
	private MPC _mpc;
	
	public void ReadData(String filename) {
		
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(instrr);
			String row = null;
			String[] rowdata = null;
			
			row = br.readLine();int nbus = Integer.parseInt(row);
			row = br.readLine();int ngen = Integer.parseInt(row);
			row = br.readLine();int nbranch = Integer.parseInt(row);
			
			int N = nbus;
			int Nb = nbranch;
			int Ng = ngen;
			int Nl = 0;
			int V0 = 0;
			
			_mpc = new MPC(nbus, ngen, nbranch);

			double[][] bus = _mpc.getBus();
			//System.out.println("lanlan");
			for (int i=0; i<nbus; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					bus[i][j] = Double.parseDouble(rowdata[j]);
				}
				V0 += Math.abs(bus[i][7]);
			}
			V0 = V0 / nbus;
			
			double[][] gen = _mpc.getGen();
			for (int i=0; i<ngen; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					gen[i][j] = Double.parseDouble(rowdata[j]);
				}
			}
			double[][] branch = _mpc.getBranch();
			for (int i=0; i<nbranch; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				//System.out.println(row);
				for (int j=0; j<rowdata.length; ++j) {
					branch[i][j] = Double.parseDouble(rowdata[j]);
				}
			}
			
			
			_mpc.setBus(bus);
			_mpc.setBranch(branch);
			_mpc.setGen(gen);
			
			Info pf_info = new Info(N, Nb, Ng, Nl, V0, 0.00001);
			Variable.setPf_info(pf_info);
			
			
			br.close();
			instrr.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	public void InitData() {
		Info info = Variable.getPf_info();
		Branch[] branch = new Branch[info.getNb()];
		Bus[] generator = new Bus[info.getNg()];
		Bus[] load = new Bus[info.getNl()];
		
		ArrayList<Integer> genIdx = new ArrayList<Integer>();
		
		double[][] brch = _mpc.getBranch();
		double[][] gen = _mpc.getBranch();
		double[][] bus = _mpc.getBus();
		
		//发电机
		for (int i = 0; i < info.getNg(); ++i) {
			generator[i].setIndex((int) gen[i][0]);
			generator[i].setP(Math.abs(gen[i][1]));
			generator[i].setQ(Math.abs(gen[i][2]));
			generator[i].setV(Math.abs(gen[i][5]));
			genIdx.add((int) gen[i][0]);
		}
		//支路
		for (int i = 0; i < info.getNb(); ++i) {
			branch[i].setFrom((int) brch[i][0]);
			branch[i].setTo((int) brch[i][1]);
			branch[i].setR(brch[i][2]);
			branch[i].setX(brch[i][3]);
			branch[i].setY0(brch[i][4]);
		}
		//负荷
		int j = 0;
		for (int i = 0; i < info.getN(); ++i) {
			if (genIdx.contains((int) bus[i][0])) continue;
			load[j].setIndex((int) bus[i][0]);
			load[j].setP(-Math.abs(bus[i][2]));
			load[j].setQ(-Math.abs(bus[i][3]));
			load[j].setV(bus[i][7]);
		}
		//
	}
	
	public static void main(String[] args) {
		ProcData pd = new ProcData();
		pd.ReadData("/Users/xyk0058/Git/newPowerFlow/src/com/dhcc/casedata/case14.txt");
		pd.InitData();
	}
}
