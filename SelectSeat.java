import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class SelectSeat {
	Path path = Paths.get("./SeatInf.txt");
	private int adult;
	private int child;
	private int hight;
	private int width;
	private int remainSeat;
	private String screenNum;
	private String date;
	private String time;
	private List<String> lines;
	private String[] arr;
	private Vector<Character> noSeat1 = new Vector<Character>();
	private Vector<Integer> noSeat2 = new Vector<Integer>();
	private String[][] SelectedSeat;

	
	public SelectSeat(String screenNum, String date, String time) {
		super();
		this.screenNum = screenNum;
		this.date = date;
		this.time = time;
	}
	
	public void SelectPerson() { 
		Scanner sc = new Scanner(System.in);
		System.out.println("Adult >> ");
		while(!sc.hasNextInt()) {
			sc.next();
			System.out.println("Error : Insert Number Only");
		}
		this.adult = sc.nextInt();
		if(this.adult > remainSeat) {
			System.out.println("Error : There are no seats for the selected number of people");
			SelectPerson();
		}
		System.out.println("Child >> ");
		while(!sc.hasNextInt()) {
			sc.next();
			System.out.println("Error : Insert Number Only");
		}
		this.child = sc.nextInt();
		if(this.child > remainSeat) {
			System.out.println("Error : There are no seats for the selected number of people");
			SelectPerson();
		}
		if(this.adult ==0 && this.child ==0) {
			System.out.println("Error : You must insert at least one");
			SelectPerson();
		}
		else if(this.adult <0 || this.child < 0) {
			System.out.println("Error : You must insert more than 0");
			SelectPerson();
		}
		else if(this.adult + this.child > remainSeat) {
			System.out.println("Error : There are no seats for the selected number of people");
			SelectPerson();
		}
	}
	
	public boolean isExist1(String[] array_seat) { //이전에 선택한 자리 혹은 없는 자리가 아닌지
		int length = array_seat.length;
		if(length == 2) {
			char seat1 = array_seat[0].charAt(0);
			int seat2 = Integer.parseInt(array_seat[1]);
			for(int i=0; i<noSeat1.size(); i++) {
				if(noSeat1.get(i).equals(seat1) && noSeat2.get(i).equals(seat2)) 
					return false;
			}
		}
		else if(length == 3) {
			char seat1 = array_seat[0].charAt(0);
			int seat2 = Integer.parseInt(array_seat[1]) *10 + Integer.parseInt(array_seat[2]);
			for(int i=0; i<noSeat1.size(); i++) {
				if(noSeat1.get(i).equals(seat1) && noSeat2.get(i).equals(seat2)) 
					return false;
			}
		}
		return true;
	}
	
	public boolean isExist(String[] str) { //영화관 안에 들어있는 자리 인지
		for(int k=0; k<str.length-1; k++) {
			String[] array_seat = str[k+1].split("");
			if(array_seat.length < 2 || array_seat.length > 3) {
				System.out.println("Error : Argument is Wrong");
				return false;
			}
			if(array_seat[0].charAt(0) - 65 > this.hight) {
				System.out.println("Error : does not exist seat");
				return false;
			}
			if(!isExist1(array_seat)) {
				System.out.println("Error : does not exist seat");
				return false;
			}
			else {
				if(array_seat.length == 2) {
					int seat_width = (int)array_seat[1].charAt(0) - 48;
					if(seat_width > this.width || seat_width > 9 || seat_width <= 0) {
						System.out.println("Error : does not exist seat");
						return false;
					}
				}
				else if(array_seat.length == 3) {
					int seat_width1 = (int)array_seat[1].charAt(0) - 48;
					int seat_width2 = (int)array_seat[2].charAt(0) - 48;
					if(seat_width1 * 10 + seat_width2 > this.width || seat_width1 > 9 || seat_width1 <= 0 || seat_width2 > 9 || seat_width2 < 0) {
						System.out.println("Error : does not exist seat");
						return false;
					}
				}
			}
			
		}
		return true;
	}
	
	public boolean isIn(String[] str, Vector<String> v) {
		for(int k=0; k<str.length-1; k++) {
			if(v.contains(str[k+1])) {
				System.out.println("Error : Already Selected");
				return false;
			}
				
		}
		return true;
	}
	
	public boolean overlap(String[] str) {
		for(int k=0; k<str.length; k++) {
			for(int i=0; i<k; i++) {
				if(str[k].equals(str[i])) {
					System.out.println("Error : Seats Overlap");
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isRight(String[] str) {
		int length = str.length;
		for(int i=0; i<length-1; i++) {
			String[] splitStr = str[i+1].split("");
			int splitLength = splitStr.length;
			if(splitLength == 2) {
				if((splitStr[0].trim().charAt(0) <65 || splitStr[0].trim().charAt(0) >90) 
					|| (splitStr[1].trim().charAt(0) < 48 || splitStr[1].trim().charAt(0) >57 )) 
					return false;
			}
			else if(splitLength ==3) {
				if((splitStr[0].trim().charAt(0) <65 || splitStr[0].trim().charAt(0) >90) 
					|| (splitStr[1].trim().charAt(0) < 48 || splitStr[1].trim().charAt(0) >57 ) 
					||(splitStr[2].trim().charAt(0) < 48 || splitStr[2].trim().charAt(0) >57))
					return false;				
			}
		}
		return true;
	}
	
	public boolean blank(String select) {
		String[] newStr = select.split("");
		for(int i=0; i<newStr.length-1; i++) {
			if(newStr[i].equals(" ") && newStr[i+1].equals(" "))
				return false;
		}
		return true;
	}
	
	public boolean deleteExist(String[] str, Vector<String> seat) {
		for(int i=0; i<str.length-1; i++) {
			for(int j=0; j<seat.size(); j++) {
				if(str[i+1].equals(seat.get(j))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void SelSeat() {
		Scanner sc = new Scanner(System.in);
		int num = this.adult + this.child;
		SelectedSeat = new String[num][2];
		Vector<String> seat = new Vector<String>(num);
		System.out.println("Select Seat Adult : "+ this.adult + " Child : " + this.child);
		while(true) {
			System.out.println("Now Selected >> " + seat);
			String select = sc.nextLine();
			String[] str = select.split(" ");
			
			if(!blank(select)) {
				System.out.println("Error : You must insert blank once");
			}
			else if(!isRight(str)) {
				System.out.println("Error : You must insert uppercase alphatbet + number");
			}
			else if(str[0].equals("add")) {
				if(str.length <2) { 
					System.out.println("Error : Insert Seat");
				}
				else {
					if(num <= seat.size() || num <str.length-1) { 
						System.out.println("Error : You can't add");
					}
					else {
						if(isExist(str) && isIn(str,seat) && overlap(str)) {
							for(int i = 0; i<str.length -1; i++) 
								seat.add(str[i+1]);
						}
					}
				}
			}
			
			else if(str[0].equals("delete")) {
				if(str.length <2) { 
					System.out.println("Error : Insert Seat");
				}
				else if(seat.size() == 0) { 
					System.out.println("Error : Select seat before delete");
				}
				else if(!deleteExist(str, seat)) {
					System.out.println("Error : You did not select this seat before");
				}
				else { 
					
					if(str.length - 1 > seat.size()) 
						System.out.println("Error : previously selected < now selected");
					else {
						if(isExist(str) && overlap(str)) {
							for(int i=0; i<str.length-1; i++)
								seat.remove(str[i+1]);
						}
					}
				}
			}
			
			else if(str[0].equals("next")) {
				if(str.length != 1) {
					System.out.println("Error : Next command must be written alone");
				}
				else if(num != seat.size()) {
					System.out.println("Error : Select seat before go next");
				}
				else {
					for(int i=0; i<seat.size(); i++) {
						SelectedSeat[i][0] = seat.get(i);
					}
					break;
				}
					
			}
			else
				System.out.println("Error : Insert [add] [delete] [next] Only");
			
		}
	}
	
	public void SeatDivision() {
		Scanner sc = new Scanner(System.in);
		int a = 0;
		int c = 0;
		System.out.println("Please insert 'Adult' or 'Child'");
		for(int i=0; i<SelectedSeat.length; i++) {
			while(true) {
				System.out.println(SelectedSeat[i][0] +" >> " );
				String div = sc.nextLine();
				if(div.equals("Adult")) {
					SelectedSeat[i][1] = "0";
					a++;
					break;
				}
				else if(div.equals("Child")) {
					SelectedSeat[i][1] = "1";
					c++;
					break;
				}
				else {
					System.out.println("Error : Please insert Only 'Adult' or 'Child'");
				}
			}
		}
		if(a != this.adult || c != this.child) {
			System.out.println("Error : You insert Wrong Age");
			SeatDivision();
		}
		String[][] str = SelectedSeat;
		PriceManager price = new PriceManager(LogIn.nowID, Movie.inform , str);
		price.priceCalculator();
	}
	
	public String[][] getSelectedSeat() {
		return SelectedSeat;
	}

	public void setSelectedSeat(String[][] selectedSeat) {
		SelectedSeat = selectedSeat;
	}

	public void GetSeat() { //txt  불러오기
		try {
			lines = Files.readAllLines(path);
			
			for(int i=0; i<lines.size(); i=i+8) {
				if(lines.get(i+1).equals(this.screenNum) && lines.get(i+4).equals(this.time) && lines.get(i+5).equals(this.date)) {
					arr = lines.get(i+6).split("\t");
				}
			}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : No File");
			e.printStackTrace();
		}
		
	}
	
	public void showSeat() { //txt  띄우기
		this.width = arr[0].length();
		this.hight = arr.length;
		System.out.print("   ");
		
		for(int i=0; i<this.width; i++) {
			System.out.printf("%3d", i+1);  
		}
		System.out.println();
		
		for(int i=0; i<this.hight; i++) {
			char a = 'A';
			System.out.printf("%2s ",(char)(a+i)); 
			String[] str = arr[i].split(""); 
			
			for(int j = 0; j<this.width; j++) {
				if(str[j].equals("0"))
					System.out.printf("%3s", "□");     
				else if(str[j].equals("1")) {
					System.out.printf("%3s","■");
					noSeat1.add((char)(a+i));
					noSeat2.add(j+1);
				}
					
				else if(str[j].equals("2")) {
					System.out.print("   ");
					noSeat1.add((char)(a+i));
					noSeat2.add(j+1);
				}
			}
			System.out.println();
		}
		remainSeat = width * hight - noSeat1.size();
//		System.out.println(remainSeat);
	}
}
