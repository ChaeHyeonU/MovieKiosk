import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PriceManager {
	private String UserName;                  // 위에서부터 넘어 올 유저 이름
	private int price;                        // 기본 가격
	private double[] time = new double[3];    // 시간 할인율 배열
	private double[] D = new double[3];       //  2D,3D등의 할인율 배열
	private double[] age = new double[2];     // 나이별 할인율 배열
	private double[] sit = new double[3];     // 좌석별 할인율 저장 
	private char[] priceArray = new char[45]; // 가격 파일을 저장
	
	private String movieInform;               // 영화 정보
	private double totalPrice=0;              // 최종 가격
	private int timeI; // only 0,1,2          // 선택한 시간대(조조,일반,심야)
	private int DI;    // only 0,1,2          // 선택한 2D,3D,4D
	private int[][] sitInform;                // int [선택한 좌석][앉는 사람의 나이]  
	
	public PriceManager(String UserName, String movieInform,int timeI, int DI,int[][] sitInform) {  // 생성자
		this.UserName = UserName;
		this.movieInform = movieInform;
		this.timeI = timeI;
		this.DI = DI;
		this.sitInform = sitInform;
	}
	
	private void FileRead() { // 가격 정보를 파일에서 불러와서 priceArray에 저장
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
	
	private void priceSort() { // save price inform
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
	
	public void priceCalculator() { // 가격 정보 출력 및 저장
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
	
	public void ReserveSave() {  // 예매 내역 저장 
		// 아직 구현이 안됨
	}
	
	public void pricePrint() { // 결제 화면 출력
		String [] temp = this.movieInform.split(" ");
		for(int i=0;i<temp.length;i++) System.out.println(temp[i]);
	}
}
