package org.yaml.snakeyaml.events;

public class ImplicitTuple
{
    private final boolean plain;
    private final boolean nonPlain;
    
    public ImplicitTuple(final boolean plain, final boolean nonplain) {
        super();
        this.plain = plain;
        this.nonPlain = nonplain;
    }
    
    public boolean canOmitTagInPlainScalar() {
        return this.plain;
    }
    
    public boolean canOmitTagInNonPlainScalar() {
        return this.nonPlain;
    }
    
    public boolean bothFalse() {
        return !this.plain && !this.nonPlain;
    }
    
    public String toString() {
        return "implicit=[" + this.plain + ", " + this.nonPlain + "]";
    }
}
