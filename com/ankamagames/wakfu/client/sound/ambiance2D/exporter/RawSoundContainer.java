package com.ankamagames.wakfu.client.sound.ambiance2D.exporter;

import java.util.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;

public class RawSoundContainer
{
    public int m_id;
    public int m_strataId;
    public boolean m_parentContainer;
    public boolean m_eventContainer;
    public boolean m_audioMarkerContainer;
    public String m_name;
    public final ArrayList<ContainerCriterion> m_criteria;
    public final TLongArrayList m_soundSources;
    public byte m_eventType;
    public boolean m_eventIsLocalized;
    public EventCriterion m_eventCriterion;
    public int m_audioMarkerType;
    public byte m_busId;
    public byte m_busType;
    public int m_playType;
    public int m_transition;
    public int m_transitionInMinDuration;
    public int m_transitionInMaxDuration;
    public int m_transitionOutMinDuration;
    public int m_transitionOutMaxDuration;
    public boolean m_transitionUseSameInOutValues;
    public int m_loopMode;
    public int m_loopDuration;
    public int m_rollOffId;
    public int m_gain;
    public int m_maxGain;
    public int m_initialDelay;
    
    public RawSoundContainer() {
        super();
        this.m_strataId = -1;
        this.m_criteria = new ArrayList<ContainerCriterion>();
        this.m_soundSources = new TLongArrayList();
        this.m_transitionUseSameInOutValues = true;
        this.m_rollOffId = -1;
        this.m_gain = -1;
        this.m_maxGain = -1;
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readInt();
        this.m_strataId = istream.readInt();
        this.m_parentContainer = istream.readBooleanBit();
        this.m_eventContainer = istream.readBooleanBit();
        this.m_audioMarkerContainer = istream.readBooleanBit();
        this.m_name = istream.readString();
        this.m_gain = istream.readInt();
        this.m_maxGain = istream.readInt();
        if (this.m_eventContainer) {
            this.m_eventType = istream.readByte();
            this.m_eventIsLocalized = istream.readBooleanBit();
            final boolean hasEventCriterion = istream.readBooleanBit();
            if (hasEventCriterion) {
                (this.m_eventCriterion = EventCriteria.getFromId(istream.readByte()).createCriterion()).load(istream);
            }
        }
        if (this.m_audioMarkerContainer) {
            this.m_audioMarkerType = istream.readInt();
        }
        final int numCrit = istream.readInt();
        this.m_criteria.ensureCapacity(numCrit);
        for (int i = 0; i < numCrit; ++i) {
            this.m_criteria.add(AmbianceCriteria.read(istream));
        }
        final int numSources = istream.readInt();
        this.m_soundSources.ensureCapacity(numSources);
        for (int j = 0; j < numSources; ++j) {
            this.m_soundSources.add(istream.readLong());
        }
        if (!this.m_parentContainer) {
            this.m_busId = istream.readByte();
            this.m_busType = istream.readByte();
            this.m_playType = istream.readInt();
            this.m_transition = istream.readInt();
            this.m_transitionInMinDuration = istream.readInt();
            this.m_transitionInMaxDuration = istream.readInt();
            this.m_transitionOutMinDuration = istream.readInt();
            this.m_transitionOutMaxDuration = istream.readInt();
            this.m_transitionUseSameInOutValues = istream.readBooleanBit();
            this.m_loopMode = istream.readInt();
            this.m_loopDuration = istream.readInt();
            this.m_rollOffId = istream.readInt();
            this.m_initialDelay = istream.readInt();
        }
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeInt(this.m_strataId);
        ostream.writeBooleanBit(this.m_parentContainer);
        ostream.writeBooleanBit(this.m_eventContainer);
        ostream.writeBooleanBit(this.m_audioMarkerContainer);
        ostream.writeString(this.m_name);
        ostream.writeInt(this.m_gain);
        ostream.writeInt(this.m_maxGain);
        if (this.m_eventContainer) {
            ostream.writeByte(this.m_eventType);
            ostream.writeBooleanBit(this.m_eventIsLocalized);
            ostream.writeBooleanBit(this.m_eventCriterion != null);
            if (this.m_eventCriterion != null) {
                ostream.writeByte(this.m_eventCriterion.getCriterionId());
                this.m_eventCriterion.save(ostream);
            }
        }
        if (this.m_audioMarkerContainer) {
            ostream.writeInt(this.m_audioMarkerType);
        }
        ostream.writeInt(this.m_criteria.size());
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            AmbianceCriteria.write(ostream, this.m_criteria.get(i));
        }
        ostream.writeInt(this.m_soundSources.size());
        for (int i = 0, size = this.m_soundSources.size(); i < size; ++i) {
            ostream.writeLong(this.m_soundSources.get(i));
        }
        if (!this.m_parentContainer) {
            ostream.writeByte(this.m_busId);
            ostream.writeByte(this.m_busType);
            ostream.writeInt(this.m_playType);
            ostream.writeInt(this.m_transition);
            ostream.writeInt(this.m_transitionInMinDuration);
            ostream.writeInt(this.m_transitionInMaxDuration);
            ostream.writeInt(this.m_transitionOutMinDuration);
            ostream.writeInt(this.m_transitionOutMaxDuration);
            ostream.writeBooleanBit(this.m_transitionUseSameInOutValues);
            ostream.writeInt(this.m_loopMode);
            ostream.writeInt(this.m_loopDuration);
            ostream.writeInt(this.m_rollOffId);
            ostream.writeInt(this.m_initialDelay);
        }
    }
    
    private void writeProperty(final OutputBitStream ostream, final String propertyName, final String value) throws IOException {
        ostream.writeString(propertyName);
        ostream.writeString(value);
    }
    
    private void setProperties(final ExtendedDataInputStream istream, final String key, final String value) {
        if (key.equals("id")) {
            this.m_id = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("strataId")) {
            this.m_strataId = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("parentContainer")) {
            this.m_parentContainer = PrimitiveConverter.getBoolean(value);
        }
        else if (key.equals("eventContainer")) {
            this.m_eventContainer = PrimitiveConverter.getBoolean(value);
        }
        else if (key.equals("audioMarkerContainer")) {
            this.m_audioMarkerContainer = PrimitiveConverter.getBoolean(value);
        }
        else if (key.equals("audioMarkerType")) {
            this.m_audioMarkerType = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("eventType")) {
            this.m_eventType = PrimitiveConverter.getByte(value);
        }
        else if (key.equals("eventIsLocalized")) {
            this.m_eventIsLocalized = PrimitiveConverter.getBoolean(value);
        }
        else if (key.equals("name")) {
            this.m_name = value;
        }
        else if (key.equals("gain")) {
            this.m_gain = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("maxGain")) {
            this.m_maxGain = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("hasEventCriterion")) {
            final boolean hasEventCriterion = PrimitiveConverter.getBoolean(value);
            if (hasEventCriterion) {
                (this.m_eventCriterion = EventCriteria.getFromId(istream.readByte()).createCriterion()).load(istream);
            }
        }
        else if (key.equals("criteriaSize")) {
            final int numCrit = PrimitiveConverter.getInteger(value);
            this.m_criteria.ensureCapacity(numCrit);
            for (int i = 0; i < numCrit; ++i) {
                this.m_criteria.add(AmbianceCriteria.read(istream));
            }
        }
        else if (key.equals("sourcesSize")) {
            final int numSources = PrimitiveConverter.getInteger(value);
            this.m_soundSources.ensureCapacity(numSources);
            for (int i = 0; i < numSources; ++i) {
                this.m_soundSources.add(istream.readLong());
            }
        }
        else if (key.equals("busId")) {
            this.m_busId = PrimitiveConverter.getByte(value);
        }
        else if (key.equals("busType")) {
            this.m_busType = PrimitiveConverter.getByte(value);
        }
        else if (key.equals("playType")) {
            this.m_playType = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transition")) {
            this.m_transition = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transitionMinDuration")) {
            this.m_transitionInMinDuration = PrimitiveConverter.getInteger(value);
            this.m_transitionOutMinDuration = this.m_transitionInMinDuration;
        }
        else if (key.equals("transitionMaxDuration")) {
            this.m_transitionInMaxDuration = PrimitiveConverter.getInteger(value);
            this.m_transitionOutMaxDuration = this.m_transitionInMaxDuration;
        }
        else if (key.equals("transitionInMinDuration")) {
            this.m_transitionInMinDuration = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transitionInMaxDuration")) {
            this.m_transitionInMaxDuration = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transitionOutMinDuration")) {
            this.m_transitionOutMinDuration = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transitionOutMaxDuration")) {
            this.m_transitionOutMaxDuration = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("transitionUseSameInOutValues")) {
            this.m_transitionUseSameInOutValues = PrimitiveConverter.getBoolean(value);
        }
        else if (key.equals("loopMode")) {
            this.m_loopMode = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("loopDuration")) {
            this.m_loopDuration = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("rollOffId")) {
            this.m_rollOffId = PrimitiveConverter.getInteger(value);
        }
        else if (key.equals("initialDelay")) {
            this.m_initialDelay = PrimitiveConverter.getInteger(value);
        }
    }
    
    public void loadAsProperties(final ExtendedDataInputStream istream) throws IOException {
        final String rscTag = istream.readString();
        if (!rscTag.equals("rsc")) {
            return;
        }
        istream.readString();
        while (true) {
            final String key = istream.readString();
            final String value = istream.readString();
            if (key.equals("/rsc")) {
                break;
            }
            this.setProperties(istream, key, value);
        }
    }
    
    public void saveAsProperties(final OutputBitStream ostream) throws IOException {
        this.writeProperty(ostream, "rsc", "");
        this.writeProperty(ostream, "id", String.valueOf(this.m_id));
        this.writeProperty(ostream, "strataId", String.valueOf(this.m_strataId));
        this.writeProperty(ostream, "parentContainer", String.valueOf(this.m_parentContainer));
        this.writeProperty(ostream, "eventContainer", String.valueOf(this.m_eventContainer));
        this.writeProperty(ostream, "eventType", String.valueOf(this.m_eventType));
        this.writeProperty(ostream, "eventIsLocalized", String.valueOf(this.m_eventIsLocalized));
        this.writeProperty(ostream, "audioMarkerContainer", String.valueOf(this.m_audioMarkerContainer));
        this.writeProperty(ostream, "audioMarkerType", String.valueOf(this.m_audioMarkerType));
        this.writeProperty(ostream, "name", this.m_name);
        this.writeProperty(ostream, "gain", String.valueOf(this.m_gain));
        this.writeProperty(ostream, "maxGain", String.valueOf(this.m_maxGain));
        this.writeProperty(ostream, "hasEventCriterion", String.valueOf(this.m_eventCriterion != null));
        if (this.m_eventCriterion != null) {
            ostream.writeByte(this.m_eventCriterion.getCriterionId());
            this.m_eventCriterion.save(ostream);
        }
        this.writeProperty(ostream, "criteriaSize", String.valueOf(this.m_criteria.size()));
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            AmbianceCriteria.write(ostream, this.m_criteria.get(i));
        }
        this.writeProperty(ostream, "sourcesSize", String.valueOf(this.m_soundSources.size()));
        for (int i = 0, size = this.m_soundSources.size(); i < size; ++i) {
            ostream.writeLong(this.m_soundSources.get(i));
        }
        if (!this.m_parentContainer) {
            this.writeProperty(ostream, "busId", String.valueOf(this.m_busId));
            this.writeProperty(ostream, "busType", String.valueOf(this.m_busType));
            this.writeProperty(ostream, "playType", String.valueOf(this.m_playType));
            this.writeProperty(ostream, "transition", String.valueOf(this.m_transition));
            this.writeProperty(ostream, "transitionInMinDuration", String.valueOf(this.m_transitionInMinDuration));
            this.writeProperty(ostream, "transitionInMaxDuration", String.valueOf(this.m_transitionInMaxDuration));
            this.writeProperty(ostream, "transitionOutMinDuration", String.valueOf(this.m_transitionOutMinDuration));
            this.writeProperty(ostream, "transitionOutMaxDuration", String.valueOf(this.m_transitionOutMaxDuration));
            this.writeProperty(ostream, "transitionUseSameInOutValues", String.valueOf(this.m_transitionUseSameInOutValues));
            this.writeProperty(ostream, "loopMode", String.valueOf(this.m_loopMode));
            this.writeProperty(ostream, "loopDuration", String.valueOf(this.m_loopDuration));
            this.writeProperty(ostream, "rollOffId", String.valueOf(this.m_rollOffId));
            this.writeProperty(ostream, "initialDelay", String.valueOf(this.m_initialDelay));
        }
        this.writeProperty(ostream, "/rsc", "");
    }
    
    public void copyTo(final RawSoundContainer dest) {
        dest.m_id = this.m_id;
        dest.m_strataId = this.m_strataId;
        dest.m_parentContainer = this.m_parentContainer;
        dest.m_eventContainer = this.m_eventContainer;
        dest.m_audioMarkerContainer = this.m_audioMarkerContainer;
        dest.m_audioMarkerType = this.m_audioMarkerType;
        dest.m_eventType = this.m_eventType;
        dest.m_eventIsLocalized = this.m_eventIsLocalized;
        dest.m_name = this.m_name;
        dest.m_eventCriterion = ((this.m_eventCriterion != null) ? this.m_eventCriterion.clone() : null);
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            dest.m_criteria.add(this.m_criteria.get(i).clone());
        }
        for (int i = 0, size = this.m_soundSources.size(); i < size; ++i) {
            dest.m_soundSources.add(this.m_soundSources.get(i));
        }
        dest.m_busId = this.m_busId;
        dest.m_busType = this.m_busType;
        dest.m_playType = this.m_playType;
        dest.m_transition = this.m_transition;
        dest.m_transitionInMinDuration = this.m_transitionInMinDuration;
        dest.m_transitionInMaxDuration = this.m_transitionInMaxDuration;
        dest.m_transitionOutMinDuration = this.m_transitionOutMinDuration;
        dest.m_transitionOutMaxDuration = this.m_transitionOutMaxDuration;
        dest.m_transitionUseSameInOutValues = this.m_transitionUseSameInOutValues;
        dest.m_loopMode = this.m_loopMode;
        dest.m_loopDuration = this.m_loopDuration;
        dest.m_rollOffId = this.m_rollOffId;
        dest.m_gain = this.m_gain;
        dest.m_maxGain = this.m_maxGain;
        dest.m_initialDelay = this.m_initialDelay;
    }
    
    @Override
    public String toString() {
        return this.m_name + " (id=" + this.m_id + ")";
    }
}
