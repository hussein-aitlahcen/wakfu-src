package gnu.trove;

import java.io.*;

public interface TObjectHashingStrategy<T> extends Serializable
{
    int computeHashCode(T p0);
    
    boolean equals(T p0, T p1);
}
