package com.ankamagames.xulor2.core.dialogclose;

import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class AbstractDialogClosesManager
{
    public static final String MRU_ID = "MRU";
    private final HashSet<String> m_unremoveableDialogs;
    private final LinkedList<String> m_elementMapsId;
    private final ArrayList<DialogCloseRequestListener> m_listeners;
    
    public AbstractDialogClosesManager() {
        super();
        this.m_unremoveableDialogs = new HashSet<String>();
        this.m_elementMapsId = new LinkedList<String>();
        this.m_listeners = new ArrayList<DialogCloseRequestListener>();
    }
    
    public void addUnremovableDialog(final String id) {
        this.m_unremoveableDialogs.add(id);
    }
    
    public void removeUnremovableDialog(final String id) {
        this.m_unremoveableDialogs.remove(id);
    }
    
    public void onDialogUnload(final String elementMapId) {
        this.removeUnremovableDialog(elementMapId);
        this.m_elementMapsId.remove(elementMapId);
    }
    
    public void addDialogCloseRequestListener(final DialogCloseRequestListener l) {
        if (l != null) {
            this.m_listeners.add(l);
        }
    }
    
    public void removeDialogCloseRequestListener(final DialogCloseRequestListener l) {
        if (l != null) {
            this.m_listeners.remove(l);
        }
    }
    
    public void pushElementMap(final String elementMapId, final boolean force) {
        if (elementMapId != null && !this.m_elementMapsId.contains(elementMapId)) {
            this.m_elementMapsId.addFirst(elementMapId);
        }
        this.recompute();
    }
    
    private void recompute() {
        final ArrayList<String> elementMapIds = new ArrayList<String>(this.m_elementMapsId.size());
        final Iterator<Widget> it = MasterRootContainer.getInstance().getLayeredContainer().getAllWidgetIterator();
        while (it.hasNext()) {
            final Widget widget = it.next();
            if (widget.getElementMap() == null) {
                continue;
            }
            final String id = widget.getElementMap().getId();
            if (!this.m_elementMapsId.contains(id)) {
                continue;
            }
            elementMapIds.add(id);
        }
        this.m_elementMapsId.clear();
        this.m_elementMapsId.addAll(elementMapIds);
    }
    
    public int size() {
        return this.m_elementMapsId.size();
    }
    
    public int closeWindow() {
        int index;
        for (int lastIndex = index = this.m_elementMapsId.size() - 1; index >= 0; --index) {
            final String elementMapId = this.m_elementMapsId.remove(index);
            if (elementMapId != null) {
                if (Xulor.getInstance().isLoaded(elementMapId)) {
                    boolean skip = false;
                    for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
                        final int returnValue = this.m_listeners.get(i).onDialogCloseRequest(elementMapId);
                        switch (returnValue) {
                            case 0: {
                                break;
                            }
                            case 3: {
                                skip = true;
                                this.m_elementMapsId.addLast(elementMapId);
                                break;
                            }
                            default: {
                                this.m_elementMapsId.addFirst(elementMapId);
                                return returnValue;
                            }
                        }
                    }
                    if (!skip && !this.isUnclosable(elementMapId)) {
                        Xulor.getInstance().unload(elementMapId);
                        return 0;
                    }
                }
            }
        }
        return 1;
    }
    
    public boolean isUnclosable(final String id) {
        return id.startsWith("MRU") || this.m_unremoveableDialogs.contains(id);
    }
}
