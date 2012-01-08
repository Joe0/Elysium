package org.moparscape.elysium.net.codec;

import org.jboss.netty.channel.*;
import org.moparscape.elysium.Server;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class ElysiumConnectionHandler extends SimpleChannelUpstreamHandler {

    private static final Server server = Server.getInstance();

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel chan = e.getChannel();
        System.out.println("Channel connected");
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        System.out.println("Channel disconnected");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

    }
}
