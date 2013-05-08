package com.meancat.bronzethistle.messages.edge;

public class ServerTimeResponse {
    public long currentTimeMillis;

    public ServerTimeResponse() {
    }

    public ServerTimeResponse(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }
}
