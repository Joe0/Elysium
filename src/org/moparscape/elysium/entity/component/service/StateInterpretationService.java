package org.moparscape.elysium.entity.component.service;

import org.moparscape.elysium.entity.component.State;

public class StateInterpretationService {

	public static boolean canLogout(State component) {
		switch (component.getState()) {
		case BUSY:
		case IN_COMBAT:
			return false;
		default:
			return true;
		}
	}

}
