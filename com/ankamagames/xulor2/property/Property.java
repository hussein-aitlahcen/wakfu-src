package com.ankamagames.xulor2.property;

import org.apache.log4j.*;
import java.util.concurrent.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.renderer.condition.*;
import com.ankamagames.xulor2.util.*;
import java.lang.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.factory.*;
import org.jetbrains.annotations.*;

public final class Property implements ResultProviderParent
{
    private static final Logger m_logger;
    private static boolean DEBUG;
    public static final String FIELD_SEPARATOR = "/";
    private final String m_name;
    private Object m_value;
    private boolean m_valueInit;
    private final List<PropertyClientData<BasicElement>> m_clients;
    private final ConcurrentLinkedQueue<AsynchronousPropertyChange> m_asynchronousPropertyChanges;
    private final ArrayList<Property> m_childrenVirtualProperties;
    private Property m_parent;
    private boolean m_virtual;
    private boolean m_asListElement;
    private int m_listIndex;
    private String m_virtualField;
    private ElementMap m_elementMap;
    private final ArrayList<Item> m_associatedItems;
    private boolean m_dispatchValueEvents;
    public static final String VIRTUAL_PROPERTY_SEPARATOR = "#";
    
    public Property(final String name, final ElementMap elementMap) {
        this(name, elementMap, false);
    }
    
    public Property(final String name, final ElementMap elementMap, final boolean virtual) {
        super();
        this.m_value = null;
        this.m_valueInit = false;
        this.m_clients = Collections.synchronizedList(new ArrayList<PropertyClientData<BasicElement>>());
        this.m_asynchronousPropertyChanges = new ConcurrentLinkedQueue<AsynchronousPropertyChange>();
        this.m_childrenVirtualProperties = new ArrayList<Property>();
        this.m_parent = null;
        this.m_virtual = false;
        this.m_asListElement = false;
        this.m_virtualField = null;
        this.m_elementMap = null;
        this.m_associatedItems = new ArrayList<Item>();
        this.m_name = name;
        this.m_virtual = virtual;
        this.m_elementMap = elementMap;
        if (this.m_elementMap != null) {
            this.m_elementMap.addProperty(this);
        }
    }
    
    public Property(final String name, final Property parent, final String virtualField, final ElementMap elementMap) {
        this(name, elementMap, true);
        this.m_virtualField = virtualField;
        if (parent != null) {
            parent.addVirtualProperty(this);
        }
    }
    
    public Property(final String name, final Property parent, final int listIndex, final ElementMap elementMap) {
        this(name, elementMap, true);
        this.m_parent = parent;
        this.m_asListElement = true;
        this.m_listIndex = listIndex;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getVirtualField() {
        return this.m_virtualField;
    }
    
    public void addAssociatedItem(final Item item) {
        this.m_associatedItems.add(item);
    }
    
    public void removeAssociatedItem(final Item item) {
        this.m_associatedItems.remove(item);
        if (this.m_associatedItems.isEmpty() && this.m_clients.isEmpty() && this.m_childrenVirtualProperties.isEmpty()) {
            PropertiesProvider.getInstance().removeProperty(this);
        }
    }
    
    public boolean isDispatchValueEvents() {
        return this.m_dispatchValueEvents;
    }
    
    public void setDispatchValueEvents(final boolean dispatchValueEvents) {
        this.m_dispatchValueEvents = dispatchValueEvents;
    }
    
    public void setElementMap(final ElementMap elementMap) {
        this.m_elementMap = elementMap;
    }
    
    public boolean isLocal() {
        return this.m_elementMap != null;
    }
    
    public boolean isValueInitialized() {
        return this.m_valueInit;
    }
    
    public ElementMap getElementMap() {
        return this.m_elementMap;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public String getString() {
        return PrimitiveConverter.getString(this.m_value);
    }
    
    public boolean getBoolean() {
        return PrimitiveConverter.getBoolean(this.m_value);
    }
    
    public int getInt() {
        return PrimitiveConverter.getInteger(this.m_value);
    }
    
    public short getShort() {
        return PrimitiveConverter.getShort(this.m_value);
    }
    
    public double getDouble() {
        return PrimitiveConverter.getDouble(this.m_value);
    }
    
    public float getFloat() {
        return PrimitiveConverter.getFloat(this.m_value);
    }
    
    public long getLong() {
        return PrimitiveConverter.getLong(this.m_value);
    }
    
    public boolean isEmpty() {
        if (this.m_value instanceof String) {
            return this.m_value.equals("");
        }
        return this.m_value == null;
    }
    
    @Nullable
    public static Object getValue(final Object value, final String fieldName) {
        if (!(value instanceof FieldProvider) || fieldName == null) {
            return value;
        }
        final ObjectPair<Object, String> pair = getFieldProviderAndField(value, fieldName);
        if (pair.getFirst() instanceof FieldProvider && pair.getSecond() != null) {
            final FieldProvider provider = pair.getFirst();
            return provider.getFieldValue(pair.getSecond());
        }
        return null;
    }
    
    public static ObjectPair<Object, String> getFieldProviderAndField(Object value, final String fieldName) {
        final ObjectPair<Object, String> ret = new ObjectPair<Object, String>(value, null);
        final String[] fieldNamePath = fieldName.split("/");
        for (int i = 0; i < fieldNamePath.length - 1; ++i) {
            if (!(value instanceof FieldProvider) || fieldNamePath[i] == null) {
                ret.setSecond(null);
                return ret;
            }
            value = ((FieldProvider)value).getFieldValue(fieldNamePath[i]);
            ret.setFirst(value);
        }
        ret.setSecond(fieldNamePath[fieldNamePath.length - 1]);
        return ret;
    }
    
    private Object getValue(final String fieldName) {
        if (this.m_value instanceof FieldProvider && fieldName != null) {
            final FieldProvider provider = (FieldProvider)this.m_value;
            return provider.getFieldValue(fieldName);
        }
        return this.m_value;
    }
    
    private Object getVirtualChildrenValue(final String fieldName) {
        final String[] fieldNamePath = fieldName.split("/");
        Object value = this.m_value;
        for (int i = 0; i < fieldNamePath.length; ++i) {
            if (!(value instanceof FieldProvider) || fieldNamePath[i] == null) {
                return value;
            }
            value = ((FieldProvider)value).getFieldValue(fieldNamePath[i]);
        }
        return value;
    }
    
    public Object getFieldObjectValue(final String fieldName) {
        return this.getValue(fieldName);
    }
    
    public String getFieldStringValue(final String fieldName) {
        return PrimitiveConverter.getString(this.getValue(fieldName));
    }
    
    public boolean getFieldBooleanValue(final String fieldName) {
        return PrimitiveConverter.getBoolean(this.getValue(fieldName));
    }
    
    public int getFieldIntValue(final String fieldName) {
        return PrimitiveConverter.getInteger(this.getValue(fieldName));
    }
    
    public long getFieldLongValue(final String fieldName) {
        return PrimitiveConverter.getLong(this.getValue(fieldName));
    }
    
    public double getFieldDoubleValue(final String fieldName) {
        return PrimitiveConverter.getDouble(this.getValue(fieldName));
    }
    
    public float getFieldFloatValue(final String fieldName) {
        return PrimitiveConverter.getFloat(this.getValue(fieldName));
    }
    
    public void updateVirtualChildren() {
        FieldProvider[] list = null;
        if (this.m_value instanceof FieldProvider[]) {
            list = (FieldProvider[])this.m_value;
        }
        for (int i = 0, size = this.m_childrenVirtualProperties.size(); i < size; ++i) {
            final Property p = this.m_childrenVirtualProperties.get(i);
            if (p.m_asListElement && list != null && list.length > p.m_listIndex) {
                p.setValue(list[p.m_listIndex], false);
            }
            else {
                p.setValue(this.getVirtualChildrenValue(p.getVirtualField()), false);
            }
        }
    }
    
    public void updateVirtualChildren(final String fieldName) {
        for (int i = 0, size = this.m_childrenVirtualProperties.size(); i < size; ++i) {
            final Property p = this.m_childrenVirtualProperties.get(i);
            if (fieldName.equals(p.m_virtualField)) {
                p.setValue(this.getVirtualChildrenValue(p.m_virtualField), false);
            }
        }
    }
    
    public boolean needProcess() {
        return !this.m_asynchronousPropertyChanges.isEmpty();
    }
    
    public void onProcess() {
        for (AsynchronousPropertyChange change = this.m_asynchronousPropertyChanges.poll(); change != null; change = this.m_asynchronousPropertyChanges.poll()) {
            change.m_client.setPropertyChange(null);
            switch (change.m_method) {
                case SET: {
                    this.setToCastValueChecked(change.m_client, change.m_value);
                    break;
                }
                case PREPEND: {
                    this.prependToCastValueChecked(change.m_client, change.m_value);
                    break;
                }
                case APPEND: {
                    this.appendToCastValueChecked(change.m_client, change.m_value);
                    break;
                }
            }
        }
    }
    
    public void addPropertyClientToVirtual(final PropertyClientData<BasicElement> pc, final boolean applyValueAtOnce) {
        final String name = pc.getFieldName().substring(0, pc.getFieldName().indexOf("/"));
        Property property = null;
        for (final Property p : this.m_childrenVirtualProperties) {
            if (p.getVirtualField().equalsIgnoreCase(name)) {
                property = p;
                break;
            }
        }
        if (property == null) {
            property = new Property(getVirtualPropertyName(this, name), this, name, this.m_elementMap);
            property.setValue(this.getFieldObjectValue(name), false);
            PropertiesProvider.getInstance().addProperty(property);
        }
        pc.setFieldName(pc.getFieldName().substring(pc.getFieldName().indexOf("/") + 1));
        property.addPropertyClient(pc, applyValueAtOnce);
    }
    
    public void addPropertyClient(final PropertyClientData<BasicElement> pc) {
        this.addPropertyClient(pc, true);
    }
    
    public void addPropertyClient(final PropertyClientData<BasicElement> pc, final boolean applyValueAtOnce) {
        if (pc.getFieldName() != null && pc.getFieldName().contains("/")) {
            this.addPropertyClientToVirtual(pc, applyValueAtOnce);
            return;
        }
        if (this.m_clients.contains(pc)) {
            Property.m_logger.error((Object)("Ajout d'un client \u00e0 une propri\u00e9t\u00e9 qui le contient d\u00e9j\u00e0 : " + pc));
        }
        else {
            this.m_clients.add(pc);
            pc.getElement().addProperty(this);
        }
        if (pc.getResultProvider() != null) {
            pc.getResultProvider().setResultProviderParent(this);
        }
        if (pc.getContentProperty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.m_name);
            if (pc.getFieldName() != null) {
                sb.append("/").append(pc.getFieldName());
            }
            ((ContentClient)pc.getElement()).setContentProperty(sb.toString(), this.m_elementMap);
        }
        if (this.m_valueInit) {
            Object value = null;
            if (this.m_value instanceof FieldProvider && pc.getFieldName() != null) {
                value = ((FieldProvider)this.m_value).getFieldValue(pc.getFieldName());
            }
            else {
                value = this.m_value;
            }
            if (applyValueAtOnce) {
                if (pc.getResultProvider() != null) {
                    value = pc.getResultProvider().getResult(value);
                }
                this.setToCastValueChecked(pc, value);
            }
            else {
                this.setToCastValue(pc, value);
            }
        }
    }
    
    public void removePropertyClient(final BasicElement element) {
        if (element == null) {
            return;
        }
        final Iterator<AsynchronousPropertyChange> it = this.m_asynchronousPropertyChanges.iterator();
        while (it.hasNext()) {
            if (it.next().m_client.getElement() == element) {
                it.remove();
            }
        }
        final Collection<PropertyClientData> toRemove = new ArrayList<PropertyClientData>();
        for (final PropertyClientData pc : this.m_clients) {
            if (pc.getElement().equals(element)) {
                toRemove.add(pc);
                if (!pc.getContentProperty()) {
                    continue;
                }
                pc.getElement().setContentProperty(null, null);
            }
        }
        this.m_clients.removeAll(toRemove);
        element.removeProperty(this);
        if (this.m_virtual && this.m_associatedItems.isEmpty() && this.m_clients.isEmpty() && this.m_childrenVirtualProperties.isEmpty()) {
            if (this.m_parent != null) {
                this.m_parent.removeVirtualProperty(this);
            }
            PropertiesProvider.getInstance().removeProperty(this);
        }
    }
    
    public PropertyClientData getPropertyClientByAttribute(final BasicElement e, final String attribute) {
        if (attribute == null) {
            return null;
        }
        for (final PropertyClientData pc : this.m_clients) {
            if (e == pc.getElement() && attribute.equalsIgnoreCase(pc.getAttribute())) {
                return pc;
            }
        }
        return null;
    }
    
    public void addVirtualProperty(final Property p) {
        p.m_parent = this;
        this.m_childrenVirtualProperties.add(p);
    }
    
    public void removeVirtualProperty(final Property p) {
        this.m_childrenVirtualProperties.remove(p);
        if (this.m_virtual && this.m_clients.isEmpty() && this.m_childrenVirtualProperties.isEmpty() && this.m_parent != null) {
            this.m_parent.removeVirtualProperty(this);
        }
    }
    
    public Property getChildProperty(final String field) {
        assert field != null : "On essaye de r\u00e9cup\u00e9rer une propri\u00e9t\u00e9 enfant avec un nom null !";
        for (int i = this.m_childrenVirtualProperties.size() - 1; i >= 0; --i) {
            final Property childProperty = this.m_childrenVirtualProperties.get(i);
            if (field.equals(childProperty.getVirtualField())) {
                return childProperty;
            }
        }
        return null;
    }
    
    protected void setToCastValue(final PropertyClientData<BasicElement> pc, final Object value) {
        if (Property.DEBUG) {
            this.setToCastValueChecked(pc, value);
        }
        else {
            final AsynchronousPropertyChange apc = pc.getPropertyChange();
            if (apc != null) {
                if (apc.m_client == pc && apc.m_method != null && apc.m_method.equals(PropertySetMethod.SET)) {
                    if (apc.m_client.getResultProvider() != null) {
                        apc.m_value = apc.m_client.getResultProvider().getResult(value);
                    }
                    else {
                        apc.m_value = value;
                    }
                }
            }
            else {
                this.m_asynchronousPropertyChanges.offer(new AsynchronousPropertyChange(pc, value, PropertySetMethod.SET));
            }
            PropertiesProvider.getInstance().addToProcessList(this);
        }
    }
    
    protected void appendToCastValue(final PropertyClientData<BasicElement> pc, final Object value) {
        if (Property.DEBUG) {
            this.appendToCastValueChecked(pc, value);
        }
        else {
            this.m_asynchronousPropertyChanges.offer(new AsynchronousPropertyChange(pc, value, PropertySetMethod.APPEND));
            PropertiesProvider.getInstance().addToProcessList(this);
        }
    }
    
    protected void prependToCastValue(final PropertyClientData<BasicElement> pc, final Object value) {
        if (Property.DEBUG) {
            this.prependToCastValueChecked(pc, value);
        }
        else {
            this.m_asynchronousPropertyChanges.offer(new AsynchronousPropertyChange(pc, value, PropertySetMethod.PREPEND));
            PropertiesProvider.getInstance().addToProcessList(this);
        }
    }
    
    private void setToCastValueChecked(final PropertyClientData<BasicElement> pc, final Object value) {
        try {
            final int attributeHash = pc.getAttributeHash();
            if (value instanceof String && EventDispatcher.ID_HASH != attributeHash && TextWidget.TEXT_HASH != attributeHash && Condition.VALUE_HASH != attributeHash && Condition.COMPARED_VALUE_HASH != attributeHash) {
                if (pc.getElement().setXMLAttribute(pc.getAttribute(), (String)value)) {
                    return;
                }
            }
            else if (pc.getElement().setPropertyAttribute(pc.getAttribute(), value)) {
                return;
            }
            Method m = pc.getSetter();
            if (m == null || !MethodUtil.checkMethodParameter(m, value)) {
                m = pc.getFactory().guessSetter(pc.getAttribute(), (value == null) ? null : value.getClass());
            }
            if (m != null) {
                this.invokeMethodAccessor(m, pc, value);
            }
            else {
                Property.m_logger.error((Object)("[" + this.m_name + "]Impossible de trouver la m\u00e9thode set" + pc.getAttribute() + " dans " + pc.getElement() + " avec la classe " + ((value == null) ? null : value.getClass())));
            }
        }
        catch (Throwable t) {
            Property.m_logger.error((Object)"Exception dans Property : ", t);
        }
    }
    
    private void prependToCastValueChecked(final PropertyClientData<BasicElement> pc, final Object value) {
        try {
            final int attributeHash = pc.getAttributeHash();
            if (value instanceof String && EventDispatcher.ID_HASH != attributeHash && TextWidget.TEXT_HASH != attributeHash && Condition.VALUE_HASH != attributeHash && Condition.COMPARED_VALUE_HASH != attributeHash) {
                if (pc.getElement().prependXMLAttribute(pc.getAttribute(), (String)value)) {
                    return;
                }
            }
            else if (pc.getElement().prependPropertyAttribute(pc.getAttribute(), value)) {
                return;
            }
            Method m = pc.getPrepender();
            if (!MethodUtil.checkMethodParameter(m, value)) {
                m = pc.getFactory().guessPrepender(pc.getAttribute(), (value == null) ? null : value.getClass());
            }
            if (m != null) {
                this.invokeMethodAccessor(m, pc, value);
            }
            else {
                Property.m_logger.error((Object)("Impossible de trouver la m\u00e9thode prepend" + pc.getAttribute() + " avec la classe " + ((value == null) ? null : value.getClass())));
            }
        }
        catch (Throwable t) {
            Property.m_logger.error((Object)"Exception dans Property : ", t);
        }
    }
    
    private void appendToCastValueChecked(final PropertyClientData<BasicElement> pc, final Object value) {
        try {
            final int attributeHash = pc.getAttributeHash();
            if (value instanceof String && EventDispatcher.ID_HASH != attributeHash && TextWidget.TEXT_HASH != attributeHash && Condition.VALUE_HASH != attributeHash && Condition.COMPARED_VALUE_HASH != attributeHash) {
                if (pc.getElement().appendXMLAttribute(pc.getAttribute(), (String)value)) {
                    return;
                }
            }
            else if (pc.getElement().appendPropertyAttribute(pc.getAttribute(), value)) {
                return;
            }
            Method m = pc.getAppender();
            if (!MethodUtil.checkMethodParameter(m, value)) {
                m = pc.getFactory().guessAppender(pc.getAttribute(), (value == null) ? null : value.getClass());
            }
            if (m != null) {
                this.invokeMethodAccessor(m, pc, value);
            }
            else {
                Property.m_logger.error((Object)("Impossible de trouver la m\u00e9thode append" + pc.getAttribute() + " dans " + pc.getElement() + " avec la classe " + ((value == null) ? null : value.getClass())));
            }
        }
        catch (Throwable t) {
            Property.m_logger.error((Object)"Exception dans Property : ", t);
        }
    }
    
    protected void invokeMethodAccessor(final Method method, final PropertyClientData<BasicElement> pc, final Object value) {
        try {
            if (method.getParameterTypes().length == 0) {
                return;
            }
            MethodUtil.castInvoke(method, pc.getElement(), new Object[] { value });
        }
        catch (IllegalArgumentException e) {
            Property.m_logger.error((Object)("Exception illegalArgument : " + e));
        }
        catch (Exception e2) {
            Property.m_logger.error((Object)("[" + e2.getClass().getSimpleName() + "] Erreur lors du InvokeMethodAccessor - Method=" + ((method == null) ? "null" : method.getName()) + " - PropertyClientData = " + pc + " - Value = " + value));
        }
    }
    
    public void setValue(final Object value, final boolean force) {
        if (!this.m_valueInit) {
            this.m_valueInit = true;
            PropertiesProvider.getInstance().dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_INIT, this);
        }
        if (!force && value == this.m_value) {
            return;
        }
        this.m_value = value;
        if (this.m_dispatchValueEvents) {
            PropertiesProvider.getInstance().dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_VALUE_CHANGED, this);
        }
        this.updateVirtualChildren();
        for (final PropertyClientData<BasicElement> client : this.m_clients) {
            final String fieldName = client.getFieldName();
            if (value instanceof FieldProvider && fieldName != null) {
                final FieldProvider fieldedPropertyProvider = (FieldProvider)value;
                final Object fieldValue = fieldedPropertyProvider.getFieldValue(fieldName);
                this.setToCastValue(client, fieldValue);
            }
            else {
                this.setToCastValue(client, value);
            }
        }
    }
    
    public void prependValue(final Object value) {
        for (final PropertyClientData<BasicElement> client : this.m_clients) {
            this.prependToCastValue(client, value);
        }
    }
    
    public void appendValue(final Object value) {
        for (final PropertyClientData<BasicElement> client : this.m_clients) {
            this.appendToCastValue(client, value);
        }
    }
    
    public void setFieldValue(final String field, final Object value) {
        if (this.m_value instanceof FieldProvider && field != null) {
            final FieldProvider fieldedPropertyProvider = (FieldProvider)this.m_value;
            fieldedPropertyProvider.setFieldValue(field, value);
            for (final PropertyClientData<BasicElement> client : this.m_clients) {
                final String fieldName = client.getFieldName();
                if (fieldName != null && fieldName.equals(field)) {
                    this.setToCastValue(client, value);
                }
            }
        }
    }
    
    public void fireFieldValueChanged(final String field) {
        this.fireFieldValueChanged(field, null);
    }
    
    public void fireFieldValueChanged(final String field, final IntObjectLightWeightMap<Object> values) {
        this.fireFieldValueChanged(new String[] { field }, values);
    }
    
    public void fireFieldValueChanged(final String[] fields, final IntObjectLightWeightMap<Object> values) {
        if (this.m_value == null || !(this.m_value instanceof FieldProvider)) {
            return;
        }
        final PropertyClientData<BasicElement>[] clients = (PropertyClientData<BasicElement>[])new PropertyClientData[this.m_clients.size()];
        this.m_clients.toArray(clients);
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; ++i) {
            this.fireFieldValueChanged(values, clients, fields[i]);
            this.updateVirtualChildren(fields[i]);
        }
    }
    
    private void fireFieldValueChanged(final IntObjectLightWeightMap<Object> values, final PropertyClientData<BasicElement>[] clients, final String field) {
        final FieldProvider fieldedPropertyProvider = (FieldProvider)this.m_value;
        final int fieldHashCode = (field != null) ? field.hashCode() : 0;
        Object value = (values != null) ? values.get(fieldHashCode) : null;
        boolean valueInit = value != null;
        for (int i = 0, clientsLength = clients.length; i < clientsLength; ++i) {
            final PropertyClientData<BasicElement> client = clients[i];
            if (field != null) {
                if (field.equals(client.getFieldName())) {
                    if (!valueInit) {
                        value = fieldedPropertyProvider.getFieldValue(field);
                        if (values != null) {
                            values.put(fieldHashCode, value);
                        }
                        valueInit = true;
                    }
                    this.setToCastValue(client, value);
                }
            }
        }
    }
    
    public void firePropertyChanged() {
        if (this.m_value instanceof FieldProvider) {
            final FieldProvider fp = (FieldProvider)this.m_value;
            for (final PropertyClientData<BasicElement> client : this.m_clients) {
                final String fieldName = client.getFieldName();
                if (fieldName != null) {
                    this.setToCastValue(client, fp.getFieldValue(fieldName));
                }
                else {
                    this.setToCastValue(client, this.m_value);
                }
            }
            this.updateVirtualChildren();
        }
        else if (this.m_valueInit) {
            for (final PropertyClientData<BasicElement> client2 : this.m_clients) {
                this.setToCastValue(client2, this.m_value);
            }
        }
    }
    
    @Override
    public void fireResultProviderChanged(final ResultProvider rp) {
        if (this.m_value instanceof FieldProvider) {
            final FieldProvider fp = (FieldProvider)this.m_value;
            for (final PropertyClientData<BasicElement> client : this.m_clients) {
                if (client.getResultProvider() != rp) {
                    continue;
                }
                final String fieldName = client.getFieldName();
                if (fieldName != null) {
                    this.setToCastValue(client, fp.getFieldValue(fieldName));
                }
                else {
                    this.setToCastValue(client, this.m_value);
                }
            }
            this.updateVirtualChildren();
        }
        else if (this.m_valueInit) {
            for (final PropertyClientData<BasicElement> client2 : this.m_clients) {
                if (client2.getResultProvider() != rp) {
                    continue;
                }
                this.setToCastValue(client2, this.m_value);
            }
        }
    }
    
    public void prependFieldValue(final String field, final Object value) {
        if (this.m_value == null || !(this.m_value instanceof FieldProvider)) {
            return;
        }
        final FieldProvider fieldedPropertyProvider = (FieldProvider)this.m_value;
        fieldedPropertyProvider.prependFieldValue(field, value);
        for (final PropertyClientData<BasicElement> client : this.m_clients) {
            if (client.getFieldName().equals(field)) {
                this.prependToCastValue(client, value);
            }
        }
    }
    
    public void appendFieldValue(final String field, final Object value) {
        if (this.m_value == null || !(this.m_value instanceof FieldProvider)) {
            return;
        }
        final FieldProvider fieldedPropertyProvider = (FieldProvider)this.m_value;
        fieldedPropertyProvider.appendFieldValue(field, value);
        for (final PropertyClientData<BasicElement> client : this.m_clients) {
            if (client.getFieldName().equals(field)) {
                this.appendToCastValue(client, value);
            }
        }
    }
    
    public void synchronizeWithLastClient() {
        if (this.m_value == null || !(this.m_value instanceof FieldProvider)) {
            this.singleValueSynchronize();
        }
        else {
            this.fieldProviderSynchronize();
        }
    }
    
    private void fieldProviderSynchronize() {
        if (this.m_value == null) {
            return;
        }
        final FieldProvider fieldProvider = (FieldProvider)this.m_value;
        final String[] fields = fieldProvider.getFields();
        if (fields == null) {
            return;
        }
        for (final String field : fields) {
            if (field != null) {
                Object fieldValue = null;
                if (!fieldProvider.isFieldSynchronisable(field)) {
                    fieldValue = fieldProvider.getFieldValue(field);
                }
                for (int i = this.m_clients.size() - 1; i >= 0; --i) {
                    final PropertyClientData<BasicElement> client = this.m_clients.get(i);
                    final String fieldName = client.getFieldName();
                    if (fieldName != null && fieldName.equals(field)) {
                        if (fieldValue == null) {
                            final Method method = client.getFactory().guessGetter(client.getAttribute(), (fieldValue == null) ? null : fieldValue.getClass());
                            try {
                                fieldValue = method.invoke(client.getElement(), new Object[0]);
                                fieldProvider.setFieldValue(field, fieldValue);
                            }
                            catch (Exception e) {
                                if (client.getElement() == null) {
                                    Property.m_logger.error((Object)("[fieldProviderSynchronize] PropertyClientData avec un element null : field = " + client.getFieldName()));
                                }
                                else if (method == null) {
                                    Property.m_logger.error((Object)("[fieldProviderSynchronize] La m\u00e9thode " + client.getElement().getClass().getName() + ".get" + client.getAttribute() + "() n'existe pas, impossible de la charger"));
                                }
                                else {
                                    Property.m_logger.error((Object)"Exception", (Throwable)e);
                                }
                            }
                        }
                        else {
                            this.setToCastValue(client, fieldValue);
                        }
                    }
                }
            }
        }
    }
    
    private void singleValueSynchronize() {
        if (this.m_clients.isEmpty()) {
            return;
        }
        final PropertyClientData propertyClient = this.m_clients.get(this.m_clients.size() - 1);
        final Class<?> type = (this.m_value == null) ? null : this.m_value.getClass();
        final Method method = propertyClient.getFactory().guessGetter(propertyClient.getAttribute(), type);
        if (method == null) {
            return;
        }
        try {
            this.m_value = method.invoke(propertyClient.getElement(), new Object[0]);
            if (!this.m_valueInit) {
                this.m_valueInit = true;
                PropertiesProvider.getInstance().dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_INIT, this);
            }
            if (this.m_dispatchValueEvents) {
                PropertiesProvider.getInstance().dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_VALUE_CHANGED, this);
            }
            this.updateVirtualChildren();
            for (int i = 0; i < this.m_clients.size() - 1; ++i) {
                final PropertyClientData<BasicElement> pc = this.m_clients.get(i);
                this.setToCastValue(pc, this.m_value);
            }
        }
        catch (Exception e) {
            Property.m_logger.error((Object)("Impossible de synchroniser la propri\u00e9t\u00e9 " + this.m_name + " avec " + propertyClient.getElement().getClass().getName() + ", l'attribut " + propertyClient.getAttribute() + " est incompatible !"));
        }
    }
    
    public boolean isCompatible(final Class<?> clazz, final Factory<?> factory) {
        final Class<?> compatibleClass = this.getCompatibleClass(factory);
        return compatibleClass != null && compatibleClass.isAssignableFrom(clazz);
    }
    
    @Nullable
    public Class<?> getCompatibleClass(final Factory<?> factory) {
        final Class<?> type = (this.m_value == null) ? null : this.m_value.getClass();
        final Method method = factory.guessSetter(this.m_name, type);
        if (method != null) {
            return method.getDeclaringClass();
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Property name=").append(this.m_name);
        if (this.m_elementMap != null) {
            sb.append(" elementMap=").append(this.m_elementMap.getId());
        }
        sb.append(" value=").append(this.m_value);
        return sb.toString();
    }
    
    private static String getVirtualPropertyName(final Property p, final String field) {
        final StringBuilder sb = new StringBuilder();
        sb.append(p.m_name).append("#").append(field);
        return sb.toString();
    }
    
    public boolean hasParent() {
        return this.m_parent != null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Property.class);
        Property.DEBUG = false;
    }
    
    public enum PropertySetMethod
    {
        SET, 
        APPEND, 
        PREPEND;
    }
    
    protected static class AsynchronousPropertyChange
    {
        final PropertyClientData<BasicElement> m_client;
        Object m_value;
        @NotNull
        final PropertySetMethod m_method;
        
        protected AsynchronousPropertyChange(final PropertyClientData<BasicElement> client, final Object value, final PropertySetMethod method) {
            super();
            (this.m_client = client).setPropertyChange(this);
            this.m_value = ((this.m_client.getResultProvider() == null) ? value : this.m_client.getResultProvider().getResult(value));
            this.m_method = method;
        }
    }
}
