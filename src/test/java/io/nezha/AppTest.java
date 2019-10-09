package io.nezha;

import io.nezha.channel.DefaultChannel;
import io.nezha.channel.DefaultEventLoopGroup;
import io.nezha.channel.EventLoopGroup;
import io.nezha.channel.IChannel;

public class AppTest {
	public static void main(String[] args) {
		EventLoopGroup group = new DefaultEventLoopGroup();
		IChannel channel = new DefaultChannel();
		group.register(channel);
	}
}
