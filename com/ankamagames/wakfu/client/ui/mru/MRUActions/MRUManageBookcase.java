package com.ankamagames.wakfu.client.ui.mru.MRUActions;

public class MRUManageBookcase extends AbstractMRUBookcase
{
    @Override
    public MRUActions tag() {
        return MRUActions.MANAGE_BOOKCASE;
    }
    
    @Override
    public String getTranslatorKey() {
        return "manageBookcase";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUManageBookcase();
    }
}
