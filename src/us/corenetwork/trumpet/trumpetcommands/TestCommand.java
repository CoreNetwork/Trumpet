package us.corenetwork.trumpet.trumpetcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.trumpet.AnnouncementNodeParser;
import us.corenetwork.trumpet.Util;

public class TestCommand extends BaseTrumpetCommand {
	public TestCommand()
	{
		desc = "Test";
		needPlayer = true;
		permission = "test";
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length == 0 || !Util.isInteger(args[0]))
        {
            sender.sendMessage("Syntax: /trumpet test <nodeNumber>");
			return;
        }

        new AnnouncementNodeParser(Integer.parseInt(args[0]), (Player) sender).parse();
	}	
}
