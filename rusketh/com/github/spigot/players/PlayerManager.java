package rusketh.com.github.spigot.players;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import rusketh.com.github.spigot.Rusketh;

public class PlayerManager implements Listener {
	private HashMap<Player, RuskPlayer> players;

	static File defYMLFile;
	static YamlConfiguration defPlyYML;
	static String dir = "plugins/RuskethCore/players";
	
	public PlayerManager() {
		players = new HashMap<Player, RuskPlayer>();
		loadDefaultData();
		loadAllPlayers();
		
		Rusketh.plugin.getServer().getPluginManager().registerEvents(this, Rusketh.plugin);
	}
	
	public void loadDefaultData() {
		try {
			defPlyYML = new YamlConfiguration();
			defYMLFile = new File(dir + "/default", "data.yml");
			
			File dir = new File(defYMLFile.getParent());
			dir.mkdirs();
			
			if (!defYMLFile.exists()) {
				defYMLFile.createNewFile();
				InputStream inStream = Rusketh.plugin.grabResource("player.yml");
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(defYMLFile));
				
				while ( inStream.available() != 0 ) {
					outputStream.write(inStream.read());
				}
				
				inStream.close();
				outputStream.close();
			}
			
			defPlyYML.load(defYMLFile);
			
		} catch (IOException | InvalidConfigurationException ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveDefaults() {
		try {
			defPlyYML.save(defYMLFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadAllPlayers() {
		players.clear();
		for(Player ply : Rusketh.plugin.getServer().getOnlinePlayers()) {
			players.put(ply, new RuskPlayer(ply));
		}
	}
	
	public RuskPlayer getPlayer(Player ply) {
		return players.get(ply);
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player ply = event.getPlayer();
		players.put(ply, new RuskPlayer(ply));
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		Player ply = event.getPlayer();
		RuskPlayer rPly = players.get(ply);
		rPly.SavePlayerYML();
		players.remove(ply);
		rPly = null;
	}
}
