package br.com.houldmc.rankup.player.account.crud;

public interface ICRUD<T, E> {
	
	void create(T object);

	T read(E object);

	void update(T object);

	void delete(E object);
	
}
