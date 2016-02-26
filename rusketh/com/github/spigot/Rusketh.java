package rusketh.com.github.spigot;

import java.io.InputStream;

import org.bukkit.plugin.java.JavaPlugin;

import rusketh.com.github.spigot.commands.Command;
import rusketh.com.github.spigot.commands.CommandManager;
import rusketh.com.github.spigot.players.PlayerManager;


public class Rusketh extends JavaPlugin {
	public static Rusketh plugin;
	public static CommandManager cmdMgr;
	public static PlayerManager plyMgr;
	
	@Override
	public void onEnable() {
		plugin = this;
		cmdMgr = new CommandManager();
		plyMgr = new PlayerManager();
		getServer().getPluginManager().registerEvents(cmdMgr, this);
		new MenuExample();
	}
	
	public InputStream grabResource(String file) {
		return getResource(file);
	}
	
	public static Command addCommand(String name, Command cmd) {
		return cmdMgr.addCommand(name, cmd);
	}
	
	public Command addCommand(String[] names, Command cmd) {
		return cmdMgr.addCommand(names, cmd);
	}
}
