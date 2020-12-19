import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class Grafo {
	
	float paleta = 0;
	
	List<Vertice> vertices = new LinkedList<Vertice>();
	
	public class Arista{
		String nombre;
		Color color;
		int peso;
		Arista(String nombre, int peso){
			this.nombre = nombre;
			this.peso = peso;
			this.color = Color.black;
		}
		
	}
	
	public class Vertice {
		Color color;
		String nombre;
		int x;
		int y;
		List<Arista> aristas = new LinkedList<Arista>();
		Vertice(int px, int py, String nombre){
			this.x = px;
			this.y = py;
			this.nombre = nombre;	
			this.color = Color.getHSBColor(paleta, 0.9f, 0.9f);
			paleta += 0.15f;
			if(paleta>=1) {
				paleta = 0;
			}
		}	
	}
	
	Vertice buscarVertice(String nombreVertice) {
		for(int i=0; i<vertices.size(); i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice)) {
				return vertices.get(i);
			}
		}
		return null;
	}
	
	void agregarVertice(int px, int py, String nombreVertice) {
		vertices.add(new Vertice(px, py, nombreVertice));
	}
	
	void eliminarVertice(String nombreVertice) {
		for(int i=0; i<vertices.size(); i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice)) {
				vertices.remove(i);
				break;
			}
		}
		for(int i=0; i<vertices.size(); i++) {
			for(int j=0; j<vertices.get(i).aristas.size(); j++) {
				if(vertices.get(i).aristas.get(j).nombre.equalsIgnoreCase(nombreVertice)) {
					vertices.get(i).aristas.remove(j);
				}
			}
		}
	}
	
	void agregarArista(String nombreVertice1, String nombreVertice2, int peso) {
		boolean agregado = false;
		for(int i=0; i<vertices.size() && !agregado; i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice1)) {
				vertices.get(i).aristas.add(new Arista(nombreVertice2, peso));
				agregado = true;
			}
		}
	}
	
	void eliminarArista(String nombreVertice1, String nombreVertice2) {
		boolean eliminado = false;
		for(int i=0; i<vertices.size() && !eliminado; i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice1)){
				for(int j=0; j<vertices.get(i).aristas.size(); j++) {
					if(vertices.get(i).aristas.get(j).nombre.equalsIgnoreCase(nombreVertice2)) {
						vertices.get(i).aristas.remove(j);
						eliminado = true;
						break;
					}
				}
			}
		}
	}

	boolean existeArista(String nombreVertice1, String nombreVertice2) {
		for(int i=0; i<vertices.size(); i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice1)) {
				for(int j=0; j<vertices.get(i).aristas.size(); j++) {
					if(vertices.get(i).aristas.get(j).nombre.equalsIgnoreCase(nombreVertice2)) {
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}	
	
	Grafo.Arista buscarArista(String nombreVertice1, String nombreVertice2) {
		for(int i=0; i<vertices.size(); i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(nombreVertice1)) {
				for(int j=0; j<vertices.get(i).aristas.size(); j++) {
					if(vertices.get(i).aristas.get(j).nombre.equalsIgnoreCase(nombreVertice2)) {
						return vertices.get(i).aristas.get(j);
					}
				}
			}
		}
		return null;
	}
	
	int indiceDeAristaComoVertice(Grafo.Arista arista) {
		for(int i=0; i<vertices.size(); i++) {
			if(vertices.get(i).nombre.equalsIgnoreCase(arista.nombre)) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	int numeroVertices() {
		return vertices.size();
	}
	
	int numeroAristas() {
		int contador = 0;
		for(int i=0; i<vertices.size(); i++) {
			contador += vertices.get(i).aristas.size();
		}
		return contador;
	}
	void vaciarGrafo() {
		vertices.clear();
	}
}

