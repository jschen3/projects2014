package services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import objects.Task;

@Path("/postponetask")
public class PostponeTaskService {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postPoneTask(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int taskId, @FormParam("startTime") String startTime){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> currTaskList= ntService.parseCurrentTaskList(username);
			ArrayList<Task> futureTaskList = ntService.parseFutureTaskList(username);
			int taskNumber = getTaskById(taskId, currTaskList);
			if (taskNumber!=-1){
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
				try {
					currTaskList.get(taskNumber).setStartTime(sdf.parse(startTime));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
					ntService.writeTaskList(username, currTaskList, "currentTasks");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return Response.status(200).build();
			}
			else{
				taskNumber = getTaskById(taskId,futureTaskList);
				if (taskNumber!=-1){
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
					try {
						futureTaskList.get(taskNumber).setStartTime(sdf.parse(startTime));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					try {
						ntService.writeTaskList(username, futureTaskList, "futureTasks");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	private int getTaskById(int id,ArrayList<Task> taskList){
	
		for (int i=0;i<taskList.size();i++){
			if (taskList.get(i).getId()==id)
				return i;
		}
		return -1;
	}
}
