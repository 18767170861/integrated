package org.wmc.integrated.hash;

public class HashTable {

    private EmpLinkedList[] empLinkedListArr;
    private int size;

    public HashTable(int size) {
        super();
        this.size = size;
        empLinkedListArr = new EmpLinkedList[size];

        for (int i = 0; i < size; i++) {
            empLinkedListArr[i] = new EmpLinkedList();
        }
    }

    // 添加雇员
    public void add(Emp emp) {
        // 根据员工的id得到改员工应该添加到哪条链表
        int empLinkedListNo = hashFun(emp.getId());
        // 将emp添加到对应的链表中
        empLinkedListArr[empLinkedListNo].add(emp);
    }

    public void list() {
        for (int i = 0; i < empLinkedListArr.length; i++) {
            empLinkedListArr[i].list(i);
        }
    }

    public void findEmpById(int id) {
        int empLinkedListNo = hashFun(id);
        Emp emp = empLinkedListArr[empLinkedListNo].findEmpByid(id);
        if (emp != null) {
            System.out.println("在第" + (empLinkedListNo + 1) + "条链表中找到id = " + id + "雇员");
        } else {
            System.out.println("在哈希表中没有找到");
        }
    }

    public void del(int id) {
        int empLinkedListNo = hashFun(id);
        boolean flag = empLinkedListArr[empLinkedListNo].del(id);
        if (flag == true) {
            System.out.println("在第" + (empLinkedListNo + 1) + "条链表中删除了id = " + id + "雇员");
        } else {
            System.out.println("在哈希表中没有找到");
        }

    }

    public int hashFun(int id) {
        return id % size;
    }
}
