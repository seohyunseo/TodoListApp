package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		boolean isList = false;
		boolean quit = false;
		TodoUtil.loadList(l, "todolist.txt");
		Menu.displaymenu();
		do {
			Menu.prompt();
			isList = false;
			String choice = sc.next();
			String keyword = "";
			if(choice.contains("find"))
				keyword = sc.next();
			
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name_asc":
				System.out.println("List sorted by name");
				l.sortByName();
				isList = true;
				break;

			case "ls_name_desc":
				System.out.println("List sorted by name in reverse order");
				l.sortByName();
				l.reverseList();
				isList = true;
				break;
				
			case "ls_date":
				System.out.println("List sorted by date");
				l.sortByDate();
				isList = true;
				break;

			case "exit":
				quit = true;
				break;
				
			case "help":
				Menu.displaymenu();
				break;
				
			case "find":
				TodoUtil.searchItem(l, keyword);
				break;
				
			case "find_cate":
				TodoUtil.searchItemCate(l, keyword);
				break;
			
			case "ls_date_desc":
				System.out.println("List sorted by date in reverse order");
				l.sortByDate();
				l.reverseList();
				isList = true;
				break;
			case "ls_cate":
				TodoUtil.listCategory(l);
				break;
			
			default:
				System.out.println("please enter one of the above mentioned command. (help : Display menu)");
				break;
			}
			
			if(isList) l.listAll();
		} while (!quit);
		TodoUtil.saveList(l, "todolist.txt");
	}
}
