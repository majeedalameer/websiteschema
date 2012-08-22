/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.hmm.*;
/**
 *
 * @author ray
 */
public class ViterbiTest {

    @Test
    public void should_return_status_333332_with_giving_observes_THTHTH() {
        List<String> o = new ArrayList<String>();
        Viterbi<String, String> viterbi = new Viterbi<String, String>();
        TreeNodeSortor sortor = new TreeNodeBinarySort();
        
        viterbi.setSortor(sortor);

        initTestData(viterbi);
        viterbi.setN(2);

        //o = [ T H T H T H ]
        o.add("T");
        o.add("H");
        o.add("T");
        o.add("H");
        o.add("T");
        o.add("H");
        try {
            List<Node<String>> s;
            s = viterbi.caculateWithLog(o);
            StringBuilder sb = new StringBuilder();
            for (Node state : s) {
                System.out.print(state.getName() + " ");
                sb.append(state.getName()).append(" ");
            }
            assert (sb.toString().trim().equals("three three three three three two"));
        } catch (ObserveListException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void should_handle_unknown_State() {
        List<String> o = new ArrayList<String>();
        Viterbi<String, String> viterbi = new Viterbi<String, String>();
        TreeNodeSortor sortor = new TreeNodeBinarySort();
        
        viterbi.setSortor(sortor);

        initTestData(viterbi);
        viterbi.setN(2);

        //o = [ T H T H T H ]
        o.add("A");
        o.add("H");

        try {
            List<Node<String>> s;
            s = viterbi.caculateWithLog(o);
            StringBuilder sb = new StringBuilder();
            for (Node state : s) {
                System.out.print(state.getName() + " ");
                sb.append(state.getName()).append(" ");
            }
            Assert.fail("should throw ObserveListException.");
        } catch (ObserveListException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void initTestData(Viterbi<String, String> v) {
        State<String> s1 = new State("one");
        v.getStateBank().add(s1);
        State<String> s2 = new State("two");
        v.getStateBank().add(s2);
        State<String> s3 = new State("three");
        v.getStateBank().add(s3);

        Observe<String> o1 = new Observe("H");
        v.getObserveBank().add(o1);
        Observe<String> o2 = new Observe("T");
        v.getObserveBank().add(o2);

        //transition
        //0.8 0.1 0.1
        //0.1 0.8 0.1
        //0.1 0.1 0.8
        v.getTran().setStateBank(v.getStateBank());
        v.getTran().setProb(s1.getIndex(), s1.getIndex(), 0.3);
        v.getTran().setProb(s1.getIndex(), s2.getIndex(), 0.3);
        v.getTran().setProb(s1.getIndex(), s3.getIndex(), 0.4);
        v.getTran().setProb(s2.getIndex(), s1.getIndex(), 0.2);
        v.getTran().setProb(s2.getIndex(), s2.getIndex(), 0.6);
        v.getTran().setProb(s2.getIndex(), s3.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s1.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s2.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s3.getIndex(), 0.6);
        v.getTran().getRoot().printTreeNode("");
        //emission
        //0.5 0.5
        //0.8 0.2
        //0.2 0.8
        v.getE().setProb(o1.getIndex(), s1.getIndex(), 0.5);
        v.getE().setProb(o2.getIndex(), s1.getIndex(), 0.5);
        v.getE().setProb(o1.getIndex(), s2.getIndex(), 0.8);
        v.getE().setProb(o2.getIndex(), s2.getIndex(), 0.2);
        v.getE().setProb(o1.getIndex(), s3.getIndex(), 0.2);
        v.getE().setProb(o2.getIndex(), s3.getIndex(), 0.8);

        //Pi = [0.2 0.3 0.5]
        v.getPi().setPi(s1.getIndex(), 0.2);
        v.getPi().setPi(s2.getIndex(), 0.4);
        v.getPi().setPi(s3.getIndex(), 0.4);
    }
}
