package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
		bhagy.addAccount(new Account("Main", 1000.0, "Checking"));
		bhagy.addAccount(new Account("Savings", 0.0, "Savings"));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.setPassword("groupfourbestgroup");
		christina.addAccount(new Account("Savings", 1500.0, "Savings"));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.setPassword("password123");
		john.addAccount(new Account("Checking", 250.0, "Checking"));
		customers.put("John", john);
	}

	private HashMap<String, Customer> getCustomers() {
		return customers;
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

	public synchronized CustomerID checkUserNameExists(String userName){
		if((customers.containsKey(userName)) ){
			return new CustomerID(userName);
		} else{
			return null;
		}

	}
	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, int request, BufferedReader in, PrintWriter out) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case 1 : return showMyAccounts(customer);
			case 2 : return createAccount(customer, in, out);
			case 3 : return moveMoneyBetweenAccounts(customer, in, out);
			case 4: return payAnotherUser(customer, in, out);
			case 5 : return editDetails(customer, in, out);
			case 7 : return deletionPrompt();
			case 8 : return logOut();
			default: return unavailableService();
			}
		}
		return "FAIL";
	}

	public synchronized String processDeletion(CustomerID customer, String accountName) {
		for (Account a : customers.get(customer.getKey()).getAccounts()) {
			if (Objects.equals(a.getAccountName(), accountName) && a.getBalance() != 0.0) {
				return "Deletion failed; nonzero account balance.";

			} else if (Objects.equals(a.getAccountName(), accountName) && a.getBalance() == 0.0) {
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
				//check initial deposit
				if (initialBalance >= 0) {
					//check if the customer already have an account from the same type
					for (Account a : customers.get(customer.getKey()).getAccounts()) {
						if (Objects.equals(a.getAccountName(), typeAccount)) {

							newAccount = false;
							return "There is already a " + typeAccount + " account created, please select a different type of account";

						}
					}
					if (newAccount) {
						loggedInCustomer.addAccount(new Account(typeAccount, initialBalance, typeAccount));
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

	private String moveMoneyBetweenAccounts(CustomerID customer, BufferedReader in, PrintWriter out) {
		Customer loggedInCustomer = bank.customers.get(customer.getKey());

		// Get list of accounts
		ArrayList<Account> customerAccounts = loggedInCustomer.getAccounts();

		// Create arraylist of account names
		HashMap<String, Account> customerAccountMap = new HashMap<>();
		for (Account account: customerAccounts) {
			customerAccountMap.put(account.getAccountName(), account);
		}

		// Print out list of accounts
		out.println("Here are your accounts.");
		out.println(loggedInCustomer.accountsToString());

		// Ask which account to move from

		out.println("Which account do you want to move money out of?");
		String outAccountString = getUserInput(in, out);
		Account outAccount = customerAccountMap.get(outAccountString);
		if (customerAccountMap.containsKey(outAccountString)) {
			;
		}
		else {
			return "That's not a valid account name. Exiting process.";
		}

		// Ask which account to move from
		out.println("Which account do you want to move money into?");
		String inAccountString = getUserInput(in, out);
		Account inAccount = customerAccountMap.get(inAccountString);
		if (customerAccountMap.containsKey(inAccountString)) {
			;
		}
		else {
			return "That's not a valid account name. Exiting process.";
		}

		// How much would you like to move?
		out.println("How much money would you like to move?");
		String quantityToMoveString = getUserInput(in, out);
		double quantityToMove;

		try {
			quantityToMove = Double.parseDouble(quantityToMoveString);
		} catch (Exception e) {
			return "Invalid input value. Please enter a number.";
		}

		if (quantityToMove < 0) {
			return "Please enter a non-negative number";
		}
		else if (outAccount.getBalance() < quantityToMove) {
			return "Not enough money in that account. Exiting process.";
		}
		else {
			// MAKE TRANSFER BETWEEN ACCOUNTS
			outAccount.subtractFromBalance(quantityToMove);
			inAccount.addToBalance(quantityToMove);
			out.println("Successfully moved money. Here are your accounts.");
			out.println(loggedInCustomer.accountsToString());
		}


		return "Returning to menu";
	}

	private String payAnotherUser(CustomerID customer, BufferedReader in, PrintWriter out) {
		Customer loggedInCustomer = bank.customers.get(customer.getKey());

		// Get list of accounts
		ArrayList<Account> customerAccounts = loggedInCustomer.getAccounts();

		// Create arraylist of account names
		HashMap<String, Account> customerAccountMap = new HashMap<>();
		for (Account account: customerAccounts) {
			customerAccountMap.put(account.getAccountName(), account);
		}

		// Print out list of accounts
		out.println("Here are your accounts.");
		out.println(loggedInCustomer.accountsToString());

		// Ask which account to move from
		out.println("Which account do you want to move money out of?");
		String outAccountString = getUserInput(in, out);
		Account outAccount = customerAccountMap.get(outAccountString);
		if (customerAccountMap.containsKey(outAccountString)) {
			;
		}
		else {
			return "That's not a valid account name. Exiting process.";
		}

		//-----------------------------------------------------------------------------------------

		// GET LIST OF ALL USERS AS HASHMAP
		out.println("Enter the name of the user you want to pay.");
		String userToPayString = getUserInput(in, out);

		if (customers.containsKey(userToPayString)) {
			;
		}
		else {
			return "The user is not a member of the bank. Exiting now.";
		}

		Customer userToPay = customers.get(userToPayString);
		ArrayList<Account> userToPayAccounts = userToPay.getAccounts();

		// Create arraylist of account names
		HashMap<String, Account> userToPayAccountMap = new HashMap<>();
		for (Account account: userToPayAccounts) {
			userToPayAccountMap.put(account.getAccountName(), account);
		}

		// --------------------------------------------------------------------------------------------

		out.println("Enter the name of the account you want to pay into.");
		String userToPayAccountString = getUserInput(in, out);
		Account inAccount = userToPayAccountMap.get(userToPayAccountString);
		if (userToPayAccountMap.containsKey(userToPayAccountString)) {
			;
		}
		else {
			return "That user does not have an account under that name. Exiting now.";
		}

		// ---------------------------------------------------------------------------------------------

		// How much would you like to move?
		out.println("How much money would you like to move?");
		String quantityToMoveString = getUserInput(in, out);
		double quantityToMove;

		try {
			quantityToMove = Double.parseDouble(quantityToMoveString);
		} catch (Exception e) {
			return "Invalid input value. Please enter a number.";
		}

		if (quantityToMove < 0) {
			return "Please enter a non-negative number";
		}
		else if (outAccount.getBalance() < quantityToMove) {
			return "Not enough money in that account. Exiting process.";
		}
		else {
			outAccount.subtractFromBalance(quantityToMove);
			inAccount.addToBalance(quantityToMove);
			out.println("Payment successful");
		}



		return "Returning to menu";
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