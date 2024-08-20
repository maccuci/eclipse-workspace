package me.feenks.core.master.data;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.Getter;
import me.feenks.core.master.data.mysql.Mysql;

public class DataController {
	
	@Getter private Mysql mysql = new Mysql();
	
	@Getter private final Executor loadAsyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Load Async Thread").build());
	@Getter private final Executor saveAsyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Save Async Thread").build());
	
	public boolean mysqlOpenConnection() {
		return mysql.open();
	}
}
