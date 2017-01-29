package io.github.anileo.expbottles;

import java.io.IOException;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.anileo.expbottles.Listeners.EXP;
import io.github.anileo.expbottles.Listeners.Sign;

public class API {

	// Notes to self:
	// - Logging levels: SEVERE WARNING INFO CONFIG FINE FINER FINEST
	// - Metrics7 is only compatible from 1.3.2 R3.0 and on (A snapshot from R2.1 is
	// compatible)
	// - Player level only goes up to 32767 on 1.2.4
	
	// TODO: Refactor the rest of the code
	
	/**
	 * The plugin's name
	 */
	public static final String name = "EXPBottles";
	
	/**
	 * The current version of the plugin in an extensive form
	 */
	public static final String version = "1.1.5 (2017.01.20) for Bukkit 1.2.4-1.11.2";
	
	/**
	 * The author's name
	 */
	public static final String author = "AnnieLeonheart";
	
	/**
	 * The plugin's main website
	 */
	public static final String url = "http://dev.bukkit.org/bukkit-plugins/expbottles";

	/**
	 * EXPBottles plugin
	 */
	public Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(name);
	
	/**
	 * EXPBottles logger
	 */
	public Logger logger = plugin.getLogger();

	/**
	 * Vault's economy
	 * Null when Vault isn't loaded or an Economy plugin isn't loaded
	 */
	public Economy eco;

	/**
	 * True if Bukkit's version is between (or equal to) 1.7.x and 1.3.1
	 * Used for XPToLevel calculations introduced in 1.3.1
	 * See: http://minecraft.gamepedia.com/Experience#Values_of_1.3.1_-_Before_1.8_.2814w02a.29
	 */
	private static boolean legacyBukkit; 
	
	/** 
	 * True if Bukkit's version is below (or equal to) 1.3.0
	 * Used for XPToLevel calculations introduced in Beta 1.8
	 * See: http://minecraft.gamepedia.com/Experience#Values_of_Beta_1.8_-_Before_1.3.1_.2812w23a.29
	 */
	private static boolean legacyBukkit2;
	
	/**
	 * True if Bukkit's version is below 1.7
	 * Used for money deposit/withdraw economy functions
	 * Supports Vault since version 1.2.13
	 */
	private static boolean legacyVault;
	
	/**
	 * True if Bukkit's version is 1.2.x or 1.3.x
	 * Loads Metrics R5 if true and Metrics R7 if false
	 */
	private static boolean legacyMetrics;

	// Config values
	public static Boolean	cfg_debugMode;
	public static Boolean 	cfg_usePermissions;
	public static Boolean 	cfg_useSigns;
	public static Boolean 	cfg_logCommands;
	public static Boolean 	cfg_logUsage;
	public static String	cfg_itemEmptyBottle;
	public static String	cfg_itemFullBottle;
	public static int 		cfg_bottlesReceiveXP;
	public static Boolean 	cfg_bottlesRandomXP;
	public static Boolean 	cfg_bottlesGiveBack;
	public static double	cfg_fillMoney;
	public static int		cfg_fillXP;
	public static int		cfg_fillBottles;

	// Message prefixes and colors
	public final ChatColor pC = ChatColor.BLUE;
	public final ChatColor sC = ChatColor.GREEN;
	public final ChatColor eC = ChatColor.RED;
	public final ChatColor yC = ChatColor.YELLOW;

	public final Object nP = "[" + name + "] ";
	public final Object eP = "" + pC + nP + eC;
	public final Object sP = "" + pC + nP + sC;

	// Locale strings
	public static String msg_noMoney;
	public static String msg_notPositive;
	public static String msg_noSpace;
	public static String msg_refunded;
	public static String msg_noBottles;
	public static String msg_xpcheckUsage;
	public static String msg_xpfillUsage;
	public static String msg_noPermission;
	public static String msg_xpcheckSelf;
	public static String msg_xpcheckOthers;
	public static String msg_nullTarget;
	public static String msg_noEXP;
	public static String msg_xpFill;
	public static String msg_xpInfo;
	public static String msg_onlyPlayers;
	
	public EXP l_exp = new EXP();
	public Sign l_sign = new Sign(); 

	// CommandSender to Player conversion.
	// Returns: Player OR null
	public Player toPlayer(CommandSender s) {
		if (s instanceof Player)
			return (Player) s;

		return null;
	}

	// If using player the CommandSender must be null
	public boolean hasPermission(Player p, CommandSender cs, String s) {
		if (!cfg_usePermissions)
			return true;

		String msg = eP + msg_noPermission;
		String perm = "expbottles." + s;

		if (p == null) {
			if (cs instanceof ConsoleCommandSender)
				return true;

			if (!cs.hasPermission(perm)) {
				cs.sendMessage(msg);
				return false;
			}
			return true;
		}

		if (!p.hasPermission(perm)) {
			p.sendMessage(msg);
			return false;
		}
		return true;
	}

	// Returns amount of EXP in that level
	public int XPOnLevel(int l) {
		if (legacyBukkit) {
			if (l > 29)
				return 62 + (l - 30) * 7;

			if (l > 15)
				return 17 + (l - 15) * 3;

			return 17;
		}
		if (legacyBukkit2) {
			return 7 + (l * 7 >> 1);
		}

		if (l <= 15)
			return 2 * l + 7;

		if ((l >= 16) && (l <= 30))
			return 5 * l - 38;

		return 9 * l - 158;
	}

	// Get player's total experience
	public int getTotalExperience(Player p) {
		int exp = Math.round(XPOnLevel(p.getLevel()) * p.getExp());
		int currentLevel = p.getLevel();
		while (currentLevel > 0) {
			currentLevel--;
			exp += XPOnLevel(currentLevel);
		}
		if (exp < 0) {
			logger.log(Level.WARNING, "Player '" + p.getName() + "' has negative exp. Reseted to 0. Previous value: " + exp);
			exp = 0;
			setTotalExperience(p, exp);
		}
		return exp;
	}

	// Set player's total experience
	public void setTotalExperience(Player p, int e) {
		if (e < 0) {
			logger.log(Level.WARNING, "Player '" + p.getName() + "' has negative exp. Reseted to 0. Previous value: " + e);
			e = 0;
		}

		// Cleans current values
		p.setExp(0.0F);
		p.setLevel(0);
		p.setTotalExperience(0);

		// While there is exp to give, give it level by level
		int a = e;
		while (a > 0) {
			int expToLevel = XPOnLevel(p.getLevel());
			a -= expToLevel;
			if (a >= 0) {
				p.giveExp(expToLevel);
			} else {
				a += expToLevel;
				p.giveExp(a);
				a = 0;
			}
		}
	}

	// Check for Vault / Economy plugin
	public void setupEconomy() {
		// If Vault is present on the server (name check)
		if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			logger.log(Level.INFO, nP + "Vault was NOT found! " + name + " will not use economy related functions.");
			eco = null;
			return;
		}

		// If Vault is present but Economy can't be loaded (no economy plugin on
		// the server)
		RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			logger.log(Level.INFO, nP + "Vault was found but no economy plugin was found. " + name
					+ " will not use economy related functions.");
			eco = null;
			return;
		}
		eco = rsp.getProvider();
	}

	// Starts Hidendra's MCStats Metrics
	public void startMetrics() {
		if (legacyMetrics) {
			try {
				new Metrics5(plugin).start();
			} catch (IOException e) {
				// Error while loading Metrics6
			}
		} else {
			try {
				new Metrics7(plugin).start();
			} catch (IOException e) {
				// Error while loading Metrics7
			}
		}
	}

	// Takes a player's money
	// Deprecated method needed for Vault 1.2.27 (MC 1.6), Vault 1.2.25 (MC 1.5)
	// and Vault 1.2.20 (MC 1.4) and Vault 1.2.13 (MC 1.2.4) support
	@SuppressWarnings("deprecation")
	public boolean takeMoney(Player p, int m) {
		if (eco == null)
			return true;

		EconomyResponse er;

		if (legacyVault) {
			er = eco.withdrawPlayer(p.getPlayer().getName(), cfg_fillMoney);
		} else {
			// This only exists in Vault for Bukkit 1.7 and superior.
			// CAN'T be reachable otherwise it will crash the plugin.
			er = eco.withdrawPlayer(p.getPlayer(), cfg_fillMoney);
		}

		if (er.transactionSuccess())
			return true;

		p.sendMessage(eP + msg_noMoney);
		return false;
	}

	public int stringToInt(String s) {
		int i;
		try {
			i = Integer.valueOf(s).intValue();
		} catch (NumberFormatException nfe) {
			return 0;
		}
		return i;
	}

	public double stringToDouble(String s) {
		Double d;
		try {
			d = Double.valueOf(s).doubleValue();
		} catch (NumberFormatException nfe) {
			return 0;
		}
		return d;
	}

	public Boolean stringToBoolean(String s) {
		if (s.equalsIgnoreCase("false"))
			return false;

		if (s.equalsIgnoreCase("true"))
			return true;

		return null;
	}

	// Deprecated method needed for Vault 1.2.27 (MC 1.6), Vault 1.2.25 (MC 1.5)
	// and Vault 1.2.20 (MC 1.4) support
	@SuppressWarnings("deprecation")
	public int checkLeftovers(HashMap<Integer, ItemStack> h, Player p, int a) {
		if (h.containsKey(Integer.valueOf(0))) {
			int l = ((ItemStack) h.get(Integer.valueOf(0))).getAmount();
			a -= l;
			p.sendMessage(eP + msg_noSpace + " " + sC + msg_refunded.replace("{bottles}", String.valueOf(l)));
			if (eco != null) {
				if (legacyVault) {
					eco.depositPlayer(p.getPlayer().getName(), (cfg_fillMoney * l));
				} else {
					// This only exists in Vault for Bukkit 1.7 and superior.
					// CAN'T be reachable otherwise it will crash the plugin.
					eco.depositPlayer(p.getPlayer(), (cfg_fillMoney * l));
				}
			}
		}
		return a;
	}

	public int getAmountInt(String s, int x, Player p) {
		int a;
		if (s.equalsIgnoreCase("all")) {
			int b = 0;
			ListIterator<ItemStack> it = p.getInventory().iterator();
			while (it.hasNext()) {
				ItemStack current = it.next();
				if (current != null && current.getType().equals(Material.GLASS_BOTTLE)) {
					b += current.getAmount();
				}
			}
			a = b;
			if (a > x / cfg_fillXP)
				a = x / cfg_fillXP;

			if (a == 0) {
				// Used all but doesn't have any bottles on inventory
				p.sendMessage(eP + msg_noBottles);
				return -1;
			}
		} else {
			a = stringToInt(s);

			if (a < 1) {
				// Provided a value smaller than 1
				p.sendMessage(eP + msg_notPositive);
				return -1;
			}
		}

		if (a == 0) {
			// Ran the command incorrectly
			p.sendMessage(eP + msg_xpfillUsage);
			return -1;
		}

		// Doesn't have the EXP to fill the requested amount
		if (x < a * cfg_fillXP) {
			p.sendMessage(eP + msg_noEXP.replace("{exp}", String.valueOf(x)));
			return -1;
		}
		return a;
	}
	
	/**
	 * Checks if a player has a minimum amount of a certain item in the inventory
	 * @param player Player to check
	 * @param item	 Item's name 
	 * @param amount Minimum amount of the item
	 * @return true or false
	 */
	public boolean hasItem(Player player, String item, int amount) {
		if (player.getInventory().contains(Material.getMaterial(item), amount * cfg_fillBottles))
			return true;

		player.sendMessage(eP + msg_noBottles);
		return false;
	}

	/**
	 * Gives a certain amount of an item to a player's inventory
	 * @param player Player to give items to (the inventory)
	 * @param item Item's name
	 * @param amount Amount of that item to give
	 * @return Returns a HashMap<Integer, ItemStack>
	 */
	public HashMap<Integer, ItemStack> giveItem(Player player, String item, int amount) {
		HashMap<Integer, ItemStack> s = player.getInventory().addItem(new ItemStack(Material.getMaterial(item), amount));
		return s;
	}

	/**
	 * Takes a certain amount of an item from a player's inventory
	 * @param player Player to take items from (the inventory)
	 * @param item	 Item's name
	 * @param amount Amount of that item to take
	 */
	public void takeItem(Player player, String item, int amount) {
		player.getInventory().removeItem(new ItemStack(Material.getMaterial(item), amount * cfg_fillBottles));
	}

	public void runXPCheckSelf(Player p) {
		p.sendMessage(sP + msg_xpcheckSelf.replace("{exp}", String.valueOf(getTotalExperience(p))));
	}

	public void runXPCheckOthers(CommandSender s, Player t) {
		s.sendMessage(sP + msg_xpcheckOthers.replace("{player}", ChatColor.RESET + t.getDisplayName() + sP).replace("{exp}", String.valueOf(getTotalExperience(t))));
	}

	@SuppressWarnings("deprecation")
	public boolean runXPFill(Player p, String s) {
		int x = getTotalExperience(p);
		int a = getAmountInt(s, x, p);

		if (a == -1 || !hasItem(p, cfg_itemEmptyBottle, a) || !takeMoney(p, a)) {
			return false;
		}

		HashMap<Integer, ItemStack> h = giveItem(p, cfg_itemFullBottle, a);
		a = checkLeftovers(h, p, a);
		takeItem(p, cfg_itemEmptyBottle, a);
		setTotalExperience(p, x - a * cfg_fillXP);

		int y = getTotalExperience(p);
		p.sendMessage(sP + msg_xpFill.replace("{amount}", String.valueOf(a)).replace("{exp}", String.valueOf(y)));
		
		p.updateInventory();

		if (cfg_logCommands) {
			logger.log(Level.INFO, p + p.getName() + " filled " + a + " Bottles! He now has " + y + " EXP");
			return true;
		}
		
		return true;
	}

	public void runXPInfo(CommandSender cs, Player p) {
		String msg = (sP + msg_xpInfo.replace("{amount}", String.valueOf(cfg_fillBottles)).replace("{exp}", String.valueOf(cfg_fillXP)).replace("{money}", String.valueOf(cfg_fillMoney)));

		if (p == null) {
			cs.sendMessage(msg);
			return;
		}
		p.sendMessage(msg);
		return;
	}

	public void registerListeners() {
		if ( !cfg_bottlesRandomXP || cfg_bottlesGiveBack || cfg_logUsage)
			Bukkit.getServer().getPluginManager().registerEvents(l_exp, plugin);

		if (cfg_useSigns)
			Bukkit.getServer().getPluginManager().registerEvents(l_sign, plugin);
	}

	public void unregisterListeners() {
		if ( !cfg_bottlesRandomXP || cfg_bottlesGiveBack || cfg_logUsage || cfg_useSigns)
			HandlerList.unregisterAll(plugin);
	}

	public void legacyCheck() {
		String v = Bukkit.getVersion();

		if (v.contains("1.3.2") || v.contains("1.3.1") || v.contains("1.3.0") || v.contains("1.2.5") || v.contains("1.2.4")) {
			legacyMetrics = true;
		} else {
			legacyMetrics = false;
		}

		if (v.contains("1.7")) {
			legacyBukkit = true;
		} else if (v.contains("1.6") || v.contains("1.5") || v.contains("1.4") || v.contains("1.3.2")
				|| v.contains("1.3.1")) {
			legacyVault = true;
			legacyBukkit = true;
			return;
		} else if (v.contains("1.3.0") || v.contains("1.2.5") || v.contains("1.2.4")) {
			legacyVault = true;
			legacyBukkit2 = true;
			return;
		}
			// No legacy methods needed
			// This shouldn't be needed, booleans are false by default. Just
			// making sure.
			legacyBukkit = false;
			legacyBukkit2 = false;
			legacyVault = false;
	}

}
