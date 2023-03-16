package GraphStreamSketch;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

class basket implements Serializable {
    public short[] src = new short[DefineConstants.Roomnum];
    public short[] dst = new short[DefineConstants.Roomnum];
    public short[] weight = new short[DefineConstants.Roomnum];
    public int idx; // need to change to long if Roomnum is larger than 4.

    public basket() {
        Arrays.fill(src, (short) 0);
        Arrays.fill(dst, (short) 0);
        Arrays.fill(weight, (short) 0);
        idx = 0;
    }
}

final class DefineConstants {
    public static final int prime = 739;
    public static final int bigger_p = 1048576;
    public static final int timer = 5;
    public static final int M = 80000;
    public static final int Roomnum = 2; // This is the parameter to control the maximum number of rooms in a bucket.
}

class mapnode {
    public int h;
    public short g;
}

class linknode implements Serializable {
    public int key;
    public short weight;
    public linknode next;
}


public class GSS implements Serializable {

    private HashTable<String> mapTable = new HashTable<>();
    private int w;
    private int r;
    private int p;
    private int s;
    private int f;
    private boolean useT;
    private int tablesize;

    private basket[] value;

    //新增哈希函数类型
    public HashFunction.hashfunctions hft;

    //新增GSS的IP地址
    public String IPaddress = InetAddress.getLocalHost().getHostAddress();

    //可以将ArrayList修改为Cache类型
    // Cache<Integer, Short> cache = CacheBuilder.newBuilder().build();

    public ArrayList<linknode> buffer = new ArrayList<>();

    public HashMap<Integer, Integer> index = new HashMap<>();
    public int n;
    // count the number of edges in the buffer to assist buffer size analysis. Self loop edge is not included as it does not use additional memory.
    public int edge_num;


    public GSS(int width, int range, int p_num, int size, int f_num, boolean usehashtable, int TableSize, HashFunction.hashfunctions hashFunctionType) throws UnknownHostException {
        w = width;
        r = range; // r x r mapped baskets
        p = p_num; //candidate buckets
        s = size; //multiple rooms
        f = f_num; //finger print lenth
        hft = hashFunctionType;//新增函数类型
        n = 0;
        edge_num = 0;
        value = new basket[w * w];
        //要全部初始化为0
        //给予一个模板basket
        basket template = new basket();
        Arrays.fill(value, template);

        useT = usehashtable;
        tablesize = TableSize;

        if (usehashtable) {
            mapTable.init(tablesize);
        }
    }

    //反序列化到内存
    public GSS deserialize(String deserializeLocation) {
        GSS temp = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(deserializeLocation))) {
            temp = (GSS) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;

    }

    //序列化到文件
    public void serialize(String serializeLocation) {
        //将testGSS的内容全部写到指定文件中去
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializeLocation))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cleanupBuffer() {
        Iterator<linknode> IT = buffer.iterator();
        linknode e;
        linknode tmp;
        while (IT.hasNext()) {
            e = IT.next();
            while (e != null) {
                tmp = e.next;
                e = tmp;
            }
        }
    }

    public void insert(String s1, String s2, int weight) {

        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int hash2 = HashFunction.CalculateHashValue(s2.getBytes(StandardCharsets.UTF_8), s2.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }

        int h1 = (hash1 >>> f) % w;
        short g2 = (short) (hash2 & tmp);

        if (g2 == 0) {
            g2 += 1;
        }

        int h2 = (hash2 >>> f) % w;
        int k1 = (h1 << f) + g1;
        int k2 = (h2 << f) + g2;

        if (useT) {
            mapTable.insert(k1, s1);
            mapTable.insert(k2, s2);
        }

        int[] tmp1 = new int[r];
        int[] tmp2 = new int[r];
        tmp1[0] = g1;
        tmp2[0] = g2;

        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
            tmp2[i] = (tmp2[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }

        boolean inserted = false;
        long key = g1 + g2;

        for (int i = 0; i < p; i++) {
            key = (key * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
            int index = (int) (key % (r * r));
            int index1 = index / r;
            int index2 = index % r;
            int p1 = (h1 + tmp1[index1]) % w;
            int p2 = (h2 + tmp2[index2]) % w;


            int pos = p1 * w + p2;
            for (int j = 0; j < s; j++) {
                if ((((value[pos].idx >>> (j << 3)) & ((1 << 8) - 1)) == (index1 | (index2 << 4))) && (value[pos].src[j] == g1) && (value[pos].dst[j] == g2)) {
                    value[pos].weight[j] += weight;
                    inserted = true;
                    break;
                }
                if (value[pos].src[j] == 0) {
                    value[pos].idx |= ((index1 | (index2 << 4)) << (j << 3));
                    value[pos].src[j] = g1;
                    value[pos].dst[j] = g2;
                    value[pos].weight[j] = (short) weight;
                    inserted = true;
                    break;
                }
            }
            if (inserted) {
                break;
            }
        }
        if (!inserted) {
            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();
                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }

            if (found) {

                int tag = entryNow.getValue();

                linknode node = buffer.get(tag);

                while (true) {
                    if (node.key == k2) {
                        node.weight += weight;
                        break;
                    }
                    if (node.next == null) {
                        linknode ins = new linknode();
                        ins.key = k2;
                        ins.weight = (short) weight;
                        ins.next = null;
                        node.next = ins;
                        edge_num++;
                        break;
                    }
                    node = node.next;
                }

            } else {
                // index[k1] = n;
                index.put(k1, n);
                n++;
                linknode node = new linknode();
                node.key = k1;
                node.weight = 0;
                if (k1 != k2) //k1==k2 means loop
                {
                    linknode ins = new linknode();
                    ins.key = k2;
                    ins.weight = (short) weight;
                    ins.next = null;
                    node.next = ins;
                    edge_num++;
                } else {
                    node.weight += weight;
                    node.next = null;
                }
                buffer.add(node);
            }
        }
        tmp1 = null;
        tmp2 = null;
    }

    public void nodeSuccessorQuery(String s1, ArrayList<String> IDs) {
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }

        int h1 = (hash1 >>> f) % w;
        int k1 = (h1 << f) + g1;
        int[] tmp1 = new int[r];
        tmp1[0] = g1;

        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }
        for (int i = 0; i < r; i++) {
            int p1 = (h1 + tmp1[i]) % w;
            for (int k = 0; k < w; k++) {
                int pos = p1 * w + k;
                for (int j = 0; j < s; ++j) {
                    if ((((value[pos].idx >>> ((j << 3))) & ((1 << 4) - 1)) == i) && (value[pos].src[j] == g1)) {
                        int tmp_g = value[pos].dst[j];
                        int tmp_s = ((value[pos].idx >>> ((j << 3) + 4)) & ((1 << 4) - 1));
                        int shifter = tmp_g;
                        for (int v = 0; v < tmp_s; v++) {
                            shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                        }
                        int tmp_h = k;
                        while (tmp_h < shifter) {
                            tmp_h += w;
                        }
                        tmp_h -= shifter;
                        int val = (tmp_h << f) + tmp_g;
                        mapTable.getID(val, IDs);
                    }
                }
            }
        }

        Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
        boolean found = false;
        Map.Entry<Integer, Integer> entryNow = null;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey().equals(k1)) {
                found = true;
                entryNow = entry;
                break;
            }
        }

        if (found) {
            int tag = entryNow.getValue();
            linknode node = buffer.get(tag);
            if (node.weight != 0) {
                mapTable.getID(k1, IDs); // self-loop
            }
            node = node.next;
            while (node != null) {
                mapTable.getID(node.key, IDs);
                node = node.next;
            }
        }
        tmp1 = null;
    }

    public void nodePrecursorQuery(String s1, ArrayList<String> IDs) {
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);
        int h1 = (hash1 >>> f) % w;

        if (g1 == 0) {
            g1 += 1;
        }
        int[] tmp1 = new int[r];
        tmp1[0] = g1;

        int k1 = (h1 << f) + g1;
        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }
        for (int i = 0; i < r; i++) {
            int p1 = (h1 + tmp1[i]) % w;
            for (int k = 0; k < w; k++) {
                int pos = p1 + k * w;
                for (int j = 0; j < s; ++j) {
                    if ((((value[pos].idx >>> ((j << 3) + 4)) & ((1 << 4) - 1)) == i) && (value[pos].dst[j] == g1)) {
                        int tmp_g = value[pos].src[j];
                        int tmp_s = ((value[pos].idx >>> (j << 3)) & ((1 << 4) - 1));

                        int shifter = tmp_g;
                        for (int v = 0; v < tmp_s; v++) {
                            shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                        }
                        int tmp_h = k;
                        while (tmp_h < shifter) {
                            tmp_h += w;
                        }
                        tmp_h -= shifter;

                        int val = (tmp_h << f) + tmp_g;
                        mapTable.getID(val, IDs);
                    }
                }
            }
        }

        Iterator<Map.Entry<Integer, Integer>> it1 = index.entrySet().iterator();
        boolean found = false;
        Map.Entry<Integer, Integer> entryNow = null;
        while (it1.hasNext()) {
            Map.Entry<Integer, Integer> entry = it1.next();
            if (entry.getKey().equals(k1)) {
                found = true;
                entryNow = entry;
                break;
            }
        }


        if (found) {
            if (buffer.get(entryNow.getValue()).weight != 0) {
                mapTable.getID(k1, IDs);
            }
        }


        for (Map.Entry<Integer, Integer> current : index.entrySet()) {
            int tag = current.getValue();
            int src = current.getKey();
            linknode node = buffer.get(tag);
            node = node.next;
            while (node != null) {
                if (node.key == k1) {
                    mapTable.getID(src, IDs);
                    break;
                }
                node = node.next;
            }
        }

        tmp1 = null;
    }

    /**
     * s1 is the ID of the source node, s2 is the ID of the destination node, return the weight of the edge
     *
     * @param s1
     * @param s2
     * @return
     */
    public int edgeQuery(String s1, String s2) {
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int hash2 = HashFunction.CalculateHashValue(s2.getBytes(StandardCharsets.UTF_8), s2.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }
        int h1 = (hash1 >>> f) % w;
        short g2 = (short) (hash2 & tmp);

        if (g2 == 0) {
            g2 += 1;
        }

        int h2 = (hash2 >>> f) % w;
        int[] tmp1 = new int[r];
        int[] tmp2 = new int[r];
        tmp1[0] = g1;
        tmp2[0] = g2;
        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
            tmp2[i] = (tmp2[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }
        int key = g1 + g2;

        for (int i = 0; i < p; i++) {
            key = (key * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
            int index = key % (r * r);
            int index1 = index / r;
            int index2 = index % r;
            int p1 = (h1 + tmp1[index1]) % w;
            int p2 = (h2 + tmp2[index2]) % w;
            int pos = p1 * w + p2;
            for (int j = 0; j < s; j++) {
                if ((((value[pos].idx >>> (j << 3)) & ((1 << 8) - 1)) == (index1 | (index2 << 4))) && (value[pos].src[j] == g1) && (value[pos].dst[j] == g2)) {
                    tmp1 = null;
                    tmp2 = null;
                    return value[pos].weight[j];
                }
            }

        }

        int k1 = (h1 << f) + g1;
        int k2 = (h2 << f) + g2;

        Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
        boolean found = false;
        Map.Entry<Integer, Integer> entryNow = null;

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey().equals(k1)) {
                found = true;
                entryNow = entry;
                break;
            }
        }

        if (found) {
            int tag = entryNow.getValue();
            linknode node = buffer.get(tag);
            while (node != null) {
                if (node.key == k2) {
                    tmp1 = null;
                    tmp2 = null;
                    return node.weight;
                }
                node = node.next;
            }
        }
        tmp1 = null;
        tmp2 = null;
        return 0;
    }

    /**
     * s1 is the ID of the source node, s2 is the ID of the destination node, return whether reachable.
     *
     * @param s1
     * @param s2
     * @return
     */
    public boolean query(String s1, String s2) {
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int hash2 = HashFunction.CalculateHashValue(s2.getBytes(StandardCharsets.UTF_8), s2.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }

        int h1 = (hash1 >>> f) % w;
        short g2 = (short) (hash2 & tmp);

        if (g2 == 0) {
            g2 += 1;
        }

        int h2 = (hash2 >>> f) % w;
        HashMap<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
        LinkedList<mapnode> q = new LinkedList<mapnode>();
        mapnode e = new mapnode();
        e.h = h1;
        e.g = g1;
        q.offer(e);
        checked.put(((h1 << f) + g1), true);

        while (!q.isEmpty()) {
            e = q.peek();
            h1 = e.h;
            g1 = e.g;
            int[] tmp1 = new int[r];
            int[] tmp2 = new int[r];
            tmp2[0] = g2;
            tmp1[0] = g1;
            for (int i = 1; i < r; i++) {
                tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                tmp2[i] = (tmp2[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
            }
            for (int i1 = 0; i1 < r; i1++) {
                int p1 = (h1 + tmp1[i1]) % w;
                for (int i2 = 0; i2 < r; i2++) {
                    int p2 = (h2 + tmp2[i2]) % w;
                    int pos = p1 * w + p2;
                    for (int i3 = 0; i3 < s; i3++) {

                        if ((((value[pos].idx >>> (i3 << 3)) & ((1 << 8) - 1)) == (i1 | (i2 << 4))) && (value[pos].src[i3] == g1) && (value[pos].dst[i3] == g2)) {
                            tmp1 = null;
                            tmp2 = null;
                            return true;
                        }
                    }
                }
            }

            int k1 = (h1 << f) + g1;

            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();
                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }

            if (found) {
                int tag = entryNow.getValue();
                linknode node = buffer.get(tag);
                while (node != null) {
                    if (node.key != k1) {

                        int val = node.key;
                        int temp_h = (val) >>> f;

                        int tmp3 = (int) Math.pow(2, f);

                        short temp_g = (short) (val % tmp3);
                        if ((temp_h == h2) && (temp_g == g2)) {
                            tmp1 = null;
                            tmp2 = null;
                            return true;
                        }

                        Iterator<Map.Entry<Integer, Boolean>> it1 = checked.entrySet().iterator();
                        found = false;
                        Map.Entry<Integer, Boolean> entryNow1 = null;

                        while (it1.hasNext()) {
                            Map.Entry<Integer, Boolean> entry = it1.next();
                            if (entry.getKey().equals(val)) {
                                found = true;
                                entryNow1 = entry;
                                break;
                            }
                        }

                        if (!found) {
                            mapnode temp_e = new mapnode();
                            temp_e.h = temp_h;
                            temp_e.g = temp_g;
                            q.offer(temp_e);
                            checked.put(val, true);
                        }
                    }
                    node = node.next;
                }
            }

            for (int i1 = 0; i1 < r; i1++) {
                int p1 = (h1 + tmp1[i1]) % w;
                for (int i2 = 0; i2 < w; i2++) {
                    int pos = p1 * w + i2;
                    for (int i3 = 0; i3 < s; i3++) {
                        if (value[pos].src[i3] == g1 && (((value[pos].idx >>> (i3 << 3)) & ((1 << 4) - 1)) == i1)) {
                            int tmp_g = value[pos].dst[i3];
                            int tmp_s = ((value[pos].idx >>> ((i3 << 3) + 4)) & ((1 << 4) - 1));
                            int shifter = tmp_g;
                            for (int v = 0; v < tmp_s; v++) {
                                shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                            }
                            int tmp_h = i2;

                            while (tmp_h < shifter) {
                                tmp_h += w;
                            }

                            tmp_h -= shifter;
                            int val = (tmp_h << f) + tmp_g;
                            Iterator<Map.Entry<Integer, Boolean>> it2 = checked.entrySet().iterator();
                            found = false;

                            while (it2.hasNext()) {
                                Map.Entry<Integer, Boolean> entry = it2.next();
                                if (entry.getKey().equals(val)) {
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                mapnode tmp_e = new mapnode();
                                tmp_e.h = tmp_h;
                                tmp_e.g = (short) tmp_g;
                                q.offer(tmp_e);
                                checked.put(val, true);

                            }
                        }

                    }
                }
            }
            tmp1 = null;
            tmp2 = null;
            q.poll();
        }
        return false;
    }

    /**
     * type 0 is for successor query, type 1 is for precursor query
     *
     * @param s1
     * @param type
     * @return
     */
    public int nodeValueQuery(String s1, int type) {
        int weight = 0;
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }

        int h1 = (hash1 >>> f) % w;
        int[] tmp1 = new int[r];
        tmp1[0] = g1;

        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }

        for (int i = 0; i < r; i++) {
            int p1 = (h1 + tmp1[i]) % w;

            for (int k = 0; k < w; k++) {
                if (type == 0) //successor query
                {
                    int pos = p1 * w + k;

                    for (int j = 0; j < s; ++j) {
                        if ((value[pos].idx >>> (j << 3) & (1 << 4) - 1) == i && value[pos].src[j] == g1) {
                            weight += value[pos].weight[j];
                        }
                    }

                } else if (type == 1) //precursor query
                {
                    int pos = p1 + k * w;

                    for (int j = 0; j < s; ++j) {
                        if ((value[pos].idx >>> (j << 3) + 4 & (1 << 4) - 1) == i && value[pos].dst[j] == g1) {
                            weight += value[pos].weight[j];
                        }
                    }

                }
            }
        }
        if (type == 0) {
            int k1 = (h1 << f) + g1;
            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();

                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }

            if (found) {
                int tag = entryNow.getValue();
                linknode node = buffer.get(tag);
                weight += node.weight;
                node = node.next;

                while (node != null) {
                    weight += node.weight;
                    node = node.next;
                }
            }
        } else if (type == 1) {
            int k1 = (h1 << f) + g1;
            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();
                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }
            if (found) {
                weight += buffer.get(entryNow.getValue()).weight;
            }
            Iterator<Map.Entry<Integer, Integer>> it1 = index.entrySet().iterator();
            while (it1.hasNext()) {
                int tag = it.next().getValue();
                linknode node = buffer.get(tag);
                node = node.next;
                while (node != null) {
                    if (node.key == k1) {
                        weight += node.weight;
                    }
                    node = node.next;
                }

            }

        }
        tmp1 = null;
        return weight;
    }

    /**
     * type 0 is for successor query, type 1 is for precursor query
     *
     * @param s1
     * @param type
     * @return
     */
    public int nodeDegreeQuery(String s1, int type) {
        int degree = 0;
        int hash1 = HashFunction.CalculateHashValue(s1.getBytes(StandardCharsets.UTF_8), s1.length(), hft);
        int tmp = (int) (Math.pow(2, f) - 1);
        short g1 = (short) (hash1 & tmp);

        if (g1 == 0) {
            g1 += 1;
        }

        int h1 = (hash1 >>> f) % w;
        int[] tmp1 = new int[r];
        tmp1[0] = g1;

        for (int i = 1; i < r; i++) {
            tmp1[i] = (tmp1[i - 1] * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
        }
        for (int i = 0; i < r; i++) {
            int p1 = (h1 + tmp1[i]) % w;
            for (int k = 0; k < w; k++) {
                if (type == 0) //successor query
                {
                    int pos = p1 * w + k;
                    for (int j = 0; j < s; ++j) {
                        if ((((value[pos].idx >>> ((j << 3))) & ((1 << 4) - 1)) == i) && (value[pos].src[j] == g1)) {
                            int tmp_g = value[pos].dst[j];
                            int tmp_s = ((value[pos].idx >>> ((j << 3) + 4)) & ((1 << 4) - 1));
                            int shifter = tmp_g;
                            for (int v = 0; v < tmp_s; v++) {
                                shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                            }
                            int tmp_h = k;
                            while (tmp_h < shifter) {
                                tmp_h += w;
                            }
                            tmp_h -= shifter;
                            int val = (tmp_h << f) + tmp_g;


                            degree += mapTable.countIDnums(val);
                        }
                    }
                } else if (type == 1) //precursor query
                {
                    int pos = p1 + k * w;
                    for (int j = 0; j < s; ++j) {
                        if ((((value[pos].idx >>> ((j << 3) + 4)) & ((1 << 4) - 1)) == i) && (value[pos].dst[j] == g1)) {
                            int tmp_g = value[pos].src[j];
                            int tmp_s = ((value[pos].idx >>> (j << 3)) & ((1 << 4) - 1));
                            int shifter = tmp_g;
                            for (int v = 0; v < tmp_s; v++) {
                                shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                            }
                            int tmp_h = k;
                            while (tmp_h < shifter) {
                                tmp_h += w;
                            }
                            tmp_h -= shifter;
                            int val = (tmp_h << f) + tmp_g;
                            degree += mapTable.countIDnums(val);
                        }
                    }
                }
            }
        }

        if (type == 0) {
            int k1 = (h1 << f) + g1;
            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();

                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }

            if (found) {
                int tag = entryNow.getValue();
                linknode node = buffer.get(tag);
                if (node.weight != 0) {
                    degree += mapTable.countIDnums(k1); // address self-loops first
                }

                node = node.next;
                while (node != null) {
                    degree += mapTable.countIDnums(node.key);
                    node = node.next;
                }
            }
        } else if (type == 1) {

            int k1 = (h1 << f) + g1;

            Iterator<Map.Entry<Integer, Integer>> it = index.entrySet().iterator();
            boolean found = false;
            Map.Entry<Integer, Integer> entryNow = null;

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();

                if (entry.getKey().equals(k1)) {
                    found = true;
                    entryNow = entry;
                    break;
                }
            }

            if (found) {
                if (buffer.get(entryNow.getValue()).weight != 0) {
                    degree += mapTable.countIDnums(k1);
                }
            }

            Iterator<Map.Entry<Integer, Integer>> it1 = index.entrySet().iterator();
            while (it1.hasNext()) {
                int tag = it.next().getValue();
                linknode node = buffer.get(tag);
                int src = node.key;
                node = node.next;
                while (node != null) {
                    if (node.key == k1) {
                        degree += mapTable.countIDnums(src);
                        break;
                    }
                    node = node.next;
                }
            }
        }
        tmp1 = null;
        return degree;
    }

    private void hvinsert(int hash, ArrayList<HashValue> v) {
        boolean find = false;
        for (HashValue hashValue : v) {
            if (hash == hashValue.key) {
                hashValue.IDnum++;
                find = true;
                break;
            }
        }
        if (!find) {
            HashValue hv = new HashValue();
            hv.key = hash;
            hv.IDnum = 1;
            v.add(hv);
        }
    }

    public int TriangleCounting() {
        GSketch gs = new GSketch();
        for (int i = 0; i < tablesize; i++) {
            HashTableNode<String> np = mapTable.table[i];
            ArrayList<HashValue> nodes = new ArrayList<HashValue>();
            for (; np != null; np = np.next) {
                hvinsert(np.key, nodes);
            }
            for (HashValue node : nodes) {
                gs.insert_node(node.key, node.IDnum);
            }
            nodes.clear();
        }
        for (int i = 0; i < w; i++) {
            int row = i * w;
            for (int k = 0; k < w; k++) {
                int pos = row + k;
                for (int j = 0; j < s; j++) {
                    if (value[pos].src[j] > 0) {
                        int tmp_g1 = value[pos].src[j];
                        int tmp_s1 = ((value[pos].idx >>> (j << 3)) & ((1 << 4) - 1));
                        int shifter = tmp_g1;

                        for (int v = 0; v < tmp_s1; v++) {
                            shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                        }

                        int tmp_h1 = i;

                        while (tmp_h1 < shifter) {
                            tmp_h1 += w;
                        }

                        tmp_h1 -= shifter;
                        int val1 = (tmp_h1 << f) + tmp_g1;
                        int tmp_g2 = value[pos].dst[j];
                        int tmp_s2 = ((value[pos].idx >>> ((j << 3) + 4)) & ((1 << 4) - 1));
                        shifter = tmp_g2;

                        for (int v = 0; v < tmp_s2; v++) {
                            shifter = (shifter * DefineConstants.timer + DefineConstants.prime) % DefineConstants.bigger_p;
                        }

                        int tmp_h2 = k;

                        while (tmp_h2 < shifter) {
                            tmp_h2 += w;
                        }

                        tmp_h2 -= shifter;
                        int val2 = (tmp_h2 << f) + tmp_g2;
                        gs.insert_edge(val1, val2);
                    }
                }
            }
        }
        for (linknode np : buffer) {
            int src = np.key;
            np = np.next;

            for (; np != null; np = np.next) {
                int dst = np.key;
                gs.insert_edge(src, dst);
            }

        }
        gs.clean();
        return gs.countTriangle();
    }
}



