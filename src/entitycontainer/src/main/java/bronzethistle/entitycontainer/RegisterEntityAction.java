package bronzethistle.entitycontainer;

import bronzethistle.messages.entities.PlayerStats;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterEntityAction implements EntityAction {
    private static final Logger log = LoggerFactory.getLogger(RegisterEntityAction.class);

    private Registrar registrar;

    public RegisterEntityAction(Registrar registrar) {
        this.registrar = registrar;
    }

    private String entityName;

    public void execute() {
        System.out.println("Register Entity Action: " + getEntityName());
        PlayerStats ps = new PlayerStats();
        ps.setXp(10);
        // something needs to register the entity here with the broker
        try {
            registrar.registerEntity(getEntityName());
        } catch (HornetQException e) {
            log.error("Failed to register entity " + getEntityName(), e);
        }
    }

    public String getEntityName() { return entityName; }

    public void setArgs(String[] args) {
        entityName = args[1];
    }
}
