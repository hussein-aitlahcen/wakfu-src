package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TauntAction extends TimedAction implements Releasable
{
    private ArrayList<CharacterInfo> m_characterList;
    private CharacterInfo m_character;
    private static final MonitoredPool m_staticPool;
    
    public static TauntAction checkout(final int uniqueId, final int actionType, final int actionId, final Collection<CharacterInfo> characterList) {
        try {
            final TauntAction tauntAction = (TauntAction)TauntAction.m_staticPool.borrowObject();
            tauntAction.setUniqueId(uniqueId);
            tauntAction.setActionType(actionType);
            tauntAction.setActionId(actionId);
            tauntAction.m_characterList = new ArrayList<CharacterInfo>(characterList);
            return tauntAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    public static TauntAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterInfo character) {
        try {
            final TauntAction tauntAction = (TauntAction)TauntAction.m_staticPool.borrowObject();
            tauntAction.setUniqueId(uniqueId);
            tauntAction.setActionType(actionType);
            tauntAction.setActionId(actionId);
            tauntAction.m_character = character;
            return tauntAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            TauntAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            TauntAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_characterList = null;
        this.m_character = null;
    }
    
    private TauntAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        int duration = 75;
        if (this.m_characterList != null && this.m_characterList.size() > 0) {
            for (int i = this.m_characterList.size() - 1; i >= 0; --i) {
                final CharacterInfo character = this.m_characterList.get(i);
                final int d = this.prepareCharacter(character);
                if (d > duration) {
                    duration = d;
                }
            }
        }
        if (this.m_character != null) {
            final int d2 = this.prepareCharacter(this.m_character);
            if (d2 > duration) {
                duration = d2;
            }
        }
        return duration;
    }
    
    private int prepareCharacter(final CharacterInfo character) {
        if (character instanceof NonPlayerCharacter) {
            final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)character;
            final CharacterActor actor = nonPlayerCharacter.getActor();
            actor.addCrossSwordParticleSystem((byte)(-1));
            return WeaponAnimHelper.prepareAnimForFight(character);
        }
        if (character instanceof PlayerCharacter) {
            if (character == WakfuGameEntity.getInstance().getLocalPlayer()) {
                WakfuSoundManager.getInstance().playFightTaunt();
            }
            character.getActor().addTauntParticleSystem();
            character.changeToSpellAttackIfNecessary();
            return 0;
        }
        return 0;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<TauntAction>() {
            @Override
            public TauntAction makeObject() {
                return new TauntAction(null);
            }
        });
    }
}
