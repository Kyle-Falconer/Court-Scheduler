package courtscheduler.domain;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 9/21/13
 * Time: 11:01 PM
 */
public class AbstractPersistable implements Serializable, Comparable<AbstractPersistable> {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

// This part is currently commented out because it's probably a bad thing to mix identification with equality

//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (id == null || !(o instanceof AbstractPersistable)) {
//            return false;
//        } else {
//            AbstractPersistable other = (AbstractPersistable) o;
//            return id.equals(other.id);
//        }
//    }
//
//    public int hashCode() {
//        if (id == null) {
//            return super.hashCode();
//        } else {
//            return id.hashCode();
//        }
//    }

    /**
     * Used by the GUI to sort the {@link org.optaplanner.core.api.score.constraint.ConstraintMatch} list
     * by {@link org.optaplanner.core.api.score.constraint.ConstraintMatch#getJustificationList()}.
     * @param other never null
     * @return comparison
     */
    public int compareTo(AbstractPersistable other) {
        return new CompareToBuilder()
                .append(getClass().getName(), other.getClass().getName())
                .append(id, other.id)
                .toComparison();
    }

    public String toString() {
        return "[" + getClass().getName().replaceAll(".*\\.", "") + "-" + id + "]";
    }
}
