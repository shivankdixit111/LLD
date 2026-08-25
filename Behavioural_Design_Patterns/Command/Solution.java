package Low_Level_Design.Behavioural_Design_Patterns.Command;

class TV {
    public void turnOn() {
        System.out.println("turning the TV on");
    }
    public void turnOff() {
        System.out.println("turning the TV on");
    }
    public void changeChannel(int channel) {
        System.out.println("changing the channel to : " + channel);
    }
    public void adjustVolume(int volume) {
        System.out.println("adjusting the volume to : " + volume);
    }
}

interface Command {
    void execute();
}

class OnCommand implements Command {
    private TV tv;
    OnCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOn();
    }
}

class OffCommand implements Command {
    private TV tv;
    OffCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOff();
    }
}

class ChangeChannel implements Command {
    private TV tv;
    private int channel;
    ChangeChannel(TV tv, int channel) {
        this.tv = tv;
        this.channel = channel;
    }

    @Override
    public void execute() {
        tv.changeChannel(channel);
    }
}

class AdjustVolume implements Command {
    private TV tv;
    private int volume;
    AdjustVolume(TV tv, int volume) {
        this.tv = tv;
        this.volume = volume;
    }

    @Override
    public void execute() {
        tv.adjustVolume(volume);
    }
}

class RemoteControl {
    private Command OnCommand;
    private Command OffCommand;
    
    public void setOnCommand(Command OnCommand) {
        this.OnCommand = OnCommand;
    }
    public void setOffCommand(Command OffCommand) {
        this.OffCommand = OffCommand;
    }
    
    public void pressOn() {
        OnCommand.execute();
    }
    public void pressOff() {
        OffCommand.execute();
    }
}

public class Solution {
    public static void main(String[] args) {
        TV tv = new TV();
        Command onCommand = new OnCommand(tv);
        Command offCommand = new OffCommand(tv);
        Command changeChannel = new ChangeChannel(tv, 3);
        Command adjustVolume = new AdjustVolume(tv, 20);

        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setOnCommand(onCommand);
        remoteControl.setOffCommand(offCommand);
        
        remoteControl.pressOn();
        changeChannel.execute();
        adjustVolume.execute();
        remoteControl.pressOff();

    }
}
