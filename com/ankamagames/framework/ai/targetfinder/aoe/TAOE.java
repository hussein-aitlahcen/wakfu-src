package com.ankamagames.framework.ai.targetfinder.aoe;

import java.util.*;
import com.ankamagames.framework.external.*;

public class TAOE extends AreaOfEffect
{
    public static String T_AOE;
    private int m_barmidlength;
    private int m_footlength;
    private List<int[]> m_patternList;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    public TAOE() {
        super();
        this.m_patternList = new ArrayList<int[]>(1);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return TAOE.PARAMETERS_LIST_SET;
    }
    
    @Override
    public ArrayList<AreaOfEffect> getSubAOEs() {
        final ArrayList<AreaOfEffect> areaOfEffects = new ArrayList<AreaOfEffect>();
        switch (this.getOrderingMethod()) {
            case 1: {
                for (int i = 0; i <= this.m_footlength; ++i) {
                    final PointsAOE npoint = new PointsAOE();
                    npoint.initialize(new int[] { i, 0 });
                    areaOfEffects.add(npoint);
                }
                final int[] points = new int[this.m_barmidlength * 4];
                for (int j = 1; j <= this.m_barmidlength; ++j) {
                    final int index = (j - 1) * 4;
                    points[index + 0] = this.m_footlength;
                    points[index + 1] = j;
                    points[index + 2] = this.m_footlength;
                    points[index + 3] = -j;
                }
                final PointsAOE point = new PointsAOE();
                point.initialize(points);
                areaOfEffects.add(point);
                break;
            }
            case 2: {
                final int[] points = new int[this.m_barmidlength * 4];
                for (int j = 1; j <= this.m_barmidlength; ++j) {
                    final int index = (j - 1) * 4;
                    points[index + 0] = this.m_footlength;
                    points[index + 1] = j;
                    points[index + 2] = this.m_footlength;
                    points[index + 3] = -j;
                }
                final PointsAOE point = new PointsAOE();
                point.initialize(points);
                areaOfEffects.add(point);
                for (int k = this.m_footlength; k >= 0; --k) {
                    final PointsAOE npoint2 = new PointsAOE();
                    npoint2.initialize(new int[] { k, 0 });
                    areaOfEffects.add(npoint2);
                }
                break;
            }
            default: {
                areaOfEffects.add(this);
                break;
            }
        }
        return areaOfEffects;
    }
    
    @Override
    public List<int[]> getPattern() {
        return this.m_patternList;
    }
    
    @Override
    public String toShortDescription() {
        return TAOE.T_AOE + "-" + this.m_barmidlength + "-" + this.m_footlength;
    }
    
    @Override
    public int getVisualRange() {
        return this.m_footlength;
    }
    
    @Override
    public void initialize(final int[] params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type T : 2 param\u00e8tres attendus, 0 trouv\u00e9");
        }
        if (params.length != 2) {
            throw new IllegalArgumentException("Param\u00e8tres invalides pour une AOE de type T : 2 param\u00e8tres attendus, " + params.length + " trouv\u00e9(s)");
        }
        this.m_barmidlength = Math.abs(params[0]);
        this.m_footlength = Math.abs(params[1]);
        this.m_patternList.clear();
        this.m_patternList.add(new int[] { 0, 0 });
        for (int i = 1; i <= this.m_footlength; ++i) {
            this.m_patternList.add(new int[] { i, 0 });
        }
        for (int i = 1; i <= this.m_barmidlength; ++i) {
            this.m_patternList.add(new int[] { this.m_footlength, i });
            this.m_patternList.add(new int[] { this.m_footlength, -i });
        }
    }
    
    @Override
    protected boolean isInvariantByRotation() {
        return false;
    }
    
    @Override
    public AreaOfEffectEnum getType() {
        return AreaOfEffectEnum.T;
    }
    
    public int getBarmidlength() {
        return this.m_barmidlength;
    }
    
    public int getFootlength() {
        return this.m_footlength;
    }
    
    @Override
    public AreaOfEffectShape getShape() {
        if (this.m_barmidlength == 0 && this.m_footlength == 0) {
            return AreaOfEffectShape.POINT;
        }
        if (this.m_barmidlength == 0) {
            return AreaOfEffectShape.VLINE;
        }
        if (this.m_footlength == 0) {
            return AreaOfEffectShape.HLINE;
        }
        return AreaOfEffectShape.CIRCLERING;
    }
    
    static {
        TAOE.T_AOE = "t";
        PARAMETERS_LIST_SET = new AOEParametersListSet(new ParameterList[] { new AOEParameterList("Zone en T (order 0: defaut, 1: pied->barre, 2: barre->pied)", new Parameter[] { new Parameter("largeur de la barre (par rapport au centre : 1 = barre de 3 cellules)"), new Parameter("hauteur du pied (par rapport au centre : 1 = barre de 2)") }) });
    }
}
