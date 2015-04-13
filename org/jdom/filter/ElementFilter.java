package org.jdom.filter;

import org.jdom.*;
import java.io.*;

public class ElementFilter extends AbstractFilter
{
    private static final String CVS_ID = "@(#) $RCSfile: ElementFilter.java,v $ $Revision: 1.18 $ $Date: 2004/09/07 06:37:20 $ $Name: jdom_1_0 $";
    private String name;
    private transient Namespace namespace;
    
    public ElementFilter() {
        super();
    }
    
    public ElementFilter(final String name) {
        super();
        this.name = name;
    }
    
    public ElementFilter(final String name, final Namespace namespace) {
        super();
        this.name = name;
        this.namespace = namespace;
    }
    
    public ElementFilter(final Namespace namespace) {
        super();
        this.namespace = namespace;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ElementFilter)) {
            return false;
        }
        final ElementFilter filter = (ElementFilter)obj;
        boolean b;
        if (this.name != null) {
            b = (this.name.equals(filter.name) ^ true);
        }
        else {
            if (filter.name != null) {
                return false;
            }
            b = false;
        }
        if (!b) {
            boolean b2;
            if (this.namespace != null) {
                b2 = (this.namespace.equals(filter.namespace) ^ true);
            }
            else {
                if (filter.namespace != null) {
                    return false;
                }
                b2 = false;
            }
            if (!b2) {
                return true;
            }
            return false;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 29 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
        return result;
    }
    
    public boolean matches(final Object obj) {
        if (obj instanceof Element) {
            final Element el = (Element)obj;
            return (this.name == null || this.name.equals(el.getName())) && (this.namespace == null || this.namespace.equals(el.getNamespace()));
        }
        return false;
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.namespace = Namespace.getNamespace((String)in.readObject(), (String)in.readObject());
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.namespace.getPrefix());
        out.writeObject(this.namespace.getURI());
    }
}
