package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;

public class Alignment9Switch extends AbstractSwitch
{
    private static Logger m_logger;
    private Alignment9 m_align;
    
    public Alignment9Switch() {
        super();
        this.m_align = null;
    }
    
    @Override
    public void setup(final SwitchClient e) {
        if (e instanceof Alignment9Client) {
            ((Alignment9Client)e).setAlign(this.m_align);
        }
    }
    
    public void setAlignment(final Alignment9 align) {
        this.m_align = align;
    }
    
    public Alignment9 getAlignment() {
        return this.m_align;
    }
    
    @Override
    public void copyElement(final BasicElement e) {
        final Alignment9Switch a = (Alignment9Switch)e;
        super.copyElement(a);
        a.m_align = this.m_align;
    }
    
    static {
        Alignment9Switch.m_logger = Logger.getLogger((Class)Alignment9Switch.class);
    }
}
