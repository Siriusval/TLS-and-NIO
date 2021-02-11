package fr.istic.chiffrement;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.Arrays;

public class Client {

	private static char[] PASSWORD = "654321".toCharArray();
	static String HOST = "192.168.1.31";
	static int PORT = 9999;

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.trustStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.trustStore", "clientstore.keys");
		System.setProperty("javax.net.ssl.trustStorePassword", "654321");
		// Création d'un SSLContext comme pour le serveur
		// avec les caractéristiques suivantes :
		// + protocole utilisé : TLS
		SSLContext context = SSLContext.getInstance("TLS");
		// + format de clef PKCS12
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("PKCS12");
		// + mot de passe store et clef : variable PASSWORD plus haut (ou null)
		// + keystore : clientstore.keys
		ks.load(new FileInputStream("clientstore.keys"), PASSWORD);
		kmf.init(ks, PASSWORD);
		context.init(kmf.getKeyManagers(), null, null);
		Arrays.fill(PASSWORD, '0'); // on efface pour qu'il ne traine pas en mémoire.

		// Creation de la socket
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(HOST,PORT);
		System.out.println("Waiting for connexion.");
		System.out.println("Please start Serveur.java.");
		// Creation des printwriter et buffered reader puis :
		// lire PING
		InputStream in = sslsocket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		//Init line buffer
		String thisLine;

		//Init output
		OutputStream out = sslsocket.getOutputStream();

		PrintWriter printWriter = new PrintWriter(out);
		printWriter.println("PING");
		printWriter.flush();
		System.out.println("Connected. Message sent : PING");
		System.out.println("Message received : "+bufferedReader.readLine());

		sslsocket.close();
	}

}