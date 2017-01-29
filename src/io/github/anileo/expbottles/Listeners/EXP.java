package io.github.anileo.expbottles.Listeners;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import io.github.anileo.expbottles.API;

public class EXP implements Listener {

	@EventHandler
	public void onEXPBottleUse(ExpBottleEvent e) {

		API nAPI = new API();

		// Set custom exp from 'XPBottles_RecieveXP' for exp bottles to give
		if (!API.cfg_bottlesRandomXP)
			e.setExperience(API.cfg_bottlesReceiveXP);

		Location l = e.getEntity().getLocation();

		// IF config setting 'XPBottles_GiveBack' is true
		// Gives back a Glass Bottle after using Bottle'o'enchanting
		if (API.cfg_bottlesGiveBack)
			l.getWorld().dropItemNaturally(l, new ItemStack(Material.getMaterial(API.cfg_itemEmptyBottle), 1));

		// IF config setting 'Log_XPBottleUse' is true
		// Logs the coordinates and the amount of EXP with Level.FINEST
		if (API.cfg_logUsage) {
			if (e.getEntity().getShooter() instanceof Player) {
				Player p = (Player) e.getEntity().getShooter();
				nAPI.logger.log(Level.INFO,
						nAPI.nP + "An EXPBottle has been used on world " + l.getWorld().getName() + " at X:" + Math.round(l.getX()*100.0)/100.0
								+ " Y:" + Math.round(l.getY()*100.0)/100.0 + " Z:" + Math.round(l.getZ()*100.0)/100.0 + " by player '" + p.getName() + "'. It dropped "
								+ e.getExperience() + " EXP");
			} else {
				nAPI.logger.log(Level.INFO,
						nAPI.nP + "An EXPBottle has been used on world " + l.getWorld().getName() + " at X:" + Math.round(l.getX()*100.0)/100.0
								+ " Y:" + Math.round(l.getY()*100.0)/100.0 + " Z:" + Math.round(l.getZ()*100.0)/100.0 + " by a non-player entity. It dropped "
								+ e.getExperience() + " EXP");
			}
		}

	}

}