package us.corenetwork.trumpet;


import java.util.ArrayList;
import java.util.HashMap;

public enum Setting {
	
	DEBUG("Debug", false),

    ANNOUNCEMENT_BASE_INTERVAL("AnnouncementBaseIntervalMinutes", 30),
    ANNOUNCEMENT_MINIMUM_INTERVAL("AnnouncementMinimumIntervalMinutes", 3),
    ANNOUNCEMENT_MAXIMUM_INTERVAL("AnnouncementMaximumIntervalMinutes", 90),

    MESSAGE_PREFIX("MessagePrefix", "&c"),

	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_CONFIGURATION_RELOADED("Messages.ConfigurationReloaded", "Configuration reloaded successfully!"),

	BUTTON_FORMAT_LEFT("ButtonFormat.Left", "&6["),
	BUTTON_FORMAT_RIGHT("ButtonFormat.Right", "&6]"),
	BUTTON_FORMAT_TEXT_COLOR("ButtonFormat.TextColor", "&5"),

	BUTTONS("Buttons", new HashMap<String, Object>(){{
		put("reddit-flatcore", new HashMap<String, Object>(){{
			put("label", "/r/flatcore");
			put("hover", new ArrayList<String>(){{
				add("&7Click to visit our subreddit");
				add("&8Make sure to subscribe!");
			}});
			put("action", "http://www.reddit.com/r/flatcore");
		}});
	}}),


	RANKS_SPECIAL("Ranks.Special", new ArrayList<String>(){{
		add("Guardian");
		add("SubSky");
		add("SubGrass");
	}}),
	RANKS_LADDER("Ranks.Ladder", new ArrayList<String>(){{
		add("Novice");
		add("Flatcorian");
		add("FlatcorianAdept");
		add("FlatcorianVeteran");
		add("FlatcorianMaster");
		add("Nomad");
		add("NomadAdept");
		add("NomadVeteran");
		add("NomadMaster");
		add("Raider");
		add("RaiderAdept");
		add("RaiderVeteran");
		add("RaiderMaster");
		add("Mercenary");
		add("MercenaryAdept");
		add("MercenaryVeteran");
		add("MercenaryMaster");
		add("Merchant");
		add("MerchantAdept");
		add("MerchantVeteran");
		add("MerchantMaster");
		add("Scribe");
		add("ScribeAdept");
		add("ScribeVeteran");
		add("ScribeMaster");
		add("Savant");
		add("SavantAdept");
		add("SavantVeteran");
		add("SavantMaster");
		add("Special");
	}}),
	;

	private String name;
	private Object def;
	
	private Setting(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getString()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
}
