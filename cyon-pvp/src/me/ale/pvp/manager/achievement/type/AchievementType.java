package me.ale.pvp.manager.achievement.type;

import lombok.Getter;

@Getter
public enum AchievementType {
	
	FIRST_JOIN("First_Join", "Entrar no servidor", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Entre pela primeira vez no servidor.", 500),
	FIRST_CHAT("First_Chat", "Mandar a primeiro mensagem no chat", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Mande a sua primeira mensagem no chat do servidor.", 100),
	FIRST_STREAK("First_Streak", "Conseguir o seu primeiro kill-streak", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Faça seu primeiro kill-streak.", 100),
	FIRST_KIT_EXTRAORDINARY("First_Kit_Extraordinary", "Desbloquear o seu primeiro kit extraordinário", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Consiga o algum kit da raridade §6Extraordinary §7para desbloquear.", 1500),
	FIRST_DEATH("First_Death", "Morrer pela primeiro vez", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Tenha a sua primeira morte no servidor.", 50),
	FINISH_CHALLENGE("First_Challenge", "Complete seu primeiro challenge", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Termine seu primeiro challenge.", 450),
	KILL_SENSEI("Kill_Sensei", "Matar um jogador na league Sensei", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Mate algum jogador na league §4Sensei§7 para desbloquear.", 3500),
	FIRST_LEAGUE("First_League", "Evolua para a primeiro league", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Evolua pelo menos uma league para desbloquear.", 600),
	TOP_10("Top_10", "Top 10 do servidor", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Chegue em algum lugar do §bTop 10§7 do servidor para desbloquear.", 800),
	LEAGUE_SENSEI("League_Sensei", "League Sensei", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Chegue na league §4Sensei§7 para desbloquear.", 6000),
	STREAK_5("Streak_5", "KillStreak 5", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Faça um killstreak de 5 para desbloquear.", 850),
	KILLER_TOP1("Killer_Top1", "Mate o Top 1", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Mate o Top 1 do servidor para desbloquear.", 2500),
	FIRST_KIT_BUYED("First_Kit_Buyed", "Compra de Kit", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Compre o seu primeiro kit no servidor para desbloquear.", 150),
	UNLOCK_ALL("Unlock_All", "Todas", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Desbloqueie todas as conquistas para desbloquear.", 9500),
	REACH_10("Reach_10", "10 Kills", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Alcance 10 kills para desbloquear.", 250),
	REACH_100("Reach_100", "100 Kills", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Alcance 100 kills para desbloquear.", 550),
	REACH_1000("Reach_1000", "1000 Kills", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Alcance 1000 kills para desbloquear.", 900),
	UNLOCK_FIRST_PACK("Unlock_First_Pack", "primeiro Pack", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Abra seu primeiro pack de kits para desbloquear.", 300),
	FIRST_KILL("First_Kill", "Mate seu primeiro oponente", "§eVocê concluiu a conquista §b%achievement-name%§e e ganhou §a%reward% §ede money.", "§7Mate o primeiro jogador no servidor para desbloquear.", 400);
	
	private String name, itemName, desc, message;
	private int reward;
	
	private AchievementType(String name, String itemName, String message, String desc, int reward) {
		this.name = name;
		this.itemName = itemName;
		this.message = message;
		this.desc = desc;
		this.reward = reward;
	}
}
