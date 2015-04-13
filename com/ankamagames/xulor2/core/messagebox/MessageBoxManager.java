package com.ankamagames.xulor2.core.messagebox;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.utils.*;

public final class MessageBoxManager
{
    private static final MessageBoxManager m_instance;
    private final TIntObjectHashMap<ArrayList<MessageBoxControler>> m_controlers;
    
    private MessageBoxManager() {
        super();
        this.m_controlers = new TIntObjectHashMap<ArrayList<MessageBoxControler>>();
    }
    
    public static MessageBoxManager getInstance() {
        return MessageBoxManager.m_instance;
    }
    
    public MessageBoxControler msgBox(final MessageBoxData message) {
        if (message.canBeIgnored()) {
            final ArrayList<MessageBoxControler> controlers = this.getControlersFromCategory(message.getCategory());
            if (controlers != null) {
                for (int i = 0, size = controlers.size(); i < size; ++i) {
                    final MessageBoxControler regControler = controlers.get(i);
                    if (regControler.getLevel() >= message.getLevel()) {
                        return null;
                    }
                    if (regControler.canBeIgnored()) {
                        regControler.cleanUpAndRemove();
                        break;
                    }
                }
            }
        }
        final MessageBoxControler controler = Xulor.getInstance().messageBox(message);
        this.addControler(controler);
        return controler;
    }
    
    private void addControler(final MessageBoxControler controler) {
        TroveUtils.insert(this.m_controlers, controler.getCategory(), controler);
    }
    
    public void removeControler(final MessageBoxControler controler) {
        final ArrayList<MessageBoxControler> messageBoxControlers = this.m_controlers.get(controler.getCategory());
        if (messageBoxControlers == null) {
            return;
        }
        messageBoxControlers.remove(controler);
    }
    
    private ArrayList<MessageBoxControler> getControlersFromCategory(final int category) {
        return this.m_controlers.get(category);
    }
    
    static {
        m_instance = new MessageBoxManager();
    }
}
