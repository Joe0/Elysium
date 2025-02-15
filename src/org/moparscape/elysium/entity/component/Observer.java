package org.moparscape.elysium.entity.component;

import org.moparscape.elysium.entity.*;
import org.moparscape.elysium.util.StatefulEntityCollection;
import org.moparscape.elysium.world.Point;
import org.moparscape.elysium.world.Region;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author lothy
 */
public final class Observer extends AbstractComponent {

    private final Map<Integer, Integer> knownPlayerAppearanceIds = new HashMap<Integer, Integer>();

    private final StatefulEntityCollection<GameObject> watchedObjects = new StatefulEntityCollection<GameObject>();

    private final StatefulEntityCollection<Item> watchedItems = new StatefulEntityCollection<Item>();

    private final StatefulEntityCollection<Npc> watchedNpcs = new StatefulEntityCollection<Npc>();

    private final StatefulEntityCollection<Player> watchedPlayers = new StatefulEntityCollection<Player>();

    private final Queue<Projectile> projectiles = new LinkedBlockingQueue<Projectile>();

    private final Queue<Player> playerHitUpdates = new LinkedBlockingQueue<Player>();

    private final Queue<Npc> npcHitUpdates = new LinkedBlockingQueue<Npc>();

    private final Queue<Bubble> bubbles = new LinkedBlockingQueue<Bubble>();

    private Player owner;

    private Appearance appearance;

    public void setOwner(Player player) {
        if (owner != null) {
            throw new IllegalStateException("Observer's player is already set");
        }
        this.owner = player;
    }

    @Override
    public void resolveDependencies(Map<Class<? extends Component>, Component> components) {
        this.appearance = Appearance.class.cast(components.get(Appearance.class));
    }

    public StatefulEntityCollection<GameObject> getWatchedObjects() {
        return watchedObjects;
    }

    public StatefulEntityCollection<Item> getWatchedItems() {
        return watchedItems;
    }

    public StatefulEntityCollection<Npc> getWatchedNpcs() {
        return watchedNpcs;
    }

    public StatefulEntityCollection<Player> getWatchedPlayers() {
        return watchedPlayers;
    }

    public Queue<Projectile> getProjectilesNeedingDisplayed() {
        return projectiles;
    }

    public Queue<Bubble> getBubblesNeedingDisplayed() {
        return bubbles;
    }

    public Queue<Npc> getNpcHitUpdates() {
        return npcHitUpdates;
    }

    public Queue<Player> getPlayerHitUpdates() {
        return playerHitUpdates;
    }

    private boolean needsAppearanceUpdateFor(Player target) {
        int targetIndex = target.getIndex();
        if (knownPlayerAppearanceIds.containsKey(targetIndex)) {
            Appearance targetAppearance = target.getComponent(Appearance.class);
            int knownAppearanceId = knownPlayerAppearanceIds.get(targetIndex);
            if (knownAppearanceId != targetAppearance.getAppearanceId()) {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    public List<Player> getPlayerAppearanceUpdates() {
        List<Player> needingUpdates = new LinkedList<Player>();
        needingUpdates.addAll(watchedPlayers.getNewEntities());
        if (appearance.appearanceChanged()) {
            needingUpdates.add(owner);
        }

        for (Player p : watchedPlayers.getKnownEntities()) {
            if (needsAppearanceUpdateFor(p)) {
                needingUpdates.add(p);
            }
        }

        return needingUpdates;
    }

    public void addPlayerAppearanceIds(int[] indices, int[] appearanceIds) {
        for (int i = 0; i < indices.length; i++) {
            knownPlayerAppearanceIds.put(indices[i], appearanceIds[i]);
        }
    }

    public void revalidateWatchedEntities() {
        revalidateWatchedPlayers();
        revalidateWatchedObjects();
        revalidateWatchedItems();
        revalidateWatchedNpcs();
    }

    public void updateWatchedEntities() {
        updateWatchedPlayers();
        updateWatchedObjects();
        updateWatchedItems();
        updateWatchedNpcs();
    }

    public void updateEntityLists() {
        watchedPlayers.update();
        watchedObjects.update();
        watchedItems.update();
        watchedNpcs.update();
    }

    public void clearDisplayLists() {
        projectiles.clear();
        playerHitUpdates.clear();
        npcHitUpdates.clear();
        bubbles.clear();
    }

    private void revalidateWatchedObjects() {
        Point loc = owner.getLocation();
        for (GameObject o : watchedObjects.getKnownEntities()) {
            if (!loc.withinRange(o.getLocation(), 21) || o.isRemoved()) {
                watchedObjects.remove(o);
            }
        }
    }

    private void revalidateWatchedItems() {
        Point loc = owner.getLocation();
        for (Item i : watchedItems.getKnownEntities()) {
            if (!loc.withinRange(i.getLocation(), 16) || i.isRemoved() || !i.isVisibleTo(owner)) {
                watchedItems.remove(i);
            }
        }
    }

    private void revalidateWatchedNpcs() {
        Point loc = owner.getLocation();
        for (Npc n : watchedNpcs.getKnownEntities()) {
            if (!loc.withinRange(n.getLocation(), 16) || n.isRemoved()) {
                watchedNpcs.remove(n);
            }
        }
    }

    private void revalidateWatchedPlayers() {
        Point loc = owner.getLocation();
        for (Player p : watchedPlayers.getKnownEntities()) {
            if (!loc.withinRange(p.getLocation(), 16) || !p.isLoggedIn()) {
                watchedPlayers.remove(p);
                knownPlayerAppearanceIds.remove(p.getIndex());
            }
        }
    }

    private void updateWatchedObjects() {
        Iterable<GameObject> objects = Region.getViewableObjects(owner.getLocation(), 21);

        for (GameObject go : objects) {
            if (!watchedObjects.contains(go)) {
                watchedObjects.add(go);
            }
        }
    }

    private void updateWatchedItems() {
        Iterable<Item> items = Region.getViewableItems(owner.getLocation(), 16);

        for (Item item : items) {
            if (!watchedItems.contains(item) && item.isVisibleTo(owner)) {
                watchedItems.add(item);
            }
        }
    }

    private void updateWatchedNpcs() {
        Iterable<Npc> npcs = Region.getViewableNpcs(owner.getLocation(), 16);

        for (Npc npc : npcs) {
            if (!watchedNpcs.contains(npc) || watchedNpcs.isRemoving(npc)) {
                watchedNpcs.add(npc);
            }
        }
    }

    private void updateWatchedPlayers() {
        Iterable<Player> players = Region.getViewablePlayers(owner, 16);

        for (Player player : players) {
            if (!watchedPlayers.contains(player) || watchedPlayers.isRemoving(player)) {
                watchedPlayers.add(player);
            }
        }
    }
}
