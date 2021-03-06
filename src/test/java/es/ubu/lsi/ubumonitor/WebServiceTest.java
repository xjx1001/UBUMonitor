package es.ubu.lsi.ubumonitor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import es.ubu.lsi.ubumonitor.controllers.AppInfo;
import es.ubu.lsi.ubumonitor.controllers.Controller;
import es.ubu.lsi.ubumonitor.controllers.ubugrades.CreatorGradeItems;
import es.ubu.lsi.ubumonitor.controllers.ubugrades.CreatorUBUGradesController;
import es.ubu.lsi.ubumonitor.controllers.ubulogs.DownloadLogController;
import es.ubu.lsi.ubumonitor.controllers.ubulogs.LogCreator;
import es.ubu.lsi.ubumonitor.model.Course;
import es.ubu.lsi.ubumonitor.model.CourseModule;
import es.ubu.lsi.ubumonitor.model.DataBase;
import es.ubu.lsi.ubumonitor.model.EnrolledUser;
import es.ubu.lsi.ubumonitor.model.GradeItem;
import es.ubu.lsi.ubumonitor.model.Logs;
import es.ubu.lsi.ubumonitor.model.MoodleUser;
import okhttp3.Response;

@TestMethodOrder(OrderAnnotation.class)
public class WebServiceTest {

	private static final String USERNAME = "teacher";
	private static final String PASSWORD = "moodle";
	private static final String HOST = "https://school.moodledemo.net";
	private static final int COURSE_ID = 62;

	private static Controller CONTROLLER;

	@Test
	@Order(1)
	public void loginTest() throws IOException {
		System.setProperty("logfile.name", "./log/" + AppInfo.APPLICATION_NAME_WITH_VERSION + "-test.log");
		CONTROLLER = Controller.getInstance();
		CONTROLLER.tryLogin(HOST, USERNAME, PASSWORD);
		CONTROLLER.setDataBase(new DataBase());
	}

	@Test
	@Order(2)
	public void createMoodleUserTest() throws IOException {
		MoodleUser moodleUser = CreatorUBUGradesController.createMoodleUser(CONTROLLER.getUsername());
		assertEquals(CONTROLLER.getUsername(), moodleUser.getUserName());
		CONTROLLER.setUser(moodleUser);
	}

	@Test
	@Order(3)
	public void getUserCourses() throws IOException {
		List<Course> courses = CreatorUBUGradesController.getUserCourses(CONTROLLER.getUser().getId());
		assertFalse(courses.isEmpty());
		assertTrue(courses.stream().anyMatch(c -> c.getId() == COURSE_ID), "User not enrolled in the course id: " + COURSE_ID);
		CONTROLLER.setActualCourse(CONTROLLER.getDataBase().getCourses().getById(COURSE_ID));
	}

	@Test
	@Order(4)
	public void getEnrolledUsers() throws IOException {
		List<EnrolledUser> enrolledUsers = CreatorUBUGradesController.createEnrolledUsers(COURSE_ID);
		assertFalse(enrolledUsers.isEmpty());
	}
	
	@Test
	@Order(5)
	public void getCourseModules() throws IOException {
		List<CourseModule> courseModules = CreatorUBUGradesController.createSectionsAndModules(COURSE_ID);
		assertFalse(courseModules.isEmpty());
	}
	
	@Test
	@Order(6)
	public void getGradeItems() throws IOException {
		CreatorGradeItems creatorGradeItems = new CreatorGradeItems(new Locale(CONTROLLER.getUser().getLang()));
		List<GradeItem> gradeItems = creatorGradeItems.createGradeItems(COURSE_ID);
		assertFalse(gradeItems.isEmpty());
	}
	@Test
	@Order(6)
	public void getActivityCompletion() throws IOException {
		CreatorUBUGradesController.createActivitiesCompletionStatus(COURSE_ID, CONTROLLER.getActualCourse().getEnrolledUsers());
	}
	
	@Test
	@Order(7)
	public void getLogs() throws IOException {
		DownloadLogController downloadLogController = LogCreator.download();

		Response response = downloadLogController.downloadLog(true);
	
		Logs logs = new Logs(downloadLogController.getServerTimeZone());
		LogCreator.parserResponse(logs, response, CONTROLLER.getActualCourse().getEnrolledUsers());
		CONTROLLER.getActualCourse().setLogs(logs);
	}

}
