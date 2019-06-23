package jads.engine;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * State
 */
public class State {

    private interface Remover {
        public void remove();
    }

    private class Removable {
        void setRemover(Remover remover) {
            this.remover = remover;
        }

        void removeFromContainer() {
            remover.remove();
            remover = null;
        }

        public boolean removed() {
            return remover == null;
        }

        private Remover remover;
    }

    public final class Entity extends Removable {
        public Entity(State state) {
            id = state.getNextId();
        }

        public final int id;

        private void addAttribute(Attribute attribute) {
            attributes.add(attribute);
            attribute.setRemover(() -> attributes.remove(attribute));
        }

        private HashSet<Attribute> attributes;
        public Set<Attribute> currentAttributes = Collections.unmodifiableSet(attributes);

        private void addEffectInstance(EffectInstance effectInstance) {
            effectInstances.add(effectInstance);
            effectInstance.setRemover(() -> effectInstances.remove(effectInstance));
        }

        private HashSet<EffectInstance> effectInstances;
        public Set<EffectInstance> currentEffectInstances = Collections.unmodifiableSet(effectInstances);
    }

    public final class Attribute extends Removable {
        public Attribute(State state, Entity entity, int type) {
            id = state.getNextId();
            this.entity = entity;
            this.type = type;
        }

        public final int id;
        public final Entity entity;
        public final int type;

        private int x;
        private int dx;
    }

    public final class EffectInstance extends Removable {
        public EffectInstance(State state, Entity entity, Effect effect) {
            id = state.getNextId();
            this.entity = entity;
            this.effect = effect;
        }

        public final int id;
        public final Entity entity;
        public final Effect effect;

        private int multuplicity;
    }

    public abstract class Effect {
        public Effect(int priority, int attributeType) {
            this.priority = priority;
            this.attributeType = attributeType;
        }

        public final int priority;
        public final int attributeType;

        protected abstract int apply(int multiplicity, int x);
    }

    private abstract class Event extends Removable implements Comparable<Event> {
        private Event(int t) {
            this.t = t;
        }

        public final int t;

        abstract void apply(State state);

        abstract void unapply(State state);

        private int priority;

        @Override
        public int compareTo(Event other) {
            return t != other.t ? t - other.t : priority - other.priority;
        }
    }

    public abstract class HigherOrderEvent extends Event {
        public HigherOrderEvent(int t) {
            super(t);
        }

        protected abstract List<Event> reduce(State state);

        private List<Event> createdEvents;

        void apply(State state) {
            createdEvents = reduce(state);
            state.queueEvents(createdEvents);
        }

        void unapply(State state) {
            // clears all references to the created events, leaving them up for garbage collection
            createdEvents.forEach(event -> event.removeFromContainer());
            createdEvents = null;
        }
    }

    public final class EntityAdded extends Event {
        public EntityAdded(int t, Entity entity) {
            super(t);
            this.entity = entity;
        }

        public final Entity entity;

        void apply(State state) {
            state.addEntity(entity);
        }

        void unapply(State state) {
            entity.removeFromContainer();
        }
    }

    public final class EntityRemoved extends Event {
        public EntityRemoved(int t, Entity entity) {
            super(t);
            this.entity = entity;
        }

        public final Entity entity;

        void apply(State state) {
            entity.removeFromContainer();
        }

        void unapply(State state) {
            state.addEntity(entity);
        }
    }

    public final class AttributeAdded extends Event {
        public AttributeAdded(int t, Attribute attribute, int type, int x, int dx) {
            super(t);
            this.attribute = attribute;
            this.type = type;
            this.x = x;
            this.dx = dx;
        }

        public final Attribute attribute;
        public final int type;
        public final int x;
        public final int dx;

        void apply(State state) {
            attribute.entity.addAttribute(attribute);
        }

        void unapply(State state) {
            attribute.removeFromContainer();
        }
    }

    public final class AttributeRemoved extends Event {
        public AttributeRemoved(int t, Attribute attribute) {
            super(t);
            this.attribute = attribute;
        }

        public final Attribute attribute;

        void apply(State state) {
            attribute.removeFromContainer();
        }

        void unapply(State state) {
            attribute.entity.addAttribute(attribute);
        }
    }

    public final class AttributeChanged extends Event {
        public AttributeChanged(int t, Attribute attribute, int type, int x, int dx) {
            super(t);
            this.attribute = attribute;
            this.type = type;
            this.x = x;
            this.dx = dx;
        }

        public final Attribute attribute;
        public final int type;
        public final int x;
        public final int dx;

        private int oldX;
        private int oldDx;

        void apply(State state) {
            oldX = attribute.x;
            oldDx = attribute.dx;
            attribute.x = x;
            attribute.dx = dx;
        }

        void unapply(State state) {
            attribute.x = oldX;
            attribute.dx = oldDx;
        }
    }

    public final class EffectAdded extends Event {
        public EffectAdded(int t, EffectInstance effectInstance, int multiplicity) {
            super(t);
            this.effectInstance = effectInstance;
            this.multiplicity = multiplicity;
        }

        public final EffectInstance effectInstance;
        public final int multiplicity;

        void apply(State state) {
            effectInstance.entity.addEffectInstance(effectInstance);
        }

        void unapply(State state) {
            effectInstance.removeFromContainer();
        }
    }

    public final class EffectRemoved extends Event {
        public EffectRemoved(int t, EffectInstance effectInstance) {
            super(t);
            this.effectInstance = effectInstance;
        }

        public final EffectInstance effectInstance;

        void apply(State state) {
            effectInstance.removeFromContainer();
        }

        void unapply(State state) {
            effectInstance.entity.addEffectInstance(effectInstance);
        }
    }

    public final class EffectChanged extends Event {
        public EffectChanged(int t, EffectInstance effectInstance, int multuplicity) {
            super(t);
            this.effectInstance = effectInstance;
            this.multiplicity = multuplicity;
        }

        public final EffectInstance effectInstance;
        public final int multiplicity;

        private int oldMultiplicity;

        void apply(State state) {
            oldMultiplicity = effectInstance.multuplicity;
            effectInstance.multuplicity = multiplicity;
        }

        void unapply(State state) {
            effectInstance.multuplicity = oldMultiplicity;
        }
    }

    private final LinkedList<Entity> entities = new LinkedList<>();

    private final ArrayDeque<Event> eventLog = new ArrayDeque<>();
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();

    private int nextId = 0;

    private int getNextId() {
        return nextId++;
    }

    private void addEntity(Entity entity) {
        entities.add(entity);
        Iterator<Entity> it = entities.descendingIterator();
        entity.setRemover(() -> it.remove());
    }

    private void queueEvents(List<Event> events) {
        events.forEach(event -> {
            event.priority = getNextId();
            event.setRemover(() -> eventQueue.remove(event));
        });
        eventQueue.addAll(events);
    }

    private boolean running = false;

    public void queueInitialEvents(List<Event> events) {
        assert !running;
        eventQueue.addAll(events);
    }

    private void backtrack(int t) {
        while (!eventLog.isEmpty()) {
            Event lastEvent = eventLog.getLast();
            if (lastEvent.t < t) {
                break;
            }
            eventLog.pollLast();
            lastEvent.unapply(this);
            eventQueue.add(lastEvent);
        }
    }

    public void run() throws InterruptedException {
        running = true;
        int t = 0;
        while (!eventQueue.isEmpty()) {
            Event nextEvent = eventQueue.peek();
            if (nextEvent.t < t) {
                backtrack(nextEvent.t);
                continue;
            }
            eventQueue.poll();
            if (nextEvent.t != t) {
                TimeUnit.MILLISECONDS.sleep(nextEvent.t - t);
            }
            nextEvent.apply(this);
            eventLog.addLast(nextEvent);
        }
    }

}