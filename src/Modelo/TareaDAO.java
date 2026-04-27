package Modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TareaDAO {

	private ObservableList<Tarea> listaTareas;

	public TareaDAO() {
		listaTareas = FXCollections.observableArrayList();
	}

	public ObservableList<Tarea> getListaTareas() {
		return listaTareas;
	}
}
