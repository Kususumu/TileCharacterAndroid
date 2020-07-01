package com.xmu.arac.shaderProgram;

import android.content.Context;

import com.xmu.arac.util.ShaderHelper;
import com.xmu.arac.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {

    //Uniform constants
    protected static final String U_VECTOR_TO_LIGHT = "u_VectorToLight";

    //Attribute constants
    protected static final String A_NORMAL = "a_Normal";

    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";

    //Uniform Color
    protected static final String U_COLOR= "u_Color";

    //Light Color
    protected static final String U_LIGHT_COLOR = "u_LightColor";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";

    //protected static final String M_MATRIX = "m_Matrix";

    //Texture Program
    protected static final String U_TEXTURE_UNIT ="u_TextureUnit";

    protected static final String A_TEXTURE_COORDINATES ="a_TextureCoordinates";

    //Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId){
        //Compile the shaders and Link the program
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(
                        context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(
                        context,fragmentShaderResourceId)
                );
    }

    public void useProgram(){
        //set the current OpenGL shader program to this program
        glUseProgram(program);
    }

}
