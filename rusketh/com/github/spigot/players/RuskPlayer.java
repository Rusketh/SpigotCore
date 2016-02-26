package rusketh.com.github.spigot.players;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable.InputStream;
import org.bukkit.entity.Player;

import rusketh.com.github.spigot.Rusketh;

public class RuskPlayer {
	
	private Player player;
	private PlayerManager manager;
	private HashMap<String, Object> vars;
	
	private String dir;
	private File file;
	private FileConfiguration config;
	
	public RuskPlayer(Player ply) {
		player = ply;
		vars = new HashMap<String, Object>();
		
		dir = Rusketh.plyMgr.dir + "/" + ply.getName();
		file = new File(dir, "data.yml");
		LoadPlayerYML();
	}
	
	public Object Get(String key) { return vars.get(key); }
	public Object Set(String key, Object value) { return vars.put(key, value); }
	public Object ContainsKey(String key) { return vars.containsKey(key); }
	public Object ContainsValue(Object value) { return vars.containsValue(value); }
	public HashMap<String, Object> GetVars() { return vars; }
	
	public FileConfiguration GetYML() { return config; }
	
	public void LoadPlayerYML() {
		try {
			File dir = new File(file.getParent());
			dir.mkdirs();
			
			config = new YamlConfiguration();
		
			if (!file.exists()) {
				file.createNewFile();
				FileInputStream inStream = new FileInputStream(Rusketh.plyMgr.defYMLFile);
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(file));
				
				while ( inStream.available() != 0 ) {
					outputStream.write(inStream.read());
				}
				
				inStream.close();
				outputStream.close();
				
				config.load(file);
			}
		} catch (IOException | InvalidConfigurationException ex) {
			ex.printStackTrace();
		}
	}
	
	public void SavePlayerYML() {
		try {
			config.save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
