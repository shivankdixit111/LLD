package Low_Level_Design.Creational_Design_Patterns.Builder;

class Car {
    private String engine;
    private int seats;
    private String color;
    private boolean sunroof;
    private boolean navigationSystem;

    private Car(Builder builder) {
        this.engine = builder.engine;
        this.seats = builder.seats;
        this.color = builder.color;
        this.sunroof = builder.sunroof;
        this.navigationSystem = builder.navigationSystem;
    }

    public String getEngine() {
        return engine;
    }
    public int getSeats() {
        return seats;
    }
    public String getColor() {
        return color;
    }
    public boolean getSunroof() {
        return sunroof;
    }
    public boolean getNavigationSystem() {
        return navigationSystem;
    }
    public String getDetails() {
        return "[ Car Details : engine = " + engine + ", seats = " + seats + ", color = " + color + ", sunroof = "
        + sunroof + ", navigationSystem = " + navigationSystem + " ] ";
    }


    static class Builder {
        private String engine;
        private int seats = 4;
        private String color = "White";
        private boolean sunroof = false;
        private boolean navigationSystem = false; //default values

        public Builder setEngine(String engine) {
            this.engine = engine;
            return this;
        }
        public Builder setSeats(int seats) {
            this.seats = seats;
            return this;
        }
        public Builder setColor(String color) {
            this.color = color;
            return this;
        }
        public Builder setSunroof(boolean sunroof) {
            this.sunroof = sunroof;
            return this;
        }
        public Builder setNavigationSystem(boolean navigationSystem) {
            this.navigationSystem = navigationSystem;
            return this;
        }

        // Build method to create a Car object
        public Car build() {
            return new Car(this); // Return a new Car created using the builder's values
        }
    }
};

public class Main {
    public static void main(String[] args) {
        Car.Builder builder = new Car.Builder();
        Car c1 = builder.setEngine("v8")
        .setSeats(5)
        .setColor("Red")
        .build();

        System.out.println(c1.getDetails());

         Car c2 = builder.setEngine("v6")
        .setSeats(6)
        .setColor("White")
        .setSunroof(true)
        .setNavigationSystem(true)
        .build();    // This build method returns the final product

        System.out.println(c2.getDetails());
    }
}
