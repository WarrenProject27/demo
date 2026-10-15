package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField; // <-- Importación corregida a JavaFX
import javafx.scene.layout.VBox; // <-- Cambiado a VBox para mejor diseño
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ventana extends Application {

	 @Override
	    public void start(Stage primaryStage) {
	        // Creacion de elementos
		 ImageView logo = crearLogo();
		 

		 	Label texto1 = new Label("PRISMA");
		 	texto1.setId("titulo");
		 	Label texto2 = new Label("Software Educativo");
		 	texto2.setId("subtitulo");
		 	
	        TextField textobox1 = new TextField();
	        TextField textobox2 = new TextField();
	        textobox1.setText("Nombre de usuario...");
	        textobox2.setText("Contraseña del usuario...");
	        
	        
	        // Agregar elementos al contenedor principal
	        VBox root = new VBox(12); //margin entre los elementos puestos en el VBox 
	        TextField nombre_usr = new TextField();
	        TextField contrasena_usr = new PasswordField();
	        Button btn = new Button("Iniciar Sesión");
	        
			Label mensaje = new Label();
	        //Funciones de los botones
	        btn.setOnAction(e -> {
				Usuario usuario = validarUsuario(
					nombre_usr.getText(),contrasena_usr.getText()
				);
				if (usuario == null) {
					mensaje.setText("Usuario o contraseña incorrectos");
				} else if ("Administrador".equalsIgnoreCase(usuario.getRol())){
					primaryStage.setScene(
						new MenuPrincipal().segundaEscena(primaryStage)
					);
				} else if ("Docente".equalsIgnoreCase(usuario.getRol())) {
					primaryStage.setScene(
						new MenuDocente().crearEscena(primaryStage, usuario)
					);
				} else if ("Estudiante".equalsIgnoreCase(usuario.getRol())) {
					primaryStage.setScene(
						new MenuEstudiante().crearEscena(primaryStage, usuario)
					);
				}

	        });
	        //Propiedades / tamaños de los elementos
	        nombre_usr.setPromptText("Usuario");
	        nombre_usr.setPrefWidth(250);
	        nombre_usr.setPrefHeight(40);
	        nombre_usr.setMaxWidth(250); //maximo de estirado
	        
	        contrasena_usr.setPromptText("Contraseña");
	        contrasena_usr.setPrefWidth(250);
	        contrasena_usr.setPrefHeight(40);
	        contrasena_usr.setMaxWidth(250);
	        
	        btn.setPrefWidth(250);
	        btn.setPrefHeight(30);
	        btn.setMaxWidth(250);
	        
	        VBox panelLogin = new VBox(12);
	        panelLogin.setAlignment(Pos.CENTER);
	        panelLogin.setMaxSize(350, 400);
	        panelLogin.getChildren().addAll(logo, texto1, texto2, nombre_usr, contrasena_usr, btn, mensaje); //metemos los elementos a la tarjeta login
	        panelLogin.setId("panel-login");
	        
	        //agregar a la raiz la tarjeta del login
	        root.getChildren().addAll(panelLogin);
	        root.setAlignment(Pos.CENTER);
	        
	        
	        // Crear la escena con ancho y alto
	        Scene scene = new Scene(root, 800, 600);
	        scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
	        
	        // Configurar la ventana (Stage)
	        primaryStage.setTitle("Ventana de login");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }
		    //validacion de usuario
			private Usuario validarUsuario(String nombre, String Contrasena) {
				if (nombre.trim().equalsIgnoreCase("docente") && Contrasena.equals("docente123")) {
					return new Usuario("Juan", "Juan@prisma.edu", "Docente");
				}

				if (nombre.trim().equalsIgnoreCase("admin") && Contrasena.equals("admin123")) {
					return new Usuario("AdminNombre", "correo@prisma.edu", "Administrador");
				} 
				if (nombre.trim().equalsIgnoreCase("estudiante") && Contrasena.equals("estudiante123")) {
					return new Usuario("Lucas", "l.perez@prisma.edu", "Estudiante");
				}

			return null;
			}

	 	public ImageView crearLogo() { //funcion que crea el logo (para ahorrar codigo al llamarlo a otras partes del codigo).
	        Image Logo = new Image(getClass().getResourceAsStream("logo.png"));
	        ImageView logo = new ImageView(Logo);
	        logo.setFitWidth(100);
	        logo.setPreserveRatio(true);
	        return logo;
	    }
	    public static void main(String[] args) {
	        launch(args);
	    }
	}