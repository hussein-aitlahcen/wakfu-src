package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import gnu.trove.*;

public abstract class UIAbstractEffectDetailFrame implements MessageFrame
{
    protected String m_lastDetailDialogId;
    private DialogUnloadListener m_dialogUnloadListener;
    protected TIntObjectHashMap<TIntObjectHashMap<EffectFieldProvider>> m_effectClientsCache;
    
    public UIAbstractEffectDetailFrame() {
        super();
        this.m_effectClientsCache = new TIntObjectHashMap<TIntObjectHashMap<EffectFieldProvider>>();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    protected void openDetailDialog(final EffectFieldProvider fieldProvider, final String fromDialogId, final int spellId) {
        final short stateId = fieldProvider.getBaseId();
        final String dialogId = this.createDialogId(stateId);
        if (Xulor.getInstance().isLoaded(dialogId)) {
            Xulor.getInstance().unload(dialogId);
        }
        else {
            Xulor.getInstance().loadAsMultiple(dialogId, Dialogs.getDialogPath("stateDetailDialog"), (this.m_lastDetailDialogId == null) ? fromDialogId : this.m_lastDetailDialogId, fromDialogId, "stateDetailDialog", 1L, (short)10000);
            this.m_lastDetailDialogId = dialogId;
            if (spellId != -1) {
                TIntObjectHashMap<EffectFieldProvider> map = this.m_effectClientsCache.get(spellId);
                if (map == null) {
                    map = new TIntObjectHashMap<EffectFieldProvider>();
                    this.m_effectClientsCache.put(spellId, map);
                }
                if (!map.containsKey(stateId)) {
                    map.put(stateId, fieldProvider);
                }
            }
            PropertiesProvider.getInstance().setLocalPropertyValue("describedState", fieldProvider, dialogId);
            PropertiesProvider.getInstance().setLocalPropertyValue("describedStateLevel", fieldProvider.getLevel(), dialogId);
        }
    }
    
    public void clearClientsCache() {
        this.m_effectClientsCache.clear();
    }
    
    protected String createDialogId(final short effectId) {
        return this.getDialogId() + effectId;
    }
    
    protected abstract String getDialogId();
    
    public abstract void updateEffectForSpell(final int p0, final int p1, final short p2);
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (!id.startsWith(UIAbstractEffectDetailFrame.this.getDialogId())) {
                        return;
                    }
                    if (id.equals(UIAbstractEffectDetailFrame.this.m_lastDetailDialogId)) {
                        UIAbstractEffectDetailFrame.this.m_lastDetailDialogId = null;
                    }
                    final short key = Short.parseShort(id.replaceAll(UIAbstractEffectDetailFrame.this.getDialogId(), ""));
                    UIAbstractEffectDetailFrame.this.removeEffectId(key);
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.stateDetails", StateDetailsActions.class);
        }
    }
    
    private void removeEffectId(final short key) {
        final TIntObjectIterator<TIntObjectHashMap<EffectFieldProvider>> it = this.m_effectClientsCache.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().remove(key);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            this.clearClientsCache();
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
}
