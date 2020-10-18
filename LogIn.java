import java.io.IOException;
import java.util.Scanner;

public class LogIn {
//    고쳐야 할 것
//    1. IntelliJ 에서의 실행이 shift + f10 이 아니라, alt + shift + f10과 (Main class를 선택할)엔터
//    2. 7.1 "입력:" 한글임ㅠ

    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        inputOrder();
    }
    private static void inputOrder(){
//        1. Log in 2. Sign up 3. Exit
//        입력:
        System.out.println("1. Log in 2. Sign up 3. Exit");
        System.out.print("입력:");
        String order = scan.nextLine();
        switch (order){
            case "1":
                logIn();
                break;
            case "2":
                signUp();
                break;
            case "3":
                System.out.println("3. Exit"); // 지울 것
                System.exit(0);
                break;
            default:
                System.out.println("ERROR : Wrong input");
                inputOrder();
                break;
        }
    }

    private static void signUp() {
//        물어볼 것
//        1. a와 b로 이루어진 문자열이면, 둘 다 꼭 필수로 들어가야 한다는 뜻인가
//        2. "input:" 뒤에 공백이 있고 없고 까지 정확히 구분하여 기획서와 똑같이 만들어야 하는가
        System.out.println("2. Sign up"); // 지울 것
        String ID = signUpID(0);
        String PW = signUpPW(0);
        System.out.println("ID : " + ID + ", PW : " + PW); // 지울 것



        System.out.print("Registration is complete. Press Enter to go to the login.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputOrder();
    }

    private static String signUpID(int errorCode) {
//        Enter 5~20 letters and numbers
//        ID:
        if (errorCode == 0)
            System.out.println("Enter 5~20 letters and numbers");
        else if (errorCode == 1)
            System.out.println("Enter only 5~20 letters and numbers");
        else if (errorCode == 2)
            System.out.println("Already exists ID");

        System.out.print("ID: ");
        String ID = scan.nextLine();
        ID = checkID(ID);
        return ID;
    }

    private static String checkID(String ID) {
//        길이가 5이상 20이하
//        공백 포함하지 않음
//        영문 소문자와 숫자로 이루어진 문자열
        if (ID.length() < 5 || ID.length() > 20) // 문법규칙 아직 길이제한만
            ID = signUpID(1);
        else if (existID()) // 의미규칙
            ID = signUpID(2);

        return ID;
    }

    private static boolean existID() {
        //아직 무조건 안 존재
        return false;
    }

    private static String signUpPW(int errorCode) {
//        Enter 8~16 letters, uppercase and lowercase letters, numbers and special characters.
//        PW:
        if (errorCode == 0)
            System.out.println("Enter 8~16 letters, uppercase and lowercase letters, numbers and special characters.");
        else if (errorCode == 1)
            System.out.println("Enter only 8~16 letters, uppercase and lowercase letters, numbers and special characters.");

        System.out.print("PW: ");
        String PW = scan.nextLine();
        PW = checkPW(PW);
        return PW;
    }

    private static String checkPW(String PW) {
//        고쳐야 할 것
//        1. (추가) 영문과 특수문자와 숫자를 포함

//        길이가 8이상 16이하
//        공백 포함하지 않음
//        로마자 영문 대/소문자를 구분
//        특수문자 포함
        if (PW.length() < 8 || PW.length() > 16) // 문법규칙 아직 길이제한만
            PW = signUpPW(1);

        return PW;
    }

    private static void logIn() {
        System.out.println("1. Log in"); // 지울 것
        System.out.print("ID:");
        String ID = scan.nextLine();
    }
}
