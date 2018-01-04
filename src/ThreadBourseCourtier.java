import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;


public class ThreadBourseCourtier extends Thread {

    Socket ssv;
    PrintWriter out;
    BufferedReader in;
    Bourse bourse;
    int nomCourtier;
    ObjectOutputStream outObject;
    ObjectInputStream inObject;

    ThreadBourseCourtier(Socket socket , Bourse bourse) {
        ssv = socket;
        this.bourse= bourse;

    }

    @Override
    public void run() {
    	while (true) {
        try {
            in = new BufferedReader(new InputStreamReader(ssv.getInputStream()));
            out = new PrintWriter(ssv.getOutputStream(), true);
            outObject = new ObjectOutputStream(ssv.getOutputStream());
            inObject = new ObjectInputStream(ssv.getInputStream());



            String rep=in.readLine();

            System.out.println ("inscription du courtier en cours");

			// si le courtier veut s'inscrire � la bourse
			if (rep.equals("inscription")) {

				String	str = "Envoyez moi votre nom ";
				out.println(str);
				String nom = in.readLine();
				System.out.println("nom du courtier " + nom);
				nomCourtier = Integer.parseInt(nom);
				bourse.getConnexion().put(nomCourtier, 0);
				//On v�rifie si c'est une premi�re inscription du courtier ou s'il revient NECESSAIRE ???
				/*for (Map.Entry<Integer, Integer> c : bourse.getConnexion().entrySet()) {
					if (nomCourtier==c.getKey()) {
						c.setValue(0);
						out.println("Vous revoila !");
						//marquer le courtier comme ouvert necessaire ???
					}
					else {
						courtier.getListeClient().put(nomClient, "ouvert");
					}
				}*/

				// Acquittement au courtier pour valider sa connexion
				out.println("ack");
				//Envoi de la l'état actuel du marché au courtier
				outObject.writeObject(bourse.getSocietes());

		        out.println("inscription courtier terminee aupres de la bourse :");
		        //TEST : Affiche la liste des clients du courtier
		        System.out.println("AFFICHAGE DES COURTIERS ET DU NB DE CLIENTS");
		        System.out.println(bourse.getConnexion().toString());
		        System.out.println();
			}




			//le courtier notifie la bourse qu'il possede un nv client
			else if (rep.equals("nvClient")) {
				System.out.println("le courtier " + nomCourtier +" possede un nouveau client");
				int nbconnexioncourtier = bourse.getConnexion().get(nomCourtier);
				bourse.getConnexion().put(nomCourtier, nbconnexioncourtier+1);
				System.out.println(bourse.getConnexion().toString());
            }
			//le courtier notifie la bourse qu'un de ses clients s'est déconnecté
			else if (rep.equals("rmClient")) {
				System.out.println("Un client du courtier " + nomCourtier +" a ferme sa journee");
				int nbconnexioncourtier = bourse.getConnexion().get(nomCourtier);
				bourse.getConnexion().put(nomCourtier, nbconnexioncourtier-1);
				System.out.println(bourse.getConnexion().toString());

				//verification si c'�tait le dernier client � se deconnecter et que tpus les courtiers sont egalement deconnecte alors on ferme la bourse
				if (bourse.getConnexion().get(nomCourtier)==0) {
					boolean ledernier=true;
					for (Map.Entry<Integer, Integer> c : bourse.getConnexion().entrySet()) {
						if (c.getValue()!=0) {
							ledernier = false;
							break;
						}

					}
					if (ledernier) bourse.setEtat("ferme");
				}
			}
			//le courtier envoie une commande client � la bourse
			else if (rep.equals("commander")) {
				Commande commande = (Commande)inObject.readObject();
				for (Societe soc : bourse.getSocietes()) {
					if (soc.getNomSociete().equals(commande.getNomSociete())){
						soc.getCommandes().add(commande);
					}
				}

			}





            in.close();
            out.close();
            outObject.close();
            inObject.close();
            ssv.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    // Vérifier le client
    // si le client se reconnecte, ila le droit de vérifier son vote
}
