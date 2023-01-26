import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JFrame {

    private JPanel registerPanel;
    private JTextField first_nameField1;
    private JTextField last_nameField2;
    private JTextField contact_noField3;
    private JTextField emailField4;
    private JTextField addressField5;
    private JPasswordField passField6;
    private JPasswordField cfField7;
    private JButton registerButton;
    private JButton cancelButton;

    public RegistrationForm(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(registerPanel);

        this.setPreferredSize(new Dimension(450,450));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void registerUser() {
        String first_name = first_nameField1.getText();
        String last_name = last_nameField2.getText();
        String contact_no = contact_noField3.getText();
        String email = emailField4.getText();
        String address = addressField5.getText();
        String password = String.valueOf(passField6.getPassword());
        String confirm_password = String.valueOf(cfField7.getPassword());

        if (first_name.isEmpty() || last_name.isEmpty() || contact_no.isEmpty() || email.isEmpty() ||
        address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all field",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm_password)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(first_name, last_name, contact_no, email, address, password);
            if (user != null) {
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Failed to register new user",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);

            }
    }

    public User user;
    private User addUserToDatabase(String first_name, String last_name, String contact_no, String email, String address, String password){
        User user = null;
        final  String DB_URL = "jdbc:mysql://127.0.0.1:3306/student_db";
        final String USERNAME = "root";
        final String PASSWORD = "kinotech03";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // connect to database successfully
            System.out.println("you are now connected");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO student (first_name, last_name, contact_no, email, address, password)"+ " VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, contact_no);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, password);

            // Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.first_name = first_name;
                user.last_name = last_name;
                user.contact_no = contact_no;
                user.email = email;
                user.address = address;
                user.password = password;
            }

            stmt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);
        myForm.setVisible(true);
        User user = myForm.user;
        if (user != null){
            System.out.println("Successfully registration of +: " + user.first_name);

        }else {
            System.out.println("Registration canceled");
        }
    }
}
