package newbank.server;

import java.util.Date;
import java.util.Objects;

public class CustomerDetails {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;

    public CustomerDetails(String firstName, String lastName, Date dateOfBirth, String phoneNumber, String email){
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }


    // START ---- GETTERS & SETTERS

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.equals(firstName, "") ?"User has not registered a first name":firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.equals(lastName, "") ?"User has not registered a last name":lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth == null ? new Date():dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = Objects.equals(phoneNumber, "") ?"User has not registered a phone number":phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.equals(email, "") ?"User has not registered an e-mail":email;
    }

    // END ---- GETTERS & SETTERS

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
        customerDetails.append("E-Mail: " + email + "\n");
        customerDetails.append("------------------------------------------\n");

        return customerDetails.toString();
    }


}
