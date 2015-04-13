package com.ankamagames.xulor2.core.form;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;

public class Form extends NonGraphicalElement
{
    public static final String TAG = "Form";
    private HashMap<String, Property> m_properties;
    private FormValidateCallBack m_formValidateCallBack;
    public static final int VALIDATE_HASH;
    
    public Form() {
        super();
        this.m_formValidateCallBack = null;
        this.m_properties = new HashMap<String, Property>();
    }
    
    @Override
    public void addProperty(final Property property) {
        this.addProperty(property.getName(), property);
    }
    
    public void addProperty(final String name, final Property prop) {
        this.m_properties.put(name, prop);
    }
    
    @Override
    public String getTag() {
        return "Form";
    }
    
    public Property getProperty(final String name) {
        return this.m_properties.get(name);
    }
    
    public Collection<Property> getProperties() {
        return this.m_properties.values();
    }
    
    public Set<String> getPropertyNames() {
        return this.m_properties.keySet();
    }
    
    public void synchronizeProperties() {
        for (final Property property : this.m_properties.values()) {
            property.synchronizeWithLastClient();
        }
    }
    
    @Override
    public boolean isValid() {
        if (this.m_formValidateCallBack != null) {
            final Object result = this.m_formValidateCallBack.invokeCallBack();
            return result != null && result instanceof Boolean && (boolean)result;
        }
        return true;
    }
    
    public void setValidate(final FormValidateCallBack formValidate) {
        this.m_formValidateCallBack = formValidate;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Form : ");
        for (final Property p : this.m_properties.values()) {
            sb.append("\n\t").append(p.toString());
        }
        return sb.toString();
    }
    
    @Override
    public void onCheckIn() {
        this.m_elementMap.getEnvironment().removeForm(this);
        super.onCheckIn();
    }
    
    @Override
    public void copyElement(final BasicElement f) {
        final Form e = (Form)f;
        super.copyElement(e);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Form.VALIDATE_HASH) {
            this.setValidate(cl.convert(FormValidateCallBack.class, value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public void preApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        super.preApplyAttributes(entry, parent, elementMaps, env);
        String formLocalId = null;
        final DocumentEntry id = entry.getParameterByName("id");
        if (id != null) {
            formLocalId = id.getStringValue();
        }
        else {
            Form.m_logger.warn((Object)"Attention : l'id du formulaire est nulle.");
        }
        env.openForm(elementMaps.peek().getId() + '.' + formLocalId, this);
    }
    
    @Override
    public void postAddChildComputeDocumentEntry(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        super.postAddChildComputeDocumentEntry(entry, parent, elementMaps, env);
        env.closeForm(elementMaps.peek().getId() + '.' + entry.getParameterByName("id").getStringValue());
    }
    
    static {
        VALIDATE_HASH = "validate".hashCode();
    }
}
