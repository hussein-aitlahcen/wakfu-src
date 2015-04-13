package com.ankamagames.baseImpl.graphics.alea.utils;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class WorldSceneInteractionUtils
{
    private static final short MAX_DELTA_FOR_SAME_Z = 2;
    private static final MobileFilter ACCEPT_ALL_MOBILES;
    
    @Nullable
    public static PathFindResult getPathSolutionFromMouseCoordinatesWithSpecificPathFinder(final PathMobile pathMobile, final Point3 target, final PathFinderParameters parameters, TopologyMapInstanceSet mapInstanceSet, final PathFinder pathFinder) {
        if (target == null) {
            return null;
        }
        final int srcX = pathMobile.getCurrentWorldX();
        final int srcY = pathMobile.getCurrentWorldY();
        final short srcZ = (pathMobile.getCarrierMobile() != null) ? pathMobile.getCarrierMobile().getWorldCellAltitude() : pathMobile.getWorldCellAltitude();
        pathFinder.clearStartCells();
        pathFinder.addStartCell(srcX, srcY, srcZ);
        pathFinder.setParameters(parameters);
        pathFinder.setMoverCaracteristics(pathMobile.getHeight(), pathMobile.getPhysicalRadius(), pathMobile.getJumpCapacity());
        TopologyMapManager.setMoverCaracteristics(pathMobile.getHeight(), pathMobile.getPhysicalRadius(), pathMobile.getJumpCapacity());
        boolean useCustomMapInstanceSet = false;
        if (mapInstanceSet == null) {
            mapInstanceSet = new TopologyMapInstanceSet();
            useCustomMapInstanceSet = true;
        }
        pathFinder.setTopologyMapInstanceSet(mapInstanceSet);
        final int destX = target.getX();
        final int destY = target.getY();
        final short destZ = TopologyMapManager.getNearestWalkableZ(destX, destY, target.getZ());
        if (useCustomMapInstanceSet) {
            TopologyMapManager.getTopologyMapInstances(srcX, srcY, destX, destY, mapInstanceSet);
        }
        pathFinder.setStopCell(destX, destY, destZ);
        final int pathSize = pathFinder.findPath();
        if (pathSize == -1 || pathSize == 0) {
            return null;
        }
        final long[] pathData = pathFinder.getPathData().clone();
        if (pathData == null) {
            return null;
        }
        final int[][] path = new int[pathSize][3];
        for (int i = 0; i < pathSize; ++i) {
            final int cellIndex = pathSize - 1 - i;
            path[i][0] = PathFinder.getXFrom3DHashCode(pathData[cellIndex]);
            path[i][1] = PathFinder.getYFrom3DHashCode(pathData[cellIndex]);
            path[i][2] = PathFinder.getZFrom3DHashCode(pathData[cellIndex]);
        }
        return new PathFindResult(path);
    }
    
    public static Point3 getNearestPoint3(final AleaWorldScene worldScene, final int mouseX, final int mouseY, final boolean mobileSelectable) {
        return getNearestPoint3(worldScene, mouseX, mouseY, mobileSelectable, mobileSelectable);
    }
    
    @Nullable
    public static Point3 getNearestPoint3(final AleaWorldScene worldScene, final int mouseX, final int mouseY, final boolean singleCellMobileSelectable, final boolean multiCellsMobileSelectable) {
        if (singleCellMobileSelectable || multiCellsMobileSelectable) {
            final ArrayList<Mobile> hitMobiles = worldScene.getMobilesUnderMousePoint(mouseX, mouseY);
            if (hitMobiles.size() != 0) {
                final Mobile mobile = hitMobiles.get(0);
                if (mobile != null && ((singleCellMobileSelectable && mobile.getPhysicalRadius() == 0) || (multiCellsMobileSelectable && mobile.getPhysicalRadius() > 0))) {
                    return mobile.getWorldCoordinates();
                }
            }
        }
        final ArrayList<DisplayedScreenElement> hitElements = worldScene.getDisplayedElementsUnderMousePoint(mouseX, mouseY, 0.0f, DisplayedScreenElementComparator.Z_ORDER);
        if (hitElements == null || hitElements.isEmpty()) {
            return null;
        }
        final ScreenElement screenElement = hitElements.get(0).getElement();
        return new Point3(screenElement.getCellX(), screenElement.getCellY(), screenElement.getCellZ());
    }
    
    @Nullable
    public static ArrayList<Point3> getNearestsPoint3(final AleaWorldScene worldScene, final int mouseX, final int mouseY, final boolean singleCellMobileSelectable, final boolean multiCellsMobileSelectable) {
        return getNearestsFilteredPoint3(worldScene, mouseX, mouseY, singleCellMobileSelectable, multiCellsMobileSelectable, WorldSceneInteractionUtils.ACCEPT_ALL_MOBILES);
    }
    
    @Nullable
    public static ArrayList<Point3> getNearestsFilteredPoint3(final AleaWorldScene worldScene, final int mouseX, final int mouseY, final boolean singleCellMobileSelectable, final boolean multiCellsMobileSelectable, final MobileFilter mobileFilter) {
        if (singleCellMobileSelectable || multiCellsMobileSelectable) {
            final ArrayList<Mobile> hitMobiles = worldScene.getMobilesUnderMousePoint(mouseX, mouseY);
            if (!hitMobiles.isEmpty()) {
                final Mobile mobile = hitMobiles.get(0);
                if (mobile != null && ((singleCellMobileSelectable && mobile.getPhysicalRadius() == 0) || (multiCellsMobileSelectable && mobile.getPhysicalRadius() > 0)) && mobileFilter.accept(mobile)) {
                    final ArrayList<Point3> list = new ArrayList<Point3>(1);
                    list.add(mobile.getWorldCoordinates());
                    return list;
                }
            }
        }
        final ArrayList<DisplayedScreenElement> hitElements = worldScene.getDisplayedElementsUnderMousePoint(mouseX, mouseY, 0.0f, DisplayedScreenElementComparator.Z_ORDER);
        if (hitElements == null || hitElements.isEmpty()) {
            return null;
        }
        return createCoordinatesList(hitElements);
    }
    
    private static ArrayList<Point3> createCoordinatesList(final ArrayList<DisplayedScreenElement> hitElements) {
        final int size = hitElements.size();
        final ArrayList<Point3> list = new ArrayList<Point3>(size);
        for (int i = 0; i < size; ++i) {
            final ScreenElement elt = hitElements.get(i).getElement();
            list.add(new Point3(elt.getCellX(), elt.getCellY(), elt.getCellZ()));
        }
        return list;
    }
    
    static {
        ACCEPT_ALL_MOBILES = new MobileFilter() {
            @Override
            public boolean accept(final Mobile mobile) {
                return true;
            }
        };
    }
}
