package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Locale;

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
	public synchronized String processRequest(CustomerID customer, int request, BufferedReader in, PrintWriter out) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case 1 : return showMyAccounts(customer);
			case 2 : return createAccount(customer, in, out);
			case 3 : return editDetails(customer, in, out);
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

	private String createAccount(CustomerID customer, BufferedReader in, PrintWriter out) {
		Customer loggedInCustomer = bank.customers.get(customer.getKey());
		String userInput;
		int initialBalance = 0;
		String typeAccount;
		boolean newAccount = true;

		// Account details
		out.println("Please specify the type of account to be created:");
		out.println("Press 1 for Main Account");
		out.println("Press 2 for Savings Account");
		out.println("Press 3 for Checking Account");
		userInput = getUserInput(in, out);

		int option = 0;
		try {
			option = Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		switch(option) {
			case 1 : typeAccount = "Main";
			break;
			case 2 : typeAccount = "Savings";
			break;
			case 3 : typeAccount = "Checking";
			break;
			default: typeAccount = "Invalid";
		}

		if (typeAccount == "Invalid") {
			return "Invalid type of account, your account has not been created";
		}
		else {
			out.println("Would you like to make an opening Deposit? If yes, type a value. If no, press enter");
			userInput = getUserInput(in, out);
			if (userInput == "") {
				userInput = "0";
			}

			try {
				initialBalance = Integer.parseInt(userInput);
				if (initialBalance >= 0) {
					for (Account a : customers.get(customer.getKey()).getAccounts()) {
						if (Objects.equals(a.getAccountName(), typeAccount)) {

							newAccount = false;
							return "There is already a " + typeAccount + " account created, please select a different type of account";

						}
					}
					if (newAccount) {

						loggedInCustomer.addAccount(new Account(typeAccount, initialBalance));
						return "Thanks, your account has been created";

					} else {
						return "There is already a " + typeAccount + " account created, please select a different type of account";
					}
				} else {
					return "The opening deposit must be a greater than 0, your account has not been created";
				}
			}
			catch(NumberFormatException e){
				e.printStackTrace();
				return "Invalid opening deposit, your account has not been created";
			}

		}
	}

	private String editDetails(CustomerID customer, BufferedReader in, PrintWriter out) {
		Customer loggedInCustomer = bank.customers.get(customer.getKey());
		CustomerDetails details = loggedInCustomer.getCustomerDetails();
		String userInput;

		// EDIT FIRST NAME
		out.println("Do you want to edit your first name? If yes, type a new value. If no, press enter");
		userInput = getUserInput(in, out);
		if (userInput == "") {
			;
		}
		else {
			loggedInCustomer.getCustomerDetails().setFirstName(userInput);
		}

		// EDIT LAST NAME
		out.println("Do you want to edit your last name? If yes, type a new value. If no, press enter");
		userInput = getUserInput(in, out);
		if (userInput == "") {
			;
		}
		else {
			loggedInCustomer.getCustomerDetails().setLastName(userInput);
		}

		// EDIT DATE OF BIRTH
		out.println("Do you want to edit your date of birth? If yes, type a new value in the date format DD-MM-YYYY. If no, press enter");
		userInput = getUserInput(in, out);
		if (userInput == "") {
			;
		}
		else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
			formatter.setLenient(false);
			Date dateInput = null;
			try {
				dateInput = formatter.parse(userInput);
				loggedInCustomer.getCustomerDetails().setDateOfBirth(dateInput);
			} catch (ParseException e) {
				e.printStackTrace();
				out.println("Invalid date format");
			}
		}

		// EDIT PHONE NUMBER
		out.println("Do you want to edit your phone number? If yes, type a new value. If no, press enter");
		userInput = getUserInput(in, out);
		if (userInput == "") {
			;
		}
		else {
			loggedInCustomer.getCustomerDetails().setPhoneNumber(userInput);
		}

		// EDIT EMAIL
		out.println("Do you want to edit your email? If yes, type a new value. If no, press enter");
		userInput = getUserInput(in, out);
		if (userInput == "") {
			;
		}
		else {
			loggedInCustomer.getCustomerDetails().setEmail(userInput);
		}

		return "Thanks, your details have been updated";
	}
	private String deletionPrompt(){
		return "deletion-requested";
	}

	private String logOut(){ return "log-user-out";	}
	private String unavailableService(){ return "oops! The selected service is currently unavailable.";	}
	private String getUserInput(BufferedReader in, PrintWriter out){
		String input = "";
		try{
			input = in.readLine();
		}
		catch (IOException e){
			System.out.println(e.getMessage());
			out.println("Sorry, something went wrong\nPlease enter your input again");
			getUserInput(in, out);
		}
		return input;
	}


}
