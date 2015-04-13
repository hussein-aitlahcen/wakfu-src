package com.ankamagames.xulor2.core.keyManager;

import gnu.trove.*;
import java.awt.event.*;
import java.util.*;

public class KeyManager
{
    private static KeyManager m_instance;
    private TIntObjectHashMap<KeyEvent> m_pressedKeys;
    private ArrayList<KeyManagerListener> m_keyListeners;
    
    private KeyManager() {
        super();
        this.m_pressedKeys = new TIntObjectHashMap<KeyEvent>();
        this.m_keyListeners = new ArrayList<KeyManagerListener>();
    }
    
    public static KeyManager getInstance() {
        return KeyManager.m_instance;
    }
    
    public KeyEvent[] getPressedKeys() {
        return this.m_pressedKeys.getValues(new KeyEvent[this.m_pressedKeys.size()]);
    }
    
    public boolean containsKey(final int keyCode) {
        return this.m_pressedKeys.containsKey(keyCode);
    }
    
    public boolean keyPressed(final KeyEvent e) {
        this.m_pressedKeys.put(e.getKeyCode(), e);
        this.m_pressedKeys.remove(e.getKeyCode());
        boolean ret = false;
        for (final KeyManagerListener keyManagerListener : this.m_keyListeners) {
            ret |= keyManagerListener.keyPressed(e);
        }
        return ret;
    }
    
    public boolean keyReleased(final KeyEvent e) {
        this.m_pressedKeys.remove(e.getKeyCode());
        boolean ret = false;
        for (final KeyManagerListener keyManagerListener : this.m_keyListeners) {
            ret |= keyManagerListener.keyReleased(e);
        }
        return ret;
    }
    
    public void addListener(final KeyManagerListener keyManagerListener) {
        if (!this.m_keyListeners.contains(keyManagerListener)) {
            this.m_keyListeners.add(keyManagerListener);
        }
    }
    
    public void removeListener(final KeyManagerListener keyManagerListener) {
        if (this.m_keyListeners.contains(keyManagerListener)) {
            this.m_keyListeners.remove(keyManagerListener);
        }
    }
    
    static {
        KeyManager.m_instance = new KeyManager();
    }
}
