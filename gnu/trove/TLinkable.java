package gnu.trove;

import java.io.*;

public interface TLinkable extends Serializable
{
    TLinkable getNext();
    
    TLinkable getPrevious();
    
    void setNext(TLinkable p0);
    
    void setPrevious(TLinkable p0);
}
