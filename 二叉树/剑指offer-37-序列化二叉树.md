## 题目描述
请实现两个函数，分别用来序列化和反序列化二叉树。

你需要设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

****
某种意义上来说leetcode的官方的序列化是一种包含null的层序
****

## 示例
![image](./img/剑指offer-37-序列化二叉树.jpg)
``` text
输入：root = [1,2,3,null,null,4,5]
输出：[1,2,3,null,null,4,5]
```

## 示例代码
``` java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root == null) return "[]";
        StringBuilder res = new StringBuilder("[");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if(node != null) {
                res.append(node.val + ",");
                queue.add(node.left);
                queue.add(node.right);
            }
            else res.append("null,");
        }
        res.deleteCharAt(res.length() - 1);
        res.append("]");
        return res.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data.equals("[]")) return null;
        String[] vals = data.substring(1, data.length() - 1).split(",");
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
        Queue<TreeNode> queue = new LinkedList<>() {{ add(root); }};
        int i = 1;
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if(!vals[i].equals("null")) {
                node.left = new TreeNode(Integer.parseInt(vals[i]));
                queue.add(node.left);
            }
            i++;
            if(!vals[i].equals("null")) {
                node.right = new TreeNode(Integer.parseInt(vals[i]));
                queue.add(node.right);
            }
            i++;
        }
        return root;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.deserialize(codec.serialize(root));
```

## 思路
这个理解起来很玄幻，感觉与其说是考二叉树，更是考设计。
如果实现leetcode的二叉树序列化就是层序，由于需要保留完整结构的二叉树，要同时记录null信息。

* 序列化Serialize流程：
  * 特例处理： 若 root 为空，则直接返回空列表 "[]" ；
  * 初始化： 队列 queue （包含根节点 root ）；序列化列表 res ；
  * 层序遍历： 当 queue 为空时跳出；
    * 节点出队，记为 node ；
    * 若 node 不为空：① 打印字符串 node.val ，② 将左、右子节点加入 queue ；
    * 否则（若 node 为空）：打印字符串 "null" ；
  * 返回值： 拼接列表，用 ',' 隔开，首尾添加中括号；

* 反序列化Deserialize流程：
  * 特例处理： 若 data 为空，直接返回 null ；
  * 初始化： 序列化列表 vals （先去掉首尾中括号，再用逗号隔开），指针 i = 1 ，根节点 root （值为 vals[0] ），队列 queue（包含 root ）；
  * 按层构建： 当 queue 为空时跳出；
    * 节点出队，记为 node ；
    * 构建 node 的左子节点：node.left 的值为 vals[i] ，并将 node.left 入队；
    * 执行 i += 1 ；
    * 构建 node 的右子节点：node.left 的值为 vals[i] ，并将 node.left 入队；
    * 执行 i += 1 ；
  * 返回值： 返回根节点 root 即可；
