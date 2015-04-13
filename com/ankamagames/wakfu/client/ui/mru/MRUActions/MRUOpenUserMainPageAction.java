package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUOpenUserMainPageAction extends AbstractMRUAction
{
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUOpenUserMainPageAction();
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.OPEN_USER_MAIN_PAGE_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUOpenUserMainPageAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        UISpellsPageFrame.getInstance().loadUnloadSpellsPage();
    }
    
    @Override
    public boolean isRunnable() {
        return this.m_source instanceof PlayerCharacter && ((PlayerCharacter)this.m_source).isLocalPlayer() && !WakfuGameEntity.getInstance().getLocalPlayer().isWaitingForResult();
    }
    
    @Override
    public String getTranslatorKey() {
        if (WakfuGameEntity.getInstance().hasFrame(UISpellsPageFrame.getInstance())) {
            return "tabletClose";
        }
        return "tabletOpen";
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BOOK.m_id;
    }
}
