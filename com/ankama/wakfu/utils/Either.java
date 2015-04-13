package com.ankama.wakfu.utils;

import com.google.common.collect.*;
import com.google.common.base.*;

public abstract class Either<E, D>
{
    public static <E, D> Either<E, D> error(final E e) {
        return new EitherError<E, D>(e);
    }
    
    public static <E, D> Either<E, D> valid(final D d) {
        return new EitherValid<E, D>(d);
    }
    
    public static <E, D> Iterable<E> collectErrors(final Iterable<Either<E, D>> eithers) {
        return (Iterable<E>)Iterables.transform(Iterables.filter((Iterable)eithers, Predicates.instanceOf((Class)EitherError.class)), (Function)new Function<Either<E, ?>, E>() {
            public E apply(final Either<E, ?> input) {
                return input.getError();
            }
        });
    }
    
    public abstract <E1> Either<E1, D> transformError(final Function<? super E, ? extends E1> p0);
    
    public abstract <D1> Either<E, D1> transform(final Function<? super D, ? extends D1> p0);
    
    public abstract Either<E, D> and(final Either<E, D> p0);
    
    public abstract boolean isValid();
    
    public abstract D getData();
    
    public abstract E getError();
    
    private static final class EitherValid<E, D> extends Either<E, D>
    {
        private final D m_data;
        
        @Override
        public boolean isValid() {
            return true;
        }
        
        @Override
        public D getData() {
            return this.m_data;
        }
        
        @Override
        public E getError() {
            throw new IllegalStateException("error is absent.");
        }
        
        @Override
        public Either<E, D> and(final Either<E, D> right) {
            return right;
        }
        
        @Override
        public <E1> Either<E1, D> transformError(final Function<? super E, ? extends E1> function) {
            Preconditions.checkNotNull((Object)function);
            return (Either<E1, D>)this;
        }
        
        @Override
        public <D1> Either<E, D1> transform(final Function<? super D, ? extends D1> function) {
            Preconditions.checkNotNull((Object)function);
            return new EitherValid<E, D1>((D1)function.apply((Object)this.m_data));
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.m_data == null) ? 0 : this.m_data.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final EitherValid<?, ?> other = (EitherValid<?, ?>)obj;
            if (this.m_data == null) {
                if (other.m_data != null) {
                    return false;
                }
            }
            else if (!this.m_data.equals(other.m_data)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return String.format("Either Valid [%s]", this.m_data);
        }
        
        EitherValid(final D data) {
            super();
            this.m_data = data;
        }
    }
    
    private static class EitherError<E, D> extends Either<E, D>
    {
        private final E m_error;
        
        @Override
        public boolean isValid() {
            return false;
        }
        
        @Override
        public D getData() {
            throw new IllegalStateException("data is absent.");
        }
        
        @Override
        public E getError() {
            return this.m_error;
        }
        
        @Override
        public Either<E, D> and(final Either<E, D> right) {
            return this;
        }
        
        @Override
        public <E1> Either<E1, D> transformError(final Function<? super E, ? extends E1> function) {
            Preconditions.checkNotNull((Object)function);
            return new EitherError<E1, D>((E1)function.apply((Object)this.m_error));
        }
        
        @Override
        public <D1> Either<E, D1> transform(final Function<? super D, ? extends D1> function) {
            Preconditions.checkNotNull((Object)function);
            return (Either<E, D1>)this;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.m_error == null) ? 0 : this.m_error.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final EitherError<?, ?> other = (EitherError<?, ?>)obj;
            if (this.m_error == null) {
                if (other.m_error != null) {
                    return false;
                }
            }
            else if (!this.m_error.equals(other.m_error)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return String.format("Either Error [%s]", this.m_error);
        }
        
        EitherError(final E error) {
            super();
            this.m_error = (E)Preconditions.checkNotNull((Object)error);
        }
    }
}
