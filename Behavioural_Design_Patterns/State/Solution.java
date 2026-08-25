package Low_Level_Design.Behavioural_Design_Patterns.State;

interface TrafficLightState {
    void next(TrafficLightContext TrafficLightContext);
    String getColor();
}

class RedLightState implements TrafficLightState {
    @Override
    public void next(TrafficLightContext context) {
         System.out.println("Switching from Red to Green : GO! ");
        context.setState(new GreenLightState());
    }
    @Override 
    public String getColor() {
        return "Red";
    }
}

class GreenLightState implements TrafficLightState {
    @Override
    public void next(TrafficLightContext context) {
        System.out.println("Switching from Green to Yellow : SLOW DOWN!");
        context.setState(new YellowLightState());
    }
    @Override 
    public String getColor() {
        return "Green";
    }
}

class YellowLightState implements TrafficLightState {
    @Override
    public void next(TrafficLightContext context) {
        System.out.println("Switching from Yellow to Red : STOP!");
        context.setState(new RedLightState());
    }
    @Override 
    public String getColor() {
        return "Yellow";
    }
}



class TrafficLightContext {
    TrafficLightState currentState; // aggregation (in setter it receives trafficLightState from outside world. doesn't fully own or manage its creation in its lifecycle)
    TrafficLightContext() {
        this.currentState = new RedLightState();
    }
    public void setState(TrafficLightState trafficLightState) { 
        this.currentState = trafficLightState;
    }

    public void next() {
        currentState.next(this);
    }
}

public class Solution {
    public static void main(String[] args) {
        TrafficLightContext trafficLight = new TrafficLightContext(); // Red
        trafficLight.next(); // Green
        trafficLight.next(); // Yellow 
        trafficLight.next(); // Red
        trafficLight.next(); // Green
    }
}
