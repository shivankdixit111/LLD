package Low_Level_Design.Behavioural_Design_Patterns.Chain_Of_Responsibility;

abstract class Approver {
    protected Approver nextApprover; 

    public void setNextApprover(Approver approver) {
        this.nextApprover = approver;
    }
    public abstract void processLeaveRequest(int days); 
}

class Supervisor extends Approver {
    @Override
    public void processLeaveRequest(int days) {
        if(days <= 3) {
            System.out.println("Supervisor : leave approved");
        } else {
            nextApprover.processLeaveRequest(days);
        }
    }
}
class Manager extends Approver {
    @Override
    public void processLeaveRequest(int days) {
        if(days <= 7) {
            System.out.println("Manager : leave approved");
        } else {
            nextApprover.processLeaveRequest(days);
        }
    }
}
class Director extends Approver {
    @Override
    public void processLeaveRequest(int days) {
        if(days <= 14) {
            System.out.println("Director : leave approved");
        } else {
            System.out.println("leave cann't be approved!  Too many days");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Approver supervisor = new Supervisor();
        Approver manager = new Manager();
        Approver director = new Director();

        supervisor.setNextApprover(manager);
        manager.setNextApprover(director);

        int days = 15;
        supervisor.processLeaveRequest(days);
    }
}
