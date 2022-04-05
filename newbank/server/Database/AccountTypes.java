package newbank.server.Database;

import java.util.ArrayList;
import java.util.List;

public class AccountTypes extends ConnectionProvider<AccountTypes> {

    public int Id;
    public String Type;

    @Override
    public List<AccountTypes> getAll() {
        StringBuilder query = new StringBuilder();
        query.append("Select * from ");
        query.append("AccountTypes");

        createConnection();
        executeQuery(query.toString());

        List<AccountTypes> accountTypes = new ArrayList<AccountTypes>();

        try {
            while (resultSet.next()){
                AccountTypes accountType = new AccountTypes();
                accountType.Id = resultSet.getInt("Id");
                System.out.println(accountType.Id);
                accountType.Type = resultSet.getString("Type");
                accountTypes.add(accountType);
            }
        }catch (Exception e){
        }

        dispose();
        return accountTypes;
    }

    @Override
    public AccountTypes get(int id) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ");
        query.append("AccountTypes");
        query.append(" WHERE Id = ");
        query.append(id);

        createConnection();
        executeQuery(query.toString());

        AccountTypes accountType = new AccountTypes();

        try {
            while (resultSet.next()){
                accountType.Id = resultSet.getInt("Id");
                System.out.println(accountType.Id);
                accountType.Type = resultSet.getString("Type");
            }
        }catch (Exception e){
        }

        dispose();
        return accountType;
    }

    @Override
    public boolean create(AccountTypes data) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append("AccountTypes");
        query.append(" (TYPE) ");
        query.append("VALUES ");
        query.append("(\""+data.Type+"\")");

        createConnection();
        try {
            executeUpdate(query.toString());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean update(int id, AccountTypes data) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append("AccountTypes");
        query.append(" SET TYPE = ");
        query.append("\"" + data.Type + "\"");
        query.append(" WHERE Id = ");
        query.append(id);

        createConnection();
        try {
            executeUpdate(query.toString());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append("AccountTypes");
        query.append(" WHERE Id = ");
        query.append(id);

        createConnection();
        try {
            executeUpdate(query.toString());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
