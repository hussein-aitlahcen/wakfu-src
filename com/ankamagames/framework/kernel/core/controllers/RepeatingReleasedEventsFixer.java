package com.ankamagames.framework.kernel.core.controllers;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class RepeatingReleasedEventsFixer implements AWTEventListener
{
    private static RepeatingReleasedEventsFixer m_repeatingReleasedEventsFixer;
    private final Map<Integer, ReleasedAction> _map;
    
    public RepeatingReleasedEventsFixer() {
        super();
        this._map = new HashMap<Integer, ReleasedAction>();
    }
    
    public static void install() {
        RepeatingReleasedEventsFixer.m_repeatingReleasedEventsFixer = new RepeatingReleasedEventsFixer();
        Toolkit.getDefaultToolkit().addAWTEventListener(RepeatingReleasedEventsFixer.m_repeatingReleasedEventsFixer, 8L);
    }
    
    public static void remove() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(RepeatingReleasedEventsFixer.m_repeatingReleasedEventsFixer);
    }
    
    @Override
    public void eventDispatched(final AWTEvent event) {
        assert event instanceof KeyEvent : "Shall only listen to KeyEvents, so no other events shall come here";
        assert assertEDT();
        if (event instanceof Reposted) {
            return;
        }
        if (event.getID() == 400) {
            return;
        }
        final KeyEvent keyEvent = (KeyEvent)event;
        if (keyEvent.isConsumed()) {
            return;
        }
        if (keyEvent.getID() == 402) {
            final Timer timer = new Timer(2, null);
            final ReleasedAction action = new ReleasedAction(keyEvent, timer);
            timer.addActionListener(action);
            timer.start();
            this._map.put(keyEvent.getKeyCode(), action);
            keyEvent.consume();
        }
        else {
            if (keyEvent.getID() != 401) {
                throw new AssertionError((Object)"All IDs should be covered.");
            }
            final ReleasedAction action2 = this._map.remove(keyEvent.getKeyCode());
            if (action2 != null) {
                action2.cancel();
            }
        }
    }
    
    private static boolean assertEDT() {
        if (!EventQueue.isDispatchThread()) {
            throw new AssertionError((Object)("Not EDT, but [" + Thread.currentThread() + "]."));
        }
        return true;
    }
    
    private class ReleasedAction implements ActionListener
    {
        private final KeyEvent _originalKeyEvent;
        private Timer _timer;
        
        ReleasedAction(final KeyEvent originalReleased, final Timer timer) {
            super();
            this._timer = timer;
            this._originalKeyEvent = originalReleased;
        }
        
        void cancel() {
            assert assertEDT();
            this._timer.stop();
            this._timer = null;
            RepeatingReleasedEventsFixer.this._map.remove(this._originalKeyEvent.getKeyCode());
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            assert assertEDT();
            if (this._timer == null) {
                return;
            }
            this.cancel();
            final KeyEvent newEvent = new RepostedKeyEvent((Component)this._originalKeyEvent.getSource(), this._originalKeyEvent.getID(), this._originalKeyEvent.getWhen(), this._originalKeyEvent.getModifiers(), this._originalKeyEvent.getKeyCode(), this._originalKeyEvent.getKeyChar(), this._originalKeyEvent.getKeyLocation());
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(newEvent);
        }
    }
    
    public static class RepostedKeyEvent extends KeyEvent implements Reposted
    {
        public RepostedKeyEvent(final Component source, final int id, final long when, final int modifiers, final int keyCode, final char keyChar, final int keyLocation) {
            super(source, id, when, modifiers, keyCode, keyChar, keyLocation);
        }
    }
    
    public interface Reposted
    {
    }
}
