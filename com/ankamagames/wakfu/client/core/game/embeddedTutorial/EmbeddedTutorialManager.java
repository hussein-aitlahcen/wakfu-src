package com.ankamagames.wakfu.client.core.game.embeddedTutorial;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.wakfu.client.core.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.io.*;
import gnu.trove.*;

public class EmbeddedTutorialManager implements FieldProvider, DialogUnloadListener
{
    public static final String TUTORIALS_FIELD = "tutorials";
    public static final String ACTIVATED_FIELD = "oneActivated";
    private static final Logger m_logger;
    private static final EmbeddedTutorialManager m_instance;
    private static final String TUTORIALS_TAG = "tutorials";
    private static final String TUTORIAL_TAG = "tutorial";
    private static final String PART_TAG = "part";
    private static final String NAME_TAG = "name";
    private static final String ACTIVATED_TAG = "activated";
    private static final String EVENT_TAG = "eventId";
    private static final String ID_TAG = "id";
    private final String[] FIELDS;
    private final TShortObjectHashMap<Tutorial> m_tutorials;
    private final TIntHashSet m_launchedTutorials;
    private boolean m_enabled;
    private static final boolean ENABLED = false;
    private int m_launchedActionId;
    private String m_linkedDialogId;
    
    public EmbeddedTutorialManager() {
        super();
        this.FIELDS = new String[] { "tutorials", "oneActivated" };
        this.m_tutorials = new TShortObjectHashMap<Tutorial>();
        this.m_launchedTutorials = new TIntHashSet();
        this.m_enabled = true;
    }
    
    public static EmbeddedTutorialManager getInstance() {
        return EmbeddedTutorialManager.m_instance;
    }
    
    public static int getScriptId(final short tutorialId, final short partId) {
        final StringBuilder id = new StringBuilder("2");
        final String stringTutorialId = String.valueOf(tutorialId);
        final String stringPartId = String.valueOf(partId);
        final StringBuilder builderTutorialId = new StringBuilder("000");
        builderTutorialId.replace(builderTutorialId.length() - stringTutorialId.length(), builderTutorialId.length(), stringTutorialId);
        id.append((CharSequence)builderTutorialId);
        final StringBuilder builderPartId = new StringBuilder("0000");
        builderPartId.replace(builderPartId.length() - stringPartId.length(), builderPartId.length(), stringPartId);
        id.append((CharSequence)builderPartId);
        return Integer.valueOf(id.toString());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("tutorials")) {
            return this.m_tutorials.getValues();
        }
        if (fieldName.equals("oneActivated")) {
            return !this.desactivated();
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
    
    public void activateDesactivateTutorial(final short tutorialId) {
        final Tutorial tutorial = this.m_tutorials.get(tutorialId);
        tutorial.setActivated(!tutorial.isActivated());
        try {
            this.saveTutorialXml(this.getTutorialFileURL());
        }
        catch (Exception e) {
            EmbeddedTutorialManager.m_logger.error((Object)"Exception", (Throwable)e);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, this.getFields());
    }
    
    public void saveTutorialXml(final String XMLFileName) throws Exception {
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        document.setRootNode(new XMLDocumentNode("tutorials", null));
        final TShortObjectIterator<Tutorial> it = this.m_tutorials.iterator();
        while (it.hasNext()) {
            it.advance();
            final Tutorial tutorial = it.value();
            final XMLDocumentNode tutorialEntry = new XMLDocumentNode("tutorial", null);
            document.getRootNode().addChild(tutorialEntry);
            tutorialEntry.addParameter(new XMLNodeAttribute("name", tutorial.getName()));
            tutorialEntry.addParameter(new XMLNodeAttribute("id", String.valueOf(tutorial.getId())));
            final TShortObjectIterator<TutorialPart> partIt = tutorial.getPartIterator();
            while (partIt.hasNext()) {
                partIt.advance();
                final TutorialPart part = partIt.value();
                final XMLDocumentNode stepEntry = new XMLDocumentNode("part", null);
                tutorialEntry.addChild(stepEntry);
                stepEntry.addParameter(new XMLNodeAttribute("id", String.valueOf(part.getId())));
                stepEntry.addParameter(new XMLNodeAttribute("name", part.getName()));
                stepEntry.addParameter(new XMLNodeAttribute("activated", String.valueOf(part.isActivated())));
                stepEntry.addParameter(new XMLNodeAttribute("eventId", String.valueOf(part.getEventId())));
            }
        }
        accessor.create(XMLFileName);
        accessor.write(document);
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    public void activateDesactivateTutorialPart(final short tutorialId, final short partId) {
        final Tutorial tutorial = this.m_tutorials.get(tutorialId);
        final TutorialPart part = tutorial.getPart(partId);
        part.setActivated(!part.isActivated());
        if (this.isLaunchedTutorial(tutorial.getId(), part.getId())) {
            getInstance().removeLaunchedTutorial(tutorial.getId(), part.getId());
        }
        try {
            this.saveTutorialXml(this.getTutorialFileURL());
        }
        catch (Exception e) {
            EmbeddedTutorialManager.m_logger.error((Object)"Exception", (Throwable)e);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, this.getFields());
    }
    
    public void activateDesactivateTutorials() throws Exception {
        final boolean desactivated = this.desactivated();
        final TShortObjectIterator<Tutorial> it = this.m_tutorials.iterator();
        while (it.hasNext()) {
            it.advance();
            final Tutorial tutorial = it.value();
            tutorial.setActivated(desactivated);
            this.saveTutorialXml(this.getTutorialFileURL());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, this.getFields());
    }
    
    private String getTutorialFileURL() throws MalformedURLException, PropertyException {
        return new URL(WakfuConfiguration.getContentPath("tutorialFile")).getFile();
    }
    
    private boolean desactivated() {
        final TShortObjectIterator<Tutorial> it = this.m_tutorials.iterator();
        while (it.hasNext()) {
            it.advance();
            if (!it.value().isActivated()) {
                return true;
            }
        }
        return false;
    }
    
    public void ensureTutorialsFileComplete(final String fileToComplete, final EmbeddedTutorialManager tutorialManager) throws Exception {
        final DocumentAccessor<XMLDocumentContainer> accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        accessor.open(fileToComplete);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        boolean changeFound = false;
        final TShortObjectIterator<Tutorial> it = tutorialManager.m_tutorials.iterator();
        while (it.hasNext()) {
            it.advance();
            final Tutorial tutorial = it.value();
            final ArrayList<DocumentEntry> tutorialEntries = document.getEntriesByName("tutorial");
            DocumentEntry correspondingTutorial = null;
            boolean isTutorialExisting = false;
            for (final DocumentEntry tutorialEntry : tutorialEntries) {
                if (tutorialEntry.getParameterByName("name") == null) {
                    EmbeddedTutorialManager.m_logger.error((Object)"Nom de tutorial invalide dans le chargement des tutoriaux");
                }
                final String name = tutorialEntry.getParameterByName("name").getStringValue();
                if (tutorial.getName().equals(name)) {
                    correspondingTutorial = tutorialEntry;
                    isTutorialExisting = true;
                    break;
                }
            }
            if (correspondingTutorial == null) {
                changeFound = true;
                correspondingTutorial = new XMLDocumentNode("tutorial", null);
                correspondingTutorial.addParameter(new XMLNodeAttribute("name", tutorial.getName()));
                correspondingTutorial.addParameter(new XMLNodeAttribute("id", String.valueOf(tutorial.getId())));
                tutorialEntries.add(correspondingTutorial);
                document.getRootNode().addChild(correspondingTutorial);
            }
            final TShortObjectIterator<TutorialPart> it2 = tutorial.getPartIterator();
            while (it2.hasNext()) {
                it2.advance();
                final TutorialPart tutorialPart = it2.value();
                if (isTutorialExisting) {
                    final ArrayList<DocumentEntry> partEntries = correspondingTutorial.getChildrenByName("part");
                    boolean partFound = false;
                    for (final DocumentEntry partDoc : partEntries) {
                        if (tutorialPart.getId() == partDoc.getParameterByName("id").getIntValue()) {
                            partFound = true;
                            break;
                        }
                    }
                    if (partFound) {
                        continue;
                    }
                    changeFound = true;
                    this.addPartToTutorial(tutorialPart, correspondingTutorial);
                }
                else {
                    this.addPartToTutorial(tutorialPart, correspondingTutorial);
                }
            }
        }
        if (changeFound) {
            accessor.create(fileToComplete);
            accessor.write(document);
        }
    }
    
    private void addPartToTutorial(final TutorialPart tutorialPart, final DocumentEntry tutorial) {
        final XMLDocumentNode partEntry = new XMLDocumentNode("part", null);
        partEntry.addParameter(new XMLNodeAttribute("id", String.valueOf(tutorialPart.getId())));
        tutorial.addChild(partEntry);
        if (tutorialPart.getName() != null && tutorialPart.getName().length() > 0) {
            partEntry.addParameter(new XMLNodeAttribute("name", tutorialPart.getName()));
            tutorial.addChild(partEntry);
        }
        partEntry.addParameter(new XMLNodeAttribute("activated", "true"));
        tutorial.addChild(partEntry);
        partEntry.addParameter(new XMLNodeAttribute("eventId", String.valueOf(tutorialPart.getEventId())));
        tutorial.addChild(partEntry);
    }
    
    public void launchTutorial(final TutorialEvent event, final String dialogId) {
        if (this.m_enabled) {
            return;
        }
    }
    
    public void stopTutorial() {
    }
    
    public void loadFromXMLFile(final String XMLFileName) throws Exception {
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        accessor.open(XMLFileName);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> tutorialEntries = document.getEntriesByName("tutorial");
        if (tutorialEntries == null) {
            return;
        }
        for (final DocumentEntry tutorialEntry : tutorialEntries) {
            if (tutorialEntry.getParameterByName("name") == null) {
                EmbeddedTutorialManager.m_logger.error((Object)"Nom de tutorial invalide dans le chargement des tutoriaux");
            }
            else {
                final String name = tutorialEntry.getParameterByName("name").getStringValue();
                final int tutorialId = tutorialEntry.getParameterByName("id").getIntValue();
                final ArrayList<DocumentEntry> partEntries = tutorialEntry.getDirectChildrenByName("part");
                final TShortObjectHashMap<TutorialPart> parts = new TShortObjectHashMap<TutorialPart>();
                for (int i = 0; i < partEntries.size(); ++i) {
                    final DocumentEntry partEntry = partEntries.get(i);
                    final DocumentEntry partName = partEntry.getParameterByName("name");
                    final DocumentEntry partActivation = partEntry.getParameterByName("activated");
                    final DocumentEntry partEventId = partEntry.getParameterByName("eventId");
                    final DocumentEntry partId = partEntry.getParameterByName("id");
                    if (partName == null || partActivation == null || partEventId == null || partId == null) {
                        EmbeddedTutorialManager.m_logger.error((Object)("Step incorrecte dans le XML de tutorial " + name));
                    }
                    else {
                        parts.put((short)partId.getIntValue(), new TutorialPart((short)partId.getIntValue(), partName.getStringValue(), partActivation.getBooleanValue(), partEventId.getByteValue()));
                    }
                }
                Tutorial tutorial = this.m_tutorials.get((short)tutorialId);
                if (tutorial != null) {
                    continue;
                }
                tutorial = new Tutorial((short)tutorialId, name, parts);
                this.m_tutorials.put((short)tutorialId, tutorial);
            }
        }
    }
    
    public boolean isLaunchedTutorial(final short tutorialId, final short partId) {
        return this.m_launchedTutorials.contains(MathHelper.getIntFromTwoShort(tutorialId, partId));
    }
    
    public void removeLaunchedTutorial(final short tutorialId, final short partId) {
        this.m_launchedActionId = -1;
        if (!this.m_launchedTutorials.remove(MathHelper.getIntFromTwoShort(tutorialId, partId))) {
            EmbeddedTutorialManager.m_logger.error((Object)"[TUTORIAL] On essaye de supprimer un TutorialAction des launchedTutorial alors que celui-ci n'y est pas.");
        }
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    @Override
    public void dialogUnloaded(final String id) {
        if (id != null && id.equals(this.m_linkedDialogId)) {
            this.stopTutorial();
        }
    }
    
    @Override
    public String toString() {
        return "EmbeddedTutorialManager{m_tutorialsCount=" + this.m_tutorials.size() + ", m_enabled=" + this.m_enabled + ", m_launchedActionId=" + this.m_launchedActionId + ", m_linkedDialogId='" + this.m_linkedDialogId + '\'' + ", m_launchedTutorialsCount=" + this.m_launchedTutorials.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmbeddedTutorialManager.class);
        m_instance = new EmbeddedTutorialManager();
    }
}
