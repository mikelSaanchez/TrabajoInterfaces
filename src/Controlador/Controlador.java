package Controlador;

import java.time.LocalDate;

import Modelo.Tarea;
import Modelo.TareaDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controlador {

	@FXML
	private TableColumn<Tarea, String> colNombre;

	@FXML
	private TableColumn<Tarea, String> colDescripcion;

	@FXML
	private TableColumn<Tarea, LocalDate> colFecha;

	@FXML
	private TableColumn<Tarea, String> colEstado;

	@FXML
	private TableView<Tarea> tablaRecordatorios;

	@FXML
	private TextField campoNombre;

	@FXML
	private TextField campoDescripcion;

	@FXML
	private TextField campoFecha;

	@FXML
	private TextField campoEstado;

	private TareaDAO TareaDao = new TareaDAO();

	@FXML
	public void initialize() {
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tablaRecordatorios.setItems(TareaDao.getListaTareas());

		tablaRecordatorios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tarea>() {
			@Override
			public void changed(ObservableValue<? extends Tarea> observable, Tarea anterior, Tarea seleccionado) {
				if (seleccionado != null) {
					campoNombre.setText(seleccionado.getNombre());
					campoDescripcion.setText(seleccionado.getDescripcion());
					campoFecha.setText(seleccionado.getFecha());
					campoEstado.setText(seleccionado.getEstado());

				}
			}
		});
	}
}
