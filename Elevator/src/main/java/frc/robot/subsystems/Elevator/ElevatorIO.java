package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Voltage;
//import org.littletonrobotics.junction.AutoLog;

public interface ElevatorIO {
    // @AutoLog
    public static class ElevatorIOInputs {
        public boolean elevatorTalonConnected = false;

        public Distance elevatorHeight = Inches.of(0);
        public LinearVelocity elevatorVelocity = MetersPerSecond.of(0.0);
        public Current elevatorSupplyCurrent = Amps.of(0.0);
        public Current elevatorStatorCurrent = Amps.of(0.0);
        public Voltage elevatorAppliedVoltage = Volts.of(0.0);
    }

    public default void updateInputs(ElevatorIOInputs inputs) {}

    public default void setElevatorPosition(Distance targetHeight) {}

    public default void setElevatorVoltage(Voltage voltage) {}

    public default void resetHeight() {}

    public default void setBrakeMode(boolean brake) {}

}
