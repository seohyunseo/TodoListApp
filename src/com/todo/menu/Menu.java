package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println("\n"+"< How to use ToDoList App >");
        System.out.println("add : Add a new item");
        System.out.println("del : Delete an existing item");
        System.out.println("edit : Update an item");
        System.out.println("ls : List all items");
        System.out.println("ls_name_asc : sort the list by name");
        System.out.println("ls_name_desc : sort the list by name");
        System.out.println("ls_date : sort the list by date");
        System.out.println("exit : exit the program");
        
    }
    public static void prompt()
    {
    	System.out.print("\nCommand > ");
    }
}
