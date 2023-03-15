package GraphStreamSketch;

import java.util.*;

public class ListGraph {
    private int n;
    private int max_d;
    private int N;

    public ArrayList<String> node = new ArrayList<>();
    public ArrayList<ListEdge> g = new ArrayList<>();
    public HashMap<String, Integer> index = new HashMap<>();
    public HashMap<String, Integer> degree = new HashMap<>();
    public HashMap<String, Integer> weight = new HashMap<>();

    public ListGraph() {
        n = 0;
        max_d = 0;
        N = 0;
    }

    /**
     * 插入一条边
     *
     * @param s1 出边节点的ID
     * @param s2 入边节点的ID
     * @param l  边的标签
     * @param w  边的权重
     */
    public void insert(String s1, String s2, int l, int w) {
        int n1 = 0;
        int n2 = 0;
        boolean found = false;

        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            if (entry.getKey().equals(s1)) {
                n1 = entry.getValue();
                found = true;
                break;
            }
        }

        if (!found) {
            node.add(s1);
            index.put(s1, n);
            n1 = n;
            n++;
            ListEdge e = new ListEdge();
            e.next = null;
            e.n = n1;
            e.weight = 0;
            e.label = 0;
            g.add(e);
        }

        Iterator<Map.Entry<String, Integer>> it2 = index.entrySet().iterator();
        //重置found
        found = false;

        while (it2.hasNext()) {
            Map.Entry<String, Integer> entry = it2.next();
            if (entry.getKey().equals(s2)) {
                n2 = entry.getValue();
                found = true;
                break;
            }
        }

        if (!found) {
            node.add(s2);
            index.put(s2, n);
            n2 = n;
            n++;
            ListEdge e = new ListEdge();
            e.next = null;
            e.n = n2;
            e.weight = 0;
            e.label = 0;
            g.add(e);
        }

        //重置found
        found = false;
        ListEdge e = g.get(n1);
        int length = 0;
        boolean find = false;

        while (true) {
            length++;
            if (e.n == n2 && e.label == l) {
                e.weight += w;
                find = true;
                break;
            }
            if (e.next == null) {
                break;
            }
            e = e.next;
        }

        if (!find) {
            for (Map.Entry<String, Integer> entry : degree.entrySet()) {
                if (entry.getKey().equals(s1)) {
                    int oldValue = entry.getValue();
                    entry.setValue(oldValue + 1);
                    found = true;
                    break;
                }
            }

            if (!found) {
                degree.put(s1, 1);
            }

            N++;
            ListEdge f = new ListEdge();
            f.label = l;
            f.n = n2;
            f.weight = w;
            f.next = null;
            e.next = f;
            length++;

            if (length > max_d) {
                max_d = length;
            }
        }
    }

    /**
     * 返回特定一条边的权重之和（不同时间戳会叠加权重）
     *
     * @param s1    出边节点的ID
     * @param s2    出边节点的ID
     * @param label 边的标签
     * @return 边的权重
     */
    public int query(String s1, String s2, int label) {
        int n1 = 0;
        int n2 = 0;
        boolean found = false;

        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            if (entry.getKey().equals(s1)) {
                n1 = entry.getValue();
                found = true;
                break;
            }
        }

        if (!found) {
            return 0;
        }

        //重置found
        found = false;

        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            if (entry.getKey().equals(s2)) {
                n2 = entry.getValue();
                found = true;
                break;
            }
        }

        if (!found) {
            return 0;
        }

        ListEdge e = g.get(n1);

        while (e != null) {
            if (e.n == n2 && e.label == label) {
                return e.weight;
            }
            e = e.next;
        }
        return 0;
    }

    /**
     * 判断有向图两点间是否可达
     *
     * @param s1    出边节点的ID
     * @param s2    入边节点的ID
     * @param label 边的标签
     * @return 有向图两点间是否可达
     */
    public boolean transquery(String s1, String s2, int label) {

        int n1 = index.get(s1);
        int n2 = index.get(s2);
        ListEdge e;
        LinkedList<Integer> q = new LinkedList<>();
        q.offer(n1);
        int s = n;
        boolean[] checked = new boolean[s];

        for (int i = 0; i < s; i++) {
            checked[i] = false;
        }

        checked[n1] = true;

        while (!q.isEmpty()) {
            int n = q.peek();
            e = g.get(n);
            while (e != null) {

                if (!checked[e.n] && e.label == label) {
                    q.offer(e.n);
                    checked[e.n] = true;
                }

                if (e.n == n2) {
                    return true;
                }
                e = e.next;
            }
            q.poll();
        }
        return false;
    }

    /**
     * 返回对于特定节点某一标签出方向/入方向的度数
     *
     * @param s1    某一节点的ID
     * @param label 边的标签
     * @param type  type = 0 is successor query ,type =1 is precursor query
     * @return 对于特定节点某一标签出方向/入方向的度数（不同时间戳不累加）
     */
    public int nodequery(String s1, int label, int type) {
        int degree = 0;
        boolean found = false;

        if (type == 0) {
            int n1 = 0;
            for (Map.Entry<String, Integer> entry : index.entrySet()) {
                if (entry.getKey().equals(s1)) {
                    n1 = entry.getValue();
                    found = true;
                    break;
                }
            }

            if (!found) {
                return 0;
            }

            ListEdge e = g.get(n1);
            e = e.next;

            while (e != null) {
                if (e.label == label) {
                    ++degree;
                }
                e = e.next;
            }
        } else if (type == 1) {
            int n2 = 0;
            for (Map.Entry<String, Integer> entry : index.entrySet()) {
                if (entry.getKey().equals(s1)) {
                    n2 = entry.getValue();
                    found = true;
                    break;
                }
            }

            if (!found) {
                return 0;
            }

            for (ListEdge e : g) {
                e = e.next;
                while (e != null) {
                    if (e.n == n2) {
                        ++degree;
                    }
                    e = e.next;
                }
            }
        }
        return degree;
    }

    /**
     * 返回对于特定节点某一标签出方向/入方向权重之和
     *
     * @param s1    某一节点的ID
     * @param label 边的标签
     * @param type  0为出方向，1为入方向
     * @return 对于特定节点某一标签出方向/入方向权重之和(不同时间戳会累加)
     */
    public int nodeValueQuery(String s1, int label, int type) {
        int weight = 0;
        boolean found = false;
        if (type == 0) {
            int n1 = 0;

            for (Map.Entry<String, Integer> entry : index.entrySet()) {
                if (entry.getKey().equals(s1)) {
                    n1 = entry.getValue();
                    found = true;
                    break;
                }
            }

            if (!found) {
                return 0;
            }

            ListEdge e = g.get(n1);

            e = e.next;
            while (e != null) {
                if (e.label == label) {
                    weight += e.weight;
                }
                e = e.next;
            }
        } else if (type == 1) {
            int n2 = 0;

            for (Map.Entry<String, Integer> entry : index.entrySet()) {
                if (entry.getKey().equals(s1)) {
                    n2 = entry.getValue();
                    found = true;
                    break;
                }
            }

            if (!found) {
                return 0;
            }

            for (ListEdge e : g) {
                e = e.next;
                while (e != null) {
                    if (e.n == n2) {
                        weight += e.weight;
                    }
                    e = e.next;
                }
            }
        }
        return weight;
    }

    /**
     * @param successor 后继
     * @param presussor 前驱
     * @param label     边的标签
     */
    public void allNodeQuery(HashMap<String, Integer> successor, HashMap<String, Integer> presussor, int label) {
        HashMap<Integer, Integer> idx2successor = new HashMap<>();
        HashMap<Integer, Integer> idx2precussor = new HashMap<>();
        boolean found = false;


        for (ListEdge e : g) {

            int head = e.n;
            Iterator<Map.Entry<Integer, Integer>> it1 = idx2successor.entrySet().iterator();

            while (it1.hasNext()) {
                Map.Entry<Integer, Integer> entry = it1.next();
                if (entry.getKey().equals(head)) {
                    break;
                }
            }

            if (!it1.hasNext()) {
                idx2successor.put(head, 0);

            }
            e = e.next;
            while (e != null) {

                int oldValue1 = idx2successor.get(head);
                idx2successor.replace(head, oldValue1 + 1);
                Iterator<Map.Entry<Integer, Integer>> it2 = idx2precussor.entrySet().iterator();

                while (it2.hasNext()) {
                    Map.Entry<Integer, Integer> entry = it2.next();
                    if (entry.getKey().equals(e.n)) {
                        break;
                    }
                }

                if (!it2.hasNext()) {
                    idx2precussor.put(e.n, 0);
                }

                int oldValue2 = idx2precussor.get(head);
                idx2successor.replace(head, oldValue2 + 1);
                e = e.next;
            }
        }

        Iterator<Map.Entry<String, Integer>> str2idx = index.entrySet().iterator();

        while (str2idx.hasNext()) {
            //这里存疑successor[str2idx->first] = idx2successor[str2idx->second];
            successor.put(str2idx.next().getKey(), idx2successor.get(str2idx.next().getValue()));
            //存疑
            presussor.put(str2idx.next().getKey(), idx2precussor.get(str2idx.next().getValue()));
        }
    }
}
