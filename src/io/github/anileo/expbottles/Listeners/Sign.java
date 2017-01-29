package io.github.anileo.expbottles.Listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.anileo.expbottles.API;

public class Sign implements Listener {

	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();

		if (b == null)
			return; // Wasn't a block

		if (!(b.getState() instanceof org.bukkit.block.Sign))
			return; // Wasn't a sign

		org.bukkit.block.Sign s = (org.bukkit.block.Sign) b.getState();

		if (!s.getLine(0).equalsIgnoreCase("[EXPBottles]"))
			return; // If first line of the sign doesn't declare it is for EXPBottles (ignores case)
		
		Player p = e.getPlayer();
		API nAPI = new API();
		
		if (s.getLine(1).equalsIgnoreCase("XPCheck")) {
			if (!nAPI.hasPermission(p, null, "xpcheck.self"))
				return; // Doesn't have permission to run XPCheck (self)

			nAPI.runXPCheckSelf(p); // Runs XPCheck (self)
			return; // TODO: Allow for multiple commands in the same sign
		}
		
		if (s.getLine(1).equalsIgnoreCase("XPInfo")) {
			if (!nAPI.hasPermission(p, null, "xpinfo"))
				return; // Doesn't have permission to run XPInfo
			
			nAPI.runXPInfo(null, p); // Runs XPInfo
			return;
		}
		
		if (s.getLine(1).equalsIgnoreCase("XPFill")) {
			if (!nAPI.hasPermission(p, null, "xpfill"))
				return; // Doesn't have permission to run XPFill
			
			nAPI.runXPFill(p, s.getLine(2)); // Runs XPFill
			// TODO: Custom not-numeric error message for signs
		}
		
	}
}
