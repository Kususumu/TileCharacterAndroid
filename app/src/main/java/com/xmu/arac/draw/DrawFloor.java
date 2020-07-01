package com.xmu.arac.draw;

import com.xmu.arac.CARenderer;
import com.xmu.arac.shaderProgram.ColorShaderProgram;

import java.util.Vector;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class DrawFloor {
    public static boolean forceUpdate;
    //Every Center has 6 vertex and 18 values total
    private static final int VALUES_PER_CENTER = 18;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    private int startVertices;
    private int numVertices;

    private int lastCenterNumber;

    //save the Vertex of Model
    private float[] vertex;

    private float zDeep = -1000;
    private float yDeep = CARenderer.pictureHeight/2-270;

    //VertexArray
    private VertexArrayFloor vertexArray = new VertexArrayFloor();

    public DrawFloor(){
        forceUpdate = false;
        startVertices = 0;
        numVertices = 0;
        lastCenterNumber = 0;
    }

    //Draw the Model By Triangles,return the vertex
    //The input of the updateTrianglesVertex is Center Group of Floor
    public boolean updateTrianglesVertex(Vector p,double lengthOfEachFloor,double widthOfEachFloor,
                                         float yDeep){

        //Push Number of Vertex
        //Each Vector Element has a Vertex2D which contain the information of graphic
        numVertices = p.size()* VALUES_PER_CENTER/POSITION_COMPONENT_COUNT;

        //calculate the vertex size
        if(lastCenterNumber!=p.size()||forceUpdate){
            vertex = new float[p.size()*VALUES_PER_CENTER];

            //Add things to vertexData
            //z of the floor is all zero(0)
            //x is the width of the screen
            //y is the height of the screen
            for(int i=0;i<p.size();i++){
                Vertex2D t = (Vertex2D) p.elementAt(i);
                //System.out.println("DrawFloor::Center:i"+i+" x:"+t.x);
                //System.out.println("DrawFloor::Center:i"+i+" y:"+t.y);
                //Test Rectangle of the Screen x,y
                /*vertex[i*VALUES_PER_CENTER] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+1] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+2] = zDeep;
                vertex[i*VALUES_PER_CENTER+3] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+4] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+5] = zDeep;
                vertex[i*VALUES_PER_CENTER+6] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+7] = (float)(t.y + widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+8] = zDeep;
                vertex[i*VALUES_PER_CENTER+9] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+10] = (float)(t.y + widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+11] = zDeep;
                vertex[i*VALUES_PER_CENTER+12] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+13] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+14] = zDeep;
                vertex[i*VALUES_PER_CENTER+15] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+16] = (float)(t.y + widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+17] = zDeep;*/
                vertex[i*VALUES_PER_CENTER] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+1] = yDeep;
                vertex[i*VALUES_PER_CENTER+2] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+3] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+4] = yDeep;
                vertex[i*VALUES_PER_CENTER+5] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+6] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+7] = yDeep;
                vertex[i*VALUES_PER_CENTER+8] = (float)(t.y + widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+9] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+10] = yDeep;
                vertex[i*VALUES_PER_CENTER+11] = (float)(t.y + widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+12] = (float)(t.x + lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+13] = yDeep;
                vertex[i*VALUES_PER_CENTER+14] = (float)(t.y - widthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+15] = (float)(t.x - lengthOfEachFloor/2);
                vertex[i*VALUES_PER_CENTER+16] = yDeep;
                vertex[i*VALUES_PER_CENTER+17] = (float)(t.y + widthOfEachFloor/2);
            }

            //push Vertex
            vertexArray.pushDataToFloatBuffer(vertex);
        }

        //save the polyhedronNumber
        lastCenterNumber = p.size();

        return true;
    }

    public boolean bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);

        return true;
    }

    //draw Command of the Model
    public void draw(){
        glDrawArrays(GL_TRIANGLES,startVertices,numVertices);
    }
}
