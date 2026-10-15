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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
//imports de java fx interfaz de modificacion administrador.
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MenuPrincipal {
    public Scene segundaEscena(Stage stage){
        stage.setTitle("Ventana de Gestion");
        
        Label Texto_Logo = new Label("PRISMA");
        Texto_Logo.setId("marca");
        
        ventana Login = new ventana(); //instanciamos la clase.
        ImageView logo = Login.crearLogo();

        Label texto1 = new Label("Gestion de Usuarios");
        texto1.setId("tituloGestion");
        Button btnAdmin = new Button("Usuarios y Permisos");
        Button btnProgramas = new Button("Programas");
        // Marca Usuarios como la vista seleccionada al abrir la pantalla.
        btnAdmin.getStyleClass().addAll("nav-button", "selected");
        btnProgramas.getStyleClass().add("nav-button");
        btnProgramas.setId("btnProgramas");
        btnAdmin.setId("btnAdmin");
        Button btnSesion = new Button("Cerrar Sesion");
        btnSesion.setId("btnSesion");
        
      //funcion del boton de cerrar sesion
        btnSesion.setOnAction(e -> {
            ventana menuLogin = new ventana();
            try {
                // Llama al método start() de la clase ventana
                menuLogin.start(stage); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });;
        
        // Menú lateral
        VBox panelGestion = new VBox(15);
        panelGestion.setAlignment(Pos.TOP_CENTER);
        panelGestion.getChildren().addAll(Texto_Logo, logo, btnAdmin, btnProgramas, btnSesion);
        panelGestion.setId("panelGestion");
        panelGestion.setMaxWidth(200);
        panelGestion.setMaxHeight(Double.MAX_VALUE);
        
        // Barrita de la sesion
        Label textoGestion = new Label("Sesion:");
        Label sesionActualText = new Label("Admin Principal"); // este texto deberia cambiar segun la sesion en la que entremos
        HBox sesionActual = new HBox(20);
        sesionActual.setAlignment(Pos.CENTER_LEFT);
        sesionActual.setMinHeight(40);
        sesionActual.setPrefHeight(40);
        sesionActual.setMaxHeight(40);
        sesionActual.getChildren().addAll(textoGestion, sesionActualText);
        sesionActual.setId("panelSesion");
        
        // Instanciamos una tabla para manejar objetos de la clase "Usuario"
        TableView<Usuario> tabla = new TableView<>();
        
        // columnas (Nombre, Email, Rol)
        TableColumn<Usuario, String> columnaNombre = new TableColumn<>("Nombre");
        
     // el PropertyValueFactory va a buscar el método 'getNombre()' en la clase Usuario, lo mismo con getEmail() y getRol().
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	
        TableColumn<Usuario, String> columnaEmail = new TableColumn<>("Email");
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<Usuario, String> columnaRol = new TableColumn<>("Rol");
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
      // Centrar elementos de las columnas en el css
        columnaNombre.setId("center");
        columnaEmail.setId("center");
        columnaRol.setId("center");
        
        //columna de botones de accion (Editar y Eliminar)
        TableColumn<Usuario, Void> columnaAcciones = new TableColumn<>("Acciones");
        columnaAcciones.setCellFactory(param -> new TableCell<>() {
        	private final Button btnEditar = new Button("Editar");
        	private final Button btnBorrar = new Button("Eliminar");
        	
        	//contenedor horizontal con separacion de 8 entre btnEditar y btnBorrar.
        	private final HBox contenedor = new HBox(8, btnEditar, btnBorrar);
        	{
                btnEditar.getStyleClass().add("action-button");
                btnBorrar.getStyleClass().add("delete-button");
        		contenedor.setAlignment(Pos.CENTER);

                // Al pulsar Editar, obtiene el usuario de esta fila y abre su menú.
                btnEditar.setOnAction(e -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    mostrarMenuEdicion(usuario);
                });
        	}
        	// Si la fila está vacía (no hay un Usuario asignado a esta fila), no mostramos nada (null)
            // Si la fila tiene datos (!empty), le inyectamos nuestro 'contenedor' (el HBox con los botones)
        	//
        	@Override
        	protected void updateItem(Void item, boolean empty) {
        		super.updateItem(item, empty);
        		setGraphic(empty ? null : contenedor);
        	}
        });
        tabla.getColumns().addAll(columnaNombre, columnaEmail, columnaRol, columnaAcciones);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Hace que las columnas ocupen todo el ancho

        //la observable list esta con el fin de actualizar los datos automaticamente en el caso de agregar o quitar.
        //estos son datos de relleno osea en verdad en la base de datos no van a estar pasa que todavia no hay CRUD ni ABM a ningun server.
        
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(
            new Usuario("Juan", "CorreoEjemplo@prisma.edu", "Docente"),
            new Usuario("María", "m.gonzalez@prisma.edu", "Administrador"),
            new Usuario("Lucas", "l.perez@prisma.edu", "Estudiante")
        );
        tabla.setItems(listaUsuarios);
        VBox.setVgrow(tabla, Priority.ALWAYS); // Hace que la tabla se estire verticalmente
        // ----------------------------

        // Panel inferior (Ahora incluye la tabla)
        VBox segundoPanel = new VBox(20);
        segundoPanel.getChildren().addAll(texto1, tabla); // <-- Añadimos la variable "tabla" aquí
        segundoPanel.setId("segundoPanel");
        segundoPanel.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(segundoPanel, Priority.ALWAYS); //que ocupe todo el vertical sobrante de la ventana

        // Se prepara la vista de programas para poder alternarla con la tabla de usuarios.
        VBox panelProgramas = new ProgramasView().crearPanel(false, ""); //no podra editar el admin
        panelProgramas.setId("segundoPanel");
        panelProgramas.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(panelProgramas, Priority.ALWAYS);
        
        // Columna derecha
        VBox columnaDerecha = new VBox(15);
        columnaDerecha.getChildren().addAll(sesionActual, segundoPanel);
        HBox.setHgrow(columnaDerecha, Priority.ALWAYS);

        // Cada botón reemplaza el contenido central y actualiza el estilo de la sección activa.
        btnAdmin.setOnAction(e -> {
            columnaDerecha.getChildren().set(1, segundoPanel);
            btnAdmin.getStyleClass().add("selected");
            btnProgramas.getStyleClass().remove("selected");
        });
        btnProgramas.setOnAction(e -> {
            columnaDerecha.getChildren().set(1, panelProgramas);
            btnProgramas.getStyleClass().add("selected");
            btnAdmin.getStyleClass().remove("selected");
        });
        
        // Contenedor principal con margen respecto a los bordes de la ventana
        HBox root = new HBox(15); 
        root.setAlignment(Pos.CENTER); 
        root.setPadding(new Insets(15)); 
        root.getChildren().addAll(panelGestion, columnaDerecha);
        
        VBox.setVgrow(panelGestion, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("estilosGestion.css").toExternalForm());
        
        return scene;
    }

    // Interfaz de modificación vista de administrador.
    private void mostrarMenuEdicion(Usuario usuario) {
        Dialog<Void> dialogo = new Dialog<>();
        dialogo.setTitle("Editar usuario");
        dialogo.setHeaderText(null);

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, guardar);

        TextField nombre = new TextField(usuario.getNombre());
        TextField email = new TextField(usuario.getEmail());

        PasswordField contrasena = new PasswordField();
        contrasena.setPromptText("Vacía = no cambiar");

        ComboBox<String> rol = new ComboBox<>();
        rol.getItems().addAll("Administrador", "Docente", "Estudiante");
        rol.setValue(usuario.getRol());

        VBox contenido = new VBox(12);
        contenido.getChildren().addAll(
            campoFormulario("Nombre", nombre),
            campoFormulario("Correo electrónico", email),
            campoFormulario("Contraseña (vacía = no cambiar)", contrasena),
            campoFormulario("Rol", rol)
        );

        dialogo.getDialogPane().setContent(contenido);
        dialogo.getDialogPane().setPrefWidth(550);
        dialogo.showAndWait();
    }

    // Coloca la etiqueta y el campo juntos para cada dato del formulario.
    private VBox campoFormulario(String texto, Control control) {
        Label etiqueta = new Label(texto);
        control.setMaxWidth(Double.MAX_VALUE);

        VBox campo = new VBox(5);
        campo.getChildren().addAll(etiqueta, control);
        return campo;
    }
}

/*
cd C:\Users\Warren\JavaVSCode\demo 
mvn javafx:run
*/