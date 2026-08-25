package Low_Level_Design.Behavioural_Design_Patterns.Visitor;

interface Patient {
    public void accept(Visitor visitor);
}

class ChildPatient implements Patient {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
class AdultPatient implements Patient {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
class SeniorPatient implements Patient {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}


interface Visitor {
    public void visit(ChildPatient childPatient);
    public void visit(AdultPatient adultPatient);
    public void visit(SeniorPatient seniorPatient);
}

class DiagnosisVisitor implements Visitor{
    public void visit(ChildPatient childPatient) {
        System.out.println("Diagnosing a child patient");
    }
    public void visit(AdultPatient adultPatient) {
        System.out.println("Diagnosing a adult patient");
    }
    public void visit(SeniorPatient seniorPatient) {
        System.out.println("Diagnosing a senior patient");
    }
}
class BillingVisitor implements Visitor{
    public void visit(ChildPatient childPatient) {
        System.out.println("Billing a child patient \n");
    }
    public void visit(AdultPatient adultPatient) {
        System.out.println("Billing a adult patient \n");
    }
    public void visit(SeniorPatient seniorPatient) {
        System.out.println("Billing a senior patient \n");
    }
}



public class Main {
    public static void main(String[] args) {
        Patient[] patients = {
            new ChildPatient(), new AdultPatient(), new SeniorPatient() 
        };
        
        // for different operations we create different visitors to allow patient to accept and
        //  let them perform operations
        Visitor diagnosisVisitor = new DiagnosisVisitor();
        Visitor billingVisitor = new BillingVisitor();

        for(Patient patient: patients) {
            patient.accept(diagnosisVisitor);
            patient.accept(billingVisitor);
        }
    }
}
