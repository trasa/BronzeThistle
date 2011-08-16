package bronzethistle.entitycontainer;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityActionParser {

    @Autowired
    protected Registrar registrar;

    public EntityAction parse(String s) {
        String[] parts = Iterables.toArray(Splitter.on(" ").trimResults().omitEmptyStrings().split(s), String.class);


        EntityActionCommand command = EntityActionCommand.getActionCommand(parts[0]);
        EntityAction a = getAction(command);
        a.setArgs(parts);
        return a;
    }

    public EntityAction getAction(EntityActionCommand command) {
        switch(command) {
            case REGISTER:
                return new RegisterEntityAction(registrar);
            default:
                return null;
        }
    }
}
