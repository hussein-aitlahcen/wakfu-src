package com.ankamagames.baseImpl.graphics.game.interactiveElement;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import gnu.trove.*;

public class AnimatedElementSceneViewManager extends AbstractInteractiveElementManager<ClientInteractiveAnimatedElementSceneView>
{
    public static final Logger m_logger;
    private static final int MAX_VISION_DISTANCE = 18;
    private static final AnimatedElementSceneViewManager m_instance;
    protected final TLongObjectHashMap<ClientInteractiveAnimatedElementSceneView> m_interactiveAnimatedElement;
    ArrayList<ClientInteractiveAnimatedElementSceneView> m_interactiveAnimatedElementToDelete;
    protected final TLongObjectHashMap<TLongArrayList> m_elementIdsOnPosition;
    private final ArrayList<InteractiveElementCreationListener> m_permanentCreationListeners;
    private final ArrayList<InteractiveElementDestructionListener> m_permanentDestructionListeners;
    private final ArrayList<InteractiveElementCreationListener> m_creationListeners;
    private final ArrayList<InteractiveElementDestructionListener> m_destructionListeners;
    private final TLongObjectProcedure<ClientInteractiveAnimatedElementSceneView> COPY_PROCEDURE;
    
    public static AnimatedElementSceneViewManager getInstance() {
        return AnimatedElementSceneViewManager.m_instance;
    }
    
    protected AnimatedElementSceneViewManager() {
        super();
        this.m_interactiveAnimatedElementToDelete = new ArrayList<ClientInteractiveAnimatedElementSceneView>(8);
        this.COPY_PROCEDURE = new TLongObjectProcedure<ClientInteractiveAnimatedElementSceneView>() {
            @Override
            public boolean execute(final long a, final ClientInteractiveAnimatedElementSceneView b) {
                return false;
            }
        };
        this.m_interactiveAnimatedElement = new TLongObjectHashMap<ClientInteractiveAnimatedElementSceneView>();
        this.m_elementIdsOnPosition = new TLongObjectHashMap<TLongArrayList>();
        this.m_permanentCreationListeners = new ArrayList<InteractiveElementCreationListener>();
        this.m_permanentDestructionListeners = new ArrayList<InteractiveElementDestructionListener>();
        this.m_creationListeners = new ArrayList<InteractiveElementCreationListener>();
        this.m_destructionListeners = new ArrayList<InteractiveElementDestructionListener>();
    }
    
    public void addElement(final ClientInteractiveAnimatedElementSceneView element) {
        if (!this.m_interactiveAnimatedElement.containsKey(element.getId())) {
            element.canBeOccluder(element.isOccluder());
            this.m_interactiveAnimatedElement.put(element.getId(), element);
            final Point3 position = element.getWorldCoordinates();
            final TLongArrayList pos = this.getOrCreateElementIdsOnPosition(position.getX(), position.getY());
            pos.add(element.getId());
            this.fireElementAdded(element, position.getX(), position.getY(), position.getZ());
            this.fireElementCreation(element);
        }
        else {
            AnimatedElementSceneViewManager.m_logger.warn((Object)("Impossible d'ajouter l'\u00e9l\u00e9ment id=" + element.getId() + " en " + element.getWorldCellX() + ":" + element.getWorldCellY() + " car il en existe d\u00e9j\u00e0 avec cet ID."));
        }
    }
    
    public void removeElement(final ClientInteractiveAnimatedElementSceneView element) {
        if (element != null) {
            this.m_interactiveAnimatedElement.remove(element.getId());
            final long coordOnLong = this.getElementCoordAsLong(element);
            this.removePosition(element, coordOnLong);
            this.fireElementDestruction(element);
            element.release();
        }
        else {
            AnimatedElementSceneViewManager.m_logger.error((Object)"Impossible de retirer un element null");
        }
    }
    
    private long getElementCoordAsLong(final ClientInteractiveAnimatedElementSceneView element) {
        return MathHelper.getLongFromTwoInt(element.getWorldCoordinates().getX(), element.getWorldCoordinates().getY());
    }
    
    public void removeElement(final long elementId) {
        final ClientInteractiveAnimatedElementSceneView element = this.m_interactiveAnimatedElement.get(elementId);
        if (element != null) {
            this.removeElement(element);
        }
        else {
            AnimatedElementSceneViewManager.m_logger.warn((Object)("Impossible de supprimer un element d'ID " + elementId + " qui n'existe pas"));
        }
    }
    
    public void removeAllElements() {
        AnimatedElementSceneViewManager.m_logger.info((Object)"Supression de tout les Element du AnimatedElementSceneViewManager.");
        ClientInteractiveAnimatedElementSceneView[] elements = new ClientInteractiveAnimatedElementSceneView[this.m_interactiveAnimatedElement.size()];
        final ClientInteractiveAnimatedElementSceneView[] arr$;
        elements = (arr$ = this.m_interactiveAnimatedElement.getValues(elements));
        for (final ClientInteractiveAnimatedElementSceneView element : arr$) {
            this.removeElement(element);
        }
        if (!this.m_interactiveAnimatedElement.isEmpty()) {
            AnimatedElementSceneViewManager.m_logger.error((Object)("Il reste encore " + this.m_interactiveAnimatedElement.size() + " apr\u00e8s la supression !!!"));
        }
    }
    
    public boolean isElementAlreadyExist(final int worldX, final int worldY) {
        return this.m_elementIdsOnPosition.contains(MathHelper.getLongFromTwoInt(worldX, worldY));
    }
    
    public ClientInteractiveAnimatedElementSceneView getElement(final long l) {
        return this.m_interactiveAnimatedElement.get(l);
    }
    
    public ArrayList<ClientInteractiveAnimatedElementSceneView> getElements(final int worldX, final int worldY) {
        final ArrayList<ClientInteractiveAnimatedElementSceneView> views = new ArrayList<ClientInteractiveAnimatedElementSceneView>();
        final TLongArrayList viewIds = this.m_elementIdsOnPosition.get(MathHelper.getLongFromTwoInt(worldX, worldY));
        if (viewIds != null) {
            for (int i = viewIds.size() - 1; i >= 0; --i) {
                views.add(this.m_interactiveAnimatedElement.get(viewIds.get(i)));
            }
            return views;
        }
        return views;
    }
    
    public int getElementsCount() {
        return this.m_interactiveAnimatedElement.size();
    }
    
    @Override
    public void process(final AleaWorldScene worldScene, final int deltaTime) {
        final int count = this.m_interactiveAnimatedElement.size();
        final ClientInteractiveAnimatedElementSceneView[] elements = new ClientInteractiveAnimatedElementSceneView[count];
        this.m_interactiveAnimatedElement.getValues(elements);
        this.m_displayedElements.clear();
        for (final ClientInteractiveAnimatedElementSceneView element : elements) {
            if (element.update(worldScene, deltaTime)) {
                if (element.canBeDeleted()) {
                    this.m_interactiveAnimatedElementToDelete.add(element);
                }
                else {
                    this.m_displayedElements.add((T)element);
                }
            }
        }
        for (int i = 0, size = this.m_interactiveAnimatedElementToDelete.size(); i < size; ++i) {
            this.removeElement(this.m_interactiveAnimatedElementToDelete.get(i));
        }
        this.m_interactiveAnimatedElementToDelete.clear();
    }
    
    @Override
    public void prepareBeforeRendering(final AleaWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_displayedElements.clear();
        final TLongObjectIterator<ClientInteractiveAnimatedElementSceneView> elementIter = this.m_interactiveAnimatedElement.iterator();
        while (elementIter.hasNext()) {
            elementIter.advance();
            final ClientInteractiveAnimatedElementSceneView element = elementIter.value();
            if (element.addToScene(scene)) {
                this.m_displayedElements.add((T)element);
            }
            this.updateScreenPosition(element, scene);
        }
    }
    
    public void clear() {
        final TLongObjectIterator<ClientInteractiveAnimatedElementSceneView> elementIter = this.m_interactiveAnimatedElement.iterator();
        while (elementIter.hasNext()) {
            elementIter.advance();
            final ClientInteractiveAnimatedElementSceneView element = elementIter.value();
            element.dispose();
            element.release();
        }
        this.m_interactiveAnimatedElement.clear();
        this.m_displayedElements.clear();
        this.m_creationListeners.clear();
        this.m_creationListeners.addAll(this.m_permanentCreationListeners);
        this.m_destructionListeners.clear();
        this.m_destructionListeners.addAll(this.m_permanentDestructionListeners);
    }
    
    public void onElementMove(final long id, final int newPosX, final int newPosY) {
        final ClientInteractiveAnimatedElementSceneView element = this.m_interactiveAnimatedElement.get(id);
        if (element != null) {
            final long coordOnLong = this.getElementCoordAsLong(element);
            this.removePosition(element, coordOnLong);
            final TLongArrayList pos = this.getOrCreateElementIdsOnPosition(newPosX, newPosY);
            pos.add(element.getId());
        }
    }
    
    private TLongArrayList getOrCreateElementIdsOnPosition(final int newPosX, final int newPosY) {
        final long XY = MathHelper.getLongFromTwoInt(newPosX, newPosY);
        TLongArrayList pos = this.m_elementIdsOnPosition.get(XY);
        if (pos == null) {
            pos = new TLongArrayList();
            this.m_elementIdsOnPosition.put(XY, pos);
        }
        return pos;
    }
    
    private void removePosition(final ClientInteractiveAnimatedElementSceneView element, final long coordOnLong) {
        final TLongArrayList pos = this.m_elementIdsOnPosition.get(coordOnLong);
        if (pos == null) {
            AnimatedElementSceneViewManager.m_logger.error((Object)("Pas d'elements enregistr\u00e9s \u00e0 la position " + element.getWorldCoordinates() + " l'IE " + element));
            return;
        }
        TroveUtils.removeFirstValue(pos, element.getId());
        if (pos.isEmpty()) {
            this.m_elementIdsOnPosition.remove(coordOnLong);
        }
    }
    
    private void fireElementCreation(final ClientInteractiveAnimatedElementSceneView element) {
        for (int i = 0, size = this.m_creationListeners.size(); i < size; ++i) {
            this.m_creationListeners.get(i).onInteractiveElementCreation(element);
        }
    }
    
    private void fireElementDestruction(final ClientInteractiveAnimatedElementSceneView element) {
        for (int i = this.m_destructionListeners.size() - 1; i >= 0; --i) {
            this.m_destructionListeners.get(i).onInteractiveElementDestruction(element);
        }
    }
    
    public void addPermanentCreationListener(final InteractiveElementCreationListener listener) {
        if (this.m_permanentCreationListeners.contains(listener)) {
            return;
        }
        this.m_permanentCreationListeners.add(listener);
        this.addCreationListener(listener);
    }
    
    public void addPermanentDestructionListener(final InteractiveElementDestructionListener listener) {
        if (this.m_permanentDestructionListeners.contains(listener)) {
            return;
        }
        this.m_permanentDestructionListeners.add(listener);
        this.addDestructionListener(listener);
    }
    
    public void addCreationListener(final InteractiveElementCreationListener listener) {
        if (!this.m_creationListeners.contains(listener)) {
            this.m_creationListeners.add(listener);
        }
    }
    
    public void addDestructionListener(final InteractiveElementDestructionListener listener) {
        if (!this.m_destructionListeners.contains(listener)) {
            this.m_destructionListeners.add(listener);
        }
    }
    
    public void removeDestructionListener(final InteractiveElementDestructionListener listener) {
        if (this.m_destructionListeners.contains(listener)) {
            this.m_destructionListeners.remove(listener);
        }
    }
    
    public Iterator<ClientInteractiveAnimatedElementSceneView> getDisplayedElementIterator() {
        return (Iterator<ClientInteractiveAnimatedElementSceneView>)this.m_displayedElements.iterator();
    }
    
    public TLongObjectIterator<ClientInteractiveAnimatedElementSceneView> getInteractiveElementIterator() {
        return this.m_interactiveAnimatedElement.iterator();
    }
    
    @Override
    public void setUndefinedMaskLayer() {
        this.m_interactiveAnimatedElement.forEachValue(new TObjectProcedure<ClientInteractiveAnimatedElementSceneView>() {
            @Override
            public boolean execute(final ClientInteractiveAnimatedElementSceneView object) {
                MaskableHelper.setUndefined(object);
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimatedElementSceneViewManager.class);
        m_instance = new AnimatedElementSceneViewManager();
    }
}
