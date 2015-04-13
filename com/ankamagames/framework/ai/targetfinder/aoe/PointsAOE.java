package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class PointsAOE extends AreaOfEffect
{
    public static String FREE_POINTS_FORM;
    private static final PointsAOE m_staticInstance;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public PointsAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PointsAOE.PARAMETERS_LIST_SET;
    }
    
    public static PointsAOE getInstance() {
        return PointsAOE.m_staticInstance;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return PointsAOE.FREE_POINTS_FORM;
    }
    
    @Override
    public int getVisualRange() {
        return 0;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null || params.length % 2 != 0) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type Point : modulo2 attendu, " + params.length + " fourni(s)");
        }
        this.m_patternList.clear();
        for (int i = 0; i < params.length; i += 2) {
            this.m_patternList.add(new int[] { params[i], params[i + 1] });
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return false;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.FREE_POINTS_FORM;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_patternList.size() == 1) {
            return AreaOfEffectShape.POINT;
        }
        return AreaOfEffectShape.SPECIAL;
    }
    
    static {
        PointsAOE.FREE_POINTS_FORM = "forme \u00e0 base de points";
        m_staticInstance = new PointsAOE();
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Liste de 1 point : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1") }), new AOEParameterList("Liste de 2 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2") }), new AOEParameterList("Liste de 3 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3") }), new AOEParameterList("Liste de 4 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4") }), new AOEParameterList("Liste de 5 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5") }), new AOEParameterList("Liste de 6 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6") }), new AOEParameterList("Liste de 7 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7") }), new AOEParameterList("Liste de 8 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8") }), new AOEParameterList("Liste de 9 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9") }), new AOEParameterList("Liste de 10 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10") }), new AOEParameterList("Liste de 11 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11") }), new AOEParameterList("Liste de 12 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11"), new Parameter("x12"), new Parameter("y12") }), new AOEParameterList("Liste de 13 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11"), new Parameter("x12"), new Parameter("y12"), new Parameter("x13"), new Parameter("y13") }), new AOEParameterList("Liste de 14 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11"), new Parameter("x12"), new Parameter("y12"), new Parameter("x13"), new Parameter("y13"), new Parameter("x14"), new Parameter("y14") }), new AOEParameterList("Liste de 15 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11"), new Parameter("x12"), new Parameter("y12"), new Parameter("x13"), new Parameter("y13"), new Parameter("x14"), new Parameter("y14"), new Parameter("x15"), new Parameter("y15") }), new AOEParameterList("Liste de 16 points : prendre l'axe sud-est pour construire", new Parameter[] { new Parameter("x1"), new Parameter("y1"), new Parameter("x2"), new Parameter("y2"), new Parameter("x3"), new Parameter("y3"), new Parameter("x4"), new Parameter("y4"), new Parameter("x5"), new Parameter("y5"), new Parameter("x6"), new Parameter("y6"), new Parameter("x7"), new Parameter("y7"), new Parameter("x8"), new Parameter("y8"), new Parameter("x9"), new Parameter("y9"), new Parameter("x10"), new Parameter("y10"), new Parameter("x11"), new Parameter("y11"), new Parameter("x12"), new Parameter("y12"), new Parameter("x13"), new Parameter("y13"), new Parameter("x14"), new Parameter("y14"), new Parameter("x15"), new Parameter("y15"), new Parameter("x16"), new Parameter("y16") }) });
    }
}
