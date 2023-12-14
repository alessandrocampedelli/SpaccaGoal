package classi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.event.ActionEvent;

public class Partita extends Gara
{
	private final int N_CARTE_INIZIO = 5;
	private String turno;
	private int posizioneGiocatoreAttaccante;
	private int posizioneGiocatoreDifensore;
	private String nomeCarta;
	private int cartePescate;
	private Leaderboard tabella = new Leaderboard();
	public Partita(ArrayList<Giocatore> giocatori, String codice)
	{
		super(giocatori,codice);
	}

	public void distribuzioneCarte()
	{
		//pulisco le mani dei giocatori da eventuali partite precedenti
		pulisciMani();
		carte.mischia();
		//distribuzione delle carte
		for(int j = 0; j < this.giocatori.length; j++) 
		{
			for(int i = 0; i < N_CARTE_INIZIO; i++) 
			{
				giocatori[j].getMano().add(carte.pesca());
				//il primo giocatore deve pescare una carta in più
				if(i == 4 && j == 0)
				{
					giocatori[j].getMano().add(carte.pesca());
				}
			}
		}
		carte.mischia();
	}
	
	public String getTurno()
	{
		return this.turno;
	}
	
	public int getPosAttaccante()
	{
		return this.posizioneGiocatoreAttaccante;
	}
	
	public int getPosDifensore()
	{
		return this.posizioneGiocatoreDifensore;
	}
	
	public String getNomeCarta()
	{
		return this.nomeCarta;
	}

	private void pulisciMani() 
	{
		for(Giocatore g : giocatori)
			g.getMano().clear();
	}
	
	public int getCartePescate() 
	{
		return this.cartePescate;
	}
	
	public void setCartePescate(int cartePescate) 
	{
		this.cartePescate = cartePescate;
	}
	public void gioca(Giocatore att, Giocatore dif, Carta cartaAtt, Carta cartaDif) 
	{
		dif.getMano().add(this.carte.pesca());
		cartePescate = 1;
		//attaccante
		if(cartaAtt.equals(Carta.ATTACCANTE)) 
		{
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA)))
			{
				att.aggiungiGoal();
			}
			else 
			{
				dif.getMano().add(this.carte.pesca());
				cartePescate++;
			}
		}
		else 
		{
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					att.aggiungiGoal();
				}
				else 
				{
					dif.getMano().add(this.carte.pesca());
					cartePescate++;
				}
			}
			else 
			{
				//rovesciata dell'anno e tiro della domenica
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					att.aggiungiGoal();
				}
				else 
				{
					//goal
					if(cartaAtt.equals(Carta.GOAL)) 
					{
						if(!(cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) 
						{
							att.aggiungiGoal();
						}
						else 
						{
							dif.getMano().add(this.carte.pesca());
							cartePescate++;
						}
					}
					else 
					{
						if(cartaAtt.equals(Carta.MISTER)) 
						{
							att.getMano().add(this.carte.pesca());
							att.getMano().add(this.carte.pesca());
							cartePescate = 2;
						}
						else 
						{
							//caso del rigore
							if(!cartaDif.equals(Carta.PORTIERE)) 
							{
								att.aggiungiGoal();
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkGiocaTurno(Carta cartaGiocata, ArrayList<Carta> manoAvversario) 
	{
		if(cartaGiocata.equals(Carta.ROVESCIATA_DELLANNO) || cartaGiocata.equals(Carta.TIRO_DOMENICA) || cartaGiocata.equals(Carta.MISTER)) 
		{
			return false;
		}
		else 
		{
			for(Carta c : manoAvversario) 
			{
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.VAR) || c.equals(Carta.FUORIGIOCO))
					return true;
			}
			return false;
		}
	}
	
	public boolean checkGiocaTurno(Giocatore att) 
	{
		for(Carta c : att.getMano()) 
		{
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				return true;
		}
		return false;
	}
	
	public boolean finePartita(int iPosAtt) 
	{
		boolean fine = false;
		if(giocatori[iPosAtt].getPunteggio() == 5)
			fine = true;
		return fine;
	}
	
	public String mostraRisultati() 
	{
		return super.mostraRisultati();
	}
	
	public void leggiTurno() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "";
		if(codice.charAt(0) == 'p')
			relativePath = "src/partite/"+codice+"/turno.txt";
		else
			relativePath = "src/tornei/"+codice.substring(0,codice.length() - 1)+"/"+codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		Scanner scan = new Scanner(f);
		turno = scan.nextLine();
		posizioneGiocatoreAttaccante = scan.nextInt();
		posizioneGiocatoreDifensore = scan.nextInt();
		cartePescate = scan.nextInt();
		//devo dire che se va nel form rigore deve leggere anche la carta giocata
		if(turno.equals("d"))
		{
			nomeCarta = scan.next();
		}
		scan.close();
	}

	public void salvaTurno(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+this.codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter pw = new PrintWriter(absolutePath);
		
		pw.println("a");
		pw.println(0);
		pw.println(1);
		pw.println(1);
		pw.close();
	}
	
	public void salvaTurnoGara(Carta cartaGiocata, Carta cartaAtt) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(this.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		else
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			if(cartaGiocata.equals(Carta.RIGORE)) 
			{
				fw.println("a");
			}
			else 
			{
				fw.println("d");
			}
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			if(!(cartaGiocata == null)) 
			{
				if((cartaAtt.equals(Carta.RIGORE_SX)||cartaAtt.equals(Carta.RIGORE_C)||cartaAtt.equals(Carta.RIGORE_DX)) && (cartaGiocata.equals(Carta.PORTIERE ))) 
				{
					fw.println("d");
					fw.println(posizioneGiocatoreAttaccante);
					fw.println(posizioneGiocatoreDifensore);
					fw.println(cartePescate);
					fw.println(cartaAtt.name());
				}
			}
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}
	
	public void salvaTurnoRigore(Carta cartaGiocata) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath;
		if(this.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		else
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			fw.println("d");
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}
	public void inviaMail() {
		// Indirizzo email e password dell'account mittente
        final String username = "spaccagooal@gmx.com";
        final String password = "N2U73GGRZ2PFIIMBSSIX";

        // Proprietà per la configurazione del server di posta
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mail.gmx.com");
        props.put("mail.smtp.port", "587");

        // Crea un oggetto Session con l'autenticazione
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try{
        	for(Giocatore g : giocatori) 
        	{
        		// Creazione del messaggio
                Message message = new MimeMessage(session);
                // Impostazione dell'indirizzo email del mittente
                message.setFrom(new InternetAddress(username));
                // Aggiunta degli indirizzi email dei destinatari
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(g.getEmail()));
                // Oggetto della mail
                message.setSubject("SPACCA GOAL - RISULTATI PARTITA");


                LocalDateTime dataEOra = LocalDateTime.now();
                LocalDate data = dataEOra.toLocalDate();
                LocalTime ora = dataEOra.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
                // Creazione di una parte di testo del messaggio
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Ciao, "+g.getAlias()+"!\nEcco a te i risultati della partita '"+this.codice+"' "
                		+ "terminata in data "+data+" alle ore "+ora+".\n"+this.mostraRisultati()+"\nIn allegato il file pdf della leaderboard aggiornata.\n"+"Grazie per aver giocato a SPACCA GOAL. A presto!\n");

                // Creazione di una parte per l'allegato
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(getPdf(data,ora));

                // Creazione di un oggetto Multipart per contenere il testo e l'allegato
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentPart);

                // Impostazione del contenuto del messaggio come il Multipart
                message.setContent(multipart);
                // Invia il messaggio
                //Transport.send(message);
        	}
        } catch (MessagingException e) {
        	e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getPdf(LocalDate date, LocalTime ora) {
		// Crea un nuovo documento PDF
        PDDocument document = new PDDocument();
        String path = "";
		try { 
			String[][] data = tabella.toMatrix();
	        // Aggiunge una nuova pagina al documento
	        PDPage page = new PDPage(PDRectangle.A4);
	        document.addPage(page);

	        // Crea un nuovo stream di contenuto per la pagina
	        PDPageContentStream contentStream = new PDPageContentStream(document, page);
	        drawTable(data, document, page, contentStream,date,ora);
	        // Chiude lo stream di contenuto
	        contentStream.close();
	        path = System.getProperty("user.dir")+"/leaderboard.pdf";
	        // Salva il documento su disco
	        document.save(path);

	        // Chiude il documento
	        document.close();
		}catch(IOException e) {
			e.getMessage();
		}
        return path;
	}
	public void drawTable(String[][] data,PDDocument document ,PDPage page, PDPageContentStream contentStream,LocalDate date, LocalTime ora) {
		try {
			float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float tableHeight = 20f; // Altezza delle celle
            float rowHeight = tableHeight / data.length;

            String path = System.getProperty("user.dir") + "/Roboto-Regular.ttf";
            PDType0Font font = PDType0Font.load(document, new FileInputStream(path));
            contentStream.setFont(font, 12);

            // Aggiungi la frase prima della matrice
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Leaderboard aggiornata in data "+date+" - "+ora);
            contentStream.newLine();
            contentStream.endText();
            yPosition -= 20; // Aggiungi uno spazio tra la frase e la matrice

            // Stampa la matrice
            for (int i = 0; i < data.length; i++) {
                float nextY = yPosition - rowHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);

                for (int j = 0; j < data[i].length; j++) {
                    contentStream.showText(data[i][j]);
                    contentStream.newLineAtOffset(tableWidth / data[i].length, 0);
                }

                contentStream.endText();
                if(i!=0)
	            	yPosition = nextY - 10; // Aggiungi uno spazio di 10 punti tra le righe
	            else
	            	yPosition = nextY - 20;
            }

		}catch(IOException e) {
			e.getMessage();
		}
	}
	public void showFinePartita(ActionEvent event, String aliasVincente, Leaderboard leaderboard, Alert_cambiaForm alert) throws IOException
	{
		giocatori[posizioneGiocatoreAttaccante].aggiungiVittoriaPartita();
		leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaPartita();
		//aggiornata una vittoria nella leaderboard, risalvo il file di testo con i valori aggiornati
		leaderboard.salvaPlayers();
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
		inviaMail();
		//s.deleteDirectory("partite");
	}
	
	public String toString() 
	{
		String info = "";
		for(Giocatore p: this.getGiocatori()) 
		{
			info += "Giocatore "+p.getAlias() +(p.isRobot() ? " (Robot)" : "") + "\n Punteggio: "+p.getPunteggio()+"\n Mano: "+p.getMano().toString()+"\n\n";
		}
		return info;
	}
}