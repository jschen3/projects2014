package services;

import java.io.IOException;
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

@Path("/updatetask")
public class UpdateTask {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateTask(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int tId,@FormParam("name") String tName, @FormParam("priority") int tPriority, 
			@FormParam("description") String tDescription,@FormParam("date") String tDate, @FormParam("startTime") String tStartTime, @FormParam("progress") int tProgress,
			@FormParam("section") int tSection){
			AuthenticationService authServ = new AuthenticationService();
			if (!(authServ.checkCredentials(username,password))){
				return Response.status(401).build();
			}
			else{
				NewTaskService ntService = new NewTaskService();
				ArrayList<Task> currTaskList= ntService.parseCurrentTaskList(username);
				ArrayList<Task> futureTaskList = ntService.parseFutureTaskList(username);
				int taskNumber = getTaskById(tId, currTaskList);
				if (taskNumber!=-1){
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
					Date nDate;
					Date nStartTime;
					try {
						nDate = sdf.parse(tDate);
						nStartTime = sdf.parse(tStartTime);
						currTaskList.get(taskNumber).updateTask(tName, tPriority, nDate, nStartTime, tSection, tDescription, tProgress);
						ntService.writeTaskList(username, currTaskList, "currentTasks");
						return Response.status(200).build();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
				else{
					taskNumber = getTaskById(tId,futureTaskList);
					if (taskNumber!=-1){
						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm");
						Date nDate;
						Date nStartTime;
						try {
							nDate = sdf.parse(tDate);
							nStartTime = sdf.parse(tStartTime);
							futureTaskList.get(taskNumber).updateTask(tName, tPriority, nDate, nStartTime, tSection, tDescription, tProgress);
							ntService.writeTaskList(username, futureTaskList, "futureTasks");
							return Response.status(200).build();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
				return Response.status(200).entity("no such task").build();
			}
	}
	private int getTaskById(int id,ArrayList<Task> taskList){
		
		for (int i=0;i<taskList.size();i++){
			if (taskList.get(i).getId()==id)
				return i;
		}
		return -1;
	}
}
