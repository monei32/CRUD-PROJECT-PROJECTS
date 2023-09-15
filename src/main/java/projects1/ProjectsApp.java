package projects1;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	// @formatter:off
	private List<String> operations = List.of(
	     "1) Add a Project",
	     "2) List Project",
	     "3) Select a Project", 
	     "4) Update project details",
	     "5) Delete Project"
	); 
	// @ formatter:on
	
	/**
	 * 
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections(); 
	
		 

	}

	private void processUserSelections() {
	    boolean done = false; 
	    
	    while(!done) {
	      try {
	    	int selection = getUserSelection(); 
	    	
	    	switch(selection) {
	    	case -1:
	    	 done = exitMenu();
	    	 break; 
	    	 
	    	case 1:
	    	  createProject();
	    	  break; 
	    	  
	    	case 2: 
	    		listProject(); 
	    		break; 
	    	
	    	case 3: 
	    		selectProject();
	    		break;
	    	
	    	case 4: 
	    		updateProject(); 
	    		break; 
	    		
	    	case 5: 
	    		deleteProject(); 
	    		break; 
	    	  
	    	  default: 
	    	   System.out.println("\n " + selection + " is not a valid selection. Try again.");
	    	   break; 
	    	}
	    }
	      catch(Exception e) {
	    	  System.out.println("\n Error: " + e + " Try again.");
	      }
	     }
	   }

	   private void deleteProject() {
	   Integer projectId = getIntInput ("\nPlease Enter Project ID for Project you want to delete");
	   
	   projectService.deleteProject(projectId);
	   System.out.println("Project + projectId + was delete sucessfully");
	   
	   if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
		   curProject = null;
	   } 
		
	}

	private void updateProject() {
		  if(Objects.isNull(curProject)) {
			  System.out.println("\nPlease select a project");
			  return; 
		  }
		   String projectName = getStringInput("Enter the project name ["
				   + curProject.getProjectName()+ "]");
		   BigDecimal estimatedHours = 
				   getDecimalInput("Enter the estimated hours ["+ curProject.getEstimatedHours() + "]"); 
		   BigDecimal actualHours = 
				   getDecimalInput("Enter actual hours ["+ curProject.getActualHours() + "]");
		   Integer difficulty = 
				   getIntInput("Enter the project difficulty ["+ curProject.getDifficulty()+ "]"); 
		   String notes = 
				   getStringInput("Enter project notes [" + curProject.getNotes() + "]");
				  
		  Project project = new Project(); 
		  project.setProjectId(curProject.getProjectId());
		  project.setProjectName(Objects.isNull(projectName)
				  ? curProject.getProjectName() : projectName);
		  
		  project.setEstimatedHours(Objects.isNull(estimatedHours
				  )
				  ? curProject.getEstimatedHours() : estimatedHours);
		  
		  project.setActualHours(Objects.isNull(actualHours)
				  ? curProject.getActualHours() : actualHours);
		  
		  project.setDifficulty(Objects.isNull(difficulty)
				  ? curProject.getDifficulty() : difficulty);
		  
		  project.setNotes(Objects.isNull(notes)
				  ? curProject.getNotes() : notes);
		  
		  
		   
		  projectService.modifyProjectDetails(project);
		  
		  curProject = projectService.fetchProjectById(curProject.getProjectId());
		  
	   }

	   
	private void selectProject() {
		listProject(); 
		Integer projectId = getIntInput("Enter a project ID to select a project"); 
		
		curProject = null; 
		
		curProject = projectService.fetchProjectById(projectId); 
		
		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

	private void listProject() {
		List<Project> projects = projectService.fetchAllProjects(); 
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out
				.println(" " + project.getProjectId()+ ": " + project.getProjectName())); 
		
	}

	private void createProject() {
	 String projectName = getStringInput("Enter the Project Name"); 
	 BigDecimal estimatedHours = getDecimalInput ("Enter the estimated hours"); 
	 BigDecimal actualHours = getDecimalInput ("Enter the actual hours"); 
	 Integer difficulty = getIntInput ("Enter the project difficulty (1-5)"); 
	 String notes = getStringInput("Enter the project notes"); 
	 
	 Project project = new Project(); 
	 
	 project.setProjectName(projectName);
	 project.setEstimatedHours(estimatedHours);
	 project.setActualHours(actualHours);
	 project.setDifficulty(difficulty);
	 project.setNotes(notes);
	 
	 Project dbProject = projectService.addProject(project); 
	 System.out.println("You have successfully created project: " + dbProject);
	}
	
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt); 
		
		if(Objects.isNull(input)) {
		 return null;
		}
	try {
		return new BigDecimal(input).setScale(2); 
	}
    catch(NumberFormatException e) {
    	throw new DbException(input + "is not a valid decimal number.");
    }
	}
	
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu Selection"); 
		
		return Objects.isNull(input) ? -1 : input; 
	    
	}
	

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt); 
		
		if(Objects.isNull(input)) {
			return null; 
		}
		
		try {
			return Integer.valueOf(input);
		}
		catch (NumberFormatException e) {
		  throw new DbException(input + " is not a valid number"); 
		}
	}
	

	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String line = scanner.nextLine();
		
		return line.isBlank() ? null : line.trim(); 
	}

	private void printOperations() {
	   System.out.println("\n These are the avaialbe selections. Press the Enter key to quit:"); 
	   
	   operations.forEach(line -> System.out.println(" " + line)); 
		
	}

}
