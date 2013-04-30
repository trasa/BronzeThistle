package com.meancat.bronzethistle.client.handlers;


public interface ClientMessageHandler<T> {
    void handleMessage(T msg);
}
