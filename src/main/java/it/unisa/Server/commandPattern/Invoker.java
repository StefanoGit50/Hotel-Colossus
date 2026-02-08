// Invoker
package it.unisa.Server.commandPattern;

import it.unisa.Server.Eccezioni.IllegalAccess;

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
     * Stack di undo dei comandi.
     */
    private Stack<Command> undoStack = new Stack<>();

    /**
     * Stack di redo dei comandi. Si svuota ogni qual volta si esegue un nuovo comando.
     */
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Esegue il comando passato come parametro esplicito, lo aggiunge allo stack e svuota lo stack {@code redoStack}.
     *
     * @pre c != null
     * @post undoStack.contains(c) && redoStack->isEmpty()
     *
     * @param c Comando da eseguire.
     */
    public void executeCommand(Command c) throws IllegalAccess {
        undoStack.push(c);
        c.execute();
        redoStack.clear(); // Invalida il redo dopo nuova azione
    }
    /**
     * Annulla l'ultimo comando eseguito.
     *
     * @pre not undoStack->isEmpty()
     * @post redoStack.size() == redoStack@pre.size() + 1
     *
     */
    public void undoCommand() {
        if (!undoStack.isEmpty()) {
            Command c = undoStack.pop();
            c.undo();
            redoStack.push(c);
        }
    }

    /**
     * Riapplica le modifiche del comando in allo stack {@code undoStack}.
     *
     * @pre not redoStack->isEmpty()
     * @post undoStack.size() == undoStack@pre.size() + 1
     *
     */
    public void redo()  {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            try {
                command.execute();
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
            undoStack.push(command);
        }
    }
}
