package com.fuzion.core.master.data.language;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.management.AccountManager;

import lombok.Getter;

public class LanguageManager {
	
	public static final HashMap<String, List<LangMessage>> messages = new HashMap<>();
	
	public String getMessage(String key, Language lang) {
		if (!messages.containsKey(key))
			return key;
		return messages.get(key).stream().filter(langm -> langm.getLang() == lang).findFirst().orElse(null).getText();
	}
	

	public String getMessage(String key, Player player) {
		int val = AccountManager.getAccount(player).getLanguage().getId();
		Language lang = Language.getLanguage(val);
		System.out.println("LANG= " + lang);
		System.out.println("VAL= " + val);

		return getMessage(key, lang);
	}
	
	public void addTextToLang(String key, String text, Language language) {
		messages.put(key, new ArrayList<>());
		messages.get(key).add(new LangMessage(language, text));
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("INSERT INTO `languages` (`id`, `language`, `key`, `value`) VALUES ('" + language.getId() +"','" + language.name() + "', '" + key + "', '" + text + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Getter
	public static final class LangMessage {
		private final Language lang;
		private final String text;

		public LangMessage(Language lang, String text) {
			this.lang = lang;
			this.text = text;
		}
	}

}
