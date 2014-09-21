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

@Path("/getprevioustasklist")
public class GetPreviousTaskList {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPreviousTaskList(@FormParam("username") String username, @FormParam("password") String password){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> cPreviousTaskList = ntService.parsePreviousTaskList(username);
			String taskListString="[\n";
			for(Task aTask: cPreviousTaskList){
				taskListString+=aTask.createTaskString();
			}
			taskListString+="]";
			return Response.status(200).entity(taskListString).build();
		}
	}

}
