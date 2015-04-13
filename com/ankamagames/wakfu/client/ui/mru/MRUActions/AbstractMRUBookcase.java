package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public abstract class AbstractMRUBookcase extends EndPathListenerMRUAction
{
    @Override
    public boolean isRunnable() {
        return this.m_source instanceof Bookcase;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof Bookcase)) {
            return this.getTranslatorKey();
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(super.getLabel());
        return sb.finishAndToString();
    }
    
    @Override
    public String getTranslatorKey() {
        return "manageBookcase";
    }
    
    @Override
    protected void startUse() {
        UIBookcaseFrame.getInstance().setBookcase((Bookcase)this.m_source);
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BOOK.m_id;
    }
}
