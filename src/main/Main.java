package main;

import config.config;
import java.util.Scanner;

public class Main {
    
    
    public static void viewborrow() {
        String Query = "SELECT * FROM tbl_borrow";
        String[] votersHeaders = {"BID", "Bok_ID", "UID", "B_DATE", "B_RETURN"};
        String[] votersColumns = {"b_id", "bok_id", "u_id", "b_date", "b_return"};
        config conf = new config();
        conf.viewRecords(Query, votersHeaders, votersColumns);
    }
    
    public static void viewbooks() {
        String Query = "SELECT * FROM tbl_books";
        String[] votersHeaders = {"BID", "Title", "Author"};
        String[] votersColumns = {"bok_id", "bok_title", "bok_author"};
        config conf = new config();
        conf.viewRecords(Query, votersHeaders, votersColumns);
    }

    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_user";
        String[] votersHeaders = {"UID", "Name", "Email", "Status"};
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
                                    System.out.println("WELCOME TO LIBRIAN DASHBOARD");
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
        System.out.println("4. Add Customer (Reader): "); // New option
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
                int bookChoice;
                do {
                    System.out.println("==== BOOK MENU ====");
                    System.out.println("1. Add Book");
                    System.out.println("2. View Books");
                    System.out.println("3. Update Book");
                    System.out.println("4. Delete Book");
                    System.out.println("5. Back");
                    System.out.print("Enter choice: ");
                    bookChoice = sc.nextInt();

                    switch (bookChoice) {
                        case 1:
                            System.out.print("Enter Book Title: ");
                            String title = sc.next();
                            System.out.println("Enter Author: ");
                            String author = sc.next();
                            
                            String addBookSQL = "INSERT INTO tbl_books (bok_title, bok_author) VALUES (?, ?)";
                            conf.addRecord(addBookSQL, title, author);
                            System.out.println("Book added successfully!");
                            break;

                        case 2:
                            viewbooks();
                            break;

                        case 3:
                            viewbooks();
                            
                            System.out.print("Enter Book ID to Update: ");
                            int bid = sc.nextInt();
                            System.out.print("Enter New Title: ");
                            title = sc.next();
                            System.out.print("Enter New Author: ");
                            author = sc.next();
                            
                            String updateBookSQL = "UPDATE tbl_books SET bok_title = ?, bok_author = ? WHERE bok_id = ?";
                            conf.updateRecord(updateBookSQL, title, author, bid);
                            System.out.println("Book updated successfully!");
                            viewbooks();
                            break;

                        case 4:
                            viewbooks();

                            System.out.print("Enter Book ID to Delete: ");
                            int delid = sc.nextInt();
                            String deleteBookSQL = "DELETE FROM tbl_books WHERE bok_id = ?";
                            conf.deleteRecord(deleteBookSQL, delid);
                            System.out.println("Book deleted successfully!");
                            viewbooks();
                            break;

                        case 5:
                            System.out.println("Going back to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }
                } while (bookChoice != 5);
                break;

            case 3:
                int borrowChoice;
                do {
                    System.out.println("==== BORROWED BOOK MENU ====");
                    System.out.println("1. Borrow a Book");
                    System.out.println("2. View Borrowed Books");
                    System.out.println("3. Return a Book");
                    System.out.println("4. Back");
                    System.out.print("Enter choice: ");
                    borrowChoice = sc.nextInt();

                    switch (borrowChoice) {
                        case 1:
                            System.out.println("Select User:");
                            viewUsers();
                            System.out.print("Enter User ID: ");
                            int uid = sc.nextInt();

                            System.out.println("Select Book:");
                            viewbooks();
                            System.out.print("Enter Book ID: ");
                            int bid = sc.nextInt();
                            

                            System.out.print("Enter Borrow Date: ");
                            String bdate = sc.next();
                            System.out.print("Enter Return Date: ");
                            String rdate = sc.next();
                            
                            if (bdate == rdate) {
                                System.out.println("Error: Borrow date and return date cannot be the same!");
                                break; // stop this case and go back to the menu
                            }

                            String addBorrowSQL = "INSERT INTO tbl_borrow (bok_id, u_id, b_date, b_return) VALUES (?, ?, ?, ?)";
                            conf.addRecord(addBorrowSQL, bid, uid, bdate, rdate);
                            System.out.println("Book borrowed successfully!");
                            break;

                        case 2:
                            viewborrow();
                            break;

                        case 3:
                            viewborrow();

                            System.out.print("Enter Borrow ID to delete (return): ");
                            int brid = sc.nextInt();
                            String deleteBorrowSQL = "DELETE FROM tbl_borrow WHERE b_id = ?";
                            conf.deleteRecord(deleteBorrowSQL, brid);
                            System.out.println("Book marked as returned (record deleted)!");
                            viewborrow();
                            break;

                        case 4:
                            System.out.println("Going back to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }
                } while (borrowChoice != 4);
                break;

            case 4: // New case for adding a reader/customer
                System.out.println("==== ADD READER (CUSTOMER) ====");
                System.out.println("Enter Reader Name: ");
                String readerName = sc.next();
                System.out.println("Enter Reader Email: ");
                String readerEmail = sc.next();

                // Ensure the email is unique
                while (true) {
                    String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
                    java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, readerEmail);

                    if (result.isEmpty()) {
                        break;
                    } else {
                        System.out.println("Email Already Exists! Enter another email: ");
                        readerEmail = sc.next();
                    }
                }

                String readerSql = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES (?, ?, ?, ?, ?)";
                conf.addRecord(readerSql, readerName, readerEmail, "Customer", "Customer", "defaultPassword");
                System.out.println("Reader (Customer) added successfully!");
                break;

            case 5:
                System.out.println("Exiting...");
                break;

            default:
                System.out.println("Invalid choice!");
        }
    } while (choice != 5);
}
}

