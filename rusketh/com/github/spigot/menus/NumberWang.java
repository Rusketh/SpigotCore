package rusketh.com.github.spigot.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import rusketh.com.github.spigot.Rusketh;

public class NumberWang implements Listener {
	private Player player;
	private String menuName;
	private BasicMenu openMenu;
	private Runnable action;
	
	private int min = 0;
	private int max = 1000000;
	private int value = 0;
	
	public NumberWang(Player ply, String name) {
		player = ply;
		menuName = name;
		createNumberWang();
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public Player getPlayer() { return player; }
	
	public int getValue() { return value; }
	public void setValue(int val) { value = val; }
	public void updateValue(int val) {
		value = val;
		if (openMenu != null) openMenu.SetTitle(menuName.replace("%v%", "" + value));
	}
	
	public int getMin() { return min; }
	public void setMin(int mn) { min = mn; }
	
	public int getMax() { return max; }
	public void setMax(int mx) { max = mx; }
	
	public void setMinMax(int mn, int mx) {
		min = mn;
		max = mx;
	}
	
	public void open() {
		if (openMenu != null) openMenu.open();
	}
	
	public void close() {
		if (openMenu != null) openMenu.close();
	}

	public boolean onChange(int value, int increase) { return true; }
	public void postChange(int value, int increase) { }
	
	private Runnable createAction(int modifier) {
		return new Runnable() {
			@Override
			public void run() {
				if (onChange(value, modifier)) {
					value = value + modifier;
					if (value < min) value = min; else if (value > max) value = max;
					if (openMenu != null) openMenu.SetTitle(menuName.replace("%v%", "" + value));
					postChange(value, modifier);
				};
			}
		};
	}
	
	public void setAction(Runnable act) {
		action = act;
		if (openMenu != null) openMenu.AddOption(4, Material.MAGMA_CREAM, "Finished", action);
	}
	
	private void createNumberWang() {
		NumberWang numberWang = this;
		
		openMenu = new BasicMenu(player, 9, menuName.replace("%v%", "" + value)) {
			@Override
			public void onClose() { numberWang.onClose(); }
		};
		
		openMenu.AddOption(0, Material.DIAMOND_BLOCK, "-100", createAction(-100));
		openMenu.AddOption(1, Material.GOLD_BLOCK, "-50", createAction(-50));
		openMenu.AddOption(2, Material.IRON_BLOCK, "-10", createAction(-10));
		openMenu.AddOption(3, Material.REDSTONE_BLOCK, "-1", createAction(-1));
		
		openMenu.AddOption(4, Material.MAGMA_CREAM, "Finished", action);

		openMenu.AddOption(5, Material.REDSTONE_BLOCK, "+1", createAction(1));
		openMenu.AddOption(6, Material.IRON_BLOCK, "+10", createAction(10));
		openMenu.AddOption(7, Material.GOLD_BLOCK, "+50", createAction(50));
		openMenu.AddOption(8, Material.DIAMOND_BLOCK, "+100", createAction(100));
		
		openMenu.setLocked(true);
	}
	
	public void onClose() {}
}
