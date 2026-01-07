Progetto universitario per l'esame di Laboratorio Interdisciplinare B – Università degli Studi dell’Insubria (2025)

Sistema client–server che permette di gestire un database di libri, valutazioni e librerie personali, con interfaccia grafica JavaFX e database su PostgreSQL.

Manuale Tecnico e manuale utente sono disponibili nella directory /doc.

Autori:
  Diana Cagnetta 757000 VA: Project Manager
  Aurora Lindangela Romano 757787 VA: System Architect
  Riccardo Fedeli 758019 VA: Document and Quality Manager
  Fabio Calliaro 756230 VA: Design Manager

Per il corretto funzionamento del tool è necessario avere una versione di JDK 17 o superiore.
il processo di build è gestito tramite Apache Maven e può essere avviato mediante l'esecuzione del comando 'mvn clean install'.

L’applicazione può essere avviata secondo due modalità operative distinte e la 
procedura d’avvio varia a seconda del sistema operativo in uso.  
L’avvio dell’applicazione avviene semplicemente eseguendo lo script corrispondente 
tramite doppio clic.  
Gli script disponibili sono i seguenti:

  • Client.bat, per l’avvio del modulo client  
  • Server.bat, per l’avvio del modulo server 
  
Per la configurazione e l’inizializzazione del database seve un collegamento ad 
internet e verrà fatto tramite l’inserimento di dati d’accesso    
In alternativa, è possibile eseguire manualmente i file JAR generati durante la fase di 
build, accedendo alla directory /bin. I punti di ingresso principali dell’applicazione 
sono le classi ClientMain e ServerMain, responsabili rispettivamente 
dell’inizializzazione del client e del server.  
Una volta avviato, il modulo server stabilisce una connessione con il database 
PostgreSQL precedentemente configurato tramite il modulo server. Successivamente, 
il modulo client può instaurare una connessione verso il server, consentendo all’utente finale di 
accedere alle funzionalità offerte dall’applicazione. 
Per i passaggi successivi fare riferimento al manuale utente.


NOTA FINALE
assicurarsi sempre che il server sia avviato prima di avviare il client.

