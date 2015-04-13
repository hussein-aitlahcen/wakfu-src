package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class RectRingAOE extends AreaOfEffect
{
    public static String RECT_RING_AOE;
    private int m_innerWidthRadius;
    private int m_outerWidthRadius;
    private int m_innerHeightRadius;
    private int m_outerHeightRadius;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public RectRingAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RectRingAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return RectRingAOE.RECT_RING_AOE + "-w:" + this.m_innerWidthRadius + "-" + this.m_outerWidthRadius + " / h:" + this.m_innerHeightRadius + "-" + this.m_outerHeightRadius;
    }
    
    @Override
    public int getVisualRange() {
        return Math.min(this.m_innerWidthRadius + this.m_outerWidthRadius, this.m_innerHeightRadius + this.m_outerHeightRadius);
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type rectring : 4 param\u00e8tres attendus, 0 trouv\u00e9");
        }
        if (params.length != 4 && params.length != 2) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type rectring :4 param\u00e8tres attendus, " + params.length + " trouv\u00e9(s)");
        }
        if (params.length == 2) {
            this.m_innerWidthRadius = Math.min(params[0], params[1]);
            this.m_innerHeightRadius = this.m_innerWidthRadius;
            this.m_outerWidthRadius = Math.max(params[0], params[1]);
            this.m_outerHeightRadius = this.m_outerWidthRadius;
        }
        else {
            this.m_innerWidthRadius = params[0];
            this.m_innerHeightRadius = params[1];
            this.m_outerWidthRadius = params[2];
            this.m_outerHeightRadius = params[3];
            if (this.m_innerHeightRadius > this.m_outerHeightRadius) {
                throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type rectring : innerY > outerY");
            }
            if (this.m_innerWidthRadius > this.m_outerWidthRadius) {
                throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type rectring : innerX > outerX");
            }
        }
        this.m_patternList.clear();
        for (int x = 0; x <= this.m_outerWidthRadius; ++x) {
            for (int y = 0; y <= this.m_outerHeightRadius; ++y) {
                if (x >= this.m_innerWidthRadius || y >= this.m_innerHeightRadius) {
                    if (x == 0 && y == 0) {
                        this.m_patternList.add(new int[] { x, y });
                    }
                    else if (x == 0) {
                        this.m_patternList.add(new int[] { x, y });
                        this.m_patternList.add(new int[] { x, -y });
                    }
                    else if (y == 0) {
                        this.m_patternList.add(new int[] { x, y });
                        this.m_patternList.add(new int[] { -x, y });
                    }
                    else {
                        this.m_patternList.add(new int[] { x, y });
                        this.m_patternList.add(new int[] { -x, y });
                        this.m_patternList.add(new int[] { x, -y });
                        this.m_patternList.add(new int[] { -x, -y });
                    }
                }
            }
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return false;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.RECT_RING;
    }
    
    public int getOuterWidthRadius() {
        return this.m_outerWidthRadius;
    }
    
    public int getInnerWidthRadius() {
        return this.m_innerWidthRadius;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        areaOfEffects.add(this);
        return areaOfEffects;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_outerHeightRadius == 0 && this.m_outerWidthRadius == 0) {
            return AreaOfEffectShape.POINT;
        }
        if (this.m_outerHeightRadius == this.m_outerWidthRadius && this.m_innerHeightRadius == this.m_innerWidthRadius) {
            return AreaOfEffectShape.SQUARERING;
        }
        return AreaOfEffectShape.RECTANGLERING;
    }
    
    static {
        RectRingAOE.RECT_RING_AOE = "rectangular or square ring";
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Pourtour d'un carr\u00e9", new Parameter[] { new Parameter("demi c\u00f4t\u00e9 inf\u00e9rieur (cellule comprise dedans)"), new Parameter("demi c\u00f4t\u00e9 sup\u00e9rieur (cellule comprise dedans)") }), new AOEParameterList("Pourtour d'un rectangle", new Parameter[] { new Parameter("demi longueur inf\u00e9rieure X (cellule comprise dedans)"), new Parameter("demi longueur inf\u00e9rieure Y (cellule comprise dedans)"), new Parameter("demi longueur sup\u00e9rieure X (cellule comprise dedans)"), new Parameter("demi longueur sup\u00e9rieure Y (cellule comprise dedans)") }) });
    }
}
