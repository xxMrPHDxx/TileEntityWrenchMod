package com.mrphd.tew;

import org.apache.logging.log4j.Logger;

import com.mrphd.tew.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@EventBusSubscriber
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static final String MODID = "tew";
	public static final String NAME = "Tile Entity Wrench";
	public static final String VERSION = "1.0";

	private static Logger logger;
	
	@Mod.Instance
	private static Main instance;
	
	@SidedProxy(modId=MODID, serverSide="com.mrphd.tew.proxy.CommonProxy", clientSide="com.mrphd.tew.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		proxy.init();
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
