package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;

public final class SpellCastNotificationAction extends AbstractFightTimedAction
{
    private long m_casterId;
    private int m_spellRefId;
    private boolean m_criticalHit;
    private boolean m_criticalMiss;
    private RawSpellLevel m_rawSpellLevel;
    private int m_x;
    private int m_y;
    private int m_z;
    
    public SpellCastNotificationAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.m_casterId);
        if (fighter == null) {
            return 0L;
        }
        final SpellLevel spellLevel = SpellCastAction.extractSpellLevel(fighter, this.m_rawSpellLevel);
        if (spellLevel == null) {
            return 0L;
        }
        if (this.consernLocalPlayer()) {
            final Fight fight = fighter.getCurrentFight();
            if (fight != null) {
                fighter.getSpellLevelCastHistory().storeSpellCastForTargets(spellLevel, fight.getTimeline().getCurrentTableturn(), fight.getPossibleTargetsAtPosition(this.m_x, this.m_y, this.m_z));
            }
            final TextWidgetFormater chatMsg = this.createBaseChatFeedback();
            if (chatMsg != null) {
                SpellCastNotificationAction.m_fightLogger.info(chatMsg.finishAndToString());
            }
            return 0L;
        }
        return 0L;
    }
    
    @Nullable
    private TextWidgetFormater createBaseChatFeedback() {
        final CharacterInfo caster = this.getFighterById(this.m_casterId);
        final Spell spell = SpellManager.getInstance().getSpell(this.m_spellRefId);
        if (spell == null) {
            SpellCastNotificationAction.m_logger.error((Object)("Sort inconnu " + this.m_spellRefId));
            return null;
        }
        final TextWidgetFormater f = new TextWidgetFormater().openText().addColor(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR);
        if (spell.isSramShadowSpell() && caster.isInvisible() && !caster.isLocalPlayer()) {
            f.append(WakfuTranslator.getInstance().getString("fight.spellCast.shadow", new TextWidgetFormater().b().append(caster.getControllerName())._b().finishAndToString()));
        }
        else {
            f.append(WakfuTranslator.getInstance().getString("fight.spellCast", new TextWidgetFormater().b().append(caster.getControllerName())._b().finishAndToString(), new TextWidgetFormater().b().append(spell.getName()).finishAndToString()));
        }
        f.closeText();
        if (this.m_criticalHit) {
            this.appendCriticalHitChatFeedback(f);
        }
        else if (this.m_criticalMiss) {
            this.appendCriticalMissChatFeedback(f);
        }
        return f;
    }
    
    private void appendCriticalMissChatFeedback(final TextWidgetFormater f) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId() == this.getFight().getId()) {
            f.b().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR);
            f.append(" (").append(WakfuTranslator.getInstance().getString("critical.miss")).append(")");
        }
    }
    
    private void appendCriticalHitChatFeedback(final TextWidgetFormater f) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId() == this.getFight().getId()) {
            f.openText().addColor(ChatConstants.CHAT_FIGHT_EFFECT_COLOR);
            f.append(" (").append(WakfuTranslator.getInstance().getString("critical")).append(")");
            f.closeText();
        }
    }
    
    public void setCasterId(final long casterId) {
        this.m_casterId = casterId;
    }
    
    public void setSpellRefId(final int spellRefId) {
        this.m_spellRefId = spellRefId;
    }
    
    public void setCriticalHit(final boolean criticalHit) {
        this.m_criticalHit = criticalHit;
    }
    
    public void setCriticalMiss(final boolean criticalMiss) {
        this.m_criticalMiss = criticalMiss;
    }
    
    public void setRawSpellLevel(final RawSpellLevel rawSpellLevel) {
        this.m_rawSpellLevel = rawSpellLevel;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public void setZ(final int z) {
        this.m_z = z;
    }
}
