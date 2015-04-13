package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class HoodedMonsterFightEventListener implements FightEventListener
{
    private static final Logger m_logger;
    public static final TIntArrayList HOODED_MONSTER_CUSTOM_SKINS;
    private final NonPlayerCharacter m_character;
    private boolean m_hoodedInFight;
    
    public static boolean isVisuallyHooded(final CharacterInfo info) {
        return HoodedMonsterFightEventListener.HOODED_MONSTER_CUSTOM_SKINS.contains(info.getGfxId());
    }
    
    public HoodedMonsterFightEventListener(final NonPlayerCharacter character) {
        super();
        this.m_character = character;
    }
    
    @Override
    public void onControllerEvent(final int eventId, final Object param) {
        switch (eventId) {
            case 301: {
                this.applyHood();
                this.showPouf();
                break;
            }
            case 300: {
                this.m_hoodedInFight = true;
                break;
            }
        }
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent fightEventType) {
        switch (fightEventType.getId()) {
            case 101: {
                if (this.m_hoodedInFight) {
                    this.m_hoodedInFight = false;
                    final UnapplySkinAction action = new UnapplySkinAction(TimedAction.getNextUid(), FightActionType.UNAPPLY_SKIN_ACTION.getId(), 0, this.m_character);
                    FightActionGroupManager.getInstance().addActionToPendingGroup(this.m_character.getCurrentFight(), action);
                    this.showPouf();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void addProperty(final PropertyType property) {
        if (property != WorldPropertyType.HOODED_MONSTER) {
            return;
        }
        this.applyHood();
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
        if (property != WorldPropertyType.HOODED_MONSTER) {
            return;
        }
        this.removeHood();
        this.m_character.setFightEventListener(null);
    }
    
    private void removeHood() {
        final CharacterActor actor = this.m_character.getActor();
        this.m_character.setForcedGfxId(0);
        this.m_character.refreshDisplayEquipment();
        actor.onAnmLoaded(actor.getAnm(), new Runnable() {
            @Override
            public void run() {
                actor.getCurrentAttack().startUsageAndNotify(actor);
            }
        });
    }
    
    private void applyHood() {
        final CharacterActor actor = this.m_character.getActor();
        actor.getCurrentAttack().endUsage(actor);
        actor.setStaticAnimationKey("AnimStatique");
        actor.setAnimation("AnimTransEffect-Debut");
        this.m_character.refreshDisplayEquipment();
        final int customIndex = (int)(Math.abs(this.m_character.getId()) % HoodedMonsterFightEventListener.HOODED_MONSTER_CUSTOM_SKINS.size());
        this.m_character.setForcedGfxId(HoodedMonsterFightEventListener.HOODED_MONSTER_CUSTOM_SKINS.get(customIndex));
    }
    
    private void showPouf() {
        if (this.m_character.isDead()) {
            return;
        }
        final FreeParticleSystem aps = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800340);
        if (aps == null) {
            HoodedMonsterFightEventListener.m_logger.error((Object)"pas d'aps 800340");
            return;
        }
        aps.setFightId(this.m_character.getCurrentFightId());
        aps.setPosition(this.m_character.getWorldCellX(), this.m_character.getWorldCellY(), this.m_character.getWorldCellAltitude());
        IsoParticleSystemManager.getInstance().addParticleSystem(aps);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HoodedMonsterFightEventListener.class);
        (HOODED_MONSTER_CUSTOM_SKINS = new TIntArrayList()).add(129402237);
        HoodedMonsterFightEventListener.HOODED_MONSTER_CUSTOM_SKINS.add(129402238);
        HoodedMonsterFightEventListener.HOODED_MONSTER_CUSTOM_SKINS.add(129402239);
    }
}
