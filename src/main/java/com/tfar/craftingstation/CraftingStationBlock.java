package com.tfar.craftingstation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

import static net.minecraft.inventory.InventoryHelper.spawnItemStack;

public class CraftingStationBlock extends Block {


  public CraftingStationBlock(Material materialIn) {
    super(materialIn);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (world.isRemote) return true;
    player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
    player.openGui(CraftingStation.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
    return true;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new CraftingStationTile();
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof CraftingStationTile) {
        dropItems((CraftingStationTile)tileentity,worldIn, pos);
        worldIn.updateComparatorOutputLevel(pos, this);
      }
      super.breakBlock(worldIn, pos, state);
    }

  public static void dropItems(CraftingStationTile table, World world, BlockPos pos) {
    IntStream.range(0, table.input.getSlots()).mapToObj(i -> table.input.getStackInSlot(i)).filter(stack -> !stack.isEmpty()).forEach(stack -> spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack));
  }
}

