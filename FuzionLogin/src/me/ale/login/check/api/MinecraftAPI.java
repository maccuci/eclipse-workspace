package me.ale.login.check.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import me.ale.login.check.Verify;
import me.ale.login.check.Check.CheckAPI;


public class MinecraftAPI implements Verify {

	private boolean result;

	@Override
	public boolean verify(String playerName) {
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(new URL(CheckAPI.MINECRAFT_API.getLink() + playerName).openStream()));
			this.setResult(true);
			return reader.readLine() != null;
		} catch (Exception e) {
			this.setResult(false);
			return false;
		}
	}

	private void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public boolean getResult() {
		return this.result;
	}

}
