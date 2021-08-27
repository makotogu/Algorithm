# TreeMap的特点
* 概念：
  * TreeMap是一个双列集合，是Map的子类，底层是红黑树结构构成。
* 特点：
  * 元素中键不能重复
  * 元素会按照大小顺序排序

# TreeMap的数据结构
## 二叉查找树
### 二叉查找树定义
* 特点：
  1. 若左子树不空，则左子树上所有结点的值均小于它的根节点的值；
  2. 若右子树不空，则右子树上所有结点的值均大于它的根节点的值；
  3. 左、右子树也分别为二叉树；
  4. 没有相等的节点
* 结论：
  * 二叉查找树就是每个结点的值按照大小排列的二叉树，二叉查找树方便对结点的值进行查找
  
### 二叉查找树的查找操作
* 查找方式
  * 从根节点开始，如果要查找的数据等于结点的值，那就返回；
  * 如果要查找的数据小于结点的值，那就再左子树中递归查找；
  * 如果要查找的数据大于结点的值，那就在右子树中递归查找。

## 平衡二叉树
### 平衡二叉树的定义
* 为了避免出现“瘸子”现象，减少树的高度，提高我们的搜索效率，又存在一种树的结构“平衡二叉树”
* 它的左右两个子树的高度差绝对值不超过1，并且左右两个子树都是一颗平衡二叉树

### 平衡二叉树的旋转
* 概念
  * 在构建一颗平衡二叉树的过程中，当有新的结点要插入时，检查是否因插入后破坏了树的平衡，如果是，则需要做旋转去改变树的结构
* 两种旋转方式
  * 左旋
    * 左旋就是将结点的右支往左拉，右子结点变成父结点，并把晋升之后把多余的左子结点出让给降级结点的右子结点
  * 右旋
    * 将结点的左支往右拉，左子结点变成了父结点，并把晋升之后多余的右子结点出让给降级结点的左子结点

## 红黑树
### 红黑树的定义
* 概述：
  * 红黑树是一种自平衡的二叉树
  * 红黑树的每一个结点上都有存储位表示结点的颜色，可以是红或者黑
  * 红黑树不是高度平衡的，它的平衡是通过“红黑树的特性”进行实现的
* 红黑树的特性：
  1. 每一个结点或是红色的，或者是黑色的；
  2. 根结点必须是黑色的；
  3. 每个叶结点是黑色的（叶结点NIL）；
  4. 如果某一个结点是红色，那么它的子结点必须是黑色（不能出现两个红色结点相连的情况）；
  5. 对每一个结点，从该结点到其所有后代叶结点的简单路径上，均包含相同数目的黑色结点。

# TreeMap的源码分析
## get()获取方法分析
get的方法
``` java
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
    }
```
Entry是结点
``` java
    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key; //TreeNode的键
        V value; //TreeNode的值
        Entry<K,V> left; //TreeNode的当前节点左子结点
        Entry<K,V> right; //TreeNode的当前节点右子结点
        Entry<K,V> parent; //TreeNode的当前节点父结点
        boolean color = BLACK; //TreeNode的当前节点的颜色

        //省略下方
    }
```
getEntry()
``` java
    final Entry<K,V> getEntry(Object key) {
        // Offload comparator-based version for sake of performance
        if (comparator != null)
            return getEntryUsingComparator(key);
        //如果键不存在 抛出空指针异常
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        // 把Object类型的键向下转型为Comparable
            Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key); //比较方法 Integer情况下就是做减法
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        //如果没有匹配的结点返回null
        return null;
    }
```
getEntryUsingComparator()
``` java
    final Entry<K,V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
            K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }
```

## put() 方法
``` java
    public V put(K key, V value) {
        Entry<K,V> t = root;
        if (t == null) {
            compare(key, key); // type (and possibly null) check 判断key的类型
            //新建一个结点
            root = new Entry<>(key, value, null);
            size = 1; //长度加一
            modCount++; //记录被修改的次数
            return null;
        }
        //根节点不是null进行下面代码
        int cmp;
        Entry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        else {
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
                Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }
```
compare()
``` java
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
            : comparator.compare((K)k1, (K)k2);
    }
```
setValue()
``` java
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
```
fixAfterInsertion()
``` java
    private void fixAfterInsertion(Entry<K,V> x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
```

# 自定义TreeMap集合
使用二叉树实现TreeMap集合，编号put(),get(),remove()方法