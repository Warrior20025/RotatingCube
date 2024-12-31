import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 *
 * @author warriordescens
 */

class RotatingCube extends JPanel {
    static float A, B, C;
    static float cubeWidth = 20;
    static int width = 160, height = 44;
    static float[] zBuffer = new float[width * height];
    static char[] buffer = new char[width * height];
    static int backgroundASCIICode = ' ';
    static int distanceFromCam = 100;
    static float horizontalOffset;
    static float K1 = 40;
    static float incrementSpeed = 0.6f;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rotating Cube");
        RotatingCube rotatingCube = new RotatingCube();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(rotatingCube);
        frame.setVisible(true);

        new Timer(16, e -> {
            rotatingCube.updateFrame();
            rotatingCube.repaint();
        }).start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));

        int xOffset = 50;
        int yOffset = 50;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char ch = buffer[x + y * width];
                g2d.drawString(String.valueOf(ch), xOffset + x * 5, yOffset + y * 12);
            }
        }
    }

    public void updateFrame() {
        Arrays.fill(buffer, (char) backgroundASCIICode);
        Arrays.fill(zBuffer, 0);

        //ajustar tamaño aqui:
        cubeWidth = 25;
        horizontalOffset = 0;

        for (float cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += incrementSpeed) {
            for (float cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += incrementSpeed) {
                calculateForSurface(cubeX, cubeY, -cubeWidth, '@');
                calculateForSurface(cubeWidth, cubeY, cubeX, '$');
                calculateForSurface(-cubeWidth, cubeY, -cubeX, '~');
                calculateForSurface(-cubeX, cubeY, cubeWidth, '#');
                calculateForSurface(cubeX, -cubeWidth, -cubeY, ';');
                calculateForSurface(cubeX, cubeWidth, cubeY, '+');
            }
        }

        A += 0.05;
        B += 0.05;
        C += 0.02;
    }

    static void calculateForSurface(float cubeX, float cubeY, float cubeZ, char ch) {
        float x = calculateX(cubeX, cubeY, cubeZ);
        float y = calculateY(cubeX, cubeY, cubeZ);
        float z = calculateZ(cubeX, cubeY, cubeZ) + distanceFromCam;

        float ooz = 1 / z;

        int xp = (int) (width / 2 + horizontalOffset + K1 * ooz * x * 2);
        int yp = (int) (height / 2 + K1 * ooz * y);

        int idx = xp + yp * width;
        if (idx >= 0 && idx < width * height) {
            if (ooz > zBuffer[idx]) {
                zBuffer[idx] = ooz;
                buffer[idx] = ch;
            }
        }
    }

    static float calculateX(float i, float j, float k) {
        return (float) (j * Math.sin(A) * Math.sin(B) * Math.cos(C) - k * Math.cos(A) * Math.sin(B) * Math.cos(C) +
                        j * Math.cos(A) * Math.sin(C) + k * Math.sin(A) * Math.sin(C) + i * Math.cos(B) * Math.cos(C));
    }

    static float calculateY(float i, float j, float k) {
        return (float) (j * Math.cos(A) * Math.cos(C) + k * Math.sin(A) * Math.cos(C) -
                        j * Math.sin(A) * Math.sin(B) * Math.sin(C) + k * Math.cos(A) * Math.sin(B) * Math.sin(C) -
                        i * Math.cos(B) * Math.sin(C));
    }

    static float calculateZ(float i, float j, float k) {
        return (float) (k * Math.cos(A) * Math.cos(B) - j * Math.sin(A) * Math.cos(B) + i * Math.sin(B));
    }
}
