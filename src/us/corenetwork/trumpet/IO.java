package us.corenetwork.trumpet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IO {
    public static YamlConfiguration config;

    public static int numOfAnnouncements;

	public static List<Object> announcementGroups;
	public static List<String> specialGroups;
	public static List<String> ladderGroups;

	public static void LoadSettings()
	{
		try {
			config = new YamlConfiguration();

			if (!new File(TrumpetPlugin.instance.getDataFolder(),"config.yml").exists()) config.save(new File(TrumpetPlugin.instance.getDataFolder(),"config.yml"));

			config.load(new File(TrumpetPlugin.instance.getDataFolder(),"config.yml"));
			for (Setting s : Setting.values())
			{
				if (config.get(s.getString()) == null && s.getDefault() != null) config.set(s.getString(), s.getDefault());
			}

			saveConfig();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

        numOfAnnouncements = config.contains("Announcements") ? config.getList("Announcements").size() : 0;
		announcementGroups = new ArrayList<Object>();
		specialGroups = config.getStringList(Setting.RANKS_SPECIAL.getString());
		ladderGroups = config.getStringList(Setting.RANKS_LADDER.getString());

		Announcer.clear();
		for(Object o : config.getList("Announcements", new ArrayList()))
		{
			HashMap cs = (HashMap) o;
			announcementGroups.add(cs.get("anno"));
		}
	}

	public static void saveConfig()
	{
		try {
			config.save(new File(TrumpetPlugin.instance.getDataFolder(),"config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
