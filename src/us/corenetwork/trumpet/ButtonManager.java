package us.corenetwork.trumpet;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ginaf on 2015-02-24.
 */
public class ButtonManager {
    private static String leftSide;
    private static String rightSide;

    public static void init()
    {
        leftSide = ChatColor.translateAlternateColorCodes('&', Settings.getString(Setting.BUTTON_FORMAT_LEFT));
        rightSide = ChatColor.translateAlternateColorCodes('&', Settings.getString(Setting.BUTTON_FORMAT_RIGHT));
    }

    public static  boolean isButton(String buttonName)
    {
        return IO.config.contains(Setting.BUTTONS.getString() + "." + buttonName);
    }

    public static FancyMessage getButton(String buttonName, FancyMessage fancyMessage)
    {
        String label = IO.config.getString(Setting.BUTTONS.getString() + "." + buttonName + ".label", "");
        label = ChatColor.translateAlternateColorCodes('&', Settings.getString(Setting.BUTTON_FORMAT_TEXT_COLOR)) + label;
        String action = IO.config.getString(Setting.BUTTONS.getString() + "." + buttonName + ".action", "");

        List<String> hover = getHover(buttonName);

        fancyMessage = fancyMessage.then(leftSide).then(label);

        if(action.startsWith("/"))
        {
            fancyMessage = fancyMessage.command(action);
        }
        else
        {
            fancyMessage = fancyMessage.link(action);
        }

        if(hover != null)
        {
            fancyMessage = fancyMessage.tooltip(hover);
        }

        return fancyMessage.then(rightSide);
    }

    private static List<String> getHover(String buttonKey)
    {
        if(!IO.config.contains(Setting.BUTTONS.getString() + "." + buttonKey + ".hover"))
        {
            return null;
        }

        Object o = IO.config.get(Setting.BUTTONS.getString() + "." + buttonKey + ".hover");

        if(o instanceof String)
        {
            return new ArrayList<String>(){{add((String) o);}};
        }
        else if (o instanceof List)
        {
            List<String> list = (ArrayList<String>) o;
            return list.stream().map(string -> ChatColor.translateAlternateColorCodes('&', string)).collect(Collectors.toList());
        }
        else
        {
            return null;
        }
    }

}

