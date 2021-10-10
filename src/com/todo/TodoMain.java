package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		l.importData("todolist.txt");
		boolean quit = false;
//		TodoUtil.loadList(l, "todolist.txt");
		Menu.displaymenu();
		do {
			Menu.prompt();
			String choice = sc.next();
			
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
				TodoUtil.listAll(l, null, 0,0);
				break;

			case "ls_name":
				System.out.println("List sorted by name");
				TodoUtil.listAll(l, "title", 1,0);
				break;

			case "ls_name_desc":
				System.out.println("List sorted by name in reverse order");
				TodoUtil.listAll(l, "title", 0,0);
				break;
				
			case "ls_date":
				System.out.println("List sorted by date");
				TodoUtil.listAll(l, "due_date", 1,0);
				break;
				
			case "ls_date_desc":
				System.out.println("List sorted by date in reverse order");
				TodoUtil.listAll(l, "due_date", 0,0);
				break;
				
			case "exit":
				quit = true;
				break;
				
			case "help":
				Menu.displaymenu();
				break;
				
			case "find":
				String keyword = sc.nextLine().trim();
				TodoUtil.searchItem(l, keyword);
				break;
				
			case "find_cate":
				String cate = sc.nextLine().trim();
				TodoUtil.searchItemCate(l, cate);
				break;
			
			case "ls_cate":
				TodoUtil.listCategory(l);
				break;
				
			case "comp":
				int com = sc.nextInt();
				TodoUtil.completeItem(l, com);
				break;
				
			case "ls_comp":
				TodoUtil.listAll(l, null, 0,1);
				break;
			
			default:
				System.out.println("please enter one of the above mentioned command. (help : Display menu)");
				break;
			}
		} while (!quit);
		TodoUtil.saveList(l, "todolist.txt");
	}
}
