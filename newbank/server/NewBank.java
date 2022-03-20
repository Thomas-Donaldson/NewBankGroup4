package newbank.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer(new CustomerDetails("Bhagy", "Bhagy",new Date(), "4475556", "bhagy@bath.ac.uk"));
		bhagy.setPassword("bananabread");
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.addAccount(new Account("Savings", 0.0));
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
			case 3 : return editDetails(customer);
			case 5 : return deletionPrompt();
			case 6 : return logOut();
			default: return unavailableService();
			}
		}
		return "FAIL";
	}

	public synchronized String processDeletion(CustomerID customer, String accountName) {
		for (Account a : customers.get(customer.getKey()).getAccounts()) {
			if (Objects.equals(a.getAccountName(), accountName) && a.getOpeningBalance() != 0.0) {
				return "Deletion failed; nonzero account balance.";

			} else if (Objects.equals(a.getAccountName(), accountName) && a.getOpeningBalance() == 0.0) {
				customers.get(customer.getKey()).getAccounts().remove(a);
				return "Deletion successful.";
			}
		}
		return "Deletion failed; account not found.";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}
	private String editDetails(CustomerID customer){
		Customer loggedInCustomer = bank.customers.get(customer.getKey());
		return loggedInCustomer.getAccountDetails();
	}
	private String deletionPrompt(){
		return "deletion-requested";
	}
	private String logOut(){ return "log-user-out";	}
	private String unavailableService(){ return "oops! The selected service is currently unavailable.";	}
}
