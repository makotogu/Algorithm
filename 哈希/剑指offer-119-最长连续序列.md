## 题目描述
给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。

## 示例
``` text
输入：nums = [100,4,200,1,3,2]
输出：4
解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
```

## 示例代码
``` java
class Solution {
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set =new HashSet<>();
        for(int num : nums) set.add(num);
        int max = 0;
        for (int num: nums) {
            if (!set.contains(num-1)) { //有比自己小的就不用计算了(因为比自己小的肯定比当前长)，减少重复
                int len = 1;
                while (set.contains(num+1)){
                    len++;
                    num++;
                } 
                max = Math.max(max, len);
            } 
        }
        return max;
    }
}
```