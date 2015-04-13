package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.dungeon.*;

public class IsNationFirstInDungeonLadder extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_instanceId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsNationFirstInDungeonLadder.signatures;
    }
    
    public boolean isInteger() {
        return true;
    }
    
    public IsNationFirstInDungeonLadder(final ArrayList<ParserObject> args) {
        super();
        this.m_instanceId = args.get(0);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final short instanceId = (short)this.m_instanceId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (criterionUser == null) {
            throw new CriteriaExecutionException("On essaie de r\u00e9cup\u00e9rer la un r\u00e9sultat de nation dans un ladder sur une cible qui n'est pas un protecteur");
        }
        int protectorNationId = 0;
        if (criterionUser instanceof BasicCharacterInfo) {
            final ProtectorBase protector = ((BasicCharacterInfo)criterionUser).getProtector();
            if (protector != null) {
                protectorNationId = protector.getCurrentNationId();
            }
        }
        else if (criterionUser instanceof ProtectorBase) {
            protectorNationId = ((ProtectorBase)criterionUser).getCurrentNationId();
        }
        final AbstractDungeonLadder ladder = DungeonLadderManager.INSTANCE.getLadder(instanceId);
        if (ladder == null) {
            return -1;
        }
        if (ladder.getResults().isEmpty()) {
            return -1;
        }
        final DungeonLadderResult firstResult = ladder.getFirstResult();
        if (firstResult == null) {
            return -1;
        }
        final DungeonLadderResultCharacter firstCharacter = firstResult.getCharacters().get(0);
        return (firstCharacter.getNationId() == protectorNationId) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_NATION_FIRST_IN_DUNGEON_LADDER;
    }
    
    static {
        IsNationFirstInDungeonLadder.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsNationFirstInDungeonLadder.signatures.add(sig);
    }
}
