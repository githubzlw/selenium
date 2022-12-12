package com.zhz.selenium.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.zhz.selenium.pojo.JsonRootBean;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class test1 {

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    public void a(){
        CountDownLatch taskLatch = new CountDownLatch(5);
        for (int i = 0; i <5 ; i++) {
            taskExecutor.execute(()->{
                try {
                    Thread.sleep(60000);
                    longestCommonPrefix(new String[]{"qweasd","qwea","qwerasdc","qweasde"});
                    taskLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("aaaaaaaa");
    }

    public static void main(String[] args) {
//        String str[] = new String[]{"qweasd","qwea","qwerasdc","qweasde"};
//        longestCommonPrefix(str);

//        isValid("{(}){})({}");
//        isValid("{[()]()[]}");

//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
//        String yesTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -1));
//        String lasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -7));
//        System.out.println(date);
//        System.out.println(startTime);
//        System.out.println(yesTime);
//        System.out.println(lasWeekTime);
//        String a= "US$79.99";
//        String s = a.replace("\n", ".").replace("$", "").replace("US", "");
//        System.out.println(s);
//        test1("12456789123456789123456789");
//        ReverseList();
//        int max = Math.max(2, Math.min(8 - 1, 4));
//        System.out.println(max);
//        int i = Runtime.getRuntime().availableProcessors();
//        System.out.println(i);
//        ConcurrentHashMap
//        ThreadLocal<Map<String,Object>> objectThreadLocal = new ThreadLocal<>();
//        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
//        HashMap<String, Object> objectObjectHashMap1 = new HashMap<>();
//        objectObjectHashMap.put("12",12);
//        objectObjectHashMap1.put("123",123);
//        objectThreadLocal.set(objectObjectHashMap);
//        objectThreadLocal.set(objectObjectHashMap1);
//        System.out.println(objectThreadLocal.get().get("123"));
//        objectThreadLocal.remove();
//        System.out.println(objectThreadLocal.get());

//        long startTime = new Date().getTime();
//        long endTime = new Date().getTime()+3600000;
//        System.out.println(startTime);
//        System.out.println(endTime);
//        for (int i = 8894; i <8914 ; i++) {
//            System.out.print(i+",");
//        }

//        Queue<String> queue = new LinkedList<>();
//        for (int i = 0; i < 4; i++) {
//            setCache(queue,""+i);
//        }

//        soft();
//        int count = 4;
//        String results = null;
//        while (count>0){
//            try {
//                results = "哈哈哈";
//            } catch (Exception e) {
//                System.out.println("getAccessToken失败重试");
//                e.printStackTrace();
//                count--;
//                continue;
//            }
//            if (!StringUtil.isEmpty(results)){
//                break;
//            }
//        }
//        if (StringUtil.isEmpty(results)){
//            System.out.println("getAccessToken结果为空");
//        }
//        System.out.println(results);
//        doubleColor();
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
//        System.out.println(date);
        System.out.println((Double.valueOf(StringUtil.toString("1.0")) - 2));
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

    public static boolean isValid(String s) {
        int n = s.length();
        if (n % 2 == 1) {
            return false;
        }

        Map<Character, Character> pairs = new HashMap<Character, Character>() {{
            put(')', '(');
            put(']', '[');
            put('}', '{');
        }};
        Deque<Character> stack = new LinkedList<Character>();
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (pairs.containsKey(ch)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(ch)) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }

    public static void test1(String s){
        char[] arr_s=s.toCharArray();
        int n=s.length();
        int sum=0;
        String[] arr=new String[]{"ling","yi","er","san","si","wu","liu","qi","ba","jiu","shi"};
        for (int i = 0; i < n; i++) {
            sum+=arr_s[i]-'0';
        }
        String num=new String(Integer.toString(sum));
        char[] arr_n=num.toCharArray();
        for (int i = 0; i < num.length(); i++) {
            if(i!=0) System.out.print(" ");
            System.out.print(arr[arr_n[i]-'0']);
        }
    }

    public static int test2(int[] numbers){
        HashMap<Object, Object> map = new HashMap<>();
        for (int a:numbers) {
            if (map.containsKey(a)){
                return a;
            }
            map.put(a,a);
        }
        return -1;
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode ReverseList(ListNode head) {
        ListNode listNode = new ListNode(head.val);
        while (head!=null){
            ListNode next = head.next;
            head.next=listNode;
            listNode=head;
            head=next;
        }
        return listNode;
    }

    public static void setCache(Queue<String> queue,String cache){
        int size = queue.size();
        if (size >= 3){
            queue.poll();
        }
        queue.add(cache);
        System.out.println("缓存中的值如下：");
        for (String q: queue) {
            System.out.println(q);
        }
    }

    static void soft(){
        // 缓存
        Map<Integer, SoftRefedStudent> map = new HashMap<Integer, SoftRefedStudent>();
        ReferenceQueue<Student> queue = new ReferenceQueue<Student>();
        int i = 0;
        while (i < 10000000) {
            Student p = new Student();
            map.put(i, new SoftRefedStudent(i, p, queue));
            //p = null;
            SoftRefedStudent pollref = (SoftRefedStudent) queue.poll();
            if (pollref != null) {//找出被软引用回收的对象
                //以key为标志，从map中移除
                System.out.println("回收"+pollref.key);
                map.remove(pollref.key);

                System.out.println(i+"新一轮================================================");
                Iterator<Map.Entry<Integer, SoftRefedStudent>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry = iterator.next();
                    if ((int)entry.getKey() == pollref.key){
                        System.out.println("见鬼了");
                    }
                }
                System.out.println(i+"新一轮================================================");


            }
            i++;
        }
        System.out.println("done");
    }

    public static void doubleColor(){
        Random random = new Random();
        HashSet<Integer> set = new HashSet<>();
        while(set.size()<6){
            set.add(random.nextInt(33)+1);
        }
        System.out.println("红"+set);
        System.out.println("蓝"+(random.nextInt(16)+1));
    }
}
class Student{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class SoftRefedStudent extends SoftReference<Student> {
    public int key;
    // 第3个参数叫做ReferenceQueue，是用来存储封装的待回收Reference对象的
    /**
     * 当Student对象被回收后，SoftRefedStudent对象会被加入到queue中。
     */
    public SoftRefedStudent(int key, Student referent, ReferenceQueue<Student> q) {
        super(referent, q);
        this.key = key;
    }
}