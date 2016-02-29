package rusketh.com.github.spigot.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import rusketh.com.github.spigot.Rusketh;

public class BasicMenu implements Listener {
	private HashMap<Integer, Runnable> actions;
	private HashMap<Integer, Boolean> lockedSlots;
	
	private Inventory inventory;
	private Player player;

	private boolean isOpen = false;
	private boolean isLocked = false;
	
	public BasicMenu(Player ply, int size, String name) {
		player = ply;
		actions = new HashMap<Integer, Runnable>();
		lockedSlots = new HashMap<Integer, Boolean>();
		inventory = Bukkit.createInventory(null, size, name);
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public boolean isOpen() { return isOpen; }
	public Player getPlayer() { return player; }
	public Inventory getInventory() { return inventory; }
	
	public ItemStack AddOption(int slot, Material mat, String name, Runnable action) {
		ItemStack items = new ItemStack(mat);
		
		setStackName(items, name);
		
		inventory.setItem(slot, items);
		
		actions.put(slot, action);
		
		return items;
	}
	
	public ItemStack AddOption(int slot, ItemStack items, String name, Runnable action) {
		setStackName(items, name);
		
		inventory.setItem(slot, items);
		
		actions.put(slot, action);
		
		return items;
	}
	
	public ItemStack GetPlayerHead(String name) {
		ItemStack items = new ItemStack(Material.SKULL_ITEM);
		SkullMeta meta = (SkullMeta) items.getItemMeta();
		meta.setOwner(name);
		items.setItemMeta(meta);
		return items;
	}
	
	public ItemStack AddHeadedOption(int slot, String ply, String name, Runnable action) {
		ItemStack items = GetPlayerHead(ply);
		
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
		lockedSlots.clear();
		inventory.clear();
		actions.clear();
	}
	
	public void open() {
		isOpen = true;
		player.openInventory(inventory);
	}
	
	public void close() {
		player.closeInventory();
	}
	
	public void reopen() {
		close();
		open();
	}
	
	public void onClose() {}
	
	public void setLocked(boolean locked) {
		isLocked = locked;
	}
	
	public void setSlotLocked(int slot, boolean locked) {
		lockedSlots.put(slot, locked);
	}
	
	public boolean isSlotLocked(int slot) {
		if (isLocked) return true;
		if (lockedSlots.containsKey(slot) && lockedSlots.get(slot)) return true;
		return false;
	}
	
	public boolean onClick(int slot, ItemStack item, InventoryAction action, boolean canceled) {
		if (actions.containsKey(slot)) {
			Runnable act = actions.get(slot);
			if (act != null) act.run(); 
		}
		
		return canceled;
	}
	
	public void onTakeItem(int slot, ItemStack item, InventoryClickEvent event) { }
	public boolean canTakeItem(int slot, ItemStack item, InventoryClickEvent event) {
		if (isSlotLocked(slot)) return false;
		return true;
	}
	
	public void onInsertItem(int slot, ItemStack item, InventoryClickEvent event) { }
	public boolean canInsertItem(int slot, ItemStack item, InventoryClickEvent event) {
		if (isSlotLocked(slot)) return false;
		return true;
	}
	
	public void onDropItem(int slot, ItemStack item, InventoryClickEvent event) { }
	public boolean canDropItem(int slot, ItemStack item, InventoryClickEvent event) {
		if (isSlotLocked(slot)) return false;
		return true;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() != player) return;
		if (!event.getInventory().equals(inventory)) return;
		
		int slot = event.getSlot();
		
		if (event.getRawSlot() != slot) return;
		
		boolean canceled = event.isCancelled();
		
		InventoryAction act = event.getAction();
		
		ItemStack item = event.getCurrentItem();
	
		if (act == InventoryAction.PLACE_ONE || act == InventoryAction.PLACE_SOME || act == InventoryAction.PLACE_ALL || act == InventoryAction.COLLECT_TO_CURSOR) item = event.getCursor();
		
		if (item == null || item.getType() == Material.AIR) return;
		
		if (act == InventoryAction.PICKUP_ONE || act == InventoryAction.PICKUP_SOME || act == InventoryAction.PICKUP_HALF || act == InventoryAction.PICKUP_ALL || act == InventoryAction.COLLECT_TO_CURSOR) {
			if (canTakeItem(slot, item, event)) { onTakeItem(slot, item, event); } else { canceled = true; }
		} else if (act == InventoryAction.PLACE_ONE || act == InventoryAction.PLACE_SOME || act == InventoryAction.PLACE_ALL) {
			if (canInsertItem(slot, item, event)) { onInsertItem(slot, item, event); } else { canceled = true; }
		} else if (act == InventoryAction.DROP_ALL_SLOT || act == InventoryAction.DROP_ONE_SLOT ) {
			if (canDropItem(slot, item, event)) { onDropItem(slot, item, event); } else { canceled = true; }
		} else if (act == InventoryAction.SWAP_WITH_CURSOR ) {
			ItemStack held = player.getItemOnCursor();
			if (canTakeItem(slot, item, event) && canInsertItem(slot, held, event)) {
				onTakeItem(slot, item, event);
				onInsertItem(slot, held, event);
			} else { canceled = true; }
		} else { canceled = true; }
		
		canceled = onClick(slot, item, act, canceled);
		
		event.setCancelled(canceled);
	}
	
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer() != player) return;
		if (!event.getInventory().equals(inventory)) return;
		
		isOpen = false;
		onClose();
	}
	
	
}
