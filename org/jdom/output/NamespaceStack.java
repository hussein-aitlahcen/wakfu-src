package org.jdom.output;

import java.util.*;
import org.jdom.*;

class NamespaceStack
{
    private static final String CVS_ID = "@(#) $RCSfile: NamespaceStack.java,v $ $Revision: 1.13 $ $Date: 2004/02/06 09:28:32 $ $Name: jdom_1_0 $";
    private Stack prefixes;
    private Stack uris;
    
    NamespaceStack() {
        super();
        this.prefixes = new Stack();
        this.uris = new Stack();
    }
    
    public String getURI(final String prefix) {
        final int index = this.prefixes.lastIndexOf(prefix);
        if (index == -1) {
            return null;
        }
        final String uri = (String)this.uris.elementAt(index);
        return uri;
    }
    
    public String pop() {
        final String prefix = this.prefixes.pop();
        this.uris.pop();
        return prefix;
    }
    
    public void push(final Namespace ns) {
        this.prefixes.push(ns.getPrefix());
        this.uris.push(ns.getURI());
    }
    
    public int size() {
        return this.prefixes.size();
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        final String sep = System.getProperty("line.separator");
        buf.append("Stack: " + this.prefixes.size() + sep);
        for (int i = 0; i < this.prefixes.size(); ++i) {
            buf.append(String.valueOf(String.valueOf(this.prefixes.elementAt(i))) + "&" + this.uris.elementAt(i) + sep);
        }
        return buf.toString();
    }
}
