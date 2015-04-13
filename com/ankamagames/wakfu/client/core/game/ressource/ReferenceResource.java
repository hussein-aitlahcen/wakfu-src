package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.common.game.resource.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ReferenceResource extends AbstractReferenceResource<ResourceEvolutionStep>
{
    protected static final Logger m_logger;
    private final short m_height;
    private final IntObjectLightWeightMap<String[]> m_gfxPaths;
    private final int m_iconGfxId;
    private final boolean m_useBigChallengeAps;
    
    public ReferenceResource(final int id, final short ressourceType, final short idealTempMin, final short idealTempMax, final short idealRainMin, final short idealRainMax, final boolean isBlocking, final short height, final TIntObjectHashMap<int[]> gfxIds, final int iconGfxId, final boolean useBigChallengeAps) {
        super(id, ressourceType, idealTempMin, idealTempMax, idealRainMin, idealRainMax, isBlocking);
        this.m_isBlocking = isBlocking;
        this.m_height = height;
        this.m_gfxPaths = new IntObjectLightWeightMap<String[]>(gfxIds.size());
        this.m_iconGfxId = iconGfxId;
        this.m_useBigChallengeAps = useBigChallengeAps;
        this.buildGfxPaths(gfxIds);
    }
    
    private void buildGfxPaths(final TIntObjectHashMap<int[]> gfxIds) {
        try {
            final String anmResourcePath = WakfuConfiguration.getInstance().getString("ANMResourcePath");
            gfxIds.forEachEntry(new TIntObjectProcedure<int[]>() {
                @Override
                public boolean execute(final int worldId, final int[] gfx) {
                    final String[] gfxFilename = new String[gfx.length];
                    for (int j = 0; j < gfx.length; ++j) {
                        gfxFilename[j] = String.format(anmResourcePath, gfx[j]);
                    }
                    ReferenceResource.this.m_gfxPaths.put(worldId, gfxFilename);
                    return true;
                }
            });
        }
        catch (PropertyException e) {
            ReferenceResource.m_logger.error((Object)"Erreur \u00e0 la r\u00e9cup\u00e9ration du chemin des anms de ressources", (Throwable)e);
        }
    }
    
    public String getGfx(final int worldX, final int worldY, final int worldId) {
        if (this.m_gfxPaths.isEmpty()) {
            ReferenceResource.m_logger.error((Object)("La resource " + this.getId() + " n'a pas de gfxId"));
            return null;
        }
        String[] gfxPaths = this.m_gfxPaths.get(worldId);
        if (gfxPaths == null) {
            gfxPaths = this.m_gfxPaths.get(-1);
            if (gfxPaths == null) {
                ReferenceResource.m_logger.error((Object)("Pas de gfxIds par d\u00e9faut pour la resource " + this.getId()));
                return null;
            }
        }
        final int gfxPattern = (int)(Math.abs(MathHelper.getLongFromTwoInt(worldX, worldY)) % gfxPaths.length);
        return gfxPaths[gfxPattern];
    }
    
    public String getResourceName() {
        return WakfuTranslator.getInstance().getString(12, this.getId(), new Object[0]);
    }
    
    public boolean isUseBigChallengeAps() {
        return this.m_useBigChallengeAps;
    }
    
    public boolean isBlocking() {
        return this.m_isBlocking;
    }
    
    public short getHeight() {
        return this.m_height;
    }
    
    public boolean canBeCollected(final byte evolutionStep) {
        return this.canBeCollected(this.getEvolutionStep(evolutionStep));
    }
    
    public boolean canBeCollected(final ResourceEvolutionStep evolutionStep) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        for (int i = 0, size = evolutionStep.getCollectsCount(); i < size; ++i) {
            final CollectAction collect = evolutionStep.getQuickCollect(i);
            if (collect.getCraftId() == 0) {
                return true;
            }
            if (player.getCraftHandler().contains(collect.getCraftId())) {
                return true;
            }
        }
        return false;
    }
    
    public int getIconGfxId() {
        return this.m_iconGfxId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceResource.class);
    }
}
