package com.example.homework_chatsentfile.server;

import com.example.homework_chatsentfile.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractMessage abstractMessage) throws Exception {
        channelHandlerContext.writeAndFlush(abstractMessage);
    }
}
