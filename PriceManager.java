package moive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	
	private String movieInform;               // String 영화 정보 (상영관, 영화이름, D, 시간, 날짜 순)
	private String[] movieInf;                // String 배열 영화 정보 (상영관, 영화이름, D, 시간, 날짜 순)
	private double totalPrice = 0;            // 최종 가격
	private String timeinform = null;         // 영화 시작 시간
	private int timeI; // only 0,1,2          // 선택한 시간대(조조,일반,심야)
	private int DI;    // only 0,1,2          // 선택한 2D,3D,4D
	private int[][] seatInform;               // int [선택한 좌석 등급][앉는 사람의 나이]  
	private String[][] seatInformStr;         // String [선택한 좌석][앉는 사람의 나이]        
	
	public PriceManager(String UserName, String movieInform,String[][] seatInformstr) {  // 생성자
		this.UserName = UserName;
		this.movieInform = movieInform;
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
	
	private void moiveChange() {   // 각 영화 정보 배열에 저장
		this.movieInf = this.movieInform.split("\r\n");
		//this.movieInf = this.movieInform.split(" ");
	}
	
	private void timeChange() {   // 조조,일반,심야 구분
		String[] str = this.movieInf[3].split(":");
		int timetemp = Integer.parseInt(str[0]);
		if(timetemp>4 && timetemp<8) this.timeI = 0;
		else if(timetemp>=8 && timetemp <24) this.timeI = 1;
		else this.timeI = 2;
	}
	
	private void DChange() {  // 2D, 3D, 4D 구분
		if(this.movieInf[2].equals("2D")) this.DI = 0;
		else if(this.movieInf[2].equals("3D")) this.DI = 1;
		else this.DI = 2;
	}
	
	private String[][] seatMoiveInf(){  // 영화 정보를 찾고 영화 정보 배열에 저장
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
		
		int x=0;
		int y=0;
		for(String a : seat_list) {if(a.equals(">")) x++;}
		String[][] seat_list_detail = new String[x][6];
		x=0;
		for(String a : seat_list) {
			if(a.equals(">")) {x++;y=0;}
			else if(!a.equals("<")){
				seat_list_detail[x][y] = a;
				y++;
			}
		}
		
		return seat_list_detail;
	}
	
	private int findSeat() {  // 사용자가 선택한 영화를 찾는 메소드
		String[][] seat_list_detail = this.seatMoiveInf();
		int find=0;
		for(int i =0;i<seat_list_detail.length;i++) {
			for(int q=0;q<this.movieInf.length;q++) {
				if(seat_list_detail[i][q].equals(this.movieInf[q])) {
					if(q == this.movieInf.length-1) find = i;
				}
				else break;
			}
		}
		return find;
	}

	private void seatInformChange() {    // 좌석 정보를  priceCalculator에게 편하게 만들어주는 메소드
		this.seatInform = new int[this.seatInformStr.length][this.seatInformStr[0].length];
		for(int i=0;i<this.seatInform.length;i++) this.seatInform[i][1] = Integer.parseInt(this.seatInformStr[i][1]);
		
		String[][] seat_list_detail = this.seatMoiveInf();
		//등급 나누기
		String[] seatline = seat_list_detail[this.findSeat()][5].split("\t");
		int grade = seatline.length/3;
		
		//등급 처리
		for(int i=0;i<this.seatInform.length;i++) {
			int temp = (int)(this.seatInformStr[i][0].charAt(0));
			if(temp<grade+65) this.seatInform[i][0] = 0;
			else if(temp<grade*2+65) this.seatInform[i][0] = 1;
			else this.seatInform[i][0] = 2;
		}
	}
	
	public void priceCalculator() {    // 가격 정보 출력 및 저장 <실제로 다른 class에서 호출하는 유일한 함수>
		Scanner sc = new Scanner(System.in);
		int adult=0;
		int child=0;
		int economy=0;
		int standard=0;
		int prime=0;
		
		moiveChange();
		timeChange();
		DChange();
		seatInformChange();
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
		System.out.println("\nIt is total of "+this.totalPrice+" won");
		System.out.println("would like to pay?");
		System.out.println("Enter \"o\" if you pay, or \"x\" if you don't pay");
		while(true) {
			System.out.print(">>");
			String str = sc.next();
			if(str.equals("o")) {
				System.out.println("You have chosen to pay");
				ReserveUserSave();
				ReserveSeatupdate();
				break;
			}
			else if(str.equals("x")) {
				System.out.println("You have not chosen to pay");
				break;
			}
			else System.out.println("Could you please re-Enter");
		}
		Select.selectOrder();
	}
	
	private int userFind(){  // user 정보를 찾는 메소드
		int find=0;
		List<String> user_list = new ArrayList<String>();
		try {
			File userInf = new File("memberInfo.txt");
			BufferedReader br = new BufferedReader(new FileReader(userInf));
			String temp;
			while((temp = br.readLine())!=null) user_list.add(temp);
			br.close();
		}catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i=1;i<user_list.size();i++) {
			if(user_list.get(i-1).equals("{")) {
				if(user_list.get(i).equals(this.UserName)) {
					find = i+2;
					break;
				}
			}
		}
		return find;
	}
	
	private void ReserveUserSave() {   // 예매 내역을 유저 정보에 저장 
		String movieInf = this.movieSort();
		String SeatInf="";
		int x=0;
		for(String[] a : this.seatInformStr) {
			SeatInf += "[";
			SeatInf += (a[0]+","+a[1]);
			if(x != this.seatInformStr.length-1) SeatInf += "],";
			else SeatInf += "]";
			x++;
		}
		movieInf  += SeatInf;
		
		File file = new File("memberInfo.txt");
		String temp = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			for(int i=0;i<this.userFind();i++) {
				line = br.readLine();
				temp += (line+ "\n");
			}
			temp += (movieInf + "\n");
			String str = br.readLine();
			temp += (str+"\n");
			
			while((line = br.readLine())!= null) temp += (line + "\n");
			FileWriter fw =new FileWriter("memberInfo.txt");
			fw.write(temp);
			fw.close();
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void ReserveSeatupdate() {                  // 예매내역을 통해 좌석 예약을 영화정보에 저장
		String[][] seat_list_detail = this.seatMoiveInf();
		String[] seatline = seat_list_detail[this.findSeat()][5].split("\t");
		char[][] seat_detail = new char[seatline.length][seatline[0].toCharArray().length];
		for(int i=0;i<seat_detail.length;i++) {for(int q=0;q<seat_detail[i].length;q++) seat_detail[i][q] = '0';}
		
		for(int i=0;i<this.seatInformStr.length;i++) {
			char[] ch = this.seatInformStr[i][0].toCharArray();
			int A = (int)ch[0] - 65;
			String number = "";
			for(int q=1;q<ch.length;q++) number += ch[q];
			seat_detail[A][Integer.parseInt(number)-1] = '1';
		}
		String str = "";
		for(int i=0;i<seat_detail.length;i++) {
			for(int q=0;q<seat_detail[i].length;q++) {
				str += seat_detail[i][q];
			}
			seatline[i] = str;
			str = "";
		}
		for(int i=0;i<seatline.length;i++) str = str + seatline[i] + "\t";
		
		
		File file = new File("SeatInf.txt");
		String temp = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line;
			for(int i=0;i<this.findSeat()*8+6;i++) {
				line = br.readLine();
				temp += (line+ "\r\n");
			}
			br.readLine();
			temp += (str + "\r\n");
			
			while((line = br.readLine())!= null) temp += (line + "\r\n");
			FileWriter fw =new FileWriter("SeatInf.txt");
			fw.write(temp);
			fw.close();
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String movieSort() {                      //출력하기 위해 영화정보 정렬
		String str = this.movieInf[4]+",";            // 날짜 
		str = str+ this.movieInf[3]+",";              // 시간
		str = str+ this.movieInf[2].charAt(0)+"D,";   // D
		str = str+ this.movieInf[1]+",";              // 영화 이름
		str = str+ "Screen "+this.movieInf[0];        // 상영관
		return str;
	}
	
	private void pricePrint() {   // 결제 화면 출력
		String[] str = movieSort().split(",");
		for(String a: str) System.out.println(a);
		System.out.println();
		
	}
}

