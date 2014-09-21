package services;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import objects.Task;

@Path("/updateprogress")
public class UpdateProgressService {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateProgress(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int taskId, @FormParam("progress") int progress){
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
				currTaskList.get(taskNumber).setProgress(progress);
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
					futureTaskList.get(taskNumber).setProgress(progress);
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
