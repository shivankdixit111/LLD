package Low_Level_Design.Behavioural_Design_Patterns.Strategy;

class PaymentProcessor {
    PaymentStrategy paymentStrategy;

    PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    void processPayment() {
        paymentStrategy.processPayment();
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}


interface PaymentStrategy {
    void processPayment();
}

class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment() {
        System.out.println("Payment processing using credit card");
    }
}
class PayPalPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment() {
        System.out.println("Payment processing using paypal");
    }
}

class StripePaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment() {
        System.out.println("Payment processing using credit stripe");
    }
}



public class Main {
    public static void main(String[] args) {
        CreditCardPaymentStrategy creditCardPaymentStrategy = new CreditCardPaymentStrategy();
        PayPalPaymentStrategy payPalPaymentStrategy = new PayPalPaymentStrategy();
        StripePaymentStrategy stripePaymentStrategy = new StripePaymentStrategy();

        PaymentProcessor paymentProcessor = new PaymentProcessor(creditCardPaymentStrategy);
        paymentProcessor.processPayment();

        paymentProcessor.setPaymentStrategy(payPalPaymentStrategy);
        paymentProcessor.processPayment();

        paymentProcessor.setPaymentStrategy(stripePaymentStrategy);
        paymentProcessor.processPayment();
    }
}


