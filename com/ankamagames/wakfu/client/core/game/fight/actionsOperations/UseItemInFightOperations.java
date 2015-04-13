package com.ankamagames.wakfu.client.core.game.fight.actionsOperations;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public final class UseItemInFightOperations
{
    private final FightInfo m_concernedFight;
    private final FighterItemUseMessage msg;
    private final boolean m_withChatFeedback;
    
    public UseItemInFightOperations(final FightInfo concernedFight, final FighterItemUseMessage msg, final boolean withChatFeedback) {
        super();
        this.m_concernedFight = concernedFight;
        this.msg = msg;
        this.m_withChatFeedback = withChatFeedback;
    }
    
    public void execute() {
        final CharacterInfo caster = this.m_concernedFight.getFighterFromId(this.msg.getUserId());
        if (caster == null) {
            return;
        }
        caster.getActor().clearActiveParticleSystem();
        final AbstractReferenceItem referenceItem = this.extractReferenceItem();
        if (this.m_withChatFeedback) {
            this.chatFeedback(caster, referenceItem);
        }
        this.createAndAddActionToGroup(caster, referenceItem);
    }
    
    private AbstractReferenceItem extractReferenceItem() {
        int refId;
        if (this.msg.getItemReferenceId() == -1) {
            refId = 2145;
        }
        else {
            refId = this.msg.getItemReferenceId();
        }
        return ReferenceItemManager.getInstance().getReferenceItem(refId);
    }
    
    private void createAndAddActionToGroup(final CharacterInfo caster, final AbstractReferenceItem referenceItem) {
        final AbstractItemType itemType = referenceItem.getItemType();
        final int weaponTypeId = WeaponAttack.getWeaponTypeId(itemType);
        final ReferenceSkill refSkill = ReferenceSkillManager.getInstance().getReferenceSkillByItemType(SkillType.WEAPON_SKILL, (short)weaponTypeId);
        if (refSkill == null) {
            return;
        }
        final int actionTypeId = this.msg.getFightActionType().getId();
        SpellLevel spellLevel = null;
        if (this.msg.isAssociatedWithSpell()) {
            if (caster.getSpellInventory() != null) {
                spellLevel = caster.getSpellInventory().getContentProvider().unSerializeContent(this.msg.getSerializedSpellLevel());
            }
            else {
                final SpellLevelProvider spellLevelProvider = new SpellLevelProvider(caster);
                spellLevel = spellLevelProvider.unSerializeContent(this.msg.getSerializedSpellLevel());
            }
        }
        if (spellLevel == null) {
            WeaponAnimHelper.addWeaponAnimationAction(this.msg.getUniqueId(), this.msg.getActionId(), this.m_concernedFight.getId(), caster, itemType, referenceItem.getGfxId());
        }
        final WeaponUseAction weaponUseAction = new WeaponUseAction(this.msg.getUniqueId(), actionTypeId, this.msg.getActionId(), this.m_concernedFight.getId(), refSkill, this.msg.isCriticalHit(), this.msg.isCriticalMiss(), this.msg.getUserId(), this.msg.getUsePositionX(), this.msg.getUsePositionY(), this.msg.getUsePositionZ(), this.msg.getItemReferenceId(), this.msg.getLevel(), spellLevel);
        final ActionGroup group = FightActionGroupManager.getInstance().addActionToPendingGroup(this.m_concernedFight.getId(), weaponUseAction);
        weaponUseAction.addJavaFunctionsLibrary(new WakfuScriptedActionFunctionsLibrary(group), new ScriptedActionFunctionsLibrary(group));
    }
    
    private void chatFeedback(final CharacterInfo caster, final AbstractReferenceItem referenceItem) {
        final TextWidgetFormater f = new TextWidgetFormater().openText().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR);
        f.append(WakfuTranslator.getInstance().getString("fight.itemUse", new TextWidgetFormater().b().append(caster.getControllerName())._b().finishAndToString(), new TextWidgetFormater().b().append(referenceItem.getName()).finishAndToString()));
        f.closeText();
        final ChatMessage chatMessage = new ChatMessage(f.toString());
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
}
