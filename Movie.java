
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
	
	private ArrayList<String> screenlist = new ArrayList<String>();
	private ArrayList<String> titlelist = new ArrayList<String>();
	private ArrayList<String> dimensionlist = new ArrayList<String>();
	private ArrayList<String> timelist = new ArrayList<String>();
	private ArrayList<String> datelist = new ArrayList<String>();
	private int sizeoflist;		
	private String inform;
	
	public Movie() {
		this.sizeoflist = 0;
	}
	
	public String getInform() {
		return inform;
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
					titlelist.add(line);
					break;
				case 3:
					dimensionlist.add(line);
					break;
				case 4:
					timelist.add(line);
					break;
				case 5:
					datelist.add(line);
					break;
				}
				if(flag == 7) {
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
		clearScreen();
		printlist();
		while(true) {


			System.out.println();
			System.out.print(">>");
			
			Scanner scan = new Scanner(System.in);
			String input = scan.nextLine();
			input = input.trim();

//			if(input.equals("back")) {
//				clearScreen();
//				printlist();
//			}
//				
			// "영화이름" 검색
			if(isTitleinput(input)) {
				searchbyTitle(input);
			}
			
			// 월/일 검색
			else if(isDateinput(input)) {
				searchbyDate(input);
			}	
			
			// 영화선택
			else if(isSelectinput(input)) {
				if(selectMovie(input)) {
					break;
				}
			}
			
			else {
				System.out.println("Error : invalid input");
			}
		}
	}
	
	private void searchbyTitle(String input) {
		int equalnum = 0;
		input = input.replace("\"","");
		for(int i = 0 ; i < sizeoflist; i++) {					
			if(input.equals(titlelist.get(i))) {
				printlist(i);
				equalnum++;
			}
		}
		if(equalnum == 0) {
			System.out.println("Error : there are no moive title of [" + input + "]");
		}
		else {
			System.out.println("found " + equalnum );
		}
	}
	
	private void searchbyDate(String input) {
		int equalnum = 0;
		for(int i = 0 ; i < sizeoflist; i++) {					
			if(input.equals(datelist.get(i))) {
				printlist(i);
				equalnum++;
			}
		}
		if(equalnum == 0) {
			System.out.println("Error : there are no date data [" + input + "]");
		}
		else {
			System.out.println("found " + equalnum );
		}
	}
	
	private boolean selectMovie(String input) {
		String[] inputarr = input.split("\\s");
		if(isDate(inputarr[0]) && isTime(inputarr[1])) {
			for(int i = 0 ; i < sizeoflist; i++) {					
				if(inputarr[0].equals(datelist.get(i)) && inputarr[1].equals(startTime(timelist.get(i)))
						&& inputarr[3].equals(screenlist.get(i))) {
					makeInform(i);
					System.out.print("Select : ");
					printlist(i);
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	private String startTime(String s) {
		String result = s.substring(0, s.lastIndexOf("~"));
		return result;
	}
	
	public static boolean isNumeric(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	public static boolean isDate(String str){
		String[] inputarr = str.split("/");
		if(!isNumeric(inputarr[0])) {
			System.out.println("Error : month input is not numeric");
			return false;
		}
		else if(!isNumeric(inputarr[1])) {
			System.out.println("Error : day input is not numeric");
			return false;
		}
		else {
			int month = Integer.parseInt(inputarr[0]);
			if(month < 1 || month > 12) {
				System.out.println("Error : month input is not in 1 ~ 12");
				return false;
			}
			
			int day = Integer.parseInt(inputarr[1]);
			if(day < 1 || day > 31) {
				System.out.println("Error : day input is not in 1 ~ 31");
				return false;
			}
			
			return true;
		}
		
	}
	
	public static boolean isTime(String str) {
		String[] inputarr = str.split(":");
		if(!isNumeric(inputarr[0])) {
			System.out.println("Error : hour input is not numeric");
			return false;
		}
		else if(!isNumeric(inputarr[1])) {
			System.out.println("Error : minute input is not numeric");
			return false;
		}
		else {
			int hour = Integer.parseInt(inputarr[0]);
			if(hour < 0 || hour > 23) {
				System.out.println("Error : hour input is not in 0 ~ 23");
				return false;
			}
			
			int min = Integer.parseInt(inputarr[1]);
			if(min < 0 || min > 59) {
				System.out.println("Error : minute input is not in 0 ~ 59");
				return false;
			}
			
			return true;
		}
	}
	
	private boolean isTitleinput(String input) {
		if(input.indexOf("\"") == 0 && input.charAt(input.length()-1) == '\"') {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isDateinput(String input) {
		String[] inputarr = input.split("\\s");
		if(input.contains("/") && inputarr.length == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isTimeinput(String input) {
		if(input.contains(":")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isSelectinput(String input) {
		
		String[] inputarr = input.split("\\s");
		
		if(inputarr.length == 4 && isDateinput(inputarr[0]) && 
				isTimeinput(inputarr[1]) && inputarr[2].equals("Screen") && isNumeric(inputarr[3])) {			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	//목록 전체 출력
	private void printlist() {
		for(int i = 0 ; i < sizeoflist; i++) {
			System.out.println(datelist.get(i) + " " + timelist.get(i) +
					" Screen " + screenlist.get(i) + " " + titlelist.get(i) + 
					" (" + dimensionlist.get(i) + ")" );
		}
	}
	
	//목록 중 i번째만 출력
	private void printlist(int i) {
		System.out.println(datelist.get(i) + " " + timelist.get(i) +
				" Screen " + screenlist.get(i) + " " + titlelist.get(i) + 
				" (" + dimensionlist.get(i) + ")" );
	}
	
	private String makeInform(int i) {
		String result;
		result = screenlist.get(i) + " ";
		result += titlelist.get(i) + " ";
		result += dimensionlist.get(i) + " ";
		result += timelist.get(i) + " ";
		result += datelist.get(i) + " ";
		inform = result;
		return result;
	}
	
	//화면 clear
	public static void clearScreen() {  
		for(int i =0 ; i < 50 ; i++)
			System.out.println();
	}  
	
	
	public static void main(String[] args){	
		Movie movie = new Movie();
		movie.initmovielist();
		movie.searchselect();
		System.out.println(movie.inform);
	}
	
}
