package com.meancat.bronzethistle.messages;

/**
 * Describes a message between the client, the edge server, and the other components of bronzethistle.
 */
public class Message {
    // TODO
    public String type;
    public String data;

    public Object payload;

    /**
     * Is this message directed at the Edge server directly?
     *
     * If so edge server can just process it.
     *
     * @return true if this is message doesn't need to be forwarded to broker.
     */
    public boolean isEdgePayload() {
        return payload instanceof EdgeMessage;
    }
}
