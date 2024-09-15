import com.ufc.molic.dao.AnotacaoDAOImpl;
import com.ufc.molic.editor.NotesPanel;
import com.ufc.molic.entity.Anotacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotesTest {

    @Mock
    private AnotacaoDAOImpl anotacaoDAO;

    @InjectMocks
    private NotesPanel notesPanel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNote() {
        doNothing().when(anotacaoDAO).save(any(Anotacao.class));

        notesPanel.addNote();

        DefaultTableModel model = (DefaultTableModel) notesPanel.notesTable.getModel();
        assertEquals(1, model.getRowCount());

        verify(anotacaoDAO, times(1)).save(any(Anotacao.class));
    }

    @Test
    void testEditNote() {
        Anotacao anotacao = new Anotacao("Anotação existente");
        when(anotacaoDAO.find("Anotação existente")).thenReturn(anotacao);

        notesPanel.notesModel.addRow(new Object[]{1, "Anotação existente"});
        notesPanel.notesTable.setRowSelectionInterval(0, 0);
        notesPanel.editNote();

        verify(anotacaoDAO, times(1)).update(any(Anotacao.class));
    }

    @Test
    void testDeleteNote() {

        Anotacao anotacao = new Anotacao("Anotação a ser excluída");
        anotacao.setId(1);
        when(anotacaoDAO.find("Anotação a ser excluída")).thenReturn(anotacao);

        notesPanel.notesModel.addRow(new Object[]{1, "Anotação a ser excluída"});
        notesPanel.notesTable.setRowSelectionInterval(0, 0);
        notesPanel.deleteNote();

        verify(anotacaoDAO, times(1)).delete(1);

        assertEquals(0, notesPanel.notesModel.getRowCount());
    }

    @Test
    void testLoadNotes() {

        when(anotacaoDAO.find()).thenReturn(Arrays.asList(
                new Anotacao("Primeira anotação"),
                new Anotacao("Segunda anotação")
        ));

        notesPanel.loadNotes();

        DefaultTableModel model = (DefaultTableModel) notesPanel.notesTable.getModel();
        assertEquals(2, model.getRowCount());
        assertEquals("Primeira anotação", model.getValueAt(0, 1));
        assertEquals("Segunda anotação", model.getValueAt(1, 1));
    }

    @Test
    void testRenumberIds() {

        notesPanel.notesModel.addRow(new Object[]{1, "Anotação 1"});
        notesPanel.notesModel.addRow(new Object[]{2, "Anotação 2"});

        notesPanel.notesModel.removeRow(0);

        notesPanel.renumberIds();

        assertEquals(1, notesPanel.notesModel.getValueAt(0, 0));
    }
}
