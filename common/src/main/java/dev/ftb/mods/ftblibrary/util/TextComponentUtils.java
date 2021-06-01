package dev.ftb.mods.ftblibrary.util;

import dev.ftb.mods.ftblibrary.icon.Icon;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TextComponentUtils {
	public interface CustomComponentParser {
		Component parse(String string, Map<String, String> properties);
	}

	@Environment(EnvType.CLIENT)
	private static final Function<String, Component> DEFAULT_STRING_TO_COMPONENT = TextComponentUtils::defaultStringToComponent;

	private static final List<CustomComponentParser> CUSTOM_COMPONENT_PARSERS = new ArrayList<>();

	public static void addCustomParser(CustomComponentParser function) {
		CUSTOM_COMPONENT_PARSERS.add(function);
	}

	@Environment(EnvType.CLIENT)
	public static Component parse(String s) {
		return TextComponentParser.parse(s, DEFAULT_STRING_TO_COMPONENT);
	}

	@Environment(EnvType.CLIENT)
	private static Component defaultStringToComponent(String s) {
		if (s.isEmpty()) {
			return TextComponent.EMPTY;
		}

		if (s.indexOf(':') != -1) {
			Map<String, String> map = StringUtils.splitProperties(s);

			for (CustomComponentParser parser : CUSTOM_COMPONENT_PARSERS) {
				Component c = parser.parse(s, map);

				if (c != null && c != TextComponent.EMPTY) {
					return c;
				}
			}

			if (map.containsKey("image")) {
				ImageComponent c = new ImageComponent();
				c.image = Icon.getIcon(map.get("image"));
				c.width = Integer.parseInt(map.getOrDefault("width", "100"));
				c.height = Integer.parseInt(map.getOrDefault("height", "100"));

				if (map.containsKey("text")) {
					c.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, parse(map.get("text")))));
				}

				return c;
			} else if (map.containsKey("open_url")) {
				return parse(map.get("text")).copy().withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, map.get("open_url"))));
			}
		}

		return parse(I18n.get(s));
	}

	@ExpectPlatform
	public static Component withLinks(String message) {
		throw new AssertionError();
	}
}