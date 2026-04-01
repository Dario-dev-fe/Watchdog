# Watchdog Selenium App

Una applicazione Java che utilizza Selenium WebDriver per monitorare e controllare automaticamente un browser Chrome, con un'interfaccia utente Swing per gestire l'automazione.

## Funzionalità

- **Automazione Web**: Avvia un'istanza di Chrome e naviga a un URL specificato
- **Watchdog**: Monitora continuamente la pagina e chiude automaticamente popup e finestre indesiderate
- **Interfaccia Utente**: UI Swing con tema scuro personalizzato per controllare start/stop dell'automazione
- **Thread Sicuro**: Gestione thread-safe delle operazioni di automazione

## Prerequisiti

Prima di eseguire il progetto, assicurati di avere installato:

- **Java JDK 25** o superiore
- **Apache Maven 3.6+**
- **Google Chrome** (versione recente)

## Installazione e Setup

1. **Clona il repository**:
   ```bash
   git clone https://github.com/tuo-username/Watchdog.git
   cd Watchdog/Selenium
   ```

2. **Compila il progetto**:
   ```bash
   mvn clean compile
   ```

3. **Crea il JAR eseguibile**:
   ```bash
   mvn package
   ```

## Come Usare

1. **Esegui l'applicazione**:
   ```bash
   java -jar target/SeleniumApp.jar
   ```

2. **Interfaccia Utente**:
   - Nella finestra "WATCHDOG CONTROL" vedrai un campo di testo con un URL predefinito
   - Modifica l'URL se necessario (default: https://www.animeunity.so/)
   - Premi il pulsante **START** per avviare l'automazione
   - Chrome si aprirà automaticamente e inizierà il monitoraggio
   - Premi **STOP** per fermare l'automazione e chiudere il browser

## Come Funziona

L'applicazione è strutturata in tre componenti principali:

### DriverService
- Gestisce l'istanza di WebDriver Chrome
- Avvia e ferma l'automazione
- Coordina il thread watchdog

### UserInterfaceController
- Crea e gestisce l'interfaccia Swing
- Gestisce gli eventi dei pulsanti start/stop
- Applica lo styling personalizzato con font e colori

### App (Main)
- Punto di ingresso dell'applicazione
- Istanzia i servizi e avvia l'UI

### Thread Watchdog
Il thread di monitoraggio:
- Identifica la finestra principale del browser
- Chiude automaticamente nuove finestre/popup che si aprono
- Continua a monitorare finché l'automazione è attiva
- È configurato come daemon thread per una chiusura pulita

## Note Tecniche

- Utilizza Selenium WebDriver 4.35.0
- Supporta solo Chrome come browser
- L'automazione è thread-safe con AtomicBoolean
- L'UI utilizza un font personalizzato (HACKED.ttf) per il tema cyberpunk
- Timeout implicito di 10 secondi per le operazioni WebDriver

## Troubleshooting

- **Errore "ChromeDriver not found"**: Assicurati che Chrome sia installato e aggiornato
- **Porta già in uso**: Chiudi eventuali istanze di Chrome esistenti
- **Problemi di compilazione**: Verifica che JDK 25 sia correttamente installato e configurato

## Licenza

[Inserisci qui la tua licenza, es. MIT]