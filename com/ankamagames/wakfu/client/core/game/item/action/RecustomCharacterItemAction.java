package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class RecustomCharacterItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public RecustomCharacterItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            RecustomCharacterItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final MessageBoxData data = new MessageBoxData(102, 1, WakfuTranslator.getInstance().getString("recustomItem.confirmUse"), 2072L);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    RecustomCharacterItemAction.this.sendRequest(item.getUniqueId());
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
        return ItemActionConstants.RECUSTOM;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RecustomCharacterItemAction.class);
    }
}
