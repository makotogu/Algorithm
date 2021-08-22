## 题目描述
某乐团的演出场地可视作 num * num 的二维矩阵 grid（左上角坐标为 [0,0])，每个位置站有一位成员。乐团共有 9 种乐器，乐器编号为 1~9，每位成员持有 1 个乐器。

为保证声乐混合效果，成员站位规则为：自 grid 左上角开始顺时针螺旋形向内循环以 1，2，...，9 循环重复排列。例如当 num = 5 时，站位如图所示

![image](./img/LCP-29-乐团站位.png)
请返回位于场地坐标 [Xpos,Ypos] 的成员所持乐器编号。

## 示例
``` text
输入：num = 3, Xpos = 0, Ypos = 2

输出：3
```

## 示例代码
``` java
class Solution {
    public int orchestraLayout(int num, int xPos, int yPos) {
        //第几圈减1
        long ranking = Math.min(xPos,yPos);
        ranking = Math.min(ranking,Math.min(num - (xPos + 1),num - (yPos + 1)));

        //第几圈的边长
        long side = num - 2 * (ranking);

        //前面的总数

        long sum = (long)num * num - side * side;

        //根据坐标计算

        //本圈左上角
        if (xPos == yPos) {
            sum = sum + 1;
        //本圈右上角
        } else if (xPos + 1 == num - yPos) {
            sum = sum + side;
        //本圈左下角
        } else if (yPos + 1 == num - xPos) {
            sum = sum + 3 * side -2;
        //本圈右下角
        } else if (num - xPos == num - yPos) {
            sum = sum + 2 * side - 1;
        //本圈上边
        } else if (xPos == ranking) {
            sum = sum + yPos + 1 - ranking;
        //本圈左边
        } else if (yPos == ranking) {
            sum = sum + 3 * side - 2 + (num - xPos - 1 - ranking);
        //本圈下边
        } else if (num - xPos - 1 == ranking) {
            sum = sum + 2 * side - 1 + (num - yPos - 1 - ranking);
        //本圈右边
        } else if (num - yPos - 1 == ranking) {
            sum = (sum + side) + (xPos - ranking);
        }
        return (int)(sum % 9 == 0 ? 9 : sum % 9);
    }
}
```


## 思路
数学题