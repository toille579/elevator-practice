package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.*;
// import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import frc.robot.Constants;
// import frc.robot.util.AlertUtils;
// import frc.robot.util.BetterStatusSignalCollection;

public class ElevatorIOTalonFX implements ElevatorIO {
    public TalonFX elevatorTalon;
    // public BetterStatusSignalCollection statusSignalCollector;

    public StatusSignal<Angle> elevatorPosition;
    public StatusSignal<AngularVelocity> elevatorVelocity;
    public StatusSignal<Current> elevatorSupplyCurrent;
    public StatusSignal<Current> elevatorStatorCurrent;
    public StatusSignal<Voltage> elevatorVoltage;
    private CANcoder cancoder;

    public MotionMagicVoltage motionMagicVoltageElevator = new MotionMagicVoltage(0).withSlot(0);

    public ElevatorIOTalonFX() {
        this.elevatorTalon = 
            new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_LEADER_ID, ElevatorConstants.ROBOT_CAN_BUS);

        TalonFXConfiguration elevatorConfig = new TalonFXConfiguration();
        elevatorConfig.Feedback.RotorToSensorRatio = -ElevatorConstants.ELEVATOR_GEARING;
        elevatorConfig.MotorOutput.Inverted = ElevatorConstants.ELEVATOR_MOTOR_DIRECTION;
        elevatorConfig.Slot0.GravityType = GravityTypeValue.Elevator_Static;
        elevatorConfig.Slot0.kG = 0.01;
        elevatorConfig.Slot0.kS = 0.0;
        elevatorConfig.Slot0.kV = 0.0;
        elevatorConfig.Slot0.kA = 0.12;
        elevatorConfig.Slot0.kP = 0.17;
        elevatorConfig.Slot0.kI = 0.0;
        elevatorConfig.Slot0.kD = 0.2;


        elevatorConfig.MotionMagic = ElevatorConstants.ELEVATOR_MOTION_MAGIC_CONFIGS;
        elevatorConfig.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        elevatorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        elevatorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        elevatorConfig.CurrentLimits.StatorCurrentLimit = 60;
        elevatorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        elevatorConfig.CurrentLimits.SupplyCurrentLimit = 30;
        elevatorConfig.CurrentLimits.SupplyCurrentLowerLimit = 25;
        elevatorConfig.CurrentLimits.SupplyCurrentLowerTime = 0.5;

    elevatorPosition = elevatorTalon.getPosition();
    elevatorVelocity = elevatorTalon.getVelocity();
    elevatorSupplyCurrent = elevatorTalon.getSupplyCurrent();
    elevatorStatorCurrent = elevatorTalon.getStatorCurrent();
    elevatorVoltage = elevatorTalon.getMotorVoltage();

    elevatorPosition.setUpdateFrequency(50);
    elevatorVelocity.setUpdateFrequency(50);
    elevatorSupplyCurrent.setUpdateFrequency(20);
    elevatorStatorCurrent.setUpdateFrequency(20);
    elevatorVoltage.setUpdateFrequency(20);
    ParentDevice.optimizeBusUtilizationForAll(elevatorTalon);
    }
    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        BaseStatusSignal.refreshAll(
            elevatorPosition,
            elevatorVelocity,
            elevatorSupplyCurrent,
            elevatorStatorCurrent,
            elevatorVoltage
        );
        inputs.elevatorTalonConnected = elevatorPosition.getStatus().isOK();
        double rotations = elevatorPosition.getValue().in(Rotations);
        inputs.elevatorHeight = Inches.of(ElevatorConstants.DRUM_CIRCUMFERENCE * rotations);
        inputs.elevatorVelocity = InchesPerSecond.of(elevatorVelocity.getValue().in(RadiansPerSecond)*ElevatorConstants.DRUM_RADIUS);
        inputs.elevatorSupplyCurrent = elevatorSupplyCurrent.getValue();
        inputs.elevatorStatorCurrent = elevatorStatorCurrent.getValue();
        inputs.elevatorAppliedVoltage = elevatorVoltage.getValue();
    }

    @Override
    public void setElevatorPosition(Distance targetHeight) {
        double targetRotations = targetHeight.in(Inches)/ElevatorConstants.DRUM_CIRCUMFERENCE;
        elevatorTalon.setControl(motionMagicVoltageElevator.withPosition(targetRotations));
    }

    @Override
    public void setElevatorVoltage(Voltage volts) {
        elevatorTalon.setVoltage(volts.in(Volts));
    }

    @Override
    public void resetHeight() {
        // elevatorTalon.setPosition(Degrees.of(ElevatorConstants.M))
    }

    @Override
    public void setBrakeMode(boolean brake) {
        elevatorTalon.setNeutralMode(brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
    }
}

