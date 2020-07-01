package com.xmu.arac.draw;

import com.xmu.arac.shaderProgram.ColorShaderProgram;

import java.util.Vector;

import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glLineWidth;

public class DrawTouchLine {
    //A Line Has only Two 2D on surface
    private static final int POSITION_COMPONENT_COUNT = 3;

    private int startVertices = 0;

    //One Point is Only One
    private int numVertices = 1;

    //save the Vertex of Model
    private float[] vertex;

    //VertexArray
    //VertexArray() length 2*float
    private VertexArrayTouchLine vertexArrayTL = new VertexArrayTouchLine();

    private int lastLineNumber = 0;

    public void updateTouchLine(Vector v){
        numVertices= v.size();

        if(numVertices!=lastLineNumber){
            vertex = new float[numVertices * POSITION_COMPONENT_COUNT];
            //push the Vertex Data to vertex
            int offset = 0;
            for(int i = 0;i<numVertices;i++){
                Vertex2D v2d = (Vertex2D)v.get(i);

                vertex[offset++]=(float) v2d.x;
                vertex[offset++]=(float) v2d.y;
                //That is the nearest value
                vertex[offset++]= 2000f;
            }

            pushData(vertex);
        }

        //save the polyhedronNumber
        lastLineNumber = v.size();
    }

    public void pushData(float[] t){
        vertexArrayTL.pushDataToFloatBuffer(t);
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArrayTL.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);

    }

    //draw Command of the Model
    public void draw(){
        //This still has problem
        glLineWidth(10);
        glDrawArrays(GL_LINE_STRIP,startVertices,numVertices);
    }
}
