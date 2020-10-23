import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PriceManager {
	private String UserName;                  // 위에서부터 넘어 올 유저 이름
	private int price;                        // 기본 가격
	private double[] time = new double[3];    // 시간 할인율 배열
	private double[] D = new double[3];       //  2D,3D등의 할인율 배열
	private double[] age = new double[2];     // 나이별 할인율 배열
	private double[] seat = new double[3];    // 좌석별 할인율 저장 
	private char[] priceArray = new char[45]; // 가격 파일을 저장
	
	private String movieInform;               // 영화 정보 (날짜, 시간, 2D, 영화이름, 상영관 순입니다)
	private double totalPrice = 0;            // 최종 가격
	private String timeinform = null;         // 영화 시작 시간
	private int timeI; // only 0,1,2          // 선택한 시간대(조조,일반,심야)
	private String timeStr;                   // 선택한 시간
	private int DI;    // only 0,1,2          // 선택한 2D,3D,4D
	private int[][] seatInform;               // int [선택한 좌석 등급][앉는 사람의 나이]  
	private String[][] seatInformStr;         // String [선택한 좌석][앉는 사람의 나이]        
	
	public PriceManager(String UserName, String movieInform,String timeStr,String[][] seatInformstr) {  // 생성자
		this.UserName = UserName;
		this.movieInform = movieInform;
		this.timeStr = timeStr;
		this.seatInformStr = seatInformstr;
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
	
	private void priceSort() {    // 각 할인율 및 가격을 알맞는 배열에 저장
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
				else if(i==4) for(int a=0;a<seat.length;a++) seat[a] = Double.parseDouble(temp[a]);
			}
		}
	}
	
	private void timeChange() {   // 조조,일반,심야 구분
		String str = this.timeStr;
		String[] arr = str.split(":");
		int timetemp = Integer.parseInt(arr[0]);
		if(timetemp>4 && timetemp<8) this.timeI = 0;
		else if(timetemp>=8 && timetemp <24) this.timeI = 1;
		else this.timeI = 2;
	}
	
	private void DChange() {  // 2D, 3D, 4D 구분
		String[] arr = this.movieInform.split(" ");
		if(arr[2].equals("2D")) this.DI = 0;
		else if(arr[2].equals("3D")) this.DI = 1;
		else this.DI = 2;
	}

	public void sitInformChange() {    // 좌석 정보를  priceCalculator 편하게 만들어주는 메소드
		this.seatInform = new int[this.seatInformStr.length][this.seatInformStr[0].length];
		
		List<String> seat_list = new ArrayList<String>();
		try {
			File seatInf = new File("SeatInf.txt");
			BufferedReader br = new BufferedReader(new FileReader(seatInf));
			String temp;
			while((temp = br.readLine())!=null) seat_list.add(temp);
			br.close();
		}catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		//여기서 부터 좌석 찾고  등급 나눠야 함
	}
	
	public void priceCalculator() { // 가격 정보 출력 및 저장
		Scanner sc = new Scanner(System.in);
		int adult=0;
		int child=0;
		int economy=0;
		int standard=0;
		int prime=0;
		timeChange();
		DChange();
		sitInformChange();
		priceSort();
		pricePrint();
		for(int i =0;i<this.seatInform.length;i++) {
			if(this.seatInform[i][1]==0) adult++;
			else if(this.seatInform[i][1]==1) child++;
			else System.out.println(" ERR!! ");
		}
		if(adult!=0) System.out.print("Adult "+adult);
		if(child!=0) System.out.println(" Child "+child);
		
		for(int i =0;i<this.seatInform.length;i++) {
			if(this.seatInform[i][0]==0) economy++;
			else if(this.seatInform[i][0]==1) standard++;
			else if(this.seatInform[i][0]==2) prime++;
			else System.out.println(" ERR!! ");
		}
		if(economy!=0) System.out.print("Economy "+economy+"Sit ");
		if(standard!=0) System.out.print("Standard "+standard+"Sit ");
		if(prime!=0)System.out.println("Prime "+prime+"sit");
		
		for(int i =0;i<this.seatInform.length;i++) this.totalPrice += this.price*this.seat[this.seatInform[i][0]]*this.age[this.seatInform[i][1]];
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
	
	private void ReserveSave() {   // 예매 내역을 유저 정보에 저장 
		//유저 찾는 함수 필요
		Reserveupdate();
	}
	
	private void Reserveupdate() { // 예매내역을 통해 좌석 예약을 업데이트
		//영화 찾는 함수 필요
	}
	
	private void pricePrint() { // 결제 화면 출력
		String [] temp = this.movieInform.split(" ");
		for(int i=0;i<temp.length-1;i++) System.out.println(temp[i]);
		System.out.println("Screen"+temp[temp.length-1]);
	}
}
