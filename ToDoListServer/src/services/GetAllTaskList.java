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

@Path("/getalltasklist")
public class GetAllTaskList {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTaskList(@FormParam("username") String username, @FormParam("password") String password){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> cAllTaskList = ntService.parseAllTaskList(username);
			String taskListString="[\n";
			for (Task aTask: cAllTaskList){
				taskListString+=aTask.createTaskString();
			}
			taskListString +="]";
			return Response.status(200).entity(taskListString).build();
		}
	}

}
