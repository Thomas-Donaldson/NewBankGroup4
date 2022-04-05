package newbank.server;

public class Account {
	
	private String accountName;
	private double balance;
	private String accountType;

	public Account(String accountName, double openingBalance, String accountType) {
		this.accountName = accountName;
		this.balance = openingBalance;
		this.accountType = accountType;
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
