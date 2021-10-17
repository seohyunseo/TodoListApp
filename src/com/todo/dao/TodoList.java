package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;
import com.todo.service.TodoUtil;

public class TodoList {
	private List<TodoItem> list;
	Connection conn;
	
	public TodoList() {
		this.list = new ArrayList<TodoItem>();
		this.conn = DbConnect.getConnection();
	}
	
	public int getCateId(TodoItem t) {
		int cate_id = 0;
		PreparedStatement pstmt;
		String sql = "SELECT * FROM category WHERE name=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getCategory());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
				cate_id = rs.getInt("id");
			pstmt.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return cate_id;
	}
	public int addItem(TodoItem t) {
//		int cate_id = 1;
		hasCategory(t.getCategory());
		int cate_id = getCateId(t);
		PreparedStatement pstmt;
		
		String sql = "insert into list (title, description, category, current_date, due_date, priority, routine)" + " values (?,?,?,?,?,?,?);";
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setInt(3, cate_id);
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getPriority());
			pstmt.setString(7, t.getRoutine());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		list.add(t);
		return count;
	}

	public int deleteItem(int index) {
//		list.remove(t);
		String sql = "DELETE FROM list WHERE id=?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
		
	}

	public int updateItem(TodoItem t) {
		hasCategory(t.getCategory());
		String sql = "update list set title=?, description=?, category=?, current_date=?, due_date=?, priority=?, routine=?" + " where id = ?;";
		PreparedStatement pstmt;
		int  count = 0;
		int cate_id = getCateId(t);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setInt(3, cate_id);
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getPriority());
			pstmt.setString(7, t.getRoutine());
			pstmt.setInt(8, t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public ArrayList<String> getCategories() {
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT name FROM category;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String cate = rs.getString("name");
				list.add(cate);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListRoutine(String day) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
				String sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id WHERE routine = ?;";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, day);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					int id = rs.getInt("id");
					String title = rs.getString("title");
					String desc = rs.getString("description");
					String category = rs.getString("name");
					String current_date = rs.getString("current_date");
					String due_date = rs.getString("due_date");
					int comp = rs.getInt("is_completed");
					int priority = rs.getInt("priority");
					String routine = rs.getString("routine");
					TodoItem t = new TodoItem(title, category,desc,  due_date, current_date, comp, priority, routine);
					t.setId(id);
					t.setCurrent_date(current_date);
					list.add(t);
				}
				pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		String sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id WHERE name = ?;";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				String category = rs.getString("name");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				int priority = rs.getInt("priority");
				String routine = rs.getString("routine");
				TodoItem t = new TodoItem(title, category,desc,  due_date, current_date, comp, priority, routine);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id ORDER BY "+orderby;
			if(ordering == 0)
				sql += " desc";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				String category = rs.getString("name");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				int priority = rs.getInt("priority");
				String routine = rs.getString("routine");
				TodoItem t = new TodoItem(title, category,desc,  due_date, current_date, comp, priority, routine);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword, int is_comp) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		String sql = "";
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			if(keyword == null) {
				sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id;";
				rs = stmt.executeQuery(sql);
				if(is_comp == 1) {
					sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id WHERE is_completed="+is_comp;
					rs = stmt.executeQuery(sql);
				}
			} else {
				keyword = "%"+keyword+"%";
				sql = "SELECT * FROM list INNER JOIN category ON list.category = category.id WHERE title like ? or description like ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				rs = pstmt.executeQuery();
			}
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				String category = rs.getString("name");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				int priority = rs.getInt("priority");
				String routine = rs.getString("routine");
				TodoItem t = new TodoItem(title, category,desc,  due_date, current_date, comp, priority, routine);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;		
	}

	public void sortByName() {
		
		Collections.sort(list, new TodoSortByName());

	}
	public int getCount(int comp) {
		Statement stmt;
		String sql = "";
		int count = 0;
		try {
			stmt = conn.createStatement();
			if(comp == 0)
				sql = "SELECT count(id) FROM list;";
			else
				sql = "SELECT count(id) FROM list WHERE is_completed="+comp;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	

	public int completeItem(int comp) {
		int count = 0;
		String sql = "update list set is_completed=?" + " where id = ?;";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, comp);
			count = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	public Boolean isDuplicate(String title) {
		PreparedStatement pstmt;
		int cnt = 0;
		String sql = "SELECT COUNT(*) cnt FROM list WHERE title=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			cnt = rs.getInt("cnt");
			if(cnt > 0)
				return true;
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void hasCategory(String cate) {
		PreparedStatement pstmt;
		int cnt = 0;
		String sql = "SELECT COUNT(*) cnt FROM category WHERE name=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cate);
			ResultSet rs = pstmt.executeQuery();
			cnt = rs.getInt("cnt");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addCategory(cate, cnt);
		
	}
	public void addCategory(String cate, int cnt) {
		PreparedStatement pstmt;
		if(cnt == 0) {
			String sql = "insert into category (name)"+" values (?);";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cate);
				int count = pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "insert into list (title, description, category, current_date, due_date)" + " values (?,?,?,?,?);";
			
			int records = 0;
			try {
				while((line = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line, "##");
					String category = st.nextToken();
					String title = st.nextToken();
					String desc = st.nextToken();
					String due_date = st.nextToken();
					String current_date = st.nextToken();
					
					
					try {
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1,  title);
						pstmt.setString(2,  desc);
						pstmt.setString(3,  category);
						pstmt.setString(4,  current_date);
						pstmt.setString(5,  due_date);
						int count = pstmt.executeUpdate();
						if(count > 0)
							records++;
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				System.out.println(records+" records read!!");
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
