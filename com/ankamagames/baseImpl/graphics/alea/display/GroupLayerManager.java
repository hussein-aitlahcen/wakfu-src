package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public class GroupLayerManager implements RenderProcessHandler<AleaWorldScene>
{
    private static final Logger m_logger;
    public static final int FADE_TIME = 300;
    private static final float[][] DEFAULT_VISIBILITY;
    private static final GroupLayerManager m_instance;
    private static final int OUTDOOR_KEY = 0;
    public static final int EMPTY_GROUP_KEY = 0;
    private boolean m_outdoorWasVisible;
    private int m_lastCameraKey;
    private final Fader m_fader;
    
    private GroupLayerManager() {
        super();
        this.m_outdoorWasVisible = true;
        this.m_lastCameraKey = Integer.MAX_VALUE;
        this.m_fader = new Fader();
    }
    
    public static GroupLayerManager getInstance() {
        return GroupLayerManager.m_instance;
    }
    
    public static boolean isOutdoor(final int maskKey) {
        return maskKey == 0;
    }
    
    public final float[] getLayerColor(@NotNull final Maskable element) {
        final int groupKey = element.getMaskKey();
        try {
            final float[] layerColor = this.m_fader.getIfExists(groupKey);
            if (layerColor != null) {
                return layerColor;
            }
        }
        catch (Exception e) {
            GroupLayerManager.m_logger.error((Object)"", (Throwable)e);
            return new float[] { 1.0f, 0.5f, 0.5f, 1.0f };
        }
        boolean visible;
        try {
            visible = this.isVisible(this.m_lastCameraKey, groupKey);
        }
        catch (Exception e2) {
            GroupLayerManager.m_logger.error((Object)"", (Throwable)e2);
            return new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        }
        float[] layerColor;
        if (visible) {
            layerColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        }
        else {
            layerColor = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        }
        this.m_fader.add(groupKey, layerColor);
        return layerColor;
    }
    
    public final boolean isVisible(final int from, final int key) {
        try {
            return WorldGroupManager.getInstance().layerVisible(from, key);
        }
        catch (Exception e) {
            return true;
        }
    }
    
    @Override
    public final void prepareBeforeRendering(@NotNull final AleaWorldScene aleaWorldScene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        if (this.m_fader.m_visibility == GroupLayerManager.DEFAULT_VISIBILITY) {
            this.m_fader.setCapacity(WorldGroupManager.getInstance().getLayerCount());
            assert this.m_fader.m_visibility != GroupLayerManager.DEFAULT_VISIBILITY;
        }
    }
    
    @Override
    public final void process(@NotNull final AleaWorldScene aleaWorldScene, final int deltaTime) {
        final AleaIsoCamera camera = aleaWorldScene.getIsoCamera();
        final int maskKey = camera.getMaskKey();
        boolean changed = false;
        if (maskKey != this.m_lastCameraKey) {
            this.m_fader.clearCache();
            final boolean outdoorVisible = this.isOutdoorVisibleFrom(maskKey);
            changed = (this.m_outdoorWasVisible != outdoorVisible);
            this.m_outdoorWasVisible = outdoorVisible;
            this.m_lastCameraKey = maskKey;
        }
        try {
            this.m_fader.update(deltaTime, changed);
        }
        catch (Exception e) {
            GroupLayerManager.m_logger.error((Object)("Probl\u00e8lme de fade de layer maskKey=" + maskKey));
            this.m_lastCameraKey = ((maskKey == 0) ? Integer.MAX_VALUE : 0);
        }
    }
    
    public final boolean outdoorVisibleFromCamera() {
        return this.isOutdoorVisibleFrom(this.m_lastCameraKey);
    }
    
    public final boolean isOutdoorVisibleFrom(final int maskKey) {
        return this.isVisible(maskKey, 0);
    }
    
    public final void clear() {
        this.m_lastCameraKey = Integer.MAX_VALUE;
        this.m_outdoorWasVisible = true;
        this.m_fader.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GroupLayerManager.class);
        (DEFAULT_VISIBILITY = new float[1][])[0] = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        m_instance = new GroupLayerManager();
    }
    
    private final class Fader
    {
        private static final float DELTA_GROUP_FADE = 0.0033333334f;
        private static final float DECREASE_SPEED_FACTOR = 2.0f;
        float[][] m_visibility;
        private final TIntArrayList m_visibilityIndex;
        private int[] m_sortedIndex;
        private boolean[] m_lastLayer;
        private boolean[] m_fadeDone;
        
        Fader() {
            super();
            this.m_visibilityIndex = new TIntArrayList(20);
            this.clear();
        }
        
        void clear() {
            this.m_visibility = GroupLayerManager.DEFAULT_VISIBILITY;
            this.m_lastLayer = null;
            this.m_fadeDone = null;
            this.m_visibilityIndex.reset();
            this.m_sortedIndex = null;
        }
        
        float[] getIfExists(final int key) {
            return this.m_visibility[(key > 0) ? key : (-key)];
        }
        
        void add(int key, final float[] values) {
            this.m_visibilityIndex.add(key);
            if (key < 0) {
                key = -key;
            }
            assert this.m_visibility[key] == null : "Le groupe est d\u00e9j\u00e0 en cache";
            this.m_visibility[key] = values;
            this.m_sortedIndex = null;
        }
        
        void setCapacity(final int size) {
            assert this.m_visibility == GroupLayerManager.DEFAULT_VISIBILITY;
            assert this.m_fadeDone == null;
            assert this.m_lastLayer == null;
            this.m_visibility = new float[size][];
            this.m_fadeDone = new boolean[size];
            this.m_lastLayer = new boolean[size];
        }
        
        private boolean decreaseColorThenAlpha(final float[] value, float delta) {
            if (value == null) {
                return false;
            }
            if (value[0] > 0.0f) {
                delta *= 2.0f;
                final int n = 0;
                value[n] -= delta;
                final int n2 = 1;
                value[n2] -= delta;
                final int n3 = 2;
                value[n3] -= delta;
                if (value[0] < 0.0f) {
                    final int n4 = 3;
                    value[n4] -= -value[0];
                    final int n5 = 0;
                    final int n6 = 1;
                    final int n7 = 2;
                    final float n8 = 0.0f;
                    value[n7] = n8;
                    value[n5] = (value[n6] = n8);
                    if (value[3] < 0.0f) {
                        value[3] = 0.0f;
                    }
                }
                return false;
            }
            if (value[3] > 0.0f) {
                final int n9 = 3;
                value[n9] -= delta;
                if (value[3] < 0.0f) {
                    value[3] = 0.0f;
                }
                return false;
            }
            return true;
        }
        
        private boolean decreaseColorAndAlpha(final float[] value, final float delta) {
            if (value == null) {
                return false;
            }
            if (value[0] > 0.0f) {
                final int n = 0;
                value[n] -= delta;
                final int n2 = 1;
                value[n2] -= delta;
                final int n3 = 2;
                value[n3] -= delta;
                final int n4 = 3;
                value[n4] -= delta;
                if (value[0] < 0.0f) {
                    final int n5 = 0;
                    final int n6 = 1;
                    final int n7 = 2;
                    final int n8 = 3;
                    final float n9 = 0.0f;
                    value[n7] = (value[n8] = n9);
                    value[n5] = (value[n6] = n9);
                }
                return false;
            }
            return true;
        }
        
        private boolean increaseAlphaThenColor(final float[] value, float delta) {
            if (value == null) {
                return false;
            }
            if (value[3] < 1.0f) {
                final int n = 3;
                value[n] += 2.0f * delta;
                if (value[3] > 1.0f) {
                    delta = value[3] - 1.0f;
                    final int n2 = 0;
                    value[n2] += delta;
                    final int n3 = 1;
                    value[n3] += delta;
                    final int n4 = 2;
                    value[n4] += delta;
                    value[3] = 1.0f;
                    if (value[0] > 1.0f) {
                        final int n5 = 0;
                        final int n6 = 1;
                        final int n7 = 2;
                        final float n8 = 1.0f;
                        value[n7] = n8;
                        value[n5] = (value[n6] = n8);
                    }
                }
                return false;
            }
            if (value[0] < 1.0f) {
                final int n9 = 0;
                value[n9] += delta;
                final int n10 = 1;
                value[n10] += delta;
                final int n11 = 2;
                value[n11] += delta;
                if (value[0] > 1.0f) {
                    final int n12 = 0;
                    final int n13 = 1;
                    final int n14 = 2;
                    final float n15 = 1.0f;
                    value[n14] = n15;
                    value[n12] = (value[n13] = n15);
                }
                return false;
            }
            return true;
        }
        
        private boolean increaseColorAndAlpha(final float[] value, final float delta) {
            if (value == null) {
                return false;
            }
            if (value[0] < 1.0f) {
                final int n = 0;
                value[n] += delta;
                final int n2 = 1;
                value[n2] += delta;
                final int n3 = 2;
                value[n3] += delta;
                final int n4 = 3;
                value[n4] += delta;
                if (value[0] > 1.0f) {
                    final int n5 = 0;
                    final int n6 = 1;
                    final int n7 = 2;
                    final int n8 = 3;
                    final float n9 = 1.0f;
                    value[n7] = (value[n8] = n9);
                    value[n5] = (value[n6] = n9);
                }
                return false;
            }
            return true;
        }
        
        void update(final int deltaTime, final boolean changed) {
            final float delta = 0.0033333334f * deltaTime;
            final int size = this.m_visibilityIndex.size();
            if (this.m_sortedIndex == null) {
                this.m_sortedIndex = new int[size];
                this.m_visibilityIndex.toNativeArray(this.m_sortedIndex, 0, size);
                Arrays.sort(this.m_sortedIndex);
            }
            for (int i = 0; i < size; ++i) {
                final int key = this.m_sortedIndex[i];
                final int idx = (key > 0) ? key : (-key);
                if (!this.m_fadeDone[idx]) {
                    if (changed) {
                        this.m_lastLayer[idx] = true;
                    }
                    final float[] value = this.m_visibility[idx];
                    if (GroupLayerManager.isOutdoor(key) || this.m_lastLayer[idx]) {
                        if (this.fadeOutdoor(key, value, delta)) {
                            this.removeKey(idx, key);
                        }
                    }
                    else if (this.fadeIndoor(key, value, delta)) {
                        this.removeKey(idx, key);
                    }
                }
            }
        }
        
        private void removeKey(final int idx, final int key) {
            this.m_visibility[idx] = null;
            this.m_lastLayer[idx] = false;
            TroveUtils.removeFirstValue(this.m_visibilityIndex, key);
        }
        
        private boolean fadeOutdoor(final int key, final float[] value, final float delta) {
            if (GroupLayerManager.this.isVisible(GroupLayerManager.this.m_lastCameraKey, key)) {
                if (this.increaseAlphaThenColor(value, delta)) {
                    this.m_fadeDone[(key > 0) ? key : (-key)] = true;
                }
                return false;
            }
            return this.decreaseColorThenAlpha(value, delta) && (this.m_fadeDone[(key > 0) ? key : (-key)] = true);
        }
        
        private boolean fadeIndoor(final int key, final float[] value, final float delta) {
            if (GroupLayerManager.this.isVisible(GroupLayerManager.this.m_lastCameraKey, key)) {
                if (this.increaseColorAndAlpha(value, delta)) {
                    this.m_fadeDone[(key > 0) ? key : (-key)] = true;
                }
                return false;
            }
            return this.decreaseColorAndAlpha(value, delta) && (this.m_fadeDone[(key > 0) ? key : (-key)] = true);
        }
        
        void clearCache() {
            if (this.m_fadeDone == null) {
                return;
            }
            for (int i = 0, size = this.m_fadeDone.length; i < size; ++i) {
                this.m_fadeDone[i] = false;
            }
            for (int i = 0, size = this.m_lastLayer.length; i < size; ++i) {
                this.m_lastLayer[i] = false;
            }
        }
    }
}
