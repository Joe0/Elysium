package org.moparscape.elysium.net.handler;

import org.moparscape.elysium.entity.Player;
import org.moparscape.elysium.entity.component.State;
import org.moparscape.elysium.entity.component.service.StateInterpretationService;
import org.moparscape.elysium.net.Packets;
import org.moparscape.elysium.net.Session;
import org.moparscape.elysium.net.codec.decoder.message.LogoutRequestMessage;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author lothy
 */
public final class LogoutRequestMessageHandler extends
		MessageHandler<LogoutRequestMessage> {
	@Override
	public void handle(Session session, Player player,
			LogoutRequestMessage message) {
		if (StateInterpretationService.canLogout(player.getComponent(State.class))) {
			Packets.sendLogout(player);
		} else {
			Packets.sendCantLogout(player);
		}
	}
}
