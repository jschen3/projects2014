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

@Path("/getcurrtasklist")
public class GetCurrTaskList {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrTaskList(@FormParam("username") String username, @FormParam("password") String password){
		AuthenticationService authServ = new AuthenticationService();
		if (!(authServ.checkCredentials(username,password))){
			return Response.status(401).build();
		}
		else{
			NewTaskService ntService = new NewTaskService();
			ArrayList<Task> cCurrTaskList = ntService.parseCurrentTaskList(username);
			String taskListString="[\n";
			for (Task aTask: cCurrTaskList){
				taskListString+=aTask.createTaskString();
			}
			return Response.status(200).entity(taskListString).build();
		}
	}
}
