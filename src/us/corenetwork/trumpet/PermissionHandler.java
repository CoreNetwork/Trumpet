package us.corenetwork.trumpet;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Created by Matej on 14.1.2014.
 */
public class PermissionHandler {
    private static Permission permission = null;

    public static boolean inGroup(Player player, String group)
    {
        if (!setupPermissions())
        {
            TLog.severe("Unable to contact Vault!");
            return false;
        }

        return permission.playerInGroup(player, group);
    }

    public static String getPrimaryGroup(Player player)
    {
        if (!setupPermissions())
        {
            TLog.severe("Unable to contact Vault!");
            return null;
        }
return permission.getPrimaryGroup(player);
        //return permission.getPlayerGroups(player);
    }
    public static String[] getGroups(Player player)
    {
        if (!setupPermissions())
        {
            TLog.severe("Unable to contact Vault!");
            return null;
        }
        return permission.getPlayerGroups(player);
    }
    private static boolean setupPermissions()
    {
        if (permission != null)
            return true;
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        return (permission != null);
    }
}
