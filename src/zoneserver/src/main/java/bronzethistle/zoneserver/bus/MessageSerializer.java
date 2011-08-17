package bronzethistle.zoneserver.bus;

import bronzethistle.messages.client.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MessageSerializer {

    public static byte[] serialize(Message message) throws SerializerException {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(message);
            out.close();
            return byteStream.toByteArray();
        } catch(IOException e) {
            throw new SerializerException("Failed to serialize message", e);
        }
    }

    public static Message deserialize(byte[] data) throws SerializerException {
        try {
            ObjectInputStream in = null;
            in = new ObjectInputStream(new ByteArrayInputStream(data));
            Message msg = (Message)in.readObject();
            in.close();
            return msg;
        } catch(Exception e) {
            throw new SerializerException("Failed to deserialize message", e);
        }
    }
}
