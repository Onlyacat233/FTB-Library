package com.feed_the_beast.mods.ftbguilibrary.widget;

import com.feed_the_beast.mods.ftbguilibrary.icon.Icon;
import com.feed_the_beast.mods.ftbguilibrary.utils.MouseButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

/**
 * @author LatvianModder
 */
public class SimpleButton extends Button {
    public interface Callback {
        void onClicked(SimpleButton widget, MouseButton button);
    }

    private final Callback consumer;

    public SimpleButton(Panel panel, Component text, Icon icon, Callback c) {
        super(panel, text, icon);
        consumer = c;
    }

    @Override
    public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
    }

    @Override
    public void onClicked(MouseButton button) {
        playClickSound();
        consumer.onClicked(this, button);
    }
}