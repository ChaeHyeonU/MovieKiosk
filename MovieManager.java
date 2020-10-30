import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MovieManager {
	static Scanner sc = new Scanner(System.in);
	public static String inputData;
	public static String saveData;
	public static String selectData;
	public static String editData;
	static String fileName = "./SeatInf.txt";
	static String screenfileName = "./ScreenInf.txt";
	public String[] movieInf;
	public String[] editInfo;
	static String date;
	static String time;
	static String theaterNum;
	static String movieTitle;
	static String theaterType;
	static int cnt; // 몇관까지 있는지

	public static void managerInput() {
		System.out.println("1. Add 2. Edit (enter 1 or 2)");
		System.out.print(">>");
		String input = sc.nextLine();
		switch (input) {
		case "1":
			add("date");
			break;
		case "2":
			editData("select");
			break;
		default:
			System.out.println("ERROR : Wrong Input You should enter 1 od 2");
			managerInput();
			break;
		}

	}

	public static void add(String data) {
		switch (data) {
		case "date":
			System.out.println("Please enter date(month/date)");
			System.out.print(">>");
			date = sc.nextLine();
			dataCheck(5, date, "add");
		case "time":
			System.out.println("Please enter time(1:1~24:0)");
			System.out.print(">>");
			time = sc.nextLine();
			dataCheck(4, time, "add");
		case "theaterNum":
			System.out.println("Please enter theater number");
			System.out.print(">>");
			theaterNum = sc.nextLine();
			dataCheck(1, theaterNum, "add");

		case "movieTitle":
			System.out.println("Please enter movie title");
			System.out.print(">>");
			movieTitle = sc.nextLine();
			dataCheck(2, movieTitle, "add");
		case "theaterType":
			System.out.println("Please enter theter type(2D,3D,4D)");
			System.out.print(">>");
			theaterType = sc.nextLine();
			dataCheck(3, theaterType, "add");
			
		}
		saveData = theaterNum + " " + movieTitle + " " + theaterType + " " + time + " " + date;
		inputData = date + " " + time + " Screen" + theaterNum + " " + movieTitle + " " + theaterType;

		saveDataFile(1, null);
	}

	public static void dataCheck(int num, String data, String ae) {
		switch (num) {
		case 1:
			try {
				if (Integer.parseInt(data) < 1 || Integer.parseInt(data) > screenInf().length) {
					errorPrint("theaterNum outof bound");
					if(ae.equals("add")) {
						add("theaterNum");
					}else if(ae.equals("edit")) {
						editData("change");
					}
					
				}
			}catch(NumberFormatException e) {
				errorPrint("theaterNum format error");
				if(ae.equals("add")) {
					add("theaterNum");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}catch(NullPointerException e) {
				errorPrint("theaterNum format error");
				if(ae.equals("add")) {
					add("theaterNum");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}
			break;
		case 2:
			if (data.length() < 1) {
				System.out.println(data.length());
				errorPrint("title length error");
				if(ae.equals("add")) {
					add("movieTitle");
				}else if(ae.equals("edit")) {
					editData("change");
				}
				
			}
			if (spaceCheck(data) == true) {
				errorPrint("movieTitle format error");
				if(ae.equals("add")) {
					add("movieTitle");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}
			if(numCheck(data) == false) {
				errorPrint("movieTitle include num");
				if(ae.equals("add")) {
					add("movieTitle");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}
			break;
		case 3:
			if (!data.equals("2D") && !data.equals("3D") && !data.equals("4D")) {
				errorPrint("theatertype error");
				if(ae.equals("add")) {
					add("theaterType");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}
			break;
		case 4:
			try {
				String[] time = data.split("~");
				String[] startTime = time[0].split(":");
				String[] endTime = time[1].split(":");
				int startHour = Integer.parseInt(startTime[0]);
				int startMin = Integer.parseInt(startTime[1]);
				int endHour = Integer.parseInt(endTime[0]);
				int endMin = Integer.parseInt(endTime[1]);
				String sstartHour = startTime[0];
				String sstartMin = startTime[1];
				String sendHour = startTime[0];
				String sendMin = startTime[1];
				
				String start = sstartHour + sstartMin; 
				String end = sendHour + sendMin; 
				Date start_movie;
				Date end_movie;
				SimpleDateFormat format = new SimpleDateFormat("Hm");
				
				
				if ((startTime[0].length() > 1 && startTime[0].startsWith("0"))
						|| (startTime[1].length() > 1 && startTime[1].startsWith("0"))
						|| (endTime[0].length() > 1 && endTime[0].startsWith("0"))
						|| (endTime[1].length() > 1 && endTime[1].startsWith("0"))) {
					errorPrint("time start with 0");
					if(ae.equals("add")) {
						add("time");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}
				if((startHour == 1 && startMin == 0) || (endHour == 24 && endMin == 1)) {
					errorPrint("time range error");
					add("time");
				}
				
				if (startHour < 1 || startHour > 24 || endHour < 1 || endHour > 24) {// 시간오류 시간분오류 시간분순서오류
					errorPrint("hour range error");
					if (startMin < 0 || startMin > 59 || endMin < 0 || endMin > 59) {
						errorPrint("min range error");
						if (startHour > endHour) {
							errorPrint("time order error");
							if(ae.equals("add")) {
								add("time");
							}else if(ae.equals("edit")) {
								editData("change");
							}
						}else {
							if(ae.equals("add")) {
								add("time");
							}else if(ae.equals("edit")) {
								editData("change");
							}
						}
						if(ae.equals("add")) {
							add("time");
						}else if(ae.equals("edit")) {
							editData("change");
						}
					}
					if(ae.equals("add")) {
						add("time");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				} else if (startMin < 0 || startMin > 59 || endMin < 0 || endMin > 59) {// 분오류 분순서오류
					errorPrint("min range error");
					if(ae.equals("add")) {
						add("time");
					}else if(ae.equals("edit")) {
						editData("change");
					}
					try {
						start_movie = format.parse(start);
						end_movie = format.parse(end);
						
						if(end_movie.getTime() < start_movie.getTime()) {
							errorPrint("time order error");
							if(ae.equals("add")) {
								add("time");
							}else if(ae.equals("edit")) {
								editData("change");
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (startHour > endHour) {
					errorPrint("time order error");
					if(ae.equals("add")) {
						add("time");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}

				try {
					start_movie = format.parse(start);
					end_movie = format.parse(end);
					
					if(end_movie.getTime() < start_movie.getTime()) {
						errorPrint("time order error");
						if(ae.equals("add")) {
							add("time");
						}else if(ae.equals("edit")) {
							editData("change");
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch (NumberFormatException e) {
				errorPrint("time format error");
				if(ae.equals("add")) {
					add("time");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}catch (ArrayIndexOutOfBoundsException e) {
				errorPrint("time format error");
				if(ae.equals("add")) {
					add("time");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}	
			break;
		case 5:
			try {
				String[] date = data.split("/");
				int month = Integer.parseInt(date[0]);
				int day = Integer.parseInt(date[1]);
				
				if (month < 1 || month > 12) {
					errorPrint("month range error");
					if (day < 1 || day > 31) {
						errorPrint("day range error");
						if(ae.equals("add")) {
							add("date");
						}else if(ae.equals("edit")) {
							editData("change");
						}
					}
					if(ae.equals("add")) {
						add("date");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}

				if (day < 1 || day > 31) {
					errorPrint("day range error");
					if(ae.equals("add")) {
						add("date");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}
				
				if ((month == 2 ||  month == 4 || month == 6 || month == 9 || month ==11) && (day == 31)) {
					errorPrint("31 error");
					if(ae.equals("add")) {
						add("date");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}
				if(month == 2 && day == 30) {
					errorPrint("30 error");
					if(ae.equals("add")) {
						add("date");
					}else if(ae.equals("edit")) {
						editData("change");
					}
				}
			}catch(NumberFormatException e) {
				errorPrint("date Format error");
				if(ae.equals("add")) {
					add("date");
				}else if(ae.equals("edit")) {
					editData("change");
				}
			}
			break;
		case 6:
			if(ae.equals("add")) {
				if (!data.equals("cancel") && !data.equals("save") && !data.equals("change") && !data.equals("SAVE")
						&& !data.equals("Save")) {
					errorPrint("answer error");
					saveDataFile(1, null);
					break;
				} else if (data.equals("save") || data.equals("SAVE") || data.equals("Save")) {
					if (ae.equals("add")) {
						dataCheck(8, saveData, "add");
					}
				} else if (data.contentEquals("change")) {
					break;
				}else if (data.equals("cancel")) {
					LogIn.inputOrder();
				}
			}else if(ae.equals("edit")) {
				if (!data.equals("cancel") && !data.equals("change")) {
					errorPrint("edit answer error");
					editData("answer");
				}else if(data.equals("cancel")) {
					LogIn.inputOrder();
				}else if(data.equals("change")) {
					break;
				}
			}
			
			break;
		case 7:
			if (!data.equals("date") && !data.equals("time") && !data.equals("theaterNum") && !data.equals("movieTitme")
					&& !data.equals("theaterType")) {
				errorPrint("editData error");
				editData("select");
				break;
			}
			break;
		case 8:// add할때 중복검사 부분
			File file = new File(fileName);
			if (file.exists()) {
				String[] dataArr = saveData.split(" ");
				String[][] movie_info_list = moiveInf();
				// System.out.println(findData(dataArr));

				if (findData(dataArr) < movie_info_list.length + 1) { // 입력한 데이터와 같은 데이터가 이미 파일에 존재
					if (ae.equals("add")) {
						errorPrint("already exist data");
						add("date");
					}
				} else {
					if (ae.equals("add")) {
						dataCheck(9, saveData, "add");
						saveDataFile(2, null);
					} else if (ae.equals("edit")) {// 수정할 데이터가 데이터 파일에 없음
						errorPrint("wrong input data");
						editData("select");
					}
				}
			}
			break;
		case 9: // 같은 관에서 겹치는 시간에 상영불가
			String[] dataArr = saveData.split(" ");
			String[][] movie_info_list = moiveInf();
			for (int i = 0; i < movie_info_list.length; i++) {
				if (i == findData(dataArr) && ae.equals("edit"))
					continue;
				if (movie_info_list[i][4].equals(dataArr[4]) && movie_info_list[i][0].equals(dataArr[0])) {// 같은 날짜 같은 상영관
					String[] time = movie_info_list[i][3].split("~"); // 시간 확인
					String [] startTime = time[0].split(":");
					String[] endTime = time[1].split(":");
					String[] addTime = dataArr[3].split("~");
					String[] addStartTime = addTime[0].split(":");
					String[] addEndTime = addTime[1].split(":");

					String startHour = startTime[0];
					String startMin = startTime[1];
					String endHour = endTime[0];
					String endMin = endTime[1];
					String addstartHour = addStartTime[0];
					String addstartMin = addStartTime[1];
					String addendHour = addEndTime[0];
					String addendMin = addEndTime[1];
					//ystem.out.println( startHour  startMin endHour  endMin);
					
					String start =  startHour + startMin; 
					String end =  endHour + endMin; 
					String addstart =  addstartHour + addstartMin; 
					String addend =  addendHour + addendMin; 
					
					SimpleDateFormat format = new SimpleDateFormat("Hm");

					try {
						Date start_movie = format.parse(start);
						Date end_movie = format.parse(end);
						Date add_start_movie = format.parse(addstart);
						Date add_end_movie = format.parse(addend);
						
						
						if(add_end_movie.getTime() > start_movie.getTime() || end_movie.getTime() > add_start_movie.getTime()){
							errorPrint("Time error");
							if (ae.equals("add")) {
								add("date");
							}else {
								editData("change");
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		break;
	case 10:
		int line = Integer.parseInt(theaterNum);
		String[] screenSit = screenLine(line);
		int possible4D = Integer.parseInt(screenSit[1]);
		if ((possible4D == 0) && (theaterType.equals("4D"))) {
			errorPrint("type error");
			add("teaterType");
		}
		break;
	case 11: // 같은 관에서 겹치는 시간에 상영불가
		dataArr = data.split(" ");
		movie_info_list = moiveInf();
		for (int i = 0; i < movie_info_list.length; i++) {
			//System.out.println("i is " + i + " line is " + (findData(dataArr)-3));
			if (i != findData(dataArr)-2) {
				if (movie_info_list[i][4].equals(dataArr[4]) && movie_info_list[i][0].equals(dataArr[0])) {// 같은 날짜 같은 상영관
					
					String[] time = movie_info_list[i][3].split("~"); // 시간 확인
					String [] startTime = time[0].split(":");
					String[] endTime = time[1].split(":");
					String[] addTime = dataArr[3].split("~");
					String[] addStartTime = addTime[0].split(":");
					String[] addEndTime = addTime[1].split(":");

					String startHour = startTime[0];
					String startMin = startTime[1];
					String endHour = endTime[0];
					String endMin = endTime[1];
					String addstartHour = addStartTime[0];
					String addstartMin = addStartTime[1];
					String addendHour = addEndTime[0];
					String addendMin = addEndTime[1];
					//ystem.out.println( startHour  startMin endHour  endMin);
					
					String start =  startHour + startMin; 
					String end =  endHour + endMin; 
					String addstart =  addstartHour + addstartMin; 
					String addend =  addendHour + addendMin; 
					
					SimpleDateFormat format = new SimpleDateFormat("Hm");

					try {
						Date start_movie = format.parse(start);
						Date end_movie = format.parse(end);
						Date add_start_movie = format.parse(addstart);
						Date add_end_movie = format.parse(addend);
						
						
						if(add_end_movie.getTime() > start_movie.getTime() || end_movie.getTime() > add_start_movie.getTime()){
							errorPrint("Time error");
							if (ae.equals("add")) {
								add("date");
							}else {
								editData("change");
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}break;
	case 12:
		String[] edataArr = data.split(" ");
		if(edataArr.length != 5) {
			errorPrint("editData error");
			editData("change");
			break;
		}
	}
}
	
	public static void errorPrint(String error) {
		switch (error) {
		case "month range error":
			System.out.println("Error : Month should be number between 1 and 12");
			break;
		case "day range error":
			System.out.println("Error : Day should be number between 1 and 31");
			break;
		case "hour range error":
			System.out.println("Error : Time should be number between 1 and 24");
			break;
		case "min range error":
			System.out.println("Error : Minute should be number between 0 and 59");
			break;
		case "time order error":
			System.out.println("Error : Endtime is faster than starttime");
			break;
		case "theatertype error":
			System.out.println("Error : Theatertype should be 2D or 3D or 4D");
			break;
		case "title length error":
			System.out.println("Error : Movietitle should be more than one letter");
			break;
		case "answer error":
			System.out.println("Error : You should enter (save) or (cancel)");
			break;
		case "editData error":
			System.out.println("Error : You should enter theaterNum, movieTitle(no white space), theaterType, time, date");
			break;
		case "already exist data":
			System.out.println("Error : This data already exists in the datafile, Please enter another data");
			break;
		case "Time error":
			System.out.println(
					"Error : You can't add data that has same theater at the same time same day, Please enter data again");
			break;
		case "theaterNum error":
			System.out.println("Error : There is no theater has that number");
			break;
		case "time start with 0":
			System.out.println("Error : You can't enter time like 00 01 02 ...");
			break;
		case "type error":
			System.out.println("Error : This theater is not possible for 4D movie");
			break;
		case "wrong input data":
			System.out.println("Error : There is no data in datafile same as you enter, Please enter another data");
			break;
		case "theaterNum outof bound":
			System.out.println("Error : There is no theaternumber you enter, Please enter another number");
			break;
		case "date Format error":
			System.out.println("Error : You have to enter date, format is month/date");
			break;
		case "time format error":
			System.out.println(("Error : You have to enter time, format is time:min~time:min"));
			break;
		case "theaterNum format error":
			System.out.println(("Error : You have to enter theater number, format is number"));
			break;
		case "movieTitle format error":
			System.out.println("Error : You can't enter movie title including space");
			break;
		case "31 error":
			System.out.println("Error : There is no 31 in month 2,4,6,9,11");
			break;
		case "30 error":
			System.out.println("Error : There is no 30 in month 2");
			break;
		case "edit answer error":
			System.out.println("Error : You have to enter (change) or (cancel)");
			break;
		case "time range error":
			System.out.println("Error : You can't enter 1:0 also 24:1");
			break;
		case "movieTitle include num":
			System.out.println("Error : You can't enter movietitle include digit");
			break;
		}
	}
	
	public static void saveDataFile(int num, String[][] arr) { // 데이터 파일에 저장하는 메소드
		switch (num) {
		case 1:
			System.out.println("Do you want to save?(save/cancel)");
			System.out.println(inputData);
			System.out.print(">>");
			String answer = sc.nextLine();
			dataCheck(6, answer, "add");

		case 2:
			String[] dataArr = saveData.split(" ");
			String theaterNum = dataArr[0];
			String movieTitle = dataArr[1];
			String theaterType = dataArr[2];
			String time = dataArr[3];
			String date = dataArr[4];
			int line = Integer.parseInt(theaterNum);
			String[] screenSit = screenLine(line);
			File file = new File(fileName);
			/*
			 * if(file.exists()) { try { BufferedWriter bw = new BufferedWriter(new
			 * FileWriter(fileName, true)); bw.newLine(); bw.close(); }catch (IOException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); } }
			 */
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
				for (int i = 2; i < screenSit.length; i++) {
					bw.write(screenSit[i]);
					bw.write("\t");
				}
				bw.newLine();
				bw.write(">\n");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LogIn.inputOrder();
		case 3:
			String data = null;
			for (int i = 0; i < arr.length; i++) {
				data = arr[i] + " ";
			}
			//dataCheck(9, data, "edit");
			file = new File(fileName);
			String temp = "";
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
				for (int i = 0; i < arr.length; i++) {
					bw.write("<");
					bw.newLine();
					for (int j = 0; j < arr[i].length; j++) {
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

	public static void editData(String data) {// 변경전 데이터와 변경후 데이터를 입력받는 메소드
		File file = new File(fileName);
		String[] dataArr = null;

			switch(data) {
			case "select":  	
			if (file.exists()) {
				showMovieList();
			System.out.println(
					"Please enter data that you want to edit\n" + "(theaterNum, movieTitle, theaterType , time, date)");
			System.out.print(">>");
			saveData = sc.nextLine();
			dataArr = saveData.split(" ");
			dataCheck(8, saveData, "edit"); // 입력한 데이터가 파일에 존재하는지
			for (int i = 0; i < dataArr.length; i++) {
				dataCheck(i + 1, dataArr[i], null);
			}
			selectData = saveData;
			}else {
				System.out.println("Error : There is no DataFile. Please add File and try again");
				managerInput();
			}

			case "change" :
		
			System.out.println("Please enter data that you want to change\n"
					+ "(theaterNum, movieTitle, theaterType , time, date)");
			System.out.print(">>");
			editData = sc.nextLine();
			String[] edataArr = editData.split(" ");
			dataCheck(12, editData, null);
			for (int i = 0; i < edataArr.length; i++) {
				dataCheck(i + 1, edataArr[i], "edit");
			}
			dataArr =  selectData.split(" ");
			
			
			case "answer" :
			dataArr =  selectData.split(" ");	
			edataArr = editData.split(" ");
			System.out.println("you want to change (" + saveData + ") to (" + editData + ")?(change/cancel)");
			System.out.print(">>");
			String answer = sc.nextLine();
			dataCheck(6, answer, "edit");
			dataCheck(11, editData, "edit");
			saveDataFile(3, changeData(dataArr, edataArr));
			LogIn.inputOrder();
			} 
			
		}
	
	
	public static String[][] changeData(String[] arr, String[] changeArr) {// 데이터를 변경하는 메소드
		String[][] movie_info_list = moiveInf();
		int changeLine = findData(arr);
		for (int i = 0; i < arr.length; i++) {
			movie_info_list[changeLine][i] = changeArr[i];
		}
		return movie_info_list;
	}


	private static String[] screenInf() {
		ArrayList<String> seat_list = new ArrayList<String>();
		try {
			File seatInf = new File("./ScreenInf.txt");
			BufferedReader br = new BufferedReader(new FileReader(seatInf));
			String temp;
			while ((temp = br.readLine()) != null)
				seat_list.add(temp);
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int x = 0;
		int y = 0;

		// for(String a : seat_list) {if(a.equals(">")) x++;}
		// ArrayList<String>[][] screen_line_list = new ArrayList[seat_list.size()][];
		String[] screen_line_list = new String[seat_list.size()];
		for (String a : seat_list) {
			screen_line_list[x] = a;
			x++;
		}

		return screen_line_list;

	}


	private static String[][] moiveInf() { // 영화 정보를 찾고 영화 정보 배열에 저장
		ArrayList<String> info_list = new ArrayList<String>();
		try {
			File movieInf = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(movieInf));
			String temp;
			while ((temp = br.readLine()) != null)
				info_list.add(temp);
			while ((temp = br.readLine()) != null)
				cnt++;
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int x = 0;
		int y = 0;
		for (String a : info_list) {
			if (a.equals(">"))
				x++;
		} // 정보 개수 확인
		String[][] info_list_detail = new String[x][6]; // 6종류 데이터
		x = 0;
		for (String a : info_list) { // 한줄씩 받아서
			if (a.equals(">")) {
				x++;
				y = 0;
			} else if (!a.equals("<")) {
				info_list_detail[x][y] = a; // 한줄에 한칸 넣기
				y++;
			}
		}

		return info_list_detail;
	}

	private static String[] screenLine(int i) {
		String[] screenInf = screenInf();
		String screeninf_list[] = screenInf[i - 1].split("\t");
		return screeninf_list;
	}

	private static int findData(String[] arr) { // 변경할 영화를 찾는 메소드
		String[][] movie_info_list = moiveInf();
		int find, line = movie_info_list.length + 1;
		try {
			for (int i = 0; i < movie_info_list.length; i++) {
				find = 0;
				for (int q = 0; q < movie_info_list[i].length - 1; q++) {
					if (movie_info_list[i][q].equals(arr[q])) {
						find++;
					} else
						break;
				}
				if (find == 5) {
					line = i;
					break;
				}
			}
			//System.out.println(line);
			//return line;
		} catch(ArrayIndexOutOfBoundsException e) {
			errorPrint("editData error");
			editData("select");
			
		}
		return line;
	}
	private static void showMovieList() { // 파일에 있는 영화 리스트 보여주기
		String[][] movie_info_list = moiveInf();
		for (int i = 0; i < movie_info_list.length; i++) {
			for (int j = 0; j < movie_info_list[i].length - 1; j++) {
				System.out.print(movie_info_list[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public static boolean spaceCheck(String spaceCheck)
	{
	    for(int i = 0 ; i < spaceCheck.length() ; i++)
	    {
	        if(spaceCheck.charAt(i) == ' ')
	            return true;
	    }
	    return false;
	}
	
	public static boolean numCheck(String data) {//영화제목이 영문자로만 이루어져있는지 판별
		char tmp;
		boolean ret = true;
		for(int i = 0 ; i < data.length() ; i++)
	    {
			tmp = data.charAt(i);
	    
	        if('0' <= tmp && tmp <= '9') {
	        	ret = false;
	    }
	
	    }
		return ret;
	}
	
}