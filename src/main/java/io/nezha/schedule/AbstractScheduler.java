package io.nezha.schedule;

import java.util.Vector;

import io.nezha.channel.ChannelFuture;
import io.nezha.channel.DefaultChannel;
import io.nezha.channel.DefaultEventLoopGroup;
import io.nezha.channel.EventLoopGroup;
import io.nezha.channel.IChannel;
import io.nezha.event.EventHandler;
import io.nezha.event.IAsyncEvent;
import io.nezha.event.IEvent;

public abstract class AbstractScheduler implements IScheduler {
	private int threadCount;
	private int maxChannelCount;
	private Vector<IChannel> channelVector = new Vector<>();
	private EventLoopGroup group;
	
	public AbstractScheduler(int threadCount, int maxChannelCount) {
		this.threadCount = threadCount;
		this.maxChannelCount = maxChannelCount;
		this.group = new DefaultEventLoopGroup(this.threadCount);
		this.channelVector = new Vector<>(this.maxChannelCount);
		for(int i = 0; i < this.maxChannelCount; i++) {
			IChannel chan = new DefaultChannel();
			chan.pipeline().addFirst(new EventHandler());
			channelVector.add(i, chan);
		}
		for(IChannel channel: channelVector) {
			group.register(channel);
		}
	}
	@Override
	public ChannelFuture submitEvent(String key, IEvent event) {
		int hashCode = key.hashCode();
                if(hashCode == Integer.MIN_VALUE) hashCode = hashCode + this.maxChannelCount;
		int index = Math.abs(hashCode) % this.maxChannelCount;
		IChannel channel = channelVector.get(index);
		return channel.write(event);
	}
	
	@Override
	public ChannelFuture submitAsyncEvent(String key, IAsyncEvent event) {
		int hashCode = key.hashCode();
                if(hashCode == Integer.MIN_VALUE) hashCode = hashCode + this.maxChannelCount;
		int index = Math.abs(hashCode) % this.maxChannelCount;
		IChannel channel = channelVector.get(index);
		return channel.write(event);
	}
	

}
