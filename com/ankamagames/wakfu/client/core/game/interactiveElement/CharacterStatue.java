package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CharacterStatue extends WakfuClientMapInteractiveElement implements CharacterDataProvider
{
    private static final Logger m_logger;
    private CharacterStatueProvider m_provider;
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return (this.m_provider != null) ? this.m_provider.getSynchronizationPart() : BinarSerialPart.EMPTY;
    }
    
    public void initialize() {
        this.notifyChangesListeners();
        this.notifyViews();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return null;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public String getName() {
        final AbstractCharacterData characterData = this.getCharacterData();
        return (characterData == null) ? "" : characterData.getName();
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        final int nbParams = params.length;
        if (nbParams != 2 && nbParams != 4) {
            CharacterStatue.m_logger.error((Object)("[LevelDesign] La CharacterStatue " + this.m_id + " n'a pas le bon nombre de param\u00e8tres"));
            return;
        }
        if (nbParams == 2) {
            final String animName = params[0];
            final int equipmentGfxId = Integer.parseInt(params[1]);
            this.m_provider = new GovernorOpinionCharacterProvider(this, animName);
            super.initializeWithParameter();
        }
        else {
            final short instanceId = Short.parseShort(params[0]);
            final byte resultIndex = Byte.parseByte(params[1]);
            final String animName2 = params[2];
            final int equipmentGfxId2 = Integer.parseInt(params[3]);
            this.m_provider = new DungeonLadderCharacterProvider(this, animName2, equipmentGfxId2);
            super.initializeWithParameter();
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_provider = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_state = 1;
        this.setVisible(true);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.m_overHeadable = true;
        this.m_selectable = false;
        assert this.m_provider == null;
    }
    
    @Override
    public CharacterStatueProvider getStatueProvider() {
        return this.m_provider;
    }
    
    @Override
    public AbstractCharacterData getCharacterData() {
        return this.m_provider.getCharacterData();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterStatue.class);
    }
    
    public static class CharacterStatueFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            CharacterStatue table;
            try {
                table = (CharacterStatue)CharacterStatueFactory.m_pool.borrowObject();
                table.setPool(CharacterStatueFactory.m_pool);
            }
            catch (Exception e) {
                CharacterStatue.m_logger.error((Object)"Erreur lors de l'extraction d'une CharacterStatue du pool", (Throwable)e);
                table = new CharacterStatue();
            }
            return table;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<CharacterStatue>() {
                @Override
                public CharacterStatue makeObject() {
                    return new CharacterStatue();
                }
            });
        }
    }
}
