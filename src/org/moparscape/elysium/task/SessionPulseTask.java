package org.moparscape.elysium.task;

import org.moparscape.elysium.net.Session;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class SessionPulseTask implements Runnable {

    private final Iterable<Session> sessions;

    private final CountDownLatch latch;

    public SessionPulseTask(Iterable<Session> sessions, CountDownLatch latch) {
        this.sessions = sessions;
        this.latch = latch;
    }

    public void run() {
        try {
            for (Session s : sessions) {
                s.pulse();
            }
        } finally {
            latch.countDown();
        }
    }
}
