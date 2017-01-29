package io.github.anileo.expbottles.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.anileo.expbottles.API;

public class XPInfo implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		API nAPI = new API();
		
		if (!nAPI.hasPermission(null, sender, "xpinfo"))
			return false;
		
		nAPI.runXPInfo(sender, null);
		return true;
	}
	
}