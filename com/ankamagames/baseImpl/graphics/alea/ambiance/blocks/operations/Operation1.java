package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

import com.ankamagames.framework.kernel.core.maths.*;

public enum Operation1
{
    Abs {
        @Override
        public Float compute(final Number number) {
            return Math.abs(number.floatValue());
        }
    }, 
    Not {
        @Override
        public Float compute(final Number number) {
            return -number.floatValue();
        }
    }, 
    Round {
        @Override
        public Float compute(final Number number) {
            return (float)MathHelper.fastRound(number.floatValue());
        }
    };
    
    public abstract Float compute(final Number p0);
}
