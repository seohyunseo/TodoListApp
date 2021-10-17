package com.todo.dao;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TodoItem {
	private int id;
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private int is_completed;
    private int priority;
    private String routine;

    public TodoItem(String title, String category, String desc, String due_date, String date, int is_completed, int priority, String routine){
        this.title=title;
        this.desc=desc;
        if(date == null) {
        	SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        	this.current_date= f.format(new Date());
        }
        else
        	this.current_date = date;
        this.category=category;
        this.due_date=due_date;
        this.is_completed=is_completed;
        this.priority=priority;
        this.routine=routine;
    }


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}


	public String getRoutine() {
		return routine;
	}


	public void setRoutine(String routine) {
		this.routine = routine;
	}


	public int getIs_completed() {
		return is_completed;
	}


	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
    	this.current_date = current_date;
    }
    
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String toSaveString() {
		return category+ "##" + title + "##" + desc + "##" + due_date + "##" + current_date + "\n";
	}

	@Override
	public String toString() {
		if(this.is_completed == 0) {
			if(this.routine == null)
				return id+" [" + category + "]"+ " " +title+" - "+desc+" - "+due_date+" - "+current_date+" ["+priority+"]";
			else
				return id+" [" + category + "]"+ " " +title+" - "+desc+" - "+due_date+" - "+current_date+" ["+priority+"] routine: "+routine;
		} else {
			if(this.routine == null)
				return id+" [" + category + "]"+ " " +title+"[v]"+" - "+desc+" - "+due_date+" - "+current_date+" ["+priority+"]";
			else
				return id+" [" + category + "]"+ " " +title+"[v]"+" - "+desc+" - "+due_date+" - "+current_date+" ["+priority+"] routine: "+routine;
		}
	}
    
}
