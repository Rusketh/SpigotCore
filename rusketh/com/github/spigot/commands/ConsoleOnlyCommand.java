package rusketh.com.github.spigot.commands;

public abstract class ConsoleOnlyCommand extends Command {

	public ConsoleOnlyCommand(int min, int max, String ex) {
		super(min, max, ex);
		setPlayerAllowed(false);
	}

}