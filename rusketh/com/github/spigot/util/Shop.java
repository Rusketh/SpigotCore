package rusketh.com.github.spigot.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rusketh.com.github.spigot.Rusketh;

public class Shop implements Listener {
	private int size;
	private String name;
	private String owner;
	private HashMap<Integer, ItemStack> items;
	private HashMap<Integer, Double> prices;
	
	public Shop(int size, String name, String owner) {
		this.size = size;
		this.name = name;
		this.owner = owner;
		this.prices = new HashMap<Integer, Double>();
		this.items = new HashMap<Integer, ItemStack>();
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public double getPrice(int slot) { return prices.get(slot); }
	
	public void setPrice(int slot, double price) { prices.put(slot, price); }
	
	public HashMap<Integer, ItemStack> getItems() { return items; }
	
	public Shop(YamlConfiguration config, String node) {
		loadFromYML(config, node);
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public void loadFromYML(YamlConfiguration config, String node) {
		size = config.getInt(node + ".Size");
		name = config.getString(node + ".Name");
		owner = config.getString(node + ".Owner");
		this.prices = new HashMap<Integer, Double>();
		this.items = new HashMap<Integer, ItemStack>();
		
		for (int i = 0; i < size * 9; i++) {
			String slotNode = node + ".Slots." + i;
			if (!config.contains(slotNode)) continue;
			prices.put(i, config.getDouble(slotNode + ".Price"));
			items.put(i, config.getItemStack(slotNode + ".Item"));
		}
	}
	
	public void saveToYML(YamlConfiguration config, String node) {
		config.set(node + ".Size", size);
		config.set(node + ".Name", name);
		config.set(node + ".Owner", owner);
		
		for (int i = 0; i < size * 9; i++) {
			String slotNode = node + ".Slots." + i;
			if (!items.containsKey(i)) continue;
			config.getDouble(slotNode + ".Price", prices.get(i));
			config.set(slotNode + ".Item", items.get(i));
		}
	}
	
	public ShopOwnerMenu openOwnerMenu(Player ply) {
		int rows = size;
		if (rows > 3) rows = 3;

		ShopOwnerMenu menu = new ShopOwnerMenu(ply, rows * 9, name);
		//menu.setShop(this);
		menu.openPage(1);
		
		return menu;
	}
}
