package org.chat.netty.project.Handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class BaseHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client have connected" + ctx);
        channels.add(ctx.channel());
        clientName = "Client#" + newClientIndex;
        newClientIndex++;
        sendMessage("SERVER", "New client was added: " + clientName);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Msg have received: " + s);
        if (s.startsWith("/"))
        {
            if (s.startsWith("/changename "))
            {
                String newNickName = s.split("\\s", 2)[1];
                sendMessage("SERVER", clientName +" have changed nickname to: " + newNickName);
                clientName = newNickName;
            }

            return;
        }

        sendMessage(clientName, s);
    }

    public void sendMessage(String clientName, String s){
        String out = String.format("[%s]: %s\n", clientName, s);
        for (Channel c : channels){
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Client " + clientName + " have disconnected");
        channels.remove(ctx.channel());
        sendMessage("SERVER", "Client leave" + clientName);
        ctx.close();
    }
}
