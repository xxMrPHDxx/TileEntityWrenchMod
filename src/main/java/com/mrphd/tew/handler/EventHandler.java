package com.mrphd.tew.handler;

import com.mrphd.tew.Main;
import com.mrphd.tew.item.ModItems;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=Main.MODID)
public class EventHandler {

	@SubscribeEvent
	public static final void onItemRegister(final Register<Item> event) {
		ModItems.registerItems(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	public static final void onGuiDrawScreenEvent(final GuiScreenEvent.DrawScreenEvent event) {
		final GuiScreen screen = event.getGui();
		if(screen instanceof GuiContainer) {
			final GuiContainer container = (GuiContainer) screen;
			container.inventorySlots.getInventory();
		}
	}
	
}
