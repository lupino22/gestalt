package mn.von.gestalt.zenphoton;

import mn.von.gestalt.zenphoton.dto.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HQZUtils {

    private static int absoluteRed = 635;
    private static int absoluteGreen = 520;
    private static int absoluteBlue = 465;
    private static float colorPower = 0.00045f;

    public static Scene initializeScene(long rays, int width, int height, float exposure, float gamma) {
        Scene scene = new Scene();
        Resolution reso = new Resolution();
        reso.setHeight(height);
        reso.setWidth(width);
        reso.toList();
        scene.setResolution(reso);
        Viewport viewport = new Viewport();
        viewport.setHeight(height);
        viewport.setWidth(width);
        viewport.setLeft(0); viewport.setTop(0);
        viewport.toList();
        scene.setViewport(viewport);
        scene.setRays(rays);
        scene.setExposure(exposure);
        scene.setGamma(gamma);
        return scene;
    }

    public static Material buildMaterial(float transmissive, float reflective, float diffuse) {
        Material mater = new Material();

        MaterialProperty transmissiveProperty = new MaterialProperty();
        transmissiveProperty.setType(MaterialProperty.MaterialPropertyType.Transmissive);
        transmissiveProperty.setWeigth(transmissive);
        transmissiveProperty.wrapToList();
        mater.addMaterialProperty(transmissiveProperty);

        MaterialProperty reflectiveProperty = new MaterialProperty();
        reflectiveProperty.setType(MaterialProperty.MaterialPropertyType.Reflective);
        reflectiveProperty.setWeigth(reflective);
        reflectiveProperty.wrapToList();
        mater.addMaterialProperty(reflectiveProperty);

        MaterialProperty diffuseProperty = new MaterialProperty();
        diffuseProperty.setType(MaterialProperty.MaterialPropertyType.Diffuse);
        diffuseProperty.setWeigth(diffuse);
        diffuseProperty.wrapToList();
        mater.addMaterialProperty(diffuseProperty);

        return mater;
    }

    public static ZObject buildObject(int materialIndex, int x0, int y0, int dx, int dy) {
        ZObject obj = new ZObject();
        obj.setMaterialIndex(materialIndex);
        obj.setX0(x0); obj.setY0(y0);
        obj.setDx(dx); obj.setDy(dy);
        obj.toList();
        return obj;
    }

    public static ZObject buildObject(int materialIndex, int x0, int y0, int dx, int dy, int a0, int da) {
        ZObject obj = new ZObject();
        obj.setMaterialIndex(materialIndex);
        obj.setX0(x0); obj.setY0(y0); obj.setA0(a0);
        obj.setDx(dx); obj.setDy(dy); obj.setDa(da);
        obj.toListExtended();
        return obj;
    }

    public static void buildRGBLight(MixedLight mixedLight, Color color, List<Integer> polarDist, ArrayList<Integer> polarAngle, ArrayList<Integer> rayAngle, int x, int y) {
        Light red = mixedLight.getRed();
        red.setPolarDistance(polarDist);
        red.setCartesianX(x); red.setCartesianY(y);
        red.setLightPower(color.getRed() * colorPower);
        red.setWaveLength(absoluteRed);
        red.setPolarAngle(polarAngle);
        red.setRayAngle(rayAngle);

        Light green = mixedLight.getGreen();
        green.setPolarDistance(polarDist);
        green.setCartesianX(x); green.setCartesianY(y);
        green.setLightPower(color.getGreen() * colorPower);
        green.setWaveLength(absoluteGreen);
        green.setPolarAngle(polarAngle);
        green.setRayAngle(rayAngle);

        Light blue = mixedLight.getBlue();
        blue.setPolarDistance(polarDist);
        blue.setCartesianX(x); blue.setCartesianY(y);
        blue.setLightPower(color.getBlue() * colorPower);
        blue.setWaveLength(absoluteBlue);
        blue.setPolarAngle(polarAngle);
        blue.setRayAngle(rayAngle);

        red.toList(); blue.toList(); green.toList();
    }

    public static void buildRGBLight(MixedLight mixedLight, Color color, List<Integer> polarDist, ArrayList<Integer> polarAngle, ArrayList<Integer> rayAngle, int x, int y, float power) {
        Light red = mixedLight.getRed();
        red.setPolarDistance(polarDist);
        red.setCartesianX(x); red.setCartesianY(y);
        red.setLightPower(color.getRed() * power);
        red.setWaveLength(absoluteRed);
        red.setPolarAngle(polarAngle);
        red.setRayAngle(rayAngle);

        Light green = mixedLight.getGreen();
        green.setPolarDistance(polarDist);
        green.setCartesianX(x); green.setCartesianY(y);
        green.setLightPower(color.getGreen() * power);
        green.setWaveLength(absoluteGreen);
        green.setPolarAngle(polarAngle);
        green.setRayAngle(rayAngle);

        Light blue = mixedLight.getBlue();
        blue.setPolarDistance(polarDist);
        blue.setCartesianX(x); blue.setCartesianY(y);
        blue.setLightPower(color.getBlue() * power);
        blue.setWaveLength(absoluteBlue);
        blue.setPolarAngle(polarAngle);
        blue.setRayAngle(rayAngle);

        red.toList(); blue.toList(); green.toList();
    }

    public static List<ZObject> buildCircle(int materialIndex, int x, int y, int radius) {
        List<ZObject> objects = new ArrayList<ZObject>();

        int lineCount = findOptimalLineCount(radius);
        double theta = Math.PI;
        double unitSpace = 2 * Math.PI / lineCount;

        int preX = -1;
        int preY = -1;

        for(int i = 0; i <= lineCount; i++, theta += unitSpace) {
            int postX = (int)(Math.cos(theta) * radius) + x;
            int postY = (int)(Math.sin(theta) * radius) + y;

            if(preX > 0 && preY > 0) {
                objects.add(buildObject(materialIndex, preX, preY, postX-preX, postY-preY));
            }

            preX = postX;
            preY = postY;
        }

        return objects;
    }

    private static int findOptimalLineCount(int radius) {
        double p = 2 * Math.PI * radius;
        return (int)(p/Math.log(radius));
    }

}
