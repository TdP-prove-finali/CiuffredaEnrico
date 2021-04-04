# Applicazione per simulazione campionato F1
## Istruzioni per l'installazione dell'applicazione

1. Effettuare il fork del repository oppure scaricarlo sotto forma di archivio ZIP.
2. Importare il progetto in Eclipse, tramite l'archivio scaricato oppure copiando la URI del repository creata nel proprio GitHub a seguito del fork.
3. Eseguire lo script `f1db2020db.sql` all'interno dell'archivio ZIP omonimo contenuto nella cartella `Database` tramite il proprio DBMS.
4. Inserire la password del DBMS utilizzato nel campo `setPassword` all'interno della classe `ConnectDB`.
5. Eseguire la classe `Main` per avviare l'applicazione.

Video dimostrativo sull'uso dell'applicazione disponibile al link: 

## Dataset
A scopo di esempio e per testare le funzionalità dell'applicazione sono stati utilizzati i dati riguardanti tutte le stagioni fino alla fine del campionato di F1 del 2020. Tali dati sono stati estratti al seguente indirizzo: http://ergast.com/mrd/db/. Allo script sql, rilasciato con licenza Attribution-NonCommercial-ShareAlike 3.0 Unported Licence, è poi necessario aggiungere uno script sql che comprende una tabella per assegnare i punteggi ad ogni pilota ed una tabella che riporta gli importi spese dalle scuderie.
Nella directory "db" è possibile trovare un file in formato sql che contiene al suo interno sia i dati delle prestazioni sia i dati riguardanti il punteggio dei piloti e le spese dei vari team (f1db2020); è inoltre disponibile anche il solo esempio dei punteggi e delle spese ed è anche disponibile un altro file utile se si ha già un database chiamato "F1".

*Enrico Ciuffreda*
