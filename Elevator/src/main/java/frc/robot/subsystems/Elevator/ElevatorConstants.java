package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.units.measure.Angle;
// import frc.robot.util.LoggedNetworkPIDFeedforwardGains;
// import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

public class ElevatorConstants {
    public static final CANBus ROBOT_CAN_BUS = CANBus.roboRIO();
    public static final int INTAKE_MOTOR_ID = 22;
    public static final int ELEVATOR_MOTOR_LEADER_ID = 20;
    public static final int ELEVATOR_MOTOR_FOLLOWER_ID = 21;
    public static final double ELEVATOR_GEARING = 9.0;
    public static final int kBOTTOM_LIMIT_SWITCH_PORT = 0;
    public static final int kINTAKE_BEAM_BREAK_PORT = 1;
    public static final double DRUM_RADIUS = 0.2387;
    public static final double DRUM_CIRCUMFERENCE = ElevatorConstants.DRUM_RADIUS*2*Math.PI;

    public static final InvertedValue ELEVATOR_MOTOR_DIRECTION =
        InvertedValue.CounterClockwise_Positive; 
    // public static final LoggedNetworkPIDFeedforwardGains ELEVATOR_GAINS = 
    //     new LoggedNetworkPIDFeedforwardGains(0.01,0,0.00,012,017,0,02,"elevator");

    public static final MotionMagicConfigs ELEVATOR_MOTION_MAGIC_CONFIGS =
        new MotionMagicConfigs()
            .withMotionMagicCruiseVelocity(1.5)
            .withMotionMagicAcceleration(3);

    
}
