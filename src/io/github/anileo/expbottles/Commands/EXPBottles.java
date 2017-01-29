package io.github.anileo.expbottles.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.anileo.expbottles.API;

public class EXPBottles implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		API nAPI = new API();

		// Reloads the config
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload") && nAPI.hasPermission(null, sender, "reload")) {
				nAPI.plugin.onDisable();
				nAPI.plugin.onEnable();

				sender.sendMessage(nAPI.sP + "Reloaded config.cfg and locale.cfg");
				// I meant not to leave a return here to display both messages.
			}
		}

		sender.sendMessage(nAPI.sP + "Using " + API.version + " by " + API.author);
		return true;
	}
}