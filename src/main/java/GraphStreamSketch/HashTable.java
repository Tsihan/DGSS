package GraphStreamSketch;

import java.io.Serializable;
import java.util.ArrayList;

class HashValue implements Comparable{
    public int key;
    public int IDnum;

    @Override
    public int compareTo(Object o) {

        HashValue hv = (HashValue) o;
        if (this.key < hv.key) {
            return 1;
        } else if (this.key > hv.key) {
            return -1;
        }
        return 0;
    }
}


class HashTableNode<T> implements Serializable{
    public T value = null;
    public int key;
    public HashTableNode<T> next;

}


public class HashTable<T> implements Serializable {
    //一张HashTable具有很多个HashTableNode节点
    public HashTableNode<T>[] table;
    //HashTable的大小
    public int tableSize;

    HashTable() {

    }

    HashTable(int s) {
        tableSize = s;

        table = (HashTableNode<T>[]) new Object[s];

        for (int i = 0; i < s; i++) {
            table[i] = null;
        }
    }


//    public static boolean mycomp(GraphStreamSketch.HashValue hv1, GraphStreamSketch.HashValue hv2) {
//        return hv1.key < hv2.key;
//    }

    public static boolean equalsTo(HashValue hv1, HashValue hv2) {
        return hv1.key == hv2.key;
    }

    public static int countjoin(ArrayList<HashValue> V1, ArrayList<HashValue> V2) {
        int i1 = 0;
        int i2 = 0;
        int count = 0;
        while (i1 < V1.size()) {
            if (i2 >= V2.size()) {
                return count;
            }
            while (V2.get(i2).key < V1.get(i1).key) {
                i2++;
                if (i2 >= V2.size()) {
                    return count;
                }
            }
            if (V2.get(i2).key == V1.get(i1).key) {
                count += V1.get(i1).IDnum;
                i1++;
                i2++;
            } else if (V2.get(i2).key > V1.get(i1).key) {
                i1++;
            }
        }
        return count;
    }

    /**
     * 将原始的哈希键值对插入表中
     *
     * @param hash  通过各种哈希函数得到的哈希值
     * @param value 原始value
     */
    public void insert(int hash, T value) {
        HashTableNode<T> np;
        boolean inTable;
        np = table[hash % tableSize];
        inTable = false;
        for (; np != null; np = np.next) {
            if (np.key == hash && np.value == value) {
                inTable = true;
                break;
            }
        }
        if (!inTable) {
            HashTableNode<T> newNode = new HashTableNode<T>();
            newNode.key = hash;
            newNode.value = value;
            newNode.next = table[hash % tableSize];
            table[hash % tableSize] = newNode;
        }
    }

    /**
     * 获得同一key值下的全部value
     *
     * @param hash 通过各种哈希函数得到的哈希值
     * @param IDs  存储所有ID（哈希value）的arraylist
     */
    public void getID(int hash, ArrayList<T> IDs) {
        HashTableNode<T> np;
        np = table[hash % tableSize];
        for (; np != null; np = np.next) {
            if (np.key == hash) {
                IDs.add(np.value);
            }
        }
    }

    /**
     * 获取同一key值出现的次数
     *
     * @param hash 通过各种哈希函数得到的哈希值
     * @return 同一key值出现的次数
     */
    public int countIDnums(int hash) {
        int num = 0;
        HashTableNode<T> np;
        np = table[hash % tableSize];
        for (; np != null; np = np.next) {
            if (np.key == hash) {
                num++;
            }
        }
        return num;
    }


    public void init(int s) {
        tableSize = s;
       // table = (GraphStreamSketch.HashTableNode<T>[]) new Object[s];
        table = new HashTableNode[s];

        for (int i = 0; i < s; i++) {
            table[i] = null;
        }
    }
}
