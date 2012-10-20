package websiteschema.mpsegment.concept;

import java.util.ArrayList;
import java.util.List;

//TODO: use concept instead of domain type.
public class Concept {

    private long id;
    private String name;
    private List<Concept> children;
    private Concept parent;

    public Concept(long id, String name) {
        this.id = id;
        this.name = name;
        children = new ArrayList<Concept>();
    }

    public List<Concept> getChildren() {
        return children;
    }

    public Concept getParent() {
        return parent;
    }

    public List<Concept> getSiblings() {
        return getParent().getChildren();
    }

    public void addChild(Concept child) {
        child.setParent(this);
        children.add(child);
    }

    private void setParent(Concept parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
