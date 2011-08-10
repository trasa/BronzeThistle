package bronzethistle.entitycontainer;


import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public enum EntityActionCommand {
    REGISTER("reg");


    private static final Map<String, EntityActionCommand> ACTION_COMMANDS = newHashMap();
    static {
        for(EntityActionCommand type : EntityActionCommand.values()) {
            ACTION_COMMANDS.put(type.code, type);
        }
    }

    private final String code;

    private EntityActionCommand(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static EntityActionCommand getActionCommand(String code) {
        return ACTION_COMMANDS.get(code);
    }
}
