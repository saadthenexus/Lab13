/*
 * This class is intended to be the main class for this Project. All necessary methods are getting calls from this class.
 *
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    // Constants for array sizes and limits
    private static final int MAX_ADMIN_USERS = 10;
    private static final int MAX_TICKETS_PER_BOOKING = 10;
    
    // Constants for menu validation
    private static final int MIN_MENU_OPTION = 0;
    private static final int MAX_MAIN_MENU_OPTION = 5;
    private static final int MAX_ADMIN_MENU_OPTION = 8;
    private static final int MAX_MANUAL_OPTION = 2;
    
    static String[][] adminUserNameAndPassword = new String[MAX_ADMIN_USERS][2];
    private static List<Customer> customersCollection = new ArrayList<>();
    public static void main(String[] args) {
        int countNumOfUsers = 1;
        RolesAndPermissions r1 = new RolesAndPermissions();
        Flight f1 = new Flight();
        FlightReservation bookingAndReserving = new FlightReservation();
        Customer c1 = new Customer();
        f1.flightScheduler();
        Scanner read = new Scanner(System.in);

        displayWelcomeMessage();
        int desiredOption = getValidMenuOption(read);

        do {
            Scanner read1 = new Scanner(System.in);
            
            switch (desiredOption) {
                case 1:
                    adminLogin(r1, c1, f1, bookingAndReserving, read, read1);
                    break;
                case 2:
                    countNumOfUsers = registerAdmin(r1, countNumOfUsers, read1);
                    break;
                case 3:
                    passengerLogin(r1, c1, f1, bookingAndReserving, read, read1);
                    break;
                case 4:
                    c1.addNewCustomer();
                    break;
                case 5:
                    manualInstructions();
                    break;
            }

            displayMainMenu();
            desiredOption = getValidMenuOption(read1);
        } while (desiredOption != 0);
    }

    private static void displayWelcomeMessage() {
        System.out.println("\n\t\t\t\t\t+++++++++++++ Welcome to BAV AirLines +++++++++++++\n\nTo Further Proceed, Please enter a value.");
        System.out.println("\n***** Default Username && Password is root-root ***** Using Default Credentials will restrict you to just view the list of Passengers....\n");
    }

    private static int getValidMenuOption(Scanner scanner) {
        int option = scanner.nextInt();
        while (option < MIN_MENU_OPTION || option > MAX_MAIN_MENU_OPTION) {
            System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
            option = scanner.nextInt();
        }
        return option;
    }

    private static void adminLogin(RolesAndPermissions r1, Customer c1, Flight f1, FlightReservation bookingAndReserving, Scanner read, Scanner read1) {
        adminUserNameAndPassword[0][0] = "root";
        adminUserNameAndPassword[0][1] = "root";
        
        System.out.print("\nEnter the UserName to login to the Management System :     ");
        String username = read1.nextLine();
        System.out.print("Enter the Password to login to the Management System :    ");
        String password = read1.nextLine();
        System.out.println();

        int privilegeLevel = r1.isPrivilegedUserOrNot(username, password);
        handleAdminPrivileges(privilegeLevel, username, c1, f1, bookingAndReserving, read, read1);
    }

    private static void handleAdminPrivileges(int privilegeLevel, String username, Customer c1, Flight f1, 
            FlightReservation bookingAndReserving, Scanner read, Scanner read1) {
        if (privilegeLevel == -1) {
            System.out.printf("\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4....\n", "");
        } else if (privilegeLevel == 0) {
            System.out.println("You've standard/default privileges to access the data... You can just view customers data..." + "Can't perform any actions on them....");
            c1.displayCustomersData(true);
        } else {
            System.out.printf("%-20sLogged in Successfully as \"%s\"..... For further Proceedings, enter a value from below....", "", username);
            handleAdminOperations(username, c1, f1, bookingAndReserving, read, read1);
        }
    }

    private static void handleAdminOperations(String username, Customer c1, Flight f1, 
            FlightReservation bookingAndReserving, Scanner read, Scanner read1) {
        int desiredOption;
        do {
            displayAdminMenu(username);
            desiredOption = read.nextInt();
            
            switch (desiredOption) {
                case 1:
                    c1.addNewCustomer();
                    break;
                case 2:
                    handleCustomerSearch(c1, read1);
                    break;
                case 3:
                    handleCustomerUpdate(c1, read1);
                    break;
                case 4:
                    handleCustomerDelete(c1, read1);
                    break;
                case 5:
                    c1.displayCustomersData(false);
                    break;
                case 6:
                    handleFlightDisplay(c1, bookingAndReserving, read1);
                    break;
                case 7:
                    handlePassengerDisplay(f1, bookingAndReserving, read1);
                    break;
                case 8:
                    handleFlightDelete(f1, read1);
                    break;
                case 0:
                    System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
                    break;
                default:
                    System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                    desiredOption = 0;
            }
        } while (desiredOption != 0);
    }

    private static void handleCustomerSearch(Customer c1, Scanner read1) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Search :\t");
        String customerID = read1.nextLine();
        System.out.println();
        c1.searchUser(customerID);
    }

    private static void handleCustomerUpdate(Customer c1, Scanner read1) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Update its Data :\t");
        String customerID = read1.nextLine();
        if (customersCollection.size() > 0) {
            c1.editUserInfo(customerID);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
        }
    }

    private static void handleCustomerDelete(Customer c1, Scanner read1) {
        c1.displayCustomersData(false);
        System.out.print("Enter the CustomerID to Delete its Data :\t");
        String customerID = read1.nextLine();
        if (customersCollection.size() > 0) {
            c1.deleteUser(customerID);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
        }
    }

    private static void handleFlightDisplay(Customer c1, FlightReservation bookingAndReserving, Scanner read1) {
        c1.displayCustomersData(false);
        System.out.print("\n\nEnter the ID of the user to display all flights registered by that user...");
        String id = read1.nextLine();
        bookingAndReserving.displayFlightsRegisteredByOneUser(id);
    }

    private static void handlePassengerDisplay(Flight f1, FlightReservation bookingAndReserving, Scanner read1) {
        System.out.print("Do you want to display Passengers of all flights or a specific flight.... 'Y/y' for displaying all flights and 'N/n' to look for a specific flight.... ");
        char choice = read1.nextLine().charAt(0);
        if ('y' == choice || 'Y' == choice) {
            bookingAndReserving.displayRegisteredUsersForAllFlight();
        } else if ('n' == choice || 'N' == choice) {
            f1.displayFlightSchedule();
            System.out.print("Enter the Flight Number to display the list of passengers registered in that flight... ");
            String flightNum = read1.nextLine();
            bookingAndReserving.displayRegisteredUsersForASpecificFlight(flightNum);
        } else {
            System.out.println("Invalid Choice...No Response...!");
        }
    }

    private static void handleFlightDelete(Flight f1, Scanner read1) {
        f1.displayFlightSchedule();
        System.out.print("Enter the Flight Number to delete the flight : ");
        String flightNum = read1.nextLine();
        f1.deleteFlight(flightNum);
    }

    private static int registerAdmin(RolesAndPermissions r1, int countNumOfUsers, Scanner read1) {
        System.out.print("\nEnter the UserName to Register :    ");
        String username = read1.nextLine();
        System.out.print("Enter the Password to Register :     ");
        String password = read1.nextLine();
        
        while (r1.isPrivilegedUserOrNot(username, password) != -1) {
            System.out.print("ERROR!!! Admin with same UserName already exist. Enter new UserName:   ");
            username = read1.nextLine();
            System.out.print("Enter the Password Again:   ");
            password = read1.nextLine();
        }

        adminUserNameAndPassword[countNumOfUsers][0] = username;
        adminUserNameAndPassword[countNumOfUsers][1] = password;
        return countNumOfUsers + 1;
    }

    private static void passengerLogin(RolesAndPermissions r1, Customer c1, Flight f1, 
            FlightReservation bookingAndReserving, Scanner read, Scanner read1) {
        System.out.print("\n\nEnter the Email to Login : \t");
        String userName = read1.nextLine();
        System.out.print("Enter the Password : \t");
        String password = read1.nextLine();
        String[] result = r1.isPassengerRegistered(userName, password).split("-");

        if (Integer.parseInt(result[0]) == 1) {
            handlePassengerOperations(userName, result[1], c1, f1, bookingAndReserving, read, read1);
        } else {
            System.out.printf("\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4....\n", "");
        }
    }

    private static void handlePassengerOperations(String userName, String userId, Customer c1, Flight f1, 
            FlightReservation bookingAndReserving, Scanner read, Scanner read1) {
        int desiredChoice;
        System.out.printf("\n\n%-20sLogged in Successfully as \"%s\"..... For further Proceedings, enter a value from below....", "", userName);
        
        do {
            displayPassengerMenu(userName);
            desiredChoice = read.nextInt();
            
            switch (desiredChoice) {
                case 1:
                    handleFlightBooking(f1, bookingAndReserving, userId, read, read1);
                    break;
                case 2:
                    c1.editUserInfo(userId);
                    break;
                case 3:
                    if (handleAccountDeletion(userName, userId, c1, read1)) {
                        desiredChoice = 0;
                    }
                    break;
                case 4:
                    f1.displayFlightSchedule();
                    f1.displayMeasurementInstructions();
                    break;
                case 5:
                    bookingAndReserving.cancelFlight(userId);
                    break;
                case 6:
                    bookingAndReserving.displayFlightsRegisteredByOneUser(userId);
                    break;
                default:
                    if (desiredChoice != 0) {
                        System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                    }
                    desiredChoice = 0;
            }
        } while (desiredChoice != 0);
    }

    private static void handleFlightBooking(Flight f1, FlightReservation bookingAndReserving, 
            String userId, Scanner read, Scanner read1) {
        f1.displayFlightSchedule();
        System.out.print("\nEnter the desired flight number to book :\t ");
        String flightToBeBooked = read1.nextLine();
        System.out.print("Enter the Number of tickets for " + flightToBeBooked + " flight :   ");
        int numOfTickets = read.nextInt();
        while (numOfTickets > MAX_TICKETS_PER_BOOKING) {
            System.out.print("ERROR!! You can't book more than 10 tickets at a time for single flight....Enter number of tickets again : ");
            numOfTickets = read.nextInt();
        }
        bookingAndReserving.bookFlight(flightToBeBooked, numOfTickets, userId);
    }

    private static boolean handleAccountDeletion(String userName, String userId, Customer c1, Scanner read1) {
        System.out.print("Are you sure to delete your account...It's an irreversible action...Enter Y/y to confirm...");
        char confirmationChar = read1.nextLine().charAt(0);
        if (confirmationChar == 'Y' || confirmationChar == 'y') {
            c1.deleteUser(userId);
            System.out.printf("User %s's account deleted Successfully...!!!", userName);
            return true;
        } else {
            System.out.println("Action has been cancelled...");
            return false;
        }
    }

    private static void displayAdminMenu(String username) {
        System.out.printf("\n\n%-60s+++++++++ 2nd Layer Menu +++++++++%50sLogged in as \"%s\"\n", "", "", username);
        System.out.printf("%-30s (a) Enter 1 to add new Passenger....\n", "");
        System.out.printf("%-30s (b) Enter 2 to search a Passenger....\n", "");
        System.out.printf("%-30s (c) Enter 3 to update the Data of the Passenger....\n", "");
        System.out.printf("%-30s (d) Enter 4 to delete a Passenger....\n", "");
        System.out.printf("%-30s (e) Enter 5 to Display all Passengers....\n", "");
        System.out.printf("%-30s (f) Enter 6 to Display all flights registered by a Passenger...\n", "");
        System.out.printf("%-30s (g) Enter 7 to Display all registered Passengers in a Flight....\n", "");
        System.out.printf("%-30s (h) Enter 8 to Delete a Flight....\n", "");
        System.out.printf("%-30s (i) Enter 0 to Go back to the Main Menu/Logout....\n", "");
        System.out.print("Enter the desired Choice :   ");
    }

    private static void displayPassengerMenu(String username) {
        System.out.printf("\n\n%-60s+++++++++ 3rd Layer Menu +++++++++%50sLogged in as \"%s\"\n", "", "", username);
        System.out.printf("%-40s (a) Enter 1 to Book a flight....\n", "");
        System.out.printf("%-40s (b) Enter 2 to update your Data....\n", "");
        System.out.printf("%-40s (c) Enter 3 to delete your account....\n", "");
        System.out.printf("%-40s (d) Enter 4 to Display Flight Schedule....\n", "");
        System.out.printf("%-40s (e) Enter 5 to Cancel a Flight....\n", "");
        System.out.printf("%-40s (f) Enter 6 to Display all flights registered by \"%s\"....\n", "", username);
        System.out.printf("%-40s (g) Enter 0 to Go back to the Main Menu/Logout....\n", "");
        System.out.print("Enter the desired Choice :   ");
    }

    private static void displayMainMenu() {
        System.out.println("\n\n\t\t(a) Press 0 to Exit.");
        System.out.println("\t\t(b) Press 1 to Login as admin.");
        System.out.println("\t\t(c) Press 2 to Register as admin.");
        System.out.println("\t\t(d) Press 3 to Login as Passenger.");
        System.out.println("\t\t(e) Press 4 to Register as Passenger.");
        System.out.println("\t\t(f) Press 5 to Display the User Manual.");
        System.out.print("\t\tEnter the desired option:    ");
    }

    private static void manualInstructions() {
        Scanner read = new Scanner(System.in);
        System.out.printf("%n%n%50s %s Welcome to BAV Airlines User Manual %s", "", "+++++++++++++++++",
                "+++++++++++++++++");
        System.out.println("\n\n\t\t(a) Press 1 to display Admin Manual.");
        System.out.println("\t\t(b) Press 2 to display User Manual.");
        System.out.print("\nEnter the desired option :    ");
        int choice = getValidManualChoice(read);
        
        if (choice == 1) {
            displayAdminManual();
        } else {
            displayUserManual();
        }
    }

    private static int getValidManualChoice(Scanner read) {
        int choice = read.nextInt();
        while (choice < 1 || choice > MAX_MANUAL_OPTION) {
            System.out.print("ERROR!!! Invalid entry...Please enter a value either 1 or 2....Enter again....");
            choice = read.nextInt();
        }
        return choice;
    }

    private static void displayAdminManual() {
        System.out.println("\n\n(1) Admin have the access to all users data...Admin can delete, update, add and can perform search for any customer...\n");
        System.out.println("(2) In order to access the admin module, you've to get yourself register by pressing 2, when the main menu gets displayed...\n");
        System.out.println("(3) Provide the required details i.e., name, email, id...Once you've registered yourself, press 1 to login as an admin... \n");
        System.out.println("(4) Once you've logged in, 2nd layer menu will be displayed on the screen...From here on, you can select from variety of options...\n");
        System.out.println("(5) Pressing \"1\" will add a new Passenger, provide the program with required details to add the passenger...\n");
        System.out.println("(6) Pressing \"2\" will search for any passenger, given the admin(you) provides the ID from the table printing above....  \n");
        System.out.println("(7) Pressing \"3\" will let you update any passengers data given the user ID provided to program...\n");
        System.out.println("(8) Pressing \"4\" will let you delete any passenger given its ID provided...\n");
        System.out.println("(9) Pressing \"5\" will let you display all registered passenger...\n");
        System.out.println("(10) Pressing \"6\" will let you display all registered passengers...After selecting, program will ask, if you want to display passengers for all flights(Y/y) or a specific flight(N/n)\n");
        System.out.println("(11) Pressing \"7\" will let you delete any flight given its flight number provided...\n");
        System.out.println("(11) Pressing \"0\" will make you logged out of the program...You can login again any time you want during the program execution....\n");
    }

    private static void displayUserManual() {
        System.out.println("\n\n(1) Local user has the access to its data only...He/She won't be able to change/update other users data...\n");
        System.out.println("(2) In order to access local users benefits, you've to get yourself register by pressing 4, when the main menu gets displayed...\n");
        System.out.println("(3) Provide the details asked by the program to add you to the users list...Once you've registered yourself, press \"3\" to login as a passenger...\n");
        System.out.println("(4) Once you've logged in, 3rd layer menu will be displayed...From here on, you embarked on the journey to fly with us...\n");
        System.out.println("(5) Pressing \"1\" will display available/scheduled list of flights...To get yourself booked for a flight, enter the flight number and number of tickets for the flight...Max num of tickets at a time is 10 ...\n");
        System.out.println("(7) Pressing \"2\" will let you update your own data...You won't be able to update other's data... \n");
        System.out.println("(8) Pressing \"3\" will delete your account... \n");
        System.out.println("(9) Pressing \"4\" will display randomly designed flight schedule for this runtime...\n");
        System.out.println("(10) Pressing \"5\" will let you cancel any flight registered by you...\n");
        System.out.println("(11) Pressing \"6\" will display all flights registered by you...\n");
        System.out.println("(12) Pressing \"0\" will make you logout of the program...You can login back at anytime with your credentials...for this particular run-time... \n");
    }

    public static List<Customer> getCustomersCollection() {
        return customersCollection;
    }
}