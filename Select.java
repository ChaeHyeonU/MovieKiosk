import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Select {
    static Scanner scan = new Scanner(System.in);
    protected static void selectOrder(){
//        1. Reservation details 2. Movie reservation 3. Log out
//        >>
        System.out.println("1. Reservation details 2. Movie reservation 3. Log out");
        System.out.print(">> ");
        String order = scan.nextLine();
        switch (order){
            case "1":
                details();
                break;
            case "2":
            	Movie m = new Movie();
            	m.initmovielist();
            	m.searchselect();
                break;
            case "3":
                LogIn.inputOrder();
                break;
            default:
                System.out.println("ERROR : Wrong input");
                selectOrder();
                break;
        }
    }

    private static void details() {
        printReport(LogIn.nowID);
        // 내역이 없으면 printReport가 아무 출력도 못하고 밑을 실행
        System.out.print("Press Enter to go to previous page.");
        scan.nextLine();
        selectOrder();
    }

    private static void printReport(String nowID) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./memberInfo.txt"));
            String str;
            while ((str = br.readLine()) != null){
                // str에 개행 전까지 받아옴
//                System.out.println(str);
                if (str.equals("{")) {
                    if (br.readLine().equals(nowID)){
                        br.readLine(); // 비밀번호
                        break;
                    }
                }
            }

            String detail;
            while (!(detail = br.readLine()).equals("}")){ // "}"가 아닐 때 까지
                System.out.println(detail);
            }
            br.close();
        } catch (IOException e) {
////            System.out.println("existID : 파일 없는데 뭐 어쩌라고");
        }
    }
}
