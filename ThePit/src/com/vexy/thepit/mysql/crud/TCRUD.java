package com.vexy.thepit.mysql.crud;

public interface TCRUD<T, E> {
	
	void create(T object);

	T read(E object);

	void update(T object);

	void delete(E object);

}
