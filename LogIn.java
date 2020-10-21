import java.io.*;
import java.util.Scanner;

public class LogIn {
//    고쳐야 할 것
//    1. IntelliJ 에서의 실행이 shift + f10 이 아니라, alt + shift + f10과 (실행할 main 함수가 포함된 class를 선택 후)엔터

    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        inputOrder();
    }
    private static void inputOrder(){
//        1. Log in 2. Sign up 3. Exit
//        입력:
        System.out.println("1. Log in 2. Sign up 3. Exit");
        System.out.print(">> ");
        String order = scan.nextLine();
        switch (order){
            case "1":
                logIn();
                break;
            case "2":
                signUp();
                break;
            case "3":
                System.exit(0);
                break;
            default:
                System.out.println("ERROR : Wrong input");
                inputOrder();
                break;
        }
    }

    private static void signUp() {
//        ID, PW 둘 다 문법규칙 세부적 조정할 것
        String ID = signUpID(0);
        String PW = signUpPW(0);
        // 데이터 파일에 회원정보 등록
        register(ID, PW);

        System.out.print("Registration is complete. Press Enter to go to the login.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputOrder();
    }

    private static void register(String ID, String PW) {
        String str = new String();
        try {
            // 기존 회원정보 str에 불러오기
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("./memberInfo.txt"));
            byte [] buffer = new byte [bis.available()];
            int buff;
            while((buff = bis.read(buffer)) != -1) {
                if (buff == 0) // 데이터 없을 시
                    break;
            }
            str = new String(buffer);
            bis.close();
        } catch (IOException e) {
//            System.out.println("register : 파일 없는데 뭐 어쩌라고");
        }
        try {
            // 새로운 회원정보 str에 추가 후 파일에 입력하기
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("./memberInfo.txt"));
            str +="{\n";
            str += ID + "\n";
            str += PW + "\n";
            str += "}\n";
            bos.write(str.getBytes());
            bos.close();
        } catch (IOException e) {
        }
    }

    private static String signUpID(int errCode) {
//        Enter 5~20 letters and numbers
//        ID:
        if (errCode == 0)
            System.out.println("Enter 5~20 characters, including at least 1 english lowercase letter and 1 number.");
        else if (errCode == 1)
            System.out.println("\"MUST\" 5~20 characters, including at least 1 english lowercase letter and 1 number.");
        else if (errCode == 2)
            System.out.println("Already exists ID");

        System.out.print("ID: ");
        String ID = scan.nextLine();
        ID = checkID(ID);
        return ID;
    }

    private static String checkID(String ID) {
//        길이가 5이상 20이하
//        공백 포함하지 않음
//        하나 이상의 영문 소문자와 하나 이상의 숫자로 이루어진 문자열
        if (ID.length() < 5 || ID.length() > 20) // 문법규칙 아직 길이제한만
            ID = signUpID(1);
        else if (existID(ID)) // 의미규칙
            ID = signUpID(2);

        return ID;
    }

    private static boolean existID(String ID) {
        try {
            // bis 방식
//            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("./memberInfo.txt"));
//            byte [] b = new byte [bis.available()];
//            while( bis.read(b) != -1) {}
//            System.out.println(new String(b));
//            bis.close();
            // br 방식 (한줄씩)
            BufferedReader br = new BufferedReader(new FileReader("./memberInfo.txt"));
            String str;
            while ((str = br.readLine()) != null){
                // str에 개행 전까지 받아옴
//                System.out.println(str);
                if (str.equals("{")){
                    String compID = br.readLine();
                    if (compID.equals(ID)){
                        return true;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
//            System.out.println("existID : 파일 없는데 뭐 어쩌라고");
        }
        return false;
    }

    private static String signUpPW(int errCode) {
//        Enter 8~16 characters, including at least 1 alphabet, 1 number and 1 special character.
//        PW:
        if (errCode == 0)
            System.out.println("Enter 8~16 characters, including at least 1 alphabet, 1 number and 1 special character.");
        else if (errCode == 1)
            System.out.println("\"MUST\" 8~16 characters, including at least 1 alphabet, 1 number and 1 special character.");

        System.out.print("PW: ");
        String PW = scan.nextLine();
        PW = checkPW(PW);
        return PW;
    }

    private static String checkPW(String PW) {
//        길이가 8이상 16이하
//        공백 포함하지 않음
//        로마자 영문 대/소문자를 구분
//        하나이상의 영문자와 하나이상의 숫자와 하나 이상의 특수문자로 이루어진 문자열
        if (PW.length() < 8 || PW.length() > 16) // 문법규칙 아직 길이제한만
            PW = signUpPW(1);

        return PW;
    }

    private static void logIn() {
        System.out.println("1. Log in"); // 지울 것
        System.out.print("ID: ");
        String ID = scan.nextLine();
    }
}
