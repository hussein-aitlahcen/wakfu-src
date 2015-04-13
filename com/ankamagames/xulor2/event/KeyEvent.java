package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class KeyEvent extends InputEvent
{
    private static Logger m_logger;
    private static final ObjectPool m_pool;
    private int m_keyCode;
    private char m_keyChar;
    
    public char getKeyChar() {
        return this.m_keyChar;
    }
    
    public void setKeyChar(final char keyChar) {
        this.m_keyChar = keyChar;
    }
    
    public int getKeyCode() {
        return this.m_keyCode;
    }
    
    public void setKeyCode(final int keyCode) {
        this.m_keyCode = keyCode;
    }
    
    public static KeyEvent checkOut() {
        KeyEvent e;
        try {
            e = (KeyEvent)KeyEvent.m_pool.borrowObject();
            e.m_currentPool = KeyEvent.m_pool;
        }
        catch (Exception ex) {
            KeyEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new KeyEvent();
            e.onCheckOut();
        }
        return e;
    }
    
    @Override
    public void onCheckIn() {
        this.m_keyChar = '\uffff';
        this.m_keyCode = -1;
    }
    
    static {
        KeyEvent.m_logger = Logger.getLogger((Class)KeyEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent makeObject() {
                return new KeyEvent();
            }
        });
    }
}
