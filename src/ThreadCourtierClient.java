import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ThreadCourtierClient extends Thread{
    Socket ssv;
    PrintWriter out;
    BufferedReader in;
    Courtier courtier;
    int nomClient;
    ObjectOutputStream outObject;
    ObjectInputStream  inObject;

    ThreadCourtierClient(Socket socket , Courtier courtier) {
        ssv = socket;
        this.courtier= courtier;

    }

    @Override
    public void run() {
      while(true) {
    	   try {
           in = new BufferedReader(new InputStreamReader(ssv.getInputStream()));
           out = new PrintWriter(ssv.getOutputStream(), true);
           outObject = new ObjectOutputStream(ssv.getOutputStream());

           String rep=in.readLine();
           System.out.println ("inscription du client en cours");

    			 // Le client demande à s'inscrire
  				 if (rep.equals("inscription")) {
    			    String	str = "Envoyez moi votre nom ";
    					out.println(str);
    					String nom = in.readLine();
    					System.out.println("nom du client " + nom);
    					nomClient = Integer.parseInt(nom);
    					//On v�rifie si c'est une premi�re inscription du client ou s'il revient
    					for (Map.Entry<Integer, String> c : courtier.getListeClient().entrySet()) {
    						if (nomClient==c.getKey()) {
    							c.setValue("ouvert");
    							out.println("Vous revoila !");
    						}
    						else {
    							courtier.getListeClient().put(nomClient, "ouvert");
    						}
    					}
    					// Informera la bourse de la nouvelle connexion d'un client
    					courtier.Actualisation_connexion();

    					// Acquittement au client pour valider sa connexion
    					out.println("ack");
    					//Envoi de la l'état actuel du marché au client
    					outObject.writeObject(courtier.getSocietes());

	            out.println("inscription client terminee aupres du courtier :");
	            out.println(courtier.getNomCourtier());
	            //TEST : Affiche la liste des clients du courtier
	            System.out.println("AFFICHAGE DES CLIENTS DE CE COURTIER");
	            System.out.println(courtier.getListeClient().toString());
	            System.out.println();

    	     }

    				//Le client demande l'état du marché à son courtier
    			 else if( rep.equals("marche")) {
    					out.println("marche");
    					outObject.writeObject(courtier.getSocietes());
    			 }

    				//Le client passe une commande à son courtier
    			 else if( rep.equals("commander")) {
    					Commande com = new Commande();
    					out.println("voulez vous acheter ou vendre ? ");
    					com.setChoix(in.readLine());
    					out.println("Quelle societe ?");
    					com.setNomSociete(in.readLine());
    					out.println("A quel prix ?");
    					com.setPrix((double)Integer.parseInt(in.readLine()));
    					out.println("Combien d'actions ?");
    					com.setNbActions(Integer.parseInt(in.readLine()));
    					com.setNomCourtier(courtier.getNomCourtier());
    					com.setNomClient(nomClient);
    					courtier.getListeAttenteCommande().put(nomClient, com);
    					System.out.println(courtier.getListeAttenteCommande().toString());
    					courtier.EnvoyerCommandeBourse(com);
       		 }

    				//Le client prévient son courtier qu'il ferme sa journée
    			 else if( rep.equals("fermer")) {
    					courtier.getListeClient().put(nomClient, "ferme");
    					//Informera la bourse que ce client a fermé sa journée
    					courtier.ActualisationClients();
    					boolean tmp = true;
    					for (Map.Entry<Integer, String> c : courtier.getListeClient().entrySet()) {
    						while(tmp) {
    							if (c.getValue().equals("ouvert")) {
    								tmp = false;
    							}
    						}
    					}
    					//Si tous ses clients sont ferm�s, le courtier se ferme
    					if(tmp) courtier.setEtat("ferme");
    			 }


          in.close();
          out.close();
          ssv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	}

    }
}
