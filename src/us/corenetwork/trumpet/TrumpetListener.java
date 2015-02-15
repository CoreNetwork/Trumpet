package us.corenetwork.trumpet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Matej on 17.1.2014.
 */
public class TrumpetListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        AnnouncementScheulder.somebodyLoggedIn();
    }
}
