package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import java.util.*;

public class DimensionalBagPlacementPolicy extends AbstractDimensionalBagPlacementPolicy
{
    public static boolean checkPosition(final Point3 bagPosition, final WorldInfoManager.WorldInfo bagWorldInfo) {
        if (!bagWorldInfo.m_dimensionalBagAllowed) {
            return false;
        }
        final WakfuClientEnvironmentMap environmentMap = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMapFromCell(bagPosition.getX(), bagPosition.getY());
        if (environmentMap == null) {
            return false;
        }
        if (environmentMap.isHavroSteryl(bagPosition.getX(), bagPosition.getY(), bagPosition.getZ())) {
            return false;
        }
        final Resource res = ResourceManager.getInstance().getResource(bagPosition.getX(), bagPosition.getY());
        if (res != null && res.getReferenceResource().isBlockingMovement()) {
            return false;
        }
        final boolean isMarket = environmentMap.isMarket();
        final ArrayList<ClientMapInteractiveElement> elements = LocalPartitionManager.getInstance().getInteractiveElementsFromPartitions();
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (!AbstractDimensionalBagPlacementPolicy.checkCompatibilityWithElement(bagPosition, elements.get(i), isMarket)) {
                return false;
            }
        }
        return true;
    }
}
