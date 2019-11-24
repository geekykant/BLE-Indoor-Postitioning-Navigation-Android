package com.michaelfotiadis.ibeaconscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class GetAbsoluteLocation extends AppCompatActivity {
    TextView answer;
    ArrayList<BluetoothLeDevice> objects;

    class GFG {

        // This functions finds the determinant of Matrix
        double determinantOfMatrix(double mat[][]) {
            double ans;
            ans = mat[0][0] * (mat[1][1] * mat[2][2] - mat[2][1] * mat[1][2])
                    - mat[0][1] * (mat[1][0] * mat[2][2] - mat[1][2] * mat[2][0])
                    + mat[0][2] * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
            return ans;
        }

        // This function finds the solution of system of
// linear equations using cramer's rule
        void findSolution(double coeff[][]) {
            // Matrix d using coeff as given in cramer's rule
            double d[][] = {
                    {coeff[0][0], coeff[0][1], coeff[0][2]},
                    {coeff[1][0], coeff[1][1], coeff[1][2]},
                    {coeff[2][0], coeff[2][1], coeff[2][2]},
            };

            // Matrix d1 using coeff as given in cramer's rule
            double d1[][] = {
                    {coeff[0][3], coeff[0][1], coeff[0][2]},
                    {coeff[1][3], coeff[1][1], coeff[1][2]},
                    {coeff[2][3], coeff[2][1], coeff[2][2]},
            };

            // Matrix d2 using coeff as given in cramer's rule
            double d2[][] = {
                    {coeff[0][0], coeff[0][3], coeff[0][2]},
                    {coeff[1][0], coeff[1][3], coeff[1][2]},
                    {coeff[2][0], coeff[2][3], coeff[2][2]},
            };

            // Matrix d3 using coeff as given in cramer's rule
            double d3[][] = {
                    {coeff[0][0], coeff[0][1], coeff[0][3]},
                    {coeff[1][0], coeff[1][1], coeff[1][3]},
                    {coeff[2][0], coeff[2][1], coeff[2][3]},
            };

            // Calculating Determinant of Matrices d, d1, d2, d3
            double D = determinantOfMatrix(d);
            double D1 = determinantOfMatrix(d1);
            double D2 = determinantOfMatrix(d2);
            double D3 = determinantOfMatrix(d3);
            System.out.printf("D is : %.6f \n", D);
            System.out.printf("D1 is : %.6f \n", D1);
            System.out.printf("D2 is : %.6f \n", D2);
            System.out.printf("D3 is : %.6f \n", D3);

            // Case 1
            if (D != 0) {
                // Coeff have a unique solution. Apply Cramer's Rule
                double x = D1 / D;
                double y = D2 / D;
                double z = D3 / D; // calculating z using cramer's rule

                System.out.printf("Value of x is : %.6f\n", x);
                System.out.printf("Value of y is : %.6f\n", y);
                System.out.printf("Value of z is : %.6f\n", z);

                answer.setText(String.format("\nX: %.2f", x));
                answer.append(String.format("\nY: %.2f", y));
                answer.append("\nZ: 0");
            }

            // Case 2
            else {
                if (D1 == 0 && D2 == 0 && D3 == 0)
                    System.out.printf("Infinite solutions\n");
                else if (D1 != 0 || D2 != 0 || D3 != 0)
                    System.out.printf("No solutions\n");

                answer.setText("Location not found!");
            }
        }

        // Driver Code
        public void main(String[] args) {
            // storing coefficients of linear
            // equations in coeff matrix
            double coeff[][] = {{2, -1, 3, 9},
                    {1, 1, 1, 6},
                    {1, -1, 1, 2}};
            findSolution(coeff);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_get_absolute_location);

        TextView textView = (TextView) findViewById(R.id.bt_device_details);
        this.answer = (TextView) findViewById(R.id.answer);
        this.objects = (ArrayList) getIntent().getBundleExtra("BUNDLE").getSerializable("ARRAYLIST");
        ArrayList arrayList = new ArrayList();
        if (this.objects.size() != 3) {
            Toast.makeText(this, "3 becons required!", Toast.LENGTH_LONG).show();
            return;
        }

        Iterator it = this.objects.iterator();
        while (it.hasNext()) {
            BluetoothLeDevice bluetoothLeDevice = (BluetoothLeDevice) it.next();
            double calculateDistance = calculateDistance(bluetoothLeDevice.getRunningAverageRssi())/2;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            stringBuilder.append(bluetoothLeDevice.getAddress());
            stringBuilder.append(" Rssi: ");
            stringBuilder.append(bluetoothLeDevice.getRunningAverageRssi());
            stringBuilder.append(" - Distaince: ");
            stringBuilder.append(String.format("%.2f", new Object[]{Double.valueOf(calculateDistance)}));
            stringBuilder.append("m");
            textView.append(stringBuilder.toString());
            arrayList.add(Double.valueOf(calculateDistance));
        }

        double[][] r1 = new double[3][];
        r1[0] = new double[]{2.0d, 2.0d, 1.0d, ((Double) arrayList.get(0)).doubleValue()};
        r1[1] = new double[]{-2.0d, 2.0d, 1.0d, ((Double) arrayList.get(1)).doubleValue()};
        r1[2] = new double[]{2.0d, -2.0d, 1.0d, ((Double) arrayList.get(2)).doubleValue()};
        new GFG().findSolution(r1);
    }

    /* Access modifiers changed, original: 0000 */
    public double calculateDistance(double d) {
        return Math.pow(10.0d, (-69.0d - d) / 20.0d);
    }
}
