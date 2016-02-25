package rusketh.com.github.spigot.menus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BasicMenu implements Listener {
	private HashMap<Integer, Runnable> actions;
	private Inventory inventory;
	private Player player;
	
	public BasicMenu(Player ply, int size, String name) {
		actions = new HashMap<Integer, Runnable>();
		inventory = Bukkit.createInventory(null, size, name);
		player = ply;
	}
	
	public Player getPlayer() { return player; }
	public Inventory getInventory() { return inventory; }
	
	public ItemStack AddOption(int slot, Material mat, String name, Runnable action) {
		ItemStack items = new ItemStack(mat);
		
		ItemMeta meta = items.getItemMeta();
    	
		if (meta != null) {
    		meta.setDisplayName(name);
    		items.setItemMeta(meta);
    	}
		
		inventory.setItem(slot, items);
		
		actions.put(slot, action);
		
		return items;
	}
	
	public void clear() {
		inventory.clear();
		actions.clear();
	}
	
	public void open() {
		player.openInventory(inventory);
	}
	
	public void close() {
		player.closeInventory();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() != player) return;
		if (!event.getInventory().equals(inventory)) return;
		if (event.getRawSlot() != event.getSlot()) return;
				
		int slot = event.getSlot();
		
		if (actions.containsKey(slot)) {
			System.out.print(event.getInventory().getName() + " Slot: " + slot);
			actions.get(slot).run();
		} else {
			System.out.print(event.getInventory().getName() + " Slot: -1");
		}
		
		event.setCancelled(true);
	}
}
