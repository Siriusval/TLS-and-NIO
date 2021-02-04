package fr.istic.chiffrement;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class TestSite {

	public static void main(String[] args) {
		String [] hosts = { "www.google.fr","nextinpact.com","istic.univ-rennes1.fr"};
		int port = 443;

		for(String host : hosts){
			connecteEtAffiche(host,port);
			System.out.println();
		}
	}

	public static void connecteEtAffiche(String host, int port) {

		System.out.println("Host :"+host);
		System.out.println("Port :"+port);
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

		try{
			System.out.println("Exercice 1 : Récupération d'informations basiques\n");
			// Creation de la socket
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host,port);

			// Choix de la cipher suite si nécessaire (sinon laisser defaut).
			// Connexion
			InputStream in = sslsocket.getInputStream();
			OutputStream out = sslsocket.getOutputStream();

			out.write(1);

			// Affichage des infos demandées :
			while (in.available() > 0) {
				System.out.print(in.read());
			}
			System.out.println("Connection sécurisée active");

			//Protocol info
			SSLSession ss = sslsocket.getSession();

			System.out.println("    Cipher suite = " + ss.getCipherSuite());
			System.out.println("    Protocol = " + ss.getProtocol());

			/*
			System.out.println("    Certificates = ");

			for (Certificate cert : ss.getPeerCertificates()) {
				System.out.println(cert);
			}

			 */
			System.out.println("Exercice 2 : Essai de différentes cipher suite");
			String [] cypherSuitesToTest = {"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",

					"SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
					"SSL_RSA_WITH_3DES_EDE_CBC_SHA",
					"TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
					"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
					"TLS_RSA_WITH_AES_128_GCM_SHA256",
					"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"};
			//"TLS_KRB5_WITH_DES_CBC_MD5"

			for (String cypherSuite : cypherSuitesToTest) {
				System.out.println("    Cipher suite = "+cypherSuite);
				sslsocket = (SSLSocket) sslsocketfactory.createSocket();
				sslsocket.setEnabledCipherSuites(new String[] {cypherSuite});
				sslsocket.connect(new InetSocketAddress(host,port));

				ss = sslsocket.getSession();

				System.out.println("    Working = "+!ss.getCipherSuite().contains("NULL"));
				System.out.println("    Protocol = " + ss.getProtocol());
				System.out.println();
			}

			System.out.println("--------------------------------------------------");


		} catch (IOException e) {
			e.printStackTrace();
		}





	}
}
