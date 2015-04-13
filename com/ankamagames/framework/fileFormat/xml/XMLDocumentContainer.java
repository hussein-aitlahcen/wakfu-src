package com.ankamagames.framework.fileFormat.xml;

import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;

public class XMLDocumentContainer implements DocumentContainer
{
    private XMLDocumentNode m_rootNode;
    private ArrayList<DocumentContainerEventsHandler> m_handlers;
    
    public XMLDocumentNode getRootNode() {
        return this.m_rootNode;
    }
    
    public void setRootNode(final XMLDocumentNode rootNode) {
        this.m_rootNode = rootNode;
    }
    
    @Override
    public DocumentEntry getEntryByName(final String name) {
        if (this.m_rootNode != null) {
            return this.m_rootNode.getChildByName(name);
        }
        return null;
    }
    
    @Override
    public ArrayList<DocumentEntry> getEntriesByName(final String name) {
        if (this.m_rootNode != null) {
            return this.m_rootNode.getChildrenByName(name);
        }
        return null;
    }
    
    @Override
    public void addEventsHandler(final DocumentContainerEventsHandler handler) {
        if (this.m_handlers == null) {
            this.m_handlers = new ArrayList<DocumentContainerEventsHandler>();
        }
        if (!this.m_handlers.contains(handler)) {
            this.m_handlers.add(handler);
        }
    }
    
    @Override
    public void notifyOnLoadBegin() {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onLoadBegin(this);
        }
    }
    
    @Override
    public void notifyOnLoadComplete() {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onLoadComplete(this);
        }
    }
    
    @Override
    public void notifyOnLoadError(final String errorMessage) {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onLoadError(this, errorMessage);
        }
    }
    
    @Override
    public void notifyOnSaveBegin() {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onSaveBegin(this);
        }
    }
    
    @Override
    public void notifyOnSaveComplete() {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onSaveComplete(this);
        }
    }
    
    @Override
    public void notifyOnSaveError(final String errorMessage) {
        if (this.m_handlers == null) {
            return;
        }
        for (int i = 0, size = this.m_handlers.size(); i < size; ++i) {
            this.m_handlers.get(i).onSaveError(this, errorMessage);
        }
    }
}
