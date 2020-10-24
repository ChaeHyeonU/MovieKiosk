
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Movie {
	
	private int sizeoflist;		
	private ArrayList<String> screenlist;
	private ArrayList<String> movielist;
	private ArrayList<String> dimensionlist;
	private ArrayList<String> timelist;
	private ArrayList<String> daylist;
	
	public Movie() {
		this.sizeoflist = 0;
		ArrayList<String> screenlist = new ArrayList<String>();
		ArrayList<String> movielist = new ArrayList<String>();
		ArrayList<String> dimensionlist = new ArrayList<String>();
		ArrayList<String> timelist = new ArrayList<String>();
		ArrayList<String> daylist = new ArrayList<String>();

	}
	
	public void moviestart() {
		initmovielist();
		searchselect();
	}

	private void initmovielist() {				
		try {
			File seatfile = new File("./SeatInf.txt");		
			FileReader filereader = new FileReader(seatfile);			
			BufferedReader bufReader = new BufferedReader(filereader);			
			String line = "";
			int flag = 0;

			while((line = bufReader.readLine()) != null) {
				switch (flag) {
				case 1:
					screenlist.add(line);
					break;
				case 2:
					movielist.add(line);
					break;
				case 3:
					dimensionlist.add(line);
					break;
				case 4:
					timelist.add(line);
					break;
				case 5:
					daylist.add(line);
					break;
				}
				if(flag == 6) {
					flag = 0;
					sizeoflist++;
				}
				else 
					flag++;
			}
			bufReader.close();
		} catch(FileNotFoundException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		}		
		
		
	}
	
	private void searchselect() {
		
		//루프
		while(true) {
			System.out.println();
			System.out.print(">>");
			
			Scanner scan = new Scanner(System.in);
			String input = scan.next();
			input = input.trim();
			String[] splinput = input.split(" ");
			
			if(splinput.length == 1) {
				
				if(input.equals("exit")) {
					//7.2로 점프
					break;
				}
					
				// "영화이름" 검색
				else if(input.indexOf("\"") == 0 && input.charAt(input.length()-1) == '\"') {
					int equalnum = 0;
					for(int i = 0 ; i < sizeoflist; i++) {					
						if(input.split("\"")[1].equals(movielist.get(i))) {
							printlist(i);
							equalnum++;
						}
					}
					if(equalnum == 0) {
						System.out.println("Error : there are no moive data of [" + input + "]");
					}
				}
				
				// 월/일 검색
				else if(input.contains("/")) {
					int equalnum = 0;
					for(int i = 0 ; i < sizeoflist; i++) {					
						if(input.equals(daylist.get(i))) {
							printlist(i);
							equalnum++;
						}
					}
					if(equalnum == 0) {
						System.out.println("Error : there are no match date data [" + input + "]");
					}
				}
				
				else {
					System.out.println("Error : wrong input [" + input + "]");	
				}
				
			}
			
			
			// 영화선택
			else if(splinput.length == 3) {
				
			}
			
			else {
				System.out.println("Error : Please type again");
			}
		}
	}
	
	private void printlist() {
		for(int i = 0 ; i < sizeoflist; i++) {
			System.out.println(daylist.get(i) + " " + timelist.get(i) +
					" Screen" + screenlist.get(i) + " " + movielist.get(i) + 
					" (" + dimensionlist.get(i) + ")" );
		}
	}
	
	private void printlist(int i) {
		System.out.println(daylist.get(i) + " " + timelist.get(i) +
				" Screen" + screenlist.get(i) + " " + movielist.get(i) + 
				" (" + dimensionlist.get(i) + ")" );
	}
	
	public static void clearScreen() {  
		for(int i =0 ; i < 50 ; i++)
			System.out.println();
	}  
	
	
	public static void main(String[] args){	
		Movie movie = new Movie();
		
		movie.moviestart();
		
	}
	
}
