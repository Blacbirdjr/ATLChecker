package com.orthus.ATLChecker;

import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import com.mojang.authlib.GameProfile;
import com.orthus.ATLChecker.*;
import com.orthus.ATLChecker.Json.JsonReader;
import com.orthus.ATLChecker.Json.VersionCompare;
import com.orthus.ATLChecker.commands.CommandConsoleMessageToggle;
import com.orthus.ATLChecker.commands.CommandManCheck;
import com.orthus.ATLChecker.commands.CommandOperatorMessageToggle;

@Mod(modid = "ATLChecker", version = "1.0.0", acceptedMinecraftVersions = "*", acceptableRemoteVersions = "*")
public class ATLChecker
{
	public static String LocalVersion;
	public static String PackName;
	public static String OpMessage;
	public static String ConsoleMessage;
	public static String LatestVersion;
	public static String FailureMessage;
	public static Boolean ConsoleOut;
	public static Boolean OperatorOut;
	public static Boolean CheckResult;
	public static Boolean OpStatus;
	
	//Forge Methods
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());

    	config.load();
    	
    	PackName = config.get(config.CATEGORY_GENERAL  ,"Pack", "Default" ).getString();
    	LocalVersion = config.get(config.CATEGORY_GENERAL  ,"Current Version", "0.0.3" ).getString();
    	OpMessage = config.get(config.CATEGORY_GENERAL  ,"Operator Message", "Server out of date." ).getString();
    	ConsoleMessage = config.get(config.CATEGORY_GENERAL  ,"Console Message", "Server out of date." ).getString();
    	FailureMessage = config.get(config.CATEGORY_GENERAL  ,"API Failure Message", "Version returned as " + LatestVersion).getString();
    	ConsoleOut = config.get(config.CATEGORY_GENERAL, "Console Output", true).getBoolean();
    	OperatorOut = config.get(config.CATEGORY_GENERAL, "Operator Output", true).getBoolean();
    	config.save();
    	peformCheck();
    	FMLCommonHandler.instance().bus().register(new ATLEventHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    @Mod.EventHandler
    public void serverstart(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CommandManCheck());
    	event.registerServerCommand(new CommandConsoleMessageToggle());
    	event.registerServerCommand(new CommandOperatorMessageToggle());
    }
    @Mod.EventHandler
    public void postInit(FMLServerStartedEvent event){
    	StartOutput();
	}
    

    // Custom Methods/functions
    
    //--------------------------------------------------------------
    
    
    public static void peformCheck()
    {
    	// get Json
    	LatestVersion = JsonReader.main(PackName);
    	CheckResult = VersionCompare.main(LatestVersion , LocalVersion);
    }
    
    /*public static void OpCheck()
    {
		OpStatus = PlayerTracker.main();
		
	}*/

    public static void StartOutput()
    {
    	// should be != only have it as == for testing output
    	if (CheckResult != true)
    	{
    		if (ConsoleOut == true)
    		{
			// outputs [FML] ConsoleMessage
    			if (LatestVersion != "null")
    			{
        			FMLLog.info(String.format(ConsoleMessage, LocalVersion, LatestVersion));
    			}
    			else 
    			{
    				FMLLog.info(String.format(FailureMessage));

    			}
    		}

    	}
    }    
}