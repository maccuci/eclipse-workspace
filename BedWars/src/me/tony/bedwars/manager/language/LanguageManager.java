package me.tony.bedwars.manager.language;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.tony.bedwars.Main;

public class LanguageManager {
	
	public static final HashMap<String, List<LangMessage>> messages = new HashMap<>();
	
	public boolean loadTranslations() {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `global_translations`;");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Language lang = Language.getLanguage(resultSet.getInt(2));
				String key = resultSet.getString(3);
				String text = resultSet.getString(4);
				
				List<LangMessage> messagesList = new ArrayList<>();

				if (messages.containsKey(key)) {
					messagesList = messages.get(key);
				}

				messagesList.add(new LangMessage(lang, text));

				messages.put(key, messagesList);

				System.out.println(String.format("Readed the message '%s' from '%s' lang.", text, lang.name()));
			}
			resultSet.close();
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error to read all langs.");
		}
		return true;
	}
	
	public void addLanguage(int id, String key, String text) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement langInsert = connection.prepareStatement("INSERT INTO `global_translations` (`language`, `key`, `value`) VALUES (?, ?, ?);");
			
			langInsert.setInt(1, id);
			langInsert.setString(2, key);
			langInsert.setString(3, text);
			
			langInsert.execute();
			langInsert.close();
			System.out.println("The key " + key + "and text " + text + " has been added to " + Language.getLanguage(id) + " language.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Error adding key and text to language " + Language.getLanguage(id).toString());
		}
	}
	
	public String getMessage(String key, Language lang) {
		if (!messages.containsKey(key))
			return "N\\A";
		return messages.get(key).stream().filter(langm -> langm.getLanguage() == lang).findFirst().orElse(null).getText();
	}
	
	public String getMessage(String key, Player player) {
		int val = 1;
		Language language = Language.getLanguage(val);
		System.out.println("LANG= " + language);
		System.out.println("VAL = " + val);

		return getMessage(key, language);
	}
	
	public static HashMap<String, List<LangMessage>> getMessages() {
		return messages;
	}
	
	public static final class LangMessage {
		private final Language lang;
		private final String text;

		public LangMessage(Language lang, String text) {
			this.lang = lang;
			this.text = text;
		}

		public Language getLanguage() {
			return lang;
		}

		public String getText() {
			return text;
		}
	}
}
