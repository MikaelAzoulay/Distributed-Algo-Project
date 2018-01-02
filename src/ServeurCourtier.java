import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServeurCourtier extends Thread{


	private int port;
	Courtier courtier;


	public ServeurCourtier(int port , Courtier courtier) {
		 this.port = port;
		 this.courtier=courtier;
	}


	public  void run(){


		ServerSocket se;
		Socket ssv=null;



		try{

		se =  new ServerSocket(this.port);
		System.out.println ("la courtier est connecte et ecoute sur le port num " + this.port);
        System.out.println ("en attente d'une connexion d'un client");



			while(true){


			ssv = se.accept(); //le courtier se connecte
			System.out.println ("client connecté au courtier");
			ThreadCourtierClient reponse = new ThreadCourtierClient(ssv, this.courtier);
            reponse.start();
            //System.out.println("inscription valide");
            //System.out.println(bourse.connexion.toString());

			}
		}
		catch (IOException e){
			System.err.println("Erreur : " +e);
		}
		finally{
			try{
				ssv.close();
			}
			catch (IOException e){}
		}
	}

}
