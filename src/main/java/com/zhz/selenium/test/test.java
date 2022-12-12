package com.zhz.selenium.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.zhz.selenium.pojo.Product;
import com.zhz.selenium.pojo.Search;
import com.zhz.selenium.pojo.SearchResult;
import com.zhz.selenium.util.DateUtil;
import com.zhz.selenium.util.StringUtil;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import javax.sound.midi.Soundbank;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.util.HashSet;

public class test {
    public static void main(String[] args) throws ParseException {
//        int a = 700;
//        int b = 0700;
//        System.out.println(a==b);
//        System.out.println(a);
//        System.out.println(b);
//
//        int c = 800;
//        int d = 800;
//        System.out.println(c==d);
////        System.out.println(c.equals(d));
//        Product search = new Product();
//        SearchResult searchResult = new SearchResult();
//        searchResult.setNotNormalCount(search.getId());
//        System.out.println(searchResult);

//        int arr[] = {2,5,5,11};
//        int[] ints = twoSum(arr, 10);
//        int[] ints1 = twoSum1(arr, 10);
//        int[] ints2 = twoSum2(arr, 10);
//        for (int a:ints) {
//            System.out.println(a);
//        }

//        boolean palindrome = isPalindrome(121);
//        System.out.println(palindrome);

//        int mcmlcx = romanToInt1("CMLII");
//        System.out.println(mcmlcx);

//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        DateTime dateTime = DateUtil.offsetDay(DateUtil.parse(date), -30);
//        String format = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
//        System.out.println(dateTime.toString());
//        System.out.println(format);
//        System.out.println(date);
//         int a = 5;
//         double b = 5;
//        System.out.println(a/100);
//        System.out.println(b/100);

//
//        String a = "";
////        int i = Integer.parseInt(a);
////        System.out.println(i);
//
//        String s = StringUtil.toString(a);
//        System.out.println(s);

//        test123();
//        HashSet s = new HashSet();
//        s.add("a");
//        s.add("a");
//        System.out.println(s);
//        String a = "B0928D5R6B";
//        String[] split = a.split(",");
//        System.out.println(split);
//        LocalDateTime localStart=LocalDateTime.of(2022,9,1,0,0);
//        LocalDateTime localEmd=LocalDateTime.of(2022,9,8,10,20);
//        OffsetDateTime offsetDateTime1 = OffsetDateTime.of(localStart, ZoneOffset.ofHours(-4));
//        OffsetDateTime offsetDateTime2 = OffsetDateTime.of(localEmd, ZoneOffset.ofHours(-4));
//        System.out.println(localStart);
//        System.out.println(localEmd);
//        System.out.println(offsetDateTime1);
//        System.out.println(offsetDateTime2);
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -10));
//
//        LocalDateTime localStart=LocalDateTime.of(Integer.parseInt(startTime.substring(0,4))
//                ,Integer.parseInt(startTime.substring(5,7)),Integer.parseInt(startTime.substring(8,10)),0,0);
//
//        OffsetDateTime offsetDateTime1 = OffsetDateTime.of(localStart, ZoneOffset.ofHours(-4));
//        System.out.println(offsetDateTime1);
//        TreeSet<String> objects = new TreeSet<>();
//        for (int i = 0; i < 100000; i++) {
//            Integer uuid=UUID.randomUUID().toString().replaceAll("-","").hashCode();
//            uuid = uuid < 0 ? -uuid : uuid;
//            objects.add(uuid);
//            System.out.println(uuid);
//        }
//        System.out.println(objects.size());
//        for (int i = 0; i < 5000000; i++) {
//            Random random = new Random();
//            StringBuffer uuid = new StringBuffer();
//            int count = 10/32+(10%32==0?0:1);
//            for (int j=1;j<=count;j++){
//                uuid.append(UUID.randomUUID());
//            }
//            int uuid1 = uuid.toString().replaceAll("-", "").substring(0, 10).hashCode();
//            uuid1 = uuid1 < 0 ? -uuid1 : uuid1;
//            objects.add(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date())+uuid1+random.nextInt(99)+random.nextInt(99));
//            System.out.println(uuid1);
//        }
//        System.out.println(objects.size());

//        BigDecimal decimal = new BigDecimal("");
//        System.out.println(decimal.compareTo(BigDecimal.ZERO));

//        long l = Long.parseLong("1669305600000");
//        Date date = DateUtil.parseDate(l);
//        System.out.println(date);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//        String formatDate = sdf.format(date);
//        System.out.println(formatDate); //2021-12-21 00:00:00 || 2021-12-21 22:38:01
//        Date format = sdf.parse("2022-11-23");
//        System.out.println(sdf1.format(format));
//        System.out.println(DateUtil.parseDate(sdf.parse("2022-11-23"),DateUtil.DATE_PATTERN_yyyyMMdd));

        String arr[] = {"asd","asdd"};
        System.out.println(Arrays.toString(arr));

    }

    public static void test123(){
        try {
            System.out.println("之前");
            int b = 0;
            int a = 1/b;
            System.out.println("之后");
        } catch (Exception e) {
            e.printStackTrace();
            test123();
            throw new RuntimeException();
        }
        System.out.println("-----");
    }

    public static int[] twoSum(int[] nums, int target) {
        for (int j = 0; j < nums.length; j++) {
            int a = nums[j];
            for (int i = j+1; i <nums.length ; i++) {
                int b = nums[i];
                if (i+1>nums.length){
                    return new int[]{};
                }
                if (target==a+b){
                    return new int[]{j,i};
                }
            }
        }
        return new int[]{};
    }

    public  static int[] twoSum1(int[] nums, int target) {
        int[] res = new int[2];
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<nums.length;i++){
            if(list.contains(nums[i])){
                res[0]=list.indexOf(nums[i]);
                res[1]=i;
            }
            list.add(target-nums[i]);
        }
        return res;
    }

    public  static int[] twoSum2(int[] nums, int target) {
        int[] res = new int[2];
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i=0;i<nums.length;i++){
            if(map.containsKey(nums[i])){
                res[0]=map.get(nums[i]);
                res[1]=i;
            }
            map.put(target-nums[i], i);
        }
        return res;
    }


    public static boolean isPalindrome(int x) {
        boolean flag = true;
        String str = String.valueOf(x);
        char arr[] = str.toCharArray();
        for (int i = 0; i <arr.length ; i++) {
            char f = arr[i];
            for (int j = arr.length-i-1; j >= 0; j--) {
                char s = arr[j];
                if (f==s){
                    break;
                }else {
                    return false;
                }
            }
        }
        return flag;
    }
    public static boolean isPalindrome2(int x) {
                //思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
                if (x < 0 || (x % 10 == 0 && x != 0)) return false;
                int revertedNumber = 0;
                while (x > revertedNumber) {
                    revertedNumber = revertedNumber * 10 + x % 10;
                    x /= 10;
                }
                return x == revertedNumber || x == revertedNumber / 10;
            }


//I             1
//V             5
//X             10
//L             50
//C             100
//D             500
//M             1000
    public static int romanToInt(String s) {
        int sum = 0;
        String I = "1";
        String V = "5";
        String X = "10";
        String L = "50";
        String C = "100";
        String D = "500";
        String M = "1000";
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('I',Integer.parseInt(I));
        map.put('V',Integer.parseInt(V));
        map.put('X',Integer.parseInt(X));
        map.put('L',Integer.parseInt(L));
        map.put('C',Integer.parseInt(C));
        map.put('D',Integer.parseInt(D));
        map.put('M',Integer.parseInt(M));
        char[] chars = s.toCharArray();
        if (chars.length==1){
            return map.get(chars[0]);
        }
        if (chars.length==2){
            if (map.get(chars[0])>=map.get(chars[1])){
                return map.get(chars[0])+map.get(chars[1]);
            }else {
                return map.get(chars[1])-map.get(chars[0]);
            }
        }
        for (int i = 0; i < chars.length; i++) {
            if (chars.length>2){
                if (i==chars.length-1){
                    return map.get(chars[i])+sum;
                }
                if (i==0){
                    if (map.get(chars[i])<map.get(chars[i+1])){
                        sum-=map.get(chars[i]);
                    }else {
                        sum+=map.get(chars[i]);
                    }
                }
                else if (map.get(chars[i])>=map.get(chars[i+1])){
                    sum+=map.get(chars[i]);
                }else {
                    sum-=map.get(chars[i]);
                    if (sum<0){
                        sum=sum*(-1);
                    }
                }
            }

        }
        return sum;
    }


    public static  int romanToInt1(String s) {
        int sum = 0;
        int preNum = getValue(s.charAt(0));
        for(int i = 1;i < s.length(); i ++) {
            int num = getValue(s.charAt(i));
            if(preNum < num) {
                sum -= preNum;
            } else {
                sum += preNum;
            }
            preNum = num;
        }
        sum += preNum;
        return sum;
    }

    private static int getValue(char ch) {
        switch(ch) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }



    public static String longestCommonPrefix(String[] strs) {
        if(strs.length==0)return "";
        //公共前缀比所有字符串都短，随便选一个先
        String s=strs[0];
        for (String string : strs) {
            while(!string.startsWith(s)){
                if(s.length()==0)return "";
                //公共前缀不匹配就让它变短！
                s=s.substring(0,s.length()-1);
            }
        }
        return s;
    }
}
