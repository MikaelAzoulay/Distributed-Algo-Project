import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;


public class ThreadBourseCourtier extends Thread {

    Socket ssv;
    PrintWriter out;
    BufferedReader in;
    Bourse bourse;

    ThreadBourseCourtier(Socket socket , Bourse bourse) {
        ssv = socket;
        this.bourse= bourse;

    }

    @Override
    public void run() {
    	
        try {
        	
            in = new BufferedReader(new InputStreamReader(ssv.getInputStream()));
            out = new PrintWriter(ssv.getOutputStream(), true);


            String rep=in.readLine();
            System.out.println ("inscription du courtier en cours");

			// si le courtier veut s'inscrire
			if (rep.equals("1")) {
				
				out.println("Envoyez moi votre nom");//envoi le message au courtier

				String nom = in.readLine();
				System.out.println("nom du courtier " + nom);
				int nomCourtier = Integer.parseInt(nom);
				bourse.connexion.put(nomCourtier, 0);
	            out.println("inscription courtier terminee");
	            System.out.println(bourse.connexion.toString());
	            
	            for (int i=0;i<2;i++) {
	            String message = in.readLine();
	            if (message.equals("nouveau client inscrit")) {
	            	String nomCourtierAActualiser =in.readLine();
					System.out.println("le courtier " + nomCourtierAActualiser +" possede un nouveau client");
					int nc= Integer.parseInt(nomCourtierAActualiser);
					int nbconnexioncourtier = bourse.connexion.get(nc);
					int nouveaunb=nbconnexioncourtier+1;
					bourse.connexion.put(nc, nouveaunb);
					System.out.println(bourse.connexion.toString());
	            }
	            }




             }



            in.close();
            out.close();
            ssv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Vérifier le client
    // si le client se reconnecte, ila le droit de vérifier son vote
}
