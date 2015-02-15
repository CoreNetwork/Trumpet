package us.corenetwork.trumpet.trumpetcommands;

import org.bukkit.command.CommandSender;
import us.corenetwork.trumpet.IO;
import us.corenetwork.trumpet.Setting;
import us.corenetwork.trumpet.Settings;
import us.corenetwork.trumpet.Util;

public class ReloadCommand extends BaseTrumpetCommand {
	public ReloadCommand()
	{
		desc = "Reload config";
		needPlayer = false;
		permission = "reload";
	}


	public void run(final CommandSender sender, String[] args) {
		IO.LoadSettings();
		Util.Message(Settings.getString(Setting.MESSAGE_CONFIGURATION_RELOADED), sender);
	}	
}
