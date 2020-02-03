package com.mrphd.tew.proxy;

import com.mrphd.tew.Main;
import com.mrphd.tew.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {

	public void preInit() {
		
	}

	public void init() {
		registerEntities();
	}
	
	public void postInit() {
		
	}
	
	public void registerModel(final Item item, final int meta, final String variant) {}

	@SuppressWarnings("unused")
	private void registerEntities() {
		int id = 0;
		
		Main.getLogger().info("Done registering entities");
	}

	@SuppressWarnings("unused")
	private void registerEntity(final String registry, Class<? extends Entity> clazz, final String name, final int id, final int trackingRange, final int updateFreq) {
		EntityRegistry.registerModEntity(
				Utils.location(registry), 
				clazz, 
				name, 
				id, Main.getInstance(),
				trackingRange, 
				updateFreq, 
				true);
	}
	
}
