package com.example;

import com.pkhelper.PKHelperPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PKHelperPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PKHelperPlugin.class);
		RuneLite.main(args);
	}
}