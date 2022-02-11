package ejemplo2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Clase UDPCliente
 * @author javie
 *
 */
public class UDPCliente {
	
	/** Atributo est�tico que recoger� el Tiempo de espera **/
	private static final int TIEMPO_ESPERA = 5000;
	
	/** Atributo est�tico que recoger� la posici�n**/
	private static final int POSICION = 5;

	/**
	 * M�todo main encargado de ejecutar la aplicaci�n
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner S = new Scanner(System.in);

		// Interacci�n con el usuario para que introduzca texto
		System.out.println("Escriba un mensaje para que el servidor lo reciba. \n");
		String texto = S.nextLine();
		
		// Muestra del mensaje
		System.out.println("Mensaje enviado al servidor " + texto + "\n");

		// Almacenamiento en un array de bytes los bytes de la variable recogida por el usuario
		byte[] bytes = texto.getBytes();
		
		// Declaraci�n de la variable de la clase DatagramSocket a null
		DatagramSocket datagramSocket = null;
		try {
			// Inicializaci�n de la variable creada anteriormente
			datagramSocket = new DatagramSocket();
			
			// Modificaci�n del tiempo de espera con el m�todo setSoTimeout de la clase DatagramSocket
			datagramSocket.setSoTimeout(TIEMPO_ESPERA);
			
			// Creaci�n de un objeto de la clase DatagramPacket llamado paqueteEnviado
			DatagramPacket paqueteEnviado = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 8889);
			
			// Creaci�n de un objeto de la clase DatagramPacket llamado paqueteRecibido
			DatagramPacket paqueteRecibido = new DatagramPacket(new byte[bytes.length], bytes.length);

			// Creaci�n de una variable de tipo Int que guardan los intentos
			int intentos = 0;
			
			// Creaci�n de una variable de tipo Boolean inicializada a False
			boolean recibido = false;
			do {
				// Uso del m�todo send para enviar la variable paqueteEnviado
				datagramSocket.send(paqueteEnviado);
				try {
					
					// Uso del m�todo receive para recibir la variable paqueteRecibido
					datagramSocket.receive(paqueteRecibido);
					
					// Establecimiento de condiciones
					if (!paqueteRecibido.getAddress().equals(paqueteEnviado.getAddress())) {
						throw new IOException("ERROR");
					}
					recibido = true;
				} catch (IOException e) {
					// En caso de fallo, se vuelve a intenar
					intentos += 1;
					System.out.println("Tiempo acabado ,  quedan " + (POSICION - intentos) + " intentos");
				}
			} while (!recibido && (intentos < POSICION));

			// Establecimiento de condiciones
			if (recibido) {
				System.out.println("El mensaje del servidor es : " + new String(paqueteRecibido.getData()));
			} else {
				System.out.println("Saliendo ...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Se cierra el Socket con el m�todo close
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}
}
