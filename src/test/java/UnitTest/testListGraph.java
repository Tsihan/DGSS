package UnitTest;

import GraphStreamSketch.ListGraph;

public class testListGraph {
    public static void main(String[] args) {
        ListGraph myListGraph = new ListGraph();

//        myListGraph.insert("70","3",1,8);
//        myListGraph.insert("3","32",1,1);
        myListGraph.insert("26","444",1,1);
        myListGraph.insert("26","444",1,1);
        myListGraph.insert("27","444",1,1);
//       System.out.println(myListGraph.query("70","3",1));
//        System.out.println(myListGraph.transquery("70","32",1));
        System.out.println(myListGraph.nodequery("444", 1, 0));
        System.out.println(myListGraph.nodequery("444", 1, 1));
        System.out.println(myListGraph.nodeValueQuery("444", 1, 0));


    }
}
