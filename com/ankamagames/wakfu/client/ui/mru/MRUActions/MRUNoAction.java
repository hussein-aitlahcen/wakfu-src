package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.graphics.image.*;

public class MRUNoAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.NO_ACTION;
    }
    
    @Override
    public void run() {
    }
    
    @Override
    public boolean isRunnable() {
        return true;
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.getTooltipColor().getRGBtoHex());
        sb.append(WakfuTranslator.getInstance().getString(this.getTranslatorKey()));
        sb._b();
        return sb.finishAndToString();
    }
    
    @Override
    public boolean isEnabled() {
        return false;
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND.m_id;
    }
    
    public Color getTooltipColor() {
        return Color.RED;
    }
    
    @Override
    public String getTranslatorKey() {
        return "error.challenge.restriction";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUNoAction();
    }
}
