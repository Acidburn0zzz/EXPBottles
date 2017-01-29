package io.github.anileo.expbottles;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;

import nu.studer.java.util.OrderedProperties;

public class WriteLocale {

	API nAPI = new API();
	
	File file = new File(nAPI.plugin.getDataFolder(), "locale.cfg");

	public void createLocale() {

		try {
			
			if (!nAPI.plugin.getDataFolder().exists())
				nAPI.plugin.getDataFolder().mkdirs();
			
			if (!file.exists()) {
				file.createNewFile();
				writeLocale();
			}
			
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE, nAPI.nP + "An error occoured! Report at " + API.url
					+ ". Additional information: " + e.toString());
		}
		
		
	}
	
	public void writeLocale() {
	
		OrderedProperties p = new OrderedProperties();
		
		try {
			p.setProperty("msg_onlyPlayers", "This command can only be run by players");
			p.setProperty("msg_noPermission", "You don't have permission to run this command");
			p.setProperty("msg_nullTarget", "That player doesn't exist or isn't online");
			p.setProperty("msg_xpfillUsage", "The correct use of this command is /xpfill <amount|all>");
			p.setProperty("msg_xpcheckUsage", "The correct use of this command is /xpcheck <player>");
			p.setProperty("msg_noBottles", "You don't have enough bottles");
			p.setProperty("msg_notPositive", "The amount needs to be more than 0");
			p.setProperty("msg_noEXP", "Not enough EXP Points! You only have {exp}");
			p.setProperty("msg_noMoney", "You don't have enough money");
			p.setProperty("msg_noSpace", "You don't have enough space");
			p.setProperty("msg_refunded", "{bottles} Bottles Refunded");
			p.setProperty("msg_xpcheckSelf", "You have {exp} EXP Points");
			p.setProperty("msg_xpcheckOthers", "{player} has {exp} EXP Points");
			p.setProperty("msg_xpFill", "You filled {amount} Bottles! {exp} EXP Points left");
			p.setProperty("msg_xpInfo", "Fill Cost = {amount} Glass Bottle + {exp} EXP Points + $ {money}");

			p.store(new FileOutputStream(file), "EXPBottles' locale file");
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE,
					nAPI.nP + "An error occoured! Report at " + API.url + ". Additional information: " + e.toString());
		}
		
	}

}
