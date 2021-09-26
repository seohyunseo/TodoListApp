package com.todo.dao;

import java.util.*;

import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;
import com.todo.service.TodoUtil;

public class TodoList {
	private List<TodoItem> list;

	public TodoList() {
		this.list = new ArrayList<TodoItem>();
	}

	public void addItem(TodoItem t) {
		list.add(t);
	}

	public void deleteItem(TodoItem t) {
		list.remove(t);
	}

	void editItem(TodoItem t, TodoItem updated) {
		int index = list.indexOf(t);
		list.remove(index);
		list.add(updated);
	}

	public ArrayList<TodoItem> getList() {
		return new ArrayList<TodoItem>(list);
	}

	public void sortByName() {
		
		Collections.sort(list, new TodoSortByName());

	}

	public void listAll() {
		int count = 0, serial = 1;
		for(TodoItem myitem : list)
			count++;
		System.out.println("\n"+"[ List all items, total "+count+" item ]");
		for (TodoItem item : list) {
			System.out.print(serial+". ");
			TodoUtil.readItem(item);
			serial++;
		}
	}
	
	public void reverseList() {
		
		Collections.reverse(list);
	}

	public void sortByDate() {
		
		Collections.sort(list, new TodoSortByDate());
	}
	

	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public Boolean isDuplicate(String title) {
		for (TodoItem item : list) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}
}
