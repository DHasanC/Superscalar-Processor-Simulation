import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.lang.Math;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class sim{

	public static void main(String[] args) throws IOException{

		int S = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);

		ArrayList<Instruction> ROB = new ArrayList<>();
		ArrayList<Integer> dispatchList = new ArrayList<>();
		ArrayList<Integer> issueList = new ArrayList<>();
		ArrayList<Integer> executeList = new ArrayList<>();
		int[][] registerFile = new int[128][2];
		for(int i = 0; i < registerFile.length; i++) {
			registerFile[i][0] = 1; 
			registerFile[i][1] = -1;
		} 
		int[] latency = new int[3];
		latency[0] = 1; latency[1] = 2; latency[2] = 5;
		int i = 0;
		int cycle = 0;
		File file = new File(args[2]);
		Scanner sc = new Scanner(file);
		int dispatchEntry = 0, issueEntry = 0, ROBhead = 0;
			
		do{	

			while(!ROB.isEmpty() && ROB.get(0).getStage() == 4) {
				int dispatchDuration = ROB.get(0).getIssueEntry() - ROB.get(0).getDispatchEntry();
				int issueDuration = ROB.get(0).getExecutionEntry() - ROB.get(0).getIssueEntry();
				System.out.println(ROB.get(0).getTag() + " "
				+ "fu{" + ROB.get(0).getOp() + "}" + " "
				+ "src{" + ROB.get(0).getSrc1() + "," + ROB.get(0).getSrc2() + "}" + " " 
				+ "dst{" + ROB.get(0).getDest() + "}" + " " 
				+ "IF{" + ROB.get(0).getFetchEntry() + ",1" + "}" + " "
				+ "ID{" + ROB.get(0).getDispatchEntry() + "," + dispatchDuration +  "}" + " "
				+ "IS{" + ROB.get(0).getIssueEntry() + "," + issueDuration  +  "}" + " "
				+ "EX{" + ROB.get(0).getExecutionEntry() + "," + latency[ROB.get(0).getOp()] + "}" + " "
				+ "WB{" + ROB.get(0).getWriteBackEntry() + ",1" + "}");
				if(ROB.get(0).getTag() == 9999) {
					double IPC = 10000.00 / cycle;
					System.out.println("number of instructions = 10000");
					System.out.println("number of cycles       = " + cycle);
					System.out.format("IPC                    = " + "%.5f", IPC);
				}
 				ROB.remove(0);
			}
			
			//execute

			for(int e = 0; e < executeList.size(); e++){
				//endWakeUp = false;
				for(Instruction a : ROB){
					if(executeList.size() > 0){
						if(e < executeList.size() && a.getTag() == executeList.get(e)){
							if(a.getAge() == latency[a.getOp()] - 1){
								a.setStage(4);
								a.setWriteBackEntry(cycle);
								if(a.getDest() != -1){
									if(registerFile[a.getDest()][1] == a.getTag()) {
										registerFile[a.getDest()][0] = 1;
										registerFile[a.getDest()][1] = -1;
									}  
									for(int u = 0; u < issueList.size(); u++){
										for(Instruction b : ROB){
											if(b.getTag() == issueList.get(u)){
												if(b.getSrc1() == a.getDest()) b.setSrc1Ready();
												if(b.getSrc2() == a.getDest()) b.setSrc2Ready();
											}
										}
									}
										
								}
								executeList.remove(e);
							}
							else a.ageIncrement();
						}
					}
				}
					
			}
					
			//issue

			int issueBW = 0;
			Collections.sort(issueList);
			for(int s = 0; s < issueList.size(); s++){
				for(Instruction a : ROB){
					if(issueBW < N + 1 && issueList.size() > 0){
						if(s < issueList.size() && a.getTag() == issueList.get(s)){
							if((a.getSrc1() == -1 || a.getSrc1Validity() == true || a.getSrc1Ready() == true) && (a.getSrc2() == -1 ||  a.getSrc2Validity() == true || a.getSrc2Ready() == true)){
								executeList.add(issueList.get(s));
								//System.out.println(issueList.get(s) + "HEREWE HHHHHHHHHHHHH");
								a.setStage(3);
								a.setExecutionEntry(cycle);
								issueEntry--;
								a.ageIncrement(); // latency timer starts with initial age = 0;
								issueBW++;
								issueList.remove(s);
								//System.out.println(a.getAge());
							}
						}
					}
				}
			}
			

			//dispatch

			Collections.sort(dispatchList);
			for(int d = 0; d < dispatchList.size(); d++) {
				for(Instruction a : ROB){
					if(d < dispatchList.size() && a.getTag() == dispatchList.get(d)){
						 if(a.getStage() == 1 && issueEntry < S){
							issueList.add(dispatchList.get(d));
							a.setStage(2);
							a.setIssueEntry(cycle);
							issueEntry++;
							dispatchEntry--;
							if(a.getSrc1() != -1){
								if(registerFile[a.getSrc1()][0] == 0) a.setSrc1Invalid();
							}
							if(a.getSrc2() != -1){
								if(registerFile[a.getSrc2()][0] == 0) a.setSrc2Invalid();
							}
							if(a.getDest() != -1) {
								registerFile[a.getDest()][1] = a.getTag(); 
								if(registerFile[a.getDest()][0] == 1) registerFile[a.getDest()][0] = 0;
							}
							dispatchList.remove(d);
						}
						else if(a.getStage() == 0) {
							a.setStage(1); 
							a.setDispatchEntry(cycle);
						}	
					}
				}
			}	

			//for(int x : dispatchList) System.out.println(cycle + " " + x);		
			
			//fetch
	
			int fetchBW = 0;
			while(sc.hasNext() && fetchBW < N && dispatchEntry < 2 * N){
					String line = sc.nextLine();
        				String[] words = line.split("\\s");
					Instruction a = new Instruction();
					a.setTag(i); a.setSrc1(Integer.parseInt(words[3])); 
					a.setSrc2(Integer.parseInt(words[4])); 
					a.setDest(Integer.parseInt(words[2])); 
					a.setOp(Integer.parseInt(words[1])); 
					a.setStage(0); 
					a.placeInROB();
					//System.out.println(i + " " + a.getDest());
					ROB.add(a);
					a.setFetchEntry(cycle);
					dispatchList.add(i);
					dispatchEntry++;
					i++;
					fetchBW++; 
			}
			//System.out.println(cycle + "th cycle");
			cycle++;	
			//System.out.println(cycle + "th cycle);
			//for(Instruction a : ROB) System.out.println(a.getTag() + " " + a.getStage());
		} while(!ROB.isEmpty());
		
		
	}
}






		

		
		