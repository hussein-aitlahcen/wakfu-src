package com.ankamagames.baseImpl.graphics.core.partitions;

import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import gnu.trove.*;

public abstract class GamePartition<Partition extends GamePartition> extends AbstractPartition<Partition>
{
    private static final Logger m_logger;
    private static final TLongObjectProcedure<DynamicElement> DE_SPAWN_DYNAMICS;
    private final TLongObjectHashMap<DynamicElement> m_dynamicElements;
    
    public GamePartition() {
        super();
        this.m_dynamicElements = new TLongObjectHashMap<DynamicElement>();
    }
    
    public void initialize() {
        this.loadDynamicElements();
    }
    
    public void deSpawnContent() {
        this.m_dynamicElements.forEachEntry(GamePartition.DE_SPAWN_DYNAMICS);
    }
    
    protected void clear() {
        this.m_dynamicElements.clear();
    }
    
    private void loadDynamicElements() {
        final ClientEnvironmentMap map = EnvironmentMapManager.getInstance().getMap((short)this.getX(), (short)this.getY());
        if (map == null) {
            return;
        }
        final DynamicElementDef[] definitions = map.getDynamicElements();
        for (int i = 0; i < definitions.length; ++i) {
            final DynamicElementDef definition = definitions[i];
            try {
                final DynamicElement elt = DynamicElement.fromDefinition(map, definition);
                elt.initialize();
                this.spawnDynamicElement(elt);
            }
            catch (IOException e) {
                GamePartition.m_logger.error((Object)("Impossible de charger le fichier d'animation de l'\u00e9l\u00e9ment dynamic " + definition), (Throwable)e);
            }
            catch (Exception e2) {
                GamePartition.m_logger.error((Object)("Impossible de charger l'\u00e9l\u00e9ment dynamic " + definition), (Throwable)e2);
            }
        }
    }
    
    public void spawnDynamicElement(final DynamicElement element) {
        if (this.m_dynamicElements.containsKey(element.getId())) {
            GamePartition.m_logger.error((Object)("Impossible d'ajouter un \u00e9l\u00e9ments interactif d'ID=" + element.getId() + " \u00e0 la partition " + this + " qui le contient d\u00e9j\u00e0."));
            return;
        }
        this.m_dynamicElements.put(element.getId(), element);
        SimpleAnimatedElementManager.getInstance().addAnimatedElement(element);
    }
    
    public void deSpawnDynamicElement(final long elementId) {
        final DynamicElement element = this.m_dynamicElements.remove(elementId);
        if (element != null) {
            element.clear();
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(element);
        }
    }
    
    public boolean forEachDynamicElement(final TObjectProcedure<DynamicElement> procedure) {
        return this.m_dynamicElements.forEachValue(procedure);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GamePartition.class);
        DE_SPAWN_DYNAMICS = new TLongObjectProcedure<DynamicElement>() {
            @Override
            public boolean execute(final long a, final DynamicElement b) {
                b.clear();
                SimpleAnimatedElementManager.getInstance().removeAnimatedElement(b);
                return true;
            }
        };
    }
}
