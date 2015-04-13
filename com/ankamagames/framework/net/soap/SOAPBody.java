package com.ankamagames.framework.net.soap;

import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public class SOAPBody
{
    public static final String TAG = "SOAP-ENV:Body";
    private final ArrayList<SOAPElement> m_elements;
    
    public SOAPBody() {
        super();
        this.m_elements = new ArrayList<SOAPElement>();
    }
    
    public void addElement(final SOAPElement elem) {
        this.m_elements.add(elem);
    }
    
    public ArrayList<SOAPElement> getElements() {
        return this.m_elements;
    }
    
    public SOAPElement getElement(final String name) {
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            final SOAPElement soapElement = this.m_elements.get(i);
            if (soapElement.getName().equalsIgnoreCase(name)) {
                return soapElement;
            }
        }
        return null;
    }
    
    public XMLDocumentNode toXMLDocumentNode() {
        final XMLDocumentNode node = new XMLDocumentNode("SOAP-ENV:Body", "");
        for (int i = 0, size = this.m_elements.size(); i < size; ++i) {
            final SOAPElement element = this.m_elements.get(i);
            node.addChild(element.toXMLDocumentNode());
        }
        return node;
    }
    
    public static SOAPBody parseSOAPXMLEntry(final DocumentEntry node) throws IllegalArgumentException {
        final String name = node.getName();
        if (!name.equalsIgnoreCase("SOAP-ENV:Body")) {
            throw new IllegalArgumentException("Le nom de l'\u00e9l\u00e9ment ne respecte pas la norme : " + name);
        }
        final SOAPBody soapBody = new SOAPBody();
        for (final DocumentEntry entry : node.getChildren()) {
            soapBody.addElement(SOAPElement.parseSOAPXMLEntry(entry));
        }
        return soapBody;
    }
    
    @Override
    public String toString() {
        return "SOAPBody{m_elements=" + this.m_elements + '}';
    }
}
