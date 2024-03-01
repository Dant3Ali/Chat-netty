package org.chat.netty.project;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.chat.netty.project.Callbacks.Callback;
import org.chat.netty.project.Callbacks.ClientCallBackHandler;

public class Network {
    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    private SocketChannel _socketChannel;
    private EventLoopGroup workerGroup;

    public Network(Callback onMessageReceivedCallback) {
        Thread t = new Thread(() ->{
            workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                _socketChannel = socketChannel;
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ClientCallBackHandler(onMessageReceivedCallback));
                            }
                        });

                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                workerGroup.shutdownGracefully();
            }

        });
        t.setDaemon(true);
        t.start();
    }

    public void sendMessage(String string){
        _socketChannel.writeAndFlush(string);
    }

    public void close(){
        try {
            _socketChannel.close().sync();
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
