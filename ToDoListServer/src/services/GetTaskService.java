package services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import objects.Task;

@Path("/gettask")
public class GetTaskService {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTask(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int taskId){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> currTaskList= ntService.parseCurrentTaskList(username);
			ArrayList<Task> previousTaskList = ntService.parsePreviousTaskList(username);
			ArrayList<Task> futureTaskList = ntService.parseFutureTaskList(username);
			int taskNumber = getTaskById(taskId,previousTaskList);
			if (taskNumber!=-1)
				return Response.status(200).entity(previousTaskList.get(taskNumber).createTaskString()).build();
			taskNumber = getTaskById(taskId,currTaskList);
			if (taskNumber!=-1)
				return Response.status(200).entity(currTaskList.get(taskNumber).createTaskString()).build();
			taskNumber = getTaskById(taskId, futureTaskList);
			if (taskNumber!=-1){
				return Response.status(200).entity(futureTaskList.get(taskNumber).createTaskString()).build();
			}
			return Response.status(204).build();
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
