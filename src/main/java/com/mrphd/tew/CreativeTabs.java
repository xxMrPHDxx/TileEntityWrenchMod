package com.mrphd.tew;

import com.mrphd.tew.item.ModItems;

import net.minecraft.item.ItemStack;

public class CreativeTabs {

	public static net.minecraft.creativetab.CreativeTabs DEFAULT = new Tab();
	
	private static class Tab extends net.minecraft.creativetab.CreativeTabs {
	
		public Tab() {
			super("tewMod");
		}
		
		@Override
		public ItemStack getTabIconItem() {
			return ModItems.WRENCH.getDefaultInstance();
		}
		
	}
	
}
