
//blueprint of an instruction
//with all its attributes
//and getters and setters

public class Instruction {

		int tag = -1; 
		int src1 = -99;
		int src2 = -99;
		int dest = -99;
		int op = -1;
		int stage = -1; 
		int age = -1;
		boolean inROB = false;
		boolean src1Ready = false;
		boolean src2Ready = false;
		boolean src1Valid = true;
		boolean src2Valid = true;
		int fetchEntry = -1;
		int dispatchEntry = -1;
		int issueEntry = -1;
		int executionEntry = -1;
		int writeBackEntry = -1;

		public void setFetchEntry(int a){
			this.fetchEntry = a;
		}
		public void setDispatchEntry(int a){
			this.dispatchEntry = a;
		}
		public void setIssueEntry(int a){
			this.issueEntry = a;
		}
		public void setExecutionEntry(int a){
			this.executionEntry = a;
		}
		public void setWriteBackEntry(int a){
			this.writeBackEntry = a;
		}

		public int getFetchEntry(){
			return this.fetchEntry;
		}
		public int getDispatchEntry(){
			return this.dispatchEntry;
		}
		public int getIssueEntry(){
			return this.issueEntry;
		}
		public int getExecutionEntry(){
			return this.executionEntry;
		}
		public int getWriteBackEntry(){
			return this.writeBackEntry;
		}

		
		public void setTag(int tag){		
			this.tag = tag;
		}
		
		public void setSrc1(int src1){		
			this.src1 = src1;
		}
		
		public void setSrc2(int src2){		
			this.src2 = src2;
		}

		public void setDest(int dest){		
			this.dest = dest;
		}

		public void setOp(int op){		
			this.op = op;
		}

		public void setStage(int stage){
			this.stage = stage;
		}
		public void placeInROB(){
			this.inROB = true;
		}

		public void removeFromROB(){
			this.inROB = false;
		}
		public void ageIncrement(){
			this.age++;
		}
		public int getStage(){
			return this.stage;
		}

		public int getAge(){
			return this.age;
		}

		public int getSrc1(){		
			return this.src1;
		}
		
		public int getSrc2(){		
			return this.src2;
		}

		public int getDest(){		
			return this.dest;
		}

		public int getOp(){		
			return this.op;
		}
		public int getTag(){		
			return this.tag;
		}

		public void setSrc1Ready(){
			this.src1Ready = true;
		}

		public void setSrc2Ready(){
			this.src2Ready = true;
		}
		public boolean getSrc1Ready(){
			return this.src1Ready;
		}

		public boolean getSrc2Ready(){
			return this.src2Ready;
		}
		public void setSrc1Invalid(){
			this.src1Valid = false;
		}
		public void setSrc2Invalid(){
			this.src2Valid = false;
		}

		public boolean getSrc1Validity(){
			return this.src1Valid;
		}
		public boolean getSrc2Validity(){
			return this.src2Valid;
		}
	
	}