package org.moparscape.elysium.entity;

import org.moparscape.elysium.entity.component.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author lothy
 */
public class Entity {

    private final Map<Class<? extends Component>, Component> components;

    private int index;

    private Entity() {
        throw new RuntimeException("Default constructor not supported.");
    }

    public Entity(Map<Class<? extends Component>, Component> components) {
        this.components = Collections.unmodifiableMap(components);
    }

    public <T extends Component> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Index: " + String.valueOf(index);
    }
}
