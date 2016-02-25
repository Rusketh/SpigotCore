package rusketh.com.github.spigot.menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PagedMenu implements Listener {
	private class MenuOption {
		public Material mat;
		public String name;
		public Runnable action;
		public ItemStack item;
		
		public MenuOption(Material mat, String name, Runnable action) {
			this.mat = mat;
			this.name = name;
			this.action = action;
			this.item = new ItemStack(mat);
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
	    		meta.setDisplayName(name);
	    		item.setItemMeta(meta);
	    	}
		}
	}
	
	private Player player;
	private String menuName;
	private int slotsPerPage;
	private ArrayList<MenuOption> options;
	private ArrayList<MenuOption> navigation;
	private BasicMenu openMenu;
	
	public PagedMenu(Player ply, int size, String name) {
		player = ply;
		menuName = name;
		slotsPerPage = size;
		options = new ArrayList<MenuOption>();
		navigation = new ArrayList<MenuOption>();
	}
	
	public ItemStack AddOption(Material mat, String name, Runnable action) {
		MenuOption option = new MenuOption(mat, name, action);
		options.add(option);
		return option.item;
	}
	
	public ItemStack AddNavOption(Material mat, String name, Runnable action) {
		MenuOption option = new MenuOption(mat, name, action);
		navigation.add(option);
		return option.item;
	}
	
	public int getTotalPages() {
		return (int) Math.ceil(options.size() / slotsPerPage);
	}
	
	public void close() {
		if (openMenu != null) openMenu.close();
	}
	
	public boolean openPage(int page) {
		if (openMenu != null) openMenu.close();
		
		int tPages = getTotalPages();
		if (page > tPages) return false;
		
		openMenu = new BasicMenu(player, slotsPerPage + 9, menuName + "(" + page + "/" + tPages + ")");
		
		int start = (page - 1) * slotsPerPage;
		
		for(int slot = 0; slot <= slotsPerPage - 1; slot++ ) {
			MenuOption opt = options.get(start + slot);
			if (opt == null) break;
			
			openMenu.AddOption(slot, opt.mat, opt.name, opt.action);
		}
		
		if (page > 1) {
			openMenu.AddOption(slotsPerPage, Material.REDSTONE, "Previous Page", new Runnable() {
				@Override
				public void run() {openPage(page - 1);}
			});
		}
		
		for(int i = 0; i <= 6; i++) {
			if (i > navigation.size() - 1) break;
			
			MenuOption opt = navigation.get(i);
			
			if (opt != null) openMenu.AddOption(slotsPerPage + 1 + i, opt.mat, opt.name, opt.action);
		}
			
		if (page < tPages) {
			openMenu.AddOption(slotsPerPage + 8, Material.REDSTONE, "Next Page", new Runnable() {
				@Override
				public void run() {openPage(page + 1);}
			});
		}
		
		openMenu.open();
		
		return true;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (openMenu != null) openMenu.onInventoryClick(event);
	}
}
