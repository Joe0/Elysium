package org.moparscape.elysium.entity.component;

/**
 * Keeps track of the state of an entity.
 * 
 * @author Joe Pritzel
 * 
 */
public class State extends AbstractComponent {

	/**
	 * The possible states an entity can be in.
	 * 
	 * @author Joe Pritzel
	 * 
	 */
	public enum STATES {
		LOGGED_IN, BUSY, IN_COMBAT;
	}

	/**
	 * The current state the entity is in.
	 */
	private STATES state;

	/**
	 * @return The state of the entity.
	 */
	public STATES getState() {
		return state;
	}

	/**
	 * @param state
	 *            - The state the entity should be placed in.
	 * @return The previous state.
	 */
	public STATES setState(STATES state) {
		STATES s = state;
		this.state = state;
		return s;
	}
}
