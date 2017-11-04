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
package io.hujian.http;

import io.hujian.http.codec.NettyHttpServerCodec;
import io.hujian.http.core.NettyHttpServerBizHandler;
import io.hujian.http.core.NettyHttpServerBootstrap;
import io.hujian.http.core.NettyHttpServerConfigure;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AsciiString;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by hujian06 on 2017/11/4.
 *
 * the sample example
 */
public class NettyHttpServerExample {

    public static void main(String ... args) {
        new HttpServerBootstrap().run();
    }
}

class HttpServerBootstrap {
    void run() {
        NettyHttpServerConfigure nettyHttpServerConfigure
                = new NettyHttpServerConfigure();

        nettyHttpServerConfigure.addOption(ChannelOption.SO_BACKLOG, 1024 * 2);

        int port = 8602;
        NettyHttpServerBootstrap nettyHttpServerBootstrap
                = new NettyHttpServerBootstrap(nettyHttpServerConfigure, port);

        nettyHttpServerBootstrap.getServerBootstrap()
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(new NettyHttpServerCodec());
                        pipeline.addLast(new HttpServerExpectContinueHandler());
                        pipeline.addLast(new HttpRequestHandler());
                    }
                });

        //start the netty http server here
        nettyHttpServerBootstrap.start();
    }
}

/**
 * the http request handler
 */
class HttpRequestHandler extends NettyHttpServerBizHandler {

    private static final byte[] CONTENT = { 'N', 'e', 't', 't', 'y', 'H', 't', 't', 'p'};

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

    @Override
    protected HttpResponse processRequest(HttpRequest httpRequest) throws Exception {

        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));

        response.headers().set(CONTENT_TYPE, "text/json");
        response.headers().setInt(CONTENT_LENGTH, ((FullHttpResponse)response).content().readableBytes());

        return response;
    }

    @Override
    protected void processException(Throwable throwable) {
        throwable.printStackTrace();
    }
}