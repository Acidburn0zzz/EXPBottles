package io.github.anileo.expbottles.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.anileo.expbottles.API;

public class XPFill implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		API NAPI = new API();
		Player p = NAPI.toPlayer(sender);

		if (p == null) {
			sender.sendMessage(NAPI.eP + API.msg_onlyPlayers);
			return false;
		}

		if (!NAPI.hasPermission(p, null, "xpfill")) {
			return false;
		}

		if (args.length == 0) {
			p.sendMessage(NAPI.eP + API.msg_xpfillUsage);
			return false;
		}

		NAPI.runXPFill(p, args[0].toString());
		return true;
	}
}