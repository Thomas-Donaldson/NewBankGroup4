package newbank.server;

public class Account {
	
	private String accountName;
	private double balance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.balance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + balance + "\n");
	}

	public double getBalance(){
		return balance;
	}
	public String getAccountName() {
		return accountName;
	}

	public void addToBalance(double amountToAdd) {
		balance = balance + amountToAdd;
	}

	public void subtractFromBalance(double amountToSubtract) {
		balance = balance - amountToSubtract;
	}

}
