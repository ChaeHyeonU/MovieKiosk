package movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Select {
    protected static void selectOrder(Movie m){
//        1. Reservation details 2. Movie reservation 3. Log out
//        >>
        System.out.println("1. Reservation details 2. Movie reservation 3. Log out");
        System.out.print(">> ");
        Scanner scan = new Scanner(System.in);
        String order = scan.nextLine();
        switch (order){
            case "1":
                details(m);
                break;
            case "2":
            	m.initmovielist();
            	m.searchselect();
                break;
            case "3":
                LogIn.inputOrder(m);
                break;
            default:
                System.out.println("ERROR : Wrong input");
                selectOrder(m);
                break;
        }
    }

    private static void details(Movie m) {
        printReport(LogIn.nowID);
        // ������ ������ printReport�� �ƹ� ��µ� ���ϰ� ���� ����
        System.out.print("Press Enter to go to previous page.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectOrder(m);
    }

    private static void printReport(String nowID) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./memberInfo.txt"));
            String str;
            while ((str = br.readLine()) != null){
                // str�� ���� ������ �޾ƿ�
//                System.out.println(str);
                if (str.equals("{")) {
                    if (br.readLine().equals(nowID)){
                        br.readLine(); // ��й�ȣ
                        break;
                    }
                }
            }

            String detail;
            while (!(detail = br.readLine()).equals("}")){ // "}"�� �ƴ� �� ����
                System.out.println(detail);
            }
            br.close();
        } catch (IOException e) {
//            System.out.println("existID : ���� ���µ� �� ��¼���");
        }
    }
}
