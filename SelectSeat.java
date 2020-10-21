package movie;

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
	private String screenNum;
	private String movieTitle;
	private String date;
	private String time;
	private List<String> lines;
	private String[] arr;
	
	public SelectSeat(String screenNum, String movieTitle, String date, String time) {
		super();
		this.screenNum = screenNum;
		this.movieTitle = movieTitle;
		this.date = date;
		this.time = time;
	}
	
	public void SelectPerson() { //� û�ҳ� �� ����
		Scanner sc = new Scanner(System.in);
		System.out.println("Adult : ");
		while(!sc.hasNextInt()) {
			sc.next();
			System.out.println("Error : Insert Number Only");
		}
		this.adult = sc.nextInt();
		
		System.out.println("Child : ");
		while(!sc.hasNextInt()) {
			sc.next();
			System.out.println("Error : Insert Number Only");
		}
		this.child = sc.nextInt();
	}
	
	public boolean isExist(String[] str) {
		for(int k=0; k<str.length-1; k++) {
			String[] array_seat = str[k+1].split("");
			if(array_seat.length != 2) {
				System.out.println("Error : Argument is Wrong");
				return false;
			}
			if((int)array_seat[0].charAt(0)-96 > this.hight || (int)array_seat[1].charAt(0) - 48 > this.width) {
				System.out.println("Error : does not exist seat");
				return false;
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
	
	public void SelSeat() {
		Scanner sc = new Scanner(System.in);
		int num = this.adult + this.child;
		int count = 0;
		Vector<String> seat = new Vector<String>(num);
		System.out.println("Select Seat Adult : "+ this.adult + " Child : " + this.child);
		while(true) {
			System.out.println("Now Selected : " + seat);
			String select = sc.nextLine();
			String[] str = select.split(" ");	
		
			//��ɾ� ó���κ�
			if(str[0].equals("add") || str[0].equals("Add")) {
				if(str.length <2) { // add ��ɾ ���ڰ� ���� ���
					System.out.println("Error : Insert Seat");
				}
				else { //����
					if(num < seat.size()) { // ���õ� �ڸ��� �ο� �� ���� ���� ���
						System.out.println("Error : You can't add");
					}
					else {
						if(isExist(str) && isIn(str,seat) && overlap(str)) {
							for(int i = 0; i<str.length -1; i++) {
								count++;
								seat.add(str[i+1]);
							}
						}
					}
				}
			}
			
			else if(str[0].equals("delete") || str[0].equals("Delete")) {
				if(str.length <2) { // delete ���ڰ� ���� ���
					System.out.println("Error : Insert Seat");
				}
				else if(seat.size() == 0) { // ������ ���õ� �¼��� ���� ���
					System.out.println("Error : Select seat before delete");
				}
				else { // ����
					if(str.length - 1 > seat.size()) // delete�� �ڸ��� ������ ������ �ڸ����� ���� ��� 
						System.out.println("Error : previously selected < now selected");
					else {
						if(isExist(str) && overlap(str)) {
							for(int i=0; i<str.length-1; i++)
								seat.remove(str[i+1]);
						}
					}
				}
			}
			
			else if(str[0].equals("next") || str[0].equals("Next")) {
				if(num != seat.size()) {
					System.out.println("Select seat before go next");
				}
				else
					break;
			}
			else
				System.out.println("Error : Insert [add] [delete] [next] Only");
		}
	}
	
	public void GetSeat() { //txt ���� �о����
		try {
			lines = Files.readAllLines(path);
			
			for(int i=0; i<lines.size(); i=i+7) {
				if(lines.get(i+1).equals(this.screenNum) && lines.get(i+2).equals(this.movieTitle) && lines.get(i+3).equals(this.time) && lines.get(i+4).equals(this.date)) {
					arr = lines.get(i+5).split("\t");
				}
			}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void showSeat() { //txt ���Ͽ��� �о�� �ڸ� ���
		this.width = arr[0].length();
		this.hight = arr.length;
		System.out.print("   ");
		
		for(int i=0; i<this.width; i++) {
			System.out.printf("%3d", i+1);  // ���� ��ȣ ���
		}
		System.out.println();
		
		for(int i=0; i<this.hight; i++) {
			char a = 'A';
			System.out.printf("%2s ",(char)(a+i)); // ���� ���ĺ� ���
			String[] str = arr[i].split(""); 
			
			for(int j = 0; j<this.width; j++) {
				if(str[j].equals("0"))
					System.out.printf("%3s", "��");     // �¼� ���
				else if(str[j].equals("1"))
					System.out.printf("%3s","��");
				else if(str[j].equals("2"))
					System.out.print("   ");
			}
			System.out.println();
		}
	}
	
}
