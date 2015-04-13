package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUBackgroundDisplayAction extends MRUGenericInteractiveAction
{
    public MRUBackgroundDisplayAction(final String name, final int gfxId) {
        super();
        this.m_name = name;
        this.m_gfxId = gfxId;
    }
    
    public MRUBackgroundDisplayAction() {
        super();
    }
    
    @Override
    public MRUGenericInteractiveAction getCopy() {
        return new MRUBackgroundDisplayAction(this.m_name, this.m_gfxId);
    }
    
    @Override
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUBackgroundDisplayAction.DEFAULT_TOOLTIP_COLOR : MRUBackgroundDisplayAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.m_name))._b();
        return sb.finishAndToString();
    }
}
