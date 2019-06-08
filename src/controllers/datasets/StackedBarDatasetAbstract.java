package controllers.datasets;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.I18n;
import controllers.ubulogs.GroupByAbstract;
import model.EnrolledUser;

public abstract class StackedBarDatasetAbstract<T> {

	static final Logger logger = LoggerFactory.getLogger(StackedBarDatasetAbstract.class);
	/**
	 * Nivel de opacidad para el color de fondo de las barras
	 */
	private static final float OPACITY_BAR = 0.4f;

	protected List<EnrolledUser> selectedUsers = new ArrayList<>();
	protected List<T> selecteds = new ArrayList<>();
	protected Map<T, int[]> colors;
	protected GroupByAbstract<?> groupBy;
	protected LocalDate start;
	protected LocalDate end;
	protected StringBuilder stringBuilder;

	private static String escapeJavaScriptText(String input) {
		return input.replaceAll("'", "\\\\'");
	}

	protected abstract String translate(T element);

	protected abstract Map<T, List<Double>> getMeans();

	protected abstract Map<EnrolledUser, Map<T, List<Long>>> getUserCounts();

	public String createData(List<EnrolledUser> selectedUsers,
			List<T> selecteds, GroupByAbstract<?> groupBy, LocalDate dateStart, LocalDate dateEnd) {

		// lo metemos en un nuevo arraylist para evitar que se actualice en tiempo real
		// los elementos
		this.selectedUsers = new ArrayList<>(selectedUsers);
		this.selecteds = new ArrayList<>(selecteds);
		this.groupBy = groupBy;
		this.start = dateStart;
		this.end = dateEnd;

		setRandomColors();

		stringBuilder = new StringBuilder();
		stringBuilder.append("{"); // llave de inicio del dataset
		setLabels();
		setDatasets();
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	public String getData() {
		return stringBuilder.toString();
	}

	private void setLabels() {
		List<String> rangeDates = groupBy.getRangeString(start, end);

		String stringLabels = joinWithQuotes(rangeDates);

		stringBuilder.append("labels:[" + stringLabels + "],");
	}

	private void setDatasets() {
		stringBuilder.append("datasets: [");
		setMeans();
		setUsersDatasets();
		stringBuilder.append("]");
	}

	/**
	 * 
	 * 
	 */
	private void setMeans() {

		Map<T, List<Double>> meanTs = getMeans();

		for (Entry<T, List<Double>> entry : meanTs.entrySet()) {
			T element = entry.getKey();
			List<Double> data = entry.getValue();

			int[] color = colors.get(element);

			stringBuilder.append("{");
			stringBuilder.append(
					"label:'" + escapeJavaScriptText(I18n.get("chart.mean") + " "
							+ escapeJavaScriptText(translate(element))) + "',");
			stringBuilder.append("type: 'line',");
			stringBuilder.append("borderWidth: 2,");
			stringBuilder.append("fill: false,");
			stringBuilder.append("borderColor: 'rgb(" + color[0] + ", " + color[1] + "," + color[2] + ")',");
			stringBuilder.append("data: [" + join(data) + "]");
			stringBuilder.append("},");
		}

	}

	private void setUsersDatasets() {
		Map<EnrolledUser, Map<T, List<Long>>> userTDataset = getUserCounts();

		
		for (EnrolledUser user : selectedUsers) {
			Map<T, List<Long>> elementDataset = userTDataset.get(user);
			for (T element : selecteds) {
				int[] c = colors.get(element);
				List<Long> data = elementDataset.get(element);

				stringBuilder.append("{");
				stringBuilder.append("label:'" + escapeJavaScriptText(translate(element)) + "',");
				stringBuilder.append("stack: '" + escapeJavaScriptText(user.toString()) + "',");
				stringBuilder.append("backgroundColor: 'rgba(" + c[0] + ", " + c[1] + "," + c[2] + "," + OPACITY_BAR + ")',");
				stringBuilder.append("data: [" + join(data) + "]");
				stringBuilder.append("},");
				
			}
		}
	
	}

	private String joinWithQuotes(List<String> list) {
		// https://stackoverflow.com/a/18229122
		return list.stream()
				.map(s -> "'" + s + "'")
				.collect(Collectors.joining(", "));
	}

	private <E> String join(List<E> datasets) {
		return datasets.stream()
				.map(E::toString)
				.collect(Collectors.joining(", "));
	}

	private void setRandomColors() {
		colors = new HashMap<>();

		for (int i = 0; i < selecteds.size(); i++) {

			// generamos un color a partir de HSB, el hue(H) es el color, saturacion (S), y
			// brillo(B)
			Color c = new Color(Color.HSBtoRGB(i / (float) selecteds.size(), 0.8f, 1.0f));
			int[] array = { c.getRed(), c.getGreen(), c.getBlue() };
			colors.put(selecteds.get(i), array);
		}

	}

}