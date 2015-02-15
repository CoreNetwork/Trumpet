package us.corenetwork.trumpet;

import com.avaje.ebeaninternal.server.autofetch.Statistics;
import net.minecraft.server.v1_8_R1.StatisticList;
import net.minecraft.server.v1_8_R1.StatisticManager;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_8_R1.CraftStatistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matej on 12.1.2014.
 */
public class AnnouncementNodeParser extends NodeParser {
    private int announcementGroup;
    private Player messagingPlayer;

    public AnnouncementNodeParser(int announcementGroup, Player messagingPlayer) {
        this.announcementGroup = announcementGroup;
        this.messagingPlayer = messagingPlayer;
    }

    public void parse()
    {
        Object node = IO.announcementGroups.get(announcementGroup);
        if (node == null)
        {
            TLog.warning("Invalid announcement group - " + announcementGroup + "!");
            return;
        }

        if (!(node instanceof List))
        {
            TLog.severe("Invalid announcement syntax! Root node must be list (beginning with - ): " + announcementGroup);
            return;
        }

        parseNodeList((List<?>) node);
    }

    @Override
    protected void parseNode(String type, Object node) {
        if (type.equals("message") && node instanceof String)
        {

            String message = (String) node;
            if (!message.trim().isEmpty())
            {
                message = Settings.getString(Setting.MESSAGE_PREFIX) + message;
                Util.Message(message, messagingPlayer);
            }

            stopParsing();

        }
        else
        {
            TLog.warning("Unknown YAML command: " + type);
        }
    }

    @Override
    protected boolean checkCondition(String variable, String operator, String value) {
        boolean returnValue = false;

        if (variable.equals("group"))
        {
            String[] arr = value.split(",");
            if(operator == null)
            {
                for(String group : arr)
                {
                    returnValue |= PermissionHandler.inGroup(messagingPlayer, value);
                }
            }
            else
            {
                String theGroup = arr[0];

                //Special Ranks cant be compared with range comparators
                if (IO.specialGroups.contains(theGroup))
                    return false;

                if (!IO.ladderGroups.contains(theGroup))
                {
                    TLog.warning("If statement contains a grop " + theGroup + " which isnt present in Ranks Ladder node");
                    return false;
                }

                int indexOfTheGroup = IO.ladderGroups.indexOf(theGroup);
                String playerPrimaryGroup = PermissionHandler.getPrimaryGroup(messagingPlayer);
                int indexOfPrimaryGroup = IO.ladderGroups.indexOf(playerPrimaryGroup);
                switch (operator)
                {
                    case "lte":
                        return indexOfPrimaryGroup <= indexOfTheGroup;
                    case "lt":
                        return indexOfPrimaryGroup < indexOfTheGroup;
                    case "gte":
                        return indexOfPrimaryGroup >= indexOfTheGroup;
                    case "gt":
                        return indexOfPrimaryGroup > indexOfTheGroup;
                }
            }
        }
        else
        {
            int statisticValue = 0;

            Statistic statistic = CraftStatistic.getBukkitStatisticByName(variable);


            net.minecraft.server.v1_8_R1.Statistic nmsStat = StatisticList.getStatistic(variable);


            if(statistic != null && nmsStat != null)
            {
                if (statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM)
                {
                    Material material = CraftStatistic.getMaterialFromStatistic(nmsStat);
                    statisticValue = messagingPlayer.getStatistic(statistic, material);
                }
                else if (statistic.getType() == Statistic.Type.ENTITY)
                {
                    EntityType entityType = CraftStatistic.getEntityTypeFromStatistic(nmsStat);
                    statisticValue = messagingPlayer.getStatistic(statistic, entityType);
                }
                else
                {
                    statisticValue = messagingPlayer.getStatistic(statistic);
                }

                int intValue = Integer.parseInt(value);

                if(operator == null)
                {
                    returnValue = statisticValue == intValue;
                }
                else
                {
                    switch (operator)
                    {
                        case "lte":
                            returnValue = statisticValue <= intValue;
                            break;
                        case "lt":
                            returnValue = statisticValue < intValue;
                            break;
                        case "gte":
                            returnValue = statisticValue >= intValue;
                            break;
                        case "gt":
                            returnValue = statisticValue > intValue;
                            break;

                    }
                }
            }
            else
            {
                TLog.warning("Unknown statistic name in if statement ! " + variable);
            }
        }
        return returnValue;
    }
}
