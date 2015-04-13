package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class HideFightOccluders implements Command
{
    private static final Logger m_logger;
    public static final int SHOW_ALL = 0;
    public static final int SHOW_WITH_ALPHA = 1;
    public static final int SHOW_NONE = 2;
    private static int m_state;
    private static float m_alpha;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        hideFightOccluders(HideFightOccluders.m_state = (HideFightOccluders.m_state + 1) % 3);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY, HideFightOccluders.m_state);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static int getStateFromAlpha(final float alpha) {
        return (alpha == 0.0f) ? 2 : ((alpha == 1.0f) ? 0 : 1);
    }
    
    private static int getFadeTimeFromState(final int state) {
        switch (state) {
            case 0: {
                return 50;
            }
            case 1:
            case 2: {
                return 1000;
            }
            default: {
                HideFightOccluders.m_logger.error((Object)("state inconnu " + state));
                return 0;
            }
        }
    }
    
    public static void hideFightOccluders(final int state) {
        hideFightOccluders(state, 0.4f);
    }
    
    public static void hideFightOccluders(final int state, final float alpha) {
        hideFightOccluders(state, alpha, getFadeTimeFromState(state));
    }
    
    public static void hideFightOccluders(final int state, final float alpha, final int fadeTime) {
        final boolean hide = state != 0;
        HideFightOccluders.m_alpha = alpha;
        switch (HideFightOccluders.m_state = state) {
            case 0: {
                break;
            }
            case 1: {
                HiddenElementManager.getInstance().setFullAlpha(HideFightOccluders.m_alpha);
                break;
            }
            case 2: {
                HiddenElementManager.getInstance().setFullAlpha(0.0f);
                break;
            }
            default: {
                HideFightOccluders.m_logger.error((Object)("state inconnu " + state));
                break;
            }
        }
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        HiddenElementManager.getInstance().hide(hide, fadeTime);
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            return;
        }
        fight.getCellLightModifier().setColor(alpha);
        final FightMap fightMap = fight.getFightMap();
        if (fightMap == null) {
            return;
        }
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final int minZ = fightMap.getMinZ();
        final int maxZ = fightMap.getMaxZ();
        final ArrayList<DisplayedScreenElement> elements = new ArrayList<DisplayedScreenElement>(64);
        final Iterator<DisplayedScreenMap> iter = DisplayedScreenWorld.getInstance().getMapsIterator();
        int lastCellX = Integer.MIN_VALUE;
        int lastCellY = Integer.MIN_VALUE;
        final ArrayList<Hiddable> elementsToHide = new ArrayList<Hiddable>();
        while (iter.hasNext()) {
            final DisplayedScreenMap screenMap = iter.next();
            final DisplayedScreenElement[] displayedElements = screenMap.getElements();
            for (int elementIndex = 0; elementIndex < displayedElements.length; ++elementIndex) {
                final DisplayedScreenElement displayedElement = displayedElements[elementIndex];
                final ScreenElement element = displayedElement.getElement();
                if (element.getCellZ() > minZ) {
                    final int cellX = element.getCellX();
                    final int cellY = element.getCellY();
                    if (cellY >= minY) {
                        if (cellX >= minX) {
                            if (fightMap.isInsideOrBorder(cellX, cellY)) {
                                if (element.getCellZ() > maxZ && !element.getCommonProperties().isMoveTop()) {
                                    elementsToHide.add(displayedElement);
                                }
                            }
                            else {
                                if (cellX != lastCellX || cellY != lastCellY) {
                                    lastCellX = cellX;
                                    lastCellY = cellY;
                                    elements.clear();
                                    DisplayedScreenWorld.getInstance().getElements(cellX, cellY, elements, ElementFilter.NOT_EMPTY);
                                    for (int i = 0; i < elements.size(); ++i) {
                                        for (int j = i + 1; j < elements.size(); ++j) {
                                            final DisplayedScreenElement a = elements.get(i);
                                            final DisplayedScreenElement b = elements.get(j);
                                            if (b.getHashCode() < a.getHashCode()) {
                                                elements.set(i, b);
                                                elements.set(j, a);
                                            }
                                        }
                                    }
                                }
                                if (elements.size() == 0 || element.getCellZ() > elements.get(0).getElement().getCellZ()) {
                                    elementsToHide.add(displayedElement);
                                }
                            }
                        }
                    }
                }
            }
        }
        ResourceManager.getInstance().foreachResource(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource resource) {
                if (testAnimatedElement(resource, minX, minY, minZ, maxZ)) {
                    elementsToHide.add(resource);
                }
                return true;
            }
        });
        final Iterator<ClientInteractiveAnimatedElementSceneView> displayedElementIter = AnimatedElementSceneViewManager.getInstance().getDisplayedElementIterator();
        while (displayedElementIter.hasNext()) {
            final ClientInteractiveAnimatedElementSceneView displayedElement2 = displayedElementIter.next();
            if (displayedElement2.getInteractiveElement() instanceof DestructibleMachine) {
                continue;
            }
            if (!testAnimatedElement(displayedElement2, minX, minY, minZ, maxZ)) {
                continue;
            }
            elementsToHide.add(displayedElement2);
        }
        for (final Hiddable element2 : elementsToHide) {
            element2.hide(hide);
        }
    }
    
    private static boolean testAnimatedElement(final AnimatedElement elt, final int minX, final int minY, final int minZ, final int maxZ) {
        return elt.getWorldCellAltitude() + elt.getVisualHeight() > minZ && elt.getWorldCellY() >= minY && elt.getWorldCellX() >= minX;
    }
    
    public static int getState() {
        return HideFightOccluders.m_state;
    }
    
    public static void hide(final AnimatedElement elt) {
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            return;
        }
        final FightMap fightMap = fight.getFightMap();
        if (fightMap == null) {
            return;
        }
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final int minZ = fightMap.getMinZ();
        final int maxZ = fightMap.getMaxZ();
        if (testAnimatedElement(elt, minX, minY, minZ, maxZ)) {
            elt.hide(HideFightOccluders.m_state != 0);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)HideFightOccluders.class);
        HideFightOccluders.m_state = 0;
    }
}
