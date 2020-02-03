package com.mrphd.tew.item;

import com.mrphd.tew.CreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends ItemBase {

	public ItemWrench() {
		super("wrench");
		setCreativeTab(CreativeTabs.DEFAULT);
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos,
			final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (!world.isRemote && player.isSneaking()) {
			final IBlockState state = world.getBlockState(pos);
			final Block block = state.getBlock();
			if (block.hasTileEntity(state)) {
				try {
					final TileEntity te = world.getTileEntity(pos);
					final ItemStack item = ModItems.TILE_BOX.getDefaultInstance();
					NBTTagCompound nbt = item.getTagCompound();
					if(nbt == null) nbt = new NBTTagCompound();
					nbt.setTag("Block", new ItemStack(block).writeToNBT(new NBTTagCompound()));
					nbt.setTag("TileData", te.serializeNBT());
					final NBTTagList properties = new NBTTagList();
					for(final IProperty<?> property : state.getPropertyKeys()) {
						final NBTTagCompound tag = new NBTTagCompound();
						tag.setString("Name", property.getName());
						tag.setString("Value", state.getValue(property).toString());
						properties.appendTag(tag);
					}
					nbt.setTag("Properties", properties);
					nbt.setInteger("Meta", block.getMetaFromState(state));
					item.setTagCompound(nbt);
					final Entity tilebox = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), item);// new EntityTileBox(world, te, state);
					world.spawnEntity(tilebox);
					
					if(te instanceof IInventory) {
						((IInventory)te).clear();
					}

					world.setBlockToAir(pos);
				} catch (final Exception e) {
					e.printStackTrace();
				}

				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

}
