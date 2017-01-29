package io.github.anileo.expbottles.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.anileo.expbottles.API;

public class XPCheck implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		API nAPI = new API();
		Player p = nAPI.toPlayer(sender);

		if (args.length == 0) {

			if (p == null) {
				sender.sendMessage(nAPI.eP + API.msg_xpcheckUsage);
				return false;
			}

			if (!nAPI.hasPermission(p, null, "xpcheck.self"))
				return false;

			nAPI.runXPCheckSelf(p);
			return true;
		}

		if (args.length > 0) {

			if (!(sender instanceof ConsoleCommandSender) && !nAPI.hasPermission(p, null, "xpcheck.others")) {
				if (p == null) {
					return false;
				}
				if (nAPI.hasPermission(p, null, "xpcheck.self"))
					nAPI.runXPCheckSelf(p);
				return true;
			}

			// TODO: Get player from UUID
			// https://bukkit.org/threads/player-name-uuid-fetcher.250926/
			@SuppressWarnings("deprecation")
			Player t = sender.getServer().getPlayer(args[0]);

			// Legacy check for older versions
			if (t == null) { 
				sender.sendMessage(nAPI.eP + API.msg_nullTarget);
				return false;
			}
			
			if (!t.isOnline()) {
				sender.sendMessage(nAPI.eP + API.msg_nullTarget);
				return false;
			}

			nAPI.runXPCheckOthers(sender, t);
			return true;
		}
		return false;
	}
}