package com.ankamagames.wakfu.client.alea.graphics.fightView;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import gnu.trove.*;

public class FightVisibilityManager
{
    private static final Logger m_logger;
    private static final FightVisibilityManager m_instance;
    private int m_participatingFight;
    private boolean m_doHideOtherFights;
    private final FightViewRules m_doNotShowExternalFightsViewRules;
    private final ShowExternalFightsViewRules m_showExternalFightsViewRules;
    private final FightViewRules m_observedFightViewRules;
    private float m_zoomFactorBeforeFight;
    public static final int MAX_LOD_LEVEL = 2;
    private final ArrayList<AnimatedInteractiveElement> m_hitElements;
    
    public static FightVisibilityManager getInstance() {
        return FightVisibilityManager.m_instance;
    }
    
    private FightVisibilityManager() {
        super();
        this.m_doNotShowExternalFightsViewRules = new DoNotShowExternalFightsViewRules();
        this.m_showExternalFightsViewRules = new ShowExternalFightsViewRules();
        this.m_observedFightViewRules = new ObservedFightViewRules();
        this.m_hitElements = new ArrayList<AnimatedInteractiveElement>();
        this.reset();
    }
    
    public void reset() {
        this.m_participatingFight = -1;
        FightViewUtils.removeAllFightRepresentations();
    }
    
    public void setParticipatingFight(final int participatingFight) {
        this.m_participatingFight = participatingFight;
    }
    
    public int getParticipatingFight() {
        return this.m_participatingFight;
    }
    
    public float getZoomFactorBeforeFight() {
        return this.m_zoomFactorBeforeFight;
    }
    
    public void setZoomFactorBeforeFight(final float zoomFactorBeforeFight) {
        this.m_zoomFactorBeforeFight = zoomFactorBeforeFight;
    }
    
    public void setDoHideOtherFights(final boolean doHideOtherFights) {
        this.m_doHideOtherFights = doHideOtherFights;
    }
    
    public void doNotShowAPSInExternalFights() {
        this.m_showExternalFightsViewRules.setParticuleVisible(false);
    }
    
    public void showAPSInExternalFights() {
        this.m_showExternalFightsViewRules.setParticuleVisible(true);
    }
    
    public void doNotShowExternalFightsBorders() {
        this.m_showExternalFightsViewRules.setBorderVisible(false);
    }
    
    public void showExternalFightsBorders() {
        this.m_showExternalFightsViewRules.setBorderVisible(true);
    }
    
    private FightViewRules getConcernedRules(final int fightId) {
        if (fightId == this.m_participatingFight) {
            return this.m_observedFightViewRules;
        }
        if (this.m_doHideOtherFights) {
            return this.m_doNotShowExternalFightsViewRules;
        }
        return this.m_showExternalFightsViewRules;
    }
    
    public void onFighterJoinFight(final CharacterInfo character, final int fightId) {
        final FightViewRules rules = this.getConcernedRules(fightId);
        rules.onFighterJoinFight(character);
    }
    
    public void onFighterLeaveFight(final CharacterInfo character, final int fightId) {
        if (WakfuGameEntity.getInstance().getLocalPlayer() == character) {
            this.setParticipatingFight(-1);
        }
        final FightViewRules rules = this.getConcernedRules(fightId);
        rules.onFighterLeaveFight(character);
    }
    
    public void onBasicEffectAreaAdded(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        final FightViewRules rules = this.getConcernedRules(fightInfo.getId());
        rules.onBasicEffectAreaAdded(fightInfo, effectArea);
    }
    
    public void onBasicEffectAreaTeleported(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        final FightViewRules rules = this.getConcernedRules((fightInfo != null) ? fightInfo.getId() : 0);
        rules.onBasicEffectAreaTeleported(fightInfo, effectArea);
    }
    
    public void onBasicEffectAreaRemoved(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        final FightViewRules rules = this.getConcernedRules(fightInfo.getId());
        rules.onBasicEffectAreaRemoved(fightInfo, effectArea);
    }
    
    public boolean isParticuleVisibleForFight(final int fightId) {
        return this.getConcernedRules(fightId).isParticuleVisibleForFight();
    }
    
    public boolean canDisplayFlyingValue(final int fightId) {
        return this.getConcernedRules(fightId).canDisplayFlyingValue();
    }
    
    public void onExternalFightCreation(final ExternalFightInfo externalFightInfo) {
        final FightViewRules rules = this.getConcernedRules(externalFightInfo.getId());
        rules.onExternalFightCreation(externalFightInfo);
    }
    
    public void updateFightVisibility() {
        FightViewUtils.removeAllFightRepresentations();
        final TIntObjectIterator<FightInfo> it = FightManager.getInstance().getFightsIterator();
        while (it.hasNext()) {
            try {
                it.advance();
                final FightInfo fightInfo = it.value();
                if (fightInfo.getStatus() == AbstractFight.FightStatus.DESTRUCTION) {
                    continue;
                }
                final FightViewRules rules = this.getConcernedRules(fightInfo.getId());
                rules.updateFightVisibility(fightInfo);
            }
            catch (Exception e) {
                FightVisibilityManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public static void deactivateFightObservationView(final FightInfo fightInfo) {
        FightViewUtils.deactivateFightObservationView(fightInfo);
        final TLongObjectIterator<ClientInteractiveAnimatedElementSceneView> it = AnimatedElementSceneViewManager.getInstance().getInteractiveElementIterator();
        while (it.hasNext()) {
            it.advance();
            final ClientInteractiveAnimatedElementSceneView view = it.value();
            view.setVisible(view.getInteractiveElement().isVisible());
        }
    }
    
    public void setFightLODLevel(final int level) {
        boolean particleVisible = false;
        switch (level) {
            case 0: {
                this.m_doHideOtherFights = true;
                particleVisible = false;
                break;
            }
            case 1: {
                this.m_doHideOtherFights = false;
                particleVisible = false;
                break;
            }
            case 2: {
                this.m_doHideOtherFights = false;
                particleVisible = true;
                break;
            }
            default: {
                FightVisibilityManager.m_logger.error((Object)("FightLODLevel inconnu " + level));
                break;
            }
        }
        this.m_showExternalFightsViewRules.setParticuleVisible(particleVisible);
        this.updateFightVisibility();
    }
    
    public static void onFightEnd(final ExternalFightInfo externalFightInfo) {
        FightViewUtils.removeFightRepresentation(externalFightInfo);
    }
    
    public ArrayList<AnimatedInteractiveElement> getFightRepresentationsUnderMousePoint(final float x, final float y) {
        this.m_hitElements.clear();
        FightViewUtils.getFightRepresentations(this.m_hitElements);
        for (int i = this.m_hitElements.size() - 1; i >= 0; --i) {
            if (!this.m_hitElements.get(i).hitTest(x, y)) {
                this.m_hitElements.remove(i);
            }
        }
        return this.m_hitElements;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightVisibilityManager.class);
        m_instance = new FightVisibilityManager();
    }
}
