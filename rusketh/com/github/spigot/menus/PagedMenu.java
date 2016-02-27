package rusketh.com.github.spigot.menus;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import rusketh.com.github.spigot.Rusketh;

public class PagedMenu implements Listener {
	private class MenuOption {
		public Material mat;
		public String name;
		public Runnable action;
		public ItemStack item;
		public boolean locked = false;
		
		public MenuOption(ItemStack item, String name, Runnable action) {
			this.name = name;
			this.action = action;
			this.item = item;
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
	    		meta.setDisplayName(name);
	    		item.setItemMeta(meta);
	    	}
		}
		
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
		
		public MenuOption(String ply, String name, Runnable action) {
			this.name = name;
			this.action = action;
			this.mat = Material.SKULL;
			this.item = new ItemStack(Material.SKULL);
			SkullMeta meta = (SkullMeta) this.item.getItemMeta();
			if (meta != null) {
	    		meta.setDisplayName(name);
				meta.setOwner(name);
	    		item.setItemMeta(meta);
	    	}
		}
	}
	
	private Player player;
	private String menuName;
	private int page = 1;
	private int slotsPerPage;
	private HashMap<Integer, MenuOption> options;
	private HashMap<Integer, MenuOption> navigation;
	protected BasicMenu openMenu;
	
	private boolean open = false;
	public boolean locked = false;
	
	public PagedMenu(Player ply, int size, String name) {
		player = ply;
		menuName = name;
		slotsPerPage = size;
		options = new HashMap<Integer, MenuOption>();
		navigation = new HashMap<Integer, MenuOption>();
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
		init();
	}
	
	public void init() {}
	
	public Player getPlayer() { return player; }
	
	public boolean isOpen() {
		if (openMenu != null) return openMenu.isOpen();
		return false;
	}

	public ItemStack AddOption(Material mat, String name, Runnable action) {
		MenuOption option = new MenuOption(mat, name, action);
		options.put(options.size(), option);
		return option.item;
	}
	
	public ItemStack AddOption(ItemStack item, String name, Runnable action) {
		MenuOption option = new MenuOption(item, name, action);
		options.put(options.size(), option);
		return option.item;
	}
	
	public ItemStack AddHeadedOption(String ply, String name, Runnable action) {
		MenuOption option = new MenuOption(ply, name, action);
		options.put(options.size(), option);
		return option.item;
	}
	
	public ItemStack AddNavOption(Material mat, String name, Runnable action) {
		MenuOption option = new MenuOption(mat, name, action);
		navigation.put(navigation.size() - 1, option);
		return option.item;
	}
	
	public ItemStack AddHeadedNavOption(String ply, String name, Runnable action) {
		MenuOption option = new MenuOption(ply, name, action);
		navigation.put(navigation.size() - 1,option);
		return option.item;
	}
	
	public ItemStack SetNavOption(int pos, Material mat, String name, Runnable action) {
		MenuOption option = new MenuOption(mat, name, action);
		navigation.put(pos, option);
		return option.item;
	}
	
	public ItemStack SetHeadedNavOption(int pos, String ply, String name, Runnable action) {
		MenuOption option = new MenuOption(ply, name, action);
		navigation.put(pos, option);
		return option.item;
	}
	
	public boolean isNavSlot(int slot) {
		return slot >= slotsPerPage;
	}
	
	public int getTotalPages() {
		int pages = (int) Math.ceil(options.size() / slotsPerPage);
		if (pages < 1) pages = 1;
		return pages;
	}
	
	public void close() {
		if (openMenu != null) openMenu.close();
	}
	
	public void clear(boolean bool) {
		page = 1;
		options.clear();
		if (openMenu != null) openMenu.clear();
		if (bool) navigation.clear();
	}
	
	public int getPage() { return page; }
	
	public boolean openPage(int page) {
		if (openMenu != null) openMenu.close();
		
		int tPages = getTotalPages();
		if (page > tPages) return false;
		
		PagedMenu pagedMenu = this;
		
		openMenu = new BasicMenu(player, slotsPerPage + 9, menuName + "(" + page + "/" + tPages + ")") {
			@Override
			public void onClose() { pagedMenu.onClose(); }
			
			@Override
			public boolean isSlotLocked(int slot) { return pagedMenu.isSlotLocked(slot); }
			
			@Override
			public boolean onClick(int slot, ItemStack item, InventoryAction action, boolean canceled) {
				return pagedMenu.onClick(slot, item, action, canceled);
			}
			
			@Override
			public void onTakeItem(int slot, ItemStack item) {
				pagedMenu.onTakeItem(toOptionSlot(slot), item);
			}
			
			@Override
			public boolean canTakeItem(int slot, ItemStack item) {
				if (isSlotLocked(slot)) return false;
				return pagedMenu.canTakeItem(toOptionSlot(slot), item);
			}
			
			@Override
			public void onInsertItem(int slot, ItemStack item) {
				pagedMenu.onInsertItem(toOptionSlot(slot), item);
			}
			
			@Override
			public boolean canInsertItem(int slot, ItemStack item) {
				if (isSlotLocked(slot)) return false;
				return pagedMenu.canInsertItem(toOptionSlot(slot), item);
			}
			
			@Override
			public void onDropItem(int slot, ItemStack item) {
				pagedMenu.onDropItem(toOptionSlot(slot), item);
			}
			
			@Override
			public boolean canDropItem(int slot, ItemStack item) {
				if (isSlotLocked(slot)) return false;
				return pagedMenu.canDropItem(toOptionSlot(slot), item);
			}
		};
		
		populatePage(page, tPages);
		
		openMenu.open();

		this.page = page;
		
		return true;
	}
	
	public boolean switchPage(int page) {
		if (openMenu == null) return openPage(page);
		
		int tPages = getTotalPages();
		if (page > tPages) return false;
		
		openMenu.clear();
		
		openMenu.SetTitle(menuName + "(" + page + "/" + tPages + ")");
		
		populatePage(page, tPages);
		
		this.page = page;
		
		player.updateInventory();
		
		return true;
	}
	
	public void populatePage(int page, int tPages) {
		int start = (page - 1) * slotsPerPage;
		
		for(int slot = 0; slot <= slotsPerPage - 1; slot++ ) {
			if (start + slot > options.size() - 1) break;
			
			MenuOption opt = options.get(start + slot);
			
			if (opt == null) break;
				
			openMenu.AddOption(slot, opt.mat, opt.name, opt.action);
		}
		
		if (page > 1) {
			openMenu.AddOption(slotsPerPage, Material.ARROW, "Previous Page", new Runnable() {
				@Override
				public void run() {switchPage(page - 1);}
			});
		} else {
			openMenu.AddOption(slotsPerPage, Material.BARRIER, "No Previous Page", null);
		}
		
		for(int i = 0; i <= 6; i++) {
			if (!navigation.containsKey(i)) continue;
			
			MenuOption opt = navigation.get(i);
			
			if (opt != null) openMenu.AddOption(slotsPerPage + 1 + i, opt.mat, opt.name, opt.action);
		}
			
		if (page < tPages) {
			openMenu.AddOption(slotsPerPage + 8, Material.ARROW, "Next Page", new Runnable() {
				@Override
				public void run() {switchPage(page + 1);}
			});
		} else {
			openMenu.AddOption(slotsPerPage + 8, Material.BARRIER, "No Next Page", null);
		}
	}
	
	public void onClose() {}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void setSlotLocked(int slot, boolean locked) {
		if (options.containsKey(slot)) {
			MenuOption opt = options.get(slot);
			if (opt != null) opt.locked = locked;
		}
	}
	
	public int toOptionSlot(int slot) {
		return slot + ((page - 1) * slotsPerPage);
	}
	
	public boolean isSlotLocked(int slot) {
		if (locked || isNavSlot(slot)) return true;
		
		int opt = toOptionSlot(slot);

		if (options.containsKey(opt)) {
			MenuOption option = options.get(opt);
			if (option != null) return option.locked;
		}
		
		return false;
	}
	
	public boolean onClick(int slot, ItemStack item, InventoryAction action, boolean canceled) {
		int opt = toOptionSlot(slot);

		if (options.containsKey(opt)) {
			MenuOption option = options.get(opt);
			if (option != null && option.action != null) option.action.run();
		}
		
		return canceled;
	}
	
	public void onTakeItem(int slot, ItemStack item) { }
	public boolean canTakeItem(int slot, ItemStack item) {
		return true;
	}
	
	public void onInsertItem(int slot, ItemStack item) { }
	public boolean canInsertItem(int slot, ItemStack item) {
		return true;
	}
	
	public void onDropItem(int slot, ItemStack item) { }
	public boolean canDropItem(int slot, ItemStack item) {
		return true;
	}
}
