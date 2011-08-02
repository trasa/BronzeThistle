package bronzethistle.messages.client;

public class LoginMessage implements Message {
    public String getCommand() {
        return "login";
    }
}
