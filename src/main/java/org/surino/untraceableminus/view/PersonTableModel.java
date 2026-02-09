package org.surino.untraceableminus.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.surino.untraceableminus.model.Person;
import org.surino.untraceableminus.model.PersonRepository;
import org.surino.untraceableminus.model.Status;

@SuppressWarnings("serial")
public class PersonTableModel extends AbstractTableModel {

    private final PersonRepository personRepository;

    public PersonTableModel(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private final String[] columnNames = { "Name", "Surname", "Address", "Status", "Note"};
    private List<Person> data;

    public void setData(List<Person> people) {
        this.data = people;
        fireTableDataChanged();
    }

    public Person getPersonAt(int row) {
        return data.get(row);
    }

    @Override public int getRowCount() { return data == null ? 0 : data.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }

    /**
     * Convertito da "Switch Expression" (Java 17) a "Switch Statement" (Java 8)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person p = data.get(rowIndex);
        Object result; // Variabile per il ritorno

        switch (columnIndex) {
            case 0:
                result = p.getName();
                break;
            case 1:
                result = p.getSurname();
                break;
            case 2:
                result = p.getAddress();
                break;
            case 3:
                result = p.getStatus();
                break;
            case 4:
                result = p.getNotes();
                break;

            default:
                result = null;
        }
        return result;
    }

    @Override public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * Convertito da "Switch Expression" (Java 17) a "Switch Statement" (Java 8)
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Person p = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                p.setName((String) value);
                break; 
            case 1:
                p.setSurname((String) value);
                break;
            case 2:
                p.setAddress((String) value);
                break;
            case 3:
                p.setStatus((Status) value);
                break;
            case 4:
                p.setNotes((String) value);
                break;
            // Nessun default necessario, come nell'originale
        }
        personRepository.save(p);
    }

    /**
     * Convertito da "Switch Expression" (Java 17) a "Switch Statement" (Java 8)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> columnClass; // Variabile per il ritorno

        switch (columnIndex) {

            case 3:
                columnClass = Status.class;
                break;
            default:
                columnClass = String.class;
        }
        return columnClass;
    }
}