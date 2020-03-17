package es.ubu.lsi.ubumonitor.clustering.algorithm;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.controlsfx.control.PropertySheet;

import es.ubu.lsi.ubumonitor.clustering.data.ClusteringParameter;
import es.ubu.lsi.ubumonitor.clustering.util.SimplePropertySheetItem;

public class AlgorithmParameters {

	private Map<ClusteringParameter, PropertySheet.Item> parameters;

	public AlgorithmParameters() {
		parameters = new LinkedHashMap<>();
	}

	public void addParameter(ClusteringParameter parameter, Object value) {
		SimplePropertySheetItem c = new SimplePropertySheetItem(parameter.getName(), value, parameter.getDescription());
		parameters.put(parameter, c);
	}

	public Collection<PropertySheet.Item> getPropertyItems() {
		return parameters.values();
	}

	@SuppressWarnings("unchecked")
	public <V> V getValue(ClusteringParameter parameter) {
		return (V) parameters.get(parameter).getValue();
	}

	@Override
	public String toString() {
		return parameters.toString();
	}
}
