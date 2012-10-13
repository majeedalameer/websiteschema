package websiteschema.mpsegment.concept;

import java.util.List;

//TODO: use concept instead of domain type.
public class Concept {

    private int id;
    private int name;

    public List<Concept> getChildren() {
        throw new UnsupportedOperationException();
    }

    public Concept getParent() {
        throw new UnsupportedOperationException();
    }

    public List<Concept> getSiblings() {
        throw new UnsupportedOperationException();
    }
}
