package rusketh.com.github.spigot.parts;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import rusketh.com.github.spigot.Rusketh;
import rusketh.com.github.spigot.commands.PlayerOnlyCommand;
import rusketh.com.github.spigot.menus.BasicMenu;
import rusketh.com.github.spigot.menus.NumberWang;
import rusketh.com.github.spigot.menus.PagedMenu;

public class MenuExample extends Part {
	public MenuExample(Rusketh plug) {
		super(plug);
		
		Rusketh.addCommand("menu1", new PlayerOnlyCommand(0,0,"Shows an example paged menu.") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				PagedMenu menu = new PagedMenu((Player) sender, 18, "Shop.");
				
				for (int i = 1; i < 9; i++) {
					int j = i;
					menu.AddOption(Material.CHEST, "Option " + j, new Runnable() {
						@Override
						public void run() {
							sender.sendMessage("You selected " + j);
							menu.close();
						}
					});
				}
				
				
				menu.AddNavOption(Material.BAKED_POTATO, "First Page", new Runnable() {
					@Override
					public void run() {
						menu.openPage(1);
					}
				});
				
				menu.AddNavOption(Material.HARD_CLAY, "Last Page", new Runnable() {
					@Override
					public void run() {
						menu.openPage(menu.getTotalPages());
					}
				});
				
				menu.AddNavOption(Material.IRON_BLOCK, "Close", new Runnable() {
					@Override
					public void run() {
						menu.close();
					}
				});
				
				menu.AddNavOption(Material.BONE, "Other Button", new Runnable() {
					@Override
					public void run() {
						menu.close();
					}
				});
				
				menu.openPage(1);
				return true;
			}
		});
		
		Rusketh.addCommand("menu2", new PlayerOnlyCommand(0,0,"Shows an example numberwang.") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				NumberWang menu = new NumberWang((Player) sender, "Buy %v% cakes?");
				
				menu.setMinMax(0, 64);
				
				menu.setAction(new Runnable() {
					@Override
					public void run() {
						Player ply = menu.getPlayer();
						ply.getInventory().addItem(new ItemStack(Material.CAKE, menu.getValue()));
						ply.sendMessage("Enjoy your " + menu.getValue() + " Cakes.");
						menu.close();
					}
				});
				
				menu.open();
				
				return true;
			}
;		});
		
		Rusketh.addCommand("menu3", new PlayerOnlyCommand(0,0,"Shows an example menu.") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				BasicMenu menu = new BasicMenu((Player) sender, 18, "Shop.");
				menu.open();
				return true;
			}
		});
	}
	
	
}
