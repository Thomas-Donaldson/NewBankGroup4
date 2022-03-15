package newbank.server;

import java.util.Date;
import java.util.Objects;

public class CustomerDetails {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String eMail;

    public CustomerDetails(String firstName, String lastName, Date dateOfBirth, String phoneNumber, String eMail){
        this.firstName = Objects.equals(firstName, "") ?"User has not registered a first name":firstName;
        this.lastName = Objects.equals(lastName, "") ?"User has not registered a last name":lastName;
        this.dateOfBirth = dateOfBirth == null ? new Date():dateOfBirth;
        this.phoneNumber = Objects.equals(phoneNumber, "") ?"User has not registered a phone number":phoneNumber;
        this.eMail = Objects.equals(eMail, "") ?"User has not registered an e-mail":eMail;
    }

    public String toString(){
        StringBuilder customerDetails = new StringBuilder();
        customerDetails.append("First Name: " + firstName + "\n");
        customerDetails.append("------------------------------------------\n");
        customerDetails.append("Last Name: " + lastName + "\n");
        customerDetails.append("------------------------------------------\n");
        customerDetails.append("Date of Birth: " + dateOfBirth + "\n");
        customerDetails.append("------------------------------------------\n");
        customerDetails.append("Phone Number: " + phoneNumber + "\n");
        customerDetails.append("------------------------------------------\n");
        customerDetails.append("E-Mail: " + eMail + "\n");
        customerDetails.append("------------------------------------------\n");

        return customerDetails.toString();
    }
}
