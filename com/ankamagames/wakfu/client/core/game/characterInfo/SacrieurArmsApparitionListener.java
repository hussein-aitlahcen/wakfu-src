package com.ankamagames.wakfu.client.core.game.characterInfo;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.fileFormat.properties.*;

public final class SacrieurArmsApparitionListener
{
    private static final Logger m_logger;
    private static final int LOW_ARMS_GFX = 1042;
    private static final int UPPER_ARMS_GFX = 1043;
    private static final int MORT_PROCHE_REFERENCE_ID = 2121;
    public static final String UPPER_ARMS_APPARITION_ANIMATION = "AnimChrage1-Debut";
    public static final String LOW_ARMS_APPARITION_ANIMATION = "AnimChrage2-Debut";
    public static final String ALL_ARMS_APPARITION_ANIMATION = "AnimStatique02-Debut";
    public static final String UPPER_ARMS_DISPARITION_ANIMATION = "AnimChrage1-Fin";
    public static final String LOW_ARMS_DISPARITION_ANIMATION = "AnimChrage2-Fin";
    public static final String ALL_ARMS_DISPARITION_ANIMATION = "AnimStatique02-Fin";
    private static final int LEVEL_ONE_UNLOCK_HP_VALUE = 40;
    private static final int LEVEL_TWO_UNLOCK_HP_VALUE = 20;
    private int m_previousValue;
    private Anm m_levelOneAnm;
    private Anm m_levelTwoAnm;
    private boolean m_enabled;
    
    public SacrieurArmsApparitionListener() {
        super();
        this.m_previousValue = -1;
        this.m_enabled = true;
    }
    
    public void onHpModification(final CharacterInfo info) {
        if (!this.m_enabled) {
            return;
        }
        if (info.getBreedId() != AvatarBreed.SACRIER.getBreedId()) {
            return;
        }
        if (info.getSpellInventory() == null) {
            return;
        }
        final AbstractSpellLevel mortProche = info.getSpellInventory().getFirstWithReferenceId(2121);
        if (mortProche == null || mortProche.getLevel() <= 0) {
            return;
        }
        if (info.getCurrentFight() == null) {
            final FighterCharacteristic hp = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
            this.m_previousValue = hp.value() * 100 / hp.max();
            return;
        }
        FightActionGroupManager.getInstance().addActionToPendingGroup(info.getCurrentFightId(), new AbstractFightTimedAction(TimedAction.getNextUid(), 0, 0, info.getCurrentFightId()) {
            @Override
            protected long onRun() {
                return SacrieurArmsApparitionListener.this.modificationAnimations(info);
            }
        });
        FightActionGroupManager.getInstance().executePendingGroup(info.getCurrentFightId());
    }
    
    public void setPreviousValue(final int previousValue) {
        this.m_previousValue = previousValue;
    }
    
    private int modificationAnimations(final CharacterInfo info) {
        final FighterCharacteristic hpCharac = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        if (hpCharac.max() == 0) {
            return 0;
        }
        final int hpPercent = hpCharac.value() * 100 / hpCharac.max();
        int duration = 0;
        if (this.hpOverLevelOne(hpPercent)) {
            if (this.m_previousValue < 40) {
                if (this.m_previousValue < 20) {
                    duration = this.unapplyLevelOneAndTwoParts(info);
                }
                else {
                    duration = this.unapplyLevelOneParts(info);
                }
            }
        }
        else if (this.hpLevelOne(hpPercent) && !this.hpLevelOne(this.m_previousValue)) {
            if (this.hpLevelTwo(this.m_previousValue)) {
                duration = this.unapplyLevelTwoParts(info);
            }
            else {
                duration = this.applyLevelOneParts(info);
            }
        }
        else if (this.hpLevelTwo(hpPercent)) {
            if (this.hpLevelOne(this.m_previousValue)) {
                duration = this.applyLevelTwoParts(info);
            }
            else if (this.hpOverLevelOne(this.m_previousValue)) {
                duration = this.applyLevelOneAndTwoParts(info);
            }
        }
        this.m_previousValue = hpPercent;
        return duration;
    }
    
    private boolean hpOverLevelOne(final int value) {
        return value > 40;
    }
    
    private boolean hpLevelOne(final int value) {
        return value <= 40 && value > 20;
    }
    
    private boolean hpLevelTwo(final int value) {
        return value <= 20;
    }
    
    private int unapplyLevelTwoParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        if (actor.getCurrentAttack().getType() != 2) {
            actor.unapplyAllPartsFrom(this.m_levelTwoAnm);
            this.m_levelTwoAnm = null;
            return 0;
        }
        actor.setAnimation("AnimChrage2-Fin");
        actor.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                actor.unapplyAllPartsFrom(SacrieurArmsApparitionListener.this.m_levelTwoAnm);
                SacrieurArmsApparitionListener.this.m_levelTwoAnm = null;
                actor.removeAnimationEndedListener(this);
            }
        });
        return actor.getAnimationDuration("AnimChrage2-Fin");
    }
    
    private int unapplyLevelOneParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        if (actor.getCurrentAttack().getType() != 2) {
            actor.unapplyAllPartsFrom(this.m_levelOneAnm);
            this.m_levelOneAnm = null;
            return 0;
        }
        actor.setAnimation("AnimChrage1-Fin");
        actor.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                actor.unapplyAllPartsFrom(SacrieurArmsApparitionListener.this.m_levelOneAnm);
                SacrieurArmsApparitionListener.this.m_levelOneAnm = null;
                actor.removeAnimationEndedListener(this);
            }
        });
        return actor.getAnimationDuration("AnimChrage1-Fin");
    }
    
    private int unapplyLevelOneAndTwoParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        if (actor.getCurrentAttack().getType() != 2) {
            actor.unapplyAllPartsFrom(this.m_levelOneAnm);
            actor.unapplyAllPartsFrom(this.m_levelTwoAnm);
            this.m_levelOneAnm = null;
            this.m_levelTwoAnm = null;
            return 0;
        }
        actor.setAnimation("AnimStatique02-Fin");
        actor.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                actor.unapplyAllPartsFrom(SacrieurArmsApparitionListener.this.m_levelOneAnm);
                actor.unapplyAllPartsFrom(SacrieurArmsApparitionListener.this.m_levelTwoAnm);
                SacrieurArmsApparitionListener.this.m_levelOneAnm = null;
                SacrieurArmsApparitionListener.this.m_levelTwoAnm = null;
                actor.removeAnimationEndedListener(this);
            }
        });
        return actor.getAnimationDuration("AnimStatique02-Fin");
    }
    
    private int applyLevelOneParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        try {
            this.m_levelOneAnm = actor.applyAllPartsFrom(Actor.getGfxFile(1043));
        }
        catch (PropertyException e) {
            SacrieurArmsApparitionListener.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        if (actor.getCurrentAttack().getType() == 2) {
            actor.onAnmLoaded(this.m_levelOneAnm, new Runnable() {
                @Override
                public void run() {
                    actor.setAnimation("AnimChrage1-Debut");
                }
            });
            return actor.getAnimationDuration("AnimChrage1-Debut");
        }
        return 0;
    }
    
    private int applyLevelTwoParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        try {
            this.m_levelTwoAnm = actor.applyAllPartsFrom(Actor.getGfxFile(1042));
        }
        catch (PropertyException e) {
            SacrieurArmsApparitionListener.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        if (actor.getCurrentAttack().getType() == 2) {
            actor.setAnimation("AnimChrage2-Debut");
            return actor.getAnimationDuration("AnimChrage2-Debut");
        }
        return 0;
    }
    
    private int applyLevelOneAndTwoParts(final CharacterInfo info) {
        final CharacterActor actor = info.getActor();
        try {
            this.m_levelOneAnm = actor.applyAllPartsFrom(Actor.getGfxFile(1043));
            this.m_levelTwoAnm = actor.applyAllPartsFrom(Actor.getGfxFile(1042));
        }
        catch (PropertyException e) {
            SacrieurArmsApparitionListener.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        if (actor.getCurrentAttack().getType() == 2) {
            actor.setAnimation("AnimStatique02-Debut");
            return actor.getAnimationDuration("AnimStatique02-Debut");
        }
        return 0;
    }
    
    public void reset() {
        this.m_previousValue = -1;
    }
    
    public void disable() {
        this.m_enabled = false;
    }
    
    public void enable() {
        this.m_enabled = true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SacrieurArmsApparitionListener.class);
    }
}
