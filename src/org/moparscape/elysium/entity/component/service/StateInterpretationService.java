package org.moparscape.elysium.entity.component.service;

import org.moparscape.elysium.entity.component.State;

/**
 * Used to interpret the state of entities.
 * 
 * @author Joe Pritzel
 * 
 */
public class StateInterpretationService {

	/**
	 * Determines if an entity is allowed to log out based on its state.
	 * 
	 * @param s
	 *            - The state to check.
	 * @return Returns true if the entity can log out.
	 */
	public static boolean canLogout(State s) {
		switch (s.getState()) {
		case BUSY:
		case IN_COMBAT:
			return false;
		default:
			return true;
		}
	}

}
