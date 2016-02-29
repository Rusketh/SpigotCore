package rusketh.com.github.spigot.parts;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import rusketh.com.github.spigot.Rusketh;
import rusketh.com.github.spigot.commands.PlayerOnlyCommand;
import rusketh.com.github.spigot.util.Shop;

public class Shops extends Part {
	private HashMap<String, Shop> shops;
	
	public Shops(Rusketh plugin) {
		super(plugin);
		shops = new HashMap<String, Shop>();
		
		Rusketh.addCommand("newshop", new PlayerOnlyCommand(2, 2, "newshop <rows> <name>") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				Shop newShop = new Shop(rusketh.getConfig(), "Shops." + args[1]);
				newShop.setSize(Integer.parseInt(args[0]) * 9);
				newShop.setOwner(((Player) sender).getName());
				newShop.setName(args[1]);
				
				for (int i = 0; i < 10; i++) newShop.addItem(new ItemStack(Material.APPLE, 5));
					
				newShop.Open((Player) sender);
				shops.put(args[1], newShop);
				return false;
			}
		});
		
		Rusketh.addCommand("shop", new PlayerOnlyCommand(1, 1, "newshop <name>") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				Shop shop = shops.get(args[0]);
				if (shop != null) shop.Open((Player) sender);
				return true;
			}
		});
	}
}
