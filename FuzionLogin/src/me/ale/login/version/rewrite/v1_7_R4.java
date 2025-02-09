package me.ale.login.version.rewrite;

import java.nio.charset.Charset;
import java.util.UUID;

import me.ale.login.util.Reflection;
import me.ale.login.util.Util;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.LoginListener;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PacketLoginOutSuccess;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

public class v1_7_R4 extends LoginListener {

	private static final MinecraftServer SERVER = MinecraftServer.getServer();

	public v1_7_R4(NetworkManager networkManager, String player) {
		super(v1_7_R4.SERVER, networkManager);
		if (this.networkManager == null) {
			Util.info("Conex�o refusada.");
			return;
		}
		Reflection.setField("o", this, this.networkManager, 0);
		Reflection.setField("i",
				Reflection.newInstance(GameProfile.class,
						UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(Charset.forName("UTF-8"))), player),
				this, 1);
	}

	@Override
	public void a() {
		this.c();
	}

	@Override
	public void c() {
		GameProfile validProfile = (GameProfile) Reflection.getField("i", this, 1);
		EntityPlayer attemptLogin = v1_7_R4.SERVER.getPlayerList().attemptLogin(this, validProfile, this.hostname);
		if (attemptLogin != null) {
			this.networkManager.handle(new PacketLoginOutSuccess(validProfile), new GenericFutureListener[0]);
			v1_7_R4.SERVER.getPlayerList().a(this.networkManager,
					v1_7_R4.SERVER.getPlayerList().processLogin(validProfile, attemptLogin));
		}
	}

}
