package com.example;

import java.util.function.Function;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class programasView {

    public VBox crearPanel() {
        // Controles para buscar programas por asignatura, grado y nivel.
        Label titulo = new Label("Programas curriculares");
        titulo.setId("tituloGestion");

        TextField asignatura = new TextField();
        asignatura.setPromptText("Ej: Matemática");

        ComboBox<String> grado = new ComboBox<>();
        grado.getItems().addAll("Todos", "1°", "2°", "3°");
        grado.setValue("Todos");

        ComboBox<String> nivel = new ComboBox<>();
        nivel.getItems().addAll("Todos", "EBI (Ciclo Básico)", "Bachillerato");
        nivel.setValue("Todos");

        HBox filtros = new HBox(10,
            campoFormulario("Asignatura", asignatura),
            campoFormulario("Grado", grado),
            campoFormulario("Nivel", nivel)
        );
        HBox.setHgrow(filtros.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(filtros.getChildren().get(1), Priority.ALWAYS);
        HBox.setHgrow(filtros.getChildren().get(2), Priority.ALWAYS);

        Button buscar = new Button("Buscar");
        buscar.setId("buscarPrograma");
        Button limpiar = new Button("Limpiar");
        limpiar.setId("limpiarPrograma");

        HBox barraBusqueda = new HBox(10, filtros, buscar, limpiar);
        barraBusqueda.setAlignment(Pos.BOTTOM_LEFT);
        HBox.setHgrow(filtros, Priority.ALWAYS);

        // Datos de ejemplo; FilteredList permite filtrar sin modificar la lista original.
        // Observable list con datos estaticos sin cargar de la base de datos
        ObservableList<Programa> programas = FXCollections.observableArrayList(
            new Programa("Matemática", "1°", "EBI (Ciclo Básico)", "Docente"),
            new Programa("---", "---", "---", "---"),
            new Programa("---", "---", "---", "---"),
            new Programa("---", "---", "---", "---"),
            new Programa("Informática", "1°", "Bachillerato", "Docente"),
            new Programa("Biología", "3°", "Bachillerato", "Docente")
        );
        FilteredList<Programa> programasFiltrados = new FilteredList<>(programas, programa -> true);

        TableView<Programa> tablaProgramas = new TableView<>(programasFiltrados);
        tablaProgramas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Las columnas muestran los datos del programa mediante sus accesores.
        tablaProgramas.getColumns().addAll(
            columnaPrograma("Asignatura", Programa::asignatura),
            columnaPrograma("Grado", Programa::grado),
            columnaPrograma("Nivel", Programa::nivel),
            columnaPrograma("Docente", Programa::docente)
        );

        TableColumn<Programa, Void> columnaAcciones = new TableColumn<>("Acciones");
        columnaAcciones.setCellFactory(columna -> new TableCell<>() {
            private final Button ver = new Button("Ver");

            {
                ver.setStyle("-fx-background-color: #008793; -fx-text-fill: white; -fx-background-radius:3px;");
                // Muestra los datos de la fila seleccionada en una ventana informativa.
                ver.setOnAction(e -> {
                    Programa programa = getTableView().getItems().get(getIndex());
                    Alert detalle = new Alert(Alert.AlertType.INFORMATION);
                    detalle.setTitle("Programa curricular");
                    detalle.setHeaderText(programa.asignatura());
                    detalle.setContentText(
                        "Grado: " + programa.grado() + "\n" +
                        "Nivel: " + programa.nivel() + "\n" +
                        "Docente: " + programa.docente()
                    );
                    detalle.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : ver);
            }
        });
        columnaAcciones.setId("center");
        tablaProgramas.getColumns().add(columnaAcciones);
        VBox.setVgrow(tablaProgramas, Priority.ALWAYS);

        // Aplica conjuntamente los criterios elegidos y actualiza el total de resultados.
        Label cantidad = new Label();
        Runnable aplicarFiltros = () -> {
            String texto = asignatura.getText().trim().toLowerCase();
            String gradoSeleccionado = grado.getValue();
            String nivelSeleccionado = nivel.getValue();
            programasFiltrados.setPredicate(programa ->
                (texto.isEmpty() || programa.asignatura().toLowerCase().contains(texto)) &&
                ("Todos".equals(gradoSeleccionado) || programa.grado().equals(gradoSeleccionado)) &&
                ("Todos".equals(nivelSeleccionado) || programa.nivel().equals(nivelSeleccionado))
            );
            cantidad.setText(programasFiltrados.size() + " programa(s) encontrados.");
        };
        buscar.setOnAction(e -> aplicarFiltros.run());
        // Restablece los campos y vuelve a mostrar todos los programas.
        limpiar.setOnAction(e -> {
            asignatura.clear();
            grado.setValue("Todos");
            nivel.setValue("Todos");
            aplicarFiltros.run();
        });
        cantidad.setText(programas.size() + " programa(s) encontrados.");

        VBox panel = new VBox(16, titulo, barraBusqueda, tablaProgramas, cantidad);
        panel.setPadding(new Insets(25));
        return panel;
    }

    private TableColumn<Programa, String> columnaPrograma(
        String titulo, Function<Programa, String> valor
    ) {
        TableColumn<Programa, String> columna = new TableColumn<>(titulo);
        // Convierte el valor obtenido del programa en el observable que usa la tabla.
        columna.setCellValueFactory(celda ->
            new ReadOnlyStringWrapper(valor.apply(celda.getValue()))
        );
        columna.setId("center");
        return columna;
    }

    private VBox campoFormulario(String texto, Control control) {
        Label etiqueta = new Label(texto);
        control.setMaxWidth(Double.MAX_VALUE);

        VBox campo = new VBox(5);
        campo.getChildren().addAll(etiqueta, control);
        return campo;
    }

    // Agrupa los datos mostrados en cada fila de la tabla de programas.
    private record Programa(String asignatura, String grado, String nivel, String docente) {
    }
}
