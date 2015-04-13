package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
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
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterItemUseMessageHandler extends UsingFightMessageHandler<FighterItemUseMessage, FightInfo>
{
    private FighterItemUseMessage m_msg;
    private boolean m_withChatFeedback;
    
    FighterItemUseMessageHandler() {
        super();
        this.m_withChatFeedback = true;
    }
    
    public void setWithChatFeedback(final boolean withChatFeedback) {
        this.m_withChatFeedback = withChatFeedback;
    }
    
    @Override
    public boolean onMessage(final FighterItemUseMessage msg) {
        this.m_msg = msg;
        final CharacterInfo caster = this.m_concernedFight.getFighterFromId(msg.getUserId());
        if (caster == null) {
            return false;
        }
        caster.getActor().clearActiveParticleSystem();
        final AbstractReferenceItem referenceItem = this.extractReferenceItem();
        if (this.m_withChatFeedback) {
            this.chatFeedback(caster, referenceItem);
        }
        this.createAndAddActionToGroup(caster, referenceItem);
        return false;
    }
    
    private AbstractReferenceItem extractReferenceItem() {
        int refId;
        if (this.m_msg.getItemReferenceId() == -1) {
            refId = 2145;
        }
        else {
            refId = this.m_msg.getItemReferenceId();
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
        final int actionTypeId = this.m_msg.getFightActionType().getId();
        SpellLevel spellLevel = null;
        if (this.m_msg.isAssociatedWithSpell()) {
            if (caster.getSpellInventory() != null) {
                spellLevel = caster.getSpellInventory().getContentProvider().unSerializeContent(this.m_msg.getSerializedSpellLevel());
            }
            else {
                final SpellLevelProvider spellLevelProvider = new SpellLevelProvider(caster);
                spellLevel = spellLevelProvider.unSerializeContent(this.m_msg.getSerializedSpellLevel());
            }
        }
        if (spellLevel == null) {
            WeaponAnimHelper.addWeaponAnimationAction(this.m_msg.getUniqueId(), this.m_msg.getActionId(), this.m_concernedFight.getId(), caster, itemType, referenceItem.getGfxId());
        }
        final WeaponUseAction weaponUseAction = new WeaponUseAction(this.m_msg.getUniqueId(), actionTypeId, this.m_msg.getActionId(), this.m_concernedFight.getId(), refSkill, this.m_msg.isCriticalHit(), this.m_msg.isCriticalMiss(), this.m_msg.getUserId(), this.m_msg.getUsePositionX(), this.m_msg.getUsePositionY(), this.m_msg.getUsePositionZ(), this.m_msg.getItemReferenceId(), this.m_msg.getLevel(), spellLevel);
        final ActionGroup group = FightActionGroupManager.getInstance().addActionToPendingGroup(this.m_concernedFight.getId(), weaponUseAction);
        weaponUseAction.addJavaFunctionsLibrary(new WakfuScriptedActionFunctionsLibrary(group));
        weaponUseAction.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(group));
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
