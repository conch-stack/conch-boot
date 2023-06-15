package com.nabob.conch.tools.utils;

/**
 * This interface represents an entity that has an identity that is represented by an id.
 */
public interface Identifiable<T> {
    /**
     * Return the (T) identity of this entity.
     */
    T getId();
}
