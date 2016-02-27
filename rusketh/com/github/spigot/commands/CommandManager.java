package rusketh.com.github.spigot.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;
import rusketh.com.github.spigot.Rusketh;

public class CommandManager implements Listener {
	private HashMap<String, Command> commands;
	
	public CommandManager() {
		commands = new HashMap<String, Command>();
		addDefaultcommands();
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public Command addCommand(String name, Command cmd) {
		commands.put(name, cmd);
		return cmd;
	}
	
	public Command addCommand(String[] names, Command cmd) {
		for (String alias : names) commands.put(alias, cmd);
		return cmd;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if ( event.getMessage().startsWith("!") ) {
			
			String line = event.getMessage().toLowerCase().substring(1);
	    	
			String cmd = line.split("[\\s]")[0];
			
			String[] args = line.substring( cmd.length() ).trim().split("[\\s]+");
			
			if (commands.containsKey(cmd)) {
				Command runable = commands.get(cmd);
				
				if (runable.Call( (CommandSender) event.getPlayer(), cmd, args) ) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	private void addDefaultcommands() {
		addCommand("help", new Command(1, 1, "help <command>") {
			@Override
			public boolean run(CommandSender sender, String usedCommand, String[] args) {
				Command runable = commands.get(args[0]);
				sender.sendMessage(ChatColor.GOLD + runable.getExample());
				return false;
			}
		});
	}
}
