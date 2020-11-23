import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;



/**
 * @author Jean-François Sergerie et Stéphanie Leduc
 *
 */
public class ClientSocket {
	
	Socket socketCommunication;
	PrintWriter out = null; 
	BufferedReader in = null;
	String fullUrl;
	
	File file;
	File fichierErreur = new File(ClientSocket.class.getResource("siteHTTP/erreur.html").getFile());
	File fichierErreur403 = new File(ClientSocket.class.getResource("siteHTTP/erreur403.html").getFile());
	File fichierErreur404 = new File(ClientSocket.class.getResource("siteHTTP/erreur404.html").getFile());
	
	ClientSocket(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
		try {
			out = new PrintWriter(socketCommunication.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socketCommunication.getInputStream()));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	/*
	 * Méthode qui retourne l'entête
	 */
	void getEntete() {
		try {
			String initialLine = in.readLine();
			fullUrl = initialLine.split(" ")[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Méthode servant à analyser la réponse et envoyer la page correspondante
	 */
	void envoiReponse() throws IOException, Exception {
		String urlSite = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
		String corps = "";
		URL url = getClass().getResource("siteHTTP/" + urlSite);
		try {
			file = new File(url.getPath());
		} catch (Exception e) {
			file = new File("siteHTTP/erreur.html");
		}

		if (file.exists() && !urlSite.equals("secret.html")) {
			corps = lireFichier(file);
			printOK(corps);
		} else if (urlSite.equals("secret.html")) {
			corps = lireFichier(fichierErreur403);
			printErreur403(corps);
		} else {
			corps = lireFichier(fichierErreur404);
			printErreur404(corps);
		}
	}
	
	/*
	 * Méthode qui sert à lire le fichier HTML demandé.
	 * @param file : le nom du fichier à aller chercher
	 */
	public static String lireFichier(File file) throws IOException {

		StringBuilder sb = new StringBuilder();
		InputStream in = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line + System.lineSeparator());
		}
		br.close();
		return sb.toString();
	}
	
	
	/**
	 * Méthode qui sert simplement à fermer les PrintWriter et BufferedReader
	 */
	void fermetureFlux() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Méthode qui renvoit le contenu de la page demandée avec le status OK
	 * @param corps : tout le contenu de la page HTML
	 */
	private void printOK(String corps) {
		
		int len = corps.length();
		out.print("HTTP-1.0 200 OK\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();

	}
	
	/**
	 * Méthode qui renvoit le code d'erreur 403 FORBIDDEN
	 * @param corps : tout le contenu de la page HTML
	 */
	private void printErreur403(String corps) {
		
		int len = corps.length();
		out.print("HTTP-1.0 403 FORBIDDEN\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();
	}
	
	
	/**
	 * Méthode qui renvoit le code d'erreur 404 NOT FOUND
	 * @param corps : tout le contenu de la page HTML
	 */
	private void printErreur404(String corps) {

		int len = corps.length();
		out.print("HTTP-1.0 404 NOT FOUND\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();
	}
}
