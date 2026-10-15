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
        Button btnIA = crearBoton("Asistente de IA");
        Button btnCerrarSesion = new Button("Cerrar sesión");
        btnCerrarSesion.setId("btnSesion");

        VBox menuLateral = new VBox(
            15, marca, logo, btnProgramas, btnIA, btnCerrarSesion
        );
        menuLateral.setAlignment(Pos.TOP_CENTER);
        menuLateral.setId("panelGestion");
        menuLateral.setMaxWidth(200);

        Label sesion = new Label("Sesión: " + usuario.getNombre());
        sesion.setId("panelSesion");

        VBox vistaProgramas = new ProgramasView().crearPanel(true, usuario.getNombre()); //este true indica que permite mostrar editar y eliminar
        vistaProgramas.setId("segundoPanel");
        VBox vistaIA = crearVista("Asistente de IA",
            "Aquí irá el asistente de IA.");

        VBox contenido = new VBox(15, sesion, vistaProgramas);
        VBox.setVgrow(vistaProgramas, Priority.ALWAYS);
        HBox.setHgrow(contenido, Priority.ALWAYS);

        btnProgramas.setOnAction(e ->
            mostrar(contenido, vistaProgramas, btnProgramas, btnIA));

        btnIA.setOnAction(e ->
            mostrar(contenido, vistaIA, btnProgramas, btnIA));

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

    private void mostrar(VBox contenido, VBox vista, Button botonActivo,
                         Button... botonesNavegacion) {
        contenido.getChildren().set(1, vista);

        for (Button boton : botonesNavegacion) {
            boton.getStyleClass().remove("selected");
        }
        botonActivo.getStyleClass().add("selected");
    }
}