/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer;

import com.google.common.collect.Sets;
import dev.dannytaylor.dtaf2026.common.registry.advancement.CriteriaRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.SomniumRealeServerPlayerEntity;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.networking.NetworkingRegistry;
import dev.dannytaylor.dtaf2026.common.registry.stats.StatRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class FleeciferFight {
	private final ServerWorld world;
	private final ServerBossBar bossBar;
	public boolean completed;
	private Predicate<Entity> showBossBarPredicate;
	private int updateTicks;
	private UUID uuid;
	private FleeciferBossEntity cachedEntity;
	private boolean finished;
	private int finishedTicks;

	public FleeciferFight(ServerWorld world) {
		this(world, BlockPos.ORIGIN, null);
	}

	public FleeciferFight(ServerWorld world, BlockPos origin) {
		this(world, origin, null);
	}

	public FleeciferFight(ServerWorld world, UUID uuid) {
		this(world, BlockPos.ORIGIN, uuid);
	}

	public FleeciferFight(ServerWorld world, BlockPos origin, UUID uuid) {
		this.world = world;
		this.showBossBarPredicate = EntityPredicates.VALID_ENTITY.and(EntityPredicates.maxDistance(origin.getX(), origin.getY(), origin.getZ(), 192.0F));
		this.bossBar = (ServerBossBar) (new ServerBossBar(Text.translatable("entity.dtaf2026.fleecifer_boss"), BossBar.Color.RED, BossBar.Style.PROGRESS)).setDragonMusic(true).setThickenFog(false);
		this.updateTicks = 21;
		this.uuid = uuid;
	}

	@Nullable
	public static FleeciferBossEntity createFleecifer(ServerWorld world, BlockPos blockPos) {
		for (int y = blockPos.getY(); y > world.getBottomY(); y--) {
			if (!world.getBlockState(blockPos.withY(y)).isAir()) {
				blockPos = blockPos.withY(y + 1);
				break;
			}
		}
		world.getWorldChunk(blockPos);
		FleeciferBossEntity fleeciferEntity = EntityRegistry.fleeciferBoss.create(world, SpawnReason.EVENT);
		if (fleeciferEntity != null) {
			fleeciferEntity.refreshPositionAndAngles(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
			world.spawnEntity(fleeciferEntity);
		}
		return fleeciferEntity;
	}

	public static void increaseStat(FleeciferFight fight, Identifier stat) {
		for (ServerPlayerEntity serverPlayer : fight.bossBar.getPlayers()) {
			increaseStat(serverPlayer, stat);
		}
	}

	public static void increaseStat(ServerPlayerEntity player, Identifier stat) {
		player.increaseStat(stat, 1);
	}

	public static <F extends FleeciferEntity> void summonFleecifer(FleeciferBossEntity fleecifer, FleeciferFight fight, EntityType<F> mobEntityType, int amount) {
		for (int i = 0; i < amount; i++) {
			F entity = createEntity(fleecifer, fight, mobEntityType, i);
			if (entity != null) {
				entity.setPreventLoot(true);
				fight.world.spawnEntity(entity);
			}
		}
	}

	public static <E extends MobEntity> E createEntity(FleeciferBossEntity fleecifer, FleeciferFight fight, EntityType<E> mobEntityType, int i) {
		float f = fleecifer.getDimensions(fleecifer.getPose()).width();
		float g = f / 2.0F;
		E entity = mobEntityType.create(fight.world, SpawnReason.NATURAL);
		if (entity != null) {
			float h = ((float) (i % 2) - 0.5F) * g;
			float m = ((float) (i / 2) - 0.5F) * g;
			entity.refreshPositionAndAngles(fleecifer.getX() + (double) h, fleecifer.getY() + (double) 0.5F, fleecifer.getZ() + (double) m, fleecifer.getRandom().nextFloat() * 360.0F, 0.0F);
			entity.setPersistent();
			return entity;
		}
		return null;
	}

	public void tick() {
		if (this.updateTicks++ >= 20) {
			this.updatePlayers();
			if (this.world.getEntity(this.getUuid()) instanceof FleeciferBossEntity fleecifer) {
				this.cachedEntity = fleecifer;
				int phase = FleeciferBossEntity.getPhase(fleecifer);
				this.bossBar.setName(Text.translatable("entity.dtaf2026.fleecifer_boss" + (phase > 0 ? "." + phase : ""), Text.translatable("entity.dtaf2026.fleecifer_boss")));
				this.bossBar.setColor(Arrays.stream(BossBar.Color.values()).toList().get(Math.min(phase, BossBar.Color.values().length - 1)));
				this.bossBar.setVisible(true);
			} else {
				this.cachedEntity = null;
				this.bossBar.setVisible(false);
			}
			this.showBossBarPredicate = EntityPredicates.VALID_ENTITY.and(EntityPredicates.maxDistance(this.cachedEntity.getX(), this.cachedEntity.getY(), this.cachedEntity.getZ(), 192.0F));
			this.updateCriteria();
			this.updateTicks = 0;
		}
		if (this.finished && !this.completed) {
			if (this.finishedTicks < 100) {
				this.finishedTicks++;
			} else {
				for (ServerPlayerEntity player : this.bossBar.getPlayers()) {
					increaseStat(player, StatRegistry.fleeciferBossWon);
					player.giveOrDropStack(new ItemStack(ItemRegistry.relicBundleUpgradeSmithingTemplate, 1));
					SomniumRealeServerPlayerEntity somniumRealeServerPlayerEntity = ((SomniumRealeServerPlayerEntity) player);
					somniumRealeServerPlayerEntity.dtaf2026$setTimeRunning(false);
					NetworkingRegistry.sendCreditsScreen(player, somniumRealeServerPlayerEntity.dtaf2026$getTime());
				}
				this.completed = true;
			}
		}
	}

	private void updatePlayers() {
		Set<ServerPlayerEntity> set = Sets.newHashSet();
		for (ServerPlayerEntity player : this.world.getPlayers(this.showBossBarPredicate)) {
			this.bossBar.addPlayer(player);
			set.add(player);
		}

		Set<ServerPlayerEntity> set2 = Sets.newHashSet(this.bossBar.getPlayers());
		set2.removeAll(set);

		for (ServerPlayerEntity player2 : set2) {
			this.bossBar.removePlayer(player2);
		}
	}

	private void updateCriteria() {
		if (this.cachedEntity != null) {
			int phase = FleeciferBossEntity.getPhase(this.cachedEntity);
			for (ServerPlayerEntity player : this.bossBar.getPlayers())
				CriteriaRegistry.fleeciferFight.trigger(player, this.finished, phase);
		}
	}

	private void setFleecifer(FleeciferBossEntity fleecifer) {
		this.uuid = fleecifer.getUuid();
	}

	public void fleeciferKilled(FleeciferBossEntity fleecifer) {
		if (fleecifer.getUuid().equals(this.getUuid())) {
			this.bossBar.setPercent(0.0F);
			this.bossBar.setVisible(false);
			this.finished = true;
			this.updateCriteria();
		}
	}

	public void updateFight(FleeciferBossEntity fleecifer) {
		if (fleecifer.getUuid().equals(this.getUuid())) {
			this.bossBar.setPercent(fleecifer.getHealth() / fleecifer.getMaxHealth());
			validatePhases();
		}
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void validatePhases() {
		if (this.cachedEntity != null) {
			int nextPhase = FleeciferBossEntity.getPhase(cachedEntity) + 1;
			float health = cachedEntity.getHealth();
			for (Phase phase : Phase.values()) {
				if (nextPhase <= phase.id) {
					if (health <= phase.health) {
						phase.action.run(cachedEntity, this);
						increaseStat(this, StatRegistry.fleeciferBossPhases);
						FleeciferBossEntity.setPhase(cachedEntity, nextPhase);
						this.bossBar.setName(Text.translatable("entity.dtaf2026.fleecifer_boss." + nextPhase, Text.translatable("entity.dtaf2026.fleecifer_boss")));
						this.bossBar.setColor(Arrays.stream(BossBar.Color.values()).toList().get((nextPhase + 1) % (BossBar.Color.values().length - 1)));
					}
					break;
				}
			}
		}
	}

	public enum Phase {
		ONE(1, 1000, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(2, 4))),
		TWO(2, 900, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(3, 8))),
		THREE(3, 800, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(4, 12))),
		FOUR(4, 700, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(5, 16))),
		FIVE(5, 600, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(6, 20))),
		SIX(6, 500, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(7, 24))),
		SEVEN(7, 400, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(8, 28))),
		EIGHT(8, 300, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(9, 32))),
		NINE(9, 200, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(10, 36))),
		TEN(10, 100, (fleecifer, fight) -> FleeciferFight.summonFleecifer(fleecifer, fight, EntityRegistry.fleecifer, fight.world.random.nextBetween(11, 40)));

		public final int id;
		public final float health;
		public final PhaseAction action;

		Phase(int id, float health, PhaseAction action) {
			this.id = id;
			this.health = health;
			this.action = action;
		}
	}

	public interface PhaseAction {
		void run(FleeciferBossEntity fleecifer, FleeciferFight fight);
	}
}
