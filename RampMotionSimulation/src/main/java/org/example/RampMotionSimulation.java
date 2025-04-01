package org.example;

import java.util.ArrayList;

public class RampMotionSimulation {

    //gravitational acceleration (m/s^2)
    private static final double G = 9.81;

    public static void main(String[] args) {
        //simulation parameters and object properties
        double mass = 1.0;
        double radius = 3;
        //moment of inertia for a solid sphere: I = (2/5)*m*r^2
        double I = (2.0 / 5.0) * mass * radius * radius;

        //ramp from (0,20) to (20,0) is at 45°, length L = 20*sqrt(2)
        double rampAngleDeg = 45.0;
        double rampAngleRad = Math.toRadians(rampAngleDeg);
        double L = 20.0 * Math.sqrt(2.0);

        //acceleration along the ramp (rolling without slipping)
        double a = (G * Math.sin(rampAngleRad)) / (1.0 + I / (mass * radius * radius));

        //time to travel full ramp length L => t = sqrt(2L / a)
        double totalTime = Math.sqrt(2.0 * L / a);
        double dt = 0.01;

        //integrate motion along ramp
        //s: distance along ramp
        //theta: rotation angle
        //v: velocity along ramp
        //omega: angular velocity
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<Double> sList = new ArrayList<>();
        ArrayList<Double> thetaList = new ArrayList<>();
        ArrayList<Double> potentialEnergy = new ArrayList<>();
        ArrayList<Double> kineticEnergy = new ArrayList<>();
        ArrayList<Double> totalEnergy = new ArrayList<>();

        double s = 0.0;
        double v = 0.0;
        double theta = 0.0;
        double omega = 0.0;

        //negative sign => clockwise rotation
        double alphaRot = -a / radius;

        for (double t = 0.0; t <= totalTime; t += dt) {
            //map 1D distance s to 2D ramp coords
            double cx = (s / L) * 20.0;         // from 0 to 20
            double cy = 20.0 - (s / L) * 20.0;  // from 20 down to 0

            //potential energy (relative to y=0)
            double pe = mass * G * cy;
            //kinetic = translational + rotational
            double ke = 0.5 * mass * v * v + 0.5 * I * omega * omega;

            potentialEnergy.add(pe);
            kineticEnergy.add(ke);
            totalEnergy.add(pe + ke);

            //midpoint method
            double vMid = v + 0.5 * dt * a;
            double omegaMid = omega + 0.5 * dt * alphaRot;

            // 2) Update to next time
            v += dt * a;
            omega += dt * alphaRot;
            s += dt * vMid;
            theta += dt * omegaMid;

            // 3) THEN store the new state
            time.add(t);
            sList.add(s);
            thetaList.add(theta);
        }


        // Convert s, theta -> (x,y) for center-of-mass and sphere marker
        ArrayList<Double> mass_x = new ArrayList<>();
        ArrayList<Double> mass_y = new ArrayList<>();
        ArrayList<Double> circlePoint_x = new ArrayList<>();
        ArrayList<Double> circlePoint_y = new ArrayList<>();

        for (int i = 0; i < sList.size(); i++) {
            double s_val = sList.get(i);
            double theta_val = thetaList.get(i);

            //center-of-mass
            double cx = (s_val / L) * 20.0;
            double cy = 20.0 - (s_val / L) * 20.0;
            mass_x.add(cx);
            mass_y.add(cy);

            //a point on the sphere’s circumference
            double offsetX = radius * Math.cos(theta_val);
            double offsetY = radius * Math.sin(theta_val);

            //eotate offset by rampAngleRad
            double cosA = Math.cos(rampAngleRad);
            double sinA = Math.sin(rampAngleRad);

            double marker_x = cx + offsetX * cosA - offsetY * sinA;
            double marker_y = cy + offsetX * sinA + offsetY * cosA;

            circlePoint_x.add(marker_x);
            circlePoint_y.add(marker_y);
        }
        //prepare the energy data
        ArrayList<Double> PotEnergy_x = time;
        ArrayList<Double> PotEnergy_y = potentialEnergy;
        ArrayList<Double> KinEnergy_x = time;
        ArrayList<Double> KinEnergy_y = kineticEnergy;
        ArrayList<Double> totalEnergy_x = time;
        ArrayList<Double> totalEnergy_y = totalEnergy;

        //GUI (two separate charts)
        new Gui(mass_x, mass_y, circlePoint_x, circlePoint_y,
                PotEnergy_x, PotEnergy_y,
                KinEnergy_x, KinEnergy_y,
                totalEnergy_x, totalEnergy_y);
    }
}
