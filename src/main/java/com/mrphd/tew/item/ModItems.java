package com.mrphd.tew.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.mrphd.tew.IHasModel;

import net.minecraft.item.Item;

public class ModItems {

	private static final List<Item> ITEMS;
	
	public static final Item WRENCH;
	public static final Item TILE_BOX;
	
	static {
		final List<Item> items = new ArrayList<Item>();
		
		items.add(WRENCH = new ItemWrench());
		items.add(TILE_BOX = new ItemTileBox());
		
		ITEMS = Collections.unmodifiableList(items);
	} 
	
	public static void registerItems(final Consumer<Item> consumer) {
		for(final Item item : ITEMS) {
			consumer.accept(item);
			if(item instanceof IHasModel) {
				((IHasModel) item).registerModel();
			}
		}
	}
	
	public static List<Item> registerModels(final Consumer<Item> consumer) {
		return ITEMS;
	}
	
}
