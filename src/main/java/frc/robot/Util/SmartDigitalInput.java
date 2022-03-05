package frc.robot.Util;

import edu.wpi.first.wpilibj.DigitalInput;

public class SmartDigitalInput extends DigitalInput{

    boolean invert = false;

    public SmartDigitalInput(int channel) {
        super(channel);
    }
    
    public void invert(){
        invert = true;
    }

    @Override
    public boolean get(){
        return super.get() ^ invert;
    }

    boolean currentVal;
    boolean prevVal;
    public void update(){
        prevVal = currentVal;
        currentVal = get();
    }

    public boolean risingEdge(){
        return !prevVal && currentVal;
    }

    public boolean fallingEdge(){
        return prevVal && !currentVal;
    }
}
