import java.io.IOException;
import java.net.Socket;

public class HandleConnexions implements Runnable {
Socket socketCommunication;
	public HandleConnexions(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
	}

	public void run() {
		// TODO Auto-generated method stub
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
