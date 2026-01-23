package Server.command;


import it.unisa.Server.command.Command;
import it.unisa.Server.command.Invoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestInvoker {

    @Mock
    Stack<Command> undoStack;

    @Mock
    Command c;
    @InjectMocks
    Invoker invoker;

@Test
    @DisplayName("undoCommandTest stack != da vuoto")
    void undoCommandTest_Void() {
      when(undoStack.isEmpty()).thenReturn(true);
      assertThrows(RuntimeException.class, () -> invoker.undoCommand());
    }

    @Test
    @DisplayName("undoCommandTest stack != da vuoto")
    void undoCommandTest_NotVoid() {
        when(undoStack.isEmpty()).thenReturn(false);
        when(undoStack.pop()).thenReturn(c);
        doNothing().when(c).undo();
        invoker.undoCommand();
    }



}
