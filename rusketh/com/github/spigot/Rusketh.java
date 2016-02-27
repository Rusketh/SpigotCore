package rusketh.com.github.spigot;

import java.io.InputStream;

import org.bukkit.plugin.java.JavaPlugin;

import rusketh.com.github.spigot.commands.Command;
import rusketh.com.github.spigot.commands.CommandManager;
import rusketh.com.github.spigot.parts.Econemy;
import rusketh.com.github.spigot.parts.MenuExample;
import rusketh.com.github.spigot.parts.Shops;
import rusketh.com.github.spigot.players.PlayerManager;


public class Rusketh extends JavaPlugin {
	public static Rusketh plugin;
	
	public static CommandManager cmdMgr;
	public static PlayerManager plyMgr;
	
	public static MenuExample menuExample;
	public static Econemy econemy;
	public static Shops shops;
	
	
	@Override
	public void onEnable() {
		plugin = this;
		loadManagers();
		loadParts();
	}
	
	private void loadManagers() {
		cmdMgr = new CommandManager();
		plyMgr = new PlayerManager();
	}
	
	private void loadParts() {
		menuExample = new MenuExample(this);
		
		//if (getConfig().getBoolean("Econemy.Enabled")) {
			econemy = new Econemy(this);
			shops = new Shops(this);
		//}
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
