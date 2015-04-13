package com.ankamagames.xulor2.core;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import org.apache.commons.pool.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.core.factory.*;
import java.util.*;

public abstract class BasicElement implements Poolable
{
    private static final Logger m_logger;
    private static final boolean DEBUG_ATTRIBUTE_APPLICATION = true;
    protected ObjectPool m_currentPool;
    protected boolean m_unloading;
    private int m_checkInCount;
    private int m_checkOutCount;
    protected BasicElement m_basicParent;
    protected ArrayList<Property> m_registeredProperties;
    protected boolean m_needPreProcess;
    protected boolean m_needMiddleProcess;
    protected boolean m_needPostProcess;
    private int m_lastPreProcessFrame;
    private int m_lastMiddleProcessFrame;
    private int m_lastPostProcessFrame;
    protected boolean m_isATemplate;
    private boolean m_isInTreeDepthManager;
    private boolean m_isAddedNextInTreeDepthManager;
    public static final String INCLUDE = "include";
    public static final String INCLUDE_ID = "includeId";
    static final int[] IGNORED_ATTRIBUTES;
    
    public BasicElement() {
        super();
        this.m_unloading = false;
        this.m_checkInCount = 0;
        this.m_checkOutCount = 0;
        this.m_registeredProperties = null;
        this.m_needPreProcess = false;
        this.m_needMiddleProcess = false;
        this.m_needPostProcess = false;
        this.m_lastPreProcessFrame = -1;
        this.m_lastMiddleProcessFrame = -1;
        this.m_lastPostProcessFrame = -1;
        this.m_isATemplate = false;
        this.m_isInTreeDepthManager = false;
        this.m_isAddedNextInTreeDepthManager = false;
    }
    
    public void addProperty(final Property property) {
        if (this.m_registeredProperties == null) {
            this.m_registeredProperties = new ArrayList<Property>(1);
        }
        if (!this.m_registeredProperties.contains(property)) {
            this.m_registeredProperties.add(property);
        }
    }
    
    public void removeProperty(final Property property) {
        if (this.m_registeredProperties != null) {
            this.m_registeredProperties.remove(property);
        }
    }
    
    public void add(final DataElement element) {
        element.setBasicParent(this);
    }
    
    public void add(final EventDispatcher element) {
        element.setBasicParent(this);
    }
    
    public void addFromXML(final DataElement element) {
        this.add(element);
    }
    
    public void addFromXML(final EventDispatcher element) {
        this.add(element);
    }
    
    public void addBasicElement(final BasicElement element) {
        element.setBasicParent(this);
        switch (element.getElementType()) {
            case EVENT_DISPATCHER: {
                this.addFromXML((EventDispatcher)element);
                break;
            }
            case DATA: {
                this.addFromXML((DataElement)element);
                break;
            }
        }
    }
    
    public int getLastPreProcessFrame() {
        return this.m_lastPreProcessFrame;
    }
    
    public void setLastPreProcessFrame(final int lastPreProcessFrame) {
        this.m_lastPreProcessFrame = lastPreProcessFrame;
    }
    
    public int getLastMiddleProcessFrame() {
        return this.m_lastMiddleProcessFrame;
    }
    
    public void setLastMiddleProcessFrame(final int lastMiddleProcessFrame) {
        this.m_lastMiddleProcessFrame = lastMiddleProcessFrame;
    }
    
    public int getLastPostProcessFrame() {
        return this.m_lastPostProcessFrame;
    }
    
    public void setLastPostProcessFrame(final int lastPostProcessFrame) {
        this.m_lastPostProcessFrame = lastPostProcessFrame;
    }
    
    public void setNeedsToPreProcess() {
        if (!this.m_isATemplate && (TreeDepthManager.getInstance().isProcessing() || !this.m_needPreProcess)) {
            TreeDepthManager.getInstance().addToPreProcessList(this);
            this.m_needPreProcess = true;
        }
    }
    
    public void setNeedsToMiddleProcess() {
        if (!this.m_isATemplate && (TreeDepthManager.getInstance().isProcessing() || !this.m_needMiddleProcess)) {
            TreeDepthManager.getInstance().addToMiddleProcessList(this);
            this.m_needMiddleProcess = true;
        }
    }
    
    public void setNeedsToPostProcess() {
        if (!this.m_isATemplate && (TreeDepthManager.getInstance().isProcessing() || !this.m_needPostProcess)) {
            TreeDepthManager.getInstance().addToPostProcessList(this);
            this.m_needPostProcess = true;
        }
    }
    
    public <T> T getParentOfType(final Class<T> type) {
        if (this.m_basicParent == null) {
            return null;
        }
        if (type.isAssignableFrom(this.m_basicParent.getClass())) {
            return (T)this.m_basicParent;
        }
        return (T)this.m_basicParent.getParentOfType((Class<Object>)type);
    }
    
    public void setBasicParent(final BasicElement parent) {
        this.m_basicParent = parent;
    }
    
    public BasicElement getBasicParent() {
        return this.m_basicParent;
    }
    
    public boolean isUnloading() {
        return this.m_unloading;
    }
    
    public abstract ElementType getElementType();
    
    public static String getTag(final Class<? extends BasicElement> type) {
        try {
            return (String)type.getDeclaredField("TAG").get(null);
        }
        catch (Exception e) {
            BasicElement.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration du tag de " + type.getSimpleName()));
            return null;
        }
    }
    
    public String getTag() {
        return "";
    }
    
    public int getTreeDepth() {
        return (this.m_basicParent != null) ? this.m_basicParent.getTreeDepth() : 0;
    }
    
    public int getTreePosition() {
        return (this.m_basicParent != null) ? this.m_basicParent.getTreePosition() : 0;
    }
    
    public boolean isInTreeDepthManager() {
        return this.m_isInTreeDepthManager;
    }
    
    public void setInTreeDepthManager(final boolean isInTreeDepthManager) {
        this.m_isInTreeDepthManager = isInTreeDepthManager;
    }
    
    public boolean isAddedNextInTreeDepthManager() {
        return this.m_isAddedNextInTreeDepthManager;
    }
    
    public void setAddedNextInTreeDepthManager(final boolean addedNextInTreeDepthManager) {
        this.m_isAddedNextInTreeDepthManager = addedNextInTreeDepthManager;
    }
    
    public void setIsATemplate(final boolean isATemplate) {
        this.m_isATemplate |= isATemplate;
    }
    
    public boolean isATemplate() {
        return this.m_isATemplate;
    }
    
    public boolean setXMLAttribute(final String key, final String value) {
        final int hash = key.hashCode();
        for (final int ignored : BasicElement.IGNORED_ATTRIBUTES) {
            if (hash == ignored) {
                return true;
            }
        }
        if (!this.setXMLAttribute(hash, value, ConverterLibrary.getInstance())) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean appendXMLAttribute(final String key, final String value) {
        if (!this.appendXMLAttribute(key.hashCode(), value, ConverterLibrary.getInstance())) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean prependXMLAttribute(final String key, final String value) {
        if (!this.prependXMLAttribute(key.hashCode(), value, ConverterLibrary.getInstance())) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean setPropertyAttribute(final String key, final Object value) {
        if (!this.setPropertyAttribute(key.hashCode(), value)) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean appendPropertyAttribute(final String key, final Object value) {
        if (!this.appendPropertyAttribute(key.hashCode(), value)) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean prependPropertyAttribute(final String key, final Object value) {
        if (!this.prependPropertyAttribute(key.hashCode(), value)) {
            BasicElement.m_logger.debug((Object)new StringBuilder("Impossible de trouver l'attribut ").append(key).append(" pour ").append(this.getClass().getSimpleName()));
            return false;
        }
        return true;
    }
    
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        return false;
    }
    
    public boolean appendXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        return false;
    }
    
    public boolean prependXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        return false;
    }
    
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return false;
    }
    
    public boolean appendPropertyAttribute(final int hash, final Object value) {
        return false;
    }
    
    public boolean prependPropertyAttribute(final int hash, final Object value) {
        return false;
    }
    
    public boolean preProcess(final int deltaTime) {
        return false;
    }
    
    public boolean middleProcess(final int deltaTime) {
        return false;
    }
    
    public boolean postProcess(final int deltaTime) {
        return false;
    }
    
    public void doPreProcess(final int deltaTime) {
        if (this.m_unloading) {
            return;
        }
        if (this.m_needPreProcess) {
            this.m_needPreProcess = false;
            if (XulorScene.checkForProcessDuration()) {
                final boolean ret = this.preProcess(deltaTime);
                this.m_needPreProcess |= ret;
            }
            if (this.m_needPreProcess) {
                TreeDepthManager.getInstance().addToNextPreProcess(this);
            }
        }
    }
    
    public void doMiddleProcess(final int deltaTime) {
        if (this.m_unloading) {
            return;
        }
        if (this.m_needMiddleProcess) {
            this.m_needMiddleProcess = false;
            if (XulorScene.checkForProcessDuration()) {
                final boolean ret = this.middleProcess(deltaTime);
                this.m_needMiddleProcess |= ret;
            }
            if (this.m_needMiddleProcess) {
                TreeDepthManager.getInstance().addToNextMiddleProcess(this);
            }
        }
    }
    
    public void doPostProcess(final int deltaTime) {
        if (this.m_unloading) {
            return;
        }
        if (this.m_needPostProcess) {
            this.m_needPostProcess = false;
            if (XulorScene.checkForProcessDuration()) {
                final boolean ret = this.postProcess(deltaTime);
                this.m_needPostProcess |= ret;
            }
            if (this.m_needPostProcess) {
                TreeDepthManager.getInstance().addToNextPostProcess(this);
            }
        }
    }
    
    public void release() {
        try {
            if (this.m_currentPool != null) {
                this.m_currentPool.returnObject(this);
            }
            else {
                this.onCheckIn();
            }
        }
        catch (Exception e) {
            BasicElement.m_logger.warn((Object)"Probl\u00e8me lors du retour dans un pool", (Throwable)e);
            this.onCheckIn();
        }
    }
    
    public void onAttributesInitialized() {
    }
    
    public void onChildrenAdded() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_unloading = true;
        if (this.m_checkInCount >= this.m_checkOutCount) {
            BasicElement.m_logger.error((Object)("Le nombre de checkIn ne correspond pas aux nombre de checkOut " + this.getClass().getSimpleName()));
        }
        ++this.m_checkInCount;
        this.m_basicParent = null;
        if (this.m_registeredProperties != null) {
            for (int i = this.m_registeredProperties.size() - 1; i >= 0; --i) {
                this.m_registeredProperties.get(i).removePropertyClient(this);
            }
            this.m_registeredProperties = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_unloading = false;
        if (this.m_checkOutCount != this.m_checkInCount) {
            BasicElement.m_logger.error((Object)("Le nombre de checkOut ne correspond pas aux nombre de checkIn " + this.getClass().getSimpleName()));
        }
        ++this.m_checkOutCount;
        this.m_needPreProcess = false;
        this.m_needMiddleProcess = false;
        this.m_needPostProcess = false;
        this.m_lastPreProcessFrame = -1;
        this.m_lastMiddleProcessFrame = -1;
        this.m_lastPostProcessFrame = -1;
    }
    
    public void copyElement(final BasicElement source) {
    }
    
    public void applyAttributes(final DocumentEntry entry) {
        final ArrayList<? extends DocumentEntry> parameters = entry.getParameters();
        final int size = (parameters != null) ? parameters.size() : 0;
        final Factory<?> factory = XulorTagLibrary.getInstance().getFactory(entry.getName());
        for (int i = 0; i < size; ++i) {
            final DocumentEntry param = (DocumentEntry)parameters.get(i);
            final String paramName = param.getName();
            final String paramValue = param.getStringValue();
            if (!this.setXMLAttribute(paramName, paramValue)) {
                DocumentParser.applyAttributes(this, factory, paramName, paramValue);
            }
        }
        this.onAttributesInitialized();
    }
    
    public void preApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
    }
    
    public void postApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
    }
    
    public void preAddChildComputeDocumentEntry(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
    }
    
    public void postAddChildComputeDocumentEntry(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
    }
    
    public BasicElement getNewElement(final String name, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        final Factory<BasicElement> factory = (Factory<BasicElement>)XulorTagLibrary.getInstance().getFactory(name);
        if (factory == null) {
            BasicElement.m_logger.error((Object)("Tag Inconnu : " + name));
            return null;
        }
        try {
            return factory.newInstance();
        }
        catch (Exception e) {
            BasicElement.m_logger.error((Object)new StringBuilder("Erreur lors de l'instanciation du tag ").append(name).append("."), (Throwable)e);
            return null;
        }
    }
    
    public void computeDocumentEntry(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        final ArrayList<? extends DocumentEntry> children = entry.getChildren();
        final int childrenSize = children.size();
        final EventDispatcher nextParent = (EventDispatcher)((this.getElementType() == ElementType.EVENT_DISPATCHER) ? this : parent);
        this.preAddChildComputeDocumentEntry(entry, parent, elementMaps, env);
        for (int i = 0; i < childrenSize; ++i) {
            final DocumentEntry child = (DocumentEntry)children.get(i);
            final String childName = child.getName();
            if (!childName.equals("#text")) {
                if (!childName.equals("#comment")) {
                    final BasicElement newElement = this.getNewElement(childName, nextParent, elementMaps, env);
                    if (newElement != null) {
                        newElement.preApplyAttributes(child, parent, elementMaps, env);
                        newElement.applyAttributes(child);
                        newElement.postApplyAttributes(child, parent, elementMaps, env);
                        this.addBasicElement(newElement);
                        if (child.getParameterByName("include") != null) {
                            final String includeId = child.getParameterByName("includeId").getStringValue();
                            if (includeId == null) {
                                BasicElement.m_logger.error((Object)"Pas d'id pour le tag Include, impossible de l'ajouter");
                            }
                            else {
                                final ElementMap currentElementMap = elementMaps.peek();
                                final String elementMapId = (currentElementMap != null) ? currentElementMap.getId() : "";
                                final ElementMap includeElementMap = env.createElementMap(elementMapId + '.' + includeId);
                                includeElementMap.setParentElementMap(currentElementMap);
                                elementMaps.push(includeElementMap);
                                newElement.computeDocumentEntry(child, nextParent, elementMaps, env);
                                elementMaps.pop();
                            }
                        }
                        else {
                            newElement.computeDocumentEntry(child, nextParent, elementMaps, env);
                        }
                    }
                }
            }
        }
        this.onChildrenAdded();
        this.postAddChildComputeDocumentEntry(entry, parent, elementMaps, env);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BasicElement.class);
        IGNORED_ATTRIBUTES = new int[] { "include".hashCode(), "includeId".hashCode(), "templateId".hashCode(), "atlas".hashCode() };
    }
}
