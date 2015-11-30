
import space.CargoShip;
import space.FighterShip;
import space.SpaceController;
import space.SpacePort;
import display.ViewManager;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import utils.ColorMaker;
import utils.Point3D;
import utils.SoundUtility;


public class Driver {

    public static void main(String[] args) {

        SoundUtility.getInstance();
        ViewManager.setup(SpaceController.xSize, SpaceController.ySize, SpaceController.zSize);

        ViewManager.getInstance();
        try {
            initTests();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        System.exit(0);

    }

    private static void initTests() throws Exception {

        FighterShip fs1 = null;
        CargoShip target = null;

        showMessage("Test 1: Create Stationary Cargo Ships (No Debris Clouds) and a  Fighter Ship.\n"
                + "Fighter will not fire at a ship of the same color.", "Test Launching");

        fs1 = new FighterShip("FighterShip1", "MAGENTA", new Point3D(300, 500, 800), new Point3D(700, 425, 600), 1000.0, 0.0, 150, 250.0);

        target = new CargoShip("CargoShip1", "GREEN", new Point3D(450, 450, 800), new Point3D(700, 450, 600), 500.0, 0.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }
        target = new CargoShip("CargoShip2", "RED", new Point3D(450, 500, 800), new Point3D(700, 500, 600), 1000.0, 0.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }
        target = new CargoShip("CargoShip3", "YELLOW", new Point3D(450, 550, 800), new Point3D(700, 550, 600), 1500.0, 0.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }
        target = new CargoShip("CargoShip4", "MAGENTA", new Point3D(450, 450, 800), new Point3D(700, 550, 600), 1500.0, 0.0, 0);

        Thread.sleep(3000);
        showMessage("Test 1A: Destroy Remaining Ships", "Destroy Fighter Ship");
        fs1.applyDamage(2000);
        target.applyDamage(2000);
        Thread.sleep(4000);

        /////////////////////////////
        showMessage("Test 2: Create Slow Moving Cargo Ships (No Debris Clouds) and a Stationary Fighter Ship - Test Missile Launch and Detonation", "Test Launching");

        fs1 = new FighterShip("FighterShip1", "TAN", new Point3D(300, 500, 800), new Point3D(700, 425, 600), 1000.0, 0.0, 150, 900.0);

        target = new CargoShip("CargoShip1", "SKYBLUE", new Point3D(350, 450, 800), new Point3D(900, 450, 600), 500.0, 2.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }
        target = new CargoShip("CargoShip2", "OLIVE", new Point3D(350, 500, 800), new Point3D(900, 500, 600), 1000.0, 2.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }
        target = new CargoShip("CargoShip3", "PINK", new Point3D(350, 550, 800), new Point3D(900, 550, 600), 1500.0, 2.0, 0);
        while (!target.isDestroyed()) {
            Thread.sleep(200);
        }

        Thread.sleep(1000);
        showMessage("Test 2A: Destroy Fighter Ship", "Destroy Fighter Ship");
        fs1.applyDamage(2000);
        Thread.sleep(4000);

        /////////////////////////////
        showMessage("Test 3: Create Moving Cargo Ships (No Debris Clouds) and\na Moving Fighter Ship - Test Missile Launch and Detonation", "Test Launching");

        target = new CargoShip("CargoShip1", "CYAN", new Point3D(400, 450, 800), new Point3D(900, 450, 600), 500.0, 7.0, 0);
        Thread.sleep(500);
        fs1 = new FighterShip("FighterShip1", "RED", new Point3D(300, 450, 800), new Point3D(900, 450, 600), 1000.0, 7.0, 150, 250.0);

        while (!target.isDestroyed()) {
            Thread.sleep(200);
            fs1.setDestination(new Point3D(target.getLocation().getX() + 50, target.getLocation().getY() + 50, target.getLocation().getZ()));
        }

        Thread.sleep(1000);
        showMessage("Test 3A: Destroy Fighter Ship", "Destroy Fighter Ship");
        fs1.applyDamage(2000);
        Thread.sleep(1000);

        /////////////////////////////
        showMessage("Test 4: Create Moving Cargo Ships (WITH Debris Clouds) and\na Moving Fighter Ship - Test Missile Launch and Detonation", "Test Launching");

        target = new CargoShip("CargoShip1", "OLIVE", new Point3D(400, 450, 800), new Point3D(900, 450, 600), 500.0, 7.0, 80);
        Thread.sleep(500);
        fs1 = new FighterShip("FighterShip1", "WHITE", new Point3D(300, 450, 800), new Point3D(900, 450, 600), 1000.0, 7.0, 150, 250.0);

        while (!target.isDestroyed()) {
            Thread.sleep(200);
            fs1.setDestination(new Point3D(target.getLocation().getX() + 50, target.getLocation().getY() + 50, target.getLocation().getZ()));
        }

        Thread.sleep(1000);
        showMessage("Test 4A: Destroy Fighter Ship", "Destroy Fighter Ship");
        fs1.applyDamage(2000);
        Thread.sleep(1000);

        /////////////////////////////
        showMessage("Test 5: Create 2 Figher Ships\n(They will be close enough to fire at each other)", "Create Fighters");
        FighterShip[] fss = new FighterShip[2];
        fss[0] = new FighterShip("FighterShip1", "TAN", new Point3D(25, 475, 800), new Point3D(800, 475, 600), 1000.0, 7.1, 150, 150.0);
        fss[1] = new FighterShip("FighterShip2", "CYAN", new Point3D(150, 575, 800), new Point3D(800, 550, 600), 1000.0, 7.0, 150, 150.0);

        Thread.sleep(7000);
        if (!fss[0].isDestroyed() || !fss[1].isDestroyed()) {
            showMessage("Test 5A: Destroy any Remaining Figher Ship(s)", "Destroy Ships");
            for (FighterShip f : fss) {
                if (!f.isDestroyed()) {
                    f.applyDamage(1000.0);
                }
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);

        /////////////////////////////
        showMessage("Test 6: Create a Yellow Space Port and Cargo Ship, and a Red Fighter Ship\n(Fighter will try to destroy the Yellow Ship and Port)", "Create Ships");
        SpacePort sp = new SpacePort("SpacePort3", "YELLOW", new Point3D(175, 175, 600), new Point3D(100, 700, 300), 1500.0, 0.025, 150);
        CargoShip cs = new CargoShip("CargoShip3", "YELLOW", new Point3D(670, 350, 800), sp.getLocation(), 500.0, 6.9, 150);
        FighterShip fs = new FighterShip("FighterShip3", "RED", new Point3D(780, 480, 800), sp.getLocation(), 1000.0, 7.0, 150, 150.0);

        while (!sp.isDestroyed() && !fs.isDestroyed()) {
            fs.setDestination(new Point3D(120, 120, 600));
            Thread.sleep(1000);

            fs.setDestination(new Point3D(120, 230, 600));
            Thread.sleep(1000);
        }

        while (!cs.isDestroyed() && 
        		!fs.isDestroyed()) {
            fs.setDestination(sp.getLocation());
            Thread.sleep(500);
        }

        if (!sp.isDestroyed() || !cs.isDestroyed() || 
        		!fs.isDestroyed()) {
            showMessage("Test 6A: Destroy any Remaining Ships/Ports", "Destroy Remainder");
            if (!sp.isDestroyed()) {
                sp.applyDamage(2000.0);
            }
            if (!cs.isDestroyed()) {
                cs.applyDamage(2000.0);
            }
            if (!fs.isDestroyed()) {
                fs.applyDamage(2000.0);
            }
            Thread.sleep(1000);
        }

        /////////////////////////////
        showMessage("Test 7: Create Multiple Forces. Simulation will continue for 60 seconds or until no Fighters remain", "Full Simulation");

        int sides = 0;
        do {
            sides = getNumber();
        } while (sides == 0);

        String[] colors = new String[sides];
        ArrayList<String> allColors = new ArrayList<String>(Arrays.asList(ColorMaker.getSupportedColors()));

        allColors.remove("GRAY");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sides; i++) {
            int pick = (int) (Math.random() * allColors.size());
            colors[i] = allColors.get(pick);
            allColors.remove(pick);
            sb.append(colors[i] + ", ");
        }

        showMessage("Using colors " + sb.toString() + sides + " colors", "Colors Selected");

        for (String color : colors) {
            
            int numPorts = 1 + (int) (Math.random() * 2);
            int numCargo = 3 + (int) (Math.random() * 3);
            int numFighter = 2 + (int) (Math.random() * 3);
            
            Point3D start = SpaceController.makePoint();
            for (int i = 0; i < numPorts; i++) {
                new SpacePort("SpacePort-" + color + i, color, SpaceController.makePointNear(start, 0.1), SpaceController.makePoint(), 1500.0, 0.025, 150);
            }
            for (int i = 0; i < numCargo; i++) {
                new CargoShip("CargoShip-" + color + i, color, start, SpaceController.makePoint(), 500.0, 6.9, 150);
            }
            for (int i = 0; i < numFighter; i++) {
                new FighterShip("FighterShip-" + color + i, color, start, SpaceController.makePoint(), 1000.0, 7.0, 150, 150.0);
            }
        }

        int ctr = 0;
        while (SpaceController.fightersRemain() && ctr < 60) {
            Thread.sleep(1000);
            ctr++;

            if (ctr >= 60) {
                int x = JOptionPane.showConfirmDialog(null, "Continue Simulation for Another 30 Seconds?", "Continue?", JOptionPane.YES_NO_OPTION);
                if (x == 0) {
                    ctr = 30;
                }

            }

        }
        if (ctr >= 60) {
            showMessage("Simulation Time Expired - Click OK to exit", "Simulation Comnplete");
        } else {
            showMessage("Simulation Complete (No Fighters Remain) - Click OK to exit", "Simulation Comnplete");
        }

    }

    public static int getNumber() {
        int sides = 0;
        String x = JOptionPane.showInputDialog(null, "Enter the Number of Colors to Use", "Number of Sides", JOptionPane.QUESTION_MESSAGE);
        try {
            sides = Integer.parseInt(x);
            if (sides <= 1 || sides > ColorMaker.getSupportedColors().length) {
                JOptionPane.showMessageDialog(null, "\"" + sides + "\" is an invalid number to use, please try again", "Invalid Number", JOptionPane.ERROR_MESSAGE);
                sides = 0;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "\"" + x + "\" is not a valid number, please try again", "Invalid Number", JOptionPane.ERROR_MESSAGE);
        }
        return sides;
    }

    public static void showMessage(String message, String title) {
        final JOptionPane pane = new JOptionPane(message);
        final JDialog d = pane.createDialog(null, title);
        d.setLocation(200, 40);
        d.setVisible(true);
    }
}


