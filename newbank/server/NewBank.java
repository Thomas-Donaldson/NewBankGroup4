package newbank.server;

import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.setPassword("bananabread");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.setPassword("groupfourbestgroup");
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.setPassword("password123");
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName) && customers.get(userName).testPassword(password)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, int request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case 1 : return showMyAccounts(customer);
			case 5: return logOut();
			default: return unavailableService();
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}
	private String logOut(){ return "log-user-out";	}
	private String unavailableService(){ return "oops! The selected service is currently unavailable.";	}

}
