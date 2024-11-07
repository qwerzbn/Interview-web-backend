package com.zbn.Interview.es;

public class TEST {
    public static double calculate_price(double baseprise, int extras, String ID) {
        double result = 0.0;
        double discount = 0.0;
        if (extras < 0 || baseprise <= 0.0)
            return -1.0;
        if (extras >= 5)
            discount = 15;
        else if (extras >= 3)
            discount = 10;
        else
            discount = 0;
        result = baseprise / 100.0 * (100 - discount - calculate_special(ID));
        return result;
    }
    public static double calculate_special(String ID) {
        if (ID.equals("SVIP")) {
            return 20;
        } else if (ID.equals("VIP")) {
            return 5;
        }
        return 0;
    }
    public static void main(String[] args) {
        // 测试不同的输入
        System.out.println("价格计算结果：");
// 测试用例 1：baseprise=100  extras=3 ID = “VIP” 应该返回 85.0
        System.out.println("Test 1: " + calculate_price(100, 3, "VIP"));
// 测试用例 2：baseprise=100  extras=5 ID = “SVIP” 应该返回130.0
        System.out.println("Test 2: " + calculate_price(200, 5, "SVIP"));
// 测试用例 3：baseprise=100  extras=1 ID = “USER” 应该返回130.0
        System.out.println("Test 3: " + calculate_price(150, 1, "USER")); // 应该返回100
// 测试用例 4：baseprise=100  extras=1 ID = “VIP” 应该返回130.0
        System.out.println("Test 4: " + calculate_price(0, 2, "VIP"));   // 应该返回 -1.0
        System.out.println("Test 5: " + calculate_price(100, -1, "SVIP"));    // 应该返回 -1.0
    }
}
