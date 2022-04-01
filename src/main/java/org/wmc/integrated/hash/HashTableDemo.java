package org.wmc.integrated.hash;

import java.util.Scanner;

public class HashTableDemo {

    public static void main(String[] args) {

        HashTable hashTable = new HashTable(7);

        String key = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("add:添加雇员");
            System.out.println("list:查看雇员");
            System.out.println("find:查找雇员");
            System.out.println("del:删除雇员");
            System.out.println("exit:退出");

            key = scanner.next();
            switch (key) {
                case "add":
                    System.out.println("请输入id：");
                    int id = scanner.nextInt();
                    System.out.println("请输入名字:");
                    String name = scanner.next();
                    Emp emp = new Emp(id, name);
                    hashTable.add(emp);
                    break;
                case "list":
                    hashTable.list();
                    break;
                case "find":
                    System.out.println("请输入id:");
                    int id2 = scanner.nextInt();
                    hashTable.findEmpById(id2);
                    break;
                case "del":
                    System.out.println("请输入id:");
                    int id3 = scanner.nextInt();
                    hashTable.del(id3);
                    break;
                case "exit":
                    System.exit(10);
                default:
                    break;
            }
        }
    }
}