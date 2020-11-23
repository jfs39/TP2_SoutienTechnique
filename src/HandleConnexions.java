
import java.net.Socket;

/**
 * @author Jean-Fran�ois Sergerie et St�phanie Leduc
 *
 */
public class HandleConnexions implements Runnable {
	Socket socketCommunication;


	public HandleConnexions(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
	}
	
	
	
/*Cette m�thode aide � envoyer des threads*/
	public void run() {
		ClientSocket connexionClient = new ClientSocket(socketCommunication);
		connexionClient.getEntete();
		try {
			connexionClient.envoiReponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		connexionClient.fermetureFlux();
		System.out.println("Un client a fait une connexion.");
	}



}
