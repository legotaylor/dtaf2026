/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.data;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Data {
	private static final String modId;
	private static final Logger logger;

	public static Identifier idOf(String path) {
		return Identifier.of(getModId(), path);
	}

	public static Identifier getSomniumRealeId() {
		return idOf("somnium_reale");
	}

	public static Identifier getSomniaMetusId() {
		return idOf("the_terrorlands");
	}

	public static String getModId() {
		return modId;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static MutableText getText(String key, Object... args) {
		return getText(Data.getModId(), key, args);
	}

	public static MutableText getText(String namespace, String key, Object... args) {
		return getTranslatableText(namespace + "." + key, args);
	}

	private static MutableText getTranslatableText(String key, Object... args) {
		return Text.translatable(key, args);
	}

	static {
		modId = "dtaf2026";
		logger = LoggerFactory.getLogger(getModId());
	}
}
