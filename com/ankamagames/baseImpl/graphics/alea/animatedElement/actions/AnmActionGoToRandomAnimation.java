package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;
import org.apache.tools.ant.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import java.util.*;

public class AnmActionGoToRandomAnimation extends AnmAction
{
    protected static Logger m_logger;
    private static final String OPTIMIZED_KEY = "#optimized";
    private String[] m_animationNames;
    private byte[] m_percents;
    
    @Override
    public final boolean run(final AnimatedObject animatedElement) {
        if (this.m_animationNames == null) {
            return false;
        }
        int percent = MathHelper.random(101);
        for (int i = 0; i < this.m_animationNames.length; ++i) {
            percent -= this.m_percents[i];
            if (percent <= 0) {
                if (this.m_animationNames[i] != null) {
                    if (this.m_animationNames[i].length() != 0) {
                        return animatedElement.setAnimation(this.m_animationNames[i]);
                    }
                    break;
                }
            }
        }
        return false;
    }
    
    @Override
    public final void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        if (parametersCount == 0) {
            return;
        }
        final String first = bitStream.readString();
        if (first.equals("#optimized")) {
            final int count = (parametersCount - 1) / 2;
            this.m_animationNames = new String[count];
            for (int i = 0; i < count; ++i) {
                this.m_animationNames[i] = bitStream.readString();
            }
            this.m_percents = bitStream.readBytes(count);
            return;
        }
        final String[] m_parameters = new String[parametersCount];
        m_parameters[0] = first;
        for (int i = 1; i < parametersCount; ++i) {
            m_parameters[i] = bitStream.readString();
        }
        if (!this.fillAnimations(m_parameters)) {
            this.m_animationNames = null;
            this.m_percents = null;
        }
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        if (this.m_animationNames == null) {
            ostream.writeByte((byte)0);
            return;
        }
        ostream.writeByte((byte)(2 * this.m_animationNames.length + 1));
        ostream.writeString("#optimized");
        assert this.m_animationNames.length == this.m_percents.length;
        for (int i = 0; i < this.m_animationNames.length; ++i) {
            ostream.writeString(this.m_animationNames[i]);
        }
        for (int i = 0; i < this.m_percents.length; ++i) {
            ostream.writeByte(this.m_percents[i]);
        }
    }
    
    private boolean fillAnimations(final String[] parameters) throws LuaException {
        boolean animRead = false;
        final ArrayList<String> anims = new ArrayList<String>();
        final TByteArrayList values = new TByteArrayList(parameters.length);
        int last = -1;
        for (int i = 0; i < parameters.length; ++i) {
            final String param = parameters[i];
            try {
                final byte percent = Byte.parseByte(param.trim());
                if (!animRead) {
                    AnmActionGoToRandomAnimation.m_logger.warn((Object)"deux pourcentage se suivent ");
                }
                else {
                    if (values.size() > last) {
                        values.set(last, (byte)(values.get(last) + percent));
                    }
                    else {
                        for (int j = values.size(); j < anims.size() - 1; ++j) {
                            values.add((byte)0);
                        }
                        values.add(percent);
                    }
                    assert anims.size() == values.size();
                    animRead = false;
                }
            }
            catch (NumberFormatException e) {
                animRead = true;
                last = anims.indexOf(param);
                if (last == -1) {
                    last = anims.size();
                    anims.add(param);
                }
            }
        }
        int remain = 100;
        for (int k = 0; k < values.size(); ++k) {
            remain -= values.get(k);
        }
        final int r = anims.size() - values.size();
        if (remain < r) {
            throw new BuildException("probl\u00e8me de pourcentage (le total est > 100)");
        }
        if (r == 0) {
            final byte v = (byte)(remain / values.size());
            final int count = remain - v * values.size();
            for (int l = 0; l < count; ++l) {
                values.set(l, (byte)(values.get(l) + v + 1));
            }
            for (int l = count; l < values.size(); ++l) {
                values.set(l, (byte)(values.get(l) + v));
            }
        }
        else {
            final byte v = (byte)(remain / r);
            final int count = remain - v * r;
            for (int l = 0; l < count; ++l) {
                values.add((byte)(v + 1));
            }
            for (int l = count; l < r; ++l) {
                values.add(v);
            }
        }
        for (int m = values.size() - 1; m >= 0; --m) {
            if (values.get(m) == 0) {
                values.remove(m);
                anims.remove(m);
            }
        }
        int total = 0;
        for (int i2 = 0; i2 < values.size(); ++i2) {
            total += values.get(i2);
        }
        assert total == 100;
        assert anims.size() == values.size();
        anims.toArray(this.m_animationNames = new String[anims.size()]);
        this.m_percents = new byte[values.size()];
        for (int i2 = 0; i2 < this.m_percents.length; ++i2) {
            this.m_percents[i2] = values.get(i2);
        }
        return true;
    }
    
    @Override
    public final AnmActionTypes getType() {
        return AnmActionTypes.GO_TO_RANDOM_ANIMATION;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionGoToRandomAnimation)) {
            return false;
        }
        final AnmActionGoToRandomAnimation that = (AnmActionGoToRandomAnimation)o;
        return Arrays.equals(this.m_animationNames, that.m_animationNames) && Arrays.equals(this.m_percents, that.m_percents);
    }
    
    @Override
    public final int hashCode() {
        int result = this.getType().getId();
        result = 31 * result + ((this.m_animationNames != null) ? Arrays.hashCode(this.m_animationNames) : 0);
        result = 31 * result + ((this.m_percents != null) ? Arrays.hashCode(this.m_percents) : 0);
        return result;
    }
    
    static {
        AnmActionGoToRandomAnimation.m_logger = Logger.getLogger((Class)AnmActionGoToRandomAnimation.class);
    }
}
