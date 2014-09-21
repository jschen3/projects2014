package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import objects.Task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Path("/newtask")
public class NewTaskService {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newTask(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int tId,@FormParam("name") String tName, @FormParam("priority") int tPriority, 
			@FormParam("description") String tDescription, @FormParam("startTime") String tStartTime, @FormParam("progress") int tProgress,
			@FormParam("section") int tSection){
			AuthenticationService authServ = new AuthenticationService();
			if (!(authServ.checkCredentials(username,password))){
				return Response.status(401).build();
			}
			else{
				createNewTask(username, tId, tName, tPriority,tDescription, tStartTime, tProgress,tSection);
				return Response.status(201).build();
			}
		
	}

	
	public void writeTaskList(String username, ArrayList<Task> theTaskList, String fileName) throws IOException {
		File file = new File("/todolist/tasks/"+username+"/"+fileName+".json");
		file.delete();
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("[\n");
		for (Task aTask:theTaskList){
			aTask.writeTaskString(file);
			bw.write(",\n");
		}
		bw.write("]");
		bw.close();
	}

	public ArrayList<Task> parseAllTaskList(String username) {
		ArrayList<Task> taskList= new ArrayList<Task>();
		File jsonFile = new File("/todolist/tasks/"+username+"/allTasks.json");
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile)));
			Gson myGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonTaskArray =  jsonParser.parse(reader).getAsJsonArray();
			for (JsonElement jsonTask : jsonTaskArray ){
				 Task aTask = myGson.fromJson(jsonTask,Task.class);
				 taskList.add(aTask);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return taskList;
	}
	public ArrayList<Task> parseCurrentTaskList(String username){
		File jsonFile = new File("/todolist/tasks/"+username+"/currentTasks.json");
		ArrayList<Task> currentTaskList = new ArrayList<Task>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile)));
			Gson myGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonTaskArray =  jsonParser.parse(reader).getAsJsonArray();
			for (JsonElement jsonTask : jsonTaskArray ){
				 Task aTask = myGson.fromJson(jsonTask,Task.class);
				 currentTaskList.add(aTask);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentTaskList;
	}
	public ArrayList<Task> parsePreviousTaskList(String username){
		File jsonFile=new File("/todolist/tasks/"+username+"/previousTasks.json");
		ArrayList<Task> previousTaskList = new ArrayList<Task>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile)));
			Gson myGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonTaskArray =  jsonParser.parse(reader).getAsJsonArray();
			for (JsonElement jsonTask : jsonTaskArray ){
				 Task aTask = myGson.fromJson(jsonTask,Task.class);
				 previousTaskList.add(aTask);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return previousTaskList;
	}
	public ArrayList<Task> parseFutureTaskList(String username){
		File jsonFile=new File("/todolist/tasks/"+username+"/futureTasks.json");
		ArrayList<Task> futureTaskList = new ArrayList<Task>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile)));
			Gson myGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonTaskArray =  jsonParser.parse(reader).getAsJsonArray();
			for (JsonElement jsonTask : jsonTaskArray ){
				 Task aTask = myGson.fromJson(jsonTask,Task.class);
				 futureTaskList.add(aTask);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return futureTaskList;
	}
	
	private void createNewTask(String username, int tId, String tName,
			int tPriority, String tDescription, String sStartTime,
			int tProgress, int tSection) {
			ArrayList<Task> cTaskList = parseAllTaskList(username);
			ArrayList<Task> cCurrentTaskList = parseCurrentTaskList(username);
			ArrayList<Task>	cFutureTaskList =parseFutureTaskList(username);
			int i;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
			Date tStartTime;
			try {
				tStartTime = sdf.parse(sStartTime);
				Date tDate = new Date();
				for (i=0;i<cTaskList.size();i++){
					if (tId==cTaskList.get(i).getId())
						break;
				}
				if (cTaskList.get(i).getId()==tId){
					cTaskList.get(i).setRepeatedTimes(cTaskList.get(i).getRepeatedTimes()+1);
					if (tSection==3){
						Task newTask = new Task(tName, username, tId, tPriority, tDate, tStartTime,tSection, tDescription, tProgress);
						cFutureTaskList.add(newTask);
						cTaskList.add(newTask);
						try {
							writeTaskList(username, cTaskList,"allTasks");
							writeTaskList(username, cFutureTaskList, "futureTasks");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{
						Task newTask = new Task(tName, username, tId, tPriority, tDate, tStartTime,tSection, tDescription, tProgress);
						cCurrentTaskList.add(newTask);
						try {
							writeTaskList(username, cTaskList,"allTasks");
							writeTaskList(username, cFutureTaskList, "currentTasks");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					Task newTask = new Task(tName, username, tId, tPriority, tDate, tStartTime,tSection, tDescription, tProgress);
					newTask.setRepeatedTimes(1);
					newTask.addPreviousDates(new Date());
					Date nStartTime = (Date) tStartTime.clone();
					newTask.addPreviousStartTimes(nStartTime);
					if (tSection==3){
						cFutureTaskList.add(newTask);
						cTaskList.add(newTask);
						try {
							writeTaskList(username, cTaskList,"allTasks");
							writeTaskList(username, cFutureTaskList, "futureTasks");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{
						cTaskList.add(newTask);
						cCurrentTaskList.add(newTask);
						try {
							writeTaskList(username, cTaskList,"allTasks");
							writeTaskList(username, cFutureTaskList, "currentTasks");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}
}
