package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public class UIFightSpellCastInteractionFrame extends UIAbstractFightCastInteractionFrame
{
    private static final UIFightSpellCastInteractionFrame m_instance;
    private SpellLevel m_selectedSpell;
    
    private UIFightSpellCastInteractionFrame() {
        super();
        this.m_selectedSpell = null;
        this.m_rangeDisplayer = SpellDisplayZone.getInstance();
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate((CustomTextureHighlightingProvider)this.m_rangeDisplayer);
    }
    
    public static UIFightSpellCastInteractionFrame getInstance() {
        return UIFightSpellCastInteractionFrame.m_instance;
    }
    
    public void setSelectedSpell(final SpellLevel selectedSpell) {
        this.m_selectedSpell = selectedSpell;
    }
    
    public SpellLevel getSelectedSpell() {
        return this.m_selectedSpell;
    }
    
    @Override
    protected EffectContainer getEffectContainer() {
        return this.m_selectedSpell;
    }
    
    @Override
    protected void sendCastMessage(final int castPositionX, final int castPositionY, final short castPositionZ) {
        final SpellLevelCastRequestMessage netMessage = new SpellLevelCastRequestMessage();
        netMessage.setFighterId(this.m_character.getId());
        netMessage.setSpellId(this.m_selectedSpell.getUniqueId());
        netMessage.setCastPosition(castPositionX, castPositionY, castPositionZ);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    protected void updateUsage() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_character == localPlayer) {
            localPlayer.incrementFightSpellUsage(this.m_selectedSpell);
        }
    }
    
    @Nullable
    @Override
    protected String getMouseText() {
        final CharacterInfo character = (CharacterInfo)this.m_character.getCurrentFight().getCharacterInfoAtPosition(UIFightFrame.getLastTarget());
        if (character == null) {
            return "";
        }
        if (!this.isHealingSpell(this.m_selectedSpell)) {
            return "";
        }
        final float healResist = character.getFinalHealResist();
        if (healResist >= 1.0f) {
            return WakfuTranslator.getInstance().getString("desc.healRes", (int)healResist);
        }
        return "";
    }
    
    private boolean isHealingSpell(final EffectContainer<WakfuEffect> container) {
        for (final WakfuEffect effect : container) {
            if (this.isHealingEffect(effect)) {
                return true;
            }
            final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effect.getEffectId());
            if (effectGroup == null) {
                continue;
            }
            final boolean healingSpell = this.isHealingSpell(effectGroup);
            if (healingSpell) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isHealingEffect(final WakfuEffect effect) {
        final WakfuRunningEffect wakfuRunningEffect = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        return wakfuRunningEffect != null && wakfuRunningEffect instanceof HPGain;
    }
    
    @Override
    protected String getCastMouseIcon() {
        if (this.m_selectedSpell != null) {
            return (String)this.m_selectedSpell.getFieldValue("smallIconUrl");
        }
        return null;
    }
    
    @Override
    protected byte getCastType() {
        return 1;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void selectRange() {
        super.selectRange();
        if (this.m_selectedSpell != null && this.m_character != null) {
            ((SpellDisplayZone)this.m_rangeDisplayer).selectSpellRange(this.m_selectedSpell, this.m_character);
        }
    }
    
    static {
        m_instance = new UIFightSpellCastInteractionFrame();
    }
}
