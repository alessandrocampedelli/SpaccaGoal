package classi;
import java.io.FileInputStream;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;
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

//la classe astratta "Gara" che accomuna i campi e metodi uguali della classe "Partita" e "Torneo"
public abstract class Gara 
{
	//campi protected con le informazioni dei giocatori che prendono parte alla gara, il codice e il mazzo di carte della gara
	protected Giocatore[] giocatori;
	protected String codice;
	protected Mazzo carte;
	//campo protected per effettuare il salvataggio della partita corrente
	protected Salvataggio s;
	//creo l'oggetto "Leaderboard" per creare la tabella
	protected Leaderboard tabella = new Leaderboard();
	
	//metodo costruttore della classe gara dove vengono passati come parametri l'arrayList dei giocatori e il codice della gara
	public Gara(ArrayList<Giocatore> giocatori, String codice)
	{
		//converto l'arrayList in un vettore perchè il numero di giocatori è fisso
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = codice;
		//instanzio un oggetto della classe mazzo
		this.carte = new Mazzo();
		this.s = new Salvataggio(this);
	}

	public String getCodiceGara()
	{
		return this.codice;
	}

	public Giocatore[] getGiocatori()
	{
		return this.giocatori;
	}
	
	public Mazzo getMazzo() 
	{
		return carte;
	}
	
	public void setMazzo(Mazzo m) 
	{
		this.carte = m;
	}
	
	//metodo che fa ritornare la stringa con la classifica finale con i punteggi di tutti i giocatori della gara
	public String mostraRisultati() 
	{
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : giocatori) 
		{
			output += g.getAlias()+ ": " + g.getPunteggio() + "\n";
		}
		return output;
	}
	
	//metodo per inviare la mail con i risultati della partita al termine di essa
	public void inviaMail(Giocatore[] giocatori) 
	{
		//indirizzo email e password dell'account mittente
		final String username = "spaccagooal@gmx.com";
		final String password = "N2U73GGRZ2PFIIMBSSIX";
	
		//proprietà per la configurazione del server di posta
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.gmx.com");
		props.put("mail.smtp.port", "587");
	
		//crea un oggetto Session con l'autenticazione
		Session session = Session.getInstance(props, new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(username, password);
			}
		});
	
		try
		{ 
			LocalDateTime dataEOra = LocalDateTime.now();
			LocalDate data = dataEOra.toLocalDate();
			LocalTime ora = dataEOra.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
			//creazione di una parte per l'allegato
			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentPart.attachFile(getPdf(data,ora));
			
			for(Giocatore g : giocatori) 
			{
				//creazione del messaggio
				Message message = new MimeMessage(session);
				//impostazione dell'indirizzo email del mittente
				message.setFrom(new InternetAddress(username));
				//aggiunta degli indirizzi email dei destinatari
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(g.getEmail()));
				//oggetto della mail
				message.setSubject("SPACCA GOAL - RISULTATI PARTITA");
	
				//creazione di una parte di testo del messaggio
				BodyPart messageBodyPart = new MimeBodyPart();
				//caso di una partita singola
				String txtEmail = "";
				if (this.codice.charAt(0) == 'p') 
				{
				    txtEmail = "Ciao, " + g.getAlias() + "!</p>" +
				            "<p>Ecco a te i <b>risultati della partita '" + this.codice + "'</b> " +
				            "terminata in data " + data + " alle ore " + ora + ".</p>" +
				            "<p><b>" + this.mostraRisultati() + "</b></p>" +
				            "<p>In allegato il file pdf della leaderboard aggiornata.</p>" +
				            "<p>Grazie per aver giocato a SPACCA GOAL. A presto!</p>";
				} 
				else if (this.giocatori.length != 2) 
				{
				    txtEmail = "Ciao, " + g.getAlias() + "!</p>" +
				            "<p>Ecco a te i <b>risultati della partita relativa al torneo '" + this.codice + "'</b> " +
				            "terminata in data " + data + " alle ore " + ora + ".</p>" +
				            "<p><b>" + this.mostraRisultati() + "</b></p>" +
				            "<p>Grazie per aver giocato a SPACCA GOAL. A presto!</p>";
				} 
				else 
				{
				    txtEmail = "Ciao, " + g.getAlias() + "!</p>" +
				            "<p>Ecco a te i <b>risultati della FINALE del TORNEO '" + this.codice + "'</b> " +
				            "terminata in data " + data + " alle ore " + ora + ".</p>" +
				            "<p><b>" + this.mostraRisultati() + "</b></p>" +
				            "<p>In allegato il file pdf della leaderboard aggiornata.</p>" +
				            "<p>Grazie per aver giocato a SPACCA GOAL. A presto!</p>";
				}
				messageBodyPart.setContent(txtEmail, "text/html; charset=utf-8");
	
				//creazione di un oggetto Multipart per contenere il testo e l'allegato
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(attachmentPart);
	
				//impostazione del contenuto del messaggio come il Multipart
				message.setContent(multipart);
				//invia il messaggio
				//Transport.send(message);
			}
		} 
		catch(MessagingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//metodo che permette di creare un file pdf
	public String getPdf(LocalDate date, LocalTime ora) 
	{
		//crea un nuovo documento PDF
		PDDocument document = new PDDocument();
		String path = "";
		try 
		{ 
			Leaderboard nuovaTabella = new Leaderboard();
			String[][] data = nuovaTabella.toMatrix();
			//aggiunge una nuova pagina al documento
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
	
			//crea un nuovo stream di contenuto per la pagina
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			drawTable(data, document, page, contentStream,date,ora);
			//chiude lo stream di contenuto
			contentStream.close();
			path = System.getProperty("user.dir")+"/leaderboard.pdf";
			//salva il documento su disco
			document.save(path);
	
			//chiude il documento
			document.close();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		return path;
	}
	
	public void drawTable(String[][] data,PDDocument document ,PDPage page, PDPageContentStream contentStream,LocalDate date, LocalTime ora) 
	{
		try 
		{
			float margin = 50;
			float yStart = page.getMediaBox().getHeight() - margin;
			float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
			float yPosition = yStart;
			//altezza delle celle
			float tableHeight = 20f; 
			float rowHeight = tableHeight / data.length;
	
			String path = System.getProperty("user.dir");
			PDType0Font font_bold = PDType0Font.load(document, new FileInputStream(path+ "/Roboto-Bold.ttf"));
			PDType0Font font_regular = PDType0Font.load(document, new FileInputStream(path+ "/Roboto-Regular.ttf"));
			contentStream.setFont(font_bold, 12);
	
			//aggiungi la frase prima della matrice
			contentStream.beginText();
			contentStream.newLineAtOffset(margin, yPosition);
			contentStream.showText("Leaderboard aggiornata in data "+date+" - "+ora);
			contentStream.newLine();
			contentStream.endText();
			//aggiungi uno spazio tra la frase e la matrice
			yPosition -= 20; 
			contentStream.setFont(font_regular, 12);
			//stampa la matrice
			for (int i = 0; i < data.length; i++) 
			{
				float nextY = yPosition - rowHeight;
				contentStream.beginText();
				contentStream.newLineAtOffset(margin, yPosition);
	
				for (int j = 0; j < data[i].length; j++) 
				{
					contentStream.showText(data[i][j]);
					contentStream.newLineAtOffset(tableWidth / data[i].length, 0);
				}
	
				contentStream.endText();
				if(i!=0)
				{
					//aggiungi uno spazio di 10 punti tra le righe
					yPosition = nextY - 10; 
				}
				else
				{
					yPosition = nextY - 20;
				}
			}
	
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
}