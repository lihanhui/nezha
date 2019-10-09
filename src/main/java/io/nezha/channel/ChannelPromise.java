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

import io.nezha.concurrent.Future;
import io.nezha.concurrent.GenericFutureListener;
import io.nezha.concurrent.Promise;

/**
 * Special {@link ChannelFuture} which is writable.
 */
public interface ChannelPromise extends ChannelFuture, Promise<Object> {

    @Override
    IChannel channel();

    @Override
    ChannelPromise setSuccess(Object result);

    ChannelPromise setSuccess();

    boolean trySuccess();

    @Override
    ChannelPromise setFailure(Throwable cause);

    @Override
    ChannelPromise addListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    ChannelPromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    ChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    ChannelPromise sync() throws InterruptedException;

    @Override
    ChannelPromise syncUninterruptibly();

    @Override
    ChannelPromise await() throws InterruptedException;

    @Override
    ChannelPromise awaitUninterruptibly();

    /**
     * Returns a new {@link ChannelPromise} if {@link #isVoid()} returns {@code true} otherwise itself.
     */
    ChannelPromise unvoid();
}
