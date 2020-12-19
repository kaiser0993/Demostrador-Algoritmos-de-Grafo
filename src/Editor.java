import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.math.*;

import javax.swing.*;
import java.util.*;

public class Editor extends JPanel implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	private Grafo grafo = new Grafo();
	private JButton btn_agregarVertice = new JButton("Agregar Vertice");
	private JButton btn_eliminarVertice = new JButton("Eliminar Vertice");
	private JButton btn_agregarArista = new JButton("Agregar Arista");
	private JButton btn_eliminarArista = new JButton("Eliminar Arista");
	private JButton btn_numVertices= new JButton("Número de Vertices");
	private JButton btn_numAristas = new JButton("Número de Aristas");
	private JButton btn_floyd = new JButton("Floyd-Warshall");
	private JButton btn_recorrido = new JButton("Recorrido BFS");
	private JButton btn_cancelar = new JButton("Cancelar");
	private JLabel label_seleccionar = new JLabel("Seleccione donde desea colocar el vertice.");
	private final int UI_distanceEntreBotones = 160;
	private final int UI_sizeVertice = 30;
	
	private AffineTransform UI_transformacion = new AffineTransform();
	private Line2D.Double UI_linea;

	Polygon UI_puntaFlecha = new Polygon();  
	
	private boolean estado = false;
	
	double algebraDesplazarLinea(double pixeles, double x1, double x2, double y1, double y2, boolean x) {
		if(x) {
			return pixeles*(x2-x1)/(Math.sqrt(Math.pow((x2-x1),2)+Math.pow(y2-y1,2)));
		}else {
			return pixeles*(y2-y1)/(Math.sqrt(Math.pow((x2-x1),2)+Math.pow(y2-y1,2)));
		}
	}
	
	void definirUI() {
		UI_puntaFlecha.addPoint( 0,5);
		UI_puntaFlecha.addPoint( -5, -5);
		UI_puntaFlecha.addPoint( 5,-5);
		
		btn_cancelar.setBounds(10, 10, 150, 30);
		btn_agregarVertice.setBounds(10, 10, 150, 30);
		btn_eliminarVertice.setBounds(10+UI_distanceEntreBotones*1, 10, 150, 30);
		btn_agregarArista.setBounds(10+UI_distanceEntreBotones*2, 10, 150, 30);
		btn_eliminarArista.setBounds(10+UI_distanceEntreBotones*3, 10, 150, 30);
		btn_numVertices.setBounds(10+UI_distanceEntreBotones*4, 10, 150, 30);
		btn_numAristas.setBounds(10+UI_distanceEntreBotones*5, 10, 150, 30);
		btn_floyd.setBounds(10+UI_distanceEntreBotones*6, 10, 150, 30);
		btn_recorrido.setBounds(10+UI_distanceEntreBotones*7, 10, 125, 30);
		
		label_seleccionar.setBounds(15+UI_distanceEntreBotones*1, 10, 550, 30);
		
		btn_cancelar.addActionListener(this);
		btn_agregarVertice.addActionListener(this);
		btn_eliminarVertice.addActionListener(this);
		btn_agregarArista.addActionListener(this);
		btn_eliminarArista.addActionListener(this);
		btn_numVertices.addActionListener(this);
		btn_numAristas.addActionListener(this);
		btn_floyd.addActionListener(this);
		btn_recorrido.addActionListener(this);
		
		addMouseListener(this);
	}
	
	void mostrarBotones() {
		btn_agregarVertice.setVisible(true);
		btn_eliminarVertice.setVisible(true);
		btn_agregarArista.setVisible(true);
		btn_eliminarArista.setVisible(true);
		btn_numVertices.setVisible(true);
		btn_numAristas.setVisible(true);
		btn_floyd.setVisible(true);
		btn_recorrido.setVisible(true);
		label_seleccionar.setVisible(true);
	}
	
	void ocultarBotones() {
		btn_agregarVertice.setVisible(false);
		btn_eliminarVertice.setVisible(false);
		btn_agregarArista.setVisible(false);
		btn_eliminarArista.setVisible(false);
		btn_numVertices.setVisible(false);
		btn_numAristas.setVisible(false);
		btn_floyd.setVisible(false);
		btn_recorrido.setVisible(false);
		label_seleccionar.setVisible(false);
	}
	
	void manejarEstados() {
		if(!estado) {
			mostrarBotones();
			btn_cancelar.setVisible(false);
			label_seleccionar.setVisible(false);
		}else {
			ocultarBotones();
			btn_cancelar.setVisible(true);
			label_seleccionar.setVisible(true);
		}
		label_seleccionar.setBounds(15+UI_distanceEntreBotones*1, 10, 550, 30);
		repaint();
	}
		
	Editor(JFrame ventana){
		definirUI();
		mostrarBotones();
		ventana.add(btn_cancelar);
		ventana.add(btn_agregarVertice);
		ventana.add(btn_eliminarVertice);
		ventana.add(btn_agregarArista);
		ventana.add(btn_eliminarArista);
		ventana.add(btn_numVertices);
		ventana.add(btn_numAristas);
		ventana.add(btn_floyd);
		ventana.add(btn_recorrido);
		ventana.add(label_seleccionar);
		manejarEstados();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Font font = new Font("Arial", Font.PLAIN, 16);
    	g2d.setFont(font);
    	
	    int width = getWidth();
	    int height = getHeight();
	    g2d.drawRect(10, 50, (int)width-30, (int)height-70);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    for (Grafo.Vertice vertice : grafo.vertices){ 
	    	g2d.setColor(Color.BLACK);
	    	// Dibujar aristas
	    	for(Grafo.Arista arista : vertice.aristas) {
	    		Grafo.Vertice vertice2 = grafo.buscarVertice(arista.nombre);
	    		
	    		AffineTransform UI_transformacionOriginal = g2d.getTransform();
	    		
	    		// Asignar recta y dibujar recta
	    		// gracias a dios que pase algebra lineal...
	    		double dx = algebraDesplazarLinea(-20, vertice.x, vertice2.x, vertice.y, vertice2.y, true);
	    		double dy = algebraDesplazarLinea(-20, vertice.x, vertice2.x, vertice.y, vertice2.y, false);
	    		
	    	    String valor = String.valueOf(arista.peso);
	    	    if(grafo.existeArista(arista.nombre, vertice.nombre)) {
	    	    	valor = grafo.buscarArista(arista.nombre, vertice.nombre).peso + " / " + valor;
	    	    }
	    		
	    		UI_linea = new Line2D.Double(vertice.x, vertice.y, vertice2.x+dx, vertice2.y+dy);
	    		g2d.setColor(Color.BLACK);
	    		g2d.drawLine(vertice.x, vertice.y, vertice2.x, vertice2.y);
	    		
	    		// Dibujar punta de flecha
	    		UI_transformacion.setToIdentity();
	    	    double UI_angle = Math.atan2(UI_linea.y2-UI_linea.y1, UI_linea.x2-UI_linea.x1);
	    	    UI_transformacion.translate(UI_linea.x2, UI_linea.y2);
	    	    UI_transformacion.rotate((UI_angle-Math.PI/2d));  

	    	    g2d.setTransform(UI_transformacion);   
	    	    g2d.fill(UI_puntaFlecha);
	    	    g2d.setTransform(UI_transformacionOriginal);
	    		
	    		// Dibujar Pesos
	    		int largoPeso = (int) g2d.getFontMetrics().getStringBounds(valor, g2d).getWidth();
		        int start = largoPeso/2 - largoPeso;
		        int midx = (vertice.x + vertice2.x)/2;
		        int midy = (vertice.y + vertice2.y)/2;
		        g2d.setColor(Color.BLACK);
		        g2d.fillRect(midx-largoPeso/2-6, midy-15, largoPeso+12, 19);
		    	g2d.setColor(Color.WHITE);
		        g2d.drawString(valor, start + midx, midy);
	    		
	    	}
	    }
	    
	    for (Grafo.Vertice vertice : grafo.vertices){ 
	    	
	    	// Parametros
	    	int x=(int)(vertice.x - UI_sizeVertice/2);
	    	int y=(int)(vertice.y - UI_sizeVertice/2);
	    	g2d.setColor(Color.BLACK);
	    	
	    	// Dibujar nombre de vertices
	        int largoString = (int) g2d.getFontMetrics().getStringBounds(vertice.nombre, g2d).getWidth();
	        int start = largoString/2 - largoString;
	        g2d.drawString(vertice.nombre, start + vertice.x, vertice.y+40);
	    	
	        // Dibujar vertices
	        //g2d.fillOval(x-1, y-1, UI_sizeVertice+2, UI_sizeVertice+2);
	    	g2d.setColor(vertice.color);
	        g2d.fillOval(x, y, UI_sizeVertice, UI_sizeVertice);
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Switch con Objetos no se puede en Java
		if(e.getSource() == btn_cancelar) {
			estado = false;
		}else if (e.getSource() == btn_agregarVertice) {
			estado = true;
		}else if (e.getSource() == btn_eliminarVertice) {
			String nombre = JOptionPane.showInputDialog(this, "Escriba el nombre del vertice que desea eliminar: ");
			if(nombre!=null && nombre.length()>0) {
				grafo.eliminarVertice(nombre);
			}
		}else if (e.getSource() == btn_numVertices) {
			JOptionPane.showMessageDialog(this, "El número de vertices en el Grafo es: " + grafo.numeroVertices(),
			"Grafo", JOptionPane.PLAIN_MESSAGE, null);
		}else if(e.getSource() == btn_numAristas) {
			JOptionPane.showMessageDialog(this, "El número de aristas en el Grafo es: " + grafo.numeroAristas(),
			"Grafo", JOptionPane.PLAIN_MESSAGE, null);
		}else if (e.getSource() == btn_agregarArista) {
			 JTextField origen = new JTextField(8);
		     JTextField destino = new JTextField(8);
		     JTextField peso = new JTextField(5);
		     JPanel dialogo = new JPanel();
		     
		     dialogo.setLayout(new GridLayout(0, 2, 2, 2));
		     dialogo.add(new JLabel("Nombre del vertice de origen: "));
		     dialogo.add(origen);
		     
		     dialogo.add(new JLabel("Nombre del vertice de destino:"));
		     dialogo.add(destino);
		     
		     dialogo.add(new JLabel("Peso de la Arista:"));
		     dialogo.add(peso);
		     
		     int resultado = JOptionPane.showConfirmDialog(null, dialogo, 
		     "Ingrese los datos solicitados: ", JOptionPane.OK_CANCEL_OPTION);
		     
		     if (resultado == JOptionPane.OK_OPTION) {
		    	 try { grafo.agregarArista(origen.getText(), destino.getText(), Integer.parseInt(peso.getText()));
		    	 }catch(NumberFormatException | NullPointerException ex){}
		         
		     }
		     
		}else if (e.getSource() == btn_eliminarArista) {
			 JTextField origen = new JTextField(8);
		     JTextField destino = new JTextField(8);
		     JPanel dialogo = new JPanel();
		     
		     dialogo.setLayout(new GridLayout(0, 2, 2, 2));
		     dialogo.add(new JLabel("Nombre del vertice de origen: "));
		     dialogo.add(origen);
		     
		     dialogo.add(new JLabel("Nombre del vertice de destino:"));
		     dialogo.add(destino);
		     
		     int resultado = JOptionPane.showConfirmDialog(null, dialogo, 
		     "Ingrese los datos solicitados: ", JOptionPane.OK_CANCEL_OPTION);
		     
		     if (resultado == JOptionPane.OK_OPTION) {
			     try { grafo.eliminarArista(origen.getText(), destino.getText());
		    	 }catch(NumberFormatException | NullPointerException ex){}
		     }
		}else if(e.getSource() == btn_floyd) {
			algoritmoFloydWarshall();
		}else if(e.getSource() == btn_recorrido) {
			String nombre = JOptionPane.showInputDialog(this, "Escriba el nombre del vertice que desea usar como inicio: ");
			if(nombre!=null && nombre.length()>0) {
				algoritmoRecorridoBFS(nombre);
			}
			
		}
		manejarEstados();
	}
	
	void algoritmoFloydWarshall() {
		StringBuffer buffer =new StringBuffer();
		int[][] floyd = new int[grafo.numeroVertices()][grafo.numeroVertices()];
		for(int i=0; i<grafo.numeroVertices();i++) {
			for(int j=0; j<grafo.numeroVertices();j++) {
				floyd[i][j] = 99999;
			}
			floyd[i][i] = 0;
			for(Grafo.Arista arista : grafo.vertices.get(i).aristas) {
				floyd[i][grafo.indiceDeAristaComoVertice(arista)] = arista.peso;
			}
		}
		for(int k = 0; k<grafo.numeroVertices(); k++) {
			for(int i = 0; i<grafo.numeroVertices(); i++) {
				for(int j = 0; j<grafo.numeroVertices(); j++) {
					if(floyd[i][k] + floyd[k][j] < floyd[i][j])
						floyd[i][j] = floyd[i][k] + floyd[k][j];
				}
			}
		}
		buffer.append("\n");
		buffer.append(String.format("%-7s ", " "));
		for(int i=0; i<grafo.numeroVertices(); i++) {
			buffer.append(String.format("%-7s ",  grafo.vertices.get(i).nombre));
		}
		buffer.append("\n");
		for(int i=0; i<grafo.numeroVertices(); i++) {
			for(int j=0; j<grafo.numeroVertices(); j++) {
				if(j==0) {
					buffer.append(String.format("%7s ", grafo.vertices.get(i).nombre));
				}
				if (floyd[j][i] == 99999){
					buffer.append(String.format("%-7s ", "-"));
				}else{
					buffer.append(String.format("%-7d ",floyd[j][i]));
				}
			}	
			buffer.append("\n");
		}
		
		if(buffer.toString().length() > 10) {
			JOptionPane.showMessageDialog(this, buffer, "Algoritmo de Floyd/Warshall", JOptionPane.PLAIN_MESSAGE, null);
		}else {
			JOptionPane.showMessageDialog(this, "Cree vertices y aristas para poder usar este algoritmo. ", "Error", JOptionPane.PLAIN_MESSAGE, null);
		}
	}
	
	
	

	
	void algoritmoRecorridoBFS(String nomVertice){
		
		StringBuffer buffer = new StringBuffer();
		Set<Grafo.Vertice> visitados = new HashSet<Grafo.Vertice>(); 
		Queue<Grafo.Vertice> cola = new LinkedList<Grafo.Vertice>();
		cola.add(grafo.buscarVertice(nomVertice));
		
		while(!cola.isEmpty()) {
			Grafo.Vertice desencolado = cola.poll();
			visitados.add(desencolado);
			buffer.append(desencolado.nombre + " ");
			for(Grafo.Arista arista: desencolado.aristas) {
				if(!visitados.contains(grafo.buscarVertice(arista.nombre))) {
					cola.add(grafo.buscarVertice(arista.nombre));
				}
			}
		}
		if(buffer.toString().length() > 1) {
			JOptionPane.showMessageDialog(this, buffer, "Algoritmo de Recorrido BFS", JOptionPane.PLAIN_MESSAGE, null);
		}else {
			JOptionPane.showMessageDialog(this, "Cree vertices y aristas para poder usar este algoritmo. ", "Error", JOptionPane.PLAIN_MESSAGE, null);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent evento) {
		if(estado) {
			String nombre = JOptionPane.showInputDialog(this, "Escriba el nombre del vertice que desea agregar: ");
			if(nombre!=null && nombre.length()>0) {
				int px = evento.getX();
				int py = evento.getY();
				px = Math.min(Math.max(26, px),1237);
				py = Math.min(Math.max(67, py),655);
				grafo.agregarVertice(px, py, nombre);
			}
		}
		manejarEstados();
	}

	@Override
	public void mouseClicked(MouseEvent noSeOcupa) {}
	@Override
	public void mouseEntered(MouseEvent noSeOcupa) {}
	@Override
	public void mouseExited(MouseEvent noSeOcupa) {}
	@Override
	public void mouseReleased(MouseEvent noSeOcupa) {}
	
}
