package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class AreaOccupationComputer
{
    private static final Logger m_logger;
    private final Collection<BasicEffectArea> m_areasIn;
    private final HashMap<BasicEffectArea, ArrayList<BasicCharacterInfo>> m_mobileAreasWithFightersOn;
    private final Collection<BasicEffectArea> m_areasEntered;
    private final Collection<BasicEffectArea> m_areasLeft;
    private final Collection<BasicEffectArea> m_areasInto;
    private final Collection<ObjectPair<BasicEffectArea, BasicCharacterInfo>> m_areasEnteredByOthers;
    private final Collection<ObjectPair<BasicEffectArea, BasicCharacterInfo>> m_areasLeftByOthers;
    private final Collection<ObjectPair<BasicEffectArea, BasicCharacterInfo>> m_areasIntoByOthers;
    private EffectUser m_mover;
    private Point3 m_moverDestinationCell;
    private final AbstractFight<? extends BasicCharacterInfo> m_fight;
    
    public AreaOccupationComputer(final AbstractFight<? extends BasicCharacterInfo> fight, final EffectUser mover, final Point3 moverDestinationCell) {
        super();
        this.m_areasIn = new ArrayList<BasicEffectArea>();
        this.m_mobileAreasWithFightersOn = new HashMap<BasicEffectArea, ArrayList<BasicCharacterInfo>>();
        this.m_areasEntered = new ArrayList<BasicEffectArea>();
        this.m_areasLeft = new ArrayList<BasicEffectArea>();
        this.m_areasInto = new ArrayList<BasicEffectArea>();
        this.m_areasEnteredByOthers = new ArrayList<ObjectPair<BasicEffectArea, BasicCharacterInfo>>();
        this.m_areasLeftByOthers = new ArrayList<ObjectPair<BasicEffectArea, BasicCharacterInfo>>();
        this.m_areasIntoByOthers = new ArrayList<ObjectPair<BasicEffectArea, BasicCharacterInfo>>();
        this.m_fight = fight;
        this.setMover(mover);
        this.setMoverDestinationCell(moverDestinationCell);
    }
    
    public void setMover(final EffectUser mover) {
        if (mover == null) {
            throw new IllegalArgumentException("Le mover ne doit jamais etre null");
        }
        this.m_mover = mover;
    }
    
    public void setMoverDestinationCell(final Point3 moverDestinationCell) {
        if (moverDestinationCell == null) {
            throw new IllegalArgumentException("La cellule destination ne doit jamais etre null");
        }
        this.m_moverDestinationCell = moverDestinationCell;
    }
    
    public void clean() {
        this.m_areasIn.clear();
        this.m_mobileAreasWithFightersOn.clear();
        this.m_areasEntered.clear();
        this.m_areasLeft.clear();
        this.m_areasInto.clear();
        this.m_areasEnteredByOthers.clear();
        this.m_areasLeftByOthers.clear();
        this.m_areasIntoByOthers.clear();
    }
    
    public void setInitialState() {
        this.setInitialState(null);
    }
    
    public void setInitialState(Point3 moverStartCell) {
        if (moverStartCell == null) {
            moverStartCell = this.m_mover.getPosition();
        }
        final Point3 moverCurrentPosition = this.m_mover.getPosition();
        if (moverCurrentPosition != moverStartCell) {
            this.m_mover.setPosition(moverStartCell);
        }
        try {
            for (final BasicEffectArea area : this.m_fight.getActiveEffectAreas()) {
                if (area.contains(this.m_mover, moverStartCell.getX(), moverStartCell.getY(), moverStartCell.getZ())) {
                    this.m_areasIn.add(area);
                }
                if (area.isPositionStatic()) {
                    continue;
                }
                ArrayList<BasicCharacterInfo> fightersOnArea = this.m_mobileAreasWithFightersOn.get(area);
                if (fightersOnArea == null) {
                    fightersOnArea = new ArrayList<BasicCharacterInfo>();
                    this.m_mobileAreasWithFightersOn.put(area, fightersOnArea);
                }
                for (final BasicCharacterInfo characterInfo : this.m_fight.getFightersInPlay()) {
                    if (area.contains(characterInfo, characterInfo.getWorldCellX(), characterInfo.getWorldCellY(), characterInfo.getWorldCellAltitude())) {
                        fightersOnArea.add(characterInfo);
                    }
                }
            }
        }
        catch (Exception e) {
            AreaOccupationComputer.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            if (moverCurrentPosition != moverStartCell) {
                this.m_mover.setPosition(moverCurrentPosition);
            }
        }
    }
    
    private void clearAreasModifications() {
        this.m_areasEntered.clear();
        this.m_areasLeft.clear();
        this.m_areasEnteredByOthers.clear();
        this.m_areasInto.clear();
        this.m_areasLeftByOthers.clear();
    }
    
    public void computeAreaModificationsOnMove() {
        this.clearAreasModifications();
        if (this.m_fight.getActiveEffectAreas().isEmpty()) {
            return;
        }
        final Collection<BasicEffectArea> activeEffectAreas = new ArrayList<BasicEffectArea>(this.m_fight.getActiveEffectAreas());
        for (final BasicEffectArea area : activeEffectAreas) {
            this.moverAreaModification(area);
            if (area.isPositionStatic()) {
                continue;
            }
            this.othersAreaModifications(area);
        }
    }
    
    public boolean willTriggerSomething() {
        if (!this.areasOccupationHasBeenModified()) {
            return false;
        }
        final Point3 currentPos = new Point3(this.m_mover.getPosition());
        this.m_mover.setPosition(this.m_moverDestinationCell);
        boolean res;
        try {
            res = (this.moverWillTriggerSomething() || this.othersWillTriggerSomething());
        }
        catch (Exception e) {
            res = false;
            AreaOccupationComputer.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        this.m_mover.setPosition(currentPos);
        return res;
    }
    
    public boolean hasToBeStopByAreaInteraction() {
        if (!this.areasOccupationHasBeenModified()) {
            return false;
        }
        for (final BasicEffectArea area : this.m_areasLeft) {
            if (area.checkApplicationTriggers(10002) && area.shouldStopMover()) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasInto) {
            if (area.checkApplicationTriggers(10008) && area.shouldStopMover()) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasEntered) {
            if (area.checkApplicationTriggers(10001) && area.shouldStopMover()) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasEnteredByOthers) {
            final BasicEffectArea area2 = pair.getFirst();
            if (area2.checkApplicationTriggers(10001) && area2.shouldStopMover()) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasIntoByOthers) {
            final BasicEffectArea area2 = pair.getFirst();
            if (area2.checkApplicationTriggers(10008) && area2.shouldStopMover()) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasLeftByOthers) {
            final BasicEffectArea area2 = pair.getFirst();
            if (area2.checkApplicationTriggers(10002) && area2.shouldStopMover()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasToCutMovementAreaInteraction() {
        if (!this.areasOccupationHasBeenModified()) {
            return false;
        }
        for (final BasicEffectArea area : this.m_areasLeft) {
            if (area.checkApplicationTriggers(10002) && !this.isMoverDeposit(area)) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasInto) {
            if (area.checkApplicationTriggers(10008) && !this.isMoverDeposit(area)) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasEntered) {
            if (area.checkApplicationTriggers(10001) && !this.isMoverDeposit(area)) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasEnteredByOthers) {
            if (pair.getFirst().checkApplicationTriggers(10001)) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasIntoByOthers) {
            if (pair.getFirst().checkApplicationTriggers(10008)) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasLeftByOthers) {
            if (pair.getFirst().checkApplicationTriggers(10002)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isMoverDeposit(final BasicEffectArea area) {
        return area.getType() == EffectAreaType.ENUTROF_DEPOSIT.getTypeId() && area.getOwner() == this.m_mover;
    }
    
    private boolean moverWillTriggerSomething() {
        for (final BasicEffectArea area : this.m_areasLeft) {
            if (area.checkTriggers(10002, this.m_mover)) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasInto) {
            if (area.checkTriggers(10008, this.m_mover)) {
                return true;
            }
        }
        for (final BasicEffectArea area : this.m_areasEntered) {
            if (area.checkTriggers(10001, this.m_mover)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean othersWillTriggerSomething() {
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasLeftByOthers) {
            if (pair.getFirst().checkTriggers(10002, this.m_mover)) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasIntoByOthers) {
            if (pair.getFirst().checkTriggers(10008, this.m_mover)) {
                return true;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasEnteredByOthers) {
            if (pair.getFirst().checkTriggers(10001, this.m_mover)) {
                return true;
            }
        }
        return false;
    }
    
    public void triggerAreaEffects() {
        this.triggerMoverAreaEffects();
        this.triggerOthersAreaEffects();
    }
    
    private void triggerMoverAreaEffects() {
        for (final BasicEffectArea area : this.m_areasLeft) {
            this.triggerAreaEffectOnTarget(area, (short)10002, this.m_mover);
            if (this.m_mover.isOffPlay() || this.m_mover.isOutOfPlay()) {
                return;
            }
        }
        for (final BasicEffectArea area : this.m_areasInto) {
            this.triggerAreaEffectOnTarget(area, (short)10008, this.m_mover);
            if (this.m_mover.isOffPlay() || this.m_mover.isOutOfPlay()) {
                return;
            }
        }
        for (final BasicEffectArea area : this.m_areasEntered) {
            this.triggerAreaEffectOnTarget(area, (short)10001, this.m_mover);
            if (this.m_mover.isOffPlay() || this.m_mover.isOutOfPlay()) {
                return;
            }
        }
    }
    
    private void triggerOthersAreaEffects() {
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasLeftByOthers) {
            this.triggerAreaEffectOnTarget(pair.getFirst(), (short)10002, pair.getSecond());
            if (this.m_mover.isOffPlay()) {
                break;
            }
            if (this.m_mover.isOutOfPlay()) {
                break;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasIntoByOthers) {
            this.triggerAreaEffectOnTarget(pair.getFirst(), (short)10008, pair.getSecond());
            if (this.m_mover.isOffPlay()) {
                break;
            }
            if (this.m_mover.isOutOfPlay()) {
                break;
            }
        }
        for (final ObjectPair<BasicEffectArea, BasicCharacterInfo> pair : this.m_areasEnteredByOthers) {
            this.triggerAreaEffectOnTarget(pair.getFirst(), (short)10001, pair.getSecond());
            if (this.m_mover.isOffPlay()) {
                break;
            }
            if (this.m_mover.isOutOfPlay()) {
                break;
            }
        }
    }
    
    private void moverAreaModification(final BasicEffectArea area) {
        if (area.getOwner() == this.m_mover && !area.isPositionStatic()) {
            return;
        }
        if (this.m_mover != null && !area.canBeTriggeredBy(this.m_mover)) {
            return;
        }
        if (area.contains(this.m_mover, this.m_moverDestinationCell.getX(), this.m_moverDestinationCell.getY(), this.m_moverDestinationCell.getZ())) {
            if (!this.m_areasIn.contains(area)) {
                this.m_areasEntered.add(area);
                this.m_areasIn.add(area);
            }
            else {
                this.m_areasInto.add(area);
            }
        }
        else if (this.m_areasIn.contains(area)) {
            this.m_areasLeft.add(area);
            this.m_areasIn.remove(area);
        }
    }
    
    private void othersAreaModifications(final BasicEffectArea area) {
        final ArrayList<BasicCharacterInfo> characterInfoArrayList = this.m_mobileAreasWithFightersOn.get(area);
        if (characterInfoArrayList == null) {
            return;
        }
        final Point3 currentPos = this.m_mover.getPosition();
        try {
            this.m_mover.setPosition(this.m_moverDestinationCell);
            for (final BasicCharacterInfo characterInfo : this.m_fight.getFightersInPlay()) {
                if (characterInfo == this.m_mover) {
                    continue;
                }
                if (this.m_mover != area.getOwner()) {
                    continue;
                }
                if (area.isPositionStatic()) {
                    continue;
                }
                final int x = characterInfo.getWorldCellX();
                final int y = characterInfo.getWorldCellY();
                final short z = characterInfo.getWorldCellAltitude();
                if (area.contains(characterInfo, x, y, z)) {
                    if (!characterInfoArrayList.contains(characterInfo)) {
                        this.m_areasEnteredByOthers.add(new ObjectPair<BasicEffectArea, BasicCharacterInfo>(area, characterInfo));
                    }
                    else {
                        this.m_areasIntoByOthers.add(new ObjectPair<BasicEffectArea, BasicCharacterInfo>(area, characterInfo));
                    }
                }
                else {
                    if (!characterInfoArrayList.contains(characterInfo)) {
                        continue;
                    }
                    this.m_areasLeftByOthers.add(new ObjectPair<BasicEffectArea, BasicCharacterInfo>(area, characterInfo));
                }
            }
        }
        catch (Exception e) {
            AreaOccupationComputer.m_logger.error((Object)"Exception lev\u00e9e ", (Throwable)e);
        }
        finally {
            this.m_mover.setPosition(currentPos);
        }
    }
    
    private boolean areasOccupationHasBeenModified() {
        return !this.m_areasEntered.isEmpty() || !this.m_areasLeft.isEmpty() || !this.m_areasInto.isEmpty() || !this.m_areasLeftByOthers.isEmpty() || !this.m_areasEnteredByOthers.isEmpty() || !this.m_areasIntoByOthers.isEmpty();
    }
    
    private void triggerAreaEffectOnTarget(final BasicEffectArea area, final short trigger, final Target target) {
        area.triggers(trigger, null, target);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AreaOccupationComputer.class);
    }
}
