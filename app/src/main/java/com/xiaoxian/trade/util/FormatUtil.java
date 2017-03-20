package com.xiaoxian.trade.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 相关格式的工具类
 */

public class FormatUtil {
    /**
     * 判断电话号码是否有效
     * 移动：134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、184、187、188
     * 联通：130、131、132、145、155、156、185、186
     * 电信：133、153、180、181、189
     * 虚拟：17x
     */
    public static boolean isMobileNO(String number) {
        if (number.startsWith("+86")) {
            number = number.substring(3);
        } else if (number.startsWith("+") || number.startsWith("0")) {
            number = number.substring(1);
        }
        //查找替换，去掉空格和连接符
        number = number.replace(" ", "").replace("-", "");
        /**
         * compile()：将给定正则表达式进行编译
         * ^ 和 $：锁定模式应用范围（匹配字符串的开始、结束）
         * "\"用于字符转义，\d{8}表示连续重复匹配8次，等价\d\d\d\d\d\d\d\d
         * Matcher类构造方法私有，不能随意创建,只能通过Pattern.matcher()得到实例
         * matches()：对整个字符串进行匹配，全部匹配才返回true
         */
        Pattern p = Pattern.compile("^1(3[0-9])|(4[57])|(5[0-35-9])|(8[0-9])|(7[0-9])\\d{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 邮件格式
     * 合法Email地址：
     * 1. 有且只有一个符号"@"
     * 2. 首个字符不是"@"、"."
     * 3. 结尾字符不是"@"、"."
     * 4. 允许"@"前字符出现"+"
     * 5. 不许出现"@."、".@"
     * 6. 不许"+"最前或者"+@"
     * ?表示重复0次/1次，{2,}表示重复2次或更多次
     */
    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 用户名匹配
     * {2,10}表示重复2次到10次
     */
    public static boolean isCorrectUserName(String name) {
        Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 密码匹配
     * 以字母开头，长度区间[6-18]，只能包含字符、数字及下划线
     */
    public static boolean isCorrectPassword(String password) {
        Pattern p = Pattern.compile("\\w{6,18}");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 检测号码位数
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || "".equals(phone) || !Pattern.compile("1[0-9]{10}").matcher(phone).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isNullOrEmpty(String text) {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
