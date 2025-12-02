package org.surino.untraceableminus.view;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.springframework.stereotype.Component;
import org.surino.untraceableminus.ImportExportService;
import org.surino.untraceableminus.PersonRepository;
import org.surino.untraceableminus.model.Person;
import org.surino.untraceableminus.model.Status;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
@Component
public class PersonPanel extends JPanel {

    private final PersonRepository personRepository;
    private final ImportExportService importExportService;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField addressField;
    private JComboBox<Status> statusCombo;
    private JTextField notesField;
    private JTextField searchField;

    private JTable table;
    private PersonTableModel tableModel;
    private List<Person> allPeople;

    public PersonPanel(PersonRepository personRepository, ImportExportService importExportService) {
        this.personRepository = personRepository;
        this.importExportService = importExportService;
        initUI();
        loadPeople();
    }

    private void initUI() {
        setLayout(new MigLayout("fillx, insets 10", "[grow, fill]", "[]10[]10[grow]"));

        // 🔹 FORM PANEL
        JPanel formPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill][][grow,fill]"));
        nameField = new JTextField(15);
        surnameField = new JTextField(15);
        addressField = new JTextField(20);
        notesField = new JTextField(30);
        statusCombo = new JComboBox<>(Status.values());


        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField, "wrap");
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Notes:")); 
        formPanel.add(notesField, "wrap");
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo, "wrap");

        // 🔹 BUTTONS
        JButton saveButton = new JButton("💾 Save");
        JButton deleteButton = new JButton("🗑️ Delete Selected");
        JButton importButton = new JButton("📥 Import CSV");
        JButton exportButton = new JButton("📤 Export CSV");

        saveButton.addActionListener(e -> savePerson());
        deleteButton.addActionListener(e -> deletePerson());
        importButton.addActionListener(e -> importCSV());
        exportButton.addActionListener(e -> exportCSV());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);

        formPanel.add(buttonPanel, "span, growx, wrap");
        add(formPanel, "growx, wrap");

        // 🔹 SEARCH PANEL
        JPanel searchPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill]"));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("🔍 Search:"));
        searchPanel.add(searchField, "growx");
        add(searchPanel, "growx, wrap");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });

        // 🔹 TABLE
        tableModel = new PersonTableModel(personRepository);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        
     // --- Popup menu ---
        JPopupMenu popupMenu = new JPopupMenu();

        // esempio: elimina persona
        JMenuItem deleteItem = new JMenuItem("Elimina");
        deleteItem.addActionListener(e -> deletePerson());
         popupMenu.add(deleteItem);
         
         JMenuItem swapItem = new JMenuItem("Scambia Nome/Cognome");
         swapItem.addActionListener(e -> {
             int row = table.getSelectedRow();
             if (row != -1) {
                 int modelRow = table.convertRowIndexToModel(row);
                 Person p = tableModel.getPersonAt(modelRow);

                 // swap
                 String tmp = p.getName();
                 p.setName(p.getSurname());
                 p.setSurname(tmp);

                 // notifica il modello che i dati sono cambiati
                 personRepository.save(p);;
             }
         });
         popupMenu.add(swapItem);
         
         
        
     // collega il menu alla tabella
        table.setComponentPopupMenu(popupMenu);
        
     // opzionale: seleziona automaticamente la riga con il click destro
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) selectRow(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) selectRow(e);
            }
            private void selectRow(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) {
                    table.setRowSelectionInterval(row, row);
                }
            }
        });
        
        
        JComboBox<Status> comboBoxEditor = new JComboBox<>(Status.values());
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBoxEditor));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow, push");
    }

    private void savePerson() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String address = addressField.getText().trim();
        String notes = notesField.getText().trim();
        Status status = (Status) statusCombo.getSelectedItem();

        if (name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Surname are required!");
            return;
        }

        // 🔹 Controllo duplicati
        List<Person> duplicates = personRepository.findAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name)
                        && p.getSurname().equalsIgnoreCase(surname))
                .collect(Collectors.toList());

        if (!duplicates.isEmpty()) {
            String dupInfo = duplicates.stream()
                    .map(p -> "- " + p.getName() + " " + p.getSurname() +
                            (p.getAddress() != null && !p.getAddress().isEmpty() ? " (" + p.getAddress() + ")" : ""))
                    .collect(Collectors.joining("\n"));

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "A person with the same name and surname already exists:\n\n" + dupInfo +
                            "\n\nDo you want to save anyway?",
                    "Duplicate Detected",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        personRepository.save(new Person(name, surname, address,  notes, status));

        nameField.setText("");
        surnameField.setText("");
        addressField.setText("");
        notesField.setText("");
        statusCombo.setSelectedIndex(0);
        loadPeople();
    }

    private void deletePerson() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a person to delete!");
            return;
        }

        Person p = tableModel.getPersonAt(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Do you really want to delete " + p.getName() + " " + p.getSurname() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            personRepository.delete(p);
            loadPeople();
        }
    }

    private void loadPeople() {
        allPeople = personRepository.findAll();
        tableModel.setData(allPeople);
    }

    private void filter() {
        if (allPeople == null) return;
        String filter = searchField.getText().toLowerCase();

        if (filter.isEmpty()) {
            tableModel.setData(allPeople);
        } else {
            List<Person> filtered = allPeople.stream()
                    .filter(p -> p.getName().toLowerCase().contains(filter)
                            || p.getSurname().toLowerCase().contains(filter)
                            || (p.getAddress() != null && p.getAddress().toLowerCase().contains(filter)))
                    .collect(Collectors.toList());
            tableModel.setData(filtered);
        }
    }

    // 🔹 Deleghe al servizio
    private void importCSV() {
        List<Person> imported = importExportService.importFromCSV(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Import People from CSV");
        importExportService.importWithDuplicatesCheck(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                imported);
        loadPeople();
    }

    private void exportCSV() {
        importExportService.exportToCSV((JFrame) SwingUtilities.getWindowAncestor(this));
    }


}
