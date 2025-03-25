import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserManager {
    private List<Customer> customersCollection;

    public UserManager() {
        this.customersCollection = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customersCollection.add(customer);
    }

    public void deleteCustomer(String userId) {
        Iterator<Customer> iterator = customersCollection.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (userId.equals(customer.getUserID())) {
                iterator.remove();
                return;
            }
        }
    }

    public Customer findCustomerById(String userId) {
        for (Customer customer : customersCollection) {
            if (userId.equals(customer.getUserID())) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return customersCollection;
    }

    public boolean isCustomerExists(String email) {
        for (Customer customer : customersCollection) {
            if (email.equals(customer.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public int getCustomerCount() {
        return customersCollection.size();
    }
}