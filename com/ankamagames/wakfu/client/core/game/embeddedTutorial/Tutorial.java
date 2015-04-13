package com.ankamagames.wakfu.client.core.game.embeddedTutorial;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

class Tutorial implements FieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String ACTIVATED_FIELD = "isActivated";
    private final String[] FIELDS;
    private final short m_id;
    private final String m_name;
    private final TShortObjectHashMap<TutorialPart> m_parts;
    private final TByteShortHashMap m_partsByEvent;
    
    Tutorial(final short id, final String name, final TShortObjectHashMap<TutorialPart> parts) {
        super();
        this.FIELDS = new String[] { "name", "isActivated" };
        this.m_partsByEvent = new TByteShortHashMap();
        this.m_id = id;
        this.m_name = name;
        (this.m_parts = parts).forEachEntry(new TShortObjectProcedure<TutorialPart>() {
            @Override
            public boolean execute(final short key, final TutorialPart part) {
                final byte eventId = part.getEventId();
                if (!Tutorial.this.m_partsByEvent.containsKey(eventId)) {
                    Tutorial.this.m_partsByEvent.put(eventId, key);
                }
                else {
                    Tutorial.m_logger.error((Object)"Plusieurs tutorials sont mapp\u00e9s sur le m\u00eame eventId");
                }
                return true;
            }
        });
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("tooltipTutorial." + this.m_name);
        }
        if (fieldName.equals("isActivated")) {
            return this.isActivated();
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isActivated() {
        final TShortObjectIterator<TutorialPart> it = this.m_parts.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().isActivated()) {
                return true;
            }
        }
        return false;
    }
    
    public void setActivated(final boolean activated) {
        final EmbeddedTutorialManager tutorialManager = EmbeddedTutorialManager.getInstance();
        this.m_parts.forEachValue(new TObjectProcedure<TutorialPart>() {
            @Override
            public boolean execute(final TutorialPart part) {
                part.setActivated(activated);
                if (tutorialManager.isLaunchedTutorial(Tutorial.this.m_id, part.getId())) {
                    tutorialManager.removeLaunchedTutorial(Tutorial.this.m_id, part.getId());
                }
                return true;
            }
        });
    }
    
    TShortObjectIterator<TutorialPart> getPartIterator() {
        return this.m_parts.iterator();
    }
    
    public TutorialPart getPart(final short partId) {
        return this.m_parts.get(partId);
    }
    
    public TutorialPart getPartByEventId(final byte eventID) {
        return this.m_parts.get(this.m_partsByEvent.get(eventID));
    }
    
    @Override
    public String toString() {
        return "Tutorial{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_partsCount=" + this.m_parts.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)Tutorial.class);
    }
}
