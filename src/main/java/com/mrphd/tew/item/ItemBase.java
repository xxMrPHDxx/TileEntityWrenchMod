package com.mrphd.tew.item;

import com.mrphd.tew.IHasModel;
import com.mrphd.tew.Main;
import com.mrphd.tew.Utils;

import net.minecraft.item.Item;

public abstract class ItemBase extends Item implements IHasModel {
	
	protected ItemBase(final String name) {
		setRegistryName(Utils.location(name));
		setUnlocalizedName(name);
	}
	
	@Override
	public void registerModel() {
		Main.proxy.registerModel(this, 0, "inventory");
	}
	
}
