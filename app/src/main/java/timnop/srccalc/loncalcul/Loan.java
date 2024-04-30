package timnop.srccalc.loncalcul;


public class Loan {
    private double annualInterestRate;
    private int numberOfYears;
    private double loanAmount;
    private java.util.Date loanDate;

    /** Default constructor */
    public Loan() {
        this(2.5, 1, 1000);
    }

    /** Construct a loan with specified annual interest rate,
     number of years, and loan amount
     */
    Loan(double annualInterestRate, int numberOfYears,
         double loanAmount) {
        this.annualInterestRate = annualInterestRate;
        this.numberOfYears = numberOfYears;
        this.loanAmount = loanAmount;
        loanDate = new java.util.Date();
    }

    /** Return annualInterestRate */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    /** Set a new annualInterestRate */
    void setAnnualInterestRate() {
        this.annualInterestRate = 0.0;
    }

    /** Return numberOfYears */
    public int getNumberOfYears() {
        return numberOfYears;
    }

    /** Set a new numberOfYears */
    void setNumberOfYears() {
        this.numberOfYears = 1;
    }

    /** Return loanAmount */
    public double getLoanAmount() {
        return loanAmount;
    }

    /** Set a newloanAmount */
    void setLoanAmount() {
        this.loanAmount = 0.0;
    }

    /** Find monthly payment */
    double getMonthlyPayment() {
        double monthlyInterestRate = annualInterestRate / 1200;
        return loanAmount * monthlyInterestRate / (1 -
                (1 / Math.pow(1 + monthlyInterestRate, numberOfYears * 12)));
    }

    /** Find total payment */
    double getTotalPayment() {
        return getMonthlyPayment() * numberOfYears * 12;
    }

    /** Return loan date */
    public java.util.Date getLoanDate() {
        return loanDate;
    }
}
