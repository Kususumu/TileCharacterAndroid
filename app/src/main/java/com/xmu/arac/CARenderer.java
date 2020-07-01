package com.xmu.arac;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.xmu.arac.draw.DrawBackground;
import com.xmu.arac.draw.DrawFloor;
import com.xmu.arac.draw.DrawFloorLine;
import com.xmu.arac.draw.DrawManBackground;
import com.xmu.arac.draw.DrawTouchLine;
import com.xmu.arac.draw.DrawTouchPoint;
import com.xmu.arac.draw.Plane;
import com.xmu.arac.draw.Ray;
import com.xmu.arac.draw.RectangleGroup;
import com.xmu.arac.draw.Vector3;
import com.xmu.arac.draw.Vertex2D;
import com.xmu.arac.draw.Vertex3D;
import com.xmu.arac.shaderProgram.ColorShaderProgram;
import com.xmu.arac.shaderProgram.PointShaderProgram;
import com.xmu.arac.shaderProgram.TextureShaderProgram;
import com.xmu.arac.util.MatrixPlayer;
import com.xmu.arac.util.ProjectionHelper;
import com.xmu.arac.util.TextureHelper;

import java.util.Vector;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;

public class CARenderer implements Renderer {

    //Click Event
    static boolean clickEvent = false;
    Ray touchRay;
    float rotateAngle = 48;
    //float rotateAngle = 0;
    float modelYmove = -300;
    float modelZmove = -600;
    int[] pointInClick;
    Vector3 zNag = new Vector3(0,0,-1);
    Vertex3D clickPointNow;
    ProjectionHelper floorProjectionHelper = new ProjectionHelper();
    Vertex2D normalizePoint;

    //Floor Event
    float yDeep;
    double centerX = 0;
    double centerY = 0;
    public static float pictureWidth = 1390 * 1.20267f;
    public static float pictureHeight = 898 * 1.20267f;
    float aspect = 1.5479f;
    //float aspect = 2.3214f;
    private boolean rectanInitOrNot = false;
    int numberOfLength = 6;
    int numberOfWidth = 6;
    RectangleGroup RectanG;
    boolean floorEvent = true;
    DrawFloor drawFloor = new DrawFloor();
    DrawFloorLine drawFloorLine = new DrawFloorLine();
    int totalLength = 800;
    int totalWidth = 800;

    //Matrix Related MOV Matrix
    private static float[] projectionMatrix = new float[16];
    private static float[] floorProjectionMatrix = new float[16];
    private static float[] modelMatrix = new float[16];
    private static float[] viewMatrix = new float[16];
    private static float[] mvpMatrix = new float[16];
    private static float[] tempRotateMatrix = new float[16];
    private static float[] inverseRotateMatrix = new float[16];
    private static float[] inverseMpMatrix = new float[16];

    //Background Image:Image need to be improved
    public static boolean textureEvent = true;
    public static boolean textureManEvent = true;

    //Background Color
    public static float backR = 0.4f;
    public static float backG = 0.4f;
    public static float backB = 0.4f;

    //boolean debug = true;
    //Initial
    private final Context context;
    public static int screenHeight;
    public static int screenWidth;
    public static int orthoZ;

    //Some Program We Need
    private ColorShaderProgram colorProgram;
    private PointShaderProgram pointProgram;
    private TextureShaderProgram textureProgram;
    private int texture;
    private int textureMan;

    //EventSystem:
    //Draw Model with Touch Line
    static boolean touchLineEvent = true;
    boolean drawLineOrNot = false;
    DrawTouchLine drawTouchLine = new DrawTouchLine();
    //
    public static Vector touchLineStroke;
    //Draw Background
    private static DrawBackground drawBackground = new DrawBackground();
    //Draw Background Man
    private static DrawManBackground drawManBackground = new DrawManBackground();
    //Event System:TouchPoint
    private float pushX;
    private float pushY;
    private boolean touchPointEvent = true;
    private DrawTouchPoint drawTouchPoint = new DrawTouchPoint();
    private Vector<Vertex2D> touchPosition = new Vector<>();
    //save every time the touch point is what
    private float tX;
    private float tY;
    //stroke to save the draw point
    public static Vector<Vertex2D> stroke;

    //#One Finger Serials
    public void handleTouchPress(float sX,float sY) {
        if(touchLineEvent){
            tX = sX;
            tY = sY;
            Vertex2D p = new Vertex2D(tX,tY);
            drawLineOrNot = true;
            touchLineStroke = new Vector();
            touchLineStroke.addElement(p);
        }

        if(touchPointEvent){
            pushX = sX;
            pushY = sY;
            touchPosition = new Vector<Vertex2D>();
        }
    }

    //#Take Function when the finger move
    public void handleTouchMove(float dX,float  dY) {
        if(touchLineEvent&&drawLineOrNot){
            tX = dX;
            tY = dY;
            Vertex2D p = new Vertex2D(dX, dY);
            touchLineStroke.addElement(p);
        }

        if(touchPointEvent){
            tX = dX;
            tY = dY;
            Vertex2D p = new Vertex2D(tX,tY);

            //Debug
            //pushX = (float)drawTouchPoint.solveScreenCoordsToZeroOne(p).x;
            //pushY = (float)drawTouchPoint.solveScreenCoordsToZeroOne(p).y;
            pushX = dX;
            pushY = dY;
            //
        }
    }

    //#Take Function when the Finger is up
    void handleTouchUp(float dX,float dY){

        if(clickEvent){

            double nX = getMousePictureXnomarlize(dX);
            double nY = getMousePictureYnomarlize(dY);

            //touchRay = new Ray(new Vertex3D(nX ,nY ,0d),zNag);
            normalizePoint = new Vertex2D(nX,nY);

            //System.out.println("CARenderer::ClickMouse: dX"+ dX + " dY:"+ dY);
            System.out.println("CARenderer::ClickMouse: nX"+ nX + " nY:"+ nY);
            //System.out.println("CARenderer::OnClickEvent");


            RectanG.clickUpdate(getRayInterectPoint());

            DrawFloor.forceUpdate = true;
        }

        if(touchLineEvent&&drawLineOrNot){
            tX = dX;
            tY = dY;
            Vertex2D p = new Vertex2D(dX, dY);
            touchLineStroke.addElement(p);
        }

        if(touchPointEvent){
            tX = dX;
            tY = dY;
            Vertex2D p = new Vertex2D(tX,tY);

            pushX = dX;
            pushY = dY;
            //
        }
    }

    public CARenderer(Context context)
    {
        this.context = context;

        //Defind the ortho Projection Z value
        orthoZ = 2000;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused , EGLConfig config)
    {
        //BackGround Color
        glClearColor(backB,backG,backB,0.0f);

        colorProgram = new ColorShaderProgram(context);
        pointProgram = new PointShaderProgram(context);

        //TextureProgram used in Background
        textureProgram = new TextureShaderProgram(context);

        //#The Picture Name : Need to be improved here
        texture = TextureHelper.loadTexture(context,R.drawable.image1390left);
        textureMan = TextureHelper.loadTexture(context,R.drawable.manrender2340v2);

        setIdentityM(modelMatrix,0);
        glEnable(GLES20.GL_BLEND);
        glBlendFunc (GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused , int width,int height)
    {
        //Set the openGL viewport
        glViewport(0,0,width,height);
        screenHeight = height;
        screenWidth = width;
    }

    public void createFloor(){

    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        //MayBe the Color Change
        glClearColor(backR, backG, backB, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        setIdentityM(modelMatrix, 0);

        //setThe view port different to floor event
        glViewport(0,0,screenWidth,screenHeight);
        //fresh the projectionMatrix
        orthoM(projectionMatrix,0,0f,screenWidth,0f,screenHeight,-orthoZ,orthoZ);

        //Use to Draw the Background Image to make Animation
        if(textureEvent){
            textureProgram.useProgram();
            textureProgram.setUniforms(projectionMatrix,texture);
            drawBackground.bindData(textureProgram);
            drawBackground.draw();
        }

        if(textureManEvent){
            textureProgram.useProgram();
            textureProgram.setUniforms(projectionMatrix,textureMan);
            drawManBackground.bindData(textureProgram);
            drawManBackground.draw();
        }




        if(floorEvent){
            //Clear the Projection Matrix

            //yDeep = CARenderer.pictureHeight/2-285 ;

            glViewport(0,0,(int)pictureWidth,(int)pictureHeight);
            viewMatrix = new float[16];
            setIdentityM(viewMatrix,0);
            //setIdentityM(modelMatrix,0);
            //setLookAtM(viewMatrix,0,pictureWidth/2,pictureHeight/2,0,
            //        pictureWidth/2,pictureHeight/2,-1
            //       ,0,1,0);

            //perspectiveM(floorProjectionMatrix,0,34,aspect,10f,2000);

            //Original
            //translateM(modelMatrix,0,pictureWidth/2, yDeep,-950);
            //Lookat Rectan Test
            //translateM(modelMatrix,0,pictureWidth/2, yDeep,-1200);
            setIdentityM(modelMatrix,0);
            translateM(modelMatrix,0,0,modelYmove ,modelZmove);

            //First do the rotate this rotate matrix use to control the angle of the floor
            rotateM(modelMatrix,0,rotateAngle,0,1,0);
            //setIdentityM(modelMatrix,0);

            //for(int i = 0; i< 16;i++){
            //    System.out.println("CARenderer:modelMatrix"+i+" :"+modelMatrix[i]);
            //}
            multiplyMM(mvpMatrix,0,viewMatrix,0,modelMatrix,0);
            multiplyMM(mvpMatrix,0,floorProjectionHelper.floorProjectionMatrix,0,mvpMatrix,0);

            if(rectanInitOrNot){

                //Draw the Green Floor
                glLineWidth(1);
                colorProgram.useProgram();
                colorProgram.setUniforms(mvpMatrix,0.4f,0.4f,0.0f,0.6f);
                //colorProgram.setUniforms(projectionMatrix,0.4f,0.4f,0.0f);
                drawFloor.updateTrianglesVertex(RectanG.showGreenPoint,RectanG.getLengthOfEach(),
                        RectanG.getWidthOfEach(),0);

                drawFloor.bindData(colorProgram);
                drawFloor.draw();

                //Draw the Red Floor
                glLineWidth(1);
                colorProgram.useProgram();
                colorProgram.setUniforms(mvpMatrix,1.0f,0.1f,0.1f,0.6f);
                //colorProgram.setUniforms(projectionMatrix,0.4f,0.4f,0.0f);
                drawFloor.updateTrianglesVertex(RectanG.showRedPoint,RectanG.getLengthOfEach(),
                        RectanG.getWidthOfEach(),0);

                drawFloor.bindData(colorProgram);
                drawFloor.draw();

                //Draw the Walk Floor
                glLineWidth(1);
                colorProgram.useProgram();
                colorProgram.setUniforms(mvpMatrix,0.9f,0.9f,0.0f,0.6f);
                //colorProgram.setUniforms(projectionMatrix,0.4f,0.4f,0.0f);
                drawFloor.updateTrianglesVertex(RectanG.showOrangePoint,RectanG.getLengthOfEach(),
                        RectanG.getWidthOfEach(),0);

                drawFloor.bindData(colorProgram);
                drawFloor.draw();

                //Draw the Floor Line
                glLineWidth(5);
                colorProgram.useProgram();
                colorProgram.setUniforms(mvpMatrix,0.0f,0.0f,0.0f,1.0f);
                //colorProgram.setUniforms(projectionMatrix,0.4f,0.4f,0.0f);
                drawFloorLine.updateLinesVertex(RectanG.groupPointCenter,RectanG.getLengthOfEach(),
                        RectanG.getWidthOfEach(),0);

                drawFloorLine.bindData(colorProgram);
                drawFloorLine.draw();

            }
        }

        glViewport(0,0,screenWidth,screenHeight);
        //fresh the projectionMatrix
        setIdentityM(projectionMatrix, 0);
        orthoM(projectionMatrix,0,0f,screenWidth,0f,screenHeight,-orthoZ,orthoZ);

        //Use to Draw the Line , help the User to catch the point they want
        if(touchLineEvent&&touchLineStroke!=null){
            colorProgram.useProgram();
            colorProgram.setUniforms(projectionMatrix,1.0f,0.4f,0.0f,1.0f);
            drawTouchLine.updateTouchLine(touchLineStroke);
            drawTouchLine.bindData(colorProgram);
            drawTouchLine.draw();
        }

        //Last
        //Use to Draw the Touch Point
        if(touchPointEvent){
            //Identity projectMatrix for touchPoint Event
            orthoM(projectionMatrix,0,0f,screenWidth,0,screenHeight,-orthoZ,orthoZ);

            //System.out.println("The touch Event Debuggings");
            //put in Matrix and Color
            //use program first
            pointProgram.useProgram();
            pointProgram.setUniforms(projectionMatrix,1.0f,0.4f,0.0f);

            drawTouchPoint.pushData(pushX);
            drawTouchPoint.pushData(pushY);
            drawTouchPoint.pushData(orthoZ);

            drawTouchPoint.bindData(pointProgram);

            glDrawArrays(GL_POINTS,0,1);
        }
    }

    double getMousePictureXnomarlize(double x){
        double nX = (x/pictureWidth) * 2 - 1;
        return nX;
    }
    double getMousePictureYnomarlize(double y){
        //System.out.println("CARenderer:getMousedY:"+y);
        double nY =  ((y /pictureHeight) * 2 - 1);
        //System.out.println("CARenderer:getMousenY:"+nY);
        return nY;
    }

    Ray getRotateInverseRay(){
        setIdentityM(tempRotateMatrix,0);
        rotateM(tempRotateMatrix,0,rotateAngle,0,1,0);
        touchRay.base = MatrixPlayer.getInverseVertex(
                MatrixPlayer.setInverseRotateMartix(tempRotateMatrix),touchRay.base);
        touchRay.direction = MatrixPlayer.getInverseVector(
                MatrixPlayer.setInverseRotateMartix(tempRotateMatrix),touchRay.direction);
        return touchRay;
    }

    Vertex3D getRayInterectPoint(){
        //We do not use the inverse
        //This is really important
        touchRay = new Ray(new Vertex3D(0,0,0)
                ,new Vector3(
                        floorProjectionHelper.thetaD2 * floorProjectionHelper.aspect * normalizePoint.x
                ,floorProjectionHelper.thetaD2 * normalizePoint.y,-1));


        /*System.out.println("CARenderer::getRayInterectPoint:TouchRayDirectionBeforeNormalize: x:"
                +floorProjectionHelper.thetaD2 * floorProjectionHelper.aspect * normalizePoint.x
                + " y:"+ floorProjectionHelper.thetaD2 * normalizePoint.y
                + " z :"+ " -1 ");*/
        touchRay.direction.normalize();
  /*      System.out.println("CARenderer::getRayInterectPoint:TouchRayDirectionAfterNormalize: x:"+touchRay.direction.x
                + " y:"+ touchRay.direction.y+" z :"+touchRay.direction.z);*/
        touchRay.base.z = -modelZmove;
        touchRay.base.y += -modelYmove;

        getRotateInverseRay();

        //System.out.println("CARenderer::getRayInterectPoint:TouchRayBaseBefore: x:"+touchRay.base.x
        //        + " y:"+ touchRay.base.y+" z :"+touchRay.base.z);
        Plane p = new Plane(new Vertex3D(0,0,0),new Vector3(0,1,0));
        Vertex3D t = p.crossPoint(touchRay);
        if(t.y<0.0001)
            t.y = 0;

        System.out.println("CARenderer::getRayInterectPoint:CrossPoint: x:"+t.x+ " y:"+ t.y+" z :"+t.z);

        return t;
        //System.out.println("CARenderer::CheckClickEnd");
    }



    void showRectangle(){
        if(!rectanInitOrNot) {
            System.out.println("CARenderer::screenWidth:"+screenWidth);
            System.out.println("CARenderer::screenHeight:"+screenHeight);
            RectanG = new RectangleGroup(centerX, centerY
                    , totalLength, totalWidth, numberOfLength, numberOfWidth);

            //Then we can init it and get the point we want to show
            RectanG.init();
            System.out.println("CARenderer::Init Rectan Success");

            clickEvent = true;
            rectanInitOrNot = true;
        }else{
            clickEvent = false;
            rectanInitOrNot = false;
        }
    }
}
