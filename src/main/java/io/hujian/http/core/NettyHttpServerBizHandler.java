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

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;

/**
 * Created by hujian06 on 2017/11/4.
 *
 * the bizHandler
 */
public abstract class NettyHttpServerBizHandler extends ChannelInboundHandlerAdapter
        implements NettyHttpServerBizIfac {

    private ChannelHandlerContext channelHandlerContext; // the handler context
    private HttpRequest httpRequest; // the httpRequest
    private HttpResponse httpResponse; // the httpResponse
    private Thread thread; // the thread that process the current channel

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    /**
     * this method is our core biz method.
     * @param ctx the context
     * @param msg the data
     * @throws Exception e
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.channelHandlerContext = ctx; // get the context
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            this.httpRequest = request; // the current request
            this.thread = Thread.currentThread(); // get the current thread

            //pre-process the data here
            preProcessRequest(request);

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            FullHttpResponse response;

            //process the request
            response = (FullHttpResponse) onHttpRead(this.httpRequest);

            this.httpResponse = response; // get the response

            if (!keepAlive) {
                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
            } else {
                ctx.write(httpResponse);
            }

            //after process the request, do some statistic job here
            afterProcessRequest(request, httpResponse);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
        onHttpExceptionCaught(cause);
        ctx.close();// it's over here
    }

    public ChannelHandlerContext context() {
        return this.channelHandlerContext;
    }

    public HttpRequest request() {
        return this.httpRequest;
    }

    public HttpResponse response() {
        return this.httpResponse;
    }

    public Thread thread() {
        return this.thread;
    }

    @Override
    public HttpResponse onHttpRead(HttpRequest httpRequest) throws Exception {
        return processRequest(httpRequest);
    }

    @Override
    public void onHttpExceptionCaught(Throwable throwable) {
        processException(throwable);
    }

    /**
     * the pre-Hook handler
     * @param httpRequest the request
     * @throws Exception e
     */
    protected void preProcessRequest(HttpRequest httpRequest) throws Exception {
        // todo
    }

    /**
     * the after-Hook handler
     * @param httpRequest the request
     * @param httpResponse the response
     * @throws Exception e
     */
    protected void afterProcessRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        //todo
    }

    /**
     * the real process handler
     * @param httpRequest the request
     * @throws Exception e
     */
    protected abstract HttpResponse processRequest(HttpRequest httpRequest) throws Exception;

    /**
     * the real process handler
     * @param throwable the e
     */
    protected abstract void processException(Throwable throwable);

}
