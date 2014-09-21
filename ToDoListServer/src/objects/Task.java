package objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Task {
	//visible properties
	final String rootFilePath = "/todolist/tasks";
	String name;
	String user;
	int priority;
	Date date; //date up to miliseconds specificity 
						//getDate() gets day of month
						//getMonth() gets month
						//getYear() gets year
	Date startTime;
	Date endTime;
	int section; //1 through 3       1 means previous , 2 means current, 3 means future
	String description;
	int progress;
	
	
	//invisible properties
	int id;
	int repeatedTimes;
	ArrayList<Date> previousDates;
	ArrayList<Date> previousStartTimes;
	ArrayList<Date> previousEndTimes;
	public Task(){};
	public Task(String nName,String nUser, int nPriority, Date nDate, Date nStartTime,
			Date nEndTime, int nSection, String nDescription, int nProgress, int nId, int nRepeatedTimes,
			ArrayList<Date> nPreviousDates, ArrayList<Date> nPreviousStartTimes, ArrayList<Date> nPreviousEndTimes,
			ArrayList<Integer> nSimilarTasks){
		name = nName;
		user = nUser;
		priority = nPriority;
		date = nDate;
		startTime= nStartTime;
		endTime = nEndTime;
		section = nSection;
		description =nDescription;
		progress = nProgress;
		id= nId;
		repeatedTimes = nRepeatedTimes;
		previousDates = new ArrayList<Date>(nPreviousDates);
		previousStartTimes = new ArrayList<Date>(nPreviousStartTimes);
		previousEndTimes = new ArrayList<Date>(nPreviousEndTimes);
	}
	public Task(String nName,String nUser,int nId, int nPriority, Date nDate, Date nStartTime,
			 int nSection, String nDescription, int nProgress){
		name=nName;
		user = nUser;
		id = nId;
		priority = nPriority;
		date = nDate;
		startTime = nStartTime;
		section = nSection;
		description = nDescription;
		progress = nProgress;
	}
	public void updateTask(String nName, int nPriority, Date nDate, Date nStartTime, int nSection, String nDescription,
			int nProgress){
		name=nName;
		priority=nPriority;
		date=nDate;
		startTime = nStartTime;
		section = nSection;
		description=nDescription;
		progress = nProgress;
	}
	public void addPreviousDates(Date nDate){
		previousDates.add(nDate);
	}
	public void addPreviousStartTimes(Date nStartTime){
		previousStartTimes.add(nStartTime);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRepeatedTimes() {
		return repeatedTimes;
	}
	public void setRepeatedTimes(int repeatedTimes) {
		this.repeatedTimes = repeatedTimes;
	}
	public ArrayList<Date> getPreviousDates() {
		return previousDates;
	}
	public void setPreviousDates(ArrayList<Date> previousDates) {
		this.previousDates = previousDates;
	}
	public ArrayList<Date> getPreviousStartTimes() {
		return previousStartTimes;
	}
	public void setPreviousStartTimes(ArrayList<Date> previousStartTimes) {
		this.previousStartTimes = previousStartTimes;
	}
	public ArrayList<Date> getPreviousEndTimes() {
		return previousEndTimes;
	}
	public void setPreviousEndTimes(ArrayList<Date> previousEndTimes) {
		this.previousEndTimes = previousEndTimes;
	}
	public String createTaskString(){
		
		StringBuilder taskString = new StringBuilder();
		taskString.append("{").append("\"id\" : \""+ id +"\"," )
		.append("\"user\" : \""+ user +"\",").append("\"name\" : \""+ name.toString() +"\",")
		.append("\"priority\" : \""+ priority +"\",").append("\"section\" : \""+ section +"\",")
		.append("\"description\" : \""+ description.toString() +"\",").append("\"progress\" : \""+ progress +"\",")
		.append("\"repeatedTimes\" : \""+ repeatedTimes +"\",");
		
		taskString.append("\"previousDates\" : \"" +"{");
		for (int i=0; i<previousDates.size();i++){
			taskString.append("\""+previousDates.get(i).toString()+"\",");
		}
		taskString.append("}");
		
		taskString.append("\"previousStartTimes\" : \"" +"{");
		for (int i=0; i<previousStartTimes.size();i++){
			taskString.append("\""+previousStartTimes.get(i).toString()+"\",");
		}
		taskString.append("}");
		
		taskString.append("\"previousEndTimes\" : \"" +"{");
		for (int i=0; i<previousEndTimes.size();i++){
			taskString.append("\""+previousEndTimes.get(i).toString()+"\",");
		}
		taskString.append("}");
		
		taskString.append("}\n");
		
		return taskString.toString();
	}
	
	public void writeTaskString(File file) throws IOException{

		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("[ \n");
		bw.write(createTaskString());
		bw.write("]");
		bw.close();
	}
}
