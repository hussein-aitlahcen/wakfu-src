package com.ankamagames.wakfu.common.game.fight.spellCastValidation;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class SpellCastThroughGateValidator
{
    private boolean m_computed;
    private final SpellCastThroughGateComputer m_computer;
    private final Set<BasicEffectArea> m_inputGates;
    private final Map<BasicEffectArea, Set<CastValidity>> m_gatesCastValidity;
    
    public SpellCastThroughGateValidator() {
        super();
        this.m_computer = new SpellCastThroughGateComputer();
        this.m_inputGates = new HashSet<BasicEffectArea>();
        this.m_gatesCastValidity = new HashMap<BasicEffectArea, Set<CastValidity>>();
    }
    
    public void computeGatesIfNecessary() {
        if (this.m_computed) {
            return;
        }
        this.m_computed = true;
        this.m_computer.compute();
        this.m_computer.fillResults(this.m_inputGates, this.m_gatesCastValidity);
    }
    
    public boolean hasInputGate() {
        final Set<BasicEffectArea> validInputGates = this.m_inputGates;
        return !validInputGates.isEmpty();
    }
    
    public boolean isValidRange() {
        for (final Set<CastValidity> castValidities : this.m_gatesCastValidity.values()) {
            if (!castValidities.contains(CastValidity.INVALID_RANGE)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasGateInRangeWithValidAlignment() {
        for (final Set<CastValidity> castValidities : this.m_gatesCastValidity.values()) {
            if (!castValidities.contains(CastValidity.CELLS_NOT_ALIGNED) && !castValidities.contains(CastValidity.INVALID_RANGE)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isValidLos() {
        for (final Set<CastValidity> castValidities : this.m_gatesCastValidity.values()) {
            if (castValidities.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public BasicEffectArea getValidOutputGate() {
        for (final Map.Entry<BasicEffectArea, Set<CastValidity>> gateValidities : this.m_gatesCastValidity.entrySet()) {
            if (gateValidities.getValue().isEmpty()) {
                return gateValidities.getKey();
            }
        }
        return null;
    }
    
    @Nullable
    public BasicEffectArea getValidInputGate() {
        final Set<BasicEffectArea> validInputGates = this.m_inputGates;
        if (validInputGates.isEmpty()) {
            return null;
        }
        return validInputGates.iterator().next();
    }
    
    public void setParams(final BasicFightInfo linkedFight, final BasicCharacterInfo fighter, final AbstractSpellLevel spellLevel, final int rangeMin, final int boostedRangeMax, final Point3 targetCell) {
        this.m_computer.setParams(linkedFight, fighter, spellLevel, rangeMin, boostedRangeMax, targetCell);
        this.m_computed = false;
    }
    
    private void clearParams() {
        this.m_computer.clearParams();
        this.m_computed = false;
    }
    
    public void clear() {
        this.clearParams();
        this.m_gatesCastValidity.clear();
        this.m_inputGates.clear();
    }
}
