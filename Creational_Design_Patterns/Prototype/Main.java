package Low_Level_Design.Creational_Design_Patterns.Prototype;

class Character implements Cloneable{
    public String name;
    public int health;
    public int attackPower;
    public int level;

    Character(String name, int health, int attackPower, int level) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.level = level;
    }

    @Override // clone() creates a shallow copy of the object using JVM’s built-in cloning mechanism via super.clone().
    public Character clone() throws CloneNotSupportedException {  //shallow copy of Character object
        return (Character) super.clone(); //return Java Obejct(generic)  conver it -> Character
    }

    public String showInfo() {
        return "[ name = " + name + " , health = " + health + " , attackPower = " + attackPower + 
                " , level = " + level + " ]";
    }
}

class CharacterFactory {
    private Character prototype;
    CharacterFactory() {
        prototype = new Character("bob", 5, 10, 2);
    }

    public Character createCharacterWithNewName(String name) throws CloneNotSupportedException {
        Character clonedCharacter = prototype.clone();
        clonedCharacter.name = name;

        return clonedCharacter;
    }
    public Character createCharacterWithNewAttackPower(int attackPower) throws CloneNotSupportedException {
        Character clonedCharacter = prototype.clone();
        clonedCharacter.attackPower = attackPower;

        return clonedCharacter;
    }
}

public class Main {
    public static void main(String[] args) {
        CharacterFactory factory = new CharacterFactory();

        try {
            Character alice = factory.createCharacterWithNewName("alice");
            Character knight = factory.createCharacterWithNewAttackPower(20);

            System.out.println("Alice information -> " + alice.showInfo());
            System.out.println("Knight information -> " + knight.showInfo());
        } catch(CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }
    }
}


/*
🔐 Why CloneNotSupportedException?

    Java requires the class to implement Cloneable interface.
    If not implemented, JVM does not allow cloning and throws this exception.


📦 What kind of copy is created by clone()?

clone() creates a SHALLOW COPY of the object.

✔ Primitive fields (int, etc.) → copied as new values
✔ Object references → copied as references (same memory object)

🧍 Example:

Character c1 = new Character("Hero", 100, 50, 1);
Character c2 = c1.clone();

Result:
- c1 and c2 are two different objects in memory
- But internal referenced objects (if any) would be shared due to shallow copy
*/