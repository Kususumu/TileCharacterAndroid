package com.xmu.arac.draw;

import com.xmu.arac.shaderProgram.PointShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;

public class DrawTouchPoint {
    //A Line Has only Two 2D on surface
    private static final int POSITION_COMPONENT_COUNT = 3;

    private int startVertices = 0;

    //One Point is Only One
    private int numVertices = 1;

    //save the Vertex of Model
    private float[] vertex;

    //VertexArray
    //VertexArray() length 2*float
    private VertexArrayTouchPoint vertexArrayTP = new VertexArrayTouchPoint();

    private int lastLineNumber = 0;

    public void pushData(float t){
        vertexArrayTP.pushDataToFloatBuffer(t);
    }

    public void bindData(PointShaderProgram pointProgram){
        vertexArrayTP.setVertexAttribPointer(0,
                pointProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);

    }

    //draw Command of the Model
    public void draw(){
        //This still has problem
        glDrawArrays(GL_POINTS,startVertices,numVertices);
    }
}
