package swervelib.imu;

import java.util.Optional;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * IMU Swerve class for the {@link ADIS16470_IMU} device.
 */
public class ADIS16470Swerve extends SwerveIMU
{

  /**
   * {@link ADIS16470_IMU} device to read the current headings from.
   */
  private final ADIS16470_IMU imu;
  /**
   * Offset for the ADIS16470 yaw reading.
   */
  private       double        yawOffset = 0;

  /**
   * Construct the ADIS16470 imu and reset default configurations. Publish the gyro to the SmartDashboard.
   */
  public ADIS16470Swerve()
  {
    imu = new ADIS16470_IMU();
    factoryDefault();
    SmartDashboard.putData(imu);
  }

  /**
   * Reset IMU to factory default.
   */
  @Override
  public void factoryDefault()
  {
    yawOffset = imu.getAngle() % 360;
  }

  /**
   * Clear sticky faults on IMU.
   */
  @Override
  public void clearStickyFaults()
  {
    // Do nothing.
  }

  /**
   * Set the yaw in degrees.
   *
   * @param yaw Yaw angle in degrees.
   */
  @Override
  public void setYaw(double yaw)
  {
    yawOffset = (yaw % 360) + (imu.getAngle() % 360);
  }

  /**
   * Fetch the yaw/pitch/roll from the IMU.
   *
   * @param yprArray Array which will be filled with {yaw, pitch, roll} in degrees.
   */
  @Override
  public void getYawPitchRoll(double[] yprArray)
  {
    yprArray[0] = (imu.getAngle() % 360) - yawOffset;
    yprArray[1] = imu.getXComplementaryAngle() % 360;
    yprArray[2] = imu.getYComplementaryAngle() % 360;
  }

    /**
   * Fetch the {@link Rotation3d} from the IMU. Robot relative.
   *
   * @return {@link Rotation3d} from the IMU.
   */
  public Rotation3d getRotation3d() 
  {
    return new Rotation3d(
            imu.getYComplementaryAngle(), imu.getXComplementaryAngle(), imu.getAngle())
        .minus(new Rotation3d(0, 0, Math.toRadians(yawOffset)));
  }

  /**
   * Fetch the acceleration [x, y, z] from the IMU in meters per second squared. If
   * acceleration isn't supported returns empty.
   *
   * @return {@link Translation3d} of the acceleration as an {@link Optional}.
   */
  @Override
  public Optional<Translation3d> getAccel() 
  {
    return Optional.of(new Translation3d(imu.getAccelX(), imu.getAccelY(), imu.getAccelZ()));
  }

  /**
   * Fetch the acceleration [x, y, z] from the IMU.
   * 
   * @param accel Array which will be filled with {x, y, z} in m/s/s.
   */
  @Override
  public void getAccel(Double[] accel) {
      accel[0] = imu.getAccelX() / 9.81;
      accel[1] = imu.getAccelY() / 9.81;
      accel[2] = imu.getAccelZ() / 9.81;
  }

  /**
   * Get the instantiated IMU object.
   *
   * @return IMU object.
   */
  @Override
  public Object getIMU()
  {
    return imu;
  }
}
