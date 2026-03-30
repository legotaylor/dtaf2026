package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.TerrorlandsPortalFrameBlock;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TerrorlandsCompassItem extends Item {
	public TerrorlandsCompassItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
		super.inventoryTick(stack, world, entity, slot);
		if (world instanceof ServerWorld serverWorld) {
			if (serverWorld.getRegistryKey() == World.OVERWORLD && stack.get(DataComponentTypes.LODESTONE_TRACKER) == null) {
				stack.set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(getLocation(serverWorld, entity.getBlockPos()), false));
			}
		}
	}

	private Optional<GlobalPos> getLocation(ServerWorld serverWorld, BlockPos currentPos) {
		BlockPos pos = serverWorld.locateStructure(TagRegistry.WorldGen.Structure.terrorlandsPortal, currentPos, 100, false);
		return pos != null ? Optional.of(GlobalPos.create(serverWorld.getRegistryKey(), pos)) : Optional.empty();
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (world instanceof ServerWorld serverWorld) {
			if (serverWorld.getRegistryKey() == World.OVERWORLD) {
				ItemStack stack = hand.equals(Hand.MAIN_HAND) ? user.getEquippedStack(EquipmentSlot.MAINHAND) : user.getEquippedStack(EquipmentSlot.OFFHAND);
				Optional<GlobalPos> pos = getLocation(serverWorld, user.getBlockPos());
				stack.set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(pos, false));
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}

	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(BlockRegistry.terrorlandsPortalFrame) && !(Boolean) blockState.get(TerrorlandsPortalFrameBlock.active)) {
			if (!world.isClient) {
				BlockState blockState2 = blockState.with(TerrorlandsPortalFrameBlock.active, true);
				Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, blockPos);
				world.setBlockState(blockPos, blockState2, 2);
				world.updateComparators(blockPos, BlockRegistry.terrorlandsPortalFrame);
				context.getStack().decrement(1);
				world.syncWorldEvent(1503, blockPos, 0); //todo; custom sound?
				BlockPattern.Result result = TerrorlandsPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
				if (result != null) {
					BlockPos blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							BlockPos blockPos3 = blockPos2.add(i, 0, j);
							world.breakBlock(blockPos3, true, null);
							world.setBlockState(blockPos3, BlockRegistry.terrorlandsPortal.getDefaultState(), 2);
						}
					}
					world.syncGlobalEvent(1038, blockPos2.add(1, 0, 1), 0); //todo; custom sound?
				}
			}
			return ActionResult.SUCCESS;
		}
		return super.useOnBlock(context);
	}
}
