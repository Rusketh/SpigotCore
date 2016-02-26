package rusketh.com.github.spigot.commands;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public abstract class Command
{
	private int min = 0;
	private int max = -1;
	private String example;
	private String[] requiredPerms;
	private boolean playerCmd = true;
	private boolean consoleCmd = true;
	
	public Command(int mn, int mx, String ex) {
		min = mn;
		max = mx;
		example = ex;
	}

	public boolean playerAllowed() { return playerCmd; }
	public void setPlayerAllowed(boolean bool) { playerCmd = bool; }
	
	public boolean consoleAllowed() { return consoleCmd; }
	public void setConsoleAllowed(boolean bool) { consoleCmd = bool; }
	
	public abstract boolean run(CommandSender sender, String usedCommand, String[] args);
	
	public boolean Call(CommandSender sender, String usedCommand, String[] args) {
		boolean sucess = false;
		
		try {
			int argCount = args.length;
			
			if ( argCount == 1 && args[0].isEmpty() ) argCount = 0;
			
			if ( argCount < min ) throw new CommandException(ChatColor.RED + "Not enought parameters:\n" + example);
			
			if ( argCount > max && max != -1 ) throw new CommandException(ChatColor.RED + "Too many parameters:\n" + example);
				
			if ( sender instanceof Player ) {
				Player player = (Player)sender;
				
				if ( !playerCmd ) throw new CommandException(ChatColor.RED + "This command can only be used from console.");
				
				if ( requiredPerms != null && requiredPerms.length > 0 ) {
					Boolean hasPerm = player.isOp();
				
					for ( String perm : requiredPerms ) {
						if ( player.hasPermission(perm) ) {
							hasPerm = true;
							break;
						}
					}
					
					if ( !hasPerm ) {
						String message = "I'm sorry Dave but i can't let you do that.";
						throw new CommandException(ChatColor.RED + message);
					}
				}
			} else if ( !consoleCmd ) {
				throw new CommandException("This command can not be used from console.");
			}
			
			sucess = run(sender, usedCommand, args);
			
		} catch (CommandException e) {
			for ( String message : e.getMessage().split("\n")){
				sender.sendMessage(message);
			}
		} catch (Throwable e) {
			System.out.print("Rusk Error: '" + e.toString() + "'.");
			System.out.print("When: calling command '" + usedCommand + "'.");
			System.out.print("Please give this error message to the Plugin developer.");
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "There was an error with this command please see console."); 
		}
		
		return sucess;
	}
	
	public String getExample() {
		return example;
	}
}