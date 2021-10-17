package com.todo.service;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;
import java.io.Reader;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		String title, cate, due, desc, routine = null;
		int priority = 0, has_routine = 0;
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
		
		System.out.print("priority (1~3) > ");
		priority = sc.nextInt();
		
		System.out.print("has routine? (1/0) > ");
		has_routine = sc.nextInt();
		
		if(has_routine == 1) {
			System.out.print("routine (MON~SUN) > ");
			routine = sc.next();
		}

		TodoItem t = new TodoItem(title, cate, desc,due,null, 0, priority, routine);
		if(list.addItem(t)>0)
			System.out.println("item added in Database");
	}

	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		int index = 0;
		String yn;
		System.out.print("How many items to delete > ");
		int i = sc.nextInt();
		int[] del_list = new int[i]; 
		System.out.println("\n"
				+ "[ Delete an existing item ]");
		System.out.print("enter the numbers to delete > ");
		for(int j = 0; j < i; j++) {
			del_list[j] = sc.nextInt();
		}
		
		for(int j = 0; j < i; j++) {
			if(l.deleteItem(del_list[j]) > 0) 
				System.out.println((j+1)+"item deleted in database");
		}
		
	}
	
	public static void completeItem(TodoList l, int comp) {
		Scanner sc = new Scanner(System.in);
		
		int[] comp_list = new int [comp];
		System.out.println("you want to complete "+comp+" times");
		System.out.print("enter the numbers to complete > ");
		for(int i = 0; i < comp; i++) {
			comp_list[i] = sc.nextInt();
		}
		
		for(int i = 0; i < comp; i++) {
			if(l.completeItem(comp_list[i]) > 0) {
				System.out.println((i+1)+" item completed");
				for(TodoItem item : l.getList(null,0)) {
					if(item.getId() == comp)
						item.setIs_completed(1);
				}
			} else {
				System.out.println("no item completed");
			}
		}
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		int index = 0, i = 1, new_priority = 0, has_routine = 0;
		String new_routine = null;
		
		System.out.println("\n"
				+ "[ Update an item ]");
		System.out.print("enter the number to update > ");
		index = sc.nextInt();


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
		
		System.out.print("new priority (1~3) > ");
		new_priority = sc.nextInt();
		
		System.out.print("has routine? (1/0) > ");
		has_routine = sc.nextInt();
		
		if(has_routine == 1) {
			System.out.print("new routine (MON~SUN) > ");
			new_routine = sc.next();
		}

		
		TodoItem t = new TodoItem(new_title, new_cate, new_description,new_due,null,0,new_priority, new_routine );
		t.setId(index);
		if(l.updateItem(t) > 0)
			System.out.println("item updated in database");
	}
	
	public static void listAll(TodoList l, String orderby, int ordering, int comp) {
		if(comp == 0)
			System.out.println("\n"+"[ List all items, total "+l.getCount(comp)+" item ]");
		else
			System.out.println("total "+l.getCount(comp)+" items have been completed");
		if(orderby == null) {
			for(TodoItem item : l.getList(null,comp)) {
					System.out.println(item.toString());
			}
		} else {
			for(TodoItem item : l.getOrderedList(orderby, ordering)) {
					System.out.println(item.toString());
			}
		}
	}
	
	public static void listCategory(TodoList l) {
		int count = 0;
		for(String item : l.getCategories()) {
			System.out.print(item+" ");
			count++;
		}
		System.out.println("\n"+"Total "+count+" category exists");
	}
	
	public static void listRoutine(TodoList l) {
		String[] routine_day = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
		System.out.println("< Weekly Schedule >");
		for(int i = 0; i < 7; i++) {
			System.out.println("\n--------------------------------["+routine_day[i]+"]--------------------------------");
			int count = 0;
			for(TodoItem item : l.getListRoutine(routine_day[i])) {
				System.out.println(item.toString());
				count++;
			}
			System.out.println("\n"+"Total "+count+" category exists for "+routine_day[i]);
		}
		
	}
	
	public static void searchItem(TodoList l, String keyword) {
		int count = 0;
		
		for(TodoItem item : l.getList(keyword,0)) {
				System.out.println(item.toString());
				count++;
		}
		System.out.println("Found total " + count + " items");
	}
	
	public static void searchItemCate(TodoList l, String cate) {
		int count = 0;
		
		for(TodoItem item : l.getListCategory(cate)) {
			System.out.println(item.toString());
			count++;
		}
		System.out.println("Found total " + count + " items");
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter(filename);
			for(TodoItem item : l.getList(null,0)) 
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
				String priority = st.nextToken();
				String routine = st.nextToken();
				
				int p = Integer.parseInt(priority);
				TodoItem t = new TodoItem(title, cate, desc, due, current_date,0,p, routine);
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
