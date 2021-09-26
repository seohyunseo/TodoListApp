package com.todo.service;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.io.Reader;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title, cate, due, desc;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("\n"
				+ "[ Add a new item ]");
		System.out.print("title > ");
		
		title = sc.next();
		if (list.isDuplicate(title)) {
			System.out.printf("title can't be duplicate");
			return;
		}
		
		System.out.print("category > ");
		cate = sc.next();
		
		System.out.print("description > ");
		sc.nextLine();
		desc = sc.nextLine();
		
		System.out.print("due date > ");
		due = sc.next();
		
		TodoItem t = new TodoItem(title, cate, desc,due,null);
		list.addItem(t);
	}

	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		int choice = 0, i = 1;
		String yn;
		System.out.println("\n"
				+ "[ Delete an existing item ]");
		System.out.print("enter the number to delete > ");
		choice = sc.nextInt();
		for (TodoItem item : l.getList()) {
			if (choice == i) {
				System.out.print(i+". ");
				readItem(item);
				System.out.print("Do you want to delete an item above? (y/n) > ");
				yn = sc.next();
				if (yn.equals("y")) {
					l.deleteItem(item);
					System.out.println("Item deleted");
				} else {
					break;
				}
				break;
			}
			i++;
		}
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		int choice = 0, i = 1;
		
		System.out.println("\n"
				+ "[ Update an item ]");
		System.out.print("enter the number to update > ");
		choice = sc.nextInt();
//		if (!l.isDuplicate(title)) {
//			System.out.println("title doesn't exist");
//			return;
//		}
		
		for(TodoItem item : l.getList()) {
			if(choice == i) {
				System.out.print(i+". ");
				readItem(item);
				break;
			}
			i++;
		}

		System.out.print("new title > ");
		String new_title = sc.next().trim();
		if (l.isDuplicate(new_title)) {
			System.out.println("title can't be duplicate");
			return;
		}
		
		System.out.print("category > ");
		String new_cate = sc.next();
		
		System.out.print("new description > ");
		String new_description;
		sc.nextLine().trim();
		new_description = sc.nextLine().trim();
		
		System.out.print("new due date > ");
		String new_due = sc.next();
		for (TodoItem item : l.getList()) {
			if (choice == i) {
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_title, new_cate, new_description,new_due,null);
				l.addItem(t);
				System.out.println("item updated");
				break;
			}
		}
	}
	
	public static void readItem(TodoItem item) {
		System.out.println("[" + item.getCategory() + "]"+ " " +item.getTitle()+" - "+item.getDesc()+" - "+item.getDue_date()+" - "+item.getCurrent_date());
	}
	
	public static void listAll(TodoList l) {
		int count = 0, serial = 1;
		for(TodoItem myitem : l.getList())
			count++;
		System.out.println("\n"+"[ List all items, total "+count+" item ]");
		for (TodoItem item : l.getList()) {
			System.out.print(serial+". ");
			readItem(item);
			serial++;
		}
	}
	
	public static void listCategory(TodoList l) {
		HashSet<String> cate = new HashSet<String>();
		int count = 0;
		
		for(TodoItem item : l.getList()) {
			cate.add(item.getCategory());
		}
		Iterator iter = cate.iterator();	// Iterator »ç¿ë
		while(iter.hasNext()) {
			count++;
		    System.out.print(iter.next());
		    if(count == cate.size()) {
		    	System.out.println();
		    	break;
		    }
		    System.out.print(" / ");
		    
		}
		System.out.println("Total "+count+" category exists");
	}
	
	public static void searchItem(TodoList l, String keyword) {
		int count = 0, serial = 1;
		
		for(TodoItem item : l.getList()) {
			if(item.getTitle().contains(keyword) || item.getDesc().contains(keyword)) {
				count++;
				System.out.print(serial+". ");
				readItem(item);
			}
			serial++;
		}
		System.out.println("Found total " + count + " items");
	}
	
	public static void searchItemCate(TodoList l, String keyword) {
		int count = 0, serial = 1;
		
		for(TodoItem item : l.getList()) {
			if(item.getCategory().contains(keyword)) {
				count++;
				System.out.print(serial+". ");
				readItem(item);
			}
			serial++;
		}
		System.out.println("Found total " + count + " items");
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter(filename);
			for(TodoItem item : l.getList()) 
				w.write(item.toSaveString());
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadList(TodoList l, String filename) {
		try {
			Reader reader = new FileReader(filename);
			BufferedReader br = new BufferedReader(reader);
			String oneLine;
			int count = 0;
			while((oneLine = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(oneLine, "##");
				String cate = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due = st.nextToken();
				String current_date = st.nextToken();
				TodoItem t = new TodoItem(title, cate, desc, due, current_date);
				l.addItem(t);
				count++;
			}
			br.close();
			System.out.println("Program read "+count+" data");
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find the file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
