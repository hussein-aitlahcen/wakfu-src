package org.jdom;

import java.util.*;
import java.io.*;
import org.jdom.filter.*;

public class Element extends Content implements Parent
{
    private static final String CVS_ID = "@(#) $RCSfile: Element.java,v $ $Revision: 1.152 $ $Date: 2004/09/03 06:35:39 $ $Name: jdom_1_0 $";
    private static final int INITIAL_ARRAY_SIZE = 5;
    protected String name;
    protected transient Namespace namespace;
    protected transient List additionalNamespaces;
    AttributeList attributes;
    ContentList content;
    
    protected Element() {
        super();
        this.attributes = new AttributeList(this);
        this.content = new ContentList(this);
    }
    
    public Element(final String name, final Namespace namespace) {
        super();
        this.attributes = new AttributeList(this);
        this.content = new ContentList(this);
        this.setName(name);
        this.setNamespace(namespace);
    }
    
    public Element(final String name) {
        this(name, (Namespace)null);
    }
    
    public Element(final String name, final String uri) {
        this(name, Namespace.getNamespace("", uri));
    }
    
    public Element(final String name, final String prefix, final String uri) {
        this(name, Namespace.getNamespace(prefix, uri));
    }
    
    public String getName() {
        return this.name;
    }
    
    public Element setName(final String name) {
        final String reason = Verifier.checkElementName(name);
        if (reason != null) {
            throw new IllegalNameException(name, "element", reason);
        }
        this.name = name;
        return this;
    }
    
    public Namespace getNamespace() {
        return this.namespace;
    }
    
    public Element setNamespace(Namespace namespace) {
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }
        this.namespace = namespace;
        return this;
    }
    
    public String getNamespacePrefix() {
        return this.namespace.getPrefix();
    }
    
    public String getNamespaceURI() {
        return this.namespace.getURI();
    }
    
    public Namespace getNamespace(final String prefix) {
        if (prefix == null) {
            return null;
        }
        if (prefix.equals("xml")) {
            return Namespace.XML_NAMESPACE;
        }
        if (prefix.equals(this.getNamespacePrefix())) {
            return this.getNamespace();
        }
        if (this.additionalNamespaces != null) {
            for (int i = 0; i < this.additionalNamespaces.size(); ++i) {
                final Namespace ns = this.additionalNamespaces.get(i);
                if (prefix.equals(ns.getPrefix())) {
                    return ns;
                }
            }
        }
        if (this.parent instanceof Element) {
            return ((Element)this.parent).getNamespace(prefix);
        }
        return null;
    }
    
    public String getQualifiedName() {
        if (this.namespace.getPrefix().equals("")) {
            return this.getName();
        }
        return this.namespace.getPrefix() + ':' + this.name;
    }
    
    public void addNamespaceDeclaration(final Namespace additional) {
        final String reason = Verifier.checkNamespaceCollision(additional, this);
        if (reason != null) {
            throw new IllegalAddException(this, additional, reason);
        }
        if (this.additionalNamespaces == null) {
            this.additionalNamespaces = new ArrayList(5);
        }
        this.additionalNamespaces.add(additional);
    }
    
    public void removeNamespaceDeclaration(final Namespace additionalNamespace) {
        if (this.additionalNamespaces == null) {
            return;
        }
        this.additionalNamespaces.remove(additionalNamespace);
    }
    
    public List getAdditionalNamespaces() {
        if (this.additionalNamespaces == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList((List<?>)this.additionalNamespaces);
    }
    
    public String getValue() {
        final StringBuffer buffer = new StringBuffer();
        for (final Content child : this.getContent()) {
            if (child instanceof Element || child instanceof Text) {
                buffer.append(child.getValue());
            }
        }
        return buffer.toString();
    }
    
    public boolean isRootElement() {
        return this.parent instanceof Document;
    }
    
    public int getContentSize() {
        return this.content.size();
    }
    
    public int indexOf(final Content child) {
        return this.content.indexOf(child);
    }
    
    public String getText() {
        if (this.content.size() == 0) {
            return "";
        }
        if (this.content.size() == 1) {
            final Object obj = this.content.get(0);
            if (obj instanceof Text) {
                return ((Text)obj).getText();
            }
            return "";
        }
        else {
            final StringBuffer textContent = new StringBuffer();
            boolean hasText = false;
            for (int i = 0; i < this.content.size(); ++i) {
                final Object obj2 = this.content.get(i);
                if (obj2 instanceof Text) {
                    textContent.append(((Text)obj2).getText());
                    hasText = true;
                }
            }
            if (!hasText) {
                return "";
            }
            return textContent.toString();
        }
    }
    
    public String getTextTrim() {
        return this.getText().trim();
    }
    
    public String getTextNormalize() {
        return Text.normalizeString(this.getText());
    }
    
    public String getChildText(final String name) {
        final Element child = this.getChild(name);
        if (child == null) {
            return null;
        }
        return child.getText();
    }
    
    public String getChildTextTrim(final String name) {
        final Element child = this.getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }
    
    public String getChildTextNormalize(final String name) {
        final Element child = this.getChild(name);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }
    
    public String getChildText(final String name, final Namespace ns) {
        final Element child = this.getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getText();
    }
    
    public String getChildTextTrim(final String name, final Namespace ns) {
        final Element child = this.getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextTrim();
    }
    
    public String getChildTextNormalize(final String name, final Namespace ns) {
        final Element child = this.getChild(name, ns);
        if (child == null) {
            return null;
        }
        return child.getTextNormalize();
    }
    
    public Element setText(final String text) {
        this.content.clear();
        if (text != null) {
            this.addContent(new Text(text));
        }
        return this;
    }
    
    public List getContent() {
        return this.content;
    }
    
    public List getContent(final Filter filter) {
        return this.content.getView(filter);
    }
    
    public List removeContent() {
        final List old = new ArrayList(this.content);
        this.content.clear();
        return old;
    }
    
    public List removeContent(final Filter filter) {
        final List old = new ArrayList();
        final Iterator itr = this.content.getView(filter).iterator();
        while (itr.hasNext()) {
            final Content child = itr.next();
            old.add(child);
            itr.remove();
        }
        return old;
    }
    
    public Element setContent(final Collection newContent) {
        this.content.clearAndSet(newContent);
        return this;
    }
    
    public Element setContent(final int index, final Content child) {
        this.content.set(index, child);
        return this;
    }
    
    public Parent setContent(final int index, final Collection collection) {
        this.content.remove(index);
        this.content.addAll(index, collection);
        return this;
    }
    
    public Element addContent(final String str) {
        return this.addContent(new Text(str));
    }
    
    public Element addContent(final Content child) {
        this.content.add(child);
        return this;
    }
    
    public Element addContent(final Element child) {
        this.content.add(child);
        return this;
    }
    
    public Element addContent(final Collection collection) {
        this.content.addAll(collection);
        return this;
    }
    
    public Element addContent(final int index, final Content child) {
        this.content.add(index, child);
        return this;
    }
    
    public Element addContent(final int index, final Collection c) {
        this.content.addAll(index, c);
        return this;
    }
    
    public List cloneContent() {
        final int size = this.getContentSize();
        final List list = new ArrayList(size);
        for (int i = 0; i < size; ++i) {
            final Content child = this.getContent(i);
            list.add(child.clone());
        }
        return list;
    }
    
    public Content getContent(final int index) {
        return (Content)this.content.get(index);
    }
    
    public boolean removeContent(final Content child) {
        return this.content.remove(child);
    }
    
    public Content removeContent(final int index) {
        return (Content)this.content.remove(index);
    }
    
    public Element setContent(final Content child) {
        this.content.clear();
        this.content.add(child);
        return this;
    }
    
    public boolean isAncestor(final Element element) {
        for (Object p = element.getParent(); p instanceof Element; p = ((Element)p).getParent()) {
            if (p == this) {
                return true;
            }
        }
        return false;
    }
    
    public List getAttributes() {
        return this.attributes;
    }
    
    public Attribute getAttribute(final String name) {
        return this.getAttribute(name, Namespace.NO_NAMESPACE);
    }
    
    public Attribute getAttribute(final String name, final Namespace ns) {
        return (Attribute)this.attributes.get(name, ns);
    }
    
    public String getAttributeValue(final String name) {
        return this.getAttributeValue(name, Namespace.NO_NAMESPACE);
    }
    
    public String getAttributeValue(final String name, final String def) {
        return this.getAttributeValue(name, Namespace.NO_NAMESPACE, def);
    }
    
    public String getAttributeValue(final String name, final Namespace ns) {
        return this.getAttributeValue(name, ns, null);
    }
    
    public String getAttributeValue(final String name, final Namespace ns, final String def) {
        final Attribute attribute = (Attribute)this.attributes.get(name, ns);
        return (attribute == null) ? def : attribute.getValue();
    }
    
    public Element setAttributes(final List newAttributes) {
        this.attributes.clearAndSet(newAttributes);
        return this;
    }
    
    public Element setAttribute(final String name, final String value) {
        return this.setAttribute(new Attribute(name, value));
    }
    
    public Element setAttribute(final String name, final String value, final Namespace ns) {
        return this.setAttribute(new Attribute(name, value, ns));
    }
    
    public Element setAttribute(final Attribute attribute) {
        this.attributes.add(attribute);
        return this;
    }
    
    public boolean removeAttribute(final String name) {
        return this.removeAttribute(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeAttribute(final String name, final Namespace ns) {
        return this.attributes.remove(name, ns);
    }
    
    public boolean removeAttribute(final Attribute attribute) {
        return this.attributes.remove(attribute);
    }
    
    public String toString() {
        final StringBuffer stringForm = new StringBuffer(64).append("[Element: <").append(this.getQualifiedName());
        final String nsuri = this.getNamespaceURI();
        if (!nsuri.equals("")) {
            stringForm.append(" [Namespace: ").append(nsuri).append("]");
        }
        stringForm.append("/>]");
        return stringForm.toString();
    }
    
    public Object clone() {
        Element element = null;
        element = (Element)super.clone();
        element.content = new ContentList(element);
        element.attributes = new AttributeList(element);
        if (this.attributes != null) {
            for (int i = 0; i < this.attributes.size(); ++i) {
                final Object obj = this.attributes.get(i);
                final Attribute attribute = (Attribute)((Attribute)obj).clone();
                element.attributes.add(attribute);
            }
        }
        if (this.additionalNamespaces != null) {
            final int additionalSize = this.additionalNamespaces.size();
            element.additionalNamespaces = new ArrayList(additionalSize);
            for (int j = 0; j < additionalSize; ++j) {
                final Object additional = this.additionalNamespaces.get(j);
                element.additionalNamespaces.add(additional);
            }
        }
        if (this.content != null) {
            for (int i = 0; i < this.content.size(); ++i) {
                final Object obj = this.content.get(i);
                if (obj instanceof Element) {
                    final Element elt = (Element)((Element)obj).clone();
                    element.content.add(elt);
                }
                else if (obj instanceof CDATA) {
                    final CDATA cdata = (CDATA)((CDATA)obj).clone();
                    element.content.add(cdata);
                }
                else if (obj instanceof Text) {
                    final Text text = (Text)((Text)obj).clone();
                    element.content.add(text);
                }
                else if (obj instanceof Comment) {
                    final Comment comment = (Comment)((Comment)obj).clone();
                    element.content.add(comment);
                }
                else if (obj instanceof ProcessingInstruction) {
                    final ProcessingInstruction pi = (ProcessingInstruction)((ProcessingInstruction)obj).clone();
                    element.content.add(pi);
                }
                else if (obj instanceof EntityRef) {
                    final EntityRef entity = (EntityRef)((EntityRef)obj).clone();
                    element.content.add(entity);
                }
            }
        }
        if (this.additionalNamespaces != null) {
            (element.additionalNamespaces = new ArrayList()).addAll(this.additionalNamespaces);
        }
        return element;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.namespace.getPrefix());
        out.writeObject(this.namespace.getURI());
        if (this.additionalNamespaces == null) {
            out.write(0);
        }
        else {
            final int size = this.additionalNamespaces.size();
            out.write(size);
            for (int i = 0; i < size; ++i) {
                final Namespace additional = this.additionalNamespaces.get(i);
                out.writeObject(additional.getPrefix());
                out.writeObject(additional.getURI());
            }
        }
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.namespace = Namespace.getNamespace((String)in.readObject(), (String)in.readObject());
        final int size = in.read();
        if (size != 0) {
            this.additionalNamespaces = new ArrayList(size);
            for (int i = 0; i < size; ++i) {
                final Namespace additional = Namespace.getNamespace((String)in.readObject(), (String)in.readObject());
                this.additionalNamespaces.add(additional);
            }
        }
    }
    
    public Iterator getDescendants() {
        return new DescendantIterator(this);
    }
    
    public Iterator getDescendants(final Filter filter) {
        return new FilterIterator(new DescendantIterator(this), filter);
    }
    
    public List getChildren() {
        return this.content.getView(new ElementFilter());
    }
    
    public List getChildren(final String name) {
        return this.getChildren(name, Namespace.NO_NAMESPACE);
    }
    
    public List getChildren(final String name, final Namespace ns) {
        return this.content.getView(new ElementFilter(name, ns));
    }
    
    public Element getChild(final String name, final Namespace ns) {
        final List elements = this.content.getView(new ElementFilter(name, ns));
        final Iterator i = elements.iterator();
        if (i.hasNext()) {
            return i.next();
        }
        return null;
    }
    
    public Element getChild(final String name) {
        return this.getChild(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeChild(final String name) {
        return this.removeChild(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeChild(final String name, final Namespace ns) {
        final List old = this.content.getView(new ElementFilter(name, ns));
        final Iterator i = old.iterator();
        if (i.hasNext()) {
            i.next();
            i.remove();
            return true;
        }
        return false;
    }
    
    public boolean removeChildren(final String name) {
        return this.removeChildren(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeChildren(final String name, final Namespace ns) {
        boolean deletedSome = false;
        final List old = this.content.getView(new ElementFilter(name, ns));
        final Iterator i = old.iterator();
        while (i.hasNext()) {
            i.next();
            i.remove();
            deletedSome = true;
        }
        return deletedSome;
    }
}
