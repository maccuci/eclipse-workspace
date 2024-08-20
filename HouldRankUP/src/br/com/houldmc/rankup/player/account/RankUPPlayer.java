package br.com.houldmc.rankup.player.account;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;
import br.com.houldmc.rankup.player.clan.utilitaries.Clan;
import br.com.houldmc.rankup.player.group.list.GroupList;
import br.com.houldmc.rankup.player.punish.PunishRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankUPPlayer {
	
	public static final HashMap<Integer, PunishRecord> punishRecords = new HashMap<>();
	
	private Player player;
	private String nickname;
	private UUID uniqueId;
	
	private Clan clan;
	private RankList rank;
	private GroupList group;
	
	private boolean groupTemporary;
	private long groupExpire;
	
	private double money;
	private int cash, souls;
	
	public RankUPPlayer(Player player) {
		this.player = player;
		this.uniqueId = player.getUniqueId();
		this.nickname = player.getName();
	}
	
	public RankUPPlayer(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public boolean canJoin() {
		return RankUPPlayerCrud.getLastActiveBan() == null;
	}
	
	public void addMoney(double money) {
		setMoney(this.money + money);
	}
	
	public void addCash(int cash) {
		setCash(this.cash + cash);
	}
	
	public void addSouls(int souls) {
		setSouls(this.souls + souls);
	}
	
	public void update() {
		new RankUPPlayerCrud().update(this);
	}
}
