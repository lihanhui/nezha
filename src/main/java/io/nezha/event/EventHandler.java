package io.nezha.event;

import io.nezha.channel.*;
public class EventHandler implements ChannelOutboundHandler{

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof IEvent) {
			IEvent event = (IEvent)msg;
			Result<?> result = event.handleEvent();
			promise.setSuccess(result);
		}else if(msg instanceof IAsyncEvent){
			IAsyncEvent event = (IAsyncEvent)msg;
			AsyncResult result = new AsyncResult(promise);
			event.handleEvent(result);
		}else {
			ctx.write(msg, promise);
		}
	}

}
