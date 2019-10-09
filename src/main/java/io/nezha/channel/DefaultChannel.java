package io.nezha.channel;

import java.net.SocketAddress;

public class DefaultChannel extends AbstractChannel {

	public DefaultChannel() {
		super(null);

	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ChannelMetadata metadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWritable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected AbstractUnsafe newUnsafe() {
		// TODO Auto-generated method stub
		return new DefaultUnsafe();
	}

	@Override
	protected boolean isCompatible(EventLoop loop) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected SocketAddress remoteAddress0() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doDisconnect() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doClose() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doBeginRead() throws Exception {
		// TODO Auto-generated method stub

	}
	public class DefaultUnsafe extends AbstractUnsafe{
		
	}
}
