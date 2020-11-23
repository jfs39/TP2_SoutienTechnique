import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
/*AUTEURS : Jean-François Sergerie et Stéphanie Leduc*/

public class ClientSocket {
	Socket socketCommunication;
	PrintWriter out = null; 
	BufferedReader in = null;
	String fullUrl;
	File file;
	
	ClientSocket(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
		try {
			out = new PrintWriter(socketCommunication.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socketCommunication.getInputStream()));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	void getEntete() {
		try {
			String initialLine = in.readLine();
			fullUrl = initialLine.split(" ")[1];
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void envoiReponse() throws IOException, Exception {
		String urlSite = fullUrl.substring(fullUrl.lastIndexOf("/")+1);
		String corps = "";
		URL url = getClass().getResource("siteHTTP/"+urlSite);
		try {
		 file = new File(url.getPath());
		}catch(Exception e) {
		 file = new File("siteHTTP/erreur.html");
		}
		File fichierErreur = new File(ClientSocket.class.getResource("siteHTTP/erreur.html").getFile());
		if(file.exists() && !urlSite.equals("secret.html") ) {
			corps= lireFichier(file);
			printOK(corps);
	        } else if(urlSite.equals("secret.html")) {
	       corps = lireFichier(fichierErreur);
	        printErreur403(corps);
	        } else {
	        	corps = lireFichier(fichierErreur);
	        	  printErreur404(corps);
	        }
		
		// longueur du corps de la réponse
		

		// envoie de la line de début, entêtes, la ligne vide et le corps


	}
	
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
	
	
	void fermetureFlux() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Cette méthode imprime la page avec le status OK */
	private void printOK( String corps) {
		int len = corps.length();
		out.print("HTTP-1.0 200 OK\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();
		
	}
	/* Cette méthode imprime la page avec le code d'erreur 403 FORBIDDEN */
	private void printErreur403(String corps){
		int len = corps.length();
		out.print("HTTP-1.0 403 FORBIDDEN\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();
	}
	
	private void printErreur404(String corps) {
		
		int len = corps.length();
		out.print("HTTP-1.0 404 NOT FOUND\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();
	}
}
