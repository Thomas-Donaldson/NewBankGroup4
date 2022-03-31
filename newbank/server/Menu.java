package newbank.server;
import java.util.*;

public class Menu {

    private final Map<Integer, String> availableMenuOptions;

    public Menu(){
        availableMenuOptions = new HashMap<>() {};
        availableMenuOptions.put(1,"display your accounts.");
        availableMenuOptions.put(2,"create a new account.");
        availableMenuOptions.put(3,"move money between your accounts");
        availableMenuOptions.put(4,"transfer money to someone else");
        availableMenuOptions.put(5,"edit your details.");
        availableMenuOptions.put(6,"request a loan.");
        availableMenuOptions.put(7,"close an account");
        availableMenuOptions.put(8,"log out");
    }
    public String display(){
        StringBuilder menu = new StringBuilder();
        availableMenuOptions.forEach( (option, description) -> menu.append("Press " + option + " to " + description + "\n"));
        return menu.toString();
    }

    public Boolean isOptionAvailable(int option){
        return availableMenuOptions.containsKey(option);
    }
}
