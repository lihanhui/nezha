package io.nezha.schedule;

import io.nezha.channel.ChannelFuture;
import io.nezha.event.IAsyncEvent;
import io.nezha.event.IEvent;

public interface IScheduler {
    ChannelFuture submitEvent(String key, IEvent event);
    ChannelFuture submitAsyncEvent(String key, IAsyncEvent event);
}
