package rusketh.com.github.spigot.util;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;


public class YAMLInventory {
	private String ConfigurationNode;
	private FileConfiguration configuration;
	
	public YAMLInventory(FileConfiguration YAML, String node) {
		configuration = YAML;
		ConfigurationNode = node;
	}
	
	/***************************************************************************/
	
	protected String getString(String node) {
		return configuration.getString(this.ConfigurationNode+"."+node);
	}
	
	protected void setString(String node, String str) {
		configuration.set(this.ConfigurationNode+"."+node, str);
	}
	
	protected boolean getBoolean(String node) {
		return configuration.getBoolean(this.ConfigurationNode+"."+node);
	}
	
	protected void setBoolean(String node, boolean value) {
		configuration.set(this.ConfigurationNode+"."+node, value);
	}
	
	protected int getInt(String node) {
		return configuration.getInt(this.ConfigurationNode+"."+node);
	}
	
	protected void setInt(String node, int value) {
		configuration.set(this.ConfigurationNode+"."+node, value);
	}
	
	protected double getDouble(String node) {
		return configuration.getDouble(this.ConfigurationNode+"."+node);
	}
	
	protected void setDouble(String node, double value) {
		configuration.set(this.ConfigurationNode+"."+node, value);
	}
	
	protected ItemStack getItemStack(String node) {
		return configuration.getItemStack(this.ConfigurationNode+"."+node);
	}
	
	protected void setItemStack(String node, ItemStack item) {
		configuration.set(this.ConfigurationNode+"."+node, item);
	}
	
	/***************************************************************************/
	
	public int getSize() {
		return getInt("Size");
	}
	
	public void setSize(int size) {
		setInt("Size", size);
	}
	
	/****************************************************************************/
	
	public ItemStack getItem(int slot) {
		return getItemStack("Slots." + slot);
	}
	
	public boolean isEmpty(int slot) {
		ItemStack item = getItemStack("Slots." + slot);
		return !(item != null && item.getType() != Material.AIR);
	}
	
	public boolean isEmpty() {
		for (int i = 0; i < getSize(); i++) {
			if (!isEmpty(i)) return false;
		}; return true;
	}
	
	public int firstEmpty() {
		for (int i = 0; i < getSize(); i++) {
			if (isEmpty(i)) return i;
		}; return -1;
	}
	
	/****************************************************************************/
	
	public boolean setItem(int slot, ItemStack item) {
		if (slot < 0 || slot > getSize()) return false;
		setItemStack("Slots." + slot + ".Item", item);
		return true;
	}
	
	public boolean addItem(ItemStack item) {
		int slot = firstEmpty();
		if (slot == -1) return false;
		return setItem(slot, item);
	}
	
	public Material getType(int slot) {
		if (isEmpty(slot)) return Material.AIR;
		return getItem(slot).getType();
	}
	
	public int getAmount(int slot) {
		if (isEmpty(slot)) return 0;
		return getItem(slot).getAmount();
	}
	
	public void setAmount(int slot, int amount) {
		if (!isEmpty(slot)) getItem(slot).setAmount(amount);
	}
	
	/****************************************************************************/
	
	public String getItemString(int slot, String node) {
		return getString("Slots." + slot + "." + node);
	}
	
	public void setItemString(int slot, String node, String str) {
		setString("Slots." + slot + "." + node, str);
	}
	
	public boolean getItemBoolean(int slot, String node) {
		return getBoolean("Slots." + slot + "." + node);
	}
	
	public void setItemBoolean(int slot, String node, boolean value) {
		setBoolean("Slots." + slot + "." + node, value);
	}
	
	public int getItemInt(int slot, String node) {
		return getInt("Slots." + slot + "." + node);
	}
	
	public void setItemInt(int slot, String node, int value) {
		setInt("Slots." + slot + "." + node, value);
	}
	
	public double getItemDoubble(int slot, String node) {
		return getDouble("Slots." + slot + "." + node);
	}
	
	public void setItemDouble(int slot, String node, double value) {
		setDouble("Slots." + slot + "." + node, value);
	}
	
	/****************************************************************************/
	
	public ArrayList<ItemStack> allItems() {
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		
		for (int i = 0; i < getSize(); i++) {
			if (!isEmpty(i)) array.add(getItem(i));
		}
		
		return array;
	}
	

	/****************************************************************************/
	
	public int insert(ItemStack item) {
		int max = item.getMaxStackSize();
		int count = item.getAmount();
		Material mat = item.getType();
		
		for (int i = 0; i < getSize() && count > 0; i++) {
			if (isEmpty(i) || !getType(i).equals(mat)) continue;
			
			int space = max - getAmount(i);
			if (space <= 0) continue;
			if (space > count) space = count;
			
			count -= space;
			if (count < 0) count = 0;
			
			setAmount(i, space);
		}
		
		if (count == 0) return 0;
		
		for (int i = 0; i < getSize() && count > 0; i++) {
			if (!isEmpty(i)) continue;
			
			int space = max;
			if (space > count) space = count;
			
			count -= space;
			if (count < 0) count = 0;
			
			setItem(i, new ItemStack(mat, space));
		}
		
		return count;
	}
}