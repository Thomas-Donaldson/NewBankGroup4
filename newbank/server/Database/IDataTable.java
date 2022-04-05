package newbank.server.Database;

import java.util.List;

public interface IDataTable<T> {

    public String name = null;

    public List<T> getAll();

    public T get(int id);

    public boolean create(T data);

    public boolean update(int id, T data);

    public boolean delete(int id);
}
