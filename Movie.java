
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Movie {

	public static void main(String[] args){
		
//		try {
//			Util.printScreen();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		clearScreen();
		int sizeoflist = 0;		
		ArrayList<String> screenlist = new ArrayList<String>();
		ArrayList<String> movielist = new ArrayList<String>();
		ArrayList<String> timelist = new ArrayList<String>();
		ArrayList<String> daylist = new ArrayList<String>();
		//ArrayList<String> seatlist = new ArrayList<String>();
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
					timelist.add(line);
					break;
				case 4:
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
			
		} catch(IOException e) {
			System.out.println(e);
		}
		
		for(int i = 0 ; i < sizeoflist; i++) {
			System.out.println(daylist.get(i) + " " + timelist.get(i) + " Screen" + screenlist.get(i) + " " + movielist.get(i) );
		}
		System.out.println();
		System.out.print(">>");

	}
	
	public static void clearScreen() {  
		for(int i =0 ; i < 50 ; i++)
			System.out.println();
	}  
}
