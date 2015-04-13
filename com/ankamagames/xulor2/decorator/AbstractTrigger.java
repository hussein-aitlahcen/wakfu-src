package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class AbstractTrigger extends AbstractAppearanceElement implements Trigger
{
    private Events m_triggerAction;
    public static final int TRIGGER_ACTION_HASH;
    
    @Override
    public Events getTriggerAction() {
        return this.m_triggerAction;
    }
    
    @Override
    public void setTriggerAction(final Events triggerAction) {
        this.m_triggerAction = triggerAction;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_triggerAction = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AbstractTrigger.TRIGGER_ACTION_HASH) {
            this.setTriggerAction(Events.value(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractTrigger.TRIGGER_ACTION_HASH) {
            this.setTriggerAction((Events)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        TRIGGER_ACTION_HASH = "triggerAction".hashCode();
    }
}
