package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.tackle.*;
import com.ankamagames.wakfu.common.game.fight.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class UIFightMovementFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIFightMovementFrame m_instance;
    private final PathDisplaySelection m_pathSelection;
    private boolean m_enabled;
    private boolean m_enableMovement;
    private int m_pathLength;
    private int m_lastMP;
    private boolean m_active;
    private ClientPathFindResult m_lastPathSelected;
    private final TargetPositionListener<PathMobile> m_cellChangedListener;
    private TackleRules m_tackleRules;
    
    public UIFightMovementFrame() {
        super();
        this.m_pathSelection = PathDisplaySelection.getInstance();
        this.m_enabled = true;
        this.m_enableMovement = true;
        this.m_pathLength = 0;
        this.m_lastMP = -1;
        this.m_active = false;
        this.m_cellChangedListener = new TargetPositionListener<PathMobile>() {
            @Override
            public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
                UIFightMovementFrame.this.refreshPathSelection();
            }
        };
        this.m_tackleRules = new TackleRules();
    }
    
    public static UIFightMovementFrame getInstance() {
        return UIFightMovementFrame.m_instance;
    }
    
    public final void enable() {
        this.m_enabled = true;
    }
    
    public final void disable() {
        this.m_enabled = false;
        this.clearPathSelection();
    }
    
    public void disableMovement() {
        this.m_enableMovement = false;
        this.clearPathSelection();
    }
    
    public void enableMovement() {
        this.m_enableMovement = true;
    }
    
    public void clearPathLength() {
        this.m_pathLength = 0;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!this.m_active) {
            UIFightMovementFrame.m_logger.warn((Object)"Traitement d'un message alors que la frame n'est plus active");
            this.clearPathSelection();
        }
        if (WakfuGameEntity.getInstance().hasFrame(UITimePointSelectionFrame.getInstance())) {
            return true;
        }
        if (!this.m_enabled || !this.m_enableMovement) {
            return true;
        }
        switch (message.getId()) {
            case 18015: {
                this.updatePathSelection();
                return true;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                GraphicalMouseManager.getInstance().hide();
                if (!msg.isButtonLeft()) {
                    this.clearPathSelection();
                    UIFightFrame.m_lastTarget.set(0, 0, (short)32767);
                    return true;
                }
                if (UIFightFrame.getInstance().getCellReportMode()) {
                    return true;
                }
                this.m_pathLength = 0;
                if (UIFightFrame.m_lastTarget == null) {
                    return false;
                }
                final CharacterInfo fighter = this.getConcernedFighter();
                if (fighter == null) {
                    return false;
                }
                if (this.m_lastPathSelected == null || this.m_lastPathSelected.getPathLength() <= 0) {
                    return false;
                }
                final PathFindResult pathFindResult = this.m_lastPathSelected.getPathFindResult();
                if (this.m_lastPathSelected.getPathLength() == 1 && fighter.getPosition().equals(pathFindResult.getFirstStep()[0], pathFindResult.getFirstStep()[1])) {
                    return false;
                }
                if (!this.isPathValidForTackle(fighter, this.m_lastPathSelected)) {
                    return false;
                }
                this.m_pathLength = this.m_lastPathSelected.getPathLength();
                this.sendPath(fighter, pathFindResult);
                this.disableMovement();
                this.clearPathSelection();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean isPathValidForTackle(final CharacterInfo fighter, final ClientPathFindResult lastPathSelected) {
        final TackleResult tackleResultOnPath = this.getTackleResultOnPath(fighter, lastPathSelected.getPathFindResult());
        final int apNeeded = tackleResultOnPath.getApLoss();
        final int mpNeeded = tackleResultOnPath.getMpLoss() + this.getMpCost(lastPathSelected);
        return apNeeded <= fighter.getCharacteristicValue(FighterCharacteristicType.AP) && mpNeeded <= fighter.getCharacteristicValue(FighterCharacteristicType.MP);
    }
    
    public void updatePathSelection() {
        if (!this.m_enabled || !this.m_enableMovement) {
            return;
        }
        if (!UIFightFrame.getInstance().getCellReportMode()) {
            UIFightFrame.getInstance();
            if (!UIFightFrame.updateMovementDisplay()) {
                this.refreshPathSelection();
            }
            else {
                this.clearPathSelection();
            }
        }
    }
    
    private CharacterInfo getConcernedFighter() {
        CharacterInfo fighter = null;
        try {
            fighter = UIFightFrame.m_fight.getTimeline().getCurrentFighter();
            if (!fighter.isControlledByLocalPlayer()) {
                UIFightMovementFrame.m_logger.warn((Object)"WORLD_SCENE_MOUSE_RELEASED demand\u00e9 pour un fighter qui n'est pas \u00e0 soi");
            }
        }
        catch (NullPointerException e) {
            UIFightMovementFrame.m_logger.error((Object)"WORLD_SCENE_MOUSE_RELEASED dans une frame de combat, sans combat ou combattant");
        }
        return fighter;
    }
    
    private void sendPath(final CharacterInfo fighter, final PathFindResult pathResult) {
        final FighterActorMovementRequestMessage netMessage = new FighterActorMovementRequestMessage();
        netMessage.setPathResult(pathResult);
        netMessage.setFighterId(fighter.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.m_active = true;
        this.m_enableMovement = true;
        this.clear();
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().addPositionListener(this.m_cellChangedListener);
        UIFightFrame.getInstance();
        UIFightFrame.updateMovementDisplay();
        this.refreshPathSelection();
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_active = false;
        this.clear();
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().removePositionListener(this.m_cellChangedListener);
    }
    
    public void clearPathSelection() {
        this.m_pathSelection.clear();
        this.m_lastPathSelected = null;
    }
    
    private void clear() {
        this.m_pathLength = 0;
        this.m_lastMP = -1;
        UIFightFrame.getInstance();
        UIFightFrame.clearMovementDisplay();
        this.clearPathSelection();
    }
    
    public void setMouse(final int mouseX, final int mouseY) {
    }
    
    public void refreshPathSelection() {
        CharacterInfo currentFighter;
        try {
            if (UIFightFrame.m_fight == null) {
                this.clearPathSelection();
                return;
            }
            currentFighter = UIFightFrame.m_fight.getTimeline().getCurrentFighter();
            if (!currentFighter.isControlledByLocalPlayer()) {
                this.clearPathSelection();
                UIFightMovementFrame.m_logger.warn((Object)"refreshPath demand\u00e9 pour un fighter qui n'est pas \u00e0 soi");
                return;
            }
        }
        catch (NullPointerException e) {
            this.clearPathSelection();
            UIFightMovementFrame.m_logger.error((Object)"GRAVE > contexte de mouvement en combat, sans combat ou fighter");
            return;
        }
        if (UIFightFrame.m_lastTarget == null || currentFighter.isActiveProperty(FightPropertyType.ROOTED) || currentFighter.isActiveProperty(FightPropertyType.DO_NOT_MOVE_IN_FIGHT)) {
            this.clearPathSelection();
            return;
        }
        int movementPoints = currentFighter.getCharacteristicValue(FighterCharacteristicType.MP);
        if (currentFighter.isActiveProperty(FightPropertyType.SEVEN_LEAGUE_BOOTS)) {
            movementPoints *= 2;
        }
        if (currentFighter.isActiveProperty(FightPropertyType.LAME)) {
            movementPoints /= 2;
        }
        if (this.m_lastMP != movementPoints) {
            this.m_lastMP = movementPoints;
            this.m_pathLength = 0;
        }
        else {
            movementPoints -= this.m_pathLength;
        }
        if (movementPoints > 0) {
            final CharacterActor actor = currentFighter.getActor();
            final ClientPathFindResult pathResult = currentFighter.getFightPathFindMethod().findPath(currentFighter, actor, movementPoints);
            final TackleResult tackleResult = this.getTackleResultOnPath(currentFighter, pathResult.getPathFindResult());
            if (pathResult != null && pathResult.getPathLength() > 0) {
                this.m_pathSelection.setPath(pathResult.getPathFindResult());
                final int ap = currentFighter.getCharacteristicValue(FighterCharacteristicType.AP);
                if (tackleResult.getMpLoss() + this.getMpCost(pathResult) > movementPoints || ap < tackleResult.getApLoss()) {
                    this.m_pathSelection.setColor(WakfuClientConstants.ERROR_HIGHLIGHT_COLOR.getFloatRGBA());
                }
                else {
                    this.m_pathSelection.setColor(WakfuClientConstants.PATH_COLOR);
                }
                this.m_lastPathSelected = pathResult;
                final String infoText = this.getMouseInfoText();
                if (infoText != null) {
                    if (GraphicalMouseManager.getInstance().isNull()) {
                        GraphicalMouseManager.getInstance().showMouseInformation(null, infoText, 30, 0, Alignment9.WEST);
                    }
                    else {
                        GraphicalMouseManager.getInstance().setText(infoText);
                    }
                }
                else {
                    GraphicalMouseManager.getInstance().hide();
                }
            }
            else {
                this.m_pathSelection.clear();
                this.m_lastPathSelected = null;
                GraphicalMouseManager.getInstance().hide();
            }
        }
        else {
            this.m_pathSelection.clear();
            this.m_lastPathSelected = null;
            GraphicalMouseManager.getInstance().hide();
        }
    }
    
    private TackleResult getTackleResultOnPath(final CharacterInfo currentFighter, final PathFindResult pathResult) {
        final List<TackleResult> tackleResultsOnPath = TackleOnPathComputer.getTackleResultsOnPath(pathResult, currentFighter);
        return this.getTotalResult(tackleResultsOnPath);
    }
    
    private TackleResult getTotalResult(final List<TackleResult> results) {
        final TackleResult finalResult = new TackleResult();
        for (final TackleResult currentResult : results) {
            finalResult.setApLoss(finalResult.getApLoss() + currentResult.getApLoss());
            finalResult.setMpLoss(finalResult.getMpLoss() + currentResult.getMpLoss());
        }
        return finalResult;
    }
    
    private String getMouseInfoText() {
        return this.getTackleInfo();
    }
    
    protected int getMpCost(final ClientPathFindResult path) {
        final int pathLength = path.getPathLength();
        final CharacterInfo concernedFighter = this.getConcernedFighter();
        if (concernedFighter == null) {
            return pathLength;
        }
        if (!path.isOnRails()) {
            return pathLength;
        }
        if (concernedFighter.hasProperty(FightPropertyType.MOVEMENT_ON_RAILS_ALWAYS_COST_1_PM)) {
            return 1;
        }
        return (pathLength + 3 - 1) / 3;
    }
    
    private String getTackleInfo() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo mover = localPlayer.getCurrentFight().getTimeline().getCurrentFighter();
        if (mover.isActiveProperty(FightPropertyType.ESCAPE_TACKLE)) {
            return null;
        }
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.FIGHT_REWORK_ENABLED)) {
            return this.getTackleCost(mover);
        }
        return this.tacklePercentMessage(localPlayer, mover);
    }
    
    private String getTackleCost(final CharacterInfo mover) {
        final TackleResult tackleResult = this.getTackleResultOnPath(mover, this.m_lastPathSelected.getPathFindResult());
        final int apLoss = tackleResult.getApLoss();
        final int mpLoss = tackleResult.getMpLoss();
        if (mpLoss == 0 && apLoss == 0) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater().b();
        if (apLoss != 0) {
            final boolean notEnoughAp = mover.getCharacteristicValue(FighterCharacteristicType.AP) < apLoss;
            sb.openText();
            if (notEnoughAp) {
                sb.addColor(Color.RED.getRGBtoHex());
            }
            sb.append(WakfuTranslator.getInstance().getString("tackle.apLoss", apLoss)).closeText();
        }
        if (mpLoss != 0) {
            if (apLoss != 0) {
                sb.newLine();
            }
            final boolean notEnoughMp = mover.getCharacteristicValue(FighterCharacteristicType.MP) < mpLoss + this.getMpCost(this.m_lastPathSelected);
            sb.openText();
            if (notEnoughMp) {
                sb.addColor(Color.RED.getRGBtoHex());
            }
            sb.append(WakfuTranslator.getInstance().getString("tackle.mpLoss", mpLoss + this.getMpCost(this.m_lastPathSelected))).closeText();
        }
        sb._b();
        final String msg = sb.finishAndToString();
        if (msg.isEmpty()) {
            return null;
        }
        return msg;
    }
    
    private String tacklePercentMessage(final LocalPlayerCharacter localPlayer, final CharacterInfo mover) {
        final TackleComputer tackleComputer = new TackleComputer(mover);
        final TIntArrayList tackleScore = new TIntArrayList(4);
        for (final CharacterInfo fighter : localPlayer.getCurrentFight().getPotentialTacklers(mover)) {
            tackleComputer.setTackler(fighter);
            final int tacklePercentage = tackleComputer.getTacklePercentage();
            if (tacklePercentage != -1 && !fighter.isInvisibleForLocalPlayer()) {
                tackleScore.add(tacklePercentage);
            }
        }
        if (tackleScore.size() > 3 || tackleScore.isEmpty()) {
            return null;
        }
        float move;
        if (tackleScore.size() == 1) {
            move = tackleScore.get(0) / 100.0f;
        }
        else if (tackleScore.size() == 2) {
            move = ProbaHelper.union(tackleScore.get(0) / 100.0f, tackleScore.get(1) / 100.0f);
        }
        else {
            move = ProbaHelper.union(tackleScore.get(0) / 100.0f, tackleScore.get(1) / 100.0f, tackleScore.get(2) / 100.0f);
        }
        int percent = 100 - Math.round(move * 100.0f);
        percent = MathHelper.clamp(percent, 5, 95);
        return WakfuTranslator.getInstance().getString("tacklePercentage", percent);
    }
    
    protected Point3 getNearestPoint3(final int mouseX, final int mouseY) {
        return WorldSceneInteractionUtils.getNearestPoint3(WakfuClientInstance.getInstance().getWorldScene(), mouseX, mouseY, false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFightMovementFrame.class);
        m_instance = new UIFightMovementFrame();
    }
}
