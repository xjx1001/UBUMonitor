package controllers.UBULogs.logCreator.logsTypes;

import java.util.List;

import model.Log;
/**
 * The user with id '' updated section number '' for the course with id ''
 * The user with id '' viewed the section number '3' of the course with id ''.

 * 
 * @author Yi Peng Ji
 *
 */
public class UserSectionCourse extends ReferencesLog{

	
	/**
	 * static Singleton instance.
	 */
	private static UserSectionCourse instance;

	/**
	 * Private constructor for singleton.
	 */
	private UserSectionCourse() {
	}

	/**
	 * Return a singleton instance of UserSectionCourse.
	 */
	public static UserSectionCourse getInstance() {
		if (instance == null) {
			instance = new UserSectionCourse();
		}
		return instance;
	}
	
	@Override
	public void setLogReferencesAttributes(Log log, List<Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	
}