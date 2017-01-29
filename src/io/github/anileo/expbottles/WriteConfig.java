package io.github.anileo.expbottles;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;

import nu.studer.java.util.OrderedProperties;

public class WriteConfig {

	API nAPI = new API();
	
	File file = new File(nAPI.plugin.getDataFolder(), "config.cfg");

	public void createConfig() {

		try {
			
			if (!nAPI.plugin.getDataFolder().exists())
				nAPI.plugin.getDataFolder().mkdirs();
			
			if (!file.exists()) {
				file.createNewFile();
				writeConfig();
			}
			
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE, nAPI.nP + "An error occoured! Report at " + API.url
					+ ". Additional information: " + e.toString());
		}
		
		
	}

	public void writeConfig() {
		OrderedProperties p = new OrderedProperties();
		
		try {
			p.setProperty("Use_Permissions", "true");
			p.setProperty("Use_Signs", "true");

			p.setProperty("Log_Commands", "true");
			p.setProperty("Log_XPBottleUse", "false");
			
			p.setProperty("Item_EmptyBottle", "GLASS_BOTTLE");
			p.setProperty("Item_FullBottle", "EXP_BOTTLE");

			p.setProperty("Fill_AmountXP", "10");
			p.setProperty("Fill_AmountMoney", "0.0");
			p.setProperty("Fill_AmountBottles", "1");

			p.setProperty("Bottles_RandomXP", "false");
			p.setProperty("Bottles_ReceiveXP", "10");
			p.setProperty("Bottles_GiveBack", "false");

			p.store(new FileOutputStream(file), "EXPBottles' configuration file");
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE,
					nAPI.nP + "An error occoured! Report at " + API.url + ". Additional information: " + e.toString());
		}
	}

}