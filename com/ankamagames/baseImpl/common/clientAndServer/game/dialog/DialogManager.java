package com.ankamagames.baseImpl.common.clientAndServer.game.dialog;

import gnu.trove.*;

public class DialogManager
{
    public static final DialogManager INSTANCE;
    private final TIntObjectHashMap<Dialog> m_dialogs;
    
    private DialogManager() {
        super();
        this.m_dialogs = new TIntObjectHashMap<Dialog>();
    }
    
    public void addDialog(final Dialog dialog) {
        this.m_dialogs.put(dialog.getId(), dialog);
    }
    
    public <D extends Dialog> D getDialog(final int dialogId) {
        return (D)this.m_dialogs.get(dialogId);
    }
    
    @Override
    public String toString() {
        return "DialogManager{m_dialogs=" + this.m_dialogs.size() + '}';
    }
    
    static {
        INSTANCE = new DialogManager();
    }
}
