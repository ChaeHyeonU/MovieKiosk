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
	
	private ArrayList<String> screenlist;
	private ArrayList<String> titlelist;
	private ArrayList<String> dimensionlist;
	private ArrayList<String> timelist;
	private ArrayList<String> datelist;
	private int sizeoflist;		
	public static String inform;
	public static String[] inform2;
	
	public Movie() {
		this.sizeoflist = 0;
		this.screenlist = new ArrayList<String>();
		this.titlelist = new ArrayList<String>();
		this.dimensionlist = new ArrayList<String>();
		this.timelist = new ArrayList<String>();
		this.datelist = new ArrayList<String>();
	}
	
	public String getInform() {
		return inform;
	}
	
	public String[] getInform2() {
		return inform2;
	}
	
	public void initmovielist() {				
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
			System.out.println("Error : File not found [./SeatInf.txt]");
			Select.selectOrder();
		} catch(IOException e) {
			System.out.println(e);
		}		
		
		
	}
	
	public void searchselect() {
		
		//화면 전환
		clearScreen();
		printlist();
		while(true) {


			System.out.println();
			System.out.print(">>");
			
			Scanner scan = new Scanner(System.in);
			String input = scan.nextLine();
			input = input.trim();

			// "영화이름"으로 검색
			if(isTitleinput(input)) {
				searchbyTitle(input);
			}
			
			// 날짜 입력으로 검색
			else if(isDateinput(input)) {
				searchbyDate(input);
			}	
			
			// 영화 선택
			else if(isSelectinput(input)) {
				if(selectMovie(input)) {
					
					//좌석선택으로 이동
					SelectSeat s = new SelectSeat(inform2[0],inform2[1],inform2[2]);
					s.GetSeat();
					s.showSeat();
					s.SelectPerson();
					s.SelSeat();
					s.SeatDivision();
					break;
				}
			}
			
			else {
				System.out.println("Error : invalid input");
			}
		}
	}
	
	//영화제목입력으로 영화 찾기
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
			System.out.println("There are no list of moive title [" + input + "]");
		}
		else {
			System.out.println("Found " + equalnum );
		}
	}
	
	//날짜 입력으로 영화 찾기
	private void searchbyDate(String input) {
		int equalnum = 0;
		for(int i = 0 ; i < sizeoflist; i++) {					
			if(input.equals(datelist.get(i))) {
				printlist(i);
				equalnum++;
			}
		}
		if(equalnum == 0) {
			if(isDate(input)) {
				System.out.println("There are no list of movie date data [" + input + "]");
			}
		}
		else {
			System.out.println("Found " + equalnum );
		}
	}
	
	//영화섵택
	private boolean selectMovie(String input) {
		String[] inputarr = input.split("\\s");
		if(isDate(inputarr[0]) && isTime(inputarr[1]) && inputarr[2].equals("Screen")) {
			for(int i = 0 ; i < sizeoflist; i++) {					
				if(inputarr[0].equals(datelist.get(i)) && 
						inputarr[1].equals(startTime(timelist.get(i))) && 
						inputarr[3].equals(screenlist.get(i))) {
					makeInform(i);
					makeInform2(i);
					System.out.print("Select : ");
					printlist(i);
					return true;
				}
				
			}
			System.out.println("There are no equal movie data [" + input + "]");
			return false;
		}
		return false;
	}
	
	//시작시간만 따오긴
	private String startTime(String s) {
		String result = s.substring(0, s.lastIndexOf("~"));
		return result;
	}
	
	//수인지 확인
	public static boolean isNumeric(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	public static boolean isScreenNumeric(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	    	System.out.println("Error : Screen number is not numeric");
	        return false;
	    }
	}
	
	//날짜 입력이 올바른지 확인
	public static boolean isDate(String str){
		String[] inputarr = str.split("/");
		String error = "Error : ";
		if(!isNumeric(inputarr[0]) && !isNumeric(inputarr[1])) {
			error += "Month input and Day input is not numeric";			
			
		}
		else if(!isNumeric(inputarr[0])) {
			error += "Month input is not numeric";
			int day = Integer.parseInt(inputarr[1]);
			if(day < 1 || day > 31) {
				error += " and Day input is not in 1 ~ 31";
			}
		}
		else if(!isNumeric(inputarr[1])){
			int month = Integer.parseInt(inputarr[0]);
			if(month < 1 || month > 12) {
				error += "Month input is not in 1 ~ 12 and";
			}
			error += " Day input is not numeric";
		}
		else {
			int month = Integer.parseInt(inputarr[0]);
			int day = Integer.parseInt(inputarr[1]);
			if((month < 1 || month > 12) || (day < 1 || day > 31)) {
				if((month < 1 || month > 12) && (day < 1 || day > 31)) {
					error += "Month input is not in 1 ~ 12 and Day input is not in 1 ~ 31";	
				}
				else if(month < 1 || month > 12) {
					error += "Month input is not in 1 ~ 12";
				}
				else {
					error += "Day input is not in 1 ~ 31";
				}
			}
			else 
				return true;
		}
		System.out.println(error);
		return false;

		
	}

	//시간 입력이 올바른지 확인
	public static boolean isTime(String str) {
		String[] inputarr = str.split(":");
		String error = "Error : ";
		if(!isNumeric(inputarr[0]) && !isNumeric(inputarr[1])) {
			error += "Hour input and Minute input is not numeric";			
			
		}
		else if(!isNumeric(inputarr[0])) {
			error += "Hour input is not numeric";
			int minute = Integer.parseInt(inputarr[1]);
			if(minute < 0 || minute > 59) {
				error += " and Minute input is not in 0 ~ 59";
			}
		}
		else if(!isNumeric(inputarr[1])){
			int hour = Integer.parseInt(inputarr[0]);
			if(hour < 0 || hour > 23) {
				error += "Hour input is not in 0 ~ 23 and";
			}
			error += " Minute input is not numeric";
		}
		else {
			int hour = Integer.parseInt(inputarr[0]);
			int minute = Integer.parseInt(inputarr[1]);
			if((hour < 0 || hour > 23) || (minute < 0 || minute > 59)) {
				if((hour < 0 || hour > 23) && (minute < 0 || minute > 59)) {
					error += "Hour input is not in 0 ~ 23 and Minute input is not in 0 ~ 59";	
				}
				else if(hour < 0 || hour > 23) {
					error += "Hour input is not in 0 ~ 23";
				}
				else {
					error += "Minute input is not in 0 ~ 59";
				}
			}
			else 
				return true;
		}
		System.out.println(error);
		return false;

		
	}

	//영화제목 입력시 입력형태가 올바른지 홧인
	private boolean isTitleinput(String input) {
		if(input.indexOf("\"") == 0 && input.charAt(input.length()-1) == '\"') {
			return true;
		}
		else {
			return false;
		}
	}
	
	//날짜 입력시 입력형태가 올바른지 확인
	private boolean isDateinput(String input) {
		String[] inputarr = input.split("\\s");
		if(input.contains("/") && inputarr.length == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//시간 입력시 입력형태가 올바른지 확인
	private boolean isTimeinput(String input) {
		if(input.contains(":")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//선택 입력시 입력형태가 올바른지 확인
	private boolean isSelectinput(String input) {
		
		String[] inputarr = input.split("\\s");
		
		if(inputarr.length == 4 && isDateinput(inputarr[0]) && 
				isTimeinput(inputarr[1]) && 
				inputarr[2].equals("Screen") && 
				isScreenNumeric(inputarr[3])) {			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	// 전체 영화목록 출력
	private void printlist() {
		for(int i = 0 ; i < sizeoflist; i++) {
			System.out.println(datelist.get(i) + " " + timelist.get(i) +
					" Screen " + screenlist.get(i) + " " + titlelist.get(i) + 
					" (" + dimensionlist.get(i) + ")" );
		}
	}
	
	// 영화목록 i번째만 출력
	private void printlist(int i) {
		System.out.println(datelist.get(i) + " " + timelist.get(i) +
				" Screen " + screenlist.get(i) + " " + titlelist.get(i) + 
				" (" + dimensionlist.get(i) + ")" );
	}
	
	//결제페이지로 넘겨줄 정보 생성
	private String makeInform(int i) {
		String result;
		result = screenlist.get(i) + "\r\n" ;
		result += titlelist.get(i) + "\r\n";
		result += dimensionlist.get(i) + "\r\n";
		result += timelist.get(i) + "\r\n";
		result += datelist.get(i);
		inform = result;
		return result;
	}
	
	//좌석선택페이지로 넘겨줄 정보 생성
	private String[] makeInform2(int i) {
		String[] result = new String[3];
		result[0] = screenlist.get(i);
		result[1] = datelist.get(i);
		result[2] = timelist.get(i);
		inform2 = result;
		return result;
	}
	
	// 화면 clear
	public static void clearScreen() {  
		for(int i =0 ; i < 50 ; i++)
			System.out.println();
	}  
	
}
