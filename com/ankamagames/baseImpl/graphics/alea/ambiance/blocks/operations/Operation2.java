package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

public enum Operation2
{
    And {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return (in1.floatValue() == 1.0f && in2.floatValue() == 1.0f) ? 1.0f : 0.0f;
        }
    }, 
    Equals {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return (in1.floatValue() == in2.floatValue()) ? 1.0f : 0.0f;
        }
    }, 
    Greater {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return (in1.floatValue() > in2.floatValue()) ? 1.0f : 0.0f;
        }
    }, 
    Lower {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return (in1.floatValue() < in2.floatValue()) ? 1.0f : 0.0f;
        }
    }, 
    Or {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return (in1.floatValue() == 1.0f || in2.floatValue() == 1.0f) ? 1.0f : 0.0f;
        }
    }, 
    Add {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return in1.floatValue() + in2.floatValue();
        }
    }, 
    Sub {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return in1.floatValue() - in2.floatValue();
        }
    }, 
    Mult {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return in1.floatValue() * in2.floatValue();
        }
    }, 
    Div {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return in1.floatValue() / in2.floatValue();
        }
    }, 
    Min {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return Math.min(in1.floatValue(), in2.floatValue());
        }
    }, 
    Max {
        @Override
        public Float compute(final Number in1, final Number in2) {
            return Math.max(in1.floatValue(), in2.floatValue());
        }
    };
    
    public abstract Float compute(final Number p0, final Number p1);
}
