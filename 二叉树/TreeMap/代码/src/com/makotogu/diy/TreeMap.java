package com.makotogu.diy;

import javafx.scene.text.TextBoundsType;

import java.util.Comparator;
import java.util.Map.Entry;

/**
 * @author jianw
 */
public class TreeMap<K, V> {

    // 定义一个比较器变量
    private final Comparator<? super K> comparator;
    // 根节点
    private Entry<K, V> root;
    // 定义集合的长度
    private int size;

    // 空参构造
    public TreeMap() {
        comparator = null;
    }

    // 有参构造
    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    // 定义内部类表示键值对
    private static class Entry<K, V> {
        K k; //键
        V v; //值
        Entry<K, V> left; //左子结点
        Entry<K, V> right; //右子结点
        Entry<K, V> parent; //父结点

        // 有参构造
        public Entry(K k, V v, Entry<K, V> left, Entry<K, V> right, Entry<K, V> parent) {
            this.k = k;
            this.v = v;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    // 获取集合长度
    public int size() {
        return size;
    }

    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.v;
    }

    // 根据键去获得Entry对象的方法
    private Entry<K, V> getEntry(Object key) {
        // 非空校验
        if (key == null) {
            throw new NullPointerException();
        }

        // 根结点取名
        Entry<K, V> t = root;

        // 有没有传入比较器
        if (comparator != null) {
            K k = (K) key;
            // 循环
            while (t != null) {
                int compare = comparator.compare(k, t.k);
                if (compare < 0) {
                    t = t.left;
                } else if (compare > 0) {
                    t = t.right;
                } else {
                    return t;
                }
            }
        } else {
            Comparable<K> k = (Comparable<K>) key;
            while (t != null) {
                int compare = k.compareTo(t.k);
                if (compare < 0) {
                    t = t.left;
                } else if (compare > 0) {
                    t = t.right;
                } else {
                    return t;
                }
            }
        }
        // 如果找不到返回null
        return null;
    }

    // put()方法的实现
    public V put(K key, V value) {
        // 给根结点赋值
        Entry<K, V> t = root;
        // 非空校验
        if (key == null) {
            throw new NullPointerException();
        }
        // 集合是否为空
        if (t == null) {
            // 创建新节点
            Entry<K, V> entry = new Entry<>(key, value, null, null, null);
            // 给根结点赋值
            root = entry;
            // 集合长度加一
            size++;
            return null;
        }
        // 创建键值对 新增节点的父结点
        Entry<K, V> parent = t;
        // 定义compare
        int compare = 0;
        if (comparator != null) {
            while (t != null) {
                // 给parent完成赋值
                parent = t;
                // 判断键
                compare = comparator.compare(key, t.k);
                if (compare < 0) {
                    t = t.left;
                } else if (compare > 0) {
                    t = t.right;
                } else {
                    //用一个新的值替换旧的值，把旧的值作为返回值返回
                    V v = t.v;
                    t.v = value;
                    return v;
                }
            }
        } else {
            Comparable<? super K> k = (Comparable<? super K>) key;
            while (t != null) {
                parent = t;
                compare = k.compareTo(t.k);
                if (compare < 0) {
                    t = t.left;
                } else if (compare > 0) {
                    t = t.right;
                } else {
                    V v = t.v;
                    t.v = value;
                    return v;
                }
            }
        }
        // 要添加的键值对 键不重复
        Entry<K, V> entry = new Entry<>(key, value, null, null, parent);
        if (compare < 0) {
            parent.left = entry;
        } else {
            parent.right = entry;
        }
        // 集合长度加一
        size++;
        return null;
    }

    // remove()方法的实现
    public V remove(K key){
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        // 删除操作
        if (entry.left == null && entry.right != null) {
            // 有右子结点没左子结点
            // 判断要删除的节点是父结点右子结点
            if (entry == root) {
                root = entry.right;
            } else if (entry.parent.right == entry) {
                entry.parent.right = entry.right;
            } else if (entry.parent.left == entry) {
                entry.parent.left = entry.right;
            }
            // 让被删除结点的子结点指向父结点
            entry.right.parent = entry.parent;
        } else if (entry.left != null && entry.right == null) {
            // 有左子结点没右子结点
            if (entry == root) {
                root =entry.right;
            } else if (entry.parent.right == entry) {
                entry.parent.right = entry.left;
            } else if (entry.parent.left == entry) {
                entry.parent.left = entry.left;
            }
            entry.left.parent = entry.parent;
        } else if (entry.left != null && entry.right != null) {
            // 有左子结点有右子结点
            // 用右子结点的最左结点 或 左子结点的最右结点
            // 这里是右子结点的最左子结点方法
            Entry<K, V> target = entry.right;
            while (target.left != null) {
                target = target.left;
            }
            if (entry.right == target) {
                // 右子结点作为后继节点
                target.parent = entry.parent;
                if (entry == root) {
                    root = target;
                }else if (entry.parent.right == entry) {
                    entry.parent.right = target;
                } else if (entry.parent.left == entry) {
                    entry.parent.left = target;
                }
                // 被删除的结点的左子结点重新指向新的父亲
                entry.left.parent = target;
                target.left = entry.left;
            } else {
                // 右子结点的最左子结点作为后继节点
                if (target.right == null) {
                    // 后继结点无右子结点
                    target.parent.left = null;
                } else {
                    // 后继结点还有右子结点
                    target.parent.left = target.right;
                    //target.right.parent = target.parent;  浪费一个结点空间 同样可以实现
                    target.right = target.parent;
                }
                // 让后继结点去替换被删除的结点
                if (entry == root) {
                    root = target;
                } else if (entry.parent.right == entry) {
                    entry.parent.right = target;
                } else if (entry.parent.left == entry) {
                    entry.parent.left = target;
                }
                // 被删除结点左右子树需要指向后继结点
                entry.left.parent = target;
                entry.right.parent = target;
                target.left = entry.left;
                target.right = entry.right;
            }
        } else {
            // 为叶子结点
            if (entry == root) {
                root = null;
            } else if (entry.parent.right == entry) {
                entry.parent.right = null;
            } else if (entry.parent.left == entry) {
                entry.parent.left = null;
            }
        }
        // 集合长度减一
        size--;
        return entry.v;
    }

    // toString()方法的实现
    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }
        String s = "{";
        String s1 = method(root);
        s = s+s1.substring(0,s1.length()-2);
        s += "}";
        return s;
    }

    // 递归的方法
    private String method(Entry<K, V> entry) {
        String s = "";

        // 拼接左子结点
        if (entry.left != null) {
            s += method(entry.left);
        }
        // 拼接中间结点自己
        s += entry.k + "=" + entry.v + ", ";
        // 拼接右子结点
        if (entry.right != null) {
            s += method(entry.right);
        }
        return s;
    }
}
