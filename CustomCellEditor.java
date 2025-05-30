import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CustomCellEditor extends DefaultCellEditor {
    public CustomCellEditor() {
        super(new JTextField());
    }

    @Override
    public boolean stopCellEditing() {
        String value = ((JTextField)getComponent()).getText().trim();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cell cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false; 
        }
        return super.stopCellEditing();
    }
}
