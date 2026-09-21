package up.csp;

/**
 * Represents a CSP variable identified by a unique name.
 * <p>
 * A variable holds a reference to a {@link Domain} containing its possible values
 * and maintains its current assignment status. In this solver, assigned values
 * are handled as primitive integers
 * </p>
 * @author Nicolas ITEY
 */
public class Variable {

    private String name;
    private Domain domain;
    private Integer value;

    /**
     * Constructor for the Variable class
     * @param name unique name for the {@link Variable}
     * @param domain the {@link Domain} must be enumerable, so it must be a sequence
     */
    public Variable(String name, Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    /**
     * Verify if the {@code value} has been assigned or not, return true if it has and false if it hasn't
     * @return {@code true} if the value has been assigned, {@code false} otherwise
     */
    public boolean isAssigned() {
        return value != null;
    }

    /**
     * Assign the {@code value} searched to the {@link Variable}
     * @param value the value of the {@link Variable}
     * @throws IllegalArgumentException if the parameter is invalid
     */
    public void assign(int value) {
        if (!domain.contains(value)) {
            throw new IllegalArgumentException(
                    "Value " + value + " not in domain"
            );
        }
        this.value = value;
    }

    /**
     * Unassign the {@code value} of the {@link Variable} and turn it into {@code null}
     */
    public void unassign() {
        if (isAssigned()) {
            this.value = null;
        }
    }

    /**
     * Get the name
     * @return {@code String} the name of the {@link Variable}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the {@link Domain}
     * @return the {@link Domain} of the {@link Variable}
     */
    public Domain getDomain() {

        return this.domain;
    }

    /**
     * Get the Value
     * @return the {@code value} of the {@link Variable}
     */
    public Integer getValue() {
        return this.value;
    }

   /**
    * replaces the {@link Domain} with a new one
    * @param d {@link Domain} to replace
    */
    protected void setDomain(Domain d){
        domain=d;
    }

    @Override
    public String toString() {
        return "variable "+name+" with value "+value+"\n has domain : \n"+domain;
    }
}
