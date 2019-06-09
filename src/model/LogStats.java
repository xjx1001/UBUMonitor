package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import controllers.ubulogs.GroupByAbstract;
import controllers.ubulogs.GroupByDay;
import controllers.ubulogs.GroupByDayOfWeek;
import controllers.ubulogs.GroupByHour;
import controllers.ubulogs.GroupByYear;
import controllers.ubulogs.GroupByYearMonth;
import controllers.ubulogs.GroupByYearQuarter;
import controllers.ubulogs.GroupByYearWeek;

/**
 * Clase contenedora que crea las instancias de las distintas agrupaciónes por
 * fechas.
 * 
 * @author Yi Peng Ji
 *
 */
public class LogStats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<GroupByAbstract<?>> stastistics;

	public LogStats(List<LogLine> logLines, Set<EnrolledUser> users) {
		stastistics = new ArrayList<>();

		stastistics.add(new GroupByDay(logLines));
		stastistics.add(new GroupByHour(logLines));
		stastistics.add(new GroupByDayOfWeek(logLines));
		stastistics.add(new GroupByYearWeek(logLines));
		stastistics.add(new GroupByYearMonth(logLines));
		stastistics.add(new GroupByYearQuarter(logLines));
		stastistics.add(new GroupByYear(logLines));

	}

	public List<GroupByAbstract<?>> getList() {
		return stastistics;
	}

}
