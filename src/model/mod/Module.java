package model.mod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Log;

public class Module implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * course module id
	 */
	private int id;

	/**
	 * activity url Optional.
	 * 
	 */
	private String url;

	/**
	 * activity module name
	 */
	private String name;

	/**
	 * instance id
	 */
	private int instance;

	/**
	 * Optional. activity description
	 */
	private String description;

	/**
	 * Optional. is the module visible
	 */
	private boolean visible;

	/**
	 * Optional. Is the module visible for the user?
	 */
	private boolean uservisible;
	/**
	 * Optional. Availability information.
	 */
	private String availabilityinfo;

	/**
	 * Optional. is the module visible on course page
	 */
	private boolean visibleoncoursepage;
	/**
	 * activity icon url
	 */
	private String modicon;

	/**
	 * activity module type
	 */
	private String modname;

	/**
	 * activity module plural name
	 */
	private String modplural;

	/**
	 * Optional. module availability settings
	 */
	private String availability;

	/**
	 * number of identation in the site
	 */
	private int indent;

	private List<Log> logs;

	public Module() {
		logs = new ArrayList<Log>();
	}

	
	public void addLog(Log log) {
		logs.add(log);
	}
	
	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isUservisible() {
		return uservisible;
	}

	public void setUservisible(boolean uservisible) {
		this.uservisible = uservisible;
	}

	public void setVisible(int uservisible) {
		this.uservisible = uservisible != 0;

	}

	public String getAvailabilityinfo() {
		return availabilityinfo;
	}

	public void setAvailabilityinfo(String availabilityinfo) {
		this.availabilityinfo = availabilityinfo;
	}

	public boolean isVisibleoncoursepage() {
		return visibleoncoursepage;
	}

	public void setVisibleoncoursepage(boolean visibleoncoursepage) {
		this.visibleoncoursepage = visibleoncoursepage;
	}

	public void setVisibleoncoursepage(int visibleoncoursepage) {
		this.visibleoncoursepage = visibleoncoursepage != 0;

	}

	public String getModicon() {
		return modicon;
	}

	public void setModicon(String modicon) {
		this.modicon = modicon;
	}

	public String getModname() {
		return modname;
	}

	public void setModname(String modname) {
		this.modname = modname;
	}

	public String getModplural() {
		return modplural;
	}

	public void setModplural(String modplural) {
		this.modplural = modplural;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Module [id=" + id + ", url=" + url + ", name=" + name + ", instance=" + instance + ", description="
				+ description + ", visible=" + visible + ", uservisible=" + uservisible + ", availabilityinfo="
				+ availabilityinfo + ", visibleoncoursepage=" + visibleoncoursepage + ", modicon=" + modicon
				+ ", modname=" + modname + ", modplural=" + modplural + ", availability=" + availability + ", indent="
				+ indent + "]";
	}

}