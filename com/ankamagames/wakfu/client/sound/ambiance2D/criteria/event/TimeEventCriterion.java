package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.regex.*;
import com.ankamagames.framework.java.util.*;
import gnu.trove.*;

public class TimeEventCriterion implements EventCriterion
{
    public static final byte CRITERION_ID = 4;
    public static final Pattern m_timeTestPattern;
    public static final Pattern m_timePattern;
    private TIntHashSet m_time;
    private static final int VERSION2KEY = 233811181;
    
    public TimeEventCriterion() {
        super();
        this.m_time = new TIntHashSet();
    }
    
    public TimeEventCriterion(final String time) {
        super();
        this.m_time = new TIntHashSet();
        this.m_time = extractTimeFromString(time);
    }
    
    public TimeEventCriterion(@NotNull final TIntHashSet time) {
        super();
        this.m_time = new TIntHashSet();
        this.m_time = time;
    }
    
    public String getTimeString() {
        final TIntIterator it = this.m_time.iterator();
        boolean first = true;
        final StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            if (!first) {
                sb.append(";");
            }
            else {
                first = false;
            }
            sb.append(it.next());
        }
        return sb.toString();
    }
    
    @Override
    public boolean isValid(final SoundEvent e) {
        if (e.getSoundEventType() != 4) {
            return false;
        }
        final TimeSoundEvent event = (TimeSoundEvent)e;
        return this.m_time.contains(event.getTime());
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        final int val = is.readInt();
        if (val == 233811181) {
            for (int i = 0, size = is.readByte(); i < size; ++i) {
                this.m_time.add(is.readInt());
            }
        }
        else {
            this.m_time.add(val);
        }
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeInt(233811181);
        os.writeByte((byte)this.m_time.size());
        final TIntIterator it = this.m_time.iterator();
        while (it.hasNext()) {
            os.writeInt(it.next());
        }
    }
    
    @Override
    public byte getCriterionId() {
        return 4;
    }
    
    @Override
    public EventCriterion clone() {
        return new TimeEventCriterion(this.m_time);
    }
    
    public static boolean isTimeValid(final String time) {
        if (time == null) {
            return false;
        }
        final Matcher matcherTest = TimeEventCriterion.m_timeTestPattern.matcher(time);
        return matcherTest.matches();
    }
    
    public static TIntHashSet extractTimeFromString(final String time) {
        final TIntHashSet times = new TIntHashSet();
        if (time == null) {
            return times;
        }
        final Matcher matcherTest = TimeEventCriterion.m_timeTestPattern.matcher(time);
        if (!matcherTest.matches()) {
            return times;
        }
        final Matcher matcher = TimeEventCriterion.m_timePattern.matcher(time);
        matcher.reset();
        while (matcher.find()) {
            String timeGroup = matcher.group(2);
            if (timeGroup == null) {
                timeGroup = matcher.group(3);
            }
            final int timeResult = PrimitiveConverter.getInteger(timeGroup);
            times.add(timeResult);
        }
        return times;
    }
    
    static {
        m_timeTestPattern = Pattern.compile("[0-9]+\\s*(;\\s*([0-9]+)\\s*)*$");
        m_timePattern = Pattern.compile("(([0-9]+)\\s*[;]\\s*)|(([0-9]+)$)");
    }
}
