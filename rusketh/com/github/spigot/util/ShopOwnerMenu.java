package rusketh.com.github.spigot.util;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rusketh.com.github.spigot.Rusketh;
import rusketh.com.github.spigot.menus.BasicMenu;
import rusketh.com.github.spigot.menus.NumberWang;
import rusketh.com.github.spigot.menus.PagedMenu;

public class ShopOwnerMenu extends PagedMenu {
	private Shop shop;
	public boolean dragDrop = true;
	
	public ShopOwnerMenu(Player ply, int size, String name) {
		super(ply, size, name);
		
		Runnable toggle = new Runnable() {
			@Override
			public void run() {
				int page = getPage();
				
				//updateInventory();
				
				if (!dragDrop) {
					dragDrop = true;
					SetNavOption(5, Material.WORKBENCH, "Switch to Price Mode", this);
				} else {
					dragDrop = false;
					SetNavOption(5, Material.EMERALD, "Switch to Edit Mode", this);
				}

				switchPage(page);
			}
		};
		
		SetNavOption(5, Material.WORKBENCH, "Switch to Price Mode", toggle);
	}
	
	/*
	public Runnable createAction(int slot, double price) {
		ShopOwnerMenu menu = this;
		
		Runnable action = new Runnable() {
			@Override
			public void run() {
				shop.setPrice(slot, price);
				menu.openPage(menu.getPage());
			}
		};
		
		return new Runnable() {
			@Override
			public void run() {
				if (dragDrop) return;
				NumberWang wang = new NumberWang(getPlayer(), "Set Price");
				wang.setAction(action);
			}
		};
	}
	
	public void setShop(Shop shop) {
		this.shop = shop;
		updateInventory();
	}
	
	public void updateInventory() {
		clear(false);
		HashMap<Integer, ItemStack> items = shop.getItems();
		for (int i = 0; i < items.size(); i++) {
			if (!items.containsKey(i)) continue;
			AddOption(items.get(i), Rusketh.econemy.unitName + shop.getPrice(i), createAction(i, shop.getPrice(i)));
		}
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		if (isNavSlot(event.getSlot())) return true;
		if (dragDrop) return false;
		return true;
	}
	
	@Override
	public boolean onMoveItem(InventoryMoveItemEvent event) {
		if (!dragDrop) return false;
		
		HashMap<Integer, ItemStack> items = shop.getItems();
		
		if (event.getDestination().equals(event.getSource())) {
			
		} else if (event.getDestination().equals(openMenu)) {
			items.put(items.size(), event.getItem());
		} else if (event.getSource().equals(openMenu)) {
			int slot = openMenu.getInventory().first(event.getItem());
			items.remove(slot);
		}
		
		return true;
	}*/
}
