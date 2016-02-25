package rusketh.com.github.spigot.commands;

public abstract class PlayerOnlyCommand extends Command {

	public PlayerOnlyCommand(int min, int max, String ex) {
		super(min, max, ex);
		setConsoleAllowed(false);
	}

}
