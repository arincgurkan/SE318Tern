import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class main {
    static PreparedStatement smt;
    static Connection conn;

    static Scanner scanner = new Scanner(System.in);
    static String loggedInEmail = null;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:/Users/arincgurkan/IdeaProjects/SE318Tern/main.java.db");

            Statement statement = conn.createStatement();
            //statement.execute("CREATE TABLE users (name TEXT, email TEXT, password TEXT)");
            //statement.execute("INSERT INTO users (name, email, password) VALUES('gozde', 'gozde@gmail.com', '123456')");
            //statement.execute("INSERT INTO users (name, email, password) VALUES('alper', 'alper@gmail.com', '123456')");
            //statement.execute("INSERT INTO users (name, email, password) VALUES('mert', 'mert@gmail.com', '123456')");

            menu();
            menuLoop();

            statement.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage()
            );
        }
    }

    public static boolean Login(String email, String password) throws SQLException {
        try {
            smt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        smt.setString (1, email);
        smt.setString (2, password);
        ResultSet rs = smt.executeQuery();

        if (rs.next()) {
            return true;
        }
        else return false;
    }

    // This method takes three parameters and returns nothing.
    public static void signUp(String name, String email, String password) {
        try {
            smt=conn.prepareStatement("insert into users values(?,?,?)");

            smt.setString(1,name);
            smt.setString(2,email);
            smt.setString(3,password);
            smt.executeUpdate();
            System.out.println("Your account successfully created " + name + "!");
        }catch(Exception e){ System.out.println(e);}

    }

    public static void menu() {
        // Show the to user and ask potential inputs from them
        System.out.println("Welcome to 318!!");
        System.out.println("Select 1 for Login \n" +
                "Select 2 for Sign up \n" +
                "Select 0 for quiting system");
    }

    public static void menuLoop() throws SQLException {
        // Store the choice in an integer
        int choice = scanner.nextInt();
        scanner.nextLine();
        // Define a parameter called loop for making infinite loop
        boolean loop = true;
        while(loop) {
            // Login part
            if (choice == 1) {
                // Get username from user
                System.out.println("Enter email: ");
                String email = scanner.nextLine();
                // Get password from user
                System.out.println("Enter password: ");
                String password = scanner.nextLine();
                // Check the given inputs are true
                if (Login(email,password)) {
                    loggedInEmail = email;
                    System.out.println("You are logged in!");
                    loop =false;
                }
                else {
                    System.out.println("Cannot login");
                    // If user enters wrong input start from the top
                    menu();
                    menuLoop();
                    loop = false;
                }
            }
            // Sign up part
            else if (choice == 2) {
                boolean loopForName = true;
                boolean loopForEmail = true;

                System.out.println("Enter your name: ");
                String name = scanner.nextLine();

                //Check whether name includes any numbers. If yes ask it again.
                if (!isWord(name)) {
                    while (loopForName) {
                        System.out.println("You typed wrong. Try it again.");
                        name = scanner.nextLine();
                        if (isWord(name)) {
                            loopForName = false;
                        }
                    }
                }

                System.out.println("Enter your email: ");
                String email = scanner.nextLine();

                //Check email is valid. If no ask it again.
                if (!isValidEmailAddress(email)) {
                    while (loopForEmail) {
                        System.out.println("You typed wrong. Try it again.");
                        email = scanner.nextLine();
                        if (isValidEmailAddress(email)) {
                            loopForEmail = false;
                        }
                    }
                }

                System.out.println("Enter password: ");
                String password = scanner.nextLine();

                signUp(name,email,password);
                loggedInEmail = email;
                loop = false;
            }
            // Closing system
            else if (choice == 0) {
                System.exit(0);
            }
            // When user enters invalid input ask it again
            else {
                System.out.println("Wrong input try again: ");
                choice = scanner.nextInt();
            }
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isWord(String name){
        return Pattern.matches("[a-zA-Z]+",name);
    }
}
