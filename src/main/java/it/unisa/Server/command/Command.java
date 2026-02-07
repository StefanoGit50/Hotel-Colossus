package it.unisa.Server.command;

import it.unisa.Server.IllegalAccess;

/**
 * Interfaccia Command - usata nel design pattern: Command.
 * Rappresenta un comando i cui metodi (execute - undo) vanno implementati
 * per poter ottenere un comando che è anche annullabile.
 */
public interface Command {
    /**
     * Esecuzione del comando.
     */
    void execute() throws IllegalAccess;

    /**
     * Annullamento del comando. Lo stato del sistema è uguale allo
     * stato precedente all'esecuzione del comando.
     */
    void undo();
}