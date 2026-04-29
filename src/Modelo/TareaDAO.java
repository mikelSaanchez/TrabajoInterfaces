package Modelo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TareaDAO {

	private ObservableList<Tarea> listaTareas;
	private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public TareaDAO() {
		ArrayList<Tarea> tarea = cargarfichero();
		listaTareas = FXCollections.observableArrayList(tarea);
	}

	public ObservableList<Tarea> getListaTareas() {
		return listaTareas;
	}

	private void guardar() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fw = new FileWriter("tareas.json");
			gson.toJson(listaTareas, fw);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<Tarea> cargarfichero() {

		ArrayList<Tarea> tareas = new ArrayList<>();

		File f = new File("tareas.json");

		if (!f.exists()) {
			return tareas;
		}

		Gson gson = new Gson();
		Type tipo = new TypeToken<ArrayList<Tarea>>() {
		}.getType();
		FileReader fr;
		try {

			fr = new FileReader(f);
			tareas = gson.fromJson(fr, tipo);
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tareas;
	}

	public void insertarTarea(Tarea Tarea) {
		listaTareas.add(Tarea);
		guardar();
	}

	public void borrarTarea(Tarea Tarea) {
		listaTareas.remove(Tarea);
		guardar();
	}

	public void actualizarTarea(Tarea Tarea, String nombre, String descripcion, String fecha, String estado) {

		Tarea.setNombre(nombre);
		Tarea.setDescripcion(descripcion);
		Tarea.setFecha(fecha);
		Tarea.setEstado(estado);
		guardar();
	}
	public int tareasPendientes() {
		int contador = 0;
		for (Tarea t : listaTareas) {
			if (t.getEstado().equalsIgnoreCase("Pendiente")) {
				contador++;
			}
		}
		return contador;
	}
	
	public boolean esFechaValida(String fecha) {
		if (fecha == null || fecha.isBlank())
			return false;
		try {
			LocalDate.parse(fecha, formatoFecha);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
