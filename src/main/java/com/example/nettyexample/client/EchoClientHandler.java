package com.example.nettyexample.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 소켓 채널이 최초 활성화 되었을 때
        String sendMessage = "Hello, Netty!";

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append("]");

        System.out.println(builder.toString());

        // write == 채널에 데이터를 기록
        // flush == 채널에 기록된 데이터를 서버로 전송
        ctx.writeAndFlush(messageBuffer);
    }

    // 서버로부터 수신된 데이터가 있을 경우 호출
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset()); // 서버로부터 수신된 데이터가 저장된 msg 객체에서 문자열 데이터를 추출

        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    // 수신된 데이터를 모두 읽었을 때 호출
    // channelRead 수행이 완료되면 자동으로 호출
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close(); // 수신된 데이터를 모두 읽은 후 서버와 연결된 채널을 닫는다.
        // 이후 데이터 송수신 채널은 닫히게 되고 클라이언트 프로그램은 종료된다.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
