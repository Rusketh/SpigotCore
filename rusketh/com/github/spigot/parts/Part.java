package rusketh.com.github.spigot.parts;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import rusketh.com.github.spigot.Rusketh;
import rusketh.com.github.spigot.commands.Command;

public abstract class Part implements Listener {
	public Rusketh rusketh;
	
	protected Part(Rusketh plugin) {
		rusketh = plugin;
		registerEvents(this);
	}
	
	public void registerEvents(Listener listener) {
		rusketh.getServer().getPluginManager().registerEvents(listener, rusketh);
	}
	
	public Server getServer() { return rusketh.getServer(); }

	public FileConfiguration getConfig() { return rusketh.getConfig(); }
	public void saveConfig() { rusketh.saveConfig(); }

	public void addCommand(String name, Command cmd) { rusketh.cmdMgr.addCommand(name, cmd); }
	public void addCommand(String[] name, Command cmd) { rusketh.cmdMgr.addCommand(name, cmd); }
	public void getPlayer(Player ply) { rusketh.plyMgr.getPlayer(ply); }
}
