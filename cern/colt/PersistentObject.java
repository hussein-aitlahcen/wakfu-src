package cern.colt;

import java.io.*;

public abstract class PersistentObject implements Serializable, Cloneable
{
    public static final long serialVersionUID = 1020L;
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
