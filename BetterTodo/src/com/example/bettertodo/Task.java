/*
 * Task Class to model the tasks in the todo list.
 */

package com.example.bettertodo;

import java.util.Collections;
import java.util.Comparator;

public class Task implements Comparable{
	
	private String name;
	private Priority priority;
	
	public Task(String name) {
		this.name = name;
		this.priority = Priority.INVALID;
	}
	
	public Task(String name, Priority priority) {
		this.name = name;
		this.priority = priority;
	}
	public Task(String name, String priority) {
		this.name = name;
		if(priority.equalsIgnoreCase("High")){
			this.priority = Priority.HIGH;
		} else if(priority.equalsIgnoreCase("Medium")) {
			this.priority = Priority.MEDIUM;
		} else if(priority.equalsIgnoreCase("Low")) {
			this.priority = Priority.LOW;
		} else {
			throw new IllegalArgumentException("ERROR!!!!! Invalid priority");
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPriority(Priority p) {
		this.priority = p;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Priority getPriority(){
		return this.priority;
	}
	
	public String getPriorityAsString(){
		return ""+this.priority;
	}

	
	public String toString(){
		return this.name+" "+this.priority;
	}
	/*
	 * Implementing compare to method to sort the tasks
	 * based on priority. High priorty tasks come first
	 */
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		Task otherTask = (Task)another;
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		if(this.priority == Priority.HIGH) {
			return BEFORE;
		}
		if(this.priority == Priority.LOW) {
			return AFTER;
		}
		if(this.priority == Priority.MEDIUM && otherTask.priority == Priority.HIGH) {
			return AFTER;
		} else {
			return BEFORE;
		}
	}
	
	@Override
	public boolean equals(Object another) {
		if(another instanceof Task){
		    Task toCompare = (Task) another;
		    return this.name.equals(toCompare.name);
		  }
		  return false;
	}
	@Override
	public int hashCode() {
	    return this.name.hashCode();
	}
	
}
