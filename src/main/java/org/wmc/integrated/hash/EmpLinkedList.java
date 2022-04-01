package org.wmc.integrated.hash;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EmpLinkedList {

    // 头指针，执行第一个Emp，因此我们这个链表的head，是直接指向第一个Emp
    private Emp head;

    // id是自增长的
    public void add(Emp emp) {
        // 如果是添加一个雇员
        if (Objects.isNull(head)) {
            head = emp;
            return;
        }
        // 如果不是第一个
        Emp curEmp = head;
        while (true) {
            if (Objects.isNull(curEmp.getNext())) {
                break;
            }
            curEmp = curEmp.getNext();
        }
        curEmp.setNext(emp);
    }

    public void list(int no) {
        if (Objects.isNull(head)) {
            System.out.println("第" + (no + 1) + "条链表为空！");
            return;
        }
        System.out.println("第" + (no + 1) + "条链表信息为：");
        Emp curEmp = head;
        while (true) {
            System.out.printf("=> id=%d name=%s\t", curEmp.getId(), curEmp.getName());
            if (Objects.isNull(curEmp.getNext())) {
                break;
            }
            curEmp = curEmp.getNext();
        }
        System.out.println();
    }

    // 根据id查找雇员
    public Emp findEmpByid(int id) {
        if (head == null) {
            System.out.println("链表为空");
            return null;
        }
        Emp curEmp = head;
        while (true) {
            if (curEmp.getId() == id) {
                break;
            }
            if (Objects.isNull(curEmp.getNext())) {
                System.out.println("遍历完了，没有找到！");
                curEmp = null;
                break;
            }
            curEmp = curEmp.getNext();
        }
        return curEmp;
    }

    // 根据id进行删除
    public boolean del(int id) {
        if (head == null) {
            System.out.println("当前链表为空！");
            return false;
        }
        if (head.getId() == id) {
            if (Objects.isNull(head.getNext())) {
                head = null;
                return true;
            }
            head = head.getNext();
            return true;
        }
        Emp curEmp = head;
        while (true) {
            System.out.println("curEmp:" + curEmp);
            if (Objects.isNull(curEmp.getNext())) {
                System.out.println("没有找改雇员！");
                return false;
            }
            // 找到了该雇员
            if (curEmp.getNext().getId() == id) {
                curEmp.setNext(curEmp.getNext().getNext());
                return true;
            }
            curEmp = curEmp.getNext();
        }
    }
}
