package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public abstract class AbstractSwitch extends AbstractStateAppearanceElement implements Switch
{
    private static Logger m_logger;
    private boolean m_decoratorSwitch;
    public static final int DECORATOR_SWITCH_HASH;
    
    @Override
    public boolean isDecoratorSwitch() {
        return this.m_decoratorSwitch;
    }
    
    @Override
    public void setDecoratorSwitch(final boolean deco) {
        this.m_decoratorSwitch = deco;
    }
    
    @Override
    public abstract void setup(final SwitchClient p0);
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_decoratorSwitch = false;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final AbstractSwitch sw = (AbstractSwitch)source;
        sw.setDecoratorSwitch(this.m_decoratorSwitch);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AbstractSwitch.DECORATOR_SWITCH_HASH) {
            this.setDecoratorSwitch(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractSwitch.DECORATOR_SWITCH_HASH) {
            this.setDecoratorSwitch(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        AbstractSwitch.m_logger = Logger.getLogger((Class)AbstractSwitch.class);
        DECORATOR_SWITCH_HASH = "decoratorSwitch".hashCode();
    }
}
