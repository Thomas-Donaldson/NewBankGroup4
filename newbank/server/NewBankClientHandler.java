package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private Menu menu;
	private Boolean customerIsLoggedIn;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		menu = new Menu();
		customerIsLoggedIn = false;
	}
	
	public void run() {
		loginSequence();
	}

	private void loginSequence() {
		// keep getting requests from the client and processing them
		try {
			// ask for username
			out.println("Enter Username");
			String userName = getUserInput();
			// ask for password
			out.println("Enter Password");
			String password = getUserInput();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them
			if(customer == null){
				out.println("Log In Failed");
				loginSequence();
			}
			out.println("Log In Successful.");
			customerIsLoggedIn = true;
			delay(1000);
			out.println("Welcome " + userName + "!\nHow can we help you today?");
			delay(1000);
			handleCustomersRequests(customer);

		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	private void handleCustomersRequests(CustomerID customer) {

		while(customerIsLoggedIn) {
			out.println(menu.display());
			int request = getUsersRequest();
			out.println(request);
			if(!menu.isOptionAvailable(request)){
				handleInvalidCustomerRequest(customer);
			}
			System.out.println("Request from " + customer.getKey());
			String response = bank.processRequest(customer, request, in, out);
			if(response.equals("log-user-out")){
				logOut();
			}
			out.println(response);
			out.println("Press enter to continue");
			getUserInput();
			out.println("Could we help you with something else today?");
			delay(1000);

		}
	}

	private int getUsersRequest()  {
		int request = 0;
		try {
			request = Integer.parseInt(getUserInput());
		} catch (NumberFormatException e) {
			out.println("Oops, you entered an invalid input.");
			delay(1000);
			out.println("Please enter the number that corresponds to your preferred option");
			delay(1000);
			out.println(menu.display());
			getUsersRequest();
		}
		return request;
	}

	private String getUserInput(){
		String input = "";
		try{
			input = in.readLine();
		}
		catch (IOException e){
			System.out.println(e.getMessage());
			out.println("Sorry, something went wrong\nPlease enter your input again");
			getUserInput();
		}
		return input;
	}
	private void handleInvalidCustomerRequest(CustomerID customer) {
		out.println("The selected option is currently unavailable or incorrect.");
		delay(1000);
		out.println("Please select a valid option from the Menu below.");
		delay(1000);

		handleCustomersRequests(customer);
	}

	private void logOut(){
		customerIsLoggedIn = false;
		out.println("You have been successfully logged out.");
		delay(1000);
		loginSequence();
	}

	private void delay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e){
			System.out.println(e.getMessage());
		}
	}
}
