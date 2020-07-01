package com.xmu.arac.util;

import com.xmu.arac.draw.Ray;
import com.xmu.arac.draw.Vector3;
import com.xmu.arac.draw.Vertex3D;

import static android.opengl.Matrix.perspectiveM;

//Projection Helper is a class help user to standard projection matrix
//Use the projection matrix to get the right ray
public class ProjectionHelper {

    public static float[] floorProjectionMatrix = new float[16];
    public float aspect;
    float nearSurface;
    float farSurface;
    float theta;
    public double thetaD2 ;
    Vertex3D nearPoint;
    Vertex3D farPoint;

    public ProjectionHelper(){
        aspect = 1.5479f;
        nearSurface = 1;
        farSurface = 2000;
        theta = 61f;
        thetaD2 = Math.tan((theta/2/180*3.1415926));
        System.out.println("ProjectionHelper::thetaD2:" + thetaD2);
        initProjectionMatrix();
    }

    void initProjectionMatrix(){
        setProjectionMatrix();
    }

    void setProjectionMatrix(){
        perspectiveM(floorProjectionMatrix,0,theta,aspect,nearSurface,farSurface);

    }
    //We know that how the projection matrix do when we need to display something
    //so We can actually use the inverse method to get the original x,y,z of the
    //nX , nY is the putin ndc near plane coordinate
    public Ray getRayInWorldCoord(float nX,float nY){
        getNearsurfacePoint(nX,nY);
        getFarsurfacePoint(nX,nY);
        System.out.println("ProjectionHelper::nearPoint x:" + nearPoint.x
                + " y: " + nearPoint.y
                + " z :" + nearPoint.z);

        System.out.println("ProjectionHelper::farPoint x:" + farPoint.x
                + " y: " + farPoint.y
                + " z :" + farPoint.z);
        Vector3 t = new Vector3(farPoint.x - nearPoint.x,
                farPoint.y - nearPoint.y,nearPoint.z - farPoint.z);
        t.normalize();
        return new Ray(new Vertex3D(nearPoint),t);
    }

    //Ze is the deep surface that you want
    //Xn is the NDC coordinate that you get
    //n  is the near surface depth
    public double calculateZdeepX(double Ze,double Xn){
        double right = Ze * thetaD2 * aspect;
        double Xe = Ze * right * Xn / nearSurface;
        return Xe;
    }

    public double calculateZdeepY(double Ze,double Yn){
        double top = Ze * thetaD2;
        double Ye = Ze * top * Yn / nearSurface;
        return Ye;
    }

    //Input the NDC and get the world point of the Z depth
    public Vertex3D getNearsurfacePoint(double Xn,double Yn){
        return nearPoint = new Vertex3D(calculateZdeepX(nearSurface,Xn)
                ,calculateZdeepY(nearSurface,Yn),nearSurface);
    }

    public Vertex3D getFarsurfacePoint(double Xn,double Yn){
        return farPoint = new Vertex3D(calculateZdeepX(farSurface,Xn)
                ,calculateZdeepY(farSurface,Yn),farSurface);
    }
}
