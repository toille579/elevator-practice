package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator.ElevatorIO.ElevatorIOInputs;

//import frc.robot.util.AlertUtils;
import java.util.function.Supplier;
//import org.littletonrobotics.junction.Logger;


public class Elevator extends SubsystemBase {
    public ElevatorIO io;
    public ElevatorIOInputs inputs;

    public Distance targetHeight = Inches.of(0);
    public boolean isBrakeMode = true;

    public Elevator(ElevatorIO io) {
        this.io = io;
        this.inputs = new ElevatorIOInputs();
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        SmartDashboard.putString("Elevator/ActiveCommand",
        getCurrentCommand() != null ? getCurrentCommand().getName() : "None");
        SmartDashboard.putNumber("Elevator/MeasurementM", inputs.elevatorHeight.in(Meters));
        SmartDashboard.putNumber("Elevator/AppliedVolts", inputs.elevatorAppliedVoltage.in(Volts));
    }
    
    public Command setElevatorHeight(Supplier<Distance> distanceSupplier) {
        return runOnce(
            () -> {
                Distance newHeight = distanceSupplier.get();
                targetHeight = newHeight;
                io.setElevatorPosition(newHeight);
            });
    }
    public Command setElevatorHeight(ElevatorSetpoints setpoint) {
        return setElevatorHeight(() -> setpoint.getHeight());
    }
    public void setHeight(Distance height) {
        targetHeight = height;
        io.setElevatorPosition(height);
    }

    public Command setIntakeArmVoltage(Supplier<Voltage> voltageSupplier){
        return runOnce(() -> io.setElevatorVoltage(voltageSupplier.get()));
    }

    public Command reset() {
        return runOnce(() -> io.resetHeight());
    }

    public Command toggleBrakeMode() {
        return runOnce(() -> {isBrakeMode = !isBrakeMode; io.setBrakeMode(isBrakeMode);}).ignoringDisable(true);
    }
    
}