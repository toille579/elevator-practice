package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Voltage;

public class ElevatorIOSim implements ElevatorIO {

    // Constants:
     private static final double ELEVATOR_MASS = 6.0;
     private static final double MIN_HEIGHT = 0.0;
     private static final double MAX_HEIGHT = 1.2;
    
    private final ElevatorSim elevatorSim =
        new ElevatorSim(
            DCMotor.getKrakenX60(1),
            ElevatorConstants.ELEVATOR_GEARING,
            ELEVATOR_MASS,
            ElevatorConstants.DRUM_RADIUS,
            MIN_HEIGHT,
            MAX_HEIGHT,
            true,
            MIN_HEIGHT
        );
    private double targetPositionHeight = MIN_HEIGHT;
    private double appliedVolts = 0.0;
    private boolean positionControl = false;

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        if(positionControl) {
            double positionErrorHeight = targetPositionHeight - elevatorSim.getPositionMeters();
            double velocityMetersPerSec = elevatorSim.getVelocityMetersPerSecond();
            appliedVolts = 8.0*positionErrorHeight - 0.3*velocityMetersPerSec;
            appliedVolts = MathUtil.clamp(appliedVolts, -12.0, 12.0);
        }

        elevatorSim.setInputVoltage(appliedVolts);
        elevatorSim.update(0.02);

        inputs.elevatorTalonConnected = true;
        inputs.elevatorHeight = Meters.of(elevatorSim.getPositionMeters());
        inputs.elevatorVelocity = MetersPerSecond.of(elevatorSim.getVelocityMetersPerSecond());
        inputs.elevatorStatorCurrent = Amps.of(Math.abs(elevatorSim.getCurrentDrawAmps()));
        inputs.elevatorAppliedVoltage = Volts.of(appliedVolts);
    }

    @Override
    public void setElevatorPosition(Distance targetHeight) {
        this.targetPositionHeight = targetHeight.in(Meters);
        positionControl = true;
    }

    @Override
    public void setElevatorVoltage(Voltage voltage) {
        appliedVolts = voltage.in(Volts);
        positionControl = false;
    }

    @Override
    public void resetHeight() {
        targetPositionHeight = MIN_HEIGHT;
        positionControl = true;
    }
    
}
