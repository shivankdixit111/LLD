package Low_Level_Design.Creational_Design_Patterns.Abstract_Factory; //remove while running the code

// Abstract Products
interface Button{
    void paint();
}
interface CheckBox {
    void render();
}

// Concret Products - macOS family
class MacButton implements Button {
    public void paint() {
        System.out.println("Painting with mac-button...");
    }
}
class MacCheckBox implements CheckBox {
    public void render() {
        System.out.println("Rendering the mac-checkbox...");
    }
}

// Concret Products - Windows Family 
class WinButton implements Button {
    public void paint() {
        System.out.println("Painting with window-button...");
    }
}
class WinCheckBox implements CheckBox {
    public void render() {
        System.out.println("Rendering the window-checkbox...");
    }
}

// Abstract Factory 
interface GUIFactory {
    Button createButton();
    CheckBox createCheckBox();
}

// Concret Factory 
class MacGUIFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public CheckBox createCheckBox() { return new MacCheckBox(); }
}

class WinGUIFactory implements GUIFactory {
    public Button createButton() { return new WinButton(); }
    public CheckBox createCheckBox() { return new WinCheckBox(); }
}


class Application {
    private final Button button;
    private final CheckBox checkBox;

    Application(GUIFactory factory) {
        button = factory.createButton();
        checkBox = factory.createCheckBox();
    }

    public void render() {
        button.paint();
        checkBox.render();
    }
}

public class Main {
    public static void main(String[] args) {
        GUIFactory factory;
        factory = new MacGUIFactory();

        Application app = new Application(factory);
        app.render();
    }
}

/*

ABSTRACT FACTORY DESIGN PATTERN

Intent & Definition:
---------------------
    A creational design pattern that provides an interface for creating families
    of related or dependent objects without specifying their concrete classes.
    
    It encapsulates a set of individual factory methods into a unified factory contract,
    ensuring that product variants belonging to the same family are always instantiated
    and used consistently together.


Primary Purpose:
-----------------
    Enforces architectural consistency across multiple related product hierarchies
    using composition.
*/