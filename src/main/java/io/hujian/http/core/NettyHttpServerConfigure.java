/*
 * Copyright 2017 The Netty-HttpServer Project
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
package io.hujian.http.core;

import io.netty.channel.ChannelOption;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hujian06 on 2017/11/4.
 *
 * the configure of httpServer
 */
public class NettyHttpServerConfigure {

    private Map<ChannelOption, Object> channelOptionObjectMap; // the option
    private int bossGroupThreads = 1; // the boss threads, default is 1
    private int workerGroupThreads = Runtime.getRuntime().availableProcessors() * 2; // the worker threads.

    public NettyHttpServerConfigure() {

    }

    public int getBossGroupThreads() {
        return this.bossGroupThreads;
    }

    public int getWorkerGroupThreads() {
        return this.workerGroupThreads;
    }

    public Map<ChannelOption, Object> getChannelOptionObjectMap() {
        return this.channelOptionObjectMap;
    }

    public NettyHttpServerConfigure setBossNThreads(int nThreads) {
        this.bossGroupThreads = nThreads;

        return this;
    }

    public NettyHttpServerConfigure setWorkerNThreads(int nThreads) {
        this.workerGroupThreads = nThreads;

        return this;
    }

    /**
     * add a option to map
     * @param channelOption the option
     * @param value the value
     * @param <T> the type
     */
    public <T> NettyHttpServerConfigure addOption(ChannelOption<T> channelOption, T value) {
        if (this.channelOptionObjectMap == null) {
            this.channelOptionObjectMap = new HashMap<ChannelOption, Object>();
        }

        this.channelOptionObjectMap.put(channelOption, value);

        return this;
    }

}
