package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.*;

public class EcaflipFightListener implements BreedSpecific
{
    public static final int LUCKY_STATE_ID = 102;
    private static final int ANIM_DURATION = 1500;
    private static final int CYCLE_DURATION = 250;
    private static final String[] DIALOGS;
    public static final String ECAFLIP_DICE_ID = "ecaflipDice";
    public static final String ECAFLIP_CLUB_IMAGE_ID = "ecaflipClubImage";
    public static final String ECAFLIP_CLUB_LABEL_ID = "ecaflipClubLabel";
    public static final String ANIM_DICE_NAME = "AnimDe-";
    private final PlayerCharacter m_character;
    private final ArrayList<AnimatedElementViewer> m_animatedElementViewers;
    private final List<Image> m_clubImages;
    private final ArrayList<Label> m_clubLabels;
    
    public EcaflipFightListener(final PlayerCharacter character) {
        super();
        this.m_animatedElementViewers = new ArrayList<AnimatedElementViewer>(1);
        this.m_clubImages = new ArrayList<Image>();
        this.m_clubLabels = new ArrayList<Label>();
        this.m_character = character;
        for (final String mapId : EcaflipFightListener.DIALOGS) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(mapId);
            if (map != null) {
                final AnimatedElementViewer element = (AnimatedElementViewer)map.getElement("ecaflipDice");
                if (element != null) {
                    this.m_animatedElementViewers.add(element);
                }
                final Image image = (Image)map.getElement("ecaflipClubImage");
                if (image != null) {
                    this.m_clubImages.add(image);
                }
                final Label label = (Label)map.getElement("ecaflipClubLabel");
                if (label != null) {
                    this.m_clubLabels.add(label);
                }
            }
        }
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
            if (state.getStateBaseId() == 102) {
                this.updateCurrentLevel(state.getLevel());
            }
        }
        else if (event.getId() == 1001) {
            final WakfuRunningEffect wakfuRunningEffect = ((EffectUnappliedEvent)event).getState();
            if (wakfuRunningEffect.getId() != RunningEffectConstants.RUNNING_STATE.getId()) {
                return;
            }
            if (wakfuRunningEffect.getTarget() != this.m_character) {
                return;
            }
            final State state = ((StateRunningEffect)wakfuRunningEffect).getState();
            if (state.getStateBaseId() == 102) {
                this.updateCurrentLevel((short)0);
            }
        }
    }
    
    @Override
    public void onBarrelCarried(final BasicEffectArea area) {
    }
    
    @Override
    public void onBarrelUncarried(final BasicEffectArea area) {
    }
    
    private void updateCurrentLevel(final short level) {
        for (int i = this.m_clubLabels.size() - 1; i >= 0; --i) {
            this.m_clubLabels.get(i).setText(String.valueOf(level));
        }
    }
    
    private static void addAllAppearances(final List<? extends Widget> widgets, final Collection<ModulationColorClient> appearances) {
        for (int i = widgets.size() - 1; i >= 0; --i) {
            appearances.add(((Widget)widgets.get(i)).getAppearance());
        }
    }
    
    public void rollDice(final byte value) {
        if (value < 1 || value > 6) {
            return;
        }
        if (this.m_clubLabels.isEmpty() || this.m_clubImages.isEmpty() || this.m_animatedElementViewers.isEmpty()) {
            return;
        }
        final ArrayList<ModulationColorClient> appearances = new ArrayList<ModulationColorClient>();
        addAllAppearances(this.m_clubLabels, appearances);
        addAllAppearances(this.m_clubImages, appearances);
        final Widget widget = this.m_clubLabels.get(0);
        widget.addTween(new ModulationColorListTween(Color.WHITE, Color.WHITE_ALPHA, appearances, 0, 250, 1, TweenFunction.PROGRESSIVE));
        widget.addTween(new ModulationColorListTween(Color.WHITE_ALPHA, Color.WHITE, appearances, 1500, 250, 1, TweenFunction.PROGRESSIVE));
        final ArrayList<ModulationColorClient> animatedElements = new ArrayList<ModulationColorClient>();
        for (int i = this.m_animatedElementViewers.size() - 1; i >= 0; --i) {
            final AnimatedElementViewer elementViewer = this.m_animatedElementViewers.get(i);
            elementViewer.setAnimName("AnimDe-" + value);
            animatedElements.add(elementViewer);
        }
        final Widget animatedElement = this.m_animatedElementViewers.get(0);
        animatedElement.addTween(new ModulationColorListTween(Color.WHITE_ALPHA, Color.WHITE, animatedElements, 0, 250, 1, TweenFunction.PROGRESSIVE));
        animatedElement.addTween(new ModulationColorListTween(Color.WHITE, Color.WHITE_ALPHA, animatedElements, 1500, 250, 1, TweenFunction.PROGRESSIVE));
    }
    
    @Override
    public void clear() {
        EcaflipFightListenerManager.INSTANCE.unregisterCharacter(this.m_character);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EcaflipFightListener");
        sb.append("{m_character=").append(this.m_character);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        DIALOGS = new String[] { "worldAndFightBarDialog" };
    }
}
