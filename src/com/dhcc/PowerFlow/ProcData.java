package com.dhcc.PowerFlow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.dhcc.Global.Variable;
import com.dhcc.model.Branch;
import com.dhcc.model.Bus;
import com.dhcc.model.Info;
import com.dhcc.model.MPC;
import com.dhcc.model.PVNode;
import com.dhcc.model.Yii;
import com.dhcc.model.Yij;

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
			int Nl = nbus - ngen;
			int Npv = 0;
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
				if(bus[i][1] == Variable.PV) ++Npv;
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
			
			Info pf_info = new Info(N, Nb, Ng, Nl, V0, Npv, 0.00001);
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
		PVNode[] pvNode = new PVNode[info.getNpv()];
		
		ArrayList<Integer> genIdx = new ArrayList<Integer>();
		
		double[][] brch = _mpc.getBranch();
		double[][] gen = _mpc.getGen();
		double[][] bus = _mpc.getBus();
		
		//发电机
		for (int i = 0; i < info.getNg(); ++i) {
			generator[i] = new Bus();
			generator[i].setIndex((int) gen[i][0]);
			generator[i].setP(Math.abs(gen[i][1]));
			generator[i].setQ(Math.abs(gen[i][2]));
			generator[i].setV(Math.abs(gen[i][5]));
			genIdx.add((int) gen[i][0]);
		}
		//支路
		for (int i = 0; i < info.getNb(); ++i) {
			branch[i] = new Branch();
			branch[i].setFrom((int) brch[i][0]);
			branch[i].setTo((int) brch[i][1]);
			branch[i].setR(brch[i][2]);
			branch[i].setX(brch[i][3]);
			//电容容纳 or 对地导纳
			branch[i].setY0(brch[i][4]);
		}
		

		//负荷
		int j = 0;
		for (int i = 0; i < info.getN(); ++i) {
			if (genIdx.contains((int) bus[i][0])) continue;
			if ((int)bus[i][1] == Variable.REF) continue;
			//System.out.println(load.length + " " + j + " " + bus[i][0]);
			load[j] = new Bus();
			load[j].setIndex((int) bus[i][0]);
			//取负值
			load[j].setP(-Math.abs(bus[i][2]));
			load[j].setQ(-Math.abs(bus[i][3]));
			if(bus[i][1] == Variable.PQ) {
				load[j].setV(bus[i][7]);
			} else if (bus[i][1] == Variable.PV) {
				load[j].setV(-Math.abs(bus[i][7]));
			}
			j++;
		}
		
		//pv节点
		j = 0;
		for (int i = 0; i < info.getN(); ++i) {
			if(bus[i][1] == Variable.PV) {
				pvNode[j] = new PVNode();
				pvNode[j].setIndex((int) bus[i][0]);
				pvNode[j].setV(Math.abs(bus[i][7]));
				j++;
			}
		}
		
		Variable.setBranch(branch);
		Variable.setGenerator(generator);
		Variable.setLoad(load);
		Variable.setPvNode(pvNode);
		
	}
	
	public void calcY() {
		Info info = Variable.getPf_info();
		Yii[] yii = new Yii[info.getN()];
		Yii[] yii1 = new Yii[info.getN()];
		Yij[] yij = new Yij[info.getNb()];
		Yij[] yij1 = new Yij[info.getNb()];
		int[] NYseq = new int[info.getN()];
		int[] NYsum = new int[info.getN()];
		
		double R,X,YK,Gij,Bij,Zmag2,b_ij;
		
		Branch[] branch = Variable.getBranch();
		
		System.out.println(info.getNb());
		
		for (int i = 0; i < info.getN(); ++i) {
			yii[i] = new Yii();
			yii1[i] = new Yii();
			yii[i].setB(0);yii[i].setG(0);
			yii1[i].setB(0);yii1[i].setG(0);
			NYsum[i] = 0;
		}
		
		for (int i = 0; i < info.getNb(); ++i) {
			yij[i] = new Yij();
			yij1[i] = new Yij();
			yij[i].setB(0);yij[i].setG(0);
			yij1[i].setB(0);yij1[i].setG(0);
		}
		
		for (int n = 0; n < info.getNb(); ++n) {
			//TODO
			int i = Math.abs(branch[n].getFrom()) - 1;
			int j = Math.abs(branch[n].getTo()) - 1;
			R = branch[n].getR();
			X = branch[n].getX();
			YK = branch[n].getY0();
			Zmag2 = R * R + X * X;
			Gij = R / Zmag2;
			Bij = X / Zmag2;
			b_ij = -1.0 / X;
			
			System.out.println(yij[n]);
			
			if (branch[n].getFrom() < 0 || branch[n].getTo() < 0) {
				yij[n].setG(-Gij / YK);
				yij[n].setB(-Bij / YK);
				yij1[n].setG(0);
				yij1[n].setB(-b_ij / YK);
			} else {
				yij[n].setG(-Gij);
				yij[n].setB(-Bij);
				yij1[n].setG(0);
				yij1[n].setB(-b_ij);
			}
			yij[n].setJ(j);
			yij1[n].setJ(j);
			if(branch[n].getFrom() <0 || branch[n].getTo() < 0) {
				yii[i].setG(yii[i].getG() + Gij/YK);
				yii[i].setB(yii[i].getB() + Bij/YK);
				yii[j].setG(yii[j].getG() + Gij/YK);
				yii[j].setB(yii[j].getB() + Bij/YK);
				
				yii1[i].setB(yii1[i].getB() + b_ij/YK);
				yii1[j].setB(yii1[j].getB() + b_ij/YK);
			} else {
				yii[i].setG(yii[i].getG() + Bij);
				yii[i].setB(yii[i].getB() + Bij);
				yii[j].setG(yii[j].getG() + Bij);
				yii[j].setB(yii[j].getB() + Bij);
				
				yii1[i].setB(yii1[i].getB() + b_ij);
				yii1[j].setB(yii1[j].getB() + b_ij);
			}
			NYsum[i] = NYsum[i] + 1;
		}
		
		NYseq[1] = 1;
		for (int i = 1; i < info.getN() - 1; ++i) {
			NYseq[i + 1] = NYseq[i] + NYsum[i];
		}
		
		for (int n = 1; n < info.getNb(); ++n) {
			int i = branch[n].getFrom() - 1;
			int j = branch[n].getTo() - 1;
			YK = branch[n].getY0();
			if (i < 0 || j < 0) {
				if (i < 0) {
					j = Math.abs(i);
					Gij = yij[n].getG();
					Bij = yij[n].getB();
					
					b_ij = yij1[n].getB();
					
					yii[i].setG(yii[i].getG() + (1.0 - 1.0 / YK) * Gij);
					yii[i].setB(yii[i].getB() + (1.0 - 1.0 / YK) * Bij);
					
					yii1[i].setB(yii1[i].getB() + (1.0 - 1.0 / YK) * b_ij);
					
					yii[j].setG(yii[i].getG() + (1.0 - YK) * Gij);
					yii[j].setB(yii[i].getB() + (1.0 - YK) * Bij);
					
					yii1[j].setB(yii1[j].getB() + (1.0 - YK) * b_ij);
				} else {
					j = Math.abs(j);
					Gij = yij[n].getG();
					Bij = yij[n].getB();
					
					b_ij = yij1[n].getB();
					
					yii[i].setG(yii[i].getG() + (1.0 - 1.0 / YK) * Gij);
					yii[i].setB(yii[i].getB() + (1.0 - 1.0 / YK) * Bij);
					
					yii1[i].setB(yii1[i].getB() + (1.0 - 1.0 / YK) * b_ij);
					
					yii[j].setG(yii[i].getG() + (1.0 - YK) * Gij);
					yii[j].setB(yii[i].getB() + (1.0 - YK) * Bij);
					
					yii1[j].setB(yii1[j].getB() + (1.0 - YK) * b_ij);
				}
			} else {
				Bij = YK / 2.0;
				b_ij = YK / 2.0;
				yii[i].setB(yii[i].getB() + Bij);
				yii[j].setB(yii[j].getB() + Bij);
				
				yii1[i].setB(yii1[i].getB() + b_ij);
				yii1[j].setB(yii1[j].getB() + b_ij);
			}
		}
		
		Variable.setNYseq(NYseq);
		Variable.setNYsum(NYsum);
		Variable.setYii(yii);
		Variable.setYii1(yii1);
		Variable.setYij(yij);
		Variable.setYij1(yij1);
	}
	
	
	public static void main(String[] args) {
		ProcData pd = new ProcData();
		pd.ReadData("/Users/xyk0058/Git/newPowerFlow/src/com/dhcc/casedata/case14.txt");
		pd.InitData();
		pd.calcY();
	}
}
