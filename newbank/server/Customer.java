package newbank.server;

import java.util.ArrayList;
import java.util.Date;

public class Customer {
	
	private ArrayList<Account> accounts;
	private String password;
	private CustomerDetails customerDetails;
	
	public Customer() {
		accounts = new ArrayList<>();
		customerDetails = new CustomerDetails("","",new Date(), "","");
	}

	public Customer(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
		accounts = new ArrayList<>();
	}

	public String getAccountDetails(){
		return customerDetails.toString();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean testPassword(String password) {
		return this.password.equals(password);
	}

	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}






}
