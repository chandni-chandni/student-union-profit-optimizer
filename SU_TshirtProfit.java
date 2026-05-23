package StudentUnion_App;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class SU_TshirtProfit extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField priceField, sensitivityField, costField;
    private JTextArea resultArea;
    private static final double MIN_M = 0.25;
    private static final double MAX_M = 0.65;
    private JCheckBox priceSensitivityCheck, costSensitivityCheck, demandSensitivityCheck;

    public SU_TshirtProfit() {
        setTitle("Student Union T-Shirt Profit Calculator");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Parameters"));
        inputPanel.setBackground(new Color(245, 245, 255));
        inputPanel.setPreferredSize(new Dimension(350, 100));

        priceField = new JTextField("30.0");
        sensitivityField = new JTextField("0.25");
        costField = new JTextField("15.0");

        inputPanel.add(labeledField("Base Price (k) £:", priceField));
        inputPanel.add(labeledField("Price Drop Rate (m)", sensitivityField));
        inputPanel.add(labeledField("Variable Cost (c) £:", costField));

        JPanel sensitivityPanel = new JPanel(new GridLayout(3, 1));
        sensitivityPanel.setBorder(BorderFactory.createTitledBorder("Sensitivity Analysis Options"));
        sensitivityPanel.setBackground(new Color(245, 245, 255));
        priceSensitivityCheck = new JCheckBox("Price Sensitivity");
        costSensitivityCheck = new JCheckBox("Cost Sensitivity");
        demandSensitivityCheck = new JCheckBox("Demand Sensitivity");
        priceSensitivityCheck.setFont(new Font("Arial", Font.PLAIN, 12));
        costSensitivityCheck.setFont(new Font("Arial", Font.PLAIN, 12));
        demandSensitivityCheck.setFont(new Font("Arial", Font.PLAIN, 12));
        sensitivityPanel.add(priceSensitivityCheck);
        sensitivityPanel.add(costSensitivityCheck);
        sensitivityPanel.add(demandSensitivityCheck);
        inputPanel.add(sensitivityPanel);

        JButton calculateBtn = createStyledButton("Calculate Optimal Production", new Color(60, 120, 180));
        JButton sensitivityBtn = createStyledButton("Run Sensitivity Analysis", new Color(100, 150, 100));
        JButton detailedBtn = createStyledButton("Detailed Production Analysis", new Color(180, 100, 150));
        JButton breakEvenBtn = createStyledButton("Break-Even Analysis", new Color(150, 100, 180));

        inputPanel.add(calculateBtn);
        inputPanel.add(sensitivityBtn);
        inputPanel.add(detailedBtn);
        inputPanel.add(breakEvenBtn);

        add(inputPanel, BorderLayout.WEST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(new Color(240, 250, 250));
        resultArea.setBorder(BorderFactory.createTitledBorder("Analysis Results"));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        calculateBtn.addActionListener(e -> calculateOptimalProduction());
        sensitivityBtn.addActionListener(e -> performComprehensiveSensitivityAnalysis());
        detailedBtn.addActionListener(e -> performDetailedAnalysis());
        breakEvenBtn.addActionListener(e -> performBreakEvenAnalysis());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 255));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private boolean validateInputs() {
        try {
            double m = Double.parseDouble(sensitivityField.getText());
            if (m < MIN_M || m > MAX_M) {
                JOptionPane.showMessageDialog(this,
                    String.format("Price Drop Rate (m) must be between %.2f and %.2f", MIN_M, MAX_M),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void calculateOptimalProduction() {
        if (!validateInputs()) return;
        try {
            double k = Double.parseDouble(priceField.getText());
            double m = Double.parseDouble(sensitivityField.getText());
            double c = Double.parseDouble(costField.getText());
            SU_TShirtResult result = SU_TshirtCalculator.calculate(k, m, c);
            DecimalFormat df = new DecimalFormat("#,##0.00");
            String output = "\n=== OPTIMAL PRODUCTION ANALYSIS ===\n\n";
            if (!result.isProfitable) {
                output += String.format("WARNING: This production scenario would result in a LOSS of £%s\n", df.format(Math.abs(result.profit)));
            } else {
                output += String.format("Optimal Production Quantity: %,d t-shirts\n", result.optimalProduction);
                output += String.format("Final Price per T-shirt: £%s\n", df.format(k - m * result.optimalProduction));
                output += String.format("Maximum Profit: £%s\n", df.format(result.profit));
            }
            output += "\nBreak-even Analysis:\n" + result.breakEvenMessage;
            output += "\n\nFixed Costs: £2,500.00 | Variable Cost per Unit: £" + df.format(c);
            resultArea.setText(output);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performComprehensiveSensitivityAnalysis() {
        if (!validateInputs()) return;
        StringBuilder sb = new StringBuilder("\n=== COMPREHENSIVE SENSITIVITY ANALYSIS ===\n\n");
        try {
            double k = Double.parseDouble(priceField.getText());
            double m = Double.parseDouble(sensitivityField.getText());
            double c = Double.parseDouble(costField.getText());
            DecimalFormat df = new DecimalFormat("#,##0.00");
            if (priceSensitivityCheck.isSelected()) {
                sb.append("--- PRICE SENSITIVITY ANALYSIS ---\n");
                double[] priceVariations = {k - 5, k, k + 5};
                for (double newK : priceVariations) {
                    SU_TShirtResult result = SU_TshirtCalculator.calculate(newK, m, c);
                    sb.append(String.format("Base Price £%s --> Optimal: %,d | Profit: £%s\n",
                        df.format(newK), result.optimalProduction, df.format(result.profit)));
                }
                sb.append("\n");
            }
            if (costSensitivityCheck.isSelected()) {
                sb.append("--- COST SENSITIVITY ANALYSIS ---\n");
                double[] costVariations = {c - 5, c, c + 5};
                for (double newC : costVariations) {
                    SU_TShirtResult result = SU_TshirtCalculator.calculate(k, m, newC);
                    sb.append(String.format("Var. Cost £%s --> Optimal: %,d | Profit: £%s\n",
                        df.format(newC), result.optimalProduction, df.format(result.profit)));
                }
                sb.append("\n");
            }
            if (demandSensitivityCheck.isSelected()) {
                sb.append("--- DEMAND SENSITIVITY ANALYSIS ---\n");
                double[] demandVariations = {Math.max(MIN_M, m - 0.1), m, Math.min(MAX_M, m + 0.1)};
                for (double newM : demandVariations) {
                    SU_TShirtResult result = SU_TshirtCalculator.calculate(k, newM, c);
                    double sensitivity = SU_TshirtCalculator.calculateDemandSensitivity(k, m, newM, result.optimalProduction);
                    sb.append(String.format("Price Drop £%s --> Opt: %,d | Profit: £%s | Demand Change: %.2f%%\n",
                        df.format(newM), result.optimalProduction, df.format(result.profit), sensitivity * 100));
                }
                sb.append("\n");
            }
            if (!priceSensitivityCheck.isSelected() && !costSensitivityCheck.isSelected() && !demandSensitivityCheck.isSelected()) {
                sb.append("Please select at least one sensitivity option to perform analysis.\n");
            }
            resultArea.setText(sb.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for sensitivity analysis.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDetailedAnalysis() {
        if (!validateInputs()) return;
        try {
            double k = Double.parseDouble(priceField.getText());
            double m = Double.parseDouble(sensitivityField.getText());
            double c = Double.parseDouble(costField.getText());
            StringBuilder sb = new StringBuilder("\n=== DETAILED PRODUCTION ANALYSIS ===\n\n");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            sb.append(String.format("%-10s %-15s %-15s %-15s %-15s\n", "Quantity", "Unit Price", "Revenue", "Total Cost", "Profit"));
            sb.append(String.format("%-10s %-15s %-15s %-15s %-15s\n", "--------", "----------", "-------", "----------", "------"));
            for (int x = 50; x <= 500; x += 50) {
                double price = k - m * x;
                double revenue = price * x;
                double cost = 2500 + c * x;
                double profit = revenue - cost;
                sb.append(String.format("%-10d £%-14s £%-14s £%-14s £%-14s\n",
                    x, df.format(price), df.format(revenue), df.format(cost), df.format(profit)));
            }
            resultArea.setText(sb.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performBreakEvenAnalysis() {
        if (!validateInputs()) return;
        try {
            double k = Double.parseDouble(priceField.getText());
            double m = Double.parseDouble(sensitivityField.getText());
            double c = Double.parseDouble(costField.getText());
            SU_TShirtResult result = SU_TshirtCalculator.calculate(k, m, c);
            StringBuilder sb = new StringBuilder("\n=== BREAK-EVEN ANALYSIS ===\n\n");
            sb.append(result.breakEvenMessage).append("\n\n");
            sb.append("Break-even occurs when:\n");
            sb.append("Revenue = Total Cost\n");
            sb.append(String.format("(%.2f - %.2fx) * x = 2500 + %.2fx\n", k, m, c));
            sb.append(String.format("%.2fx - %.2fx² = 2500 + %.2fx\n", k, m, c));
            sb.append(String.format("-%.2fx² + (%.2f - %.2f)x - 2500 = 0\n", m, k, c));
            resultArea.setText(sb.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SU_TshirtProfit();
        });
    }
}
