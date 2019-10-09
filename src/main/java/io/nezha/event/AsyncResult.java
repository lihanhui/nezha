package io.nezha.event;

import io.nezha.channel.ChannelPromise;

public class AsyncResult {
	private ChannelPromise promise;

	public AsyncResult(ChannelPromise promise) {
		super();
		this.promise = promise;
	}
	public void setResult(Result<?> result){
		promise.setSuccess(result);
	}
}
