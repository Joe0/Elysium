package org.moparscape.elysium.entity;

import org.moparscape.elysium.entity.component.Component;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class Player extends Entity {

    private boolean loggedIn;

    public Player(Map<Class<? extends Component>, Component> components) {
        super(components);
    }

    public boolean isLoggedIn() {
        return false;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }
}
