package com.ankamagames.baseImpl.common.clientAndServer.game.dialog;

import java.util.*;

public class Dialog
{
    private final int m_id;
    private final ArrayList<DialogChoice> m_choices;
    
    public Dialog(final int id) {
        super();
        this.m_choices = new ArrayList<DialogChoice>();
        this.m_id = id;
    }
    
    public void addChoice(final DialogChoice choice) {
        if (!this.m_choices.contains(choice)) {
            this.m_choices.add(choice);
        }
    }
    
    public <Choice extends DialogChoice> Choice getChoice(final int choiceId) {
        for (int i = 0; i < this.m_choices.size(); ++i) {
            final DialogChoice choice = this.m_choices.get(i);
            if (choice.getId() == choiceId) {
                return (Choice)choice;
            }
        }
        return null;
    }
    
    public int getDialogChoiceSize() {
        return this.m_choices.size();
    }
    
    public Iterator<DialogChoice> getDialogChoiceIterator() {
        return this.m_choices.iterator();
    }
    
    public int getId() {
        return this.m_id;
    }
}
