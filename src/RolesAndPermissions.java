import java.util.*;

public class RolesAndPermissions extends User {
    //        ************************************************************ Behaviours/Methods ************************************************************
    public static final int AUTH_FAILED = -1;
    public static final int AUTH_ADMIN = 1;
    public static final int AUTH_PASSENGER = 2; 
    private static final List<Admin> adminUsers = new ArrayList<>();
    
    static {
        // Initialize with root admin
        adminUsers.add(new Admin("root", "root"));
    }

    public void addAdmin(String username, String password) {
        adminUsers.add(new Admin(username, password));
    }

    public int isPrivilegedUserOrNot(String username, String password) {
        for (int i = 0; i < adminUsers.size(); i++) {
            Admin admin = adminUsers.get(i);
            if (username.equals(admin.getUsername()) && 
                password.equals(admin.getPassword())) {
                return i;
            }
        }
        return -1;
    }
    public class AuthResult {
        private final int authType;
        private final String userId;
    
        public AuthResult(int authType, String userId) {
            this.authType = authType;
            this.userId = userId;
        }
    
        public int getAuthType() {
            return authType;
        }
    
        public String getUserId() {
            return userId;
        }
    }
    
    public AuthResult authenticateUser(String username, String password) {
            // Try admin authentication first
        int adminIndex = isPrivilegedUserOrNot(username, password);
        if (adminIndex >= 0) {
            return new AuthResult(AUTH_ADMIN, String.valueOf(adminIndex));
        }
    
            // Try passenger authentication
        String passengerResult = isPassengerRegistered(username, password);
        if (passengerResult.startsWith("1-")) {
            return new AuthResult(AUTH_PASSENGER, passengerResult.substring(2));
        }
    
            // Authentication failed
        return new AuthResult(AUTH_FAILED, null);
    }
    


    
   

    /**
     * Checks if the passenger with specified credentials is registered or not.
     * @param email of the specified passenger
     * @param password of the specified passenger
     * @return 1 with the userID if the passenger is registered, else 0
     */
    public String isPassengerRegistered(String email, String password) {
        String isFound = "0";
        for (Customer c : Customer.customerCollection) {
            if (email.equals(c.getEmail())) {
                if (password.equals(c.getPassword())) {
                    isFound = "1-" + c.getUserID();
                    break;
                }
            }
        }
        return isFound;
    }
}
