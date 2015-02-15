package us.corenetwork.trumpet;


import java.util.ArrayList;

public enum Setting {
	
	DEBUG("Debug", false),

    ANNOUNCEMENT_BASE_INTERVAL("AnnouncementBaseIntervalMinutes", 30),
    ANNOUNCEMENT_MINIMUM_INTERVAL("AnnouncementMinimumIntervalMinutes", 3),
    ANNOUNCEMENT_MAXIMUM_INTERVAL("AnnouncementMaximumIntervalMinutes", 90),

    MESSAGE_PREFIX("MessagePrefix", "&c"),

	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_CONFIGURATION_RELOADED("Messages.ConfigurationReloaded", "Configuration reloaded successfully!"),

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
