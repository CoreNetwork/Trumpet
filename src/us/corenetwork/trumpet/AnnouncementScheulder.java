package us.corenetwork.trumpet;

import org.bukkit.Bukkit;

import java.io.*;

/**
 * Created by Matej on 17.1.2014.
 */
public class AnnouncementScheulder implements Runnable {
    public static final int TICKS_PER_MINUTE = 60 * 20;
    public static final File SCHEDULE_FILE = new File(TrumpetPlugin.instance.getDataFolder().getPath(), "next.txt");
    private static long nextTime = 0;

    public static void init()
    {
        loadNextTime();
        Bukkit.getScheduler().runTaskTimer(TrumpetPlugin.instance, new AnnouncementScheulder(), 20, 20 * 5);
    }

    public static void somebodyLoggedIn()
    {
        if (nextTime < 0)
        {
            TLog.debug("Scheduled new announcement: Somebody came online!");

            reschedule();
        }
    }

    private static void reschedule()
    {
        int numOfPlayers = Bukkit.getServer().getOnlinePlayers().size();
        if (numOfPlayers == 0)
        {
            TLog.debug("Scheduled new announcement: No players online!");

            setNextTime(-1);
            IO.saveConfig();
            return;
        }

        int max = Settings.getInt(Setting.ANNOUNCEMENT_MAXIMUM_INTERVAL) * TICKS_PER_MINUTE;
        int min = Settings.getInt(Setting.ANNOUNCEMENT_MINIMUM_INTERVAL) * TICKS_PER_MINUTE;
        int scheduleTime = (int) Math.round(Settings.getDouble(Setting.ANNOUNCEMENT_BASE_INTERVAL) / numOfPlayers) * TICKS_PER_MINUTE;

        if (scheduleTime > max)
            scheduleTime = max;
        else if (scheduleTime < min)
            scheduleTime = min;

        setNextTime(getWorldTime() + scheduleTime);

        TLog.debug("Scheduled new announcement: at " + nextTime + " (" + scheduleTime + " ticks from now), players: " + numOfPlayers);
    }

    @Override
    public void run() {
        if (nextTime >= 0 && nextTime < getWorldTime())
        {
            Announcer.announce();
            reschedule();
        }
    }

    private static long getWorldTime()
    {
        return Bukkit.getWorlds().get(0).getFullTime();
    }

    private static void loadNextTime()
    {
        if (!SCHEDULE_FILE.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE_FILE));
            nextTime = Long.parseLong(reader.readLine());
            reader.close();
        } catch (Exception e) {
            TLog.warning("Error while loading next.txt. Is file corrupt? - " + e.getMessage());
            nextTime = 0;
        }
    }

    private static void setNextTime(long time)
    {
        nextTime = time;
        saveNextTime();
    }

    public static void saveNextTime()
    {
        try {
            FileWriter writer = new FileWriter(SCHEDULE_FILE);
            writer.write(Long.toString(nextTime));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
