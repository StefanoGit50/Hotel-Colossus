// Invoker
package it.unisa.Server.command;

import java.util.Objects;
import java.util.Stack;

/*
// Invoker: esegue i metodi nell'ordine in cui li riceve nello stack
// Command: interfaccia execute() e undo()
// ConcreteCommand: SetStatoInPuliziaCommand - Comando per la creazione di una prenotazione
// Reciever: Catalogo delle camere: questa classe implementa solamente getter e setter mentre il resto della logica
è implementata dai comandi
// Client: Può usare i ConcreteCommand forniti dall'applicazione e li esegue tramite l'invoker
 */

/**
 * La classe invoker è la classe delegata all'esecuzione dei comandi.
 * Presenta uno stack che tiene traccia dell'ordine di esecuzione dei comandi.
 */
public class Invoker {
    /**
     * Stack di esecuzione dei comandi.
     */
    private Stack<Command> stackOperazioni = new Stack<>();

    /**
     * Esegue il comando passato come parametro esplicito e lo aggiunge allo stack.
     * @param c Comando da eseguire.
     */
    public void executeCommand(Command c) {
        stackOperazioni.push(c);
        c.execute();
    }
    /**
     * Annulla l'ultimo comando eseguito.
     */
    public void undoCommand() {
        if (!stackOperazioni.isEmpty()) {
            Command c = stackOperazioni.pop();
            c.undo();
        }
    }
}
