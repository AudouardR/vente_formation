package fr.pbvf.flux;

import java.sql.Connection;
import java.util.List;

public interface Dao<T, I> {

	public abstract void create(T obj, Connection connection);
	
	public abstract T findById(I id, Connection connection);
	
	public abstract List<T> findAll(Connection connection);

	public abstract void update(I id, T obj, Connection connection);

	public abstract void delete(T obj, Connection connection);
	
}
