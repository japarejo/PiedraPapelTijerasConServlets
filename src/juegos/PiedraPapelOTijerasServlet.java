package juegos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

/**
 * Servlet implementation class PiedraPapelOTijerasServlet
 */
@WebServlet("/jugar")
public class PiedraPapelOTijerasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Table<Jugada,Jugada,Resultado> reglas=ImmutableTable.<Jugada, Jugada, Resultado>builder()
    		.put(Jugada.Piedra, Jugada.Piedra,  Resultado.Empate)
    		.put(Jugada.Piedra, Jugada.Papel,   Resultado.GanaCliente)
    		.put(Jugada.Piedra, Jugada.Tijeras, Resultado.GanaServidor)
    		.put(Jugada.Papel,  Jugada.Piedra,  Resultado.GanaServidor)
    		.put(Jugada.Papel,  Jugada.Papel,   Resultado.Empate)
    		.put(Jugada.Papel,  Jugada.Tijeras, Resultado.GanaCliente)
    		.put(Jugada.Tijeras,  Jugada.Piedra,  Resultado.GanaCliente)
    		.put(Jugada.Tijeras,  Jugada.Papel,   Resultado.GanaServidor)
    		.put(Jugada.Tijeras,  Jugada.Tijeras, Resultado.Empate)
    		.build();
    
    public PiedraPapelOTijerasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	// IMPLEMENTA UN CONTROLADOR:
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtener parámetros y validar:		
		Jugada jugadaCliente=Jugada.valueOf(request.getParameter("jugada"));
		// Invocar al modelo:
		Jugada jugadaServidor=generarJugada();				
		Resultado resultado=evaluarResultado(jugadaServidor,jugadaCliente);
		// Ordenar a la vista que se muestre con la información adecuada
		response.setContentType("text/html");		
		response.getWriter().append(mostrarJugada(jugadaCliente,jugadaServidor,resultado));
	}

	private String mostrarJugada(Jugada jugadaCliente, Jugada jugadaServidor, Resultado resultado) {
		return "<div>"+
					"<div> Su jugada: "+jugadaCliente.render()+"</div><p>"+
					"<div> La jugada del servidor: "+jugadaServidor.render()+"</div><p>"+
					"<div><h1>"+resultado.mensaje+"</h1></div>"+
				"</div>";
	}

	private Resultado evaluarResultado(Jugada jugadaServidor, Jugada jugadaCliente) {
		return reglas.get(jugadaServidor, jugadaCliente);		
	}

	private Jugada generarJugada() {
		int indice=(int)Math.floor(Math.random()*3.0);
		return Jugada.values()[indice];
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public enum Jugada{
		Piedra("img/piedra.png"),
		Papel("img/papel.jpg"),
		Tijeras("img/tijeras.jpg");
		
		public final String imagen;
		
		private Jugada(String imagen) {
			this.imagen=imagen;
		}	
		
		public String render() {
			return "<img src='"+imagen+"' width='100px' height='100px'>";
		}
	};
	public enum Resultado{
		GanaServidor("Usted pierde!!! ;-P"),
		GanaCliente("Enhorabuena!!! Usted gana :-D"),
		Empate("Empate!!! :-/");
		
		public final String mensaje;
		
		private Resultado(String mensaje) {
			this.mensaje=mensaje;
		}
		public String render() {
			return mensaje;
		}
	};
}
