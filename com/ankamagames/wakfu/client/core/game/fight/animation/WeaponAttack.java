package com.ankamagames.wakfu.client.core.game.fight.animation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.alea.animation.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public class WeaponAttack extends AttackType
{
    private static final Logger m_logger;
    private static final int ID_TYPE_WEAPON = 100;
    public static final int ID_TYPE_TOOLS = 237;
    private final int m_weaponTypeId;
    private final boolean m_useTwoHands;
    private final String[] ANIM_WAITING;
    private boolean m_started;
    
    public WeaponAttack(final AbstractItemType itemType, final int weaponGfxId) {
        super();
        assert itemType != null;
        assert itemType.getEquipmentPositions() != null : "pas d'arme => utiliser plut\u00f4t BareHandAttack.getInstance()";
        assert itemType.getEquipmentPositions()[0] == EquipmentPosition.FIRST_WEAPON : "pas utilisation d'une arme";
        this.m_weaponGfxId = weaponGfxId;
        this.m_weaponTypeId = getWeaponTypeId(itemType);
        this.m_useTwoHands = false;
        assert this.m_weaponTypeId != 237;
        assert itemType.getLinkedPositions()[0] == EquipmentPosition.SECOND_WEAPON;
        final String animSuffix = this.getAnimSuffix();
        this.ANIM_WAITING = new String[] { "AnimStatique03-Fun" + animSuffix };
    }
    
    @Override
    public int getType() {
        return 1;
    }
    
    @Override
    public void endUsage(final CharacterActor actor) {
        super.endUsage(actor);
        this.m_started = false;
        actor.setAnimation(this.getEndUsageAnimation());
        actor.setHitAnimationKey("AnimHit");
    }
    
    @Override
    public void startUsage(final CharacterActor actor) {
        if (this.m_started) {
            return;
        }
        this.m_started = true;
        final String suffix = this.getAnimSuffix();
        actor.setAnimation(this.getStartUsageAnimation());
        actor.setStaticAnimationKey("AnimStatique03-Boucle" + suffix);
        if (this.m_weaponTypeId == 219) {
            actor.setHitAnimationKey("AnimHit");
        }
        else {
            actor.setHitAnimationKey("AnimHit" + suffix);
        }
        this.setMovementSelector(actor);
    }
    
    @Override
    public void setMovementSelector(final CharacterActor actor) {
        final String suffix = this.getAnimSuffix();
        PathMovementStyle movementStyle;
        if (this.m_weaponTypeId == 219) {
            movementStyle = RunMovementStyle.getInstance();
        }
        else {
            actor.setHitAnimationKey("AnimHit" + suffix);
            movementStyle = new WalkWithWeaponMovementStyle((short)this.m_weaponTypeId);
        }
        actor.setMovementSelector(new CustomMovementSelector(SimpleMovementSelector.getInstance(), movementStyle, RunMovementStyle.getInstance()));
    }
    
    private String getAnimSuffix() {
        return "-" + this.m_weaponTypeId + (this.m_useTwoHands ? "-2" : "");
    }
    
    @Override
    public boolean equals(final AttackType attack) {
        if (!super.equals(attack)) {
            return false;
        }
        final WeaponAttack weaponAttack = (WeaponAttack)attack;
        return this.m_weaponTypeId == weaponAttack.m_weaponTypeId && this.m_useTwoHands == weaponAttack.m_useTwoHands;
    }
    
    public static int getWeaponTypeId(final AbstractItemType itemType) {
        AbstractItemType parentType = itemType;
        while (!parentType.hasEquipmentPositions()) {
            parentType = parentType.getParentType();
            if (parentType == null) {
                WeaponAttack.m_logger.error((Object)("Arme pas dans la bonne cat\u00e9gorie " + itemType.getId()));
                return 0;
            }
        }
        return parentType.getId();
    }
    
    @Override
    protected String getEndUsageAnimation() {
        return "AnimStatique03-Fin" + this.getAnimSuffix();
    }
    
    @Override
    protected String getStartUsageAnimation() {
        return "AnimStatique03-Debut" + this.getAnimSuffix();
    }
    
    @Override
    public String getAnimTackle() {
        return "AnimHit" + this.getAnimSuffix();
    }
    
    @Override
    public String[] getAnimWaiting() {
        return this.ANIM_WAITING;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WeaponAttack.class);
    }
}
