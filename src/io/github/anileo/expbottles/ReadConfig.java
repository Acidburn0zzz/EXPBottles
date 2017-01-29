package io.github.anileo.expbottles;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;

import nu.studer.java.util.OrderedProperties;

public class ReadConfig {

	API nAPI = new API(); // non-static
	
	File file = new File(nAPI.plugin.getDataFolder(), "config.cfg");

	public void readConfig() {
		OrderedProperties p = new OrderedProperties();

		try {
			p.load(new FileInputStream(file));

			API.cfg_debugMode = nAPI.stringToBoolean(p.getProperty("DebugMode", "false"));
			
			API.cfg_usePermissions = nAPI.stringToBoolean(p.getProperty("Use_Permissions", "true"));
			API.cfg_useSigns = nAPI.stringToBoolean(p.getProperty("Use_Signs", "true"));
			
			API.cfg_itemEmptyBottle = p.getProperty("Item_EmptyBottle", "GLASS_BOTTLE");
			API.cfg_itemFullBottle = p.getProperty("Item_FullBottle", "EXP_BOTTLE");
			
			API.cfg_logCommands = nAPI.stringToBoolean(p.getProperty("Log_Commands", "true"));
			API.cfg_logUsage = nAPI.stringToBoolean(p.getProperty("Log_XPBottleUse", "false"));
			
			API.cfg_fillXP = nAPI.stringToInt(p.getProperty("Fill_AmountXP", "10"));
			API.cfg_fillMoney = nAPI.stringToDouble(p.getProperty("Fill_AmountMoney", "0.0"));
			API.cfg_fillBottles = nAPI.stringToInt(p.getProperty("Fill_AmountBottles", "1"));
			
			API.cfg_bottlesRandomXP = nAPI.stringToBoolean(p.getProperty("Bottles_RandomXP", "false"));
			API.cfg_bottlesReceiveXP = nAPI.stringToInt(p.getProperty("Bottles_ReceiveXP", "10"));
			API.cfg_bottlesGiveBack = nAPI.stringToBoolean(p.getProperty("Bottles_GiveBack", "false"));		
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE, nAPI.nP + "An exception occoured while reading the config.cfg file! "
					+ "Report at " + API.url + ". Additional information: " + e.toString());
		}
	}
	
	public void debugConfig() {
		nAPI.logger.log(Level.INFO, "Use_Permissions: " + API.cfg_usePermissions);
		nAPI.logger.log(Level.INFO, "Use_Signs: " + API.cfg_usePermissions);
		nAPI.logger.log(Level.INFO, "Log_Commands: " + API.cfg_logCommands);
		nAPI.logger.log(Level.INFO, "Log_XPBottleUse: " + API.cfg_logUsage);
		nAPI.logger.log(Level.INFO, "Item_EmptyBottle: " + API.cfg_itemEmptyBottle);
		nAPI.logger.log(Level.INFO, "Item_FullBottle: ", API.cfg_itemFullBottle);
		nAPI.logger.log(Level.INFO, "Fill_AmountXP: " + API.cfg_fillXP);
		nAPI.logger.log(Level.INFO, "Fill_AmountMoney: " + API.cfg_fillMoney);
		nAPI.logger.log(Level.INFO, "Fill_AmountBottles: " + API.cfg_fillBottles);
		nAPI.logger.log(Level.INFO, "Bottles_RandomXP: " + API.cfg_bottlesRandomXP);
		nAPI.logger.log(Level.INFO, "Bottles_RecieveXP: " + API.cfg_bottlesReceiveXP);
		nAPI.logger.log(Level.INFO, "Bottles_GiveBack: " + API.cfg_bottlesGiveBack);
	}

}