package rusketh.com.github.spigot.parts;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
				Shop shop = new Shop(Integer.parseInt(args[0]), args[1], ((Player) sender).getName());
				shops.put(args[1], shop);
				shop.openOwnerMenu((Player) sender);
				sender.sendMessage("Shop Created!");
				return true;
			}
		});
	}
}
