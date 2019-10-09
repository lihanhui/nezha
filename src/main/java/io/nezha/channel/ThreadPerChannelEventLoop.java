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

/**
 * {@link SingleThreadEventLoop} which is used to handle OIO {@link IChannel}'s. So in general there will be
 * one {@link ThreadPerChannelEventLoop} per {@link IChannel}.
 *
 */
public class ThreadPerChannelEventLoop extends SingleThreadEventLoop {

    private final ThreadPerChannelEventLoopGroup parent;
    private IChannel ch;

    public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup parent) {
        super(parent, parent.executor, true);
        this.parent = parent;
    }

    @Override
    public ChannelFuture register(ChannelPromise promise) {
        return super.register(promise).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ch = future.channel();
                } else {
                    deregister();
                }
            }
        });
    }

    @Override
    protected void run() {
        for (;;) {
            Runnable task = takeTask();
            if (task != null) {
                task.run();
                updateLastExecutionTime();
            }

            IChannel ch = this.ch;
            if (isShuttingDown()) {
                if (ch != null) {
                    ch.unsafe().close(ch.unsafe().voidPromise());
                }
                if (confirmShutdown()) {
                    break;
                }
            } else {
                if (ch != null) {
                    // Handle deregistration
                    if (!ch.isRegistered()) {
                        runAllTasks();
                        deregister();
                    }
                }
            }
        }
    }

    protected void deregister() {
        ch = null;
        parent.activeChildren.remove(this);
        parent.idleChildren.add(this);
    }
}
