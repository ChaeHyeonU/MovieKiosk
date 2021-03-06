import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LogIn {
    static String nowID;
    static Scanner scan = new Scanner(System.in);
//    public static void main(String[] args) {
//        inputOrder();
//    }
    protected static void inputOrder(){
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
        String ID = signUpID(0);
        String PW = signUpPW(0);
        // 데이터 파일에 회원정보 등록
        register(ID, PW);

        System.out.print("Registration is complete. Press Enter to go to the login.");
        scan.nextLine();
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
            System.out.println("Enter 5~20 characters, only including at least 1 English lowercase letter and 1 number");
        else if (errCode == 1)
            System.out.println("Enter 5~20 characters");
        else if (errCode == 2)
            System.out.println("Don't type white spaces");
        else if (errCode == 3)
            System.out.println("Enter 5~20 characters, including at least 1 English lowercase letter");
        else if (errCode == 4)
            System.out.println("Enter 5~20 characters, including at least 1 number");
        else if (errCode == 5)
            System.out.println("Enter 5~20 characters, \"MUST ONLY\" including at least 1 English lowercase letter and 1 number");
        else if (errCode == 6)
            System.out.println("Already exists ID");

        System.out.print("ID: ");
        String ID = scan.nextLine();
        return checkID(ID);
    }

    private static String checkID(String ID) {
//        길이가 5이상 20이하
//        공백 포함하지 않음
//        하나 이상의 영문 소문자와 하나 이상의 숫자로 이루어짐
        int ch;
        if (ID.length() < 5 || ID.length() > 20) // 길이
            return signUpID(1);
        else if (includeBlank(ID)) // 공백X
            return signUpID(2);
        else if ((ch = checkIDChar(ID)) != 0) // 필수 문자만 포함
            return signUpID(ch);
        else if (existID(ID)) // 의미규칙
            return signUpID(6);

        return ID;
    }

    private static int checkIDChar(String ID) {
        boolean chSEng = false, chNum = false;
        int errCode = 0;
        for (int i = 0; i < ID.length(); i++) {
            if (Pattern.matches("[a-z]", String.valueOf(ID.charAt(i)))) {
                chSEng = true;
            } else if (Pattern.matches("[0-9]", String.valueOf(ID.charAt(i)))) {
                chNum = true;
            } else
                return 5; // 다른게 있음
        }
        if (!chSEng)
            errCode = 3; // 영어소문자
        else if (!chNum)
            errCode = 4; // 숫자

        return errCode;
    }

    private static boolean existID(String ID) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./memberInfo.txt"));
            String str;
            while ((str = br.readLine()) != null){
                // str에 개행 전까지 받아옴
//                System.out.println(str);
                if (str.equals("{")){
                    String compID = br.readLine();
                    if (compID.equals(ID)){
                        br.close();
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
            System.out.println("Enter 8~16 characters, only including at least 1 alphabet, 1 number and 1 special character(~!@#$%^&*_+=-,./?)");
        else if (errCode == 1)
            System.out.println("Enter 8~16 characters");
        else if (errCode == 2)
            System.out.println("Don't type white spaces");
        else if (errCode == 3)
            System.out.println("Enter 8~16 characters, including at least 1 alphabet");
        else if (errCode == 4)
            System.out.println("Enter 8~16 characters, including at least 1 number");
        else if (errCode == 5)
            System.out.println("Enter 8~16 characters, including at least 1 special character(~!@#$%^&*_+=-,./?)");
        else if (errCode == 6)
            System.out.println("Enter 8~16 characters, \"MUST ONLY\" including at least 1 alphabet, 1 number and 1 special character(~!@#$%^&*_+=-,./?)");


        System.out.print("PW: ");
        String PW = scan.nextLine();
        return checkPW(PW);
    }

    private static String checkPW(String PW) {
//        길이가 8이상 16이하
//        공백 포함하지 않음
//        로마자 영문 대/소문자를 구분
//        하나이상의 영문자와 하나이상의 숫자와 하나 이상의 특수문자(~!@#$%^&*_+=-,./?)로 이루어짐
        int ch;
        if (PW.length() < 8 || PW.length() > 16) // 길이
            return signUpPW(1);
        else if (includeBlank(PW)) // 공백X
            return signUpPW(2);
        else if ((ch = checkPWChar(PW)) != 0) // 필수 문자만 포함
            return signUpPW(ch);

        return PW;
    }

    private static int checkPWChar(String PW) {
        boolean chEng = false, chNum = false, chSp = false;
        int errCode = 0;
        for (int i = 0; i < PW.length(); i++) {
            if (Pattern.matches("[a-zA-z]", String.valueOf(PW.charAt(i)))) {
                chEng = true;
            } else if (Pattern.matches("[0-9]", String.valueOf(PW.charAt(i)))) {
                chNum = true;
            } else if (Pattern.matches("[~!@#$%^&*_+=\\-,./?]", String.valueOf(PW.charAt(i)))) {
                chSp = true;
            } else
                return 6; // 다른게 있음
        }
        if (!chEng)
            errCode = 3; // 영어
        else if (!chNum)
            errCode = 4; // 숫자
        else if (!chSp)
            errCode = 5; // 특수문자

        return errCode;
    }

    private static boolean includeBlank(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i)))
                return true;
        }
        return false;
    }

    private static void logIn() {
        nowID = logInID();
        if (logInPW(nowID) == 0) // 관리자라면 메뉴 실행
            MovieManager.managerInput();
        else // 7.2 실행 및 nowID 전달
            Select.selectOrder();
    }

    private static String logInID() {
        System.out.print("ID: ");
        String ID = scan.nextLine();
        if (ID.equals("admin")) // 존재 검사하기 전에 관리자면 바로 비번입력
            return ID;
        if (ID.length() == 0 || includeBlank(ID) || !existID(ID)) {
            System.out.println("This ID is not registered");
            return logInID();
        }

        return ID;
    }

    private static int logInPW(String ID) {
        System.out.print("PW: ");
        String PW = scan.nextLine();
        if (ID.equals("admin") && PW.equals("secret123")) // 관리자 계정일 때
            return 0;
        else if(PW.length() == 0 || includeBlank(PW) || !matchID(ID, PW)){
            System.out.println("ID and password do not match");
            return logInPW(ID);
        }
        return 1;
    }

    private static boolean matchID(String ID, String PW) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./memberInfo.txt"));
            String str;
            while ((str = br.readLine()) != null){
                // str에 개행 전까지 받아옴
//                System.out.println(str);
                if (str.equals("{")){
                    if (br.readLine().equals(ID)) {
                        String compPW = br.readLine();
                        br.close();
                        return compPW.equals(PW);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
//            System.out.println("existID : 파일 없는데 뭐 어쩌라고");
        }
        return false;
    }
}
