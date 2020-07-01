package com.xmu.arac.draw;

import com.xmu.arac.shaderProgram.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;

public class DrawManBackground {

    private boolean landScape = true;

    //1390 * 898 * 1.202
    public static float widthOfImage = 672f;
    public static float heightOfImage = 1080f;
    private static float pictureDeep = -1800;

    private VertexArrayBackground vertexArrayB;

    private int POSITION_COMPONENT_COUNT = 3;

    private int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    private int STRIDE = (POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    //X,Y,S,T

    private static float[] vertexDataLandScape = {
            2340,0.0f, -pictureDeep,0.0f,1.0f,//0,0
            2340-widthOfImage,heightOfImage,-pictureDeep,1.0f,0.0f,//1,1
            2340,heightOfImage,-pictureDeep,0.0f,0.0f,//0,1
            2340,0.0f,-pictureDeep,0.0f,1.0f,//0,0
            2340-widthOfImage,heightOfImage,-pictureDeep,1.0f,0.0f,//1,1
            2340-widthOfImage,0.0f,-pictureDeep,1.0f,1.0f//1,0
    };

    public DrawManBackground(){
        vertexArrayB = new VertexArrayBackground();
        if(landScape)
            vertexArrayB.pushDataToFloatBuffer(vertexDataLandScape);
    }

    public void bindData(TextureShaderProgram textureProgram){
        vertexArrayB.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );


        vertexArrayB.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLES,0,6);
    }
}
