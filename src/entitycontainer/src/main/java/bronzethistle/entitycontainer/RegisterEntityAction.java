package bronzethistle.entitycontainer;

public class RegisterEntityAction implements EntityAction {
    private String entityName;

    public void execute() {
        System.out.println("Register Entity Action: " + getEntityName());
    }

    public String getEntityName() { return entityName; }
    public void setEntityName(String s) { entityName = s; }

    public void setArgs(String[] args) {
        entityName = args[1];
    }
}
