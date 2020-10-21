import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PriceManager {
	String UserName;
	int price;
	double[] time = new double[3];
	double[] D = new double[3];
	double[] age = new double[2];
	double[] sit = new double[3];
	char[] priceArray = new char[45]; //price array
	
	String movieInform;
	double totalPrice=0;
	int timeI; // only 0,1,2
	int DI;    // only 0,1,2
	int[][] sitInform; //int [sit][age]
	
	public PriceManager(String UserName, String movieInform,int timeI, int DI,int[][] sitInform) {
		this.UserName = UserName;
		this.movieInform = movieInform;
		this.timeI = timeI;
		this.DI = DI;
		this.sitInform = sitInform;
	}
	
	private void FileRead() { // price file read and save in array
		File price_file = new File("price.txt");
		try {
			FileReader fileR = new FileReader(price_file);
			fileR.read(priceArray);
			fileR.close();
		}catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void priceSort() { //save price inform
		FileRead();
		String data = String.valueOf(priceArray);
		String[] array = data.split(",");
		for(int i=0; i<5;i++) {
			if(i==0) price = Integer.parseInt(array[0]);
			else {
				String[] temp = array[i].split("/");
				if(i==1) for(int a=0;a<time.length;a++) time[a] = Double.parseDouble(temp[a]);
				else if(i==2) for(int a=0;a<D.length;a++) D[a] = Double.parseDouble(temp[a]);
				else if(i==3) for(int a=0;a<age.length;a++) age[a] = Double.parseDouble(temp[a]);
				else if(i==4) for(int a=0;a<sit.length;a++) sit[a] = Double.parseDouble(temp[a]);
			}
		}
	}
	
	public void priceCalculator() {
		Scanner sc = new Scanner(System.in);
		int adult=0;
		int child=0;
		int economy=0;
		int standard=0;
		int prime=0;
		priceSort();
		pricePrint();
		for(int i =0;i<this.sitInform.length;i++) {
			if(this.sitInform[i][1]==0) adult++;
			else if(this.sitInform[i][1]==1) child++;
			else System.out.println(" ERR!! ");
		}
		if(adult!=0) System.out.print("Adult "+adult);
		if(child!=0) System.out.println(" Child "+child);
		
		for(int i =0;i<this.sitInform.length;i++) {
			if(this.sitInform[i][0]==0) economy++;
			else if(this.sitInform[i][0]==1) standard++;
			else if(this.sitInform[i][0]==2) prime++;
			else System.out.println(" ERR!! ");
		}
		if(economy!=0) System.out.print("Economy "+economy+"Sit ");
		if(standard!=0) System.out.print("Standard "+standard+"Sit ");
		if(prime!=0)System.out.println("Prime "+prime+"sit");
		
		for(int i =0;i<this.sitInform.length;i++) this.totalPrice += this.price*this.sit[this.sitInform[i][0]]*this.age[this.sitInform[i][1]];
		this.totalPrice = this.totalPrice*this.time[this.timeI]*this.D[this.DI];
		System.out.println("It is total of "+this.totalPrice+" won");
		System.out.println("would like to pay?");
		System.out.println("Enter \"o\" if you pay, or \"x\" if you don't pay");
		while(true) {
			System.out.print(">>");
			String str = sc.next();
			if(str.equals("o")) {
				System.out.println("You have chosen to pay");
				ReserveSave();
				break;
			}
			else if(str.equals("x")) {
				System.out.println("You have not chosen to pay");
				break;
			}
			else System.out.println("Could you please re-Enter");
		}
		//여기에 7.2로 넘어가는 함수 입력
	}
	
	public void ReserveSave() {  //예매 내역 저장 
		//아직 구현이 안됨
	}
	
	public void pricePrint() {
		String [] temp = this.movieInform.split(" ");
		for(int i=0;i<temp.length;i++) System.out.println(temp[i]);
	}
}
