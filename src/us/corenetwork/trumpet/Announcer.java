package us.corenetwork.trumpet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Matej on 17.1.2014.
 */
public class Announcer {

    private static Map<UUID, List<Integer>> playerAnnouncementQueue = new HashMap<UUID, List<Integer>>();

    public static void clear()
    {
        playerAnnouncementQueue.clear();
    }

    public static void announce()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            Integer announcementGroup = getNextAnnouncementForPlayer(player.getUniqueId());

            if(announcementGroup != null)
            {
                new AnnouncementNodeParser(announcementGroup, player).parse();
            }
        }
    }

    private static Integer getNextAnnouncementForPlayer(UUID uuid)
    {
        List<Integer> queue = playerAnnouncementQueue.get(uuid);

        if(queue == null || queue.isEmpty())
        {
            queue = generateNewQueue();
            playerAnnouncementQueue.put(uuid, queue);
        }

        //if at this point in time there were no announcements in config (can happen due to reload), return null
        if(queue == null || queue.isEmpty())
        {
            return null;
        }

        int nextGroup = queue.get(0);
        queue.remove(0);

        if(queue.size() == 0)
        {
            queue = generateNewQueue();

            if(queue.size() > 1)
            {
                while(queue.get(0) == nextGroup)
                    Collections.shuffle(queue);
            }

            playerAnnouncementQueue.put(uuid, queue);
        }

        return nextGroup;
    }


    private static List<Integer> generateNewQueue()
    {
        List<Integer> newQueue = IntStream.range(0, IO.announcementGroups.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(newQueue);
        return newQueue;
    }
}
