package com.mars.server.server.request;

import java.io.*;

/**
 * 响应对象，对netty原生response的扩展
 * <p>
 * 暂时没有提供response的支持
 *
 * @author yuye
 */
public interface HttpMarsResponse {

    /**
     * 获取java原生httpExchange
     * @return java原生通道
     */
    <T> T geNativeResponse(Class<T> cls);

    /**
     * 设置响应头
     *
     * @param key   键
     * @param value 值
     */
    void setHeader(String key, String value);

    /**
     * 响应数据
     *
     * @param context 消息
     */
    void send(String context);

    /**
     * 文件下载
     * @param fileName
     * @param inputStream
     */
    void downLoad(String fileName, InputStream inputStream);

}
