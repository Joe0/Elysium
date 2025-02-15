package org.moparscape.elysium.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.moparscape.elysium.entity.Player;
import org.moparscape.elysium.entity.component.Combat;
import org.moparscape.elysium.entity.component.Communication;
import org.moparscape.elysium.entity.component.Skills;
import org.moparscape.elysium.external.Shop;
import org.moparscape.elysium.util.Formulae;
import org.moparscape.elysium.world.Point;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class Packets {

    public static ChannelFuture sendScreenshot(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(181);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendCombatStyle(Player player) {
        Combat combat = player.getComponent(Combat.class);
        PacketBuilder pb = new PacketBuilder(1, true);
        pb.setId(129);
        pb.writeByte(combat.getCombatStyle()); // Combat style
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendFatigue(Player player) {
        Skills skills = player.getComponent(Skills.class);
        PacketBuilder pb = new PacketBuilder(2, true);
        pb.setId(126);
        pb.writeShort(skills.getFatigue());
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture hideMenu(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(127);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendMenu(Player player, String[] options) {
        PacketBuilder pb = new PacketBuilder();
        pb.setId(223);
        pb.writeByte(options.length);
        for (String s : options) {
            pb.writeByte(s.length());
            pb.writeBytes(s.getBytes());
        }
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture showBank(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture hideBank(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(171);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture updateBankItem(Player player, int slot, int newId, int amount) {
        PacketBuilder pb = new PacketBuilder(7, true);
        pb.setId(139);
        pb.writeByte(slot);
        pb.writeShort(newId);
        pb.writeInt(amount);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture showShop(Player player, Shop shop) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture hideShop(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(220);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture startShutdown(Player player, int seconds) {
        PacketBuilder pb = new PacketBuilder(2, true);
        pb.setId(172);
        pb.writeShort((int) (((double) seconds / 32D) * 50));
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendAlert(Player player, String message, boolean big) {
        PacketBuilder pb = new PacketBuilder(message.length(), true);
        pb.setId(big ? 64 : 148);
        pb.writeBytes(message.getBytes());
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendSound(Player player, String soundName) {
        PacketBuilder pb = new PacketBuilder(soundName.length(), true);
        pb.setId(11);
        pb.writeBytes(soundName.getBytes());
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendDied(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(11);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendPrivateMessage(Player player, long usernameHash, byte[] message) {
        PacketBuilder pb = new PacketBuilder(8 + message.length, true);
        pb.setId(170);
        pb.writeLong(usernameHash);
        pb.writeBytes(message);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendFriendUpdate(Player player, long usernameHash, int world) {
        PacketBuilder pb = new PacketBuilder(9, true);
        pb.setId(25);
        pb.writeLong(usernameHash);
        pb.writeByte(99); // World number
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendFriendList(Player player) {
        Communication com = player.getComponent(Communication.class);
        List<Long> friendList = com.getFriendList();
        int size = friendList.size();

        PacketBuilder pb = new PacketBuilder(1 + (size * 9), true);
        pb.setId(249);
        pb.writeByte(size);
        for (long hash : friendList) {
            pb.writeLong(hash);
            pb.writeByte(99); // The world they're on
        }
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendIgnoreList(Player player) {
        Communication com = player.getComponent(Communication.class);
        List<Long> ignoreList = com.getIgnoreList();
        int size = ignoreList.size();

        PacketBuilder pb = new PacketBuilder(1 + (size * 8), true);
        pb.writeByte(size);
        for (long hash : ignoreList) {
            pb.writeLong(hash);
        }
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendTradeAccept(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendDuelAccept(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendTradeAcceptUpdate(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendDuelAcceptUpdate(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendDuelSettingUpdate(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendTradeItems(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendDuelItems(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendTradeWindowOpen(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendDuelWindowOpen(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendTradeWindowClose(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(187);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendDuelWindowClose(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(160);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendAppearanceScreen(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(207);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendServerInfo(Player player) {
        String location = "Australia";
        PacketBuilder pb = new PacketBuilder(8 + location.length(), true);
        pb.setId(110);
        pb.writeLong(System.currentTimeMillis()); // Start time
        pb.writeBytes(location.getBytes());
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendTeleBubble(Player player, int x, int y, boolean grab) {
        Point loc = player.getLocation();
        PacketBuilder pb = new PacketBuilder(3, true);
        pb.setId(23);
        pb.writeByte(grab ? 1 : 0);
        pb.writeByte(x - loc.getX());
        pb.writeByte(y - loc.getY());
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendMessage(Player player, String message) {
        byte[] msg = message.getBytes();
        PacketBuilder pb = new PacketBuilder(msg.length, true);
        pb.setId(48);
        pb.writeBytes(msg);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendRemoveItem(Player player, int slot) {
        PacketBuilder pb = new PacketBuilder(1, true);
        pb.setId(191);
        pb.writeByte(slot);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendUpdateItem(Player player, int slot) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendInventory(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendEquipmentStats(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendStat(Player player, int stat) {
        Skills skills = player.getComponent(Skills.class);
        PacketBuilder pb = new PacketBuilder(7, true);
        pb.setId(208);
        pb.writeByte(stat);
        pb.writeByte(skills.getCurStat(stat));
        pb.writeByte(skills.getMaxStat(stat));
        pb.writeInt(skills.getExp(stat));
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendStats(Player player) {
        Skills skills = player.getComponent(Skills.class);
        PacketBuilder pb = new PacketBuilder(108, true);
        pb.setId(180);

        for (int lvl : skills.getCurStats()) {
            pb.writeByte(lvl);
        }
        for (int lvl : skills.getMaxStats()) {
            pb.writeByte(lvl);
        }
        for (int exp : skills.getExps()) {
            pb.writeInt(exp);
        }

        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendWorldInfo(Player player) {
        PacketBuilder pb = new PacketBuilder(10, true);
        pb.setId(131);
        pb.writeShort(player.getIndex());
        pb.writeShort(2304);
        pb.writeShort(1776);
        pb.writeShort(Formulae.getHeight(player.getLocation()));
        pb.writeShort(944);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendPrayers(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendGameSettings(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendPrivacySettings(Player player) {
        throw new UnsupportedOperationException();
    }

    public static ChannelFuture sendLogout(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(222);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendCantLogout(Player player) {
        PacketBuilder pb = new PacketBuilder(0, true);
        pb.setId(136);
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendLoginBox(Player player) {
        PacketBuilder pb = new PacketBuilder(20, true);
        pb.setId(248);
        pb.writeShort(0); // Days since last login
        pb.writeShort(0); // Subscription time left
        pb.writeBytes("127.0.0.1".getBytes()); // Ip address
        return player.getSession().write(pb.toPacket());
    }

    public static ChannelFuture sendLoginResponse(Player player, LoginResponse response) {
        ChannelBuffer buffer = ChannelBuffers.buffer(1);
        buffer.writeByte(response.getResponseCode());
        return player.getSession().write(buffer);
    }

    public static enum LoginResponse {

        UNKNOWN(22),
        SERVER_FULL(10),
        FAILED_PROFILE_DECODE(7),
        CLIENT_UPDATED(4),
        ACCOUNT_DISABLED(6),
        SESSION_REJECTED(5),
        USERNAME_IN_USE(3),
        INVALID_USER_PASS(2),
        SUCCESS(0);

        private final int response;

        LoginResponse(int response) {
            this.response = response;
        }

        public int getResponseCode() {
            return response;
        }
    }
}
