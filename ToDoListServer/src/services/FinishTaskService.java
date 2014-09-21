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

@Path("/finishtask")
public class FinishTaskService {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response finishTask(@FormParam("username") String username, @FormParam("password") String password, @FormParam("id") int taskId){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> currTaskList= ntService.parseCurrentTaskList(username);
			ArrayList<Task> previousTaskList = ntService.parsePreviousTaskList(username);
			int taskNumber = getTaskById(taskId,currTaskList);
			previousTaskList.add(currTaskList.get(taskNumber));
			currTaskList.remove(taskNumber);
			try {
				ntService.writeTaskList(username, previousTaskList, "previousTasks");
				ntService.writeTaskList(username, currTaskList,"currentTasks");	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.status(200).build();
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
