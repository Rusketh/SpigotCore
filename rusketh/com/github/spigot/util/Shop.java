package rusketh.com.github.spigot.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import rusketh.com.github.spigot.Rusketh;
import rusketh.com.github.spigot.menus.PagedMenu;

public class Shop extends YAMLInventory {

	public Shop(FileConfiguration YAML, String node) {
		super(YAML, node);
	}
	
	/***************************************************************************
	
		getString("Owner");
		setString("Owner", owner);
		getString("Name");
		setString("Name", name);
		getItemDoubble(slot, "Price");
		setItemDoubble(slot, "Price", value);
	
	 ***************************************************************************/
	
	/***************************************************************************/
	
	public String getName() {
		return getString("Name");
	}
	
	public void setName(String name) {
		setString("Name", name);
	}
	
	public String getOwner() {
		return getString("Owner");
	}
	
	public void setOwner(String name) {
		setString("Owner", name);
	}
	
	/***************************************************************************/
	
	
	private class ShopMenu extends PagedMenu {
		private Shop shopYAML;
		
		public ShopMenu(Player ply, int size, String name) {
			super(ply, size, name);
		}
		
		public ShopMenu(Player ply, Shop shop) {
			super(ply, shop.getSize(), shop.getString("Name"));
			shopYAML = shop;
			loadInventory();
		}
		
		public void loadShop(Shop shop) {
			shopYAML = shop;
			loadInventory();
		}
		
		private void loadInventory() {
			clear(false);
			for (int i = 0; i < getSize(); i++) {
				if (shopYAML.isEmpty(i))
					AddOption(Material.AIR, "", null);
				else
					importSlot(i);
			}
		}
		
		private void importSlot(int slot) {
			if (shopYAML.isEmpty(slot)) return;
			ItemStack item = shopYAML.getItem(slot);
			System.out.println("Importing slot: " + slot + " - " + item);
			
			if (item == null) return;
			
			double price = shopYAML.getItemDoubble(slot, "Price");
			List<String> lore = getLore(item);
			
			if (lore != null) {
				lore.add("Price: " + price);
				lore.add("Avalible: " + item.getAmount());
				setLore(item, lore);
			}
			
			AddOption(item, item.toString(), null);
		}
		
		public List<String> getLore(ItemStack item) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				return meta.getLore();
			}; return null;
		}
		
		public void setLore(ItemStack item, List<String> lore) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
		}
		
		@Override
		public void onInsertItem(int slot, ItemStack item, InventoryClickEvent event) {
			if (isSlotLocked(slot)) return;// false;
			
			int amount = shopYAML.insert(item);
			item.setAmount(amount);
			
			//if (amount == 0) event.setCurrentItem(new ItemStack(Material.AIR, 0));
			//else event.setCurrentItem(item);
			
			loadInventory();
			switchPage(getPage());
		}
	}
	
	public ShopMenu Open(Player ply) {
		ShopMenu menu = new ShopMenu(ply, this) {
			@Override
			public void onClose() {
				Rusketh.plugin.saveConfig();
			}
		};
		
		menu.openPage(1);
		
		return menu;
	}
}

