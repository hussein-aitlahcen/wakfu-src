package org.jdom;

import java.io.*;

public abstract class Content implements Cloneable, Serializable
{
    protected Parent parent;
    
    protected Content() {
        super();
        this.parent = null;
    }
    
    public Object clone() {
        try {
            final Content c = (Content)super.clone();
            c.parent = null;
            return c;
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    public Content detach() {
        if (this.parent != null) {
            this.parent.removeContent(this);
        }
        return this;
    }
    
    public final boolean equals(final Object ob) {
        return ob == this;
    }
    
    public Document getDocument() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.getDocument();
    }
    
    public Parent getParent() {
        return this.parent;
    }
    
    public Element getParentElement() {
        final Parent parent = this.getParent();
        return (Element)((parent instanceof Element) ? parent : null);
    }
    
    public abstract String getValue();
    
    public final int hashCode() {
        return super.hashCode();
    }
    
    protected Content setParent(final Parent parent) {
        this.parent = parent;
        return this;
    }
}
