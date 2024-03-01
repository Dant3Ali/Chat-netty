package org.chat.netty.project.Callbacks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientCallBackHandler extends SimpleChannelInboundHandler<String> {
    private Callback _onMessageReceivedCallback;

    public ClientCallBackHandler(Callback onMessageReceivedCallback){
        _onMessageReceivedCallback = onMessageReceivedCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if (_onMessageReceivedCallback != null){
            _onMessageReceivedCallback.callback(s);
        }
    }
}
