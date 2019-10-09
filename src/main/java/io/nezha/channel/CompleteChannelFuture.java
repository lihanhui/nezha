/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.nezha.channel;

import io.nezha.concurrent.CompleteFuture;
import io.nezha.concurrent.EventExecutor;
import io.nezha.concurrent.Future;
import io.nezha.concurrent.GenericFutureListener;

/**
 * A skeletal {@link ChannelFuture} implementation which represents a
 * {@link ChannelFuture} which has been completed already.
 */
abstract class CompleteChannelFuture extends CompleteFuture<Object> implements ChannelFuture {

    private final IChannel channel;

    /**
     * Creates a new instance.
     *
     * @param channel the {@link IChannel} associated with this future
     */
    protected CompleteChannelFuture(IChannel channel, EventExecutor executor) {
        super(executor);
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
    }

    @Override
    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return channel().eventLoop();
        } else {
            return e;
        }
    }

    @Override
    public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Object>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
        super.addListeners(listeners);
        return this;
    }

    @Override
    public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Object>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }

    @Override
    public ChannelFuture syncUninterruptibly() {
        return this;
    }

    @Override
    public ChannelFuture sync() throws InterruptedException {
        return this;
    }

    @Override
    public ChannelFuture await() throws InterruptedException {
        return this;
    }

    @Override
    public ChannelFuture awaitUninterruptibly() {
        return this;
    }

    @Override
    public IChannel channel() {
        return channel;
    }

    @Override
    public Void getNow() {
        return null;
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
