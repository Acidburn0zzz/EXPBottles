package io.github.anileo.expbottles;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.anileo.expbottles.Commands.XPCheck;
import io.github.anileo.expbottles.Commands.XPFill;
import io.github.anileo.expbottles.Commands.XPInfo;

public class EXPBottles extends JavaPlugin {

	@Override
	public void onEnable() {
		API nAPI = new API();
		
		WriteConfig WC = new WriteConfig();
		ReadConfig RC = new ReadConfig();
		WriteLocale WL = new WriteLocale();
		ReadLocale RL = new ReadLocale();

		WC.createConfig();
		RC.readConfig();
		WL.createLocale();
		RL.readLocale();
		
		// Print config to the log for debugging purposes
		if (API.cfg_debugMode) {
			RC.debugConfig();
			nAPI.logger.log(Level.INFO, Bukkit.getServer().getVersion());
		}
		
		nAPI.setupEconomy();
		nAPI.registerListeners();
		// Legacy support for 1.7, 1.6, 1.5, 1.4, 1.3, 1.2.5 and 1.2.4
		nAPI.legacyCheck();
		nAPI.startMetrics();

		getCommand("XPCheck").setExecutor(new XPCheck());
		getCommand("XPFill").setExecutor(new XPFill());
		getCommand("XPInfo").setExecutor(new XPInfo());
		getCommand("EXPBottles").setExecutor(new io.github.anileo.expbottles.Commands.EXPBottles());
	}

	@Override
	public void onDisable() {
		API nAPI = new API();
		nAPI.unregisterListeners();
	}
	
}