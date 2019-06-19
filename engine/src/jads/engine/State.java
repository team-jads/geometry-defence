package jads.engine;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * State
 */
public class State {

    private interface Remover {
        public void remove();
    }

    private class Removable {
        protected void setRemover(Remover remover) {
            this.remover = remover;
        }

        protected void removeFromContainer() {
            remover.remove();
            remover = null;
        }

        private Remover remover;
    }

    private class Entity extends Removable {
        protected Entity(State state) {
            id = state.getNextId();
        }

        public final int id;

        private void addAttribute(Attribute attribute) {
            attributes.add(attribute);
            attribute.setRemover(()->attributes.remove(attribute));
        }

        private HashSet<Attribute> attributes;

        private void addEffectInstance(EffectInstance effectInstance) {
            effectInstances.add(effectInstance);
            effectInstance.setRemover(()->effectInstances.remove(effectInstance));
        }

        private HashSet<EffectInstance> effectInstances;
    }

    private class Attribute extends Removable {
        protected Attribute(State state, Entity entity, int type) {
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

    private class EffectInstance extends Removable {
        protected EffectInstance(State state, Entity entity, Effect effect) {
            id = state.getNextId();
            this.entity = entity;
            this.effect = effect;
        }

        public final int id;
        public final Entity entity;
        public final Effect effect;

        private int multuplicity;
    }

    abstract public class Effect {
        public Effect(int priority, int attributeType) {
            this.priority = priority;
            this.attributeType = attributeType;
        }

        public final int priority;
        public final int attributeType;

        public abstract int apply(int multiplicity, int x);
    }

    abstract public class Event {
        public Event(int t) {
            this.t = t;
        }

        public final int t;

        protected List<Event> apply(State state) {
            return Collections.emptyList();
        }
    }

    public final class EntityCreated extends Event {
        public EntityCreated(int t, Entity entity) {
            super(t);
            this.entity = entity;
        }

        public final Entity entity;

        protected List<Event> apply(State state) {
            state.addEntity(entity);
            return super.apply(state);
        }
    }

    public final class EntityDestroyed extends Event {
        public EntityDestroyed(int t, Entity entity) {
            super(t);
            this.entity = entity;
        }

        public final Entity entity;

        protected List<Event> apply(State state) {
            entity.removeFromContainer();
            return super.apply(state);
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

        protected List<Event> apply(State state) {
            attribute.entity.addAttribute(attribute);
            return super.apply(state);
        }
    }

    public final class AttributeRemoved extends Event {
        public AttributeRemoved(int t, Attribute attribute) {
            super(t);
            this.attribute = attribute;
        }

        public final Attribute attribute;

        protected List<Event> apply(State state) {
            attribute.removeFromContainer();
            return super.apply(state);
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

        protected List<Event> apply(State state) {
            oldX = attribute.x;
            oldDx = attribute.dx;
            attribute.x = x;
            attribute.dx = dx;
            return super.apply(state);
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

        protected List<Event> apply(State state) {
            effectInstance.entity.addEffectInstance(effectInstance);
            return super.apply(state);
        }
    }

    public final class EffectRemoved extends Event {
        public EffectRemoved(int t, EffectInstance effectInstance) {
            super(t);
            this.effectInstance = effectInstance;
        }

        public final EffectInstance effectInstance;

        protected List<Event> apply(State state) {
            effectInstance.removeFromContainer();
            return super.apply(state);
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

        protected List<Event> apply(State state) {
            oldMultiplicity = effectInstance.multuplicity;
            effectInstance.multuplicity = multiplicity;
            return super.apply(state);
        }
    }

    private LinkedList<Entity> entities;

    private Vector eventLog;
    private ArrayDeque eventQueue;

    private int nextId = 0;
    private int getNextId() {
        return nextId++;
    }

    private void addEntity(Entity entity) {
        entities.add(entity);
        Iterator<Entity> it = entities.descendingIterator();
        entity.setRemover(()->it.remove());
    }

}