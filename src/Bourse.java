import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Bourse {

	private int port;
	private ArrayList<Societe> societes ;
	private HashMap<Integer, Integer> connexion;
	String etat;

	public String getEtat() {
		return etat;
	}


	public void setEtat(String etat) {
		this.etat = etat;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public ArrayList<Societe> getSocietes() {
		return societes;
	}


	public void setSocietes(ArrayList<Societe> societes) {
		this.societes = societes;
	}


	public HashMap<Integer, Integer> getConnexion() {
		return connexion;
	}


	public void setConnexion(HashMap<Integer, Integer> connexion) {
		this.connexion = connexion;
	}


	public Bourse(int port) {
		 this.port = port;
		 this.connexion = new HashMap <Integer, Integer>();
		 this.societes = new ArrayList<Societe>();
		 this.etat = "ouvert";
	}


	public static void main(String[] args){


		ServerSocket se;
		Socket ssv=null;
		Bourse bourse;


		try{

			if (args.length > 0)
	            bourse = new Bourse(Integer.parseInt(args[0]));
	        else
	            bourse = new Bourse(4020);

		se =  new ServerSocket(bourse.port);
		System.out.println ("la bourse est connectee et ecoute sur le port num " + bourse.port);
        System.out.println ("en attente d'une connexion d'un courtier");



			while(true){


			ssv = se.accept(); //le courtier se connecte
			System.out.println ("courtier connect� a la bourse");
			ThreadBourseCourtier reponse = new ThreadBourseCourtier (ssv, bourse);
            reponse.start();

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
