## 题目描述
``` text
请完成一个函数，输入一个二叉树，该函数输出它的镜像。

例如输入：

     4
   /   \
  2     7
 / \   / \
1   3 6   9
镜像输出：

     4
   /   \
  7     2
 / \   / \
9   6 3   1
```

## 示例
``` text
输入：root = [4,2,7,1,3,6,9]
输出：[4,7,2,9,6,3,1]
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
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        if (root == null) {
           return null;
        }
        TreeNode leftNode = mirrorTree(root.right);
        TreeNode rightNode = mirrorTree(root.left);
        root.left = leftNode;
        root.right = rightNode;
        return root;
    }
}
```