
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.ubugrades.CreatorUBUGradesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.MoodleUser;

/**
 * Clase controlador de la ventana de Login
 * 
 * @author Claudia Martínez Herrero
 * @version 1.0
 *
 */
public class LoginController implements Initializable {

	static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private Controller controller = Controller.getInstance();

	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private TextField txtHost;
	@FXML
	private Button btnLogin;
	@FXML
	private ChoiceBox<Languages> languageSelector;

	// Host por defecto
	private static final String HOST = "https://ubuvirtual.ubu.es/";

	/**
	 * Crea el selector de idioma.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtHost.setText(HOST);
		ObservableList<Languages> languages = FXCollections.observableArrayList(Languages.values());
		languageSelector.setItems(languages);
		languageSelector.setValue(controller.getSelectedLanguage());

		// Carga la interfaz con el idioma seleccionado
		languageSelector.getSelectionModel().selectedItemProperty().addListener((ov, value, newValue) -> {
			controller.setResourceBundle(newValue);
			logger.info("Idioma cargado: {}", controller.getResourceBundle().getLocale().toString());
			logger.info("[Bienvenido a UBUGrades]");
			changeScene(getClass().getResource("/view/Login.fxml"));
		});
	}

	/**
	 * Hace el login de usuario al pulsar el botón Entrar. Si el usuario es
	 * incorrecto, muestra un mensaje de error.
	 * 
	 * @param event
	 *            El ActionEvent.
	 */
	public void login(ActionEvent event) {
		if (txtHost.getText().isEmpty() || txtPassword.getText().isEmpty() || txtUsername.getText().isEmpty()) {
			lblStatus.setText(controller.getResourceBundle().getString("error.fields"));
		} else {
			controller.getStage().getScene().setCursor(Cursor.WAIT);

			Task<Void> loginTask = getUserDataWorker();
			lblStatus.textProperty().bind(loginTask.messageProperty());

			loginTask.setOnSucceeded(s -> {
				controller.getStage().getScene().setCursor(Cursor.DEFAULT);
				changeScene(getClass().getResource("/view/Welcome.fxml"));
			});

			loginTask.setOnFailed(e -> {
				controller.getStage().getScene().setCursor(Cursor.DEFAULT);
				txtPassword.setText("");
			});
			Thread th = new Thread(loginTask, "login");

			th.start();
		}
	}

	/**
	 * Permite cambiar la ventana actual.
	 * 
	 * @param sceneFXML
	 *            La ventanan a la que se quiere cambiar.
	 *
	 * @throws IOException
	 */
	private void changeScene(URL sceneFXML) {
		try {
			// Accedemos a la siguiente ventana
			FXMLLoader loader = new FXMLLoader(sceneFXML, controller.getResourceBundle());
			controller.getStage().close();
			Stage stage = new Stage();
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.getIcons().add(new Image("/img/logo_min.png"));
			stage.setTitle("UBUGrades");
			stage.setResizable(false);
			stage.show();
			controller.setStage(stage);
		} catch (IOException e) {
			logger.info("No se ha podido cargar la ventana de bienvenida: {}", e);
		}
	}

	/**
	 * Realiza las tareas mientras carga la barra de progreso
	 * 
	 * @return tarea
	 */
	private Task<Void> getUserDataWorker() {
		return new Task<Void>() {
			@Override
			protected Void call() throws IOException {
				try {
					
					controller.tryLogin(txtHost.getText(), txtUsername.getText(), txtPassword.getText());

				} catch (IOException e) {
					logger.error("No se ha podido conectar con el host.", e);
					updateMessage(controller.getResourceBundle().getString("error.host"));
					throw e;
				} catch (JSONException e) {
					logger.error("Usuario y/o contraseña incorrectos", e);
					updateMessage(controller.getResourceBundle().getString("error.login"));
					throw e;

				}
				try {
					MoodleUser moodleUser = CreatorUBUGradesController.createMoodleUser(controller.getUsername());
					controller.setUser(moodleUser);
				} catch (IOException e) {
					logger.error("Error al obtener los datos del usuario.", e);
					updateMessage("Error al obtener los datos del usuario.");
				}

				return null;
			}
		};

	}

	/**
	 * Borra los parámetros introducidos en los campos
	 * 
	 * @param event
	 *            El ActionEvent.
	 */
	public void clear(ActionEvent event) {
		txtUsername.setText("");
		txtPassword.setText("");
		txtHost.setText("");
	}
}
