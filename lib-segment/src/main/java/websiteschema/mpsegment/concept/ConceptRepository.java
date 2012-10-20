package websiteschema.mpsegment.concept;

public class ConceptRepository {
    private ConceptTree nounConceptTree;
    private ConceptTree verbConceptTree;
    private ConceptTree adjConceptTree;

    public ConceptRepository() {
        nounConceptTree = new ConceptLoader("websiteschema/mpsegment/noun-concepts.txt").getConceptTree();
        verbConceptTree = new ConceptLoader("websiteschema/mpsegment/verb-concepts.txt").getConceptTree();
        adjConceptTree = new ConceptLoader("websiteschema/mpsegment/adj-concepts.txt").getConceptTree();
    }

    public ConceptTree getNounConceptTree() {
        return nounConceptTree;
    }

    public ConceptTree getAdjConceptTree() {
        return adjConceptTree;
    }

    public ConceptTree getVerbConceptTree() {
        return verbConceptTree;
    }
}
