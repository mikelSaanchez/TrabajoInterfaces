package Controlador;

import java.time.LocalDate;

import Modelo.Tarea;
import Modelo.TareaDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	private TableColumn<Tarea, String> colPrioridad;

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

	@FXML
	private TextField campoPrioridad;

	@FXML
	private Label labelErrores;

	@FXML
	private Label labelPendientes;

	@FXML
	private Label labelSemana;

	@FXML
	private Label labelPorcentaje;

	@FXML
	private Label labelPrioridad;

	private TareaDAO TareaDao = new TareaDAO();

	@FXML
	public void initialize() {
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
		tablaRecordatorios.setItems(TareaDao.getListaTareas());
		actualizarEstadisticas();
		tablaRecordatorios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tarea>() {
			@Override
			public void changed(ObservableValue<? extends Tarea> observable, Tarea anterior, Tarea seleccionado) {
				if (seleccionado != null) {
					campoNombre.setText(seleccionado.getNombre());
					campoDescripcion.setText(seleccionado.getDescripcion());
					campoFecha.setText(seleccionado.getFecha());
					campoEstado.setText(seleccionado.getEstado());
					campoPrioridad.setText(seleccionado.getPrioridad());
					labelErrores.setText("");

				}
			}
		});
	}

	@FXML
	public void insertarTarea() {

		String nombre = campoNombre.getText();
		String descripcion = campoDescripcion.getText();
		String fecha = campoFecha.getText();
		String estado = campoEstado.getText();
		String prioridad = campoPrioridad.getText();

		if (!TareaDao.esFechaValida(fecha)) {
			mostrarError("Error. Usa el formato dd/MM/yyyy.", true);
			return;
		}
		if (!estado.equalsIgnoreCase("Pendiente") && !estado.equalsIgnoreCase("Completada")) {
			mostrarError("Error. Debe ser 'Pendiente' o 'Completada'.", true);
			return;
		}

		if (!prioridad.equalsIgnoreCase("Alta") && !prioridad.equalsIgnoreCase("Media") && !prioridad.equalsIgnoreCase("Baja")) {
			mostrarError("Error. Debe ser 'Alta' , 'Media' o 'Baja'.", true);
			return;
		}

		Tarea nuevoTarea = new Tarea(nombre, descripcion, fecha, estado, prioridad);
		TareaDao.insertarTarea(nuevoTarea);
		limpiarFormulario();
		actualizarEstadisticas();
		mostrarError("Tarea Insertada Correctamente", false);

	}

	@FXML
	public void borrarTarea() {

		Tarea seleccionado = tablaRecordatorios.getSelectionModel().getSelectedItem();
		if (seleccionado == null) {
			mostrarError("Error. Debe seleccionar una tarea.", true);
			return;
		}

		TareaDao.borrarTarea(seleccionado);
		mostrarError("Tarea borrada correctamente", false);
		actualizarEstadisticas();
	}

	@FXML
	public void modificarTarea() {
		Tarea seleccionado = tablaRecordatorios.getSelectionModel().getSelectedItem();

		if (seleccionado == null) {
			mostrarError("Error. Debe seleccionar una tarea.", true);
			return;
		}

		String nombre = campoNombre.getText();
		String descripcion = campoDescripcion.getText();
		String fecha = campoFecha.getText();
		String estado = campoEstado.getText();
		String prioridad = campoPrioridad.getText();

		if (!TareaDao.esFechaValida(fecha)) {
			mostrarError("Error. Usa el formato dd/MM/yyyy.", true);
			return;
		}

		if (!estado.equalsIgnoreCase("Pendiente") && !estado.equalsIgnoreCase("Completada")) {
			mostrarError("Error. Debe ser 'Pendiente' o 'Completada'.", true);
			return;
		}

		if (!prioridad.equalsIgnoreCase("Alta") && !prioridad.equalsIgnoreCase("Media") && !prioridad.equalsIgnoreCase("Baja")) {
			mostrarError("Error. Debe ser 'Alta' , 'Media' o 'Baja'.", true);
			return;
		}

		TareaDao.actualizarTarea(seleccionado, nombre, descripcion, fecha, estado, prioridad);
		tablaRecordatorios.refresh();

		labelErrores.setText("Tarea Modificada Correctamente");
		labelErrores.setStyle("-fx-text-fill: #8000ff;");
		limpiarFormulario();
		actualizarEstadisticas();
	}

	private void actualizarEstadisticas() {
		labelPendientes.setText(String.valueOf(TareaDao.tareasPendientes()));
		labelSemana.setText(String.valueOf(TareaDao.tareasSemana()));
		labelPorcentaje.setText(String.valueOf(TareaDao.porcentajeTareas() + "%"));
		labelPrioridad.setText(String.valueOf(TareaDao.tareasPendientesAltas()));
	}

	private void limpiarFormulario() {
		campoNombre.clear();
		campoDescripcion.clear();
		campoFecha.clear();
		campoEstado.clear();
		campoPrioridad.clear();
	}

	private void mostrarError(String mensaje, boolean esError) {
		labelErrores.setText(mensaje);
		if (esError) {
			labelErrores.setStyle("-fx-text-fill: red;");
		} else {
			labelErrores.setStyle("-fx-text-fill: green;");
		}
	}
}
