package me.ale.login;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.huskehhh.mysql.MySQL;

import me.ale.login.command.controller.CommandFactory;
import me.ale.login.database.SQLManager;
import me.ale.login.listener.Auth;
import me.ale.login.listener.Commons;
import me.ale.login.listener.Auth.LoginType;
import me.ale.login.storage.Storage;
import me.ale.login.util.Config;
import me.ale.login.util.Util;
import me.ale.login.version.Version;

public class Bukkit extends JavaPlugin {

	private static final Storage STORAGE;

	private static MySQL mySQL;
	private static Connection connection;
	private static SQLManager sqlManager;

	@Override
	public void onEnable() {
		Config.loadConfig();

		if (!this.getServer().getOnlineMode()) {
			Util.setOnlineMode(true);
		}

		Util.info("Carregando plugin.");

		if (Version.getPackageVersion() != null) {
			Util.info(Version.getPackageVersion().toString());
		} else {
			Util.info("Não foi.");
			this.getServer().getPluginManager().disablePlugin(this);
		}

		if (Version.is1_7()) {
			new me.ale.login.protocol.v1_7.LoginReceiver();
		} else {
			new me.ale.login.protocol.v1_8.LoginReceiver();
		}

		if (this.getConfig().getBoolean("USE_MYSQL")) {
			try {
				Util.info("Conectando ao mysql");

				Bukkit.mySQL = new MySQL(this.getConfig().getString("MYSQL.HOSTNAME"),
						this.getConfig().getString("MYSQL.PORT"), this.getConfig().getString("MYSQL.DATABASE"),
						this.getConfig().getString("MYSQL.USERNAME"), this.getConfig().getString("MYSQL.PASSWORD"));
				Bukkit.connection = Bukkit.getMySQL().openConnection();
				Bukkit.sqlManager = new SQLManager();
			} catch (ClassNotFoundException | SQLException e) {
				Util.info("Erro ao conectar no mysql.");
				e.printStackTrace();
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}

		this.registerEvents();

		if (!this.getConfig().getBoolean("ONLY_USE_PREMIUM_CHECKER")) {
			CommandFactory.loadCommands();
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player player : Util.getOnlinePlayers()) {
						if (Bukkit.getStorage().needLogin(player.getName())) {
							if (Auth.getLoginMap().get(player) == LoginType.LOGIN) {
								player.sendMessage("§cLogue usando: /login <password>");
							} else {
								player.sendMessage("§cRegistre-se usando: /register <password>");
							}
						}
					}
				}
			}.runTaskTimer(this, 0L, 20 * this.getConfig().getInt("TIME_TO_REPEAT_LOGIN_MESSAGE"));
		} else {
//			if (this.getConfig().getBoolean("PLUGIN_AUTO_LOGIN.AUTHME")) {
//				if (this.getServer().getPluginManager().getPlugin("AuthMe") != null) {
//					Util.info("AuthMe");
//					return;
//				}
//			}
		}
	}

	@Override
	public void onDisable() {
		if (this.getServer().getOnlineMode() == false) {
			return;
		}

		Util.info("Disabling...");

		if (Bukkit.useMySQL()) {
			try {
				Bukkit.getConnection().close();
				Bukkit.getMySQL().closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		HandlerList.unregisterAll();
	}

	private void registerEvents() {
		final PluginManager manager = this.getServer().getPluginManager();

		manager.registerEvents(new Commons(), this);

		if (!this.getConfig().getBoolean("ONLY_USE_PREMIUM_CHECKER")) {
			manager.registerEvents(new Auth(), this);
		}
	}

	public static Plugin getPlugin() {
		return JavaPlugin.getPlugin(Bukkit.class);
	}

	public static MySQL getMySQL() {
		return Bukkit.mySQL;
	}

	public static Connection getConnection() {
		return Bukkit.connection;
	}

	public static SQLManager getSQLManager() {
		return Bukkit.sqlManager;
	}

	public static boolean useMySQL() {
		return Bukkit.getPlugin().getConfig().getBoolean("USE_MYSQL");
	}

	public static Storage getStorage() {
		return Bukkit.STORAGE;
	}

	static {
		STORAGE = new Storage();
	}

}
