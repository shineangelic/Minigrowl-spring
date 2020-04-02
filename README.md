#Formazione e Lavoro Kiosk/Totem
Software di recupero informazioni smart-card e invocazione WS lxt.

E` un progetto maven, lanciandolo si avvia un runtime che risponde su localhost:8080 ad uso e consumo di un front-end abbinato.

application.properties contiene le variabili per la configurazione. Sono previste tre modalita di esecuzione, `DEBUG`, `TEST` e `PRODUCTION`. La prima funziona sempre, la seconda richiede MyPortal su plutone:41000 mentre l'ultima è quella finale.

#Compilazione back-end

1. git clone
2. apri eclipse ed importa il progetto come "import existing maven project"
3. lanciare l'applicazione come applicazione java standard. Alternativamente, è possibile utilizzare l'esecuzione **mvn spring-boot:run**
4. (opzionale) includere il compilato del front-end. All'interno di `resources/static` si trovano le risorse compilate della UI. Possono essere sovrascritte con una versione più aggiornata dopo aver compilato il progetto front-end con yarn/npm.
5. Visitare http://localhost:8080/

##Rimozione Warning smart-card
The solution is to change the access restrictions.

Go to the properties of your Java project,
i.e. by selecting "Properties" from the context menu of the project in the "Package Explorer".
Go to "Java Build Path", tab "Libraries".
Expand the library entry
select
"Access rules",
"Edit..." and
"Add..." a "Resolution: Accessible" with a corresponding rule pattern. For me that was "javax/smartcardio/**", for you it might instead be "com/apple/eawt/**".

##Versioni


###0.1.1
Bugfixes, timeout scheda

###0.1.0 
Versione installata 10/2019 per avvio pilota a Bologna

##Analisi log

Su http://gitlab-sil.bo.eng.it/grupposil/internal-tools è disponibile un tool di log-analisi


