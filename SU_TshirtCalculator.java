package StudentUnion_App;

public class SU_TshirtCalculator {
    private static final double FIXED_COST = 2500.0;
    private static final double MIN_PRICE_DROP = 0.25;
    private static final double MAX_PRICE_DROP = 0.65;

    public static SU_TShirtResult calculate(double basePrice, double priceDropRate, double variableCost) {
        // Validate price drop rate
        if (priceDropRate < MIN_PRICE_DROP || priceDropRate > MAX_PRICE_DROP) {
            return new SU_TShirtResult(0, 0, 
                String.format("Error: Price drop rate must be between £%.2f and £%.2f", MIN_PRICE_DROP, MAX_PRICE_DROP), 
                false);
        }

        // Calculate optimal production quantity
        int optimalQuantity = (int) Math.round((basePrice - variableCost) / (2 * priceDropRate));
        double finalPrice = basePrice - priceDropRate * optimalQuantity;
        
        // Validate final price
        if (finalPrice < variableCost) {
            return new SU_TShirtResult(0, 0, 
                "Error: Final price would be below production cost", 
                false);
        }

        // Calculate financial metrics
        double revenue = finalPrice * optimalQuantity;
        double totalCost = FIXED_COST + variableCost * optimalQuantity;
        double profit = revenue - totalCost;

        // Calculate break-even points
        String breakEvenMessage = calculateBreakEvenPoints(basePrice, priceDropRate, variableCost);

        return new SU_TShirtResult(optimalQuantity, profit, breakEvenMessage, profit >= 0);
    }

    private static String calculateBreakEvenPoints(double basePrice, double priceDropRate, double variableCost) {
        double a = -priceDropRate;
        double b = basePrice - variableCost;
        double c = -FIXED_COST;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return "No break-even points (always loss)";
        } else if (discriminant == 0) {
            int breakEven = (int) Math.round(-b / (2 * a));
            return String.format("Single break-even point: %,d t-shirts", breakEven);
        } else {
            double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            int minBreakEven = (int) Math.round(Math.min(root1, root2));
            int maxBreakEven = (int) Math.round(Math.max(root1, root2));
            return String.format("Break-even range: %,d to %,d t-shirts", minBreakEven, maxBreakEven);
        }
    }

    public static double calculateDemandSensitivity(double basePrice, double currentDrop, double newDrop, int quantity) {
        double originalPrice = basePrice - currentDrop * quantity;
        double newPrice = basePrice - newDrop * quantity;
        return (newPrice - originalPrice) / originalPrice;
    }
}