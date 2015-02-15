package us.corenetwork.trumpet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TLog {
	public static void debug(String text)
	{
		if (Settings.getBoolean(Setting.DEBUG))
			sendLog("&f[&3Trumpet&f]&f " + text);
	}

	public static void info(String text)
	{
		sendLog("&f[&fTrumpet&f]&f " + text);
	}

	public static void warning(String text)
	{
		sendLog("&f[&eTrumpet&f]&f " + text);
	}

	public static void severe(String text)
	{
		sendLog("&f[&cTrumpet&f]&f " + text);
	}

	public static void sendLog(String text)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}
}
