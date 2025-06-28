package id.ac.binainsani.bookinghotel.view.swing;

import id.ac.binainsani.bookinghotel.model.HotelRoom;
import id.ac.binainsani.bookinghotel.model.Transaction;
import id.ac.binainsani.bookinghotel.service.AuthService;
import id.ac.binainsani.bookinghotel.service.AuthServiceImpl;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;
import id.ac.binainsani.bookinghotel.service.TransactionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.TableCellRenderer;

public class MainFrame extends JFrame {

    private final AuthService authService;
    private final BookingService bookingService;
    private final HotelRoomService roomService;
    private final TransactionService txService;

    private JTabbedPane tabbedPane;
    private JTable transactionTable;
    private JTable roomTable;

    public MainFrame(AuthService authService, BookingService bookingService,
                     HotelRoomService roomService, TransactionService txService) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.txService = txService;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hotel Booking System - PT. Bina Insani");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();

        // Create tabs
        tabbedPane.addTab("Booking", createBookingPanel());
        tabbedPane.addTab("Rooms", createRoomsPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());
        tabbedPane.addTab("Admin", createAdminPanel());

        add(tabbedPane);
    }
    private void bookRoom(String name, String id, String phone, String email,
                     JComboBox<String> roomCombo, String checkInDate,
                     JSpinner durationSpinner, JTextField paymentField,
                     List<HotelRoom> availableRooms) {
    // Validate inputs
    if (name.isEmpty() || id.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Please fill all required guest information",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (roomCombo.getSelectedIndex() < 0) {
        JOptionPane.showMessageDialog(this,
                "Please select a room",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Get selected room
        String selected = (String) roomCombo.getSelectedItem();
        String roomNumber = selected.split(" - ")[0];
        
        HotelRoom room = availableRooms.stream()
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);

        if (room == null) {
            JOptionPane.showMessageDialog(this,
                    "Selected room is no longer available",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate total price
        int days = (Integer) durationSpinner.getValue();
        int totalPrice = room.getPrice() * days;
        int payment = Integer.parseInt(paymentField.getText());

        if (payment < totalPrice) {
            JOptionPane.showMessageDialog(this,
                    "Payment amount is less than total price",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int change = payment - totalPrice;

        // Create transaction
        Transaction transaction = new Transaction(
                "Booking",
                "HOTEL",
                roomNumber,
                days,
                totalPrice,
                payment,
                change
        );
        transaction.setCustomerName(name);

        // Save to database
        bookingService.bookHotelRoom(roomNumber, days, payment, name);

        // Update room availability
        roomService.updateRoomAvailability(roomNumber, false);

        // Show success message
        JOptionPane.showMessageDialog(this,
                "Booking successful!\n" +
                "Room: " + roomNumber + "\n" +
                "Check-in: " + checkInDate + "\n" +
                "Duration: " + days + " nights\n" +
                "Total: Rp " + totalPrice + "\n" +
                "Change: Rp " + change,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        // Refresh all tables and panels
        refreshRoomTable();
        refreshTransactionTable();
        
        // Switch to transactions tab
        tabbedPane.setSelectedIndex(2);
        
        // Refresh booking panel to show updated room availability
        tabbedPane.setComponentAt(0, createBookingPanel());

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Please enter a valid payment amount",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error processing booking: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}


    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Guest Information
        JPanel guestPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        guestPanel.setBorder(new TitledBorder("Guest Information"));

        guestPanel.add(new JLabel("Full Name:"));
        JTextField nameField = new JTextField();
        guestPanel.add(nameField);

        guestPanel.add(new JLabel("ID/Passport Number:"));
        JTextField idField = new JTextField();
        guestPanel.add(idField);

        guestPanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField();
        guestPanel.add(phoneField);

        guestPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        guestPanel.add(emailField);

        // Room Selection
        JPanel roomPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        roomPanel.setBorder(new TitledBorder("Room Selection and Payment"));

        // Get available rooms
        List<HotelRoom> availableRooms = roomService.getAllRooms()
                .stream()
                .filter(HotelRoom::isAvailable)
                .toList();

        roomPanel.add(new JLabel("Select Room:"));
        JComboBox<String> roomCombo = new JComboBox<>();
        for (HotelRoom room : availableRooms) {
            roomCombo.addItem(room.getRoomNumber() + " - " + room.getType());
        }
        roomPanel.add(roomCombo);

        roomPanel.add(new JLabel("Check-in Date:"));
        JTextField checkInField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        roomPanel.add(checkInField);

        roomPanel.add(new JLabel("Duration (nights):"));
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 30, 1);
        JSpinner durationSpinner = new JSpinner(spinnerModel);
        roomPanel.add(durationSpinner);

        roomPanel.add(new JLabel("Total Price:"));
        JTextField priceField = new JTextField();
        priceField.setEditable(false);
        roomPanel.add(priceField);

        // Calculate price when room or duration changes
        roomCombo.addActionListener(e -> updatePrice(roomCombo, durationSpinner, priceField, availableRooms));
        durationSpinner.addChangeListener(e -> updatePrice(roomCombo, durationSpinner, priceField, availableRooms));

        // Payment fields
        roomPanel.add(new JLabel("Payment Amount:"));
        JTextField paymentField = new JTextField();
        roomPanel.add(paymentField);

        roomPanel.add(new JLabel("Change:"));
        JTextField changeField = new JTextField();
        changeField.setEditable(false);
        roomPanel.add(changeField);

        // Calculate change when payment changes
        paymentField.addActionListener(e -> {
            try {
                int payment = Integer.parseInt(paymentField.getText());
                int total = Integer.parseInt(priceField.getText());
                int change = payment - total;
                changeField.setText(String.valueOf(change));
            } catch (NumberFormatException ex) {
                changeField.setText("");
            }
        });

        // Booking button
        JButton bookButton = new JButton("Confirm Booking");
        bookButton.setBackground(new Color(70, 130, 180));
        bookButton.setForeground(Color.WHITE);
        bookButton.addActionListener(e -> bookButton(
                nameField.getText(),
                idField.getText(),
                phoneField.getText(),
                emailField.getText(),
                roomCombo,
                checkInField.getText(),
                durationSpinner,
                paymentField,
                availableRooms
        ));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(bookButton);

        // Add components to main panel
        panel.add(guestPanel, BorderLayout.NORTH);
        panel.add(roomPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updatePrice(JComboBox<String> roomCombo, JSpinner durationSpinner,
                             JTextField priceField, List<HotelRoom> availableRooms) {
        if (roomCombo.getSelectedIndex() >= 0 && durationSpinner.getValue() != null) {
            String selected = (String) roomCombo.getSelectedItem();
            if (selected != null) {
                String roomNumber = selected.split(" - ")[0];
                HotelRoom room = availableRooms.stream()
                        .filter(r -> r.getRoomNumber().equals(roomNumber))
                        .findFirst()
                        .orElse(null);

                if (room != null) {
                    int days = (Integer) durationSpinner.getValue();
                    int total = room.getPrice() * days;
                    priceField.setText(String.valueOf(total));
                }
            }
        }
    }

    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Table for rooms
        String[] columnNames = {"Room Number", "Type", "Price", "Available"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        roomTable = new JTable(model);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshRoomTable());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshRoomTable();

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Table for transactions
        String[] columnNames = {"ID", "Customer", "Service", "Item", "Duration", "Total", "Payment", "Change"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(model);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTransactionTable());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTransactions(searchField.getText()));

        searchPanel.add(new JLabel("Customer Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshTransactionTable();

        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Login/Main panel
        if (!authService.IsAdminLoggedIn()) {
            return createLoginPanel();
        } else {
            return createAdminControlsPanel();
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new TitledBorder("Admin Login"));

        panel.add(new JLabel("Username:"));
        JTextField userField = new JTextField();
        panel.add(userField);

        panel.add(new JLabel("Password:"));
        JPasswordField passField = new JPasswordField();
        panel.add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (authService.LoginAdmin(username, password)) {
                JOptionPane.showMessageDialog(this,
                        "Login successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Switch admin panel to controls
                tabbedPane.setComponentAt(3, createAdminControlsPanel());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel createAdminControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, Administrator");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Room management panel
        JPanel roomManagementPanel = new JPanel(new BorderLayout());

        // Room table with checkboxes for availability
        String[] columnNames = {"Room Number", "Type", "Price", "Available", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        JTable roomTable = new JTable(model);
        roomTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        roomTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        roomTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Populate table
        refreshAdminRoomTable(model);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Add new room panel
        JPanel addRoomPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addRoomPanel.setBorder(new TitledBorder("Add New Room"));

        addRoomPanel.add(new JLabel("Room Number:"));
        JTextField roomNumberField = new JTextField();
        addRoomPanel.add(roomNumberField);

        addRoomPanel.add(new JLabel("Room Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        addRoomPanel.add(typeCombo);

        JButton addButton = new JButton("Add Room");
        addButton.addActionListener(e -> {
            String roomNumber = roomNumberField.getText().trim();
            String roomType = (String) typeCombo.getSelectedItem();

            if (!roomNumber.isEmpty()) {
                roomService.addRoom(new HotelRoom(roomNumber, roomType));
                refreshAdminRoomTable(model);
                roomNumberField.setText("");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        roomManagementPanel.add(scrollPane, BorderLayout.CENTER);
        roomManagementPanel.add(addRoomPanel, BorderLayout.NORTH);
        roomManagementPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            ((AuthServiceImpl) authService).adminLoggedIn = false;
            tabbedPane.setComponentAt(3, createAdminPanel());
        });

        // Add components
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(roomManagementPanel, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAdminRoomTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<HotelRoom> rooms = roomService.getAllRooms();
        for (HotelRoom room : rooms) {
            model.addRow(new Object[]{
                room.getRoomNumber(),
                room.getType(),
                room.getPrice(),
                room.isAvailable(),
                "Delete"
            });
        }
    }

    // Custom cell editor for delete button
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private DefaultTableModel model;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            this.model = (DefaultTableModel) table.getModel();
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String roomNumber = (String) table.getValueAt(table.getEditingRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Delete room " + roomNumber + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    roomService.deleteRoom(roomNumber);
                    model.removeRow(table.getEditingRow());
                }
            }
            isPushed = false;
            return label;
        }
    }

    // Custom cell renderer for delete button
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private void refreshRoomTable() {
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        model.setRowCount(0); // Clear table

        List<HotelRoom> rooms = roomService.getAllRooms();
        for (HotelRoom room : rooms) {
            model.addRow(new Object[]{
                room.getRoomNumber(),
                room.getType(),
                room.getPrice(),
                room.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void refreshTransactionTable() {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0); // Clear table

        List<Transaction> transactions = txService.getAll();
        for (Transaction tx : transactions) {
            model.addRow(new Object[]{
                null, // You might want to add ID field
                tx.getCustomerName(),
                tx.getServiceType(),
                tx.getItemName(),
                tx.getDuration(),
                tx.getTotalPrice(),
                tx.getPayment(),
                tx.getChange()
            });
        }
    }

    private void searchTransactions(String customerName) {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0); // Clear table

        List<Transaction> transactions = bookingService.getUserTransactions(customerName);
        for (Transaction tx : transactions) {
            model.addRow(new Object[]{
                null,
                tx.getCustomerName(),
                tx.getServiceType(),
                tx.getItemName(),
                tx.getDuration(),
                tx.getTotalPrice(),
                tx.getPayment(),
                tx.getChange()
            });
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Hotel Booking System\n"
                + "Version 1.0\n"
                + "© 2023 PT. Bina Insani\n"
                + "Developed by Your Name",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
}
