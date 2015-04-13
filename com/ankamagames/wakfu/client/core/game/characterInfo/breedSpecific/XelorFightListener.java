package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.common.*;

public class XelorFightListener implements BreedSpecific
{
    public static final int TICK_STATE_ID = 988;
    public static final int TACK_STATE_ID = 995;
    public static final String TICK_TACK_IMAGE = "xelorTickTackImage";
    public static final String TICK_STYLE = "xelorTick";
    public static final String TACK_STYLE = "xelorTack";
    private final PlayerCharacter m_character;
    private Image m_image;
    
    public XelorFightListener(final PlayerCharacter character) {
        super();
        this.m_character = character;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
        if (map == null) {
            return;
        }
        this.m_image = (Image)map.getElement("xelorTickTackImage");
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent event) {
        if (event.getId() == 1000) {
            final WakfuRunningEffect wakfuRunningEffect = ((EffectAppliedEvent)event).getEffect();
            if (wakfuRunningEffect.getId() != RunningEffectConstants.RUNNING_STATE.getId()) {
                return;
            }
            if (wakfuRunningEffect.getTarget() != this.m_character) {
                return;
            }
            final State state = ((StateRunningEffect)wakfuRunningEffect).getState();
            if (state.getStateBaseId() == 988) {
                this.m_image.setStyle("xelorTick");
            }
            else if (state.getStateBaseId() == 995) {
                this.m_image.setStyle("xelorTack");
            }
        }
    }
    
    @Override
    public void onBarrelCarried(final BasicEffectArea area) {
    }
    
    @Override
    public void onBarrelUncarried(final BasicEffectArea area) {
    }
    
    @Override
    public void clear() {
        XelorFightListenerManager.INSTANCE.unregisterCharacter(this.m_character);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EcaflipFightListener");
        sb.append("{m_character=").append(this.m_character);
        sb.append('}');
        return sb.toString();
    }
}
