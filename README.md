# Applicazione per simulazione campionato F1
## Istruzioni per l'installazione dell'applicazione
1. Forkare e clonare sul proprio editor (ad esempio Eclipse) il progetto al link: https://github.com/TdP-prove-finali/CiuffredaEnrico
2. Importare sul proprio server locale il database "F1" situato all'interno della cartella "database" eseguendo lo script 'f1db2020db.sql"
3. Modificare, all'interno della classe ConnectDB, la password di accesso al Database
4. Aprire il progetto e lanciare la classe Main
5. Per una breve guida introduttiva, guarda il video al link: https://youtu.be/sgw8gFQJ-VA

## Dataset
A scopo di esempio e per testare le funzionalità dell'applicazione sono stati utilizzati i dati riguardanti tutte le stagioni fino alla fine del campionato di F1 del 2020. Tali dati sono stati estratti al seguente indirizzo: http://ergast.com/mrd/db/. Allo script sql, rilasciato con licenza Attribution-NonCommercial-ShareAlike 3.0 Unported Licence, è poi necessario aggiungere uno script sql che comprende una tabella per assegnare i punteggi ad ogni pilota ed una tabella che riporta gli importi spese dalle scuderie.
Nella directory "db" è possibile trovare un file in formato sql che contiene al suo interno sia i dati delle prestazioni sia i dati riguardanti il punteggio dei piloti e le spese dei vari team (f1db2020); è inoltre disponibile anche il solo esempio dei punteggi e delle spese ed è anche disponibile un altro file utile se si ha già un database chiamato "F1".

*Enrico Ciuffreda*
