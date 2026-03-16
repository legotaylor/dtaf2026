/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.SomniumRealeServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements SomniumRealeServerPlayerEntity {
	@Unique
	private boolean seenModCredits = false;
	@Unique
	private long time = 0L;
	@Unique
	private long lastTimeTick = 0L;
	@Unique
	private boolean timeFinished = false;

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "readCustomData", at = @At("RETURN"))
	private void dtaf2026$readCustomData(ReadView view, CallbackInfo ci) {
		this.seenModCredits = view.getBoolean(Data.getModId() + ".seenCredits", false);
		this.time = view.getLong(Data.getModId() + ".time", 0L);
		this.lastTimeTick = view.getLong(Data.getModId() + ".last_time_tick", 0L);
		this.timeFinished = view.getBoolean(Data.getModId() + ".time_finished", false);
	}

	@Inject(method = "writeCustomData", at = @At("RETURN"))
	private void dtaf2026$writeCustomData(WriteView view, CallbackInfo ci) {
		view.putBoolean(Data.getModId() + ".seenCredits", this.seenModCredits);
		view.putLong(Data.getModId() + ".time", this.time);
		view.putLong(Data.getModId() + ".last_time_tick", this.lastTimeTick);
		view.putBoolean(Data.getModId() + ".time_finished", this.timeFinished);
	}

	@Inject(method = "copyFrom", at = @At("RETURN"))
	private void dtaf2026$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		this.seenModCredits = ((SomniumRealeServerPlayerEntity) oldPlayer).dtaf2026$getSeenModCredits();
		this.time = ((SomniumRealeServerPlayerEntity) oldPlayer).dtaf2026$getTime();
		this.lastTimeTick = ((SomniumRealeServerPlayerEntity) oldPlayer).dtaf2026$getLastTimeTick();
		this.timeFinished = ((SomniumRealeServerPlayerEntity) oldPlayer).dtaf2026$getTimeRunning();
	}

	@Override
	public boolean dtaf2026$getSeenModCredits() {
		return this.seenModCredits;
	}

	@Override
	public void dtaf2026$setSeenModCredits(boolean value) {
		this.seenModCredits = value;
	}

	@Override
	public long dtaf2026$getTime() {
		return this.time;
	}

	@Override
	public void dtaf2026$setTime(long value) {
		this.time = value;
	}

	@Override
	public long dtaf2026$getLastTimeTick() {
		return this.lastTimeTick;
	}

	@Override
	public void dtaf2026$setLastTimeTick(long value) {
		this.lastTimeTick = value;
	}

	@Override
	public boolean dtaf2026$getTimeRunning() {
		return !this.timeFinished;
	}

	@Override
	public void dtaf2026$setTimeRunning(boolean value) {
		this.timeFinished = !value;
	}
}
