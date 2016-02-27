package rusketh.com.github.spigot.players;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import rusketh.com.github.spigot.Rusketh;

public class RuskPlayer {
	
	private Player player;
	private HashMap<String, Object> vars;
	
	public RuskPlayer(Player ply) {
		player = ply;
		vars = new HashMap<String, Object>();
	}
	
	public Object get(String key) { return vars.get(key); }
	public Object set(String key, Object value) { return vars.put(key, value); }
	public Object containsKey(String key) { return vars.containsKey(key); }
	public Object containsValue(Object value) { return vars.containsValue(value); }
	public HashMap<String, Object> getVars() { return vars; }
	
	public FileConfiguration getData() { return Rusketh.plyMgr.getPlayerData(player.getName()); }
	public void saveData() { Rusketh.plyMgr.savePlayerData(player.getName()); }
	
}
