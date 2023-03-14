package GraphStreamSketch;

import java.util.*;

class GSnode {
    public int h;
    public GSnode next;
}

class degree_node implements Comparable {
    public int h;
    public int degree;

    @Override
    public int compareTo(Object o) {

        degree_node dn = (degree_node) o;
        if (this.degree < dn.degree) {
            return 1;
        } else if (this.degree > dn.degree) {
            return -1;
        }
        return 0;
    }


}

/**
 * 将原始图初步压缩得到的摘要
 */
public class GSketch {
    private HashMap<Integer, Integer> degree = new HashMap<>();
    private HashMap<Integer, Integer> index = new HashMap<>();
    private HashMap<Integer, Integer> idnums = new HashMap<>();
    private ArrayList<degree_node> sorted_nodes = new ArrayList<>();
    private ArrayList<GSnode> sketch;

    private int nodenum;
    private int triangle_num;
    private int edge_num;


    GSketch() {
        nodenum = 0;
        triangle_num = 0;
        edge_num = 0;
    }


    /**
     * 向初步压缩得到的图摘要添加节点
     *
     * @param h1    ID值（哈希值）
     * @param IDnum 映射到该ID的原始图的节点数目
     */
    public void insert_node(int h1, int IDnum) {
        index.put(h1, nodenum);
        degree.put(h1, 0);
        idnums.put(h1, IDnum);
        GSnode np = new GSnode();
        np.h = h1;
        np.next = null;
        sketch.add(np);
        nodenum++;
    }

    /**
     * 向初步压缩得到的图摘要添加边
     *
     * @param h1 出边的ID值（哈希值）
     * @param h2 入边的ID值（哈希值）
     */
    public void insert_edge(int h1, int h2) {
        int oldValue = degree.get(h1);
        degree.replace(h1, oldValue + 1);
        oldValue = degree.get(h2);
        degree.replace(h2, oldValue + 1);
        GSnode np = sketch.get(index.get(h1));

        for (; np != null; np = np.next) {
            if (np.h == h2) {
                break;
            }
            if (np.next == null) {
                GSnode newn = new GSnode();
                newn.h = h2;
                newn.next = null;
                np.next = newn;
                edge_num++;
                break;
            }
        }

        np = sketch.get(index.get(h2));

        for (; np != null; np = np.next) {
            if (np.h == h1) {
                break;
            }
            if (np.next == null) {
                GSnode newn = new GSnode();
                newn.h = h1;
                newn.next = null;
                np.next = newn;
                edge_num++;
                break;
            }
        }
    }

    /**
     * 清理
     */
    public void clean() {

        HashMap<Integer, Integer> sequence = new HashMap<>();

        for (Map.Entry<Integer, Integer> current : degree.entrySet()) {
            degree_node dn = new degree_node();
            dn.h = current.getKey();
            dn.degree = current.getValue();
            //   sorted_nodes.push_back(dn);
            sorted_nodes.add(dn);
        }

        Collections.sort(sorted_nodes);

        for (int i = 0; i < nodenum; i++) {
            sequence.put(sorted_nodes.get(i).h, i);
        }
        for (int i = 0; i < nodenum; i++) {
            GSnode np = sketch.get(i);
            int seq_d = sequence.get(np.h);
            GSnode parent = np;
            np = np.next;
            while (np != null) {
                if (sequence.get(np.h) <= seq_d) {
                    parent.next = np.next;
                    np = parent.next;
                    edge_num--;
                } else {
                    parent = np;
                    np = np.next;
                }
            }
        }
    }

    /**
     * 得到特定节点的邻居
     * @param h 哈希值
     * @param hv 邻居节点的哈希值arraylist
     */
    public void get_neighbors(int h, ArrayList<HashValue> hv) {
        Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
        boolean found = false;
        Map.Entry<Integer, Integer> entryNow = null;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey().equals(h)) {
                found = true;
                entryNow = entry;
                break;
            }
        }

        if (!found) {
            return;
        }

        GSnode np = sketch.get(entryNow.getValue());
        np = np.next;

        for (; np != null; np = np.next) {
            if (np.h == h) {
                System.out.println("loop!");
            }
            HashValue new_hv = new HashValue();
            new_hv.key = np.h;
            new_hv.IDnum = idnums.get(np.h);
            hv.add(new_hv);
        }
    }

    public int countTriangle()
    {
        int count = 0;
        for (int i = 0;i < nodenum;i++)
        {

            int h1 = sorted_nodes.get(i).h;
            int num1 = idnums.get(h1);
            ArrayList<HashValue> set1 = new ArrayList<HashValue>();
            get_neighbors(h1, set1);
            Collections.sort(set1);

            for (int j = 0;j < set1.size();j++)
            {

                int h2 = set1.get(j).key;
                int num2 = set1.get(j).IDnum;
                ArrayList<HashValue> set2 = new ArrayList<HashValue>();
                get_neighbors(h2,set2);
                Collections.sort(set2);
                int num3 = HashTable.countjoin(set1,set2);

                count += num1 * num2 * num3;
            }
        }
        return count;
    }



}
