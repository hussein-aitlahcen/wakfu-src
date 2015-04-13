package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public final class CompanionActivationItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_companionBreedId;
    
    CompanionActivationItemAction(final int id) {
        super(id);
    }
    
    public int getCompanionBreedId() {
        return this.m_companionBreedId;
    }
    
    @Override
    public void parseParameters(final String[] params) {
        if (params.length != 1) {
            CompanionActivationItemAction.m_logger.error((Object)"[LD] Mauvais nombre de param\u00e8tres sur une action d'item");
            return;
        }
        this.m_companionBreedId = Integer.parseInt(params[0]);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            CompanionActivationItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.activateCompanion", item.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    CompanionActivationItemAction.this.sendRequest(item.getUniqueId());
                }
            }
        });
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.COMPANION_ACTIVATION;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionActivationItemAction.class);
    }
}
