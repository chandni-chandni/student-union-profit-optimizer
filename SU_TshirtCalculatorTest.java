package StudentUnion_App;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SU_TshirtCalculatorTest {

	@Test
	public void testValidCalculation() {
	    SU_TShirtResult result = SU_TshirtCalculator.calculate(65.0, 0.25, 10.0);
	    assertTrue(result.isProfitable);
	    assertTrue(result.profit > 0);
	    assertTrue(result.optimalProduction > 0);
	}

    @Test
    public void testInvalidPriceDropBelowMinimum() {
        SU_TShirtResult result = SU_TshirtCalculator.calculate(30.0, 0.1, 15.0);
        assertFalse(result.isProfitable);
        assertTrue(result.breakEvenMessage.toLowerCase().contains("price drop rate"));
    }

    @Test
    public void testInvalidPriceDropAboveMaximum() {
        SU_TShirtResult result = SU_TshirtCalculator.calculate(30.0, 0.7, 15.0);
        assertFalse(result.isProfitable);
        assertTrue(result.breakEvenMessage.toLowerCase().contains("price drop rate"));
    }

    @Test
    public void testFinalPriceBelowCost() {
        double basePrice = 30.0;
        double priceDropRate = 0.5;
        double variableCost = 25.0;
        
        SU_TShirtResult result = SU_TshirtCalculator.calculate(basePrice, priceDropRate, variableCost);
        
        System.out.println("Test Debug:");
        System.out.println("- Base Price: " + basePrice);
        System.out.println("- Price Drop: " + priceDropRate);
        System.out.println("- Variable Cost: " + variableCost);
        System.out.println("- Optimal Production: " + result.optimalProduction);
        System.out.println("- Final Price: " + (basePrice - priceDropRate * result.optimalProduction));
        System.out.println("- Result Message: " + result.breakEvenMessage);

        assertFalse(result.isProfitable, 
            "Should be unprofitable when final price would be below cost");

        String lowerMsg = result.breakEvenMessage.toLowerCase();
        assertTrue(
            lowerMsg.contains("below") || 
            lowerMsg.contains("cost") ||
            lowerMsg.contains("error") ||
            lowerMsg.contains("invalid") ||
            lowerMsg.contains("loss"),
            "Expected cost-related error message. Got: " + result.breakEvenMessage
        );
    }


    @Test
    public void testBreakEvenRangeMessage() {
        SU_TShirtResult result = SU_TshirtCalculator.calculate(50.0, 0.3, 15.0);
        System.out.println("✅ Break-even message: " + result.breakEvenMessage);
        assertNotNull(result.breakEvenMessage);
        assertTrue(result.breakEvenMessage.toLowerCase().contains("break-even") ||
                   result.breakEvenMessage.toLowerCase().contains("single"));
    }

    @Test
    public void testNoBreakEvenPoints() {
        SU_TShirtResult result = SU_TshirtCalculator.calculate(15.0, 0.5, 15.0);
        assertTrue(result.breakEvenMessage.toLowerCase().contains("no break-even") ||
                   result.breakEvenMessage.toLowerCase().contains("always loss"));
    }

    @Test
    public void testDemandSensitivityCalculation() {
        double basePrice = 30.0;
        double currentDrop = 0.25;
        double newDrop = 0.35;
        int quantity = 100;

        double result = SU_TshirtCalculator.calculateDemandSensitivity(basePrice, currentDrop, newDrop, quantity);

        double originalPrice = basePrice - currentDrop * quantity;
        double newPrice = basePrice - newDrop * quantity;
        double expected = (newPrice - originalPrice) / originalPrice;

        System.out.println("✅ Demand Sensitivity Expected: " + expected + ", Actual: " + result);
        assertEquals(expected, result, 0.0001);
    }
}
