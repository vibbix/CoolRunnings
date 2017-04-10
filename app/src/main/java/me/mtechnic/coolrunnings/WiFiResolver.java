package me.mtechnic.coolrunnings;

import android.net.wifi.ScanResult;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

/**
 * Resolves WiFi signals to a 2D coordinate plane
 */
public class WiFiResolver {
    //{bakebox, CoolRunnings, ddwrt{
    private double[][] positions = {{15.0, 0.0}, {30, 22.5}, {45.0, 15.0}};
    private double[] distances = new double[]{8.06, 13.97, 23.32};
    private double scalar = 1.0;

    private static double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public void setPositions(int index, double[] coords) {
        this.positions[index] = coords;
    }

    public void setDistances(ScanResult sr) {
        double distance = scalar * calculateDistance(sr.level, sr.frequency);
        if (sr.BSSID.toLowerCase().equals(AccessPoints.BakeBox.getMACAddress())) {
            distances[0] = distance;
        } else if (sr.BSSID.toLowerCase().equals(AccessPoints.CoolRunnings.getMACAddress())) {
            distances[1] = distance;
        } else if ((sr.BSSID.toLowerCase().equals(AccessPoints.DDWRT.getMACAddress()))) {
            distances[2] = distance;
        }
    }

    public void setScalar(double scalar) {
        this.scalar = scalar;
    }

    public double[] getCoordinate() {
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();
        return optimum.getPoint().toArray();
    }

    enum AccessPoints {
        BakeBox("00:22:75:d6:8e:54"),
        CoolRunnings("b4:75:0e:e0:e4:3f"),
        DDWRT("00:1c:10:0a:f4:f0");
        private String macaddress;

        AccessPoints(String macaddress) {
            this.macaddress = macaddress;
        }

        public String getMACAddress() {
            return this.macaddress;
        }
    }
}
