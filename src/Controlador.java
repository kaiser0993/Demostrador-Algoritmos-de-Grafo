import javax.swing.*;

public class Controlador {

	JFrame ventana;
	Editor editor;
	
	void iniciar(){
		iniciarVentana();
		iniciarEditor();
		
	}
	
	void iniciarVentana(){
		ventana = new JFrame("Demostrador de Grafos");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setSize(1280, 720);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	} 
	
	void iniciarEditor(){
		editor = new Editor(ventana);
		ventana.add(editor);
	}
	
	public static void main(String[] args) {
		Controlador controlador = new Controlador();
		controlador.iniciar();
	}
}