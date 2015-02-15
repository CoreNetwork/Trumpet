package us.corenetwork.trumpet;

import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NodeParser {
    private static final Pattern IF_PATTERN = Pattern.compile("if ([a-zA-Z0-9\\.]*) is([\\s]+not)?([\\s]+(?:lt|lte|gt|gte))?([\\s]+[a-zA-Z0-9,]+)");

	private double chanceMultiplier;
	private double chanceAdder;

	private boolean anyChance = false;
	private boolean stopped = false;

	public NodeParser()
	{
		chanceMultiplier = 1;
		chanceAdder = 0;
	}

	public NodeParser(double chanceMultiplier, double chanceAdder)
	{
		this.chanceMultiplier = chanceMultiplier;
		this.chanceAdder = chanceAdder;
	}

	public boolean didAnyItemHadAnyChance()
	{
		return anyChance;
	}

    protected void stopParsing()
    {
        stopped = true;
    }

	protected void parseNodeList(List<?> node)
	{
		for (Object setObject : node)
		{
			LinkedHashMap<String,?> hashMap = (LinkedHashMap<String,?>) setObject;
			Entry<String,?> firstEntry = (Entry<String, ?>) hashMap.entrySet().toArray()[0];

			String type = firstEntry.getKey();

            if (type.startsWith("if"))
                parseIfGroup(hashMap);
            else
			    parseNodeObject(type, firstEntry.getValue());

            if (stopped)
                return;
		}
	}

	protected void parseNodeObject(String type, Object node)
	{
        if (node instanceof List)
		{
			if (type.toLowerCase().startsWith("pick"))
				parsePickList(type, (List<?>) node);
			else
				parseNodeList((List<?>) node);
		}
		else if (node instanceof LinkedHashMap)
		{
            LinkedHashMap<String,?> nodeMap = (LinkedHashMap<String,?>) node;

			int count = getNumberOfRolls(node, true);
			for (int i = 0; i < count; i++)
			{
                parseNode(type, nodeMap);

                if (stopped)
                    return;
            }
		}
        else
        {
            parseNode(type, node);
        }

	}

    private void parseIfGroup(LinkedHashMap<String,?> nodeMap)
    {
        for (String key : nodeMap.keySet())
        {
            if (key.startsWith("if"))
            {
                Matcher matcher = IF_PATTERN.matcher(key.trim());
                if (!matcher.matches())
                {
                    TLog.severe("Invalid YAML configuration! Invalid if statement syntax.");
                    return;
                }

				boolean equals = matcher.group(2) == null;

				String variable = matcher.group(1);
				String operator = matcher.group(3);
                String value = matcher.group(4);

				if(variable != null)
					variable = variable.trim();
				if(operator != null)
					operator = operator.trim();
				if(value != null)
					value = value.trim();

                if (checkCondition(variable, operator, value) == equals)
                {
                    parseNodeObject(key, nodeMap.get(key));
                }
                else
                {
                    Object elseNode = nodeMap.get("else");
                    if (elseNode != null)
                        parseNodeObject("else", elseNode);
                }

                return;
            }
        }
    }

	protected void parsePickList(String params, List<?> node)
	{
		int count = getNumberOfRolls(node, false);

		int childCount = 0;
		for (Object o : node)
			if (o instanceof LinkedHashMap) childCount++;

		int[] weights = new int[childCount];
		for (int i = 0; i < childCount; i++)
			weights[i] = 1;

		int pickCount = 1;
		String paramSplit[] = params.split(" ");
		if (paramSplit.length > 1)
			pickCount = Integer.parseInt(paramSplit[1]);

		if (pickCount > childCount)
		{
			TLog.warning("Invalid Loot tables config! Amount of items to pick must be smaller or equal to amount of items!");
			return;
		}

		for (Object o : node)
		{
			if (o instanceof String)
			{
				String text = (String) o;
				if (text.startsWith("weights "))
				{
					String[] textSplit = text.split(" ");
					for (int i = 0; i < childCount; i++)
					{
						try
						{
							weights[i] = Integer.parseInt(textSplit[i + 1]);
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							TLog.warning("Invalid Loot tables config! Amount of weights must be equal to amount of items!");
							return;
						}
					}
				}

			}
		}


		int weightsSum = 0;
		for (int i = 0; i < childCount; i++)
			weightsSum += weights[i];


		for (int a = 0; a < count; a++)
		{
			List<Integer> pickedItems = new ArrayList<Integer>();
			for (int b = 0; b < pickCount; b++)
			{
				int selection = 0;
				do
				{
					int pickedNumber = TrumpetPlugin.random.nextInt(weightsSum);
					int sum = 0;
					for (int i = 0; i < childCount; i++)
					{
						sum += weights[i];
						if (pickedNumber < sum)
						{
							selection = i;
							break;
						}
					}
				}
				while (pickedItems.contains(selection));

				pickedItems.add(selection);

				int counter = -1;
				for (int i = 0; i < node.size(); i++)
				{
					Object o = node.get(i);
					if (o instanceof LinkedHashMap) 
						counter++;
					else
						continue;

					if (counter == selection)
					{
						LinkedHashMap<?,?> hashMap = (LinkedHashMap<?,?>) o;
						Entry<?,?> firstEntry = hashMap.entrySet().toArray(new Entry<?,?>[0])[0];
						parseNodeObject((String) firstEntry.getKey(), firstEntry.getValue());
						break;
					}

				}

			}
		}
	}

    protected abstract void parseNode(String type, Object node);
    protected abstract boolean checkCondition(String variable, String operator, String value);

	protected int getNumberOfRolls(Object node, boolean lowLevel)
	{
		int rolls = 1;
		double chance = 1;

		if (node instanceof List)
		{
			for (Object o : (List<?>) node)
			{
				if (o instanceof String)
				{
					String textSplit[] = ((String) o).split(" ");
					if (textSplit.length > 1 && textSplit[0].equalsIgnoreCase("rolls") && Util.isInteger(textSplit[1]))
					{
						rolls = Integer.parseInt(textSplit[1]);
					}
					else if (textSplit.length > 1 && textSplit[0].equalsIgnoreCase("chance") && Util.isDouble(textSplit[1]))
					{
						chance = Double.parseDouble(textSplit[1]);
					}
				}
			}
		}
		else if (node instanceof LinkedHashMap<?,?>)
		{
			LinkedHashMap<?,?> mapNode = (LinkedHashMap<?,?>) node;
			Integer rollsObject = (Integer) mapNode.get("rolls");
			Number chanceObject = (Number) mapNode.get("chance");

			if (rollsObject != null)
				rolls = rollsObject;
			if (chanceObject != null)
				chance = chanceObject.doubleValue();
		}

		if (lowLevel)
		{
			chance += chanceAdder;
			chance *= chanceMultiplier;			
		}

		if (chance < 0.01)
			return 0;

		anyChance = true;
		
		int num = 0;
		for (int i = 0; i < rolls; i++)
		{
			num += Math.floor(chance / 1.0);
			double newChance = chance % 1;

			double rand = TrumpetPlugin.random.nextDouble();
			if (rand < newChance)
				num++;
		}

		return num;
	}	

	public static String pickNodeChance(MemorySection section)
	{
		Map<String, Object> nodes = section.getValues(false);

		int childCount = 0;
		for (Object o : nodes.values())
			if (o instanceof MemorySection) childCount++;

		int[] weights = new int[childCount];
		for (int i = 0; i < childCount; i++)
			weights[i] = 1;

		String[] keys = new String[childCount];
		
		int counter = 0;
		for (Entry<String, Object> e : nodes.entrySet())
		{
			if (e.getValue() instanceof MemorySection)
			{
				keys[counter] = e.getKey();
				weights[counter] = ((MemorySection) e.getValue()).getInt("weight", 1);
				counter++;
			}
		}

		int weightsSum = 0;
		for (int i = 0; i < childCount; i++)
			weightsSum += weights[i];


		int pickedNumber = TrumpetPlugin.random.nextInt(weightsSum);
		int sum = 0;
		for (int i = 0; i < childCount; i++)
		{
			sum += weights[i];
			if (pickedNumber < sum)
			{
				return keys[i];
			}
		}

		return null;
	}
}
