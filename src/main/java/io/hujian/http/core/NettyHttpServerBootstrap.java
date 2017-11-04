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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;

/**
 * Created by hujian06 on 2017/11/4.
 *
 * the httpServer bootstrap
 */
public class NettyHttpServerBootstrap {

    private EventLoopGroup bossGroup; // the boss group
    private EventLoopGroup workerGroup; // the worker group

    private NettyHttpServerConfigure configure; // the configure

    private ServerBootstrap serverBootstrap; // the http Server bootstrap

    private int port = 8080; // the server port,default is 8080

    /**
     * you should offer the configure to set up the httpServer
     * @param configure the conf
     */
    public NettyHttpServerBootstrap(NettyHttpServerConfigure configure) {
        this.configure = configure;
    }

    /**
     * it's ok to return the serverBootstrap to user
     * @return the bootstrap
     */
    public ServerBootstrap getServerBootstrap() {
        if (this.serverBootstrap == null) {
            init();
        }
        return this.serverBootstrap;
    }
    /**
     * extra port.
     * @param configure conf
     * @param port p
     */
    public NettyHttpServerBootstrap(NettyHttpServerConfigure configure, int port) {
        this.configure = configure;
        this.port = port;
    }

    /**
     * set up the env
     */
    public void init() {

        if (this.configure == null) {
            throw new NullPointerException("The Netty Http Server Configure is null");
        }

        this.bossGroup = new NioEventLoopGroup(this.configure.getBossGroupThreads());
        this.workerGroup = new NioEventLoopGroup(this.configure.getBossGroupThreads());

        this.serverBootstrap = new ServerBootstrap();

        Map<ChannelOption, Object> configObjectMap = this.configure.getChannelOptionObjectMap();

        //set up the option
        if (configObjectMap != null && !configObjectMap.isEmpty()) {
            for (Map.Entry<ChannelOption, Object> entry : configObjectMap.entrySet()) {
                this.serverBootstrap.option(entry.getKey(), entry.getValue());
            }
        }

        //set up some config for the http server
        this.serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class);

    }

    /**
     * reset the configure and re-start the http server
     */
    public void reset() {
        throw new UnsupportedOperationException("UnsupportedOperation Now!");
    }

    /**
     * start the server
     */
    public void start() {
        if (this.serverBootstrap == null) {
            init();
        }
        try {

            System.err.println("Netty Http Server start on: http://127.0.0.1:" + this.port + "/");

            this.serverBootstrap
                    .bind(this.port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }
}
