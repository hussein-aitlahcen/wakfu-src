package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;

public class SkillOrSpellXpModificationAction extends TimedAction
{
    private final long m_playerId;
    private final Collection<SkillOrSpellXpModification> m_xpModifications;
    private boolean m_hasSkillXpModification;
    
    private SkillOrSpellXpModificationAction(final int uniqueId, final long playerId) {
        super(uniqueId, FightActionType.SKILL_OR_SPELL_XP_GAIN.getId(), 0);
        this.m_xpModifications = new ArrayList<SkillOrSpellXpModification>();
        this.m_playerId = playerId;
    }
    
    public static SkillOrSpellXpModificationAction[] buildFromMessage(final PlayerXpModificationMessage msg) {
        final SkillOrSpellXpModificationAction[] actions = new SkillOrSpellXpModificationAction[msg.size()];
        final Iterable<PlayerXpModification> playerXpModifications = msg.getXpModifications();
        int index = 0;
        for (final PlayerXpModification playerXpModification : playerXpModifications) {
            final SkillOrSpellXpModificationAction action = buildFromPlayerXpModification(playerXpModification);
            actions[index++] = action;
        }
        return actions;
    }
    
    private static SkillOrSpellXpModificationAction buildFromPlayerXpModification(final PlayerXpModification playerXpModification) {
        final SkillOrSpellXpModificationAction action = new SkillOrSpellXpModificationAction(TimedAction.getNextUid(), playerXpModification.getPlayerId());
        final Iterator<SkillOrSpellXpModification> it = playerXpModification.iterator();
        while (it.hasNext()) {
            action.m_xpModifications.add(it.next());
        }
        return action;
    }
    
    public long onRun() {
        final CharacterInfo player = CharacterInfoManager.getInstance().getCharacter(this.m_playerId);
        if (player == null) {
            return 0L;
        }
        this.m_hasSkillXpModification = false;
        for (final SkillOrSpellXpModification xpModification : this.m_xpModifications) {
            this.processSkillOrSpellXpModification(player, xpModification);
        }
        if (player != WakfuGameEntity.getInstance().getLocalPlayer()) {
            return 0L;
        }
        if (this.m_xpModifications.isEmpty()) {
            return 0L;
        }
        if (Xulor.getInstance().isLoaded("spellsDialog")) {
            final UISpellsPageFrame spellsDlg = UISpellsPageFrame.getInstance();
            spellsDlg.updateAll();
        }
        if (Xulor.getInstance().isLoaded("spellsDialog")) {
            PropertiesProvider.getInstance().firePropertyValueChanged(player.getBreedInfo(), player.getBreedInfo().getFields());
            PropertiesProvider.getInstance().firePropertyValueChanged(player.getSpellInventoryManager(), player.getSpellInventoryManager().getFields());
        }
        return 0L;
    }
    
    private void processSkillOrSpellXpModification(final CharacterInfo player, final SkillOrSpellXpModification xpModification) {
        final SkillOrSpell skillOrSpell = xpModification.getSubject();
        final long xpDiff = xpModification.getXpModification().getXpDifference();
        final boolean levelGained = xpModification.getXpModification().doesLevelUp();
        if (skillOrSpell.isSkill()) {
            player.addSkillXp(skillOrSpell.getRefId(), xpDiff, levelGained);
            this.m_hasSkillXpModification = true;
        }
        else {
            player.addSpellXp(skillOrSpell.getRefId(), xpDiff, levelGained);
        }
    }
    
    @Override
    protected void onActionFinished() {
    }
}
