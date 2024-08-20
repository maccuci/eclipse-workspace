package me.tony.commons.core.data.backend;

public interface Database {
	
	boolean openConnection();
	void recallConnection();
}
