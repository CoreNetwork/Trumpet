package us.corenetwork.trumpet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import us.corenetwork.trumpet.trumpetcommands.BaseTrumpetCommand;
import us.corenetwork.trumpet.trumpetcommands.ReloadCommand;
import us.corenetwork.trumpet.trumpetcommands.TestCommand;

import java.util.HashMap;
import java.util.Random;

public class TrumpetPlugin extends JavaPlugin {
    public static TrumpetPlugin instance;

    public static Random random;

    public static HashMap<String, BaseTrumpetCommand> trumpetCommands = new HashMap<String, BaseTrumpetCommand>();

    @Override
    public void onEnable()
    {
        instance = this;
        random = new Random();

        trumpetCommands.put("reload", new ReloadCommand());
        trumpetCommands.put("test", new TestCommand());

        IO.LoadSettings();
        if (IO.numOfAnnouncements < 1)
        {
            TLog.warning("No announcements find! Shutting down plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        AnnouncementScheulder.init();
        getServer().getPluginManager().registerEvents(new TrumpetListener(), this);
    }

    @Override
    public void onDisable()
    {
        AnnouncementScheulder.saveNextTime();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 1 || Util.isInteger(args[0]))
        {
            return false;
        }

        BaseTrumpetCommand cmd = trumpetCommands.get(args[0]);
        if (cmd != null)
            return cmd.execute(sender, args, true);

        return false;
    }
}
