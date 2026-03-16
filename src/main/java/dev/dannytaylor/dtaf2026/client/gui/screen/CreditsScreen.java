/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui.screen;

import com.google.common.collect.Lists;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import dev.dannytaylor.dtaf2026.common.data.Data;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.MusicType;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CreditsScreen extends Screen {
	private static final Identifier VIGNETTE_TEXTURE = Identifier.ofVanilla("textures/misc/credits_vignette.png");
	private static final String OBFUSCATION_PLACEHOLDER;
	private static final float SPACE_BAR_SPEED_MULTIPLIER = 5.0F;
	private static final float CTRL_KEY_SPEED_MULTIPLIER = 15.0F;

	static {
		OBFUSCATION_PLACEHOLDER = String.valueOf(Formatting.WHITE) + Formatting.OBFUSCATED + Formatting.GREEN + Formatting.AQUA;
	}

	private final Identifier textLocation;
	private final Runnable finishAction;
	private final IntSet pressedCtrlKeys = new IntOpenHashSet();
	private final float baseSpeed;
	private final LogoDrawer logoDrawer = new LogoDrawer(false);
	private final String playerTime;
	public float alpha;
	private float time;
	private List<OrderedText> credits;
	private List<Text> narratedCredits;
	private int creditsHeight;
	private boolean spaceKeyPressed;
	private float speed;
	private int speedMultiplier;

	public CreditsScreen(String name, Runnable finishAction, String playerTime) {
		this(Data.idOf("texts/" + name + ".txt"), finishAction, playerTime);
	}

	public CreditsScreen(Identifier textLocation, Runnable finishAction, String playerTime) {
		super(NarratorManager.EMPTY);
		this.textLocation = textLocation;
		this.finishAction = finishAction;
		this.baseSpeed = 0.5F;
		this.speedMultiplier = 1;
		this.speed = this.baseSpeed;
		this.playerTime = playerTime;
	}

	private float getSpeed() {
		return this.spaceKeyPressed ? this.baseSpeed * (SPACE_BAR_SPEED_MULTIPLIER + (float) this.pressedCtrlKeys.size() * CTRL_KEY_SPEED_MULTIPLIER) * (float) this.speedMultiplier : this.baseSpeed * (float) this.speedMultiplier;
	}

	public void tick() {
		if (this.time < 12) this.alpha = Math.clamp(this.alpha + 9, 0, 100);
		else if (this.time > this.creditsHeight + this.height + 12) this.alpha = Math.clamp(this.alpha - 9, 0, 100);
		if (this.client != null) {
			this.client.getMusicTracker().tick();
			this.client.getSoundManager().tick(false);
		}
		float f = (float) (this.creditsHeight + this.height + 24);
		if (this.time > f) this.closeScreen();
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 265) this.speedMultiplier = -1;
		else if (keyCode != 341 && keyCode != 345) {
			if (keyCode == 32) this.spaceKeyPressed = true;
		} else this.pressedCtrlKeys.add(keyCode);
		this.speed = this.getSpeed();
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 265) this.speedMultiplier = 1;
		if (keyCode == 32) this.spaceKeyPressed = false;
		else if (keyCode == 341 || keyCode == 345) this.pressedCtrlKeys.remove(keyCode);
		this.speed = this.getSpeed();
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	public void close() {
		this.closeScreen();
	}

	private void closeScreen() {
		this.finishAction.run();
	}

	protected void init() {
		if (this.credits == null) {
			this.credits = Lists.newArrayList();
			this.narratedCredits = Lists.newArrayList();
			this.load(this.textLocation, this::readTxt);
			this.creditsHeight = this.credits.size() * 12;
		}
	}

	public Text getNarratedTitle() {
		return ScreenTexts.joinSentences(this.narratedCredits.toArray(Text[]::new));
	}

	private void load(Identifier fileLocation, CreditsReader reader) {
		if (this.client != null) {
			try (Reader reader2 = this.client.getResourceManager().openAsReader(fileLocation)) {
				reader.read(reader2);
			} catch (Exception exception) {
				Data.getLogger().error("Couldn't load credits from file '{}': {}", fileLocation, exception);
			}
		}
	}

	private void readTxt(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		Random random = Random.create(8124371L);

		String string;
		while ((string = bufferedReader.readLine()) != null) {
			int i;
			String string2;
			String string3;
			for (string = string.replaceAll("MODNAME", ClientData.getText("name").getString()).replaceAll("PLAYERTIME", this.playerTime).replaceAll("PLAYERNAME", this.client != null ? this.client.getSession().getUsername() : "Steve"); (i = string.indexOf(OBFUSCATION_PLACEHOLDER)) != -1; string = string2 + Formatting.WHITE + Formatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + string3) {
				string2 = string.substring(0, i);
				string3 = string.substring(i + OBFUSCATION_PLACEHOLDER.length());
			}

			this.addText(string);
			if (string.isBlank()) this.addEmptyLine();
		}

		for (int i = 0; i < 8; ++i) {
			this.addEmptyLine();
		}

	}

	private void addEmptyLine() {
		this.credits.add(OrderedText.EMPTY);
		this.narratedCredits.add(ScreenTexts.EMPTY);
	}

	private void addText(String text) {
		if (this.client != null) {
			Text text2 = Text.literal(text);
			this.credits.addAll(this.client.textRenderer.wrapLines(text2, 256));
			this.narratedCredits.add(text2);
		}
	}

	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		this.renderVignette(context);
		this.time = Math.max(0.0F, this.time + deltaTicks * this.speed);
		int i = this.width / 2 - 128;
		int j = this.height + 12;
		float f = -this.time;
		context.getMatrices().pushMatrix();
		context.getMatrices().translate(0.0F, f);
		context.createNewRootLayer();
		this.logoDrawer.draw(context, this.width, 1.0F, j);
		int k = j + 100;

		for (int l = 0; l < this.credits.size(); ++l) {
			if (l == this.credits.size() - 1) {
				float g = (float) k + f - (float) (this.height / 2 - 6);
				if (g < 0.0F) {
					context.getMatrices().translate(0.0F, -g);
				}
			}

			if ((float) k + f + 12.0F + 8.0F > 0.0F && (float) k + f < (float) this.height) {
				OrderedText orderedText = this.credits.get(l);
				context.drawCenteredTextWithShadow(this.textRenderer, orderedText, i + 128, k, -1);
			}

			k += 12;
		}

		context.getMatrices().popMatrix();
	}

	private void renderVignette(DrawContext context) {
		context.drawTexture(RenderPipelines.VIGNETTE, VIGNETTE_TEXTURE, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
	}

	public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		ScreenHelper.renderSleepPortalOverlay(context, this.alpha);
	}

	protected void renderDarkening(DrawContext context, int x, int y, int width, int height) {
		float f = this.time * 0.5F;
		Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0F, f, width, height);
	}

	public void removed() {
		if (this.client != null) this.client.getMusicTracker().stop(MusicType.CREDITS);
	}

	public MusicSound getMusic() {
		return MusicType.CREDITS;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@FunctionalInterface
	@Environment(EnvType.CLIENT)
	interface CreditsReader {
		void read(Reader reader) throws IOException;
	}
}
