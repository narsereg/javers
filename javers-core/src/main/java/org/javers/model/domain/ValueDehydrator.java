package org.javers.model.domain;

import org.javers.core.json.JsonConverter;
import org.javers.model.domain.changeType.ValueAdded;
import org.javers.model.domain.changeType.ValueChange;
import org.javers.model.domain.changeType.ValueRemoved;
import org.javers.model.visitors.ChangeVisitor;

/**
 * @author bartosz walacik
 */
public class ValueDehydrator {
    private JsonConverter jsonConverter;

    public ValueDehydrator(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public void dehydrate(Diff diff) {
        ChangeVisitor visitor = new ValueDehydrateVisitor();
        diff.accept(visitor);
    }

    private void dehydrate(Value value) {
        String json = jsonConverter.toJson(value.getValue());
        value.dehydrate(json);
    }

    private class ValueDehydrateVisitor implements ChangeVisitor{

        @Override
        public void visit(Change object) {
            if (object instanceof ValueChange) {
                ValueChange value = (ValueChange)object;
                dehydrate(value.getLeftValue());
                dehydrate(value.getRightValue());
            }

            if (object instanceof ValueAdded) {
                ValueAdded value = (ValueAdded)object;
                dehydrate(value.getAddedValue());
            }

            if (object instanceof ValueRemoved) {
                ValueRemoved value = (ValueRemoved)object;
                dehydrate(value.getRemovedValue());
            }
        }
    }
}
