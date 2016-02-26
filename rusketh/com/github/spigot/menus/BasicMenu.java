package rusketh.com.github.spigot.menus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import rusketh.com.github.spigot.Rusketh;

public class BasicMenu implements Listener {
	private HashMap<Integer, Runnable> actions;
	private Inventory inventory;
	private Player player;
	
	public BasicMenu(Player ply, int size, String name) {
		actions = new HashMap<Integer, Runnable>();
		inventory = Bukkit.createInventory(null, size, name);
		player = ply;
		
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public Player getPlayer() { return player; }
	public Inventory getInventory() { return inventory; }
	
	public ItemStack AddOption(int slot, Material mat, String name, Runnable action) {
		ItemStack items = new ItemStack(mat);
		
		setStackName(items, name);
		
		inventory.setItem(slot, items);
		
		actions.put(slot, action);
		
		return items;
	}
	
	public boolean setStackName(ItemStack items, String name) {
		ItemMeta meta = items.getItemMeta();
		
		if (meta != null) {
			meta.setDisplayName(name);
			items.setItemMeta(meta);
			return true;
		}
		
		return false;
	}
	
	public void SetTitle(String title) {
		//Thanks to gyurix
		EntityPlayer entPly = ((CraftPlayer) player).getHandle();
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(entPly.activeContainer.windowId, "minecraft:chest", new ChatMessage(title), inventory.getSize());
		entPly.playerConnection.sendPacket(packet);
		entPly.updateInventory(entPly.activeContainer);
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
	
	public void reopen() {
		close();
		open();
	}
	
	public boolean onClick(InventoryClickEvent event) { return true; }
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() != player) return;
		if (!event.getInventory().equals(inventory)) return;
		if (event.getRawSlot() != event.getSlot()) return;
		if (!onClick(event)) return;
		
		int slot = event.getSlot();
		if (actions.containsKey(slot)) {
			Runnable act = actions.get(slot);
			if (act != null) act.run();
		}
		
		event.setCancelled(true);
	}
}
