package com.ankamagames.wakfu.client.ui.mru.MRUActions;

public class MRUBrowseBookcase extends AbstractMRUBookcase
{
    @Override
    public MRUActions tag() {
        return MRUActions.BROWSE_BOOKCASE;
    }
    
    @Override
    public String getTranslatorKey() {
        return "browseBookcase";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUBrowseBookcase();
    }
}
