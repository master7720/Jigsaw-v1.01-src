package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatSpam extends Module {

	public ChatSpam() {
		super("ChatSpam", Keyboard.KEY_NONE, Category.MISC, "Spams the chat.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		mc.player.connection.sendPacket(new CPacketChatMessage("Spam"));

		super.onUpdate();
	}

}
