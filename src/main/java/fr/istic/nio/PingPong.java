package fr.istic.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class PingPong {

	private static int PORT_NUMBER = 9999;
	private static int BUFFER_SIZE = 100;
	public static void main(String[] args) {


		// On se met en attente sur le port :
		ServerSocketChannel server;
		try {
			server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(PORT_NUMBER));
			server.socket().setReuseAddress(true);

			// Configuration du mode non bloquant :
			server.configureBlocking(false);

			Selector selector = Selector.open();
			// Enregistre le server auprès du selecteur
			// Demande à être prévenu des événements OP_ACCEPT
			server.register(selector, SelectionKey.OP_ACCEPT);

			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			int number = 0;
			System.out.println("Server ready.");
			System.out.println("Connect with nc 192.168.1.31 9999");
			System.out.println("And send \"PING <number>\"");

			// On boucle :
			while (true) {
				// On commence par attendre sur un selector
				int channelCount = selector.select(); //channelCount est le nombre de cannaux disponibles
				if (channelCount > 0) {
					//On récupére la liste des selections :
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = keys.iterator();
					// On traite tous les channels disponibles
					while (iterator.hasNext()) {
						//Passe à la clef suivante :
						SelectionKey key = iterator.next();
						//Enleve la selection de l'ensemble pour ne pas la traiter deux fois
						iterator.remove();
						//si la clef n'est pa valide, on ne fait rien
						if (!key.isValid()) {
							System.out.println("- Key not valid");
							continue;
						}
						//Traitement des clefs
						if (key.isAcceptable()) {
							System.out.println("- Key Acceptable.");
							// Si le channel est prêt à accepter une connexion
							System.out.println("\tNouvelle connexion : ");
							SocketChannel client = server.accept();
							System.out.println("\t"+client.getRemoteAddress()+"");

							// On configure la socket en mode non bloquant :
							client.configureBlocking(false);

							// On demande à écouter les opérations de lectures :
							client.register(key.selector(), SelectionKey.OP_READ);
							System.out.println("\tGoes to read.");
						} else if (key.isReadable()) {
							System.out.println("- Key Readable. ");
							// Si le channel à envoyé une information :

							SocketChannel client = (SocketChannel) key.channel();
							//ByteBuffer buffer = (ByteBuffer) key.attachment();
							// Lit du channel vers le buffer :
							if (client.read(buffer) < 0) {
								System.out.println("\tNothing left in buffer. Connexion closed.");
								// si le client n'a plus rien à écrire
								// on désenregistre le client :
								key.cancel();
								// on ferme
								client.close();
							} else {
								System.out.println("\tReading buffer.");
								buffer.flip();
								byte[] bytes = new byte[buffer.remaining()];
								buffer.get(bytes, 0, buffer.remaining());

								//Get string of message
								String cmd = new String(bytes).trim();
								System.out.println("\tMessage received : "+cmd);

								//check if has a number
								try{
									String [] msgArray = cmd.split(" ");
									if(msgArray.length != 2){
										throw new Exception("\tWrong command received, close connexion.");
									}
									else if (cmd.startsWith("PING")) {
										key.interestOps(SelectionKey.OP_WRITE);
										number = Integer.parseInt(msgArray[1].trim());
										System.out.println("\tGoes to WRITE");
									}
								}
								catch(Exception e){
									e.printStackTrace();
									key.cancel();
									client.close();
								}

								buffer.clear();
							}
						} else if (key.isWritable()){
							System.out.println("- Key WRITABLE.");
							// si on attend une écriture
							SocketChannel client = (SocketChannel) key.channel();
							//Get int
							String newMessage = "PONG "+number;
							buffer.put(newMessage.getBytes(StandardCharsets.UTF_8));
							buffer.flip();
							client.write(buffer);
							System.out.println("\tMessage sent :"+newMessage);

							// une fois envoyé, on enlève le client :
							key.cancel();
							client.close();
							System.out.println("\tConnexion closed.");
						}

					}
				}


				//En mode non bloquant la méthode retourne null si il n'y a pas de client.
				//SocketChannel client = server.accept();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
