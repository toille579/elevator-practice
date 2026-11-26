package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.Inches;

import edu.wpi.first.units.measure.Distance;

public enum ElevatorSetpoints {
    Bottom(Inches.of(0.0)),
    Level1(Inches.of(0.4)),
    Level2(Inches.of(0.8)),
    Level3(Inches.of(1.2));

    public final Distance heightMeters;

    ElevatorSetpoints(Distance heightMeters) {
        this.heightMeters = heightMeters;
    }
    public Distance getHeight() {
        return heightMeters;
    }
}