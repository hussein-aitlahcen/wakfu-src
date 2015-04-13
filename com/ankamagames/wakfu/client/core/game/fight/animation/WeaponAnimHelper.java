package com.ankamagames.wakfu.client.core.game.fight.animation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class WeaponAnimHelper
{
    private static final Logger m_logger;
    public static final int NO_WEAPON = 0;
    
    public static int startUsage(final CharacterActor actor, final AttackType currentAttack) {
        currentAttack.startUsageAndNotify(actor);
        return currentAttack.getStartUsageDuration(actor);
    }
    
    public static int endUsage(final CharacterActor actor, final AttackType currentAttack) {
        currentAttack.endUsage(actor);
        return currentAttack.getEndUsageDuration(actor);
    }
    
    public static void changeWeapon(final CharacterActor actor, final int oldWeapon, final int newWeapon) {
        if (newWeapon == oldWeapon) {
            return;
        }
        if (newWeapon != -1) {
            actor.unapplyEquipment(EquipmentPosition.FIRST_WEAPON.m_id);
            actor.applyEquipment(newWeapon, EquipmentPosition.FIRST_WEAPON.m_id, newWeapon != 2192145);
        }
    }
    
    public static int changeAttack(final CharacterActor actor, final AttackType newAttack) {
        final AttackType currentAttack = actor.getCurrentAttack();
        final int endUsageDuration = currentAttack.getEndUsageDuration(actor);
        if (endUsageDuration == 0) {
            currentAttack.endUsage(actor);
            actor.setCurrentAttack(newAttack);
            newAttack.startUsageAndNotify(actor);
        }
        else {
            actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    actor.removeAnimationEndedListener(this);
                    final int oldWeapon = actor.getCurrentAttack().getWeaponGfxId();
                    actor.setCurrentAttack(newAttack);
                    WeaponAnimHelper.changeWeapon(actor, oldWeapon, newAttack.getWeaponGfxId());
                    newAttack.startUsageAndNotify(actor);
                }
            });
            currentAttack.endUsage(actor);
        }
        return endUsageDuration + newAttack.getStartUsageDuration(actor);
    }
    
    public static void addSpellAnimationAction(final int uid, final int actionId, final int fightId, final CharacterInfo caster) {
        final AttackType attack = new SpellAttack(caster);
        addAnimationAction(uid, actionId, fightId, caster.getId(), attack);
    }
    
    public static void addWeaponAnimationAction(final int uid, final int actionId, final int fightId, final CharacterInfo caster, final AbstractItemType itemType, final int weaponGfxId) {
        final AttackType attack = new WeaponAttack(itemType, weaponGfxId);
        addAnimationAction(uid, actionId, fightId, caster.getId(), attack);
    }
    
    private static void addAnimationAction(final int uid, final int actionId, final int fightId, final long casterId, final AttackType currentAttack) {
        final AnimationAction animationAction = new AnimationAction(uid, FightActionType.ANIMATION.getId(), actionId, fightId, casterId, currentAttack);
        FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, animationAction);
    }
    
    public static void setAttackFromWeaponForFighter(final CharacterInfo fighter) {
        AttackType attack;
        if (fighter instanceof PlayerCharacter) {
            final TByteIntHashMap equipements = fighter.getEquipmentAppearance();
            int weaponId = (equipements != null) ? equipements.get(EquipmentPosition.FIRST_WEAPON.m_id) : 0;
            if (weaponId <= 0) {
                weaponId = 2145;
            }
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(weaponId);
            final AbstractItemType itemType = referenceItem.getItemType();
            final int weaponTypeId = WeaponAttack.getWeaponTypeId(itemType);
            if (weaponTypeId == 237) {
                attack = new SpellAttack(fighter);
            }
            else {
                final int weaponGfxId = referenceItem.getGfxId();
                attack = new WeaponAttack(itemType, weaponGfxId);
                changeWeapon(fighter.getActor(), -1, weaponGfxId);
            }
        }
        else {
            attack = new SpellAttack(fighter);
        }
        final CharacterActor actor = fighter.getActor();
        actor.setCurrentAttack(attack);
    }
    
    public static int prepareAnimForFight(final CharacterInfo character) {
        character.changeToSpellAttackIfNecessary();
        final CharacterActor actor = character.getActor();
        if (actor.getAnmInstance() == null) {
            actor.getCurrentAttack().startUsageAndNotify(actor);
            return 0;
        }
        int duration;
        if (actor.getAnmInstance().isReady()) {
            final AttackType currentAttack = actor.getCurrentAttack();
            duration = currentAttack.getStartUsageDuration(actor);
            currentAttack.startUsageAndNotify(actor);
        }
        else {
            actor.onAnmLoaded(new Runnable() {
                @Override
                public void run() {
                    actor.getCurrentAttack().startUsageAndNotify(actor);
                }
            });
            duration = 0;
        }
        return duration;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WeaponAnimHelper.class);
    }
}
