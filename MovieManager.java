//데이터 파일 저장하는 부분 추가 필요
//데이터 파일 확인하는 부분 추가 필요
//add에서 add할 데이터 추가하는 부분 수정(하나씩 받아서 문법검사 -> 한줄에 한꺼번에 받아서 무결성 검사)
//edit에서 에디트할 데이터 선택하는 부분이 수정(5개 데이터 한꺼번에 -> 수정할 데이터 선택)
//시간 입력시 영화 시작시간보다 끝나는시간이 빠른경우의 오류 추가

package manager;

import java.util.Scanner;

public class MovieManager {
	static Scanner sc = new Scanner(System.in);
	public static String input;
	
	public static void main(String[] args) {
		managerInput();
	}
	
	//데이터 파일 확인하는 부분 추가
	public static void managerInput() {
//		1. Add 2. Edit
//		Input :
		System.out.println("1. Add 2. Edit");
		System.out.print("입력 :");
		String input = sc.nextLine();
		switch (input)
		{
		case "1" :
			add();
			break;
		case "2" :
			edit();
			break;
		default:
			System.out.println("ERROR : Wrong Input");
			managerInput();
			break;
		}
		
	}
	//기획서에는 하나씩 단계별로 받는걸로 되어있지만 한번에 받아도 되지 않을까
	public static void add() {
		System.out.println("Please enter date(month/date) time(00:00 ~ 23:59) theaterNum movieTitle theaterType");
		input = sc.nextLine();
		String[] dataArr = input.split(" "); 
		String date = dataArr[0];
		String time =  dataArr[1];
		int theaterNum = Integer.parseInt(dataArr[2]);
		String movieTitle = dataArr[3];
		String theaterType = dataArr[4];
	
		dataCheck(1, date);
		dataCheck(2, time);
		dataCheck(3, Integer.toString(theaterNum));
		dataCheck(4, movieTitle);
		dataCheck(5, theaterType);
	}
	
	
	
	//순서도에는 영화제목과 날짜로 선택하는걸로 되어있는데 기획서에는 데이터 5개 다 넣어서 한번에 선택
	public static void edit() {
		System.out.println("Please enter data that you want to edit\n"
				+ "date(month/date) time(00:00 ~ 23:59) theaterNum movieTitle theaterType");
		input = sc.nextLine();
		String[] dataArr = input.split(" "); 
		String date = dataArr[0];
		String time =  dataArr[1];
		int theaterNum = Integer.parseInt(dataArr[2]);
		String movieTitle = dataArr[3];
		String theaterType = dataArr[4];
		
		editData();
		
	}
	
	public static void dataCheck(int num, String data) {
		switch(num) 
		{
		case 1:
			String[] date = data.split("/");
			int month = Integer.parseInt(date[0]);
			int day = Integer.parseInt(date[1]);
			if(month < 0 || month > 12) { 
				errorPrint("month range error");
				add();
			}
			if(day < 0 || day > 31) { 
				errorPrint("day range error");
				add();
			}
			break;
		case 2:
			String[] time = data.split("~");
			String[] startTime = time[0].split(":");
			String[] endTime = time[1].split(":");
			int startHour = Integer.parseInt(startTime[0]);
			int startMin = Integer.parseInt(startTime[1]);
			int endHour = Integer.parseInt(endTime[0]);
			int endMin = Integer.parseInt(endTime[1]);
			if(startHour < 0 || startHour > 23) {
				errorPrint("hour range error");
				add();
			}
			if(startMin < 0 || startMin > 59) {
				errorPrint("min range error");
				add();
			}
			if(endHour < 0 || endHour > 23) {
				errorPrint("hour range error");
				add();
			}
			if(endMin < 0 || endMin > 59) {
				errorPrint("min range error");
				add();
			}	
			if(startHour > endHour) {
				errorPrint("time order error");
				add();
			}
			break;
		case 3:
			break;
		case 4:
			if(data.length() < 0) {
				System.out.println(data.length());
				errorPrint("title length error");
				add();
			}
			break;
		case 5:
			if(!data.equals("2D") && !data.equals("3D") && !data.equals("4D")) {
				errorPrint("theatertype error");
				add();
			}
			saveDataFile(1);
			break;
		case 6:
			if(!data.equals("cancle") && !data.equals("save")) {
				errorPrint("answer error");
				saveDataFile(1);
				break;
			} else if(data.equals("save")) {
				saveDataFile(2);
			}
			break;
		case 7:
			if(!data.equals("date") && !data.equals("time") && !data.equals("theaterNum") && !data.equals("movieTitme") && !data.equals("theaterType")) {
				errorPrint("editData error");
				editData();
			break;
			}
			break;
		}
		
	}
	
	//영화가 끝나는시간이 시작시간보다 빠른경우의 오류추가
	public static void errorPrint(String error) {
		switch(error) 
		{
		case "month range error":
			System.out.println("month should be number between 0 and 12");
			break;
		case "day range error":
			System.out.println("day should be number between 0 and 31");
			break;
		case "hour range error" :
			System.out.println("time should be number between 0 and 23");
			break;
		case "min range error" :
			System.out.println("min should be number between 0 and 59");
			break;
		case "time order error":
			System.out.println("endtime is faster than starttime");
			break;
		case "theatertype error":
			System.out.println("theatertype should be 2D or 3D or 4D");
			break;
		case "title length error":
			System.out.println("movietitle should be more than one letter");
			break;
		case "answer error":
			System.out.println("you should enter Save or Cancle");
			break;
		case "editData error":
			System.out.println("you should enter date, time, theaterNum, movieTitle or theaterType");
		
		}
	}
	
	public static void saveDataFile(int num) {
		switch(num) {
		case 1:
			System.out.println("Do you want to save?(Save/Cancel)");
			System.out.println(input);
			String answer = sc.nextLine();
			dataCheck(6, answer);
		case 2:
			//데이터 파일에 저장하는 부분 추가
			managerInput();
		}
	
		
	}
	
	//데이터파일에 저장하는 부분 추가
	public static void editData() {
		System.out.println("Please enter data that you want to change\n"
				+ "(date, time, theaterNum, movieTitle theaterType)");
		String editData = sc.nextLine();
		dataCheck(7, editData);
		System.out.println("please enter change " + editData);
		String data = sc.nextLine();
		switch(editData)
		{
		case "date" :
			dataCheck(1,data);
			break;
		case "time" :
			dataCheck(2,data);
			break;
		case "theaterNum" :
			dataCheck(3,data);
			break;
		case "movieTtitle" :
			dataCheck(4,data);
			break;
		case "theaterType" :
			dataCheck(5,data);
			break;
		}
		System.out.println("you want to change " + editData + " to " + data + "?(Save/Cancel)");
		String answer = sc.nextLine();
		dataCheck(6,answer);
		
		saveDataFile(2);
	}
	

	
	
}
