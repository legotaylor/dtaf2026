/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui.screen;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
	private final Screen parent;

	public ConfigScreen(Screen parent) {
		super(Text.empty());
		this.parent = parent;
	}

	public void close() {
		ClientData.getMinecraft().setScreen(parent);
	}
}
