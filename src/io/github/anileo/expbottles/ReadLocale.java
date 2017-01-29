package io.github.anileo.expbottles;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;

import nu.studer.java.util.OrderedProperties;

public class ReadLocale {

	API nAPI = new API(); // non-static
	
	File file = new File(nAPI.plugin.getDataFolder(), "locale.cfg");

	public void readLocale() {
		OrderedProperties pr = new OrderedProperties();
		
		try {
			pr.load(new FileInputStream(file));

			API.msg_onlyPlayers = pr.getProperty("msg_onlyPlayers", "This command can only be run by players");
			API.msg_noPermission = pr.getProperty("msg_noPermission", "You don't have permission to run this command");
			API.msg_nullTarget = pr.getProperty("msg_nullTarget", "That player doesn't exist or isn't online");
			API.msg_xpfillUsage = pr.getProperty("msg_xpfillUsage", "The correct use of this command is /xpfill <amount|all>");
			API.msg_xpcheckUsage = pr.getProperty("msg_xpcheckUsage", "The correct use of this command is /xpcheck <player>");
			API.msg_noBottles = pr.getProperty("msg_noBottles", "You don't have enough bottles");
			API.msg_notPositive = pr.getProperty("msg_notPositive", "The amount needs to be more than 0");
			API.msg_noEXP = pr.getProperty("msg_noEXP", "Not enough EXP Points! You only have {exp}");
			API.msg_noMoney = pr.getProperty("msg_noMoney", "You don't have enough money");
			API.msg_noSpace = pr.getProperty("msg_noSpace", "You don't have enough space");
			API.msg_refunded = pr.getProperty("msg_refunded", "{bottles} Bottles Refunded");
			API.msg_xpcheckSelf = pr.getProperty("msg_xpcheckSelf", "You have {exp} EXP points");
			API.msg_xpcheckOthers = pr.getProperty("msg_xpcheckOthers", "{player} has {exp} EXP Points");
			API.msg_xpFill = pr.getProperty("msg_xpFill", "You filled {amount} Bottles! {exp} EXP Points left");
			API.msg_xpInfo = pr.getProperty("msg_xpInfo", "Fill Cost = {amount} Glass Bottle + {exp} EXP Points + $ {money}");		
			
		} catch (Exception e) {
			nAPI.logger.log(Level.SEVERE, nAPI.nP + "An exception occoured while reading the locale.cfg file! "
					+ "Report at " + API.url + ". Additional information: " + e.toString());
		}
	}
	
}
