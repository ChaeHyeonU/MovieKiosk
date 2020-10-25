//데이터 파일 저장하는 부분 추가 필요
//데이터 파일 확인하는 부분 추가 필요
//add에서 add할 데이터 추가하는 부분 수정(하나씩 받아서 문법검사 -> 한줄에 한꺼번에 받아서 무결성 검사)
//edit에서 에디트할 데이터 선택하는 부분이 수정(5개 데이터 한꺼번에 -> 수정할 데이터 선택)
//시간 입력시 영화 시작시간보다 끝나는시간이 빠른경우의 오류 추가

package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieManager {
	static Scanner sc = new Scanner(System.in);
	public static String inputData;
	public static String saveData;
	static String fileName = "MovieInfo.txt";
	public String[] movieInf;
	public String[] editInfo;
	
	
	
	public static void main(String[] args) {
		managerInput();
	}
	

	public static void managerInput() {
		System.out.println("1. Add 2. Edit");
		System.out.print(">>");
		String input = sc.nextLine();
		switch (input)
		{
		case "1" :
			add();
			break;
		case "2" :
			editData();
			break;
		default:
			System.out.println("ERROR : Wrong Input");
			managerInput();
			break;
		}
		
	}
	//중복 데이터 입력시 입력 불가 기능 추가필요
	public static void add() {
		System.out.println("Please enter date(month/date)");
		String date = sc.nextLine();
		dataCheck(5, date);
		System.out.println("Please enter time(00:00 ~ 23:59)");
		String time = sc.nextLine();
		dataCheck(4, time);
		System.out.println("Please enter theater number");
		String theaterNum = sc.nextLine();
		dataCheck(1, theaterNum);
		System.out.println("Please enter movie title");
		String movieTitle = sc.nextLine();
		dataCheck(2, movieTitle);
		System.out.println("Please enter theter type(2D,3D)");
		String theaterType = sc.nextLine();
		dataCheck(3, theaterType);
		saveData = theaterNum +" "+ movieTitle +" "+ theaterType +" "+ time +" "+ date;
		inputData = date +" "+ time +" Screen"+theaterNum +" "+ movieTitle +" "+ theaterType;

		saveDataFile(1, null);
	}
	
	
	

	public static void dataCheck(int num, String data) {
		switch(num) 
		{
		case 1:
			break;
		case 2:
			if(data.length() < 0) {
				System.out.println(data.length());
				errorPrint("title length error");
				add();
			}
			break;
		case 3:
			if(!data.equals("2D") && !data.equals("3D") && !data.equals("4D")) {
				errorPrint("theatertype error");
				add();
			}
			break;
		case 4:
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
		case 5:
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
		case 6:
			if(!data.equals("cancle") && !data.equals("save") && !data.equals("change")) {
				errorPrint("answer error\n");
				saveDataFile(1, null);
				break;
			} else if(data.equals("save")) {
				dataCheck(8, saveData);
				//saveDataFile(2, null);
			}else if(data.contentEquals("change")) {
				break;
			}
			break;
		case 7:
			if(!data.equals("date") && !data.equals("time") && !data.equals("theaterNum") && !data.equals("movieTitme") && !data.equals("theaterType")) {
				errorPrint("editData error");
				editData();
			break;
			}
			break;
		case 8://add할때 중복검사 부분 
			String[] dataArr = saveData.split(" ");			
			String[][] movie_info_list = moiveInf();
			//System.out.println(findData(dataArr));
			if(findData(dataArr) < movie_info_list.length) {
				errorPrint("already exist data");	
				add();
			}else {
				saveDataFile(2, null);
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
			System.out.println("you should enter date, time, theaterNum, movieTitle and theaterType");
			break;
		case "already exist data":
			System.out.println("This data already exists in the datafile, Please enter another data");
			break;
		
		}
	}
	
	
	
	public static void saveDataFile(int num, String[][] arr) {
		switch(num) {
		case 1:
			System.out.println("Do you want to save?(Save/Cancel)");
			System.out.println(inputData);
			System.out.print(">>");
			String answer = sc.nextLine();
			dataCheck(6, answer);
		case 2:
			String[] dataArr = saveData.split(" "); 
			String theaterNum = dataArr[0];
			String movieTitle = dataArr[1];
			String theaterType = dataArr[2];
			String time =  dataArr[3];
			String date = dataArr[4];
			
	
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
				bw.write("<");
				bw.newLine();
				bw.write(theaterNum);
				bw.newLine();
				bw.write(movieTitle);
				bw.newLine();
				bw.write(theaterType);
				bw.newLine();
				bw.write(time);
				bw.newLine();
				bw.write(date);
				bw.newLine();
				bw.write(">");
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			managerInput();
		case 3:
			File file = new File(fileName);
			String temp = "";
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
				for(int i=0; i < arr.length; i++) {
					bw.write("<");
					bw.newLine();
					for(int j = 0; j < arr[i].length; j++) {
						bw.write(arr[i][j]);
						bw.newLine();
					}
					bw.write(">");
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}
	
	
	public static void editData() {//변경전 데이터와 변경후 데이터를 입력받는 메소드
		File file = new File(fileName);
		if(file.exists()) {
			showMovieList();
			
			System.out.println("Please enter data that you want to edit\n"
					+ "(theaterNum, movieTitle, theaterType , time, date)");
			String data = sc.nextLine();
			String[] dataArr = data.split(" "); 
			for(int i = 0; i < dataArr.length; i++) {
				dataCheck(i+1, dataArr[i]);
			}
			
			System.out.println("Please enter data that you want to change\n"
					+ "(theaterNum, movieTitle, theaterType , time, date)");
			String editData = sc.nextLine();
			String[] edataArr = editData.split(" "); 
			for(int i = 0; i < edataArr.length; i++) {
				dataCheck(i+1, edataArr[i]);
			}
		
	        System.out.println("you want to change (" + data + ") to (" + editData + ")?(Change/Cancel)");
			System.out.print(">>");
			String answer = sc.nextLine();
			dataCheck(6,answer);
			saveDataFile(3, changeData(dataArr, edataArr));
			managerInput();
		} else {
			System.out.println("There is no DataFile. Please add File and try again");
		}
	}
	
	
	public static String[][] changeData(String[] arr, String[] changeArr) {//데이터를 변경하는 메소드
		String[][] movie_info_list  = moiveInf();
		int changeLine = findData(arr);
		for(int i = 0; i < arr.length; i++) {
			movie_info_list[changeLine][i] = changeArr[i];
		}
		return movie_info_list;
	}
	
	
	private static String[][] moiveInf(){  // 영화 정보를 찾고 영화 정보 배열에 저장
		ArrayList<String> info_list = new ArrayList<String>();
		try {
			File movieInf = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(movieInf));
			String temp;
			while((temp = br.readLine())!=null) info_list.add(temp);
			br.close();
		}catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		int x=0;
		int y=0;
		for(String a : info_list) {if(a.equals(">")) x++;}
		String[][] info_list_detail = new String[x][5];
		x=0;
		for(String a : info_list) {
			if(a.equals(">")) {x++;y=0;}
			else if(!a.equals("<")){
				info_list_detail[x][y] = a;
				y++;
			}
		}
		
		return info_list_detail;
	}
	
	private static int findData(String[] arr) {  //변경할 영화를 찾는 메소드
		String[][] movie_info_list = moiveInf();
		int find, line = movie_info_list.length+1;
		for(int i =0;i<movie_info_list.length;i++) {
			find=0;
			for(int q=0;q<movie_info_list[i].length;q++) {
				if(movie_info_list[i][q].equals(arr[q])) {
					find++;
				}
				else break;
			}
			if(find == 5) {
				line = i;
				break;
			}
		}
		//System.out.println(line);
		return line;
	}
	
	private static void showMovieList( ) {
		String[][] movie_info_list = moiveInf();
		for(int i = 0; i < movie_info_list.length; i ++) {
			for(int j = 0; j < movie_info_list[i].length; j++) {
				System.out.print(movie_info_list[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

}