package main;

import config.config;
import java.util.Scanner;

public class Main {

    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_user";
        String[] votersHeaders = {"UID", "Name","Email", "Status"};
        String[] votersColumns = {"u_id", "u_name", "u_email", "u_status"};
        config conf = new config();
        conf.viewRecords(Query, votersHeaders, votersColumns);
    }

    public static void main(String[] args) {
        char cont;
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        do {
            System.out.println("==== MAIN MENU ====");
            System.out.println("1. Login: ");
            System.out.println("2. Register: ");
            System.out.println("3. Exit: ");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
    System.out.println("Enter Email: ");
    String em = sc.next();
    System.out.println("Enter Password: ");
    String pass = sc.next();

    // Check if email exists before password validation
    String emailCheckQry = "SELECT * FROM tbl_user WHERE u_email = ?";
    java.util.List<java.util.Map<String, Object>> emailCheckResult = conf.fetchRecords(emailCheckQry, em);

    if (emailCheckResult.isEmpty()) {
        System.out.println("Account is not registered. Please register first.");
        break;  // Exit this case early
    }

    while (true) {
        String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, em, pass);

        if (result.isEmpty()) {
            System.out.println("INVALID CREDENTIALS");
            break;
        } else {
            java.util.Map<String, Object> user = result.get(0);
            String stat = user.get("u_status").toString();
            String type = user.get("u_type").toString();
            if (stat.equals("Pending")) {
                System.out.println("Account is Pending, Contact the Admin!");
                break;
            } else {
                System.out.println("LOGIN SUCCESS!");
                if (type.equals("Admin")) {
                    System.out.println("WELCOME TO ADMIN DASHBOARD");
                    System.out.println("1. Approve Account!");
                    int respo = sc.nextInt();

                    switch (respo) {
                        case 1:
                            viewUsers();
                            System.out.print("Enter ID to Approve: ");
                            int ids = sc.nextInt();

                            String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                            conf.updateRecord(sql, "Approved", ids);
                            break;
                    }
                } else if (type.equals("Librian")) {
                    System.out.println("WELCOME TO TEACHER DASHBOARD");
                    librianMenu();
                }
            }
        }
        break;
    }
    break;


                case 2:
                    System.out.print("Enter user name: ");
                    String name = sc.next();
                    System.out.print("Enter user email: ");
                    String email = sc.next();

                    while (true) {
                        String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
                        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, email);

                        if (result.isEmpty()) {
                            break;
                        } else {
                            System.out.println("Email Already Exists");
                            System.out.println("Enter other Email: ");
                            email = sc.next();
                        }
                    }

                    System.out.print("Enter user Type (1 - Admin/2 -Librian): ");
                    int type = sc.nextInt();
                    while (type > 2 || type < 1) {
                        System.out.print("Invalid, choose between 1 & 2 only: ");
                        type = sc.nextInt();
                    }
                    String tp = "";
                    if (type == 1) {
                        tp = "Admin";
                    } else {
                        tp = "Librian";
                    }

                    System.out.print("Enter Password: ");
                    pass = sc.next();

                    String sql = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES (?, ?, ?, ?, ?)";
                    conf.addRecord(sql, name, email, tp, "Pending", pass);
                    break;

                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }

            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');

        System.out.println("Thank you! Program ended.");
    }
    
    public static void librianMenu() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        int choice;

        do {
            System.out.println("==== MAIN MENU ====");
            System.out.println("1. Add User: ");
            System.out.println("2. Add Books: ");
            System.out.println("3. Add Borrowed Book: ");
            System.out.println("5. Exit ");
            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    int userchoice;
                    System.out.println("==== USER MENU ====");
                    System.out.println("1. Add User");
                    System.out.println("2. View User");
                    System.out.println("3. Update User");
                    System.out.println("4. Delete User");
                    System.out.println("5. Back");
                    userchoice = sc.nextInt();

                    switch (userchoice) {
                        case 1:
                            System.out.println("Enter Name: ");
                            String name = sc.next();
                            System.out.println("Enter Email: ");
                            String em = sc.next();

                            String sql = "INSERT INTO tbl_user (u_name, u_email) VALUES (?, ?)";
                            conf.addRecord(sql, name, em);
                            break;

                        case 2:
                            viewUsers();
                            break;

                        case 3:
                            viewUsers();
                            System.out.println("Enter id to update: ");
                            int id = sc.nextInt();
                            System.out.println("Enter new Name: ");
                            name = sc.next();
                            
                            System.out.println("Enter new Email: ");
                            em = sc.next();
                            String qry = "UPDATE tbl_user SET u_name = ?, u_contact = ?, u_email = ? WHERE u_id = ?";
                            conf.updateRecord(qry, name, em, id);
                            viewUsers();
                            break;

                        case 4:
                            viewUsers();
                            System.out.println("Enter id to delete: ");
                            id = sc.nextInt();
                            qry = "DELETE FROM tbl_user WHERE u_id = ?";
                            conf.deleteRecord(qry, id);
                            viewUsers();
                            break;

                        case 5:
                            System.out.println("Going back to main menu...");
                            break;

                        default:
                            System.out.println("Invalid Choice!");
                    }
                    break;

                case 2:
                    System.out.println("Add Books functionality goes here...");
                    break;

                case 3:
                    System.out.println("Add Borrowed Book functionality goes here...");
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 5);
    }

}
