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

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * Created by hujian06 on 2017/11/4.
 *
 * the biz iFac for programmer
 */
public interface NettyHttpServerBizIfac {

    /**
     * you should handler the httpRequest, then set the httpResponse.
     * The Netty will write the httpResponse of this method to the
     * per-Channel(send to per.),
     * @param httpRequest the request
     * @return  httpResponse the response
     */
    HttpResponse onHttpRead(HttpRequest httpRequest) throws Exception;

    /**
     * Netty http Server handler will call this method while exceptionCaught.
     * @param throwable the e
     */
    void onHttpExceptionCaught(Throwable throwable);

}