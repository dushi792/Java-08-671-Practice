import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jzhao2
 */
public class Practice extends JFrame{
    private JPanel formPane, buttonPane;
    private JButton toObama, toRomney, showObama, showRomney;
    private JTextField lastName, firstName, amountField;
    private JTextArea resArea;
    private List<Contribution> listObama, listRomney;

    private static class Contribution implements Comparable<Contribution> {
        private String lastName;
        private String firstName;
        private int amount;
        private String accepter;

        Contribution(String firstName, String lastName, int amount, String accepter) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.amount = amount;
            this.accepter = accepter;
        }

        @Override
        public String toString() {
            String name = lastName + ", " + firstName;

            if (name.length() > 15) {
                name = name.substring(0, 15);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-15s          %,10d        %6s %n", name, amount, accepter));

            int pos = sb.indexOf(String.format("%,d", amount));
            sb.insert(pos, "$ ");

            return sb.toString();
        }

        @Override
        public int compareTo(Contribution o) {
            if (amount != o.getAmount()) {
                return o.getAmount() - amount;
            } else if (!lastName.equals(o.getLastName())){
                return lastName.compareTo(o.getLastName());
            } else {
                return firstName.compareTo(firstName);
            }
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public int getAmount() {
            return amount;
        }
    }

    private class ContributionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == toObama || e.getSource() == toRomney) {

                if (isValid(firstName, lastName, amountField)) {
                    String first = firstName.getText().trim();
                    String last = lastName.getText().trim();
                    String to = e.getSource() == toObama ? "Obama" : "Romney";
                    int amount = Integer.parseInt(amountField.getText().trim());


                    Contribution con = new Contribution(first, last, amount, to);

                    if (to.equals("Obama")) {
                        listObama.add(con);
                    } else if (to.equals("Romney")) {
                        listRomney.add(con);
                    }

                    resArea.append(con.toString());
                    firstName.setText(null);
                    lastName.setText(null);
                    amountField.setText(null);
                }
            }
        }

        private boolean isValid(JTextField firstName, JTextField lastName, JTextField amountField) {
            try {
                int amount = Integer.parseInt(amountField.getText());

                if (amount > 10_000_000) {
                    resArea.append("*** The amount of contribution is too large. " +
                            "($ 10,000,000 is the maximum allowed) ***\n");

                    return false;
                }

                if (amount <= 0) {
                    resArea.append("*** The amount of contribution is too small. " +
                            "($ 1 is the maximum allowed) ***\n");

                    return false;
                }

            } catch (NumberFormatException e) {
                resArea.append("*** The amount of contribution is not a valid integer format. ***\n");
                return false;
            }

            if (lastName.getText().trim().length() == 0) {
                resArea.append("*** Please provide the last name. ***\n");
                return false;
            }

            if (firstName.getText().trim().length() == 0) {
                resArea.append("*** Please provide the first name. ***\n");
                return false;
            }

            return true;
        }


    }

    private class ListButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Contribution> list = null;
            String accepter = null;

            if (e.getSource() == showObama) {
                list = listObama;
                accepter = "Obama";
            } else if (e.getSource() == showRomney) {
                list = listRomney;
                accepter = "Romney";
            }

            if (list != null) {
                resArea.setText(null);
                Collections.sort(list);

                long total = 0;
                for (Contribution con : list) {
                    resArea.append(con.toString());
                    total += con.getAmount();

                }

                String totalRes = String.format("\nTotal contributions for " + accepter + ": $ %,d %n%n", total);
                resArea.append(totalRes);
            }

        }
    }

    public Practice() {
        setLayout(new FlowLayout());
        setTitle("MidTerm Campaign Contribution Application");

        listObama = new ArrayList<>();
        listRomney = new ArrayList<>();
        Font font = new Font("monospaced", Font.PLAIN, 12);

        formPane = new JPanel(new FlowLayout());
        buttonPane = new JPanel(new GridLayout(2,2));
        toObama = new JButton("Contribute to Obama");
        toRomney = new JButton("Contribute to Romney");
        showObama = new JButton("List Obama Contributes");
        showRomney = new JButton("List Romney Contributes");
        lastName = new JTextField(10);
        firstName = new JTextField(10);
        amountField = new JTextField(10);
        resArea = new JTextArea(30, 80);

        lastName.setFont(font);
        firstName.setFont(font);
        amountField.setFont(font);
        resArea.setEditable(false);
        resArea.setFont(font);

        ContributionActionListener contribution = new ContributionActionListener();
        toObama.addActionListener(contribution);
        toRomney.addActionListener(contribution);

        ListButtonActionListener list = new ListButtonActionListener();
        showObama.addActionListener(list);
        showRomney.addActionListener(list);

        formPane.add(new JLabel("Contributor Last Name: "));
        formPane.add(lastName);
        formPane.add(new JLabel("First Name: "));
        formPane.add(firstName);
        formPane.add(new JLabel("Amount: "));
        formPane.add(amountField);

        buttonPane.add(toObama);
        buttonPane.add(toRomney);
        buttonPane.add(showObama);
        buttonPane.add(showRomney);

        add(formPane);
        add(buttonPane);
        add(new JScrollPane(resArea));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Practice();

    }
}
