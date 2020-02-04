package com.mrphd.tew.item;

import java.util.List;

import com.google.common.base.Optional;
import com.mrphd.tew.Main;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemTileBox extends ItemBase {

	public ItemTileBox() {
		super("tile_box");
	}

	@Override
	public ItemStack getDefaultInstance() {
		final ItemStack stack = super.getDefaultInstance();
		final NBTTagCompound nbt;
		if (!stack.hasTagCompound()) stack.setTagCompound(nbt = new NBTTagCompound());
		else nbt = stack.getTagCompound();
		if (!nbt.hasKey("Block")) {
			nbt.setTag("Block", Item.getItemFromBlock(Blocks.AIR).getDefaultInstance().writeToNBT(new NBTTagCompound()));
		}
		if (!nbt.hasKey("TileData")) {
			nbt.setTag("TileData", new NBTTagCompound());
		}
		if (!nbt.hasKey("Properties")) {
			nbt.setTag("Properties", new NBTTagList());
		}
		return stack;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return super.hasCustomEntity(stack);
	}

	@Override
	public Entity createEntity(final World world, final Entity location, ItemStack stack) {
		return super.createEntity(world, location, stack);
	}

	@Override
	public void readNBTShareTag(final ItemStack stack, NBTTagCompound nbt) {
		super.readNBTShareTag(stack, nbt);
	}

	@Override
	public NBTTagCompound getNBTShareTag(final ItemStack stack) {
		return super.getNBTShareTag(stack);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		final String name = super.getItemStackDisplayName(stack);
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Block")) {
			final ItemStack blockstack = new ItemStack(stack.getTagCompound().getCompoundTag("Block"));
			final Item item = blockstack.getItem();
			if (item instanceof ItemBlock) {
				return ((ItemBlock) item).getItemStackDisplayName(blockstack) + " (" + name + ")";
			}
		}
		return name;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack);
	}

	@SuppressWarnings("unchecked")
	<P extends Comparable<P>, V> IBlockState prop(IBlockState state, IProperty<P> prop, V value) {
		return state.withProperty(prop.getClass().cast(prop), prop.getValueClass().cast(value));
	}

	@Override
	public int getItemStackLimit() {
		return 64;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		try {
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Block")) {
				final ItemStack blockStack = new ItemStack(stack.getTagCompound().getCompoundTag("Block"));
				if (blockStack.getItem() instanceof ItemBlock && stack.getTagCompound().hasKey("TileData")) {
					Block block = ((ItemBlock) blockStack.getItem()).getBlock();
					if (block == Blocks.MOB_SPAWNER) {
						String entity = stack.getTagCompound().getCompoundTag("TileData").getCompoundTag("SpawnData").getString("id");
						final StringBuilder sb = new StringBuilder();
						for (final String s : entity.split(":")[1].split("_")) {
							sb.append(String.join("", s.substring(0, 1).toUpperCase(), s.substring(1)));
						}
						entity = String.format("entity.%s.name", entity.split(":")[1].contentEquals("zombie_pigman") ? "PigZombie" : sb);
						tooltip.add(String.format("Entity: %s", new TextComponentTranslation(entity).getFormattedText()));
					}
				}
			}
		} catch (final Exception e) {
			Main.getLogger().catching(e);
		}
		super.addInformation(stack, world, tooltip, flagIn);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (!world.isRemote) {
			final BlockPos offset = pos.offset(side);

			if (world.getTileEntity(pos) != null && !player.isSneaking()) return EnumActionResult.PASS;

			try {
				final ItemStack stack;
				if ((stack = player.getHeldItem(hand)).getItem() == ModItems.TILE_BOX && stack.hasTagCompound()) {
					final NBTTagCompound nbt = stack.getTagCompound();
					if (nbt.hasKey("Block") && nbt.hasKey("TileData") && nbt.hasKey("Properties") && nbt.hasKey("Meta")) {
						ItemStack blockstack = new ItemStack(nbt.getCompoundTag("Block"));
						Block block = null;
						if (!(blockstack.getItem() instanceof ItemBlock)) return EnumActionResult.PASS;

						block = ((ItemBlock) blockstack.getItem()).getBlock();
						
						// Try to get the block states
						IBlockState state = block.getDefaultState();
						final NBTTagList properties = (NBTTagList) nbt.getTag("Properties");
						for (final NBTBase prop : properties) {
							final NBTTagCompound tag = (NBTTagCompound) prop;
							if (!(prop instanceof NBTTagCompound) && tag.hasKey("Name") && tag.hasKey("Value")) continue;
							for (final IProperty<?> p : state.getPropertyKeys()) {
								if (!p.getName().equals(tag.getString("Name"))) continue;
								final Optional<?> newValue = p.parseValue(tag.getString("Value"));
								if (newValue.isPresent()) {
									state = prop(state, p, newValue.get());
								}
							}
						}

						// Try to get Tile Data
						final TileEntity te = TileEntity.create(world, nbt.getCompoundTag("TileData"));

						// Trying to place the block
						world.setBlockState(offset, state, 3);
						world.setTileEntity(offset, te);

						// Decrease the stack if the player not in creative
						if (!player.isCreative()) stack.shrink(1);

						return EnumActionResult.FAIL;
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		return EnumActionResult.FAIL;
	}
	
	@Override
	public void registerModel() {
		
	}

}
