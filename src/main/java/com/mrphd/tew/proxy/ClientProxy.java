package com.mrphd.tew.proxy;

import com.mrphd.tew.Utils;
import com.mrphd.tew.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}

	@Override
	public void init() {
		super.init();
		
		System.out.println("Registering custom item mesh definitions...");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.TILE_BOX, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				final NBTTagCompound nbt;
				if (stack.getItem() == ModItems.TILE_BOX && stack.hasTagCompound() && (nbt = stack.getTagCompound()).hasKey("Block")) {
					final ItemStack blockStack = new ItemStack(nbt.getCompoundTag("Block"));
					if (blockStack != null && !blockStack.isEmpty() && blockStack.getItem() instanceof ItemBlock) {
						final Block block = ((ItemBlock) blockStack.getItem()).getBlock();
						if(block == Blocks.CHEST) {
							return new ModelResourceLocation(Utils.location("tile_box"), "inventory");
						}
						return new ModelResourceLocation(block.getRegistryName(), "inventory");
					}
				}
				return null;
			}
		});
		System.out.println("Done registering custom item mesh definitions.");
	}
	
	@Override
	public void postInit() {
		super.postInit();
	}

	@Override
	public void registerModel(Item item, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
	}

}
