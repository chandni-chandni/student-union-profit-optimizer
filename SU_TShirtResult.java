package StudentUnion_App;

public class SU_TShirtResult {
    public final int optimalProduction;
    public final double profit;
    public final String breakEvenMessage;
    public final boolean isProfitable;
    public final double finalPrice;
    public final double revenue;
    public final double totalCost;

    public SU_TShirtResult(int optimalProduction, double profit, 
                          String breakEvenMessage, boolean isProfitable) {
        this.optimalProduction = optimalProduction;
        this.profit = profit;
        this.breakEvenMessage = breakEvenMessage;
        this.isProfitable = isProfitable;
        this.finalPrice = 0;
        this.revenue = 0;  
        this.totalCost = 0;  
    }
}