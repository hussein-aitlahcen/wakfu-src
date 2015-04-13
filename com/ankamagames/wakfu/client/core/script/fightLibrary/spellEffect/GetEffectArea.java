package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class GetEffectArea extends SpellEffectActionFunction
{
    private static final String NAME = "getEffectArea";
    private static final String DESC = "Retourne un tableau contenant la liste des cellules (x,y) de l'aire d'effet de l'effet";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    public GetEffectArea(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getEffectArea";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetEffectArea.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final Iterable<int[]> iterable = extractCells(this.m_spellEffectAction);
        if (iterable == null) {
            this.addReturnValue(0);
            return;
        }
        Iterator<int[]> iterator = iterable.iterator();
        int cells = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++cells;
        }
        this.addReturnValue(cells);
        iterator = iterable.iterator();
        this.L.newTable();
        final int tableIdx = this.L.getTop();
        int i = 1;
        while (iterator.hasNext()) {
            final int[] cell = iterator.next();
            this.L.pushNumber((double)(i++));
            this.L.newTable();
            final int rowIdx = this.L.getTop();
            this.L.pushString("x");
            this.L.pushNumber((double)cell[0]);
            this.L.setTable(rowIdx);
            this.L.pushString("y");
            this.L.pushNumber((double)cell[1]);
            this.L.setTable(rowIdx);
            this.L.setTable(tableIdx);
        }
        ++this.m_returnValueCount;
    }
    
    public static Iterable<int[]> extractCells(final SpellEffectActionInterface spellAction) {
        final WakfuRunningEffect runningEffect = spellAction.getRunningEffect();
        if (runningEffect == null) {
            return null;
        }
        final WakfuEffect genericEffect = ((RunningEffect<WakfuEffect, EC>)runningEffect).getGenericEffect();
        if (genericEffect == null) {
            return null;
        }
        final AreaOfEffect areaOfEffect = genericEffect.getAreaOfEffect();
        if (areaOfEffect == null) {
            return null;
        }
        final Point3 targetCell = runningEffect.getTargetCell();
        final EffectUser caster = runningEffect.getCaster();
        Iterable<int[]> iterable;
        if (caster != null) {
            iterable = areaOfEffect.getCells(targetCell.getX(), targetCell.getY(), targetCell.getZ(), caster.getWorldCellX(), caster.getWorldCellY(), targetCell.getZ(), caster.getDirection());
        }
        else {
            iterable = areaOfEffect.getCells(targetCell.getX(), targetCell.getY(), targetCell.getZ(), targetCell.getX(), targetCell.getY(), targetCell.getZ());
        }
        return iterable;
    }
    
    @Override
    public String getDescription() {
        return "Retourne un tableau contenant la liste des cellules (x,y) de l'aire d'effet de l'effet";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("cells", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("cells", null, LuaScriptParameterType.TABLE, true) };
    }
}
