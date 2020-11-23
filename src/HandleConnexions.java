import java.io.IOException;
import java.net.Socket;

public class HandleConnexions implements Runnable {
Socket socketCommunication;
	public HandleConnexions(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
	}
/*Cette méthode aide à envoyer des threads*/
	public void run() {
		ClientSocket connexionClient = new ClientSocket(socketCommunication);
		connexionClient.getEntete();
		try {
			connexionClient.envoiReponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		connexionClient.fermetureFlux();
		System.out.println("un client a fait une connexion");
	}



}
