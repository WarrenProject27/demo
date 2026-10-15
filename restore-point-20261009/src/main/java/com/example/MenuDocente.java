package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuDocente {

    public Scene crearEscena(Stage stage, Usuario usuario) {
        Label marca = new Label("PRISMA");
        marca.setId("marca");

        ImageView logo = new ventana().crearLogo();

        Button btnProgramas = crearBoton("Programas");
        Button btnEstudiantes = crearBoton("Estudiantes");
        Button btnIA = crearBoton("Asistente de IA");
        Button btnCerrarSesion = new Button("Cerrar sesión");
        btnCerrarSesion.setId("btnSesion");

        VBox menuLateral = new VBox(
            15, marca, logo, btnProgramas, btnEstudiantes, btnIA, btnCerrarSesion
        );
        menuLateral.setAlignment(Pos.TOP_CENTER);
        menuLateral.setId("panelGestion");
        menuLateral.setMaxWidth(200);

        Label sesion = new Label("Sesión: " + usuario.getNombre());
        sesion.setId("panelSesion");

        VBox vistaProgramas = new programasView().crearPanel();
        vistaProgramas.setId("segundoPanel");
        VBox vistaEstudiantes = crearVista("Estudiantes",
            "Aquí irá la sección de estudiantes.");
        VBox vistaIA = crearVista("Asistente de IA",
            "Aquí irá el asistente de IA.");

        VBox contenido = new VBox(15, sesion, vistaProgramas);
        VBox.setVgrow(vistaProgramas, Priority.ALWAYS);
        HBox.setHgrow(contenido, Priority.ALWAYS);

        btnProgramas.setOnAction(e ->
            mostrar(contenido, vistaProgramas, btnProgramas,
                    btnEstudiantes, btnIA));

        btnEstudiantes.setOnAction(e ->
            mostrar(contenido, vistaEstudiantes, btnProgramas,
                    btnEstudiantes, btnIA));

        btnIA.setOnAction(e ->
            mostrar(contenido, vistaIA, btnProgramas,
                    btnEstudiantes, btnIA));

        btnCerrarSesion.setOnAction(e -> new ventana().start(stage));

        btnProgramas.getStyleClass().add("selected");

        HBox raiz = new HBox(15, menuLateral, contenido);
        raiz.setPadding(new Insets(15));

        Scene escena = new Scene(raiz, 800, 600);
        escena.getStylesheets().add(
            getClass().getResource("estilosGestion.css").toExternalForm()
        );
        return escena;
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);
        boton.getStyleClass().add("nav-button");
        return boton;
    }

    private VBox crearVista(String titulo, String descripcion) {
        Label encabezado = new Label(titulo);
        Label detalle = new Label(descripcion);

        VBox vista = new VBox(15, encabezado, detalle);
        vista.setId("segundoPanel");
        VBox.setVgrow(vista, Priority.ALWAYS);
        return vista;
    }

    private void mostrar(VBox contenido, VBox vista,
                         Button btnProgramas, Button btnEstudiantes, Button btnIA) {
        contenido.getChildren().set(1, vista);

        btnProgramas.getStyleClass().remove("selected");
        btnEstudiantes.getStyleClass().remove("selected");
        btnIA.getStyleClass().remove("selected");
        obtenerBotonSeleccionado(vista, btnProgramas, btnEstudiantes, btnIA)
            .getStyleClass().add("selected");
    }

    private Button obtenerBotonSeleccionado(VBox vista,
                                             Button btnProgramas,
                                             Button btnEstudiantes,
                                             Button btnIA) {
        if ("Estudiantes".equals(((Label) vista.getChildren().get(0)).getText())) {
            return btnEstudiantes;
        }
        if ("Asistente de IA".equals(((Label) vista.getChildren().get(0)).getText())) {
            return btnIA;
        }
        return btnProgramas;
    }
}